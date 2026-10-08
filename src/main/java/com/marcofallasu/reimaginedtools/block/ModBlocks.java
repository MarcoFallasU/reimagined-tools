package com.marcofallasu.reimaginedtools.block;

import com.marcofallasu.reimaginedtools.ReimaginedTools;
import java.util.function.Function;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlocks {
    private ModBlocks() {}

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ReimaginedTools.MODID);

    // Se comportan como la mena de diamante: necesitan pico de hierro o mejor, y sueltan experiencia.
    // Los drops están en data/.../loot_table/blocks/ y la generación en data/.../worldgen/ + forge/biome_modifier/.
    public static final RegistryObject<Block> RUBY_ORE = register("ruby_ore", Blocks.DIAMOND_ORE);
    public static final RegistryObject<Block> DEEPSLATE_RUBY_ORE = register("deepslate_ruby_ore", Blocks.DEEPSLATE_DIAMOND_ORE);

    private static RegistryObject<Block> register(String name, Block copyFrom) {
        Function<BlockBehaviour.Properties, Block> factory = props -> new DropExperienceBlock(UniformInt.of(3, 7), props);
        return BLOCKS.register(name, () -> factory.apply(BlockBehaviour.Properties.ofFullCopy(copyFrom).setId(BLOCKS.key(name))));
    }
}
