package leviathan143.dangersense.common.effects;

import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class ModEffects 
{	
	public static Potion dangerSense;
	
	public static void register()
	{
		dangerSense = GameRegistry.register(new EffectDangerSense().setPotionName("potion.dangersense").setBeneficial().setRegistryName("dangersense"));
	}
}
