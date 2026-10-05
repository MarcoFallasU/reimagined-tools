package com.marcofallasu.reimaginedtools.ability;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Una habilidad que se le pega a un ítem (herramienta o pieza de armadura).
 * Cada método es un "momento" en el que la habilidad puede reaccionar; los que no uses se quedan vacíos.
 * Todos se llaman solo en el servidor.
 */
public interface Ability {
    /** Texto que aparece en la descripción (tooltip) del ítem. */
    Component description();

    /** El portador golpea cuerpo a cuerpo a {@code target} con el ítem en la mano principal. */
    default void onHit(LivingEntity attacker, LivingEntity target, ItemStack stack, float damage) {}

    /** El portador rompe un bloque con el ítem en la mano principal. */
    default void onBlockBreak(ServerPlayer player, ItemStack stack, BlockState state, BlockPos pos) {}

    /** El portador recibe daño llevando puesta esta pieza de armadura. */
    default void onWearerHurt(LivingEntity wearer, DamageSource source, ItemStack stack, float damage) {}

    /** Cada tick mientras el ítem está en la mano principal o en un slot de armadura. */
    default void onTick(LivingEntity holder, ItemStack stack, EquipmentSlot slot) {}
}
