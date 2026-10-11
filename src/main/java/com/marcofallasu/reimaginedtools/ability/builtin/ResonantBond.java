package com.marcofallasu.reimaginedtools.ability.builtin;

import com.marcofallasu.reimaginedtools.ability.Ability;
import com.marcofallasu.reimaginedtools.ability.AbilityTasks;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

/**
 * Música: clic derecho para unir a todos los jugadores cercanos (incluido quien la usa). Tras un delay, sin bloquear a
 * nadie, todos los unidos reciben Velocidad II y Fuerza I. Quien se desconecta o muere durante el delay no recibe nada.
 */
public record ResonantBond(double radius, int delayTicks, int effectSeconds, int cooldownTicks) implements Ability {
    @Override
    public Component description() {
        return Component.translatable("ability.reimaginedtools.bond", (int) radius, String.format(Locale.ROOT, "%.0f", delayTicks / 20.0), effectSeconds);
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public InteractionResult onUse(ServerPlayer player, ItemStack stack, InteractionHand hand) {
        ServerLevel level = player.level();
        List<UUID> linked = new ArrayList<>();
        for (ServerPlayer other : level.getEntitiesOfClass(ServerPlayer.class, player.getBoundingBox().inflate(radius), p -> p.isAlive() && !p.isSpectator())) {
            if (other.distanceToSqr(player) <= radius * radius) {
                linked.add(other.getUUID());
            }
        }
        player.getCooldowns().addCooldown(stack, cooldownTicks);
        stack.hurtAndBreak(1, player, (hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND));
        player.displayClientMessage(Component.translatable("message.reimaginedtools.bond.linked", linked.size()), true);
        level.playSound(null, player.blockPosition(), SoundEvents.NOTE_BLOCK_CHIME.value(), SoundSource.PLAYERS, 1.0F, 0.8F);

        AbilityTasks.run(delayTicks,
            elapsed -> {
                if (elapsed % 4 == 0) {
                    showBond(level, player, linked, elapsed);
                }
                if (elapsed % 15 == 0 && elapsed > 0) {
                    level.playSound(null, player.blockPosition(), SoundEvents.NOTE_BLOCK_CHIME.value(), SoundSource.PLAYERS, 0.8F, Math.min(2.0F, 0.9F + elapsed / 15 * 0.15F));
                }
            },
            () -> finish(level, player, linked),
            () -> { });
        return InteractionResult.SUCCESS;
    }

    /** Notas sobre cada jugador unido y una línea de notas desde quien activa la habilidad. */
    private void showBond(ServerLevel level, ServerPlayer caster, List<UUID> linked, int elapsed) {
        Vec3 from = caster.position().add(0, 1.2, 0);
        for (UUID id : linked) {
            ServerPlayer p = level.getServer().getPlayerList().getPlayer(id);
            if (p == null || !p.isAlive()) {
                continue;
            }
            double hue = ((elapsed / 4) % 24) / 24.0;
            level.sendParticles(ParticleTypes.NOTE, p.getX(), p.getY() + 2.2, p.getZ(), 0, hue, 0, 0, 1.0);
            if (p != caster) {
                Vec3 to = p.position().add(0, 1.2, 0);
                for (int i = 1; i < 6; i++) {
                    Vec3 point = from.lerp(to, i / 6.0);
                    level.sendParticles(ParticleTypes.NOTE, point.x, point.y, point.z, 0, hue, 0, 0, 1.0);
                }
            }
        }
    }

    private void finish(ServerLevel level, ServerPlayer caster, List<UUID> linked) {
        int affected = 0;
        for (UUID id : linked) {
            ServerPlayer p = level.getServer().getPlayerList().getPlayer(id);
            if (p == null || !p.isAlive()) {
                continue;
            }
            p.addEffect(new MobEffectInstance(MobEffects.SPEED, effectSeconds * 20, 1, false, true, true), caster);
            p.addEffect(new MobEffectInstance(MobEffects.STRENGTH, effectSeconds * 20, 0, false, true, true), caster);
            level.sendParticles(ParticleTypes.NOTE, p.getX(), p.getY() + 1.0, p.getZ(), 12, 0.5, 0.8, 0.5, 1.0);
            level.playSound(null, p.blockPosition(), SoundEvents.NOTE_BLOCK_BELL.value(), SoundSource.PLAYERS, 1.0F, 1.2F);
            affected++;
        }
        if (caster.isAlive()) {
            caster.displayClientMessage(Component.translatable("message.reimaginedtools.bond.done", affected), true);
        }
    }
}
