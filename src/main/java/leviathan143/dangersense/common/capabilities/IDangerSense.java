package leviathan143.dangersense.common.capabilities;

import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.INBTSerializable;

public interface  IDangerSense extends INBTSerializable<NBTTagString> 
{
	public String getSense();
	
	public void setSense(String string);
}
