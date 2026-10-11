package com.marcofallasu.reimaginedtools.client;

import com.marcofallasu.reimaginedtools.item.ModelArmorItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomModelData;

/**
 * Dibuja las piezas de armadura con modelo 3D propio. Por cada pieza se dibuja un modelo por hueso (torso, brazos,
 * piernas, cabeza): el ítem elige su modelo con el dato personalizado "head", "body", "right_arm", "left_arm", "right_leg" o
 * "left_leg" (ver assets/.../items/darkness_chestplate.json), y cada modelo está escrito con coordenadas relativas
 * al pivote de ese hueso, así que se coloca justo donde el hueso tiene su origen y se mueve y gira con él.
 */
public final class ModelArmorLayer<S extends HumanoidRenderState, M extends HumanoidModel<S>, A extends HumanoidModel<S>> extends RenderLayer<S, M> {
    private final ArmorModelSet<A> models;
    private final ArmorModelSet<A> babyModels;
    private final float babyScale;

    /**
     * @param babyModels modelos de armadura para la versión pequeña (armor stand pequeño, bebés)
     * @param babyScale  escala extra de las piezas en la versión pequeña. El armor stand pequeño reduce los huesos a la
     *                   mitad dentro de su modelo (0.5); en jugadores y mobs el renderizador ya escala todo el cuerpo (1.0)
     */
    public ModelArmorLayer(RenderLayerParent<S, M> parent, ArmorModelSet<A> models, ArmorModelSet<A> babyModels, float babyScale) {
        super(parent);
        this.models = models;
        this.babyModels = babyModels;
        this.babyScale = babyScale;
    }

    @Override
    public void submit(PoseStack pose, SubmitNodeCollector collector, int light, S state, float yRot, float xRot) {
        piece(pose, collector, light, state, state.headEquipment, EquipmentSlot.HEAD, "head");
        piece(pose, collector, light, state, state.chestEquipment, EquipmentSlot.CHEST, "body", "right_arm", "left_arm");
        piece(pose, collector, light, state, state.legsEquipment, EquipmentSlot.LEGS, "right_leg", "left_leg");
        piece(pose, collector, light, state, state.feetEquipment, EquipmentSlot.FEET, "right_leg", "left_leg");
    }

    private void piece(PoseStack pose, SubmitNodeCollector collector, int light, S state, ItemStack stack, EquipmentSlot slot, String... bones) {
        if (!(stack.getItem() instanceof ModelArmorItem)) {
            return;
        }
        A model = (state.isBaby ? babyModels : models).get(slot);
        model.setupAnim(state);
        for (String bone : bones) {
            ModelPart part = switch (bone) {
                case "head" -> model.head;
                case "body" -> model.body;
                case "right_arm" -> model.rightArm;
                case "left_arm" -> model.leftArm;
                case "right_leg" -> model.rightLeg;
                default -> model.leftLeg;
            };
            if (!part.visible) {
                continue; // p. ej. los brazos de un armor stand sin brazos
            }
            ItemStack variant = stack.copy();
            variant.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of(), List.of(), List.of(bone), List.of()));
            ItemStackRenderState renderState = new ItemStackRenderState();
            Minecraft.getInstance().getItemModelResolver().updateForTopItem(renderState, variant, ItemDisplayContext.NONE, Minecraft.getInstance().level, null, 0);
            if (renderState.isEmpty()) {
                continue;
            }
            pose.pushPose();
            part.translateAndRotate(pose);
            if (state.isBaby) {
                pose.scale(babyScale, babyScale, babyScale);
            }
            // del espacio del modelo de entidad (y hacia abajo) al espacio de un modelo de ítem (y hacia arriba), igual que
            // hace la capa de cabeza de vanilla; 1 unidad del modelo del ítem = 1 píxel del cuerpo
            pose.mulPose(Axis.YP.rotationDegrees(180.0F));
            pose.scale(1.0F, -1.0F, -1.0F);
            renderState.submit(pose, collector, light, OverlayTexture.NO_OVERLAY, state.outlineColor);
            pose.popPose();
        }
    }
}
