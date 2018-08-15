package com.elytradev.riftfluids;

import com.elytradev.riftfluids.FluidImpl.FluidStack;
import net.minecraft.block.Block;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

public class BlockFluidTank extends Block implements IBucketPickupHandler, ILiquidContainer, ITileEntityProvider {


    public BlockFluidTank(Builder builder) {
        super(builder);
    }

    @Override
    public boolean hasTileEntity() {
        return true;
    }

    @Override
    public TileEntity getTileEntity(IBlockReader world) {
        return new TileEntityTank();
    }

    @Override
    public Fluid getFluid(IWorld world, BlockPos pos, IBlockState state) {
        TileEntityTank te = (TileEntityTank)world.getTileEntity(pos);
        return (te != null)? te.fillBucket() : Fluid.REGISTRY.getObject(new ResourceLocation("empty"));
    }

    @Override
    public boolean canContainFluid(IBlockReader world, BlockPos pos, IBlockState state, Fluid fluid) {
        TileEntityTank te = (TileEntityTank)world.getTileEntity(pos);

        FluidStack newFluid = new FluidStack(fluid, 1000);
        return te.getTank().getFluid().isFluidStackable(newFluid);
    }

    @Override
    public boolean receiveFluid(IWorld world, BlockPos pos, IBlockState state, IFluidState fluidState) {
        TileEntityTank te = (TileEntityTank)world.getTileEntity(pos);
        return (te != null) && te.emptyBucket(fluidState.getFluid());
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }
}
