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
        //Find nearest block by looking direction
        BlockPos pad = null;
        double dist = 9999.0; //Max distance, we only check 16x256x16, so we shouldn't really hit it.
        for(BlockPos warp : warps)
        {
            //Ignore if start block
            if (warp.getX() == ignore.getX() && warp.getY() == ignore.getY() && warp.getZ() == ignore.getZ())
                continue;
            //Ignore if not air above block
            if (world.getBlockState(warp.add(0, 1, 0)) != Blocks.AIR.getDefaultState())
                continue;

            //Compare squared distance
            Vec3d _warp = new Vec3d(warp.getX(), warp.getY(), warp.getZ());
            double newDist = pos.squaredDistanceTo(_warp);
            if (newDist < dist)
            {
                //Update pad and distance if new dist is smaller
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
        //loop through warps to find nearest warp to blockpos
        for(BlockPos warp : warps)
        {
            //Ignore if same block
            if (warp.getX() == pos.getX() && warp.getY() == pos.getY() && warp.getZ() == pos.getZ())
                continue;
                
            //Ignore if no air above
            if (world.getBlockState(warp.add(0, 1, 0)) != Blocks.AIR.getDefaultState())
                continue;

            //Immediately accept if no pad found
            if (pad == null)
            {
                pad = warp;
                continue;
            }

            //In manhattan distance is smaller, set as new pad. Manhattan distance is only addition by block count.
            int manhattanA, manhattanB;
            manhattanA = warp.getManhattanDistance(pos);
            manhattanB = pad.getManhattanDistance(pos);
            if (manhattanA < manhattanB)
                pad = warp;
            else if (manhattanA == manhattanB)
            {
                //If the current block is the same distance as the new position, select by yaw.
                Vec3d forward = new Vec3d((double)pos.getX() - Math.sin(lookingYaw / 180.0 * Math.PI), pos.getY(), (double)pos.getZ() + Math.cos(lookingYaw/ 180.0 * Math.PI));
                BlockPos nearestInFront = findNearestBlockFromPosition(pos, world, forward);
                if (nearestInFront != null)
                    pad = nearestInFront;
            }
        }
        return pad;
    }
}