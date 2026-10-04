package com.marcofallasu.reimaginedtools.item;

import java.util.function.BiFunction;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.ToolMaterial;

/**
 * Cada tipo de herramienta/arma del mod. Para añadir un tipo nuevo (hacha de guerra, martillo, etc.):
 * 1. añade una entrada aquí con su fábrica,
 * 2. añade texturas/modelos {@code <material>_<sufijo>} y la traducción,
 * 3. (opcional) añade el tag minecraft:<tipo> para que reciba encantamientos.
 * Todos los materiales registrados con {@link ModItems#registerToolSet} lo recibirán automáticamente.
 */
public enum ToolKind {
    SWORD("sword", (m, p) -> new Item(p.sword(m, 3.0F, -2.4F))),
    PICKAXE("pickaxe", (m, p) -> new Item(p.pickaxe(m, 1.0F, -2.8F))),
    AXE("axe", (m, p) -> new AxeItem(m, 6.0F, -3.1F, p)),
    SHOVEL("shovel", (m, p) -> new ShovelItem(m, 1.5F, -3.0F, p)),
    HOE("hoe", (m, p) -> new HoeItem(m, -1.0F, -1.0F, p)),
    SPEAR("spear", (m, p) -> new Item(p.spear(m, 1.05F, 1.075F, 0.5F, 3.0F, 7.5F, 6.5F, 5.1F, 10.0F, 4.6F))),
    SCYTHE("scythe", (m, p) -> new ScytheItem(m, 2.0F, -2.6F, p)),
    SAW("saw", (m, p) -> new Item(p.tool(m, net.minecraft.tags.BlockTags.MINEABLE_WITH_AXE, 2.5F, -2.2F, 0.0F)));

    private final String suffix;
    private final BiFunction<ToolMaterial, Item.Properties, Item> factory;

    ToolKind(String suffix, BiFunction<ToolMaterial, Item.Properties, Item> factory) {
        this.suffix = suffix;
        this.factory = factory;
    }

    public String suffix() {
        return suffix;
    }

    public Item create(ToolMaterial material, Item.Properties properties) {
        return factory.apply(material, properties);
    }
}
