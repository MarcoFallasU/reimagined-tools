package com.marcofallasu.reimaginedtools.ability;

import com.marcofallasu.reimaginedtools.ability.builtin.EffectOnHit;
import com.marcofallasu.reimaginedtools.ability.builtin.EffectWhileHeld;
import com.marcofallasu.reimaginedtools.ability.builtin.IgniteOnHit;
import com.marcofallasu.reimaginedtools.ability.builtin.LifeSteal;
import com.marcofallasu.reimaginedtools.ability.builtin.ReflectDamage;
import com.marcofallasu.reimaginedtools.item.ModItems;
import com.marcofallasu.reimaginedtools.item.ToolKind;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.equipment.ArmorType;

/**
 * Aquí se decide QUÉ habilidades tiene cada ítem. Para darle una habilidad a algo:
 * {@code Abilities.attach(<ítem>, new MiHabilidad(...))}. Para crear una habilidad nueva, mira {@link Ability}.
 */
public final class ModAbilities {
    private ModAbilities() {}

    public static void register() {
        // ---- Herramientas de rubí ----
        Abilities.attach(ModItems.RUBY_TOOLS.get(ToolKind.SWORD), new IgniteOnHit(4), new LifeSteal(0.10F));
        Abilities.attach(ModItems.RUBY_TOOLS.get(ToolKind.SPEAR), new EffectOnHit(MobEffects.SLOWNESS, 3, 1));
        Abilities.attach(ModItems.RUBY_TOOLS.get(ToolKind.PICKAXE), new EffectWhileHeld(MobEffects.HASTE, 0));
        Abilities.attach(ModItems.RUBY_TOOLS.get(ToolKind.SCYTHE), new EffectWhileHeld(MobEffects.SPEED, 0));

        // ---- Armadura de rubí: habilidades por pieza ----
        Abilities.attach(ModItems.RUBY_ARMOR.get(ArmorType.HELMET), new EffectWhileHeld(MobEffects.WATER_BREATHING, 0));
        Abilities.attach(ModItems.RUBY_ARMOR.get(ArmorType.CHESTPLATE), new ReflectDamage(0.20F));
        Abilities.attach(ModItems.RUBY_ARMOR.get(ArmorType.BOOTS), new EffectWhileHeld(MobEffects.JUMP_BOOST, 0));

        // ---- Bonus de set completo (se anota en el casco, pero exige las 4 piezas puestas) ----
        Abilities.attach(ModItems.RUBY_ARMOR.get(ArmorType.HELMET),
            SetBonus.of(ModItems.RUBY_ARMOR.values(), new EffectWhileHeld(MobEffects.FIRE_RESISTANCE, 0)));
    }
}
