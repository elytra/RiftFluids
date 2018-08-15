package com.elytradev.riftfluids.FluidImpl;

import javax.annotation.Nullable;

/**
 * Wrapper class used to encapsulate information about an IFluidTank.
 *
 * Ripped fully from Forge, removing fluid nullability
 */
public final class FluidTankInfo {
    public final FluidStack fluid;
    public final int capacity;

    public FluidTankInfo(FluidStack fluid, int capacity) {
        this.fluid = fluid;
        this.capacity = capacity;
    }

    public FluidTankInfo(IFluidTank tank) {
        this.fluid = tank.getFluid();
        this.capacity = tank.getCapacity();
    }
}