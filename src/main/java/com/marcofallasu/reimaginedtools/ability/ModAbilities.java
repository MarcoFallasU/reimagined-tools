package com.marcofallasu.reimaginedtools.ability;

import com.marcofallasu.reimaginedtools.ability.builtin.EffectOnHit;
import com.marcofallasu.reimaginedtools.ability.builtin.EffectWhileHeld;
import com.marcofallasu.reimaginedtools.ability.builtin.IgniteOnHit;
import com.marcofallasu.reimaginedtools.ability.builtin.LifeSteal;
import com.marcofallasu.reimaginedtools.ability.builtin.LightningDash;
import com.marcofallasu.reimaginedtools.ability.builtin.ResonantBond;
import com.marcofallasu.reimaginedtools.ability.builtin.TeleportToLookedBlock;
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

        // ---- Habilidades especiales (clic derecho en la espada; el pico, la guadaña y la sierra también podrían llevarlas) ----
        // Luz: teletransporte al bloque mirado (alcance 30, delay de 0.75 s bloqueado, enfriamiento 3 s)
        Abilities.attach(ModItems.LIGHT_TOOLS.get(ToolKind.SWORD), new TeleportToLookedBlock(30, 15, 60));
        // Electricidad: dash de 10 bloques en 5 ticks que daña (x1.5 del ataque) a lo que atraviesa, enfriamiento 4 s
        Abilities.attach(ModItems.ELECTRICITY_TOOLS.get(ToolKind.SWORD), new LightningDash(10, 5, 1.5F, 80));
        // Música: une a los jugadores a 12 bloques; a los 3 s les da Velocidad II y Fuerza I durante 15 s, enfriamiento 20 s
        Abilities.attach(ModItems.MUSIC_TOOLS.get(ToolKind.SWORD), new ResonantBond(12.0, 60, 15, 400));
        // Oscuridad: habilidad especial pendiente de definir (la idea es que dañe a quien la usa).
    }
}
