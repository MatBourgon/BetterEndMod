package net.matt_with_a_hat.WarpPadHelpers;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WarpPadTree
{
    //To save memory and time, we store a list of all chunks that contain warp pads. Within those chunks, we can access all warp pads contained
    Map<Long, ChunkPadList> chunks;

    public WarpPadTree()
    {
        chunks = new HashMap<Long, ChunkPadList>();
    }

    //Get the chunk id from its x and z positions
    long chunkToLong(int chunkX, int chunkZ)
    {
        //The key to the chunk map is the chunk's X and Z position merged into a 64 bit long from two 32 bit ints.
        return ((long)chunkX << 32) | chunkZ;
    }

    //Check if we currently have a chunk with that id stored
    boolean chunkExists(int chunkX, int chunkZ)
    {
        return chunks.containsKey(chunkToLong(chunkX, chunkZ));
    }

    //Get the list stored the chunk's position. If this chunk doesn't exist, we create an entry for it
    ChunkPadList getChunk(int chunkX, int chunkZ)
    {
        if (!chunkExists(chunkX, chunkZ))
        {
            chunks.put(chunkToLong(chunkX, chunkZ), new ChunkPadList(chunkX, chunkZ));
        }
        return chunks.get(chunkToLong(chunkX, chunkZ));
    }

    //We add a warp pad's position to the list at a given chunk
    public void addBlockToChunk(int chunkX, int chunkZ, BlockPos pos)
    {
        getChunk(chunkX, chunkZ).add(pos);
    }

    //We remove a warp pad's position to the list at a given chunk
    public void removeBlockFromChunk(int chunkX, int chunkZ, BlockPos pos)
    {
        getChunk(chunkX, chunkZ).remove(pos);
    }

    //Gets the nearest warp pad from a given position, taking looking direction into account if there must be a tie-breaker.
    @Nullable
    public BlockPos getNearestBlock(BlockPos pos, World world, float lookingYaw)
    {
        int chunkX = pos.getX() / 16;
        int chunkZ = pos.getZ() / 16;

        BlockPos nearestCandidate = null;

        //Check center chunk and directly adjecent chunks for the nearest warp pad
        if (chunkExists(chunkX, chunkZ))
        {
            nearestCandidate = getChunk(chunkX, chunkZ).findNearestBlock(pos, world, lookingYaw);
        }
        //Check adjecent chunks
        if (chunkExists(chunkX-1, chunkZ))
        {
            BlockPos t = getChunk(chunkX-1, chunkZ).findNearestBlock(pos, world, lookingYaw);
            if (t != null)
            {
                if (nearestCandidate == null)
                {
                    nearestCandidate = t;
                }
                else if (t.getManhattanDistance(pos) < nearestCandidate.getManhattanDistance(pos))
                    nearestCandidate = t;
            }
        }
        if (chunkExists(chunkX+1, chunkZ))
        {
            BlockPos t = getChunk(chunkX+1, chunkZ).findNearestBlock(pos, world, lookingYaw);
            if (t != null)
            {
                if (nearestCandidate == null)
                {
                    nearestCandidate = t;
                }
                else if (t.getManhattanDistance(pos) < nearestCandidate.getManhattanDistance(pos))
                    nearestCandidate = t;
            }
        }
        if (chunkExists(chunkX, chunkZ-1))
        {
            BlockPos t = getChunk(chunkX, chunkZ-1).findNearestBlock(pos, world, lookingYaw);
            if (t != null)
            {
                if (nearestCandidate == null)
                {
                    nearestCandidate = t;
                }
                else if (t.getManhattanDistance(pos) < nearestCandidate.getManhattanDistance(pos))
                    nearestCandidate = t;
            }
            
        }
        if (chunkExists(chunkX, chunkZ+1))
        {
            BlockPos t = getChunk(chunkX, chunkZ+1).findNearestBlock(pos, world, lookingYaw);
            if (t != null)
            {
                if (nearestCandidate == null)
                {
                    nearestCandidate = t;
                }
                else if (t.getManhattanDistance(pos) < nearestCandidate.getManhattanDistance(pos))
                    nearestCandidate = t;
            }
            
        }

        //If we found the nearest candidate, make sure it's within the range of -40 <= X <= 40, -40 <= Z <= 40, -20 <= Y <= 20, relative to our position
        if (nearestCandidate != null)
        {
            BlockPos dpos = pos.subtract(nearestCandidate);
            if (Math.abs(dpos.getX()) >= 40)
                nearestCandidate = null;
            else if (Math.abs(dpos.getZ()) >= 40)
                nearestCandidate = null;
            else if (Math.abs(dpos.getY()) >= 20)
                nearestCandidate = null;
        }
        return nearestCandidate;
    }
    
}