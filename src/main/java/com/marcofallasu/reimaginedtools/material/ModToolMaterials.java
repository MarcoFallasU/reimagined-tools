package com.marcofallasu.reimaginedtools.material;

import net.minecraft.world.item.ToolMaterial;

/** Materiales de herramienta. Añade aquí uno nuevo por cada "tier" que quieras crear. */
public final class ModToolMaterials {
    private ModToolMaterials() {}

    //                                         bloques no minables,                    durabilidad, velocidad, daño extra, encantab., reparación
    public static final ToolMaterial RUBY = new ToolMaterial(
        ModTags.INCORRECT_FOR_RUBY_TOOL, 1800, 8.5F, 3.5F, 12, ModTags.RUBY_TOOL_MATERIALS);

    /** Cobre mejorado: más duro y rápido que el cobre de vanilla (190 de durabilidad). Mina hasta hierro. */
    public static final ToolMaterial COPPER = new ToolMaterial(
        ModTags.INCORRECT_FOR_COPPER_TOOL, 320, 5.5F, 1.5F, 14, ModTags.COPPER_TOOL_MATERIALS);

    /** Lapislázuli: frágil y flojo, pero el más encantable del mod. Mina como piedra. */
    public static final ToolMaterial LAPIS = new ToolMaterial(
        ModTags.INCORRECT_FOR_LAPIS_TOOL, 200, 4.5F, 1.0F, 30, ModTags.LAPIS_TOOL_MATERIALS);

    /** Amatista: muy rápida y con buen daño, pero se rompe pronto. Mina hasta hierro. */
    public static final ToolMaterial AMETHYST = new ToolMaterial(
        ModTags.INCORRECT_FOR_AMETHYST_TOOL, 150, 9.0F, 2.5F, 22, ModTags.AMETHYST_TOOL_MATERIALS);
}
