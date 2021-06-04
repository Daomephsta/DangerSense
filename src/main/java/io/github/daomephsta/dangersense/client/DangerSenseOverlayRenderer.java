package io.github.daomephsta.dangersense.client;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;

import io.github.daomephsta.dangersense.DangerSense;
import io.github.daomephsta.dangersense.DangerSenseConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DangerSense.MOD_ID, value = Dist.CLIENT)
public class DangerSenseOverlayRenderer extends AbstractGui
{
    private static final ResourceLocation VIGNETTE_TEXTURE = new ResourceLocation("textures/misc/vignette.png");
    private static final Minecraft client = Minecraft.getInstance();
    // Buffer active eyes between detection ticks
    private static final Set<ItemStack> activeEyesBuffer = new HashSet<>();
    
    public static boolean isEyeActive(ItemStack stack)
    {
        return activeEyesBuffer.contains(stack);
    }
    
    @SubscribeEvent
    public static void tickDetection(ClientTickEvent event)
    {
        if (client.level == null)
        {
            activeEyesBuffer.clear();
            return;
        }   
        if (client.level.getGameTime() % DangerSenseConfig.detection.interval.get() != 0)
            return;
        
        activeEyesBuffer.clear();
        double radius = DangerSenseConfig.detection.rangeXZ.get();
        AxisAlignedBB searchBox = client.player.getBoundingBox().inflate(
            radius, DangerSenseConfig.detection.rangeY.get(), radius);
        for (int slot = 0; slot < client.player.inventory.getContainerSize(); slot++)
        {
            ItemStack stack = client.player.inventory.getItem(slot);
            if (stack.getItem() == DangerSense.EYE_OF_HOSTILITY) 
                searchForFocus(stack, searchBox, radius);
        }
    }

    private static void searchForFocus(ItemStack stack, AxisAlignedBB searchBox, double radius)
    {
        if (DangerSense.EYE_OF_HOSTILITY.hasFocus(stack))
        {
            DangerSense.EYE_OF_HOSTILITY.getFocus(stack).ifPresent(focus ->
            {
                List<?> found = client.level.getEntities(focus, searchBox, 
                    candidate -> isDetectable(client.player, candidate, radius));
                if (!found.isEmpty())
                    activeEyesBuffer.add(stack);
            });
        }
        else
        {
            List<?> found = client.level.getEntities(client.player, searchBox, candidate -> 
                isDetectable(client.player, candidate, radius) && DangerSenseConfig.detection.isHostile(candidate));
            if (!found.isEmpty())
                activeEyesBuffer.add(stack);
        }
    }
    
    private static boolean isDetectable(Entity owner, Entity entity, double radius)
    {
        return DangerSenseConfig.detection.isDetectable(entity) && entity.distanceToSqr(owner) <= radius * radius;
    }

    @SubscribeEvent
    public static void renderOverlay(RenderGameOverlayEvent.Pre event)
    {
        if (activeEyesBuffer.isEmpty() || event.getType() != ElementType.ALL) return;
        
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
}
