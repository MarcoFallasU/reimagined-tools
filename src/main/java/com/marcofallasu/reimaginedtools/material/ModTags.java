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

    // Cobre, lapislázuli y amatista (cada uno con su tag de reparación y su tag de nivel de minado)
    public static final TagKey<Block> INCORRECT_FOR_COPPER_TOOL = block("incorrect_for_copper_tool");
    public static final TagKey<Item> COPPER_TOOL_MATERIALS = item("copper_tool_materials");
    public static final TagKey<Block> INCORRECT_FOR_LAPIS_TOOL = block("incorrect_for_lapis_tool");
    public static final TagKey<Item> LAPIS_TOOL_MATERIALS = item("lapis_tool_materials");
    public static final TagKey<Block> INCORRECT_FOR_AMETHYST_TOOL = block("incorrect_for_amethyst_tool");
    public static final TagKey<Item> AMETHYST_TOOL_MATERIALS = item("amethyst_tool_materials");

    // Reparación de armaduras
    public static final TagKey<Item> COPPER_ARMOR_MATERIALS = item("copper_armor_materials");
    public static final TagKey<Item> LAPIS_ARMOR_MATERIALS = item("lapis_armor_materials");
    public static final TagKey<Item> AMETHYST_ARMOR_MATERIALS = item("amethyst_armor_materials");

    private static TagKey<Block> block(String name) {
        return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(ReimaginedTools.MODID, name));
    }

    private static TagKey<Item> item(String name) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(ReimaginedTools.MODID, name));
    }
}
