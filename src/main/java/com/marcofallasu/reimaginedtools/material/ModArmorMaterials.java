package com.marcofallasu.reimaginedtools.material;

import com.marcofallasu.reimaginedtools.ReimaginedTools;
import java.util.Map;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

/** Materiales de armadura. El asset (modelo/textura) vive en assets/reimaginedtools/equipment/<nombre>.json */
public final class ModArmorMaterials {
    private ModArmorMaterials() {}

    private static ResourceKey<EquipmentAsset> asset(String name) {
        return ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(ReimaginedTools.MODID, name));
    }

    public static final ResourceKey<EquipmentAsset> RUBY_ASSET = asset("ruby");

    public static final ArmorMaterial RUBY = new ArmorMaterial(
        36, // multiplicador de durabilidad (diamante = 33)
        Map.of(
            ArmorType.BOOTS, 3,
            ArmorType.LEGGINGS, 6,
            ArmorType.CHESTPLATE, 8,
            ArmorType.HELMET, 3,
            ArmorType.BODY, 11),
        12, // encantabilidad
        SoundEvents.ARMOR_EQUIP_DIAMOND,
        1.5F, // dureza (toughness)
        0.0F, // resistencia al empuje
        ModTags.RUBY_ARMOR_MATERIALS,
        RUBY_ASSET);

    /** Cobre: defensa media y buena durabilidad. */
    public static final ArmorMaterial COPPER = new ArmorMaterial(
        16,
        Map.of(ArmorType.BOOTS, 2, ArmorType.LEGGINGS, 4, ArmorType.CHESTPLATE, 5, ArmorType.HELMET, 2, ArmorType.BODY, 8),
        14, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F, ModTags.COPPER_ARMOR_MATERIALS, asset("copper"));

    /** Lapislázuli: poca defensa y poca durabilidad, pero la más encantable. */
    public static final ArmorMaterial LAPIS = new ArmorMaterial(
        10,
        Map.of(ArmorType.BOOTS, 1, ArmorType.LEGGINGS, 3, ArmorType.CHESTPLATE, 4, ArmorType.HELMET, 1, ArmorType.BODY, 6),
        30, SoundEvents.ARMOR_EQUIP_GOLD, 0.0F, 0.0F, ModTags.LAPIS_ARMOR_MATERIALS, asset("lapis"));

    /** Amatista: buena defensa y algo de dureza, pero frágil. */
    public static final ArmorMaterial AMETHYST = new ArmorMaterial(
        9,
        Map.of(ArmorType.BOOTS, 2, ArmorType.LEGGINGS, 5, ArmorType.CHESTPLATE, 6, ArmorType.HELMET, 2, ArmorType.BODY, 9),
        22, SoundEvents.ARMOR_EQUIP_DIAMOND, 1.0F, 0.0F, ModTags.AMETHYST_ARMOR_MATERIALS, asset("amethyst"));
}
