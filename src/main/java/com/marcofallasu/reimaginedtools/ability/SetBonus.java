package com.marcofallasu.reimaginedtools.ability;

import java.util.Collection;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;

/** Envuelve una habilidad para que solo funcione si el portador lleva TODAS las piezas del set. */
public record SetBonus(Set<Identifier> pieces, Ability inner) implements Ability {
    public static SetBonus of(Collection<? extends RegistryObject<?>> pieces, Ability inner) {
        return new SetBonus(pieces.stream().map(RegistryObject::getId).collect(java.util.stream.Collectors.toUnmodifiableSet()), inner);
    }

    private boolean wearsAll(LivingEntity entity) {
        int worn = 0;
        for (EquipmentSlot slot : new EquipmentSlot[] {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
            ItemStack stack = entity.getItemBySlot(slot);
            if (!stack.isEmpty() && pieces.contains(BuiltInRegistries.ITEM.getKey(stack.getItem()))) {
                worn++;
            }
        }
        return worn == pieces.size();
    }

    @Override
    public Component description() {
        return Component.translatable("ability.reimaginedtools.set_bonus", inner.description());
    }

    @Override
    public void onHit(LivingEntity attacker, LivingEntity target, ItemStack stack, float damage) {
        if (wearsAll(attacker)) inner.onHit(attacker, target, stack, damage);
    }

    @Override
    public void onBlockBreak(ServerPlayer player, ItemStack stack, BlockState state, BlockPos pos) {
        if (wearsAll(player)) inner.onBlockBreak(player, stack, state, pos);
    }

    @Override
    public void onWearerHurt(LivingEntity wearer, DamageSource source, ItemStack stack, float damage) {
        if (wearsAll(wearer)) inner.onWearerHurt(wearer, source, stack, damage);
    }

    @Override
    public void onTick(LivingEntity holder, ItemStack stack, EquipmentSlot slot) {
        if (wearsAll(holder)) inner.onTick(holder, stack, slot);
    }
}
