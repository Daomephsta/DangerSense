package io.github.daomephsta.dangersense;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.IMob;
import net.minecraft.tags.EntityTypeTags;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.Tags.IOptionalNamedTag;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;

public class DangerSenseConfig 
{
    public static final Detection detection;
    public static class Detection
    {
        public final DoubleValue rangeXZ, rangeY;
        public final IntValue interval;
        private final BooleanValue heuristics;
        static final IOptionalNamedTag<EntityType<?>> BLACKLIST = 
            EntityTypeTags.createOptional(DangerSense.location("blacklist"));
        static final IOptionalNamedTag<EntityType<?>> WHITELIST = 
            EntityTypeTags.createOptional(DangerSense.location("whitelist"));
        static final IOptionalNamedTag<EntityType<?>> HOSTILE = 
            EntityTypeTags.createOptional(DangerSense.location("hostile"));
        
        Detection(Builder builder)
        {
            builder.comment("Detection uses a cylinder centred on the player")
                .push("detection");
            rangeXZ = builder
                .defineInRange("radius", 5.0D, 3.0D, 10.0D);
            rangeY = builder.comment("Distance of the cylinder top and bottom from the player's feet")
                .defineInRange("y", 2.0D, 1.0D, 3.0D);
            interval = builder.comment("Ticks between detection scans. Shorter intervals may impact performance.")
                .defineInRange("interval", 20, 10, 100);
            heuristics = builder.comment("Enables additional hostility detection mechanisms. Disable to use only"
                + " the entity type tag " + HOSTILE.getName())
                .define("heuristics", true);
            builder.pop();
        }
        
        public boolean isDetectable(Entity entity)
        {
            if (WHITELIST.isDefaulted())
                return !BLACKLIST.contains(entity.getType());
            else
                return WHITELIST.contains(entity.getType()) && !BLACKLIST.contains(entity.getType());
        }

        public boolean isHostile(Entity candidate)
        {
            if (candidate.getType().is(HOSTILE))
                return true;
            else
                return heuristics.get() && candidate instanceof IMob;
        }
    }
    public static final EyeCategory eyeOfHostility;
    public static class EyeCategory
    {
        public final IntValue degradationTicks;
        
        EyeCategory(Builder builder)
        {
            builder.push("eye_of_hostility");
            degradationTicks = builder.comment("How many ticks the Eye of Hostility takes to degrade. " + 
                "The default is 4 Minecraft days, and the maximum is over 3 realtime years.")
                .defineInRange("degradation.ticks", 4 * 24000, 1, Integer.MAX_VALUE);
            builder.pop();
        }
    }
    public static final Overlay overlay;
    public static class Overlay
    {
        public final IntValue red, green, blue;
        
        Overlay(Builder builder)
        {
            builder.push("overlay.colour");
            red = builder.comment("The red component of the overlay color")
                .defineInRange("red", 255, 0, 255);
            green = builder.comment("The green component of the overlay color")
                .defineInRange("green", 0, 0, 255);
            blue = builder.comment("The blue component of the overlay color")
                .defineInRange("blue", 0, 0, 255);
            builder.pop(2);
        }
    }
    private static final ForgeConfigSpec specification;
    static
    {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        detection = new Detection(builder);
        eyeOfHostility = new EyeCategory(builder);
        overlay = new Overlay(builder);
        specification = builder.build();
    }
    
    public static void register()
    {
        ModLoadingContext.get().registerConfig(Type.COMMON, specification);
    }
}
