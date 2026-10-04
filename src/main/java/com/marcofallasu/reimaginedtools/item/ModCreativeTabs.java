package com.marcofallasu.reimaginedtools.item;

import com.marcofallasu.reimaginedtools.ReimaginedTools;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class ModCreativeTabs {
    private ModCreativeTabs() {}

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ReimaginedTools.MODID);

    public static final RegistryObject<CreativeModeTab> MAIN = TABS.register("main", () -> CreativeModeTab.builder()
        .title(Component.translatable("itemGroup." + ReimaginedTools.MODID + ".main"))
        .withTabsBefore(CreativeModeTabs.COMBAT)
        .icon(() -> ModItems.RUBY_TOOLS.get(ToolKind.SWORD).get().getDefaultInstance())
        .displayItems((parameters, output) -> ModItems.ITEMS.getEntries().forEach(item -> output.accept(item.get())))
        .build());
}
