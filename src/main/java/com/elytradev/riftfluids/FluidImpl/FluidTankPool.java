package com.elytradev.riftfluids.FluidImpl;

public class FluidTankPool {
    protected FluidTank[] tanks;

    /**
     * Insert fluid into the first tank that can accept the entire FluidStack.
     * @param fluid the FluidStack to insert
     * @param action if {@link ActionType#SIMULATE}, the fill will only be simulated.
     * @return the amount of fluid successfully inserted: either the input stack or none
     */
    public FluidStack fillFirst(FluidStack fluid, ActionType action) {
        for (FluidTank tank : tanks) {
            FluidStack push = tank.fill(fluid, ActionType.SIMULATE);
            if (push.isFluidIdentical(fluid)) {
                tank.fill(fluid, ActionType.EXECUTE);
                return fluid;
            }
        }
        return FluidStack.EMPTY;
    }

    /**
     * Extract fluid from the first tank that contains the entire amount of fluid.
     * @param amount The amount of fluid to extract
     * @param action If {@link ActionType#SIMULATE}, the drain will only be simulated
     * @return Amount of fluid successfully extracted: either a FluidStack of the specefied amount or none
     */
    public FluidStack drainFirst(int amount, ActionType action) {
        for (FluidTank tank : tanks) {
            FluidStack fluid = new FluidStack(tank.getFluid(), amount);
            FluidStack pull = tank.drain(amount, ActionType.SIMULATE);
            if (pull.isFluidIdentical(fluid)) {
                tank.drain(fluid, ActionType.EXECUTE);
                return fluid;
            }
        }
        return FluidStack.EMPTY;
    }

    /**
     * Extract fluid from the first tank that contains the entire FluidStack.
     * @param fluid  The FluidStack to extract
     * @param action If {@link ActionType#SIMULATE}, the drain will only be simulated
     * @return Amount of fluid successfully extracted: either the input stack or none
     */
    public FluidStack drainFirst(FluidStack fluid, ActionType action) {
        for (FluidTank tank : tanks) {
            FluidStack pull = tank.drain(fluid, ActionType.SIMULATE);
            if (pull.isFluidIdentical(fluid)) {
                tank.drain(fluid, action);
                return fluid;
            }
        }
        return FluidStack.EMPTY;
    }

    /**
     * Insert fluid into the pool as a whole, starting from the first available tank and moving up
     * @param fluid  The FluidStack to insert
     * @param action If {@link ActionType#SIMULATE}, the fill will only be simulated
     * @return FluidStack with the amount of fluid successfully inserted across all tanks
     */
    public FluidStack fillFromFirst(FluidStack fluid, ActionType action) {
        FluidStack fluidLeft = fluid;
        for (FluidTank tank : tanks) {
            FluidStack push = tank.fill(fluidLeft, ActionType.SIMULATE);
            if (push.getAmount() < fluidLeft.getAmount()) {
                tank.fill(push, action);
                fluidLeft = push;
            } else {
                return fluid;
            }
        }
        return new FluidStack(fluid, fluid.getAmount()-fluidLeft.getAmount());
    }

    /**
     * Extract an amount of fluid from the pool as a whole, starting fro the first available tank and moving up
     * @param amount The amount of fluid to extract
     * @param action If {@link ActionType#SIMULATE}, the drain will only be simulated
     * @return The amount of fluid successfully extracted across all tanks
     */
    public int drainFromFirst(int amount, ActionType action) {
        int amountLeft = amount;
        for (FluidTank tank : tanks) {
            FluidStack pull = tank.drain(amount, ActionType.SIMULATE);
            if (pull.getAmount() < amountLeft) {
                tank.drain(pull, action);
                amountLeft = pull.getAmount();
            } else {
                return amount;
            }
        }
        return amount-amountLeft;
    }

    /**
     * Extract a specific fluid from the pool as a whole, starting from the first available tank and moving up
     * @param fluid  The FluidStack to extract
     * @param action If {@link ActionType#SIMULATE}, the drain will only be simulated
     * @return FluidStack with the amount of fluid successfully extracted across all tanks
     */
    public FluidStack drainFromFirst(FluidStack fluid, ActionType action) {
        FluidStack fluidLeft = fluid;
        for (FluidTank tank : tanks) {
            FluidStack pull = tank.drain(fluidLeft, ActionType.SIMULATE);
            if (pull.getAmount() < fluidLeft.getAmount()) {
                tank.drain(pull, action);
                fluidLeft = pull;
            } else {
                return fluid;
            }
        }
        return new FluidStack(fluid, fluid.getAmount()-fluidLeft.getAmount());
    }

