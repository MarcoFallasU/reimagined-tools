package com.marcofallasu.reimaginedtools.item;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/** Guadaña: al romper un cultivo u hojas con ella, rompe también los vecinos en un área de 3x3. */
public class ScytheItem extends Item {
    public ScytheItem(ToolMaterial material, float attackDamage, float attackSpeed, Item.Properties properties) {
        super(properties.tool(material, BlockTags.MINEABLE_WITH_HOE, attackDamage, attackSpeed, 0.0F));
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miner) {
        boolean result = super.mineBlock(stack, level, state, pos, miner);
        if (level.isClientSide() || !isSweepable(state)) {
            return result;
        }
        for (BlockPos neighbour : BlockPos.betweenClosed(pos.offset(-1, 0, -1), pos.offset(1, 0, 1))) {
            if (stack.isEmpty()) {
                break;
            }
            if (neighbour.equals(pos)) {
                continue;
            }
            BlockState neighbourState = level.getBlockState(neighbour);
            if (isSweepable(neighbourState) && level.destroyBlock(neighbour.immutable(), true, miner)) {
                stack.hurtAndBreak(1, miner, EquipmentSlot.MAINHAND);
            }
        }
        return result;
    }

    private static boolean isSweepable(BlockState state) {
        return state.is(BlockTags.CROPS) || state.is(BlockTags.LEAVES) || state.is(BlockTags.FLOWERS);
    }
}
