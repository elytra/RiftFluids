package com.elytradev.riftfluids.FluidImpl;

import javax.annotation.Nullable;

/**
 * A tank is the unit of interaction with Fluid inventories.
 *
 * A reference implementation can be found at {@link FluidTank}.
 *
 * Ripped fully from Mezz's ActionType pr to Forge, modified for new FluidStack
 */
public interface IFluidTank {
    /**
     * @return FluidStack representing the fluid in the tank, null if the tank is empty.
     */
    FluidStack getFluid();

    /**
     * @return Current amount of fluid in the tank.
     */
    int getFluidAmount();

    /**
     * @return Capacity of this fluid tank.
     */
    int getCapacity();

    /**
     * Returns a wrapper object {@link FluidTankInfo } containing the capacity of the tank and the
     * FluidStack it holds.
     *
     * Should prevent manipulation of the IFluidTank. See {@link FluidTank}.
     *
     * @return State information for the IFluidTank.
     */
    FluidTankInfo getInfo();

    /**
     * @param resource FluidStack attempting to fill the tank.
     * @param action   If {@link ActionType#SIMULATE}, the fill will only be simulated.
     * @return Amount of fluid that was accepted by the tank.
     */
    FluidStack fill(FluidStack resource, ActionType action);

    /**
     * @param maxDrain Maximum amount of fluid to be removed from the container.
     * @param action   If {@link ActionType#SIMULATE}, the drain will only be simulated.
     * @return Amount of fluid that was removed from the tank.
     */
    @Nullable
    FluidStack drain(int maxDrain, ActionType action);
}