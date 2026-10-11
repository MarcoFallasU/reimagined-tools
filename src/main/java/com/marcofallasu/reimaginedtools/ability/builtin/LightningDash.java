package com.marcofallasu.reimaginedtools.ability.builtin;

import com.marcofallasu.reimaginedtools.ability.Ability;
import com.marcofallasu.reimaginedtools.ability.AbilityTasks;
import com.marcofallasu.reimaginedtools.ability.ActionLock;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
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
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * Electricidad: clic derecho para hacer un dash en la dirección en la que miras. Daña una sola vez a cada entidad
 * que atraviesa y se detiene antes de chocar con un bloque. El daño es el de ataque del jugador por un multiplicador.
 */
public record LightningDash(int distance, int ticks, float damageMultiplier, int cooldownTicks) implements Ability {
    @Override
    public Component description() {
        return Component.translatable("ability.reimaginedtools.dash", distance, String.format(Locale.ROOT, "%.1f", damageMultiplier));
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
        Vec3 direction = player.getViewVector(1.0F).normalize();
        double reach = freeDistance(level, player, direction);
        if (reach < 1.5) {
            player.displayClientMessage(Component.translatable("message.reimaginedtools.dash.no_space"), true);
            return InteractionResult.FAIL;
        }

        player.getCooldowns().addCooldown(stack, cooldownTicks);
        stack.hurtAndBreak(2, player, (hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND));
        level.playSound(null, player.blockPosition(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 0.7F, 1.8F);

        float damage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE) * damageMultiplier;
        Vec3 step = direction.scale(reach / ticks);
        Set<UUID> alreadyHit = new HashSet<>();

        AbilityTasks.run(ticks,
            elapsed -> {
                if (!player.isAlive() || player.isRemoved() || player.level() != level) {
                    return;
                }
                // todo lo que toca el cuerpo del jugador entre su posición actual y la siguiente
                AABB now = player.getBoundingBox();
                AABB sweep = now.minmax(now.move(step)).inflate(0.3);
                for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, sweep, e -> canHit(player, e, alreadyHit))) {
                    alreadyHit.add(target.getUUID());
                    target.hurtServer(level, level.damageSources().playerAttack(player), damage);
                    level.sendParticles(ParticleTypes.ELECTRIC_SPARK, target.getX(), target.getY() + target.getBbHeight() / 2, target.getZ(), 18, 0.3, 0.4, 0.3, 0.3);
                    level.playSound(null, target.blockPosition(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 0.5F, 2.0F);
                }
                for (int i = 0; i < 4; i++) {
                    Vec3 p = player.position().add(step.scale(i / 4.0)).add(0, 0.9, 0);
                    level.sendParticles(ParticleTypes.ELECTRIC_SPARK, p.x, p.y, p.z, 3, 0.2, 0.4, 0.2, 0.05);
                }
                Vec3 next = player.position().add(step);
                player.teleportTo(level, next.x, next.y, next.z, Set.of(), player.getYRot(), player.getXRot(), false);
                player.fallDistance = 0.0F;
            },
            () -> { },
            () -> { });
        return InteractionResult.SUCCESS;
    }

    private static boolean canHit(ServerPlayer player, LivingEntity e, Set<UUID> alreadyHit) {
        return e != player && e.isAlive() && !e.isSpectator() && !alreadyHit.contains(e.getUUID())
            && !(e instanceof TamableAnimal pet && pet.isOwnedBy(player));
    }

    /** Hasta dónde puede avanzar el cuerpo del jugador en esa dirección sin entrar en un bloque. */
    private double freeDistance(ServerLevel level, ServerPlayer player, Vec3 direction) {
        double free = 0.0;
        for (double d = 0.25; d <= distance + 1.0E-6; d += 0.25) {
            AABB box = player.getBoundingBox().move(direction.scale(d));
            if (!level.noCollision(player, box)) {
                break;
            }
            free = d;
        }
        return free;
    }
}
