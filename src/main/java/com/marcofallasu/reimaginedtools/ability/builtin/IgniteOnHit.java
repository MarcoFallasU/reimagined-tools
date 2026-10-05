package com.marcofallasu.reimaginedtools.ability.builtin;

import com.marcofallasu.reimaginedtools.ability.Ability;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/** Prende fuego al enemigo golpeado. */
public record IgniteOnHit(int seconds) implements Ability {
    @Override
    public Component description() {
        return Component.translatable("ability.reimaginedtools.ignite", seconds);
    }

    @Override
    public void onHit(LivingEntity attacker, LivingEntity target, ItemStack stack, float damage) {
        target.igniteForSeconds(seconds);
    }
}
