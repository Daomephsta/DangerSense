package io.github.daomephsta.dangersense;

import io.github.daomephsta.dangersense.item.DegradedEyeOfHostilityItem;
import io.github.daomephsta.dangersense.item.EyeOfHostilityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;

@Mod(DangerSense.MOD_ID)
public class DangerSense
{
    public static final String MOD_ID = "dangersense";
    @ObjectHolder(MOD_ID + ":eye_of_hostility")
    public static final EyeOfHostilityItem EYE_OF_HOSTILITY = null;
    @ObjectHolder(MOD_ID + ":degraded_eye_of_hostility")
    public static final Item DEGRADED_EYE_OF_HOSTILITY = null;
    public static final ITag<Item> HOSTILE_DROP = ItemTags.createOptional(location("eye_of_hostility_ingredient"));

    public DangerSense()
    {
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
        DangerSenseConfig.register();
    }

    public static ResourceLocation location(String id)
    {
        return new ResourceLocation(MOD_ID, id);
    }
   
    public static String langKey(String prefix, String suffix)
    {
        return prefix + '.' + MOD_ID + '.' + suffix;
    }
    
    private static <T extends IForgeRegistryEntry<T>> T withId(String id, T item)
    {
        item.setRegistryName(location(id));
        return item;
    }
    
    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().registerAll(
            withId("eye_of_hostility", new EyeOfHostilityItem(
                new Item.Properties().tab(ItemGroup.TAB_TOOLS).stacksTo(1))),
            withId("degraded_eye_of_hostility", new DegradedEyeOfHostilityItem(new Item.Properties()))
        );
    }

    @SubscribeEvent
    public void registerRecipeSerialisers(RegistryEvent.Register<IRecipeSerializer<?>> event)
    {
        event.getRegistry().register(
            withId("repair_eye_of_hostility", DegradedEyeOfHostilityRepairRecipe.SERIALISER));
    }
}
