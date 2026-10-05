package com.marcofallasu.reimaginedtools.item;

import com.marcofallasu.reimaginedtools.ReimaginedTools;
import com.marcofallasu.reimaginedtools.material.ModArmorMaterials;
import com.marcofallasu.reimaginedtools.material.ModToolMaterials;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    private ModItems() {}

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ReimaginedTools.MODID);

    // ---- Rubí ----
    public static final RegistryObject<Item> RUBY = register("ruby", Item::new);
    public static final Map<ToolKind, RegistryObject<Item>> RUBY_TOOLS = registerToolSet("ruby", ModToolMaterials.RUBY);
    public static final Map<ArmorType, RegistryObject<Item>> RUBY_ARMOR = registerArmorSet("ruby", ModArmorMaterials.RUBY);

    // ---- Minerales poco usados: herramientas y armadura (las recetas son custom y se añaden aparte) ----
    public static final Map<ToolKind, RegistryObject<Item>> COPPER_TOOLS = registerToolSet("copper", ModToolMaterials.COPPER);
    public static final Map<ToolKind, RegistryObject<Item>> LAPIS_TOOLS = registerToolSet("lapis", ModToolMaterials.LAPIS);
    public static final Map<ToolKind, RegistryObject<Item>> AMETHYST_TOOLS = registerToolSet("amethyst", ModToolMaterials.AMETHYST);

    public static final Map<ArmorType, RegistryObject<Item>> COPPER_ARMOR = registerArmorSet("copper", ModArmorMaterials.COPPER);
    public static final Map<ArmorType, RegistryObject<Item>> LAPIS_ARMOR = registerArmorSet("lapis", ModArmorMaterials.LAPIS);
    public static final Map<ArmorType, RegistryObject<Item>> AMETHYST_ARMOR = registerArmorSet("amethyst", ModArmorMaterials.AMETHYST);

    // Para un material nuevo: define su ToolMaterial/ArmorMaterial y añade aquí otras dos líneas como las de arriba.

    /** Registra todas las herramientas de {@link ToolKind} para un material: {@code <material>_sword}, {@code _scythe}... */
    public static Map<ToolKind, RegistryObject<Item>> registerToolSet(String material, ToolMaterial toolMaterial) {
        Map<ToolKind, RegistryObject<Item>> set = new EnumMap<>(ToolKind.class);
        for (ToolKind kind : ToolKind.values()) {
            set.put(kind, register(material + "_" + kind.suffix(), props -> kind.create(toolMaterial, props)));
        }
        return set;
    }

    /** Registra casco, peto, pantalones y botas: {@code <material>_helmet}, etc. */
    public static Map<ArmorType, RegistryObject<Item>> registerArmorSet(String material, ArmorMaterial armorMaterial) {
        Map<ArmorType, RegistryObject<Item>> set = new EnumMap<>(ArmorType.class);
        for (ArmorType type : new ArmorType[] {ArmorType.HELMET, ArmorType.CHESTPLATE, ArmorType.LEGGINGS, ArmorType.BOOTS}) {
            set.put(type, register(material + "_" + type.getName(), props -> new Item(props.humanoidArmor(armorMaterial, type))));
        }
        return set;
    }

    private static RegistryObject<Item> register(String name, Function<Item.Properties, Item> factory) {
        return ITEMS.register(name, () -> factory.apply(new Item.Properties().setId(ITEMS.key(name))));
    }
}
