package com.elytradev.riftfluids.FluidImpl;

import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.NBTTagCompound;


public class FluidTank implements IFluidTank {
    protected FluidStack fluid;
    protected int capacity;

    public FluidTank(int capacity) {
        this(FluidStack.EMPTY, capacity);
    }

    public FluidTank(FluidStack stack, int capacity) {
        this.fluid = stack;
        this.capacity = capacity;
    }

    public FluidTank(Fluid fluid, int amount, int capacity) {
        this(new FluidStack(fluid, amount), capacity);
    }

    public FluidStack getFluid() {
        return fluid;
    }

    public int getFluidAmount() {
        return fluid.getAmount();
    }

    public int getCapacity() {
        return capacity;
    }

    public FluidStack fill(FluidStack fillFluid, ActionType action) {
        if (fillFluid.isEmpty()) return FluidStack.EMPTY;
        if (action == ActionType.SIMULATE) {
            if (fluid.isEmpty()) return new FluidStack(fillFluid.getFluid(), Math.min(capacity, fillFluid.getAmount()));

            if (!fluid.isFluidStackable(fillFluid)) return FluidStack.EMPTY;

            return new FluidStack(fluid.getFluid(), Math.min(capacity, fillFluid.getAmount()));
        }

        if (fluid.isEmpty()) {
            fluid.setFluid(fillFluid.getFluid());
            fluid.increase(Math.min(capacity, fillFluid.getAmount()));

            return new FluidStack(fillFluid, Math.min(capacity, fillFluid.getAmount()));
        }

        if (!fluid.isFluidStackable(fillFluid)) {
            return FluidStack.EMPTY;
        }

        int maxFill = capacity - fluid.getAmount();
        fluid.increase(Math.min(maxFill, fillFluid.getAmount()));

        return new FluidStack(fillFluid.getFluid(), Math.min(maxFill, fillFluid.getAmount()));
    }

    public FluidStack drain(int drainMax, ActionType action) {
        return drain(new FluidStack(fluid.getFluid(), drainMax), action);
    }

    public FluidStack drain(FluidStack drain, ActionType action) {
        if (!fluid.isFluidStackable(drain)) {
            return FluidStack.EMPTY;
        }

        int toDrain = drain.getAmount();
        if (fluid.getAmount() < toDrain) {
            toDrain = fluid.getAmount();
        }

        FluidStack drained = new FluidStack(drain, toDrain);
        if (action == ActionType.EXECUTE) {
            fluid.decrease(drain.getAmount());

            onContentsChanged();
        }

        return drained.isEmpty()? FluidStack.EMPTY : drained;

    }

    public void setFluid(FluidStack fluid) {
        this.fluid = fluid;
    }

    public FluidTank readFromNBT(NBTTagCompound nbt) {
        if (!nbt.hasKey("Empty")) {
            FluidStack fluid = FluidStack.loadFluidStackFromNBT(nbt);
            setFluid(fluid);
        }
        else {
            setFluid(FluidStack.EMPTY);
        }
        return this;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        if (fluid != null) {
            fluid.writeToNBT(nbt);
        }
        else {
            nbt.setString("Empty", "");
        }
        return nbt;
    }

    public void onContentsChanged() {

    }
}
