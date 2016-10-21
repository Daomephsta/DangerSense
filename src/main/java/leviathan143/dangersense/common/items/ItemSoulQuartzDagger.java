package leviathan143.dangersense.common.items;

import javax.annotation.Nullable;

import com.google.common.collect.Multimap;

import leviathan143.dangersense.common.capabilities.CapabilityDangerSense;
import leviathan143.dangersense.common.capabilities.IDangerSense;
import leviathan143.dangersense.common.config.Config;
import leviathan143.dangersense.common.effects.EffectDangerSense;
import leviathan143.dangersense.common.effects.ModEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSoulQuartzDagger extends Item
{
	private static final String TAG_ENTITY_SOUL = "SoulType";
	private static final ResourceLocation SOUL_GETTER_ID = new ResourceLocation("hasSoul");
	private static final IItemPropertyGetter SOUL_GETTER = new IItemPropertyGetter()
	{
		@SideOnly(Side.CLIENT)
		public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
		{
			return hasSoul(stack) ? 1.0F : 0.0F;
		}
	};

	public ItemSoulQuartzDagger() 
	{
		setMaxStackSize(1);
		setMaxDamage(9);
		this.addPropertyOverride(SOUL_GETTER_ID, SOUL_GETTER);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) 
	{
		if(!(entityLiving instanceof EntityPlayer)) return stack;
		IDangerSense dangerSense = entityLiving.<IDangerSense>getCapability(CapabilityDangerSense.DANGERSENSE_CAP, null);
		if(dangerSense.getSense().equals(getSoul(stack))) return stack;
		else if(hasSoul(stack))
		{
			dangerSense.setSense(getSoul(stack));
			entityLiving.removeActivePotionEffect(ModEffects.dangerSense);
			entityLiving.addPotionEffect(new PotionEffect(ModEffects.dangerSense, Config.potionDuration * 20));
			if(worldIn.isRemote)			
			{
				entityLiving.addChatMessage(new TextComponentTranslation("dangersense.potion.startMessage"));
			}
			stack.damageItem(1, entityLiving);
			return stack;
		}
		return stack;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		IDangerSense dangerSense = playerIn.<IDangerSense>getCapability(CapabilityDangerSense.DANGERSENSE_CAP, null);
		if(dangerSense.getSense().equals(getSoul(itemStackIn)) && playerIn.getActivePotionEffect(ModEffects.dangerSense) != null) 
			return ActionResult.newResult(EnumActionResult.PASS, itemStackIn);
		else if(hasSoul(itemStackIn))
		{
			playerIn.setActiveHand(hand);
			return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
		}
		return ActionResult.newResult(EnumActionResult.PASS, itemStackIn);
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target,
			EnumHand hand) 
	{
		if(playerIn.capabilities.isCreativeMode) 
		{
			if(playerIn.worldObj.isRemote) playerIn.addChatMessage(new TextComponentString(EntityList.getEntityString(target)));
			return true;
		}
		return false;
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) 
	{
		target.attackEntityFrom(DamageSourceSoulExtraction.causeSoulExtractionDamage(attacker), 3.0F);
		return true;
	}

	@Override
	public boolean hasEffect(ItemStack stack) 
	{
		return EffectDangerSense.DETECT_ALL_HOSTILES.equals(getSoul(stack));
	}

	@Override
	public EnumAction getItemUseAction(ItemStack itemstack)
	{
		return EnumAction.DRINK;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack itemstack)
	{
		return 32;
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) 
	{
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
		if (slot == EntityEquipmentSlot.MAINHAND)
		{
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", -1.0F + 0.001F, 0));
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4000000953674316D, 0));
		}
		return multimap;
	}

	public static String getSoul(ItemStack stack)
	{
		if(!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		return stack.getTagCompound().getString(TAG_ENTITY_SOUL);
	}

	public static void setSoul(ItemStack stack, Entity entity)
	{
		setSoul(stack, EntityList.getEntityString(entity));
	}
	
	public static void setSoul(ItemStack stack, String entityName)
	{
		if(!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setString(TAG_ENTITY_SOUL, entityName);
	}

	public static boolean hasSoul(ItemStack stack)
	{
		if(!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		return stack.getTagCompound().hasKey(TAG_ENTITY_SOUL);
	}
}
