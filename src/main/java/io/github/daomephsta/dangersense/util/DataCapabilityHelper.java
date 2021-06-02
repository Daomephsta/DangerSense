package io.github.daomephsta.dangersense.util;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class DataCapabilityHelper
{
    private DataCapabilityHelper() {}
    
    public static <T> void register(Class<T> type)
    {
        CapabilityManager.INSTANCE.register(type, new IStorage<T>()
        {
            @Override
            public INBT writeNBT(Capability<T> capability, T instance, Direction side)
            {
                return unsupported();
            }

            @Override
            public void readNBT(Capability<T> capability, T instance, Direction side, INBT nbt)
            {
                unsupported();
            }
        }, DataCapabilityHelper::unsupported);
    }
    
    private static <T> T unsupported()
    {
        throw new UnsupportedOperationException("Internal data capability");
    }
}
