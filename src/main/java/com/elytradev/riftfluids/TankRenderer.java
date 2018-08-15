package com.elytradev.riftfluids;

import com.elytradev.riftfluids.FluidImpl.FluidStack;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.fluid.Fluid;
import org.lwjgl.opengl.GL11;

public class TankRenderer extends TileEntityRenderer<TileEntityTank> {

    @Override
    public void func_199341_a(TileEntityTank te, double x, double y, double z, float partialTicks, int destroyStage) {
        IBlockState ibs = te.getWorld().getBlockState(te.getPos());
        if (ibs.getBlock() != RiftFluids.TANK) return;
        FluidStack fluid = te.getTank().getFluid();
        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.translate(x, y, z);
        if (!fluid.isEmpty()) {
            TextureAtlasSprite tas = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(Fluid.REGISTRY.getNameForObject(te.getTank().getFluid().getFluid()).toString());
            bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.enableTexture2D();
            GlStateManager.color(1, 1, 1);
            renderCube(2.25f, 0, 2.25f, 11.5f, 15.5f * (fluid.getAmount() / (float) te.getTank().getCapacity()), 11.5f, tas, true, true, true);
            GlStateManager.disableBlend();
        }

        GlStateManager.popMatrix();
        super.func_199341_a(te, x, y, z, partialTicks, destroyStage);
    }

    private void renderCube(float x, float y, float z, float w, float h, float d, TextureAtlasSprite tas, boolean renderTop, boolean renderBottom, boolean renderSides) {
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder vb = tess.getBuffer();

        float minVX = tas.getInterpolatedV(x);
        float maxVX = tas.getInterpolatedV(x+w);
        float minVY = tas.getInterpolatedV(y);
        float maxVY = tas.getInterpolatedV(y+h);

        float minUX = tas.getInterpolatedU(x);
        float maxUX = tas.getInterpolatedU(x+w);
        float minUZ = tas.getInterpolatedU(z);
        float maxUZ = tas.getInterpolatedU(z+d);

        float s = 1/16f;

        vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
        if (renderSides) {
            vb.pos((x+0)*s, (y+h)*s, (z+0)*s).tex(minUX, maxVY).normal(0, 0, -1).endVertex();
            vb.pos((x+w)*s, (y+h)*s, (z+0)*s).tex(maxUX, maxVY).normal(0, 0, -1).endVertex();
            vb.pos((x+w)*s, (y+0)*s, (z+0)*s).tex(maxUX, minVY).normal(0, 0, -1).endVertex();
            vb.pos((x+0)*s, (y+0)*s, (z+0)*s).tex(minUX, minVY).normal(0, 0, -1).endVertex();

            vb.pos((x+w)*s, (y+h)*s, (z+d)*s).tex(maxUX, maxVY).normal(0, 0, 1).endVertex();
            vb.pos((x+0)*s, (y+h)*s, (z+d)*s).tex(minUX, maxVY).normal(0, 0, 1).endVertex();
            vb.pos((x+0)*s, (y+0)*s, (z+d)*s).tex(minUX, minVY).normal(0, 0, 1).endVertex();
            vb.pos((x+w)*s, (y+0)*s, (z+d)*s).tex(maxUX, minVY).normal(0, 0, 1).endVertex();


            vb.pos((x+0)*s, (y+0)*s, (z+d)*s).tex(maxUZ, minVY).normal(-1, 0, 0).endVertex();
            vb.pos((x+0)*s, (y+h)*s, (z+d)*s).tex(maxUZ, maxVY).normal(-1, 0, 0).endVertex();
            vb.pos((x+0)*s, (y+h)*s, (z+0)*s).tex(minUZ, maxVY).normal(-1, 0, 0).endVertex();
            vb.pos((x+0)*s, (y+0)*s, (z+0)*s).tex(minUZ, minVY).normal(-1, 0, 0).endVertex();

            vb.pos((x+w)*s, (y+0)*s, (z+0)*s).tex(minUZ, minVY).normal(1, 0, 0).endVertex();
            vb.pos((x+w)*s, (y+h)*s, (z+0)*s).tex(minUZ, maxVY).normal(1, 0, 0).endVertex();
            vb.pos((x+w)*s, (y+h)*s, (z+d)*s).tex(maxUZ, maxVY).normal(1, 0, 0).endVertex();
            vb.pos((x+w)*s, (y+0)*s, (z+d)*s).tex(maxUZ, minVY).normal(1, 0, 0).endVertex();
        }

        if (renderBottom) {
            vb.pos((x+0)*s, (y+0)*s, (z+0)*s).tex(minUZ, minVX).normal(0, -1, 0).endVertex();
            vb.pos((x+w)*s, (y+0)*s, (z+0)*s).tex(minUZ, maxVX).normal(0, -1, 0).endVertex();
            vb.pos((x+w)*s, (y+0)*s, (z+d)*s).tex(maxUZ, maxVX).normal(0, -1, 0).endVertex();
            vb.pos((x+0)*s, (y+0)*s, (z+d)*s).tex(maxUZ, minVX).normal(0, -1, 0).endVertex();
        }

        if (renderTop) {
            vb.pos((x+0)*s, (y+h)*s, (z+0)*s).tex(minUZ, minVX).normal(0, 1, 0).endVertex();
            vb.pos((x+0)*s, (y+h)*s, (z+d)*s).tex(maxUZ, minVX).normal(0, 1, 0).endVertex();
            vb.pos((x+w)*s, (y+h)*s, (z+d)*s).tex(maxUZ, maxVX).normal(0, 1, 0).endVertex();
            vb.pos((x+w)*s, (y+h)*s, (z+0)*s).tex(minUZ, maxVX).normal(0, 1, 0).endVertex();

        }
        tess.draw();
    }
}
