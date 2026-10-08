package com.marcofallasu.reimaginedtools;

import com.marcofallasu.reimaginedtools.ability.AbilityEvents;
import com.marcofallasu.reimaginedtools.ability.ModAbilities;
import com.marcofallasu.reimaginedtools.block.ModBlocks;
import com.marcofallasu.reimaginedtools.item.ModCreativeTabs;
import com.marcofallasu.reimaginedtools.item.ModItems;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ReimaginedTools.MODID)
public final class ReimaginedTools {
    public static final String MODID = "reimaginedtools";

    public ReimaginedTools(FMLJavaModLoadingContext context) {
        var modBusGroup = context.getModBusGroup();

        ModBlocks.BLOCKS.register(modBusGroup);
        ModItems.ITEMS.register(modBusGroup);
        ModCreativeTabs.TABS.register(modBusGroup);

        ModAbilities.register();
        AbilityEvents.register();
    }
}
