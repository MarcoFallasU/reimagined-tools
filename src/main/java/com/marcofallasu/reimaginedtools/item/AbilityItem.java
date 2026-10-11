package com.marcofallasu.reimaginedtools.item;

import com.marcofallasu.reimaginedtools.ability.Abilities;
import com.marcofallasu.reimaginedtools.ability.Ability;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Ítem que puede tener habilidades de clic derecho. Si no tiene ninguna asignada se comporta igual que un {@link Item}.
 * Las herramientas que ya tienen su propio clic derecho en vanilla (hacha, pala, azada, lanza) no lo usan.
 */
public class AbilityItem extends Item {
    public AbilityItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        List<Ability> active = Abilities.of(stack).stream().filter(Ability::isActive).toList();
        if (active.isEmpty()) {
            return super.use(level, player, hand);
        }
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResult.SUCCESS; // el cliente solo muestra el gesto; la lógica es del servidor
        }
        InteractionResult result = InteractionResult.PASS;
        for (Ability ability : active) {
            result = ability.onUse(serverPlayer, stack, hand);
            if (result.consumesAction()) {
                break;
            }
        }
        return result;
    }
}
