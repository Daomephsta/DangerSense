package com.leviathan143.dangersense.handlers;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.FOVUpdateEvent;

import com.leviathan143.dangersense.config.Config;
import com.leviathan143.dangersense.items.DSPotions;
import com.leviathan143.dangersense.lib.DSConstants;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

public class DangerSenseHandler 
{	
	private boolean hostileNearby;
	private Entity nearbyEntity;
	private static final ResourceLocation RES_DANGER_OVERLAY = new ResourceLocation(DSConstants.MODID + ":" + "misc/danger_overlay.png");
	//private static final Logger logger = LogManager.getLogger();
	private EntityPlayer player;
	private Minecraft mc = Minecraft.getMinecraft();

	@SubscribeEvent
	public void getPlayer(TickEvent.PlayerTickEvent event)
	{
		player = event.player;
	}

	@SubscribeEvent
	public void dangerSense(TickEvent.ServerTickEvent event)
	{	
		if (player != null && event.phase == Phase.START && player.ticksExisted % Config.scanTimeInTicks == 0)
		{	
			if (player.isPotionActive(DSPotions.dangerSense))
			{
				if(player.getActivePotionEffect(DSPotions.dangerSense).getDuration() < 20 && player.worldObj.isRemote)
				{
					player.addChatMessage(new ChatComponentTranslation("dangersense.potion.endMessage", new Object[0]));
				}
				double playerX = player.posX;
				double playerY = player.posY;
				double playerZ = player.posZ;
				AxisAlignedBB searchBox = AxisAlignedBB.getBoundingBox(playerX - Config.detectionRange, playerY - Config.lowerBounds, playerZ - Config.detectionRange,
						playerX + Config.detectionRange, playerY + Config.detectionRange, playerZ + Config.detectionRange);
				World world = MinecraftServer.getServer().getEntityWorld();
				@SuppressWarnings("unchecked")
				List<Entity> entitiesNearby = world.getEntitiesWithinAABB(EntityLiving.class, searchBox);
				int entityCount = entitiesNearby.size();
				if (!entitiesNearby.isEmpty())
				{
					for(int i = 0; i < entityCount; i++)
					{
						nearbyEntity = entitiesNearby.get(i);
						boolean hostile = nearbyEntity.isCreatureType(EnumCreatureType.monster, false) && !entitiesNearby.isEmpty(); 
						if(hostile)
						{
							hostileNearby = true;
						}
					}	
				}
				else
				{
					hostileNearby = false;
				}
			}
			else
			{
				hostileNearby = false;
			}	
		}

	}

	@SubscribeEvent
	public void renderDangerOverlay(TickEvent.RenderTickEvent event)
	{
<<<<<<< HEAD
		if(hostileNearby && !(mc.currentScreen instanceof GuiMainMenu))
=======
		/*if (OpenGlHelper.shadersSupported)
		{

		}
		else 
		{*/
			ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
			int k = scaledresolution.getScaledWidth();
        	int l = scaledresolution.getScaledHeight();
			if (event.phase == Phase.END && hostileNearby)
			{	
	        	
			}
		//}
		if (blindSpot == 7)
>>>>>>> origin/master
		{
			Tessellator tess = Tessellator.instance;
			TextureManager textureManager = mc.getTextureManager();
			ScaledResolution scaledRes = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
			textureManager.bindTexture(RES_DANGER_OVERLAY);
			GL11.glTranslated(0.0D, 0.0D, 0.0D);
			GL11.glEnable(GL11.GL_BLEND);
			tess.startDrawingQuads();
			tess.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
			tess.addVertexWithUV(0.0D, scaledRes.getScaledHeight(), 0.0D, 0.0D, 1.0D);
			tess.addVertexWithUV(scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), 0.0D, 1.0D, 1.0D);
			tess.addVertexWithUV(scaledRes.getScaledWidth(), 0.0D, 0.0D, 1.0D, 0.0D);
			tess.draw();
			GL11.glDisable(GL11.GL_BLEND);
		}
	}
}
