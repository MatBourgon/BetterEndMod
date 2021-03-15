package net.matt_with_a_hat.blocks;

import net.matt_with_a_hat.BetterEnd;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;

public class BlockWarpPadEntity extends BlockEntity {
    
    public BlockWarpPadEntity()
    {
        super(BetterEnd.WARP_PAD_ENTITY);

    }

    //Should only ever be called when loading, or network syncs
    @Override
    public void fromTag(BlockState state, CompoundTag tag)
    {
        super.fromTag(state, tag);

        //This is to add the block to the tree at load.
        BlockWarpPad.warpPadTree.addBlockToChunk(pos.getX()/16, pos.getZ()/16, pos);
    }
}
