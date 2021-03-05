package net.matt_with_a_hat;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.NopeDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import software.bernie.geckolib3.GeckoLib;
import net.matt_with_a_hat.blocks.BlockFluoriteBlock;
import net.matt_with_a_hat.blocks.BlockFluoriteOre;
import net.matt_with_a_hat.blocks.BlockWarpPad;
import net.matt_with_a_hat.blocks.BlockWarpPadEntity;
import net.matt_with_a_hat.entity.EntityChorusENT;
import net.matt_with_a_hat.features.FluoriteCraterFeature;
import net.minecraft.item.Item;
import net.matt_with_a_hat.items.ItemFluorite;
import net.matt_with_a_hat.items.ItemVersEye;

@SuppressWarnings("deprecation")
public class BetterEnd implements ModInitializer {

    public static Feature<DefaultFeatureConfig> FLUORITE_CRATER = new FluoriteCraterFeature(DefaultFeatureConfig.CODEC);
    public static ConfiguredFeature<?, ?> FLUORITE_CRATER_CONFIGURED = FLUORITE_CRATER.configure(FeatureConfig.DEFAULT)
    .decorate(Decorator.HEIGHTMAP_WORLD_SURFACE.configure(new NopeDecoratorConfig()));
    
    public static BlockEntityType<BlockWarpPadEntity> WARP_PAD_ENTITY;

    public static final Block blockFluoriteOre = new BlockFluoriteOre(FabricBlockSettings.of(Material.STONE).requiresTool().strength(3f, 9f).breakByTool(FabricToolTags.PICKAXES, 3));
    public static final Block blockFluoriteBlock = new BlockFluoriteBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(3f, 9f).breakByTool(FabricToolTags.PICKAXES, 3));
    public static final Block blockWarpPad = new BlockWarpPad(FabricBlockSettings.of(Material.STONE).requiresTool().strength(3f, 5f).breakByTool(FabricToolTags.PICKAXES, 3));
    public static final Item itemFluorite = new ItemFluorite(new FabricItemSettings().group(ItemGroup.MATERIALS));
    public static final Item itemVersEye = new ItemVersEye(new FabricItemSettings().group(ItemGroup.MATERIALS));

    public static EntityType<EntityChorusENT> CHORUS_ENT = Registry.register(
        Registry.ENTITY_TYPE,
        new Identifier("betterend", "chorus_ent"),
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, EntityChorusENT::new).size(1.8f, 3.2f).build()
    );


    @Override
    public void onInitialize() {
        GeckoLib.initialize();

        Registry.register(Registry.BLOCK, new Identifier("betterend", "fluorite_ore"), blockFluoriteOre);
        Registry.register(Registry.BLOCK, new Identifier("betterend", "fluorite_block"), blockFluoriteBlock);
        Registry.register(Registry.BLOCK, new Identifier("betterend", "warppad"), blockWarpPad);
        Registry.register(Registry.ITEM, new Identifier("betterend", "fluorite_ore"), new BlockItem(blockFluoriteOre, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)));
        Registry.register(Registry.ITEM, new Identifier("betterend", "fluorite_block"), new BlockItem(blockFluoriteBlock, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)));
        Registry.register(Registry.ITEM, new Identifier("betterend", "warppad"), new BlockItem(blockWarpPad, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)));
        Registry.register(Registry.ITEM, new Identifier("betterend", "fluorite"), itemFluorite);
        Registry.register(Registry.ITEM, new Identifier("betterend", "vers_eye"), itemVersEye);
        Registry.register(Registry.ITEM, new Identifier("betterend", "chorus_ent_egg"), new SpawnEggItem(CHORUS_ENT, 0x8b6db6, 0xf9d5f8, new FabricItemSettings().group(ItemGroup.MISC)));
        WARP_PAD_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "betterend:warppad", BlockEntityType.Builder.create(BlockWarpPadEntity::new, blockWarpPad).build(null));

        RegistryKey<ConfiguredFeature<?, ?>> fluoriteCrater = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN,
                                                                            new Identifier("betterend", "fluorite_crater"));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, fluoriteCrater.getValue(), FLUORITE_CRATER_CONFIGURED);
        BiomeModifications.addFeature(BiomeSelectors.foundInTheEnd(), GenerationStep.Feature.UNDERGROUND_ORES, fluoriteCrater);
        Registry.register(Registry.FEATURE, new Identifier("betterend", "fluorite_crater"), FLUORITE_CRATER);



        FabricDefaultAttributeRegistry.register(CHORUS_ENT, EntityChorusENT.createHostileAttributes()
        .add(EntityAttributes.GENERIC_MAX_HEALTH, 30)
        .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, .15)
        .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1)
        .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 26.0)
        .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0)
        );

    }

}
