package leviathan143.dangersense.common.capabilities;



import leviathan143.dangersense.common.DangerSenseMain.Constants;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class PlayerDangerSense implements ICapabilitySerializable<NBTTagString>
{
	public static final ResourceLocation CAP_KEY = new ResourceLocation(Constants.MODID, "dangersense");
	
	private IDangerSense dangerSense;
	
	public PlayerDangerSense()
	{
		dangerSense = new PlayerDangerSenseCap();
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) 
	{
		if(capability == CapabilityDangerSense.DANGERSENSE_CAP) 
			{
				return true;
			}
		return false;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) 
	{
		if(capability == CapabilityDangerSense.DANGERSENSE_CAP) 
		{
			return CapabilityDangerSense.DANGERSENSE_CAP.cast(dangerSense);
		}
		return null;
	}
	


	@Override
	public NBTTagString serializeNBT() 
	{
		return dangerSense.serializeNBT();
	}

	@Override
	public void deserializeNBT(NBTTagString nbt) 
	{
		dangerSense.deserializeNBT(nbt);
	}
	
	public class PlayerDangerSenseCap implements IDangerSense
	{
		private String currentSense = "";

		@Override
		public NBTTagString serializeNBT() 
		{
			return new NBTTagString(currentSense);
		}

		@Override
		public void deserializeNBT(NBTTagString nbt) 
		{
			currentSense = nbt.getString();
		}

		@Override
		public String getSense() 
		{
			return currentSense;
		}

		@Override
		public void setSense(String sense) 
		{
			currentSense = sense;
		}
	}
}
