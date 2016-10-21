package leviathan143.dangersense.common.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;

public class DamageSourceSoulExtraction extends EntityDamageSource
{	
	public DamageSourceSoulExtraction(EntityLivingBase source) 
	{
		super("soulExtraction", source);
	}

	public static DamageSource causeSoulExtractionDamage(EntityLivingBase source)
	{
		return new DamageSourceSoulExtraction(source);
	}
}
