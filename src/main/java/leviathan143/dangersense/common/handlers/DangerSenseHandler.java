package leviathan143.dangersense.common.handlers;

import java.util.List;

import leviathan143.dangersense.common.DangerSenseMain.Constants;
import leviathan143.dangersense.common.config.Config;
import leviathan143.dangersense.common.items.ModPotions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

import org.lwjgl.opengl.GL11;

public class DangerSenseHandler 
{	
	private static final ResourceLocation MC_VIGNETTE_TEX = new ResourceLocation("textures/misc/vignette.png");
	private Minecraft mc = Minecraft.getMinecraft();

	private boolean hostileNearby;

	@SubscribeEvent
	public void dangerSense(TickEvent.PlayerTickEvent event)
	{	
		if (event.phase == Phase.START && event.player.ticksExisted % Config.scanTimeInTicks == 0)
		{	
			if (event.player.isPotionActive(ModPotions.dangerSense))
			{
				if(event.player.getActivePotionEffect(ModPotions.dangerSense).getDuration() < 20 && event.player.worldObj.isRemote)
					event.player.addChatMessage(new TextComponentTranslation("dangersense.potion.endMessage", new Object[0]));
				World world = event.player.worldObj;

				AxisAlignedBB searchBox = event.player.getEntityBoundingBox().expand(Config.detectionRange, Config.lowerBounds, Config.detectionRange);
				List<EntityLivingBase> entitiesNearby = world.getEntitiesWithinAABB(EntityLivingBase.class, searchBox);
				boolean hostileFound = false;
				for(EntityLivingBase living : entitiesNearby)
				{
					if(living instanceof EntityPlayer) continue;
					if(living.isCreatureType(EnumCreatureType.MONSTER, false))
					{
						hostileFound = hostileNearby = true;
					}
				}
				if(!hostileFound || (event.player.getActivePotionEffect(ModPotions.dangerSense).getDuration() <= 0 && event.player.worldObj.isRemote)) 
					hostileNearby = false;
			}
			else if(hostileNearby)
				hostileNearby = false;
		}
	}

	@SubscribeEvent
	public void renderDangerOverlay(RenderGameOverlayEvent.Pre event)
	{
		if(event.getType() == ElementType.ALL && !(mc.currentScreen instanceof GuiMainMenu) && hostileNearby)
		{
			Tessellator tess = Tessellator.getInstance();
			VertexBuffer vtxBuf = tess.getBuffer();
			
			GlStateManager.disableDepth();
	        GlStateManager.depthMask(false);
	        GlStateManager.enableBlend();
	        GlStateManager.color(Config.overlayColourR, Config.overlayColourG, Config.overlayColourB, 1.0F);
	        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			
			mc.getTextureManager().bindTexture(MC_VIGNETTE_TEX);
			GlStateManager.pushMatrix();
			vtxBuf.begin(7, DefaultVertexFormats.POSITION_TEX);
			vtxBuf.pos(0.0D, 0.0D, 0.0D).tex(0.0D, 0.0D).endVertex();
			vtxBuf.pos(0.0D, mc.displayHeight, 0.0D).tex(0.0D, 1.0D).endVertex();
			vtxBuf.pos(mc.displayWidth, mc.displayHeight, 0.0D).tex(1.0D, 1.0D).endVertex();
			vtxBuf.pos(mc.displayWidth, 0.0D, 0.0D).tex(1.0D, 0).endVertex();
			tess.draw();
			GlStateManager.popMatrix();
			
			GlStateManager.enableDepth();
			GlStateManager.depthMask(true);
	        GlStateManager.disableBlend();
	        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		}
	}
}
