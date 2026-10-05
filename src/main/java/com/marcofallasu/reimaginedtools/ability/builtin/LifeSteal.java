package com.marcofallasu.reimaginedtools.ability.builtin;

import com.marcofallasu.reimaginedtools.ability.Ability;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/** Cura al portador un porcentaje del daño que hace. */
public record LifeSteal(float fraction) implements Ability {
    @Override
    public Component description() {
        return Component.translatable("ability.reimaginedtools.life_steal", Math.round(fraction * 100));
    }

    @Override
    public void onHit(LivingEntity attacker, LivingEntity target, ItemStack stack, float damage) {
        attacker.heal(damage * fraction);
    }
}
