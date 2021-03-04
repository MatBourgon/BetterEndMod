package net.matt_with_a_hat.WarpPadHelpers;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ChunkPadList
{
    List<BlockPos> warps;
    int chunkX, chunkZ;

    public ChunkPadList(int chunkX, int chunkZ)
    {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        warps = new ArrayList<BlockPos>();
    }

    public int getX() {return chunkX;}
    public int getZ() {return chunkZ;}

    public boolean isWarpInList(BlockPos pad)
    {
        return warps.contains(pad);
    }

    public void add(BlockPos warp)
    {
        if (!isWarpInList(warp))
            warps.add(warp);

    }

    public void remove(BlockPos warp)
    {
        warps.remove(warp);
    }

    @Nullable
    private BlockPos findNearestBlockFromPosition(BlockPos ignore, World world, Vec3d pos)
    {
        BlockPos pad = null;
        double dist = 9999.0;
        for(BlockPos warp : warps)
        {
            if (warp.getX() == ignore.getX() && warp.getY() == ignore.getY() && warp.getZ() == ignore.getZ())
                continue;
            if (world.getBlockState(warp.add(0, 1, 0)) != Blocks.AIR.getDefaultState())
                continue;

            Vec3d _warp = new Vec3d(warp.getX(), warp.getY(), warp.getZ());
            double newDist = pos.squaredDistanceTo(_warp);
            if (newDist < dist)
            {
                pad = warp;
                dist = newDist;
            }
        }
        return pad;
    }

    @Nullable
    public BlockPos findNearestBlock(BlockPos pos, World world, float lookingYaw)
    {
        BlockPos pad = null;
        //int lastManhattan = 9999;
        for(BlockPos warp : warps)
        {
            if (warp.getX() == pos.getX() && warp.getY() == pos.getY() && warp.getZ() == pos.getZ())
                continue;
                
            if (world.getBlockState(warp.add(0, 1, 0)) != Blocks.AIR.getDefaultState())
                continue;

            if (pad == null)
            {
                pad = warp;
                continue;
            }
            int manhattanA, manhattanB;
            manhattanA = warp.getManhattanDistance(pos);
            manhattanB = pad.getManhattanDistance(pos);
            if (manhattanA < manhattanB)
                pad = warp;
            else if (manhattanA == manhattanB)
            {
                //if (manhattanA < lastManhattan)
                {
                    Vec3d forward = new Vec3d((double)pos.getX() - Math.sin(lookingYaw / 180.0 * Math.PI), pos.getY(), (double)pos.getZ() + Math.cos(lookingYaw/ 180.0 * Math.PI));
                    BlockPos nearestInFront = findNearestBlockFromPosition(pos, world, forward);
                    if (nearestInFront != null)
                    {
                        //lastManhattan = manhattanA;
                        pad = nearestInFront;
                    }
                }
            }
        }
        if (pad != null)
            System.out.println("Chosen: " + pad.toString());
        return pad;
    }
}