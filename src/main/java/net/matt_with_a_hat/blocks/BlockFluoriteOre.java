package net.matt_with_a_hat.blocks;

import java.util.Random;

import net.minecraft.block.OreBlock;
import net.minecraft.util.math.MathHelper;

public class BlockFluoriteOre extends OreBlock {
    public BlockFluoriteOre(Settings settings)
    {
        super(settings);
    }

    @Override
    protected int getExperienceWhenMined(Random random) {
        return MathHelper.nextInt(random, 3, 7);
    }

    
}
