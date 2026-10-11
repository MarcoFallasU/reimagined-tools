package com.marcofallasu.reimaginedtools.client;

import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.armorstand.ArmorStandArmorModel;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraftforge.client.event.EntityRenderersEvent;

/** Registro de lo que solo existe en el cliente: la capa que dibuja la armadura con modelo 3D. */
public final class ClientSetup {
    private ClientSetup() {}

    public static void register() {
        EntityRenderersEvent.AddLayers.BUS.addListener(ClientSetup::addLayers);
    }

    private static void addLayers(EntityRenderersEvent.AddLayers event) {
        ArmorStandRenderer stand = event.getEntityRenderer(EntityType.ARMOR_STAND);
        if (stand != null) {
            stand.addLayer(new ModelArmorLayer<>(stand,
                ArmorModelSet.bake(ModelLayers.ARMOR_STAND_ARMOR, event.getEntityModels(), ArmorStandArmorModel::new),
                ArmorModelSet.bake(ModelLayers.ARMOR_STAND_SMALL_ARMOR, event.getEntityModels(), ArmorStandArmorModel::new), 0.5F));
        }
        for (PlayerModelType type : event.getModelTypes()) {
            var player = event.<net.minecraft.client.renderer.entity.player.AvatarRenderer<net.minecraft.client.player.AbstractClientPlayer>>getPlayerRenderer(type);
            if (player != null) {
                boolean slim = type == PlayerModelType.SLIM;
                var set = ArmorModelSet.bake(slim ? ModelLayers.PLAYER_SLIM_ARMOR : ModelLayers.PLAYER_ARMOR, event.getEntityModels(), part -> new PlayerModel(part, slim));
                player.addLayer(new ModelArmorLayer<>(player, set, set, 1.0F));
            }
        }
    }
}
