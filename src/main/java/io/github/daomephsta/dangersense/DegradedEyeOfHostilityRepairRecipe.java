package io.github.daomephsta.dangersense;

import com.google.gson.JsonObject;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class DegradedEyeOfHostilityRepairRecipe extends ShapelessRecipe
{   
    public static final IRecipeSerializer<DegradedEyeOfHostilityRepairRecipe> SERIALISER = new Serialiser();
    
    DegradedEyeOfHostilityRepairRecipe(ResourceLocation id, 
        String group, ItemStack output, NonNullList<Ingredient> inputs)
    {
        super(id, group, output, inputs);
    }

    @Override
    public ItemStack assemble(CraftingInventory inventory)
    {
        for (int slot = 0; slot < inventory.getContainerSize(); slot++)
        {
            ItemStack stack = inventory.getItem(slot);
            if (stack.getItem() == DangerSense.DEGRADED_EYE_OF_HOSTILITY)
            {
                ItemStack eye = new ItemStack(DangerSense.EYE_OF_HOSTILITY);
                eye.setTag(stack.getTag().copy());
                return eye;
            }
        }
        throw new IllegalStateException("Failed to find degraded eye of hostility. This should be impossible.");
    }
    
    @Override
    public IRecipeSerializer<?> getSerializer()
    {
        return SERIALISER;
    }
    
    public boolean isSpecial() 
    {
       return true;
    }

    public ItemStack getResultItem() 
    {
       return ItemStack.EMPTY;
    }
    
    private static class Serialiser extends ForgeRegistryEntry<IRecipeSerializer<?>>  
        implements IRecipeSerializer<DegradedEyeOfHostilityRepairRecipe>
    {
        @Override
        public DegradedEyeOfHostilityRepairRecipe fromJson(ResourceLocation id, JsonObject json)
        {
            ShapelessRecipe shapeless = IRecipeSerializer.SHAPELESS_RECIPE.fromJson(id, json);
            return new DegradedEyeOfHostilityRepairRecipe(id, shapeless.getGroup(), 
                shapeless.getResultItem(), shapeless.getIngredients());
        }

        @Override
        public DegradedEyeOfHostilityRepairRecipe fromNetwork(ResourceLocation id, PacketBuffer bytes)
        {
            ShapelessRecipe shapeless = IRecipeSerializer.SHAPELESS_RECIPE.fromNetwork(id, bytes);
            return new DegradedEyeOfHostilityRepairRecipe(id, shapeless.getGroup(), 
                shapeless.getResultItem(), shapeless.getIngredients());
        }

        @Override
        public void toNetwork(PacketBuffer bytes, DegradedEyeOfHostilityRepairRecipe recipe)
        {
            IRecipeSerializer.SHAPELESS_RECIPE.toNetwork(bytes, recipe);
        }
    }
}
