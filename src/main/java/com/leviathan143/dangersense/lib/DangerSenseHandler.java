package com.leviathan143.dangersense.lib;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.GuiOpenEvent;

import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

public class DangerSenseHandler 
{	
	private float blindSpot;
	private float localFov;
	private double Xcoord;
	private boolean hostileNearby;
	private String nearbyEntity;
	private static final ResourceLocation RES_DANGER_OVERLAY = new ResourceLocation("dangersense" + ":" + "misc/danger_overlay.png");
	private static Minecraft mc;
	private DSUtils d = new DSUtils();
	EntityPlayer player;
	
	@SubscribeEvent
	public void getPlayer(TickEvent.PlayerTickEvent event)
	{
		player = event.player;
	}
	
	@SubscribeEvent
	public void dangerSense(TickEvent.RenderTickEvent event)
	{	
		if (player != null && event.phase == Phase.START && player.ticksExisted % 20 == 0)
		{	
			double playerX = player.posX;
			double playerY = player.posY;
			double playerZ = player.posZ;
			double distance = 5.0; 
			AxisAlignedBB searchBox = AxisAlignedBB.getBoundingBox(playerX - distance,playerY - 1,playerZ - distance,
				playerX + distance,playerY + distance,playerZ + distance);
			World world = MinecraftServer.getServer().getEntityWorld();
			List entitiesNearby = world.getEntitiesWithinAABB(EntityLiving.class, searchBox);
				int entityCount = entitiesNearby.size();
				if (!entitiesNearby.isEmpty())
			{
				for(int i = 0; i < entityCount; i++)
				{
					nearbyEntity = d.getEntityFromEntitiesNearby(entitiesNearby, i);
					BiomeGenBase[] biomes = BiomeGenBase.getBiomeGenArray();
					for(int b = 0; b < biomes.length; b++)
					{
						List protomonsterList = biomes[1].getSpawnableList(EnumCreatureType.monster);
						List monsterList = new ArrayList();
						for(int m = 0; m < protomonsterList.size() ; m++)
						{
							String monster = d.getEntityFromSpawnableList(protomonsterList, m);
							monsterList.add(monster);
						}
						if(monsterList.contains(nearbyEntity) && !entitiesNearby.isEmpty())
						{
							hostileNearby = true;
						}
						if(!(monsterList.contains(nearbyEntity)))
						{
							hostileNearby = false;  
						}
						
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
	public void Test(TickEvent.RenderTickEvent event)
	{
		ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        int k = scaledresolution.getScaledWidth();
        int l = scaledresolution.getScaledHeight();
		if (event.phase == Phase.END && hostileNearby)
		{	
	        GL11.glDepthMask(false);
	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	        GL11.glDisable(GL11.GL_ALPHA_TEST);
	        Minecraft.getMinecraft().getTextureManager().bindTexture(RES_DANGER_OVERLAY);
	        Tessellator tessellator = Tessellator.instance;
	        tessellator.startDrawingQuads();
	        tessellator.addVertexWithUV(0.0D, (double)l, -90.0D, 0.0D, 1.0D);
	        tessellator.addVertexWithUV((double)k, (double)l, -90.0D, 1.0D, 1.0D);
	        tessellator.addVertexWithUV((double)k, 0.0D, -90.0D, 1.0D, 0.0D);
	        tessellator.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
	        tessellator.draw();
	        GL11.glDepthMask(true);
	        GL11.glEnable(GL11.GL_ALPHA_TEST);
	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	        
		}
	}

	@SubscribeEvent
	public void BlindSpot(FOVUpdateEvent event)
	{	if (event.entity == Minecraft.getMinecraft().thePlayer)
		{		localFov = Minecraft.getMinecraft().gameSettings.fovSetting;
				blindSpot = 360 - localFov;
		}
	}
}