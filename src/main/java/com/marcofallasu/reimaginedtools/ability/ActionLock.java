package com.marcofallasu.reimaginedtools.ability;

import com.marcofallasu.reimaginedtools.ReimaginedTools;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;

/**
 * Bloquea al jugador "para que no pueda hacer nada" durante un delay: no se mueve, no salta, no ataca,
 * no rompe ni coloca bloques, no interactúa ni usa ítems. Se usa con {@link #lock} y {@link #unlock}.
 */
public final class ActionLock {
    private ActionLock() {}

    private static final Identifier MODIFIER_ID = Identifier.fromNamespaceAndPath(ReimaginedTools.MODID, "action_lock");
    private static final Set<UUID> LOCKED = new HashSet<>();

    public static boolean isLocked(Player player) {
        return LOCKED.contains(player.getUUID());
    }

    public static void lock(Player player) {
        LOCKED.add(player.getUUID());
        // Velocidad de movimiento a 0 y salto imposible. Son modificadores transitorios: no se guardan en disco.
        apply(player.getAttribute(Attributes.MOVEMENT_SPEED), new AttributeModifier(MODIFIER_ID, -1.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        apply(player.getAttribute(Attributes.JUMP_STRENGTH), new AttributeModifier(MODIFIER_ID, -1.0, AttributeModifier.Operation.ADD_VALUE));
    }

    public static void unlock(Player player) {
        LOCKED.remove(player.getUUID());
        remove(player.getAttribute(Attributes.MOVEMENT_SPEED));
        remove(player.getAttribute(Attributes.JUMP_STRENGTH));
    }

    /** Al cerrar el servidor no queda nadie bloqueado. */
    static void clear() {
        LOCKED.clear();
    }

    private static void apply(AttributeInstance attribute, AttributeModifier modifier) {
        if (attribute != null) {
            attribute.removeModifier(MODIFIER_ID);
            attribute.addTransientModifier(modifier);
        }
    }

    private static void remove(AttributeInstance attribute) {
        if (attribute != null) {
            attribute.removeModifier(MODIFIER_ID);
        }
    }

    public static void register() {
        PlayerInteractEvent.RightClickBlock.BUS.addListener(e -> { return isLocked(e.getEntity()); });
        PlayerInteractEvent.RightClickItem.BUS.addListener(e -> { return isLocked(e.getEntity()); });
        PlayerInteractEvent.EntityInteract.BUS.addListener(e -> { return isLocked(e.getEntity()); });
        PlayerInteractEvent.EntityInteractSpecific.BUS.addListener(e -> { return isLocked(e.getEntity()); });
        PlayerInteractEvent.LeftClickBlock.BUS.addListener(e -> { return isLocked(e.getEntity()); });
        AttackEntityEvent.BUS.addListener(e -> { return isLocked(e.getEntity()); });
        BlockEvent.BreakEvent.BUS.addListener(e -> { return isLocked(e.getPlayer()); });
        LivingEntityUseItemEvent.Start.BUS.addListener(e -> { return e.getEntity() instanceof Player p && isLocked(p); });
    }
}
