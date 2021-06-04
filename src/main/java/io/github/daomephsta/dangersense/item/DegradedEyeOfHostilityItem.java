package io.github.daomephsta.dangersense.item;

import io.github.daomephsta.dangersense.DangerSense;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

public class DegradedEyeOfHostilityItem extends Item
{
    public DegradedEyeOfHostilityItem(Properties properties)
    {
        super(properties);
    }
    
    @Override
    public ITextComponent getName(ItemStack stack)
    {
        return DangerSense.EYE_OF_HOSTILITY.getName(stack, super.getName(stack));
    }
}
