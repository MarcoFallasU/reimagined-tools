package com.marcofallasu.reimaginedtools.ability.builtin;

import com.marcofallasu.reimaginedtools.ability.Ability;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/** Aplica un efecto de poción al enemigo golpeado. */
public record EffectOnHit(Holder<MobEffect> effect, int seconds, int amplifier) implements Ability {
    @Override
    public Component description() {
        return Component.translatable("ability.reimaginedtools.effect_on_hit", effect.value().getDisplayName(), seconds);
    }

    @Override
    public void onHit(LivingEntity attacker, LivingEntity target, ItemStack stack, float damage) {
        target.addEffect(new MobEffectInstance(effect, seconds * 20, amplifier), attacker);
    }
}
