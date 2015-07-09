package com.leviathan143.dangersense.handlers;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.FOVUpdateEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import com.leviathan143.dangersense.config.Config;
import com.leviathan143.dangersense.items.DSPotions;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

public class DangerSenseHandler 
{	
	private float blindSpot;
	private float localFov;
	private boolean hostileNearby;
	private Entity nearbyEntity;
	private static final ResourceLocation RES_DANGER_OVERLAY = new ResourceLocation("dangersense" + ":" + "misc/danger_overlay.png");
	private static final Logger logger = LogManager.getLogger();
	private EntityPlayer player;
	private Minecraft mc = Minecraft.getMinecraft();
	//private ResourceLocation shader =  new ResourceLocation();
	
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
			double playerX = player.posX;
			double playerY = player.posY;
			double playerZ = player.posZ;
			AxisAlignedBB searchBox = AxisAlignedBB.getBoundingBox(playerX - Config.detectionRange, playerY - 1, playerZ - Config.detectionRange,
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
		{
			
		}
	}


	@SubscribeEvent
	public void BlindSpot(FOVUpdateEvent event)
	{	if (event.entity == mc.thePlayer)
		{		localFov = mc.gameSettings.fovSetting;
				blindSpot = 360 - localFov;
		}
	}
}
