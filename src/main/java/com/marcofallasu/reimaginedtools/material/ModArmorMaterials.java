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

    public static final ResourceKey<EquipmentAsset> RUBY_ASSET = ResourceKey.create(
        EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(ReimaginedTools.MODID, "ruby"));

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
}