    /**
     * Insert fluid into the last tank that can accept the entire FluidStack.
     * @param fluid the FluidStack to insert
     * @param action if {@link ActionType#SIMULATE}, the fill will only be simulated.
     * @return the amount of fluid successfully inserted: either the input stack or none
     */
    public FluidStack fillLast(FluidStack fluid, ActionType action) {
        for (int i = tanks.length - 1; i >= 0; i--) {
            FluidStack push = tanks[i].fill(fluid, ActionType.SIMULATE);
            if (push.isFluidIdentical(fluid)) {
                tanks[i].fill(fluid, ActionType.EXECUTE);
                return fluid;
            }
        }
        return FluidStack.EMPTY;
    }

    /**
     * Extract fluid from the last tank that contains the entire amount of fluid.
     * @param amount The amount of fluid to extract
     * @param action If {@link ActionType#SIMULATE}, the drain will only be simulated
     * @return Amount of fluid successfully extracted: either a FluidStack of the specefied amount or none
     */
    public FluidStack drainLast(int amount, ActionType action) {
        for (int i = tanks.length - 1; i >= 0; i--) {
            FluidStack fluid = new FluidStack(tanks[i].getFluid(), amount);
            FluidStack pull = tanks[i].drain(amount, ActionType.SIMULATE);
            if (pull.isFluidIdentical(fluid)) {
                tanks[i].drain(fluid, ActionType.EXECUTE);
                return fluid;
            }
        }
        return FluidStack.EMPTY;
    }

    /**
     * Extract fluid from the last tank that contains the entire FluidStack.
     * @param fluid  The FluidStack to extract
     * @param action If {@link ActionType#SIMULATE}, the drain will only be simulated
     * @return Amount of fluid successfully extracted: either the input stack or none
     */
    public FluidStack drainLast(FluidStack fluid, ActionType action) {
        for (int i = tanks.length - 1; i >= 0; i--) {
            FluidStack pull = tanks[i].drain(fluid, ActionType.SIMULATE);
            if (pull.isFluidIdentical(fluid)) {
                tanks[i].drain(fluid, action);
                return fluid;
            }
        }
        return FluidStack.EMPTY;
    }

    /**
     * Insert fluid into the pool as a whole, starting from the last available tank and moving down
     * @param fluid  The FluidStack to insert
     * @param action If {@link ActionType#SIMULATE}, the fill will only be simulated
     * @return FluidStack with the amount of fluid successfully inserted across all tanks
     */
    public FluidStack fillFromLast(FluidStack fluid, ActionType action) {
        FluidStack fluidLeft = fluid;
        for (int i = tanks.length - 1; i >= 0; i--) {
            FluidStack push = tanks[i].fill(fluidLeft, ActionType.SIMULATE);
            if (push.getAmount() < fluidLeft.getAmount()) {
                tanks[i].fill(push, action);
                fluidLeft = push;
            } else {
                return fluid;
            }
        }
        return new FluidStack(fluid, fluid.getAmount()-fluidLeft.getAmount());
    }

    /**
     * Extract an amount of fluid from the pool as a whole, starting fro the last available tank and moving down
     * @param amount The amount of fluid to extract
     * @param action If {@link ActionType#SIMULATE}, the drain will only be simulated
     * @return The amount of fluid successfully extracted across all tanks
     */
    public int drainFromLast(int amount, ActionType action) {
        int amountLeft = amount;
        for (int i = tanks.length - 1; i >= 0; i--) {
            FluidStack pull = tanks[i].drain(amount, ActionType.SIMULATE);
            if (pull.getAmount() < amountLeft) {
                tanks[i].drain(pull, action);
                amountLeft = pull.getAmount();
            } else {
                return amount;
            }
        }
        return amount-amountLeft;
    }

    /**
     * Extract a specific fluid from the pool as a whole, starting from the last available tank and moving down
     * @param fluid  The FluidStack to extract
     * @param action If {@link ActionType#SIMULATE}, the drain will only be simulated
     * @return FluidStack with the amount of fluid successfully extracted across all tanks
     */
    public FluidStack drainFromLast(FluidStack fluid, ActionType action) {
        FluidStack fluidLeft = fluid;
        for (int i = tanks.length - 1; i >= 0; i--) {
            FluidStack pull = tanks[i].drain(fluidLeft, ActionType.SIMULATE);
            if (pull.getAmount() < fluidLeft.getAmount()) {
                tanks[i].drain(pull, action);
                fluidLeft = pull;
            } else {
                return fluid;
            }
        }
        return new FluidStack(fluid, fluid.getAmount()-fluidLeft.getAmount());
    }

}
