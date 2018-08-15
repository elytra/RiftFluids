package com.elytradev.riftfluids;

import com.elytradev.riftfluids.FluidImpl.ActionType;
import com.elytradev.riftfluids.FluidImpl.FluidStack;
import com.elytradev.riftfluids.FluidImpl.FluidTank;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;

public class TileEntityTank extends TileEntity {
    private FluidTank tank;

    public TileEntityTank(TileEntityType<?> type) {
        super(type);
        this.tank = new FluidTank(8000);
    }

    public TileEntityTank() {
        this(RiftFluids.TANK_TE);
    }

    public FluidTank getTank() {
        return this.tank;
    }

    public Fluid fillBucket() {
        FluidStack test = tank.drain(1000, ActionType.SIMULATE);
        if (test.getAmount() == 1000) {
            tank.drain(1000, ActionType.EXECUTE);
            return test.getFluid();
        }
        return Fluid.REGISTRY.getObject(new ResourceLocation("empty"));
    }

    public boolean emptyBucket(Fluid fluid) {
        FluidStack test = tank.fill(new FluidStack(fluid, 1000), ActionType.SIMULATE);
        if (test.getAmount() == 1000) {
            tank.fill(test, ActionType.EXECUTE);
            return true;
        }
        return false;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 3);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound tag = super.writeToNBT(compound);
        tag.setTag("Tank", tank.writeToNBT(new NBTTagCompound()));
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        tank.readFromNBT(compound.getCompoundTag("Tank"));
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
    }

}
