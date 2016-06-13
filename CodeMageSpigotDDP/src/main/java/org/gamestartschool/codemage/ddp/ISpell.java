package org.gamestartschool.codemage.ddp;

import org.bukkit.Material;
import org.bukkit.event.block.Action;

public interface ISpell {
	public int getPreprocessPriority();
	public String getCode();
	public String getName();
	public boolean getStatus();
	public String getId();
	public String userId();
	public Action getAction();
	public Material getMaterial();
}