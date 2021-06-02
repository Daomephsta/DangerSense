package io.github.daomephsta.dangersense;

import io.github.daomephsta.dangersense.util.Chat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.StringNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

public class SoulQuartzDaggerItem extends SwordItem
{
    private static final String TAG_ENTITY_SOUL = "soul_type";
    private static final ResourceLocation SOUL_GETTER_ID = new ResourceLocation("has_soul");
    private static final IItemPropertyGetter SOUL_GETTER = (stack, world, entity) -> hasSoul(stack) ? 1.0F : 0.0F;
    public SoulQuartzDaggerItem(Properties properties)
    {
        super(ItemTier.DIAMOND, 0, -2.6F, properties);
        ItemModelsProperties.register(this, SOUL_GETTER_ID, SOUL_GETTER);
    }
    
    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        if (player.hasEffect(DangerSense.EFFECT) && 
            DangerSenseData.get(player).filter(sense -> sense.getTarget() == getSoul(stack)).isPresent())
        {
            return ActionResult.pass(stack);
        }
        else if(hasSoul(stack))
        {
            player.startUsingItem(hand);
            return ActionResult.success(stack);
        }
        return ActionResult.pass(stack);
    }
    
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity user)
    {
        if (target.isDeadOrDying()) 
            setSoul(stack, target);
        return super.hurtEnemy(stack, target, user);
    }
    
    @Override
    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity entity)
    {
        DangerSenseData.get(entity)
            .filter(dangerSense -> dangerSense.getTarget() != getSoul(stack))
            .ifPresent(dangerSense ->
            {
                dangerSense.setTarget(getSoul(stack));
                entity.removeEffect(DangerSense.EFFECT);
                entity.addEffect(new EffectInstance(DangerSense.EFFECT, 
                    DangerSenseConfig.potion.duration.get() * 20));
                stack.getTag().remove(TAG_ENTITY_SOUL);
            });
        return stack;
    }
    
    @Override
    public ActionResultType interactLivingEntity(ItemStack stack, 
        PlayerEntity player, LivingEntity target, Hand hand)
    {
        if(player.isCreative()) 
        {
            Chat.sendMessage(player, new StringTextComponent(target.getType().getRegistryName().toString()));
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack)
    {
        return UseAction.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack)
    {
        return 32;
    }

    public static EntityType<?> getSoul(ItemStack stack)
    {
        if (!hasSoul(stack))
            return null;
        return ForgeRegistries.ENTITIES.getValue(new ResourceLocation(stack.getTag().getString(TAG_ENTITY_SOUL)));
    }

    public static void setSoul(ItemStack stack, Entity entity)
    {
        stack.addTagElement(TAG_ENTITY_SOUL, StringNBT.valueOf(entity.getType().getRegistryName().toString()));
    }
    
    public static boolean hasSoul(ItemStack stack)
    {
        return stack.hasTag() && stack.getTag().contains(TAG_ENTITY_SOUL);
    }
}
