package com.marcofallasu.reimaginedtools.ability.builtin;

import com.marcofallasu.reimaginedtools.ability.Ability;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/** Da un efecto mientras el ítem está en la mano principal (herramientas) o puesto (armadura). */
public record EffectWhileHeld(Holder<MobEffect> effect, int amplifier) implements Ability {
    @Override
    public Component description() {
        return Component.translatable("ability.reimaginedtools.effect_while_held", effect.value().getDisplayName());
    }

    @Override
    public void onTick(LivingEntity holder, ItemStack stack, EquipmentSlot slot) {
        // Se renueva cada medio segundo con 2 segundos de duración: desaparece solo al soltar/quitarse el ítem.
        if (holder.tickCount % 10 == 0) {
            holder.addEffect(new MobEffectInstance(effect, 40, amplifier, true, false, true));
        }
    }
}
