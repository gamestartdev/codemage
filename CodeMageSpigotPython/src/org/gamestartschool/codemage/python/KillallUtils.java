package org.gamestartschool.codemage.python;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class KillallUtils
{
	public static void killall(World world)
	{
		for(Entity e : world.getEntities())
		{
			if(!(e instanceof Player))
			{
				e.remove();
			}
		}
	}
}
