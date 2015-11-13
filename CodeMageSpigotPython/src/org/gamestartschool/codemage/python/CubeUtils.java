package org.gamestartschool.codemage.python;

import org.bukkit.Material;
import org.bukkit.World;

public class CubeUtils
{
	public static void setRect(int x, int y, int z, int xlen, int height, int ylen, Material material, World world)
	{
		for(int setx = x; setx < x + xlen; setx++)
		{
			for(int sety = y; sety < y + height; sety++)
			{
				for(int setz = z; setz < z + height; setz++)
				{
					world.getBlockAt(setx, sety, setz).setType(material);
				}
			}
		}
	}
}
