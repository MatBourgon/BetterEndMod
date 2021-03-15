package net.matt_with_a_hat.blocks;

import net.matt_with_a_hat.WarpPadHelpers.WarpPadTree;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class BlockWarpPad extends Block implements BlockEntityProvider {
    
    public static WarpPadTree warpPadTree = new WarpPadTree();

    public BlockWarpPad(Settings settings)
    {
        super(settings);
    }
    
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context)
    {
        return VoxelShapes.cuboid(0f, 0f, 0f, 1f, 0.5f, 1f);
    }

    @Override
    public boolean hasSidedTransparency(BlockState state) {
        return true;
    } 

    @Override
    public BlockEntity createBlockEntity(BlockView view)
    {
        return new BlockWarpPadEntity();
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify)
    {
        super.onBlockAdded(state, world, pos, oldState, notify);
        warpPadTree.addBlockToChunk(pos.getX()/16, pos.getZ()/16, pos); //Add to list
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        super.onStateReplaced(state, world, pos, newState, moved);
        if (this.hasBlockEntity() && !state.isOf(newState.getBlock())) {
           warpPadTree.removeBlockFromChunk(pos.getX()/16, pos.getZ()/16, pos); //if destroyed, from from list
        }
  
    }

}
