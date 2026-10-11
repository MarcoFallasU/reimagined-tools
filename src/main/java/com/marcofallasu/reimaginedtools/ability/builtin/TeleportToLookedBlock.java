package com.marcofallasu.reimaginedtools.ability.builtin;

import com.marcofallasu.reimaginedtools.ability.Abilities;
import com.marcofallasu.reimaginedtools.ability.AbilityTasks;
import com.marcofallasu.reimaginedtools.ability.Ability;
import com.marcofallasu.reimaginedtools.ability.ActionLock;
import java.util.Locale;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Luz: clic derecho para teletransportarte al bloque que estás mirando. Antes de teletransportarte hay un
 * pequeño delay durante el cual el jugador queda bloqueado (no puede hacer nada). El destino se fija al activar.
 */
public record TeleportToLookedBlock(int range, int delayTicks, int cooldownTicks) implements Ability {
    @Override
    public Component description() {
        return Component.translatable("ability.reimaginedtools.teleport", String.format(Locale.ROOT, "%.2f", delayTicks / 20.0), range);
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public InteractionResult onUse(ServerPlayer player, ItemStack stack, InteractionHand hand) {
        if (ActionLock.isLocked(player)) {
            return InteractionResult.FAIL;
        }
        ServerLevel level = player.level();
        Vec3 eye = player.getEyePosition();
        Vec3 end = eye.add(player.getViewVector(1.0F).scale(range));
        BlockHitResult hit = level.clip(new ClipContext(eye, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        if (hit.getType() == HitResult.Type.MISS) {
            player.displayClientMessage(Component.translatable("message.reimaginedtools.teleport.no_target", range), true);
            return InteractionResult.FAIL;
        }
        Vec3 destination = findStandingSpot(level, player, hit);
        if (destination == null) {
            player.displayClientMessage(Component.translatable("message.reimaginedtools.teleport.no_space"), true);
            return InteractionResult.FAIL;
        }

        player.getCooldowns().addCooldown(stack, cooldownTicks);
        stack.hurtAndBreak(1, player, (hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND));
        ActionLock.lock(player);
        level.playSound(null, player.blockPosition(), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 0.8F, 1.6F);

        AbilityTasks.run(delayTicks,
            elapsed -> {
                // carga: partículas de luz que suben alrededor del jugador, y destino marcado
                double angle = elapsed * 0.9;
                level.sendParticles(ParticleTypes.END_ROD, player.getX() + Math.cos(angle) * 0.7, player.getY() + 0.1 + elapsed * 0.1, player.getZ() + Math.sin(angle) * 0.7, 1, 0, 0.02, 0, 0.0);
                level.sendParticles(ParticleTypes.END_ROD, destination.x, destination.y + 0.1, destination.z, 1, 0.25, 0.1, 0.25, 0.01);
            },
            () -> finish(level, player, destination),
            () -> ActionLock.unlock(player));
        return InteractionResult.SUCCESS;
    }

    private static void finish(ServerLevel level, ServerPlayer player, Vec3 destination) {
        ActionLock.unlock(player);
        if (!player.isAlive() || player.isRemoved() || player.level() != level) {
            return;
        }
        level.sendParticles(ParticleTypes.END_ROD, player.getX(), player.getY() + 1.0, player.getZ(), 25, 0.3, 0.6, 0.3, 0.08);
        player.teleportTo(level, destination.x, destination.y, destination.z, Set.of(), player.getYRot(), player.getXRot(), false);
        player.fallDistance = 0.0F;
        level.sendParticles(ParticleTypes.END_ROD, destination.x, destination.y + 1.0, destination.z, 25, 0.3, 0.6, 0.3, 0.08);
        level.playSound(null, player.blockPosition(), SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.3F);
    }

    /** Busca dónde caben los pies del jugador junto al bloque mirado: encima, al lado o debajo según la cara. */
    private static Vec3 findStandingSpot(ServerLevel level, LivingEntity player, BlockHitResult hit) {
        BlockPos pos = hit.getBlockPos();
        Direction face = hit.getDirection();
        double x = pos.getX() + 0.5, z = pos.getZ() + 0.5;
        double baseY = switch (face) {
            case UP -> pos.getY() + 1.0;
            case DOWN -> pos.getY() - player.getBbHeight();
            default -> pos.relative(face).getY();
        };
        if (face.getAxis().isHorizontal()) {
            x = pos.relative(face).getX() + 0.5;
            z = pos.relative(face).getZ() + 0.5;
        }
        for (int up = 0; up <= 1; up++) {
            Vec3 candidate = new Vec3(x, baseY + up, z);
            AABB box = player.getBoundingBox().move(candidate.subtract(player.position()));
            if (level.noCollision(player, box)) {
                return candidate;
            }
        }
        return null;
    }
}
