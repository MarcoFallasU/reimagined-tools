package com.marcofallasu.reimaginedtools.item;

import net.minecraft.world.item.Item;

/**
 * Pieza de armadura con modelo 3D propio (peto, pantalones, botas). No usa la capa de armadura de vanilla: la dibuja
 * {@code ModelArmorLayer}, que coloca un modelo del ítem sobre cada parte del cuerpo. Solo marca el tipo de ítem.
 */
public class ModelArmorItem extends Item {
    public ModelArmorItem(Item.Properties properties) {
        super(properties);
    }
}
