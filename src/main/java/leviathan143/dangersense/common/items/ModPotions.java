package leviathan143.dangersense.common.items;

import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class ModPotions 
{	
	public static Potion dangerSense;
	
	public static void register()
	{
		dangerSense = GameRegistry.register(new SensePotion().setPotionName("potion.dangersense").setBeneficial()
				.setRegistryName("dangersense"));
	}
}
