package leviathan143.dangersense.common.capabilities;

import java.util.concurrent.Callable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityDangerSense 
{
	@CapabilityInject(IDangerSense.class)
	public static Capability<IDangerSense> DANGERSENSE_CAP = null;
	
	public static void register()
	{
		CapabilityManager.INSTANCE.register(IDangerSense.class, new Capability.IStorage<IDangerSense>() 
		{
			@Override
			public NBTBase writeNBT(Capability<IDangerSense> capability,
					IDangerSense instance, EnumFacing side) {return null;}

			@Override
			public void readNBT(Capability<IDangerSense> capability, IDangerSense instance, EnumFacing side, NBTBase nbt) {}
		}, 
		new Callable<IDangerSense>() 
		{
			@Override
			public IDangerSense call() throws Exception {return null;}
		});
	}
}
