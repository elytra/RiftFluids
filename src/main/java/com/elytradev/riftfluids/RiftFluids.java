package com.elytradev.riftfluids;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import org.dimdev.rift.listener.BlockAdder;
import org.dimdev.rift.listener.ItemAdder;
import org.dimdev.rift.listener.TileEntityTypeAdder;
import org.dimdev.rift.listener.client.TileEntityRendererAdder;

import java.util.Map;


public class RiftFluids implements BlockAdder, ItemAdder, TileEntityTypeAdder, TileEntityRendererAdder {

    public static final BlockFluidTank TANK = new BlockFluidTank(Block.Builder.create(Material.GLASS, MapColor.AIR).hardnessAndResistance(2f, 3f).soundType(SoundType.GLASS));

    public static TileEntityType<TileEntityTank> TANK_TE;

    @Override
    public void registerBlocks() {
        Block.registerBlock(new ResourceLocation("riftfluids:tank"), TANK);
    }

    @Override
    public void registerItems() {
        Item.registerItemBlock(TANK, ItemGroup.DECORATIONS);
    }

    @Override
    public void registerTileEntityTypes() {
        TANK_TE = TileEntityType.registerTileEntityType("riftfluids:tank", TileEntityType.Builder.create(TileEntityTank::new));
    }

    @Override
    public void addTileEntityRenderers(Map<Class<? extends TileEntity>, TileEntityRenderer<? extends TileEntity>> renderers) {
        renderers.put(TileEntityTank.class, new TankRenderer());
    }
}
