package com.leviathan143.dangersense.lib;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiCreateFlatWorld;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

import org.lwjgl.opengl.GL11;

import com.leviathan143.dangersense.config.Config;

public class DangerSenseHandler 
{	
	private float blindSpot;
	private float localFov;
	private boolean hostileNearby;
	private static boolean guiOpen = false;
	private Entity nearbyEntity;
	private static final ResourceLocation RES_DANGER_OVERLAY = new ResourceLocation("dangersense" + ":" + "misc/danger_overlay.png");
	EntityPlayer player;
	
	@SubscribeEvent
	public void getPlayer(TickEvent.PlayerTickEvent event)
	{
		player = event.player;
	}
	
	@SubscribeEvent
	public void dangerSense(TickEvent.RenderTickEvent event)
	{	
		if (player != null && event.phase == Phase.START && player.ticksExisted % Config.scanTimeInTicks == 0)
		{	
			double playerX = player.posX;
			double playerY = player.posY;
			double playerZ = player.posZ;
			AxisAlignedBB searchBox = new AxisAlignedBB(playerX - Config.detectionRange, playerY - 1, playerZ - Config.detectionRange,
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
					boolean hostile = nearbyEntity.isCreatureType(EnumCreatureType.MONSTER, false) && !entitiesNearby.isEmpty();
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
	}
	
	@SubscribeEvent
	public void renderDangerOverlay(TickEvent.RenderTickEvent event)
	{
		ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        int k = scaledresolution.getScaledWidth();
        int l = scaledresolution.getScaledHeight();
		if (event.phase == Phase.END && hostileNearby && !guiOpen)
		{	

		}
		if (blindSpot == 7)
		{
			
		}
	}

	@SubscribeEvent
	public void BlindSpot(FOVUpdateEvent event)
	{	if (event.entity == Minecraft.getMinecraft().thePlayer)
		{		localFov = Minecraft.getMinecraft().gameSettings.fovSetting;
				blindSpot = 360 - localFov;
		}
	}
	
	@SubscribeEvent
	public void fixOverlayArtifact(GuiOpenEvent event)
	{
		System.out.println(event.gui);
		if (!(event.gui instanceof GuiMainMenu || event.gui instanceof GuiCreateWorld 
				|| event.gui instanceof GuiCreateFlatWorld || event.gui instanceof GuiSelectWorld 
				|| event.gui instanceof GuiDownloadTerrain || event.gui instanceof GuiIngameMenu 
				|| event.gui instanceof GuiContainerCreative || event.gui instanceof GuiInventory || event.gui == null))
		{
			guiOpen = true;
		}
		if(event.gui == null)
		{
			guiOpen = false;
		}
	}

	public static void setGuiOpen()
	{
		guiOpen = false;
	}
}
