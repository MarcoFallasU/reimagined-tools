package com.marcofallasu.reimaginedtools.ability;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

/** Libreta que anota qué habilidades tiene cada ítem, por su nombre (ej. reimaginedtools:ruby_sword). */
public final class Abilities {
    private Abilities() {}

    private static final Map<Identifier, List<Ability>> BY_ITEM = new HashMap<>();

    public static void attach(RegistryObject<Item> item, Ability... abilities) {
        BY_ITEM.merge(item.getId(), List.of(abilities), (a, b) -> java.util.stream.Stream.concat(a.stream(), b.stream()).toList());
    }

    public static List<Ability> of(ItemStack stack) {
        if (stack.isEmpty()) {
            return List.of();
        }
        return BY_ITEM.getOrDefault(BuiltInRegistries.ITEM.getKey(stack.getItem()), List.of());
    }
}
