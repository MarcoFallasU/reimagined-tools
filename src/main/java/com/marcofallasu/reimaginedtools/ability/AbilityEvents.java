package com.marcofallasu.reimaginedtools.ability;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraft.ChatFormatting;

/**
 * El "cartero": escucha los eventos del juego (golpes, bloques rotos, ticks) y avisa a las habilidades
 * del ítem correspondiente. Las habilidades en sí no saben nada de Forge.
 */
public final class AbilityEvents {
    private AbilityEvents() {}

    private static final EquipmentSlot[] ARMOR = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    public static void register() {
        LivingHurtEvent.BUS.addListener(AbilityEvents::onHurt);
        BlockEvent.BreakEvent.BUS.addListener(AbilityEvents::onBreak);
        TickEvent.PlayerTickEvent.Post.BUS.addListener(AbilityEvents::onPlayerTick);
        ItemTooltipEvent.BUS.addListener(AbilityEvents::onTooltip);
    }

    /** Muestra las habilidades del ítem en su descripción. */
    private static void onTooltip(ItemTooltipEvent event) {
        for (Ability ability : Abilities.of(event.getItemStack())) {
            event.getToolTip().add(ability.description().copy().withStyle(ChatFormatting.GOLD));
        }
    }

    private static boolean onHurt(LivingHurtEvent event) {
        LivingEntity victim = event.getEntity();
        if (victim.level().isClientSide()) {
            return false;
        }
        var source = event.getSource();

        // Habilidades de la herramienta del atacante (solo golpes cuerpo a cuerpo directos)
        if (source.getEntity() instanceof LivingEntity attacker && source.getDirectEntity() == attacker && !source.is(DamageTypes.THORNS)) {
            ItemStack weapon = attacker.getMainHandItem();
            for (Ability ability : Abilities.of(weapon)) {
                ability.onHit(attacker, victim, weapon, event.getAmount());
            }
        }

        // Habilidades de la armadura de quien recibe el daño
        for (EquipmentSlot slot : ARMOR) {
            ItemStack piece = victim.getItemBySlot(slot);
            for (Ability ability : Abilities.of(piece)) {
                ability.onWearerHurt(victim, source, piece, event.getAmount());
            }
        }
        return false; // no cancelar el evento
    }

    private static boolean onBreak(BlockEvent.BreakEvent event) {
        if (event.getPlayer() instanceof ServerPlayer player) {
            ItemStack tool = player.getMainHandItem();
            for (Ability ability : Abilities.of(tool)) {
                ability.onBlockBreak(player, tool, event.getState(), event.getPos());
            }
        }
        return false;
    }

    private static void onPlayerTick(TickEvent.PlayerTickEvent.Post event) {
        Player player = event.player();
        if (player.level().isClientSide()) {
            return;
        }
        tick(player, EquipmentSlot.MAINHAND);
        for (EquipmentSlot slot : ARMOR) {
            tick(player, slot);
        }
    }

    private static void tick(Player player, EquipmentSlot slot) {
        ItemStack stack = player.getItemBySlot(slot);
        for (Ability ability : Abilities.of(stack)) {
            ability.onTick(player, stack, slot);
        }
    }
}
