package com.marcofallasu.reimaginedtools.material;

import com.marcofallasu.reimaginedtools.ReimaginedTools;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class ModTags {
    private ModTags() {}

    /** Bloques que una herramienta de rubí NO puede minar con drops (ver data/.../tags/block). */
    public static final TagKey<Block> INCORRECT_FOR_RUBY_TOOL = block("incorrect_for_ruby_tool");

    /** Ítems que sirven para reparar herramientas de rubí en el yunque. */
    public static final TagKey<Item> RUBY_TOOL_MATERIALS = item("ruby_tool_materials");

    /** Ítems que sirven para reparar armadura de rubí en el yunque. */
    public static final TagKey<Item> RUBY_ARMOR_MATERIALS = item("ruby_armor_materials");

    private static TagKey<Block> block(String name) {
        return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(ReimaginedTools.MODID, name));
    }

    private static TagKey<Item> item(String name) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(ReimaginedTools.MODID, name));
    }
}
