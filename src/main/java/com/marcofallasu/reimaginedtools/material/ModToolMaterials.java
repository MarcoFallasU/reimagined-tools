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

    /** Oscuridad: idéntica a la netherite (la habilidad especial se añadirá más adelante). */
    public static final ToolMaterial DARKNESS = new ToolMaterial(
        ModTags.INCORRECT_FOR_DARKNESS_TOOL, 2031, 9.0F, 4.0F, 15, ModTags.DARKNESS_TOOL_MATERIALS);

    /** Luz: equilibrada y muy encantable. Mina como diamante. */
    public static final ToolMaterial LIGHT = new ToolMaterial(
        ModTags.INCORRECT_FOR_LIGHT_TOOL, 900, 7.0F, 3.0F, 25, ModTags.LIGHT_TOOL_MATERIALS);

    /** Electricidad: la más rápida. Mina como hierro. */
    public static final ToolMaterial ELECTRICITY = new ToolMaterial(
        ModTags.INCORRECT_FOR_ELECTRICITY_TOOL, 700, 10.0F, 2.5F, 18, ModTags.ELECTRICITY_TOOL_MATERIALS);

    /** Música: poco daño y poca durabilidad, pero la encantabilidad más alta. Mina como piedra. */
    public static final ToolMaterial MUSIC = new ToolMaterial(
        ModTags.INCORRECT_FOR_MUSIC_TOOL, 500, 6.0F, 1.5F, 30, ModTags.MUSIC_TOOL_MATERIALS);
}
