package com.marcofallasu.reimaginedtools.ability.builtin;

import com.marcofallasu.reimaginedtools.ability.Ability;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/** Devuelve al atacante un porcentaje del daño recibido. */
public record ReflectDamage(float fraction) implements Ability {
    @Override
    public Component description() {
        return Component.translatable("ability.reimaginedtools.reflect", Math.round(fraction * 100));
    }

    @Override
    public void onWearerHurt(LivingEntity wearer, DamageSource source, ItemStack stack, float damage) {
        if (source.is(DamageTypes.THORNS) || !(source.getEntity() instanceof LivingEntity attacker)) {
            return; // evita bucles infinitos de rebote entre dos portadores
        }
        if (wearer.level() instanceof ServerLevel level) {
            attacker.hurtServer(level, wearer.damageSources().thorns(wearer), damage * fraction);
        }
    }
}
