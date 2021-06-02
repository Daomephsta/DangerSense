package io.github.daomephsta.dangersense;

import java.util.List;

import io.github.daomephsta.dangersense.client.DangerSenseOverlayRenderer;
import io.github.daomephsta.dangersense.util.Chat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TranslationTextComponent;

public class DangerSenseEffect extends Effect
{
    public DangerSenseEffect()
    {
        super(EffectType.BENEFICIAL, 0xF00000);
    }
    
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier)
    {
        return duration % DangerSenseConfig.detection.interval.get() == 0;
    }
    
    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier)
    {
        // Detection is clientside. No point adding server load when the resulting overlay is client-side only.
        if (!entity.level.isClientSide()) return;
        DangerSenseData.get(entity).ifPresent(dangerSense -> 
        {
            if (dangerSense.getTarget() == null) return;
            double radius = DangerSenseConfig.detection.rangeXZ.get();
            AxisAlignedBB searchBox = entity.getBoundingBox().inflate(
                radius, DangerSenseConfig.detection.rangeY.get(), radius);
            List<? extends Entity> found = entity.level.getEntities(dangerSense.getTarget(), searchBox, 
                candidate -> candidate.distanceToSqr(entity) <= radius * radius);
            DangerSenseOverlayRenderer.setActive(!found.isEmpty());
        });
    }
    
    @Override
    public void addAttributeModifiers(LivingEntity entity, AttributeModifierManager manager, int amplifier)
    {
        super.addAttributeModifiers(entity, manager, amplifier);
        if (entity instanceof PlayerEntity)
        {
            Chat.pushToActionbar((PlayerEntity) entity, 
                new TranslationTextComponent("message." + DangerSense.MOD_ID + ".potion.start"));
        }
    }
    
    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeModifierManager manager, int amplifier)
    {
        super.removeAttributeModifiers(entity, manager, amplifier);
        DangerSenseData.get(entity).ifPresent(DangerSenseData::clearTarget);
        if (entity instanceof PlayerEntity)
        {
            Chat.pushToActionbar((PlayerEntity) entity, 
                new TranslationTextComponent("message." + DangerSense.MOD_ID + ".potion.end"));
        }
    }
}
