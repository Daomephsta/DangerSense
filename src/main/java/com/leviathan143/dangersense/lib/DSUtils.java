package com.leviathan143.dangersense.lib;

import java.util.List;

public class DSUtils 
{
	public String getEntityFromEntitiesNearby(List list, int index)
	{
		String element = list.get(index).toString();
		element = element.substring(0, element.indexOf("["));
		return element;
	}
	
	public String getEntityFromSpawnableList(List list, int index)
	{
		String element = list.get(index).toString();
		element = element.substring(0, element.indexOf("*"));
		return element;
	}
}
