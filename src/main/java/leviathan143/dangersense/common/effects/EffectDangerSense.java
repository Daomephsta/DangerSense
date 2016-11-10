package leviathan143.dangersense.common.effects;


import java.util.List;

import leviathan143.dangersense.common.DangerSenseMain.Constants;
import leviathan143.dangersense.common.capabilities.CapabilityDangerSense;
import leviathan143.dangersense.common.capabilities.IDangerSense;
import leviathan143.dangersense.common.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentTranslation;

public class EffectDangerSense extends Potion
{
	public static final String DETECT_ALL_HOSTILES = "detectHostiles";

	private static final ResourceLocation ICON_RES = new ResourceLocation(Constants.MODID, "textures/misc/dangerSense.png");

	private static boolean hostileNearby;

	public EffectDangerSense()
	{
		super(false, 0);
	}

	@Override
	public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha) 
	{
		renderEffect(x, y, effect, mc, 4);
	}

	@Override
	public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) 
	{
		renderEffect(x, y, effect, mc, 8);
		mc.fontRendererObj.drawStringWithShadow(I18n.format(effect.getEffectName())
				, (float)(x + 10 + 18), (float)(y + 6), 16777215);
		String s = I18n.format("entity." + mc.thePlayer.<IDangerSense>getCapability(CapabilityDangerSense.DANGERSENSE_CAP, null).getSense() + ".name");
		mc.fontRendererObj.drawStringWithShadow(s
				, (float)(x + 10 + 18), (float)(y + 6 + 10), 16777215);
		mc.fontRendererObj.drawStringWithShadow(Potion.getPotionDurationString(effect, 1.0F)
				, (float)(x + 33 + mc.fontRendererObj.getStringWidth(s)), (float)(y + 6 + 10), 8355711);
	}

	public void renderEffect(int x, int y, PotionEffect effect, Minecraft mc, float offset)
	{
		Tessellator tess = Tessellator.getInstance();
		VertexBuffer vtxBuf = tess.getBuffer();

		mc.getTextureManager().bindTexture(ICON_RES);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + offset, y + offset, 0);
		vtxBuf.begin(7, DefaultVertexFormats.POSITION_TEX);
		vtxBuf.pos(0.0D, 0.0D, 0.0D).tex(0.0D, 0.0D).endVertex();
		vtxBuf.pos(0.0D, 16, 0.0D).tex(0.0D, 1.0D).endVertex();
		vtxBuf.pos(16, 16, 0.0D).tex(1.0D, 1.0D).endVertex();
		vtxBuf.pos(16, 0.0D, 0.0D).tex(1.0D, 0).endVertex();
		tess.draw();
		GlStateManager.popMatrix();
	}

	@Override
	public void performEffect(EntityLivingBase livingIn, int amplifier) 
	{

		IDangerSense dangerSense = livingIn.getCapability(CapabilityDangerSense.DANGERSENSE_CAP, null);
		if(livingIn.getActivePotionEffect(ModEffects.dangerSense).getDuration() < 20 && livingIn.worldObj.isRemote)
		{
			livingIn.addChatMessage(new TextComponentTranslation("dangersense.potion.endMessage"));
			dangerSense.setSense("");
		}

		AxisAlignedBB searchBox = livingIn.getEntityBoundingBox().expand(Config.detectionRange, Config.lowerBounds, Config.detectionRange);
		List<EntityLivingBase> entitiesNearby = livingIn.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, searchBox);
		boolean hostileFound = false;
		for(EntityLivingBase living : entitiesNearby)
		{
			if(living instanceof EntityPlayer) continue;
			if(doesEntityMatchSense(living, dangerSense) && !Config.isEntityBlacklisted(living))
			{
				hostileFound = hostileNearby = true;
			}
		}
		if(!hostileFound || (livingIn.getActivePotionEffect(ModEffects.dangerSense).getDuration() <= 0)) 
			hostileNearby = false;
	}

	@Override
	public void removeAttributesModifiersFromEntity(EntityLivingBase living, AbstractAttributeMap attributeMapIn, int amplifier) 
	{
		if((living instanceof EntityPlayer))
		{
			living.getCapability(CapabilityDangerSense.DANGERSENSE_CAP, null).setSense("");
		}
		super.removeAttributesModifiersFromEntity(living, attributeMapIn, amplifier);
	}

	private boolean doesEntityMatchSense(EntityLivingBase living, IDangerSense dangerSense)
	{
		return (dangerSense.getSense() == DETECT_ALL_HOSTILES && living.isCreatureType(EnumCreatureType.MONSTER, false)) || EntityList.getEntityString(living).equals(dangerSense.getSense());
	}

	public static boolean isHostileNearby()
	{
		return hostileNearby;
	}

	@Override
	public boolean shouldRenderInvText(PotionEffect effect) 
	{
		return false;
	}

	@Override
	public boolean isReady(int duration, int amplifier) 
	{
		return duration % Config.scanTimeInTicks == 0;
	}
}
