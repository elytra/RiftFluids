package com.elytradev.riftfluids.FluidImpl;

import net.minecraft.fluid.EmptyFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class FluidStack {
    public static final FluidStack EMPTY = new FluidStack((Fluid)null);
    private Fluid fluid;
    private int amount;
    private NBTTagCompound tag;
    private boolean isEmpty;

    public FluidStack(Fluid fluid) {
        this(fluid, 0, null);
    }

    public FluidStack(Fluid fluid, int amount) {
        this(fluid, amount, null);
    }

    public FluidStack(Fluid fluid, int amount, @Nullable NBTTagCompound nbt) {
        this.fluid = fluid;
        this.amount = amount;
        if (nbt != null) this.tag = nbt.copy();
        updateEmptyState();
    }

    public FluidStack(FluidStack stack, int amount) {
        this(stack.getFluid(), amount, stack.getTag());
    }

    private void updateEmptyState()
    {
        this.isEmpty = this.isEmpty();
    }

    public Fluid getFluid() {
        return fluid;
    }

    public int getAmount() {
        return amount;
    }

    public NBTTagCompound getTag() {
        return tag;
    }

    public boolean isEmpty() {
        if (this == EMPTY) return true;
        else if (!(fluid instanceof EmptyFluid || fluid == null)) {
            return amount <= 0;
        }
        else return true;
    }

    @Nullable
    public static FluidStack loadFluidStackFromNBT(NBTTagCompound nbt) {
        if (nbt == null) return null;
        if (!nbt.hasKey("Fluid", 8)) return null;
        String name = nbt.getString("Name");
        if (Fluid.REGISTRY.getObject(new ResourceLocation(name)) == null) {
            return null;
        }
        FluidStack stack = new FluidStack(Fluid.REGISTRY.getObject(new ResourceLocation(name)), nbt.getInteger("Amount"));

        if (nbt.hasKey("Tag")) {
            stack.tag = nbt.getCompoundTag("Tag");
        }
        return stack;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setString("Fluid", Fluid.REGISTRY.getNameForObject(getFluid()).toString());
        nbt.setInteger("Amount", getAmount());

        if (tag != null) {
            nbt.setTag("Tag", getTag());
        }
        return nbt;
    }

    public FluidStack copy() {
        return new FluidStack(getFluid(), getAmount(), getTag());
    }

    public boolean isFluidMatch(FluidStack compare) {
        return fluid.isSameAs(compare.getFluid());
    }

    public boolean isFluidLevelMatch(FluidStack compare) {
        return amount == compare.getAmount();
    }

    public boolean isFluidNBTMatch(FluidStack compare) {
        return tag == null ? compare.tag == null : compare.tag != null && tag.equals(compare.getTag());
    }

    public boolean doFluidsNBTMatch(FluidStack compare1, FluidStack compare2) {
        return compare1.isFluidNBTMatch(compare2);
    }

    public boolean isFluidStackable(FluidStack compare) {
        if (this.isEmpty() || compare.isEmpty()) return true;
        else return (isFluidMatch(compare) && isFluidNBTMatch(compare));
    }

    public boolean isFluidIdentical(FluidStack compare) {
        return isFluidStackable(compare) && isFluidLevelMatch(compare);
    }

    @Override
    public final boolean equals(Object o)
    {
        if (!(o instanceof FluidStack))
        {
            return false;
        }

        return isFluidStackable((FluidStack) o);
    }

    public void increase(int amount) {
        this.amount += amount;
    }

    public void decrease(int amount) {
        this.amount -= amount;
        if (this.amount <= 0) {
            this.fluid = Fluid.REGISTRY.getObject(new ResourceLocation("empty"));
            setAmount(0);
        }
    }

    public void setAmount(int amount) {
        if (amount >= 0) this.amount = amount;
    }

    public void setFluid(Fluid fluid) {
        this.fluid = fluid;
    }

}
