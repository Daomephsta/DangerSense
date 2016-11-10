package leviathan143.dangersense.common.handlers;

import java.util.List;

import leviathan143.dangersense.common.config.Config;
import leviathan143.dangersense.common.effects.EffectDangerSense;
import leviathan143.dangersense.common.effects.ModEffects;
import leviathan143.dangersense.common.items.ItemSoulQuartzDagger;
import leviathan143.dangersense.common.items.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RenderHandler 
{
	private static final ResourceLocation MC_VIGNETTE_TEX = new ResourceLocation("textures/misc/vignette.png");
	private Minecraft mc = Minecraft.getMinecraft();

	@SubscribeEvent
	public void renderDangerOverlay(RenderGameOverlayEvent.Pre event)
	{
		if(mc.thePlayer.getActivePotionEffect(ModEffects.dangerSense) == null)
			return;
		if(event.getType() == ElementType.ALL && !(mc.currentScreen instanceof GuiMainMenu) && EffectDangerSense.isHostileNearby())
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

	//addInformation isn't powerful enough
	@SubscribeEvent
	public void handleTooltip(ItemTooltipEvent event)
	{	
		Item item = event.getItemStack().getItem();
		if(item == ModItems.SOUL_QUARTZ_DAGGER)
		{
			List<String> tooltip = event.getToolTip();
			tooltip.clear();
			String s = event.getItemStack().getDisplayName();

			if (event.getItemStack().hasDisplayName())
			{
				s = TextFormatting.ITALIC + s;
			}

			s = s + TextFormatting.RESET;

			if (event.isShowAdvancedItemTooltips())
			{
				String s1 = "";

				if (!s.isEmpty())
				{
					s = s + " (";
					s1 = ")";
				}
				s = s + String.format("#%04d%s", new Object[] {Integer.valueOf(Item.getIdFromItem(item)), s1});
			}
			tooltip.add(s);
			if(ItemSoulQuartzDagger.hasSoul(event.getItemStack()))
				tooltip.add("Stored Soul: " + I18n.format("entity." + ItemSoulQuartzDagger.getSoul(event.getItemStack()) + ".name"));
			tooltip.add("");
			tooltip.add(I18n.format("item.modifiers.mainhand"));
			tooltip.add(" 1.6 " + I18n.format("attribute.name." + SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName()));
			tooltip.add(" 3 " + I18n.format("attribute.name." + SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName()));
			if (event.isShowAdvancedItemTooltips())
			{
				if (event.getItemStack().isItemDamaged())
				{
					tooltip.add("Durability: " + (event.getItemStack().getMaxDamage() - event.getItemStack().getItemDamage()) + " / " + event.getItemStack().getMaxDamage());
				}

				tooltip.add(TextFormatting.DARK_GRAY + ((ResourceLocation)Item.REGISTRY.getNameForObject(item)).toString());

				if (event.getItemStack().hasTagCompound())
				{
					tooltip.add(TextFormatting.DARK_GRAY + "NBT: " + event.getItemStack().getTagCompound().getKeySet().size() + " tag(s)");
				}
			}
		}
	}
}
