package com.marcofallasu.reimaginedtools.material;

import net.minecraft.world.item.ToolMaterial;

/** Materiales de herramienta. Añade aquí uno nuevo por cada "tier" que quieras crear. */
public final class ModToolMaterials {
    private ModToolMaterials() {}

    //                                         bloques no minables,                    durabilidad, velocidad, daño extra, encantab., reparación
    public static final ToolMaterial RUBY = new ToolMaterial(
        ModTags.INCORRECT_FOR_RUBY_TOOL, 1800, 8.5F, 3.5F, 12, ModTags.RUBY_TOOL_MATERIALS);
}
