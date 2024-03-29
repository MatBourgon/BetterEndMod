package net.matt_with_a_hat.features;

import java.util.Random;

import com.mojang.serialization.Codec;

import net.matt_with_a_hat.BetterEnd;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public class FluoriteCraterFeature extends Feature<DefaultFeatureConfig>
{


    public FluoriteCraterFeature(Codec<DefaultFeatureConfig> configFactory)
    {
        super(configFactory);
    }

    //Carve a sphere into the world
    void CarveSphere(StructureWorldAccess world, BlockPos pos, Random random, int radiusX, int radiusY, int radiusZ)
    {
        for(int x = -radiusX; x < radiusX; ++x)
        {
            for(int y = -radiusY; y < radiusY; ++y)
            {
                for(int z = -radiusZ; z < radiusZ; ++z)
                {
                    float ix, iy, iz;
                    ix = x / (float)radiusX;
                    iy = y / (float)radiusY;
                    iz = z / (float)radiusZ;
                    if ((ix * ix + iy * iy + iz * iz) < .6f)
                    {
                        BlockPos checkPos = pos.add(x, y, z);
                        world.setBlockState(checkPos, Blocks.AIR.getDefaultState(), 0);
                    }
                }
            }
        }
    }

    //Place ore. if the ore would be exposed, don't create it.
    void GenerateOre(StructureWorldAccess world, BlockPos pos, Random random, int stepsLeft)
    {
        if (!IsExposed(world, pos))
        {
            if (world.getBlockState(pos) == Blocks.END_STONE.getDefaultState())
            {
                if (random.nextFloat() < 0.2f)
                    world.setBlockState(pos, BetterEnd.blockFluoriteOre.getDefaultState(), 0);
            }
        }
        if (stepsLeft <= 0)
            return;
        GenerateOre(world, pos.add(-1, 0, 0), random, stepsLeft-1);
        GenerateOre(world, pos.add(1, 0, 0), random, stepsLeft-1);
        GenerateOre(world, pos.add(0, -1, 0), random, stepsLeft-1);
        GenerateOre(world, pos.add(0, 1, 0), random, stepsLeft-1);
        GenerateOre(world, pos.add(0, 0, -1), random, stepsLeft-1);
        GenerateOre(world, pos.add(0, 0, 1), random, stepsLeft-1);
    }

    //Check if a given coordinate is exposed to air
    boolean IsExposed(StructureWorldAccess world, BlockPos pos)
    {
        if (world.getBlockState(pos.add(0, 1, 0)) == Blocks.AIR.getDefaultState())
            return true;
        if (world.getBlockState(pos.add(-1, 0, 0)) == Blocks.AIR.getDefaultState())
            return true;
        if (world.getBlockState(pos.add(1, 0, 0)) == Blocks.AIR.getDefaultState())
            return true;
        if (world.getBlockState(pos.add(0, -1, 0)) == Blocks.AIR.getDefaultState())
            return true;
        if (world.getBlockState(pos.add(0, 0, -1)) == Blocks.AIR.getDefaultState())
            return true;
        if (world.getBlockState(pos.add(0, 0, 1)) == Blocks.AIR.getDefaultState())
            return true;
        return false;
    }

    //Check if a given coordinate is completely exposed to air
    boolean IsFloating(StructureWorldAccess world, BlockPos pos)
    {
        if (world.getBlockState(pos.add(0, 1, 0)) != Blocks.AIR.getDefaultState())
            return false;
        if (world.getBlockState(pos.add(-1, 0, 0)) != Blocks.AIR.getDefaultState())
            return false;
        if (world.getBlockState(pos.add(1, 0, 0)) != Blocks.AIR.getDefaultState())
            return false;
        if (world.getBlockState(pos.add(0, -1, 0)) != Blocks.AIR.getDefaultState())
            return false;
        if (world.getBlockState(pos.add(0, 0, -1)) != Blocks.AIR.getDefaultState())
            return false;
        if (world.getBlockState(pos.add(0, 0, 1)) != Blocks.AIR.getDefaultState())
            return false;
        return true;
    }

    //If there are any floating pieces, either convert it to endstone or air. Return true if we had to convert anything
    boolean CleanupFloaties(StructureWorldAccess world, BlockPos pos, Random random, int radiusX, int radiusY, int radiusZ, boolean forceEndstone)
    {
        boolean fixedAnyFloaties = false;
        for(int x = -radiusX; x < radiusX; ++x)
        {
            for(int y = -radiusY; y < radiusY; ++y)
            {
                for(int z = -radiusZ; z < radiusZ; ++z)
                {
                    if (world.getBlockState(pos.add(x, y, z)) == BetterEnd.blockFluoriteOre.getDefaultState())
                    {
                        if (forceEndstone || random.nextFloat() < 0.1f)
                            world.setBlockState(pos.add(x, y, z), Blocks.END_STONE.getDefaultState(), 0);
                        else
                        {
                            world.setBlockState(pos.add(x, y, z), Blocks.AIR.getDefaultState(), 0);
                            fixedAnyFloaties = true;
                        }
                        
                    }
                }
            }
        }
        return fixedAnyFloaties;
    }

    //Generate ore craters and clusters
    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator generator, Random random, BlockPos pos, DefaultFeatureConfig config)
    {
        if (random.nextFloat() > 0.1f)
            return false;
        if (pos.getX() * pos.getX() + pos.getZ() * pos.getZ() < 1000000)
            return false;


        int radiusY = 3;
        int yOffset = - (1 + (random.nextInt(radiusY+1)-1));
        int xOffset = random.nextInt(5) - 2;
        int zOffset = random.nextInt(5) - 2;

        if (random.nextFloat() < 0.3f)
        {
            //Crater generator
            GenerateOre(world, pos.add(8 + xOffset, yOffset, 8 + zOffset), random, (radiusY * radiusY)/2);
            CarveSphere(world, pos.add(8 + xOffset, yOffset, 8 + zOffset), random, 3, radiusY-2, 3);
            while(CleanupFloaties(world, pos.add(8 + xOffset, yOffset, 8 + zOffset), random, 3, radiusY-2, 3, false));
        }
        else
        {
            //Cluster generator
            yOffset -= random.nextInt(3);
            GenerateOre(world, pos.add(8 + xOffset, yOffset, 8 + zOffset), random, radiusY);
            while(CleanupFloaties(world, pos.add(8 + xOffset, yOffset, 8 + zOffset), random, 3, radiusY-2, 3, true));
        }

        return true;
    }
}