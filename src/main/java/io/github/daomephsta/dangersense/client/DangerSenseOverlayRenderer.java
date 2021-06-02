package io.github.daomephsta.dangersense.client;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;

import io.github.daomephsta.dangersense.DangerSense;
import io.github.daomephsta.dangersense.DangerSenseConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DangerSense.MOD_ID, value = Dist.CLIENT)
public class DangerSenseOverlayRenderer extends AbstractGui
{
    private static final ResourceLocation VIGNETTE_TEXTURE = new ResourceLocation("textures/misc/vignette.png");
    private static final Minecraft client = Minecraft.getInstance();
    private static boolean active = false;

    @SubscribeEvent
    public static void renderOverlay(RenderGameOverlayEvent.Pre event)
    {
        if (!active || event.getType() != ElementType.ALL) return;
        
        Tessellator tesselator = Tessellator.getInstance();
        BufferBuilder vtxBuf = tesselator.getBuilder();
        int screenHeight = client.getWindow().getGuiScaledHeight();
        int screenWidth = client.getWindow().getGuiScaledWidth();
        float red = DangerSenseConfig.overlay.red.get() / 255.0F,
            green = DangerSenseConfig.overlay.green.get() / 255.0F,
            blue = DangerSenseConfig.overlay.blue.get() / 255.0F;

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_COLOR, 1, 0);

        client.getTextureManager().bind(VIGNETTE_TEXTURE);
        vtxBuf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEX);
        vtxBuf.vertex(0.0D, 0.0D, 0.0D).color(red, green, blue, 1.0F).uv(0.0F, 0.0F).endVertex();
        vtxBuf.vertex(0.0D, screenHeight, 0.0D).color(red, green, blue, 1.0F).uv(0.0F, 1.0F).endVertex();
        vtxBuf.vertex(screenWidth, screenHeight, 0.0D).color(red, green, blue, 1.0F).uv(1.0F, 1.0F).endVertex();
        vtxBuf.vertex(screenWidth, 0.0D, 0.0D).color(red, green, blue, 1.0F).uv(1.0F, 0.0F).endVertex();
        tesselator.end();

        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
        RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
    }
    
    public static void setActive(boolean active)
    {
        DangerSenseOverlayRenderer.active = active;
    }
}
