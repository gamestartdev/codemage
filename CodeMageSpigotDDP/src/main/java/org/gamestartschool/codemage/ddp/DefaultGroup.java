package org.gamestartschool.codemage.ddp;

import java.util.List;

public class DefaultGroup implements IGroup {
	
	public static IGroup INSTANCE = new DefaultGroup();
	
	@Override
	public List<String> getMemberIds() {
		throw new UnsupportedOperationException("DefaultGroup is a fallback, not an actual group!");
	}

	@Override
	public List<String> getWrapperIds() {
		throw new UnsupportedOperationException("DefaultGroup is a fallback, not an actual group!");
	}

	@Override
	public String getName() {
		return null;
	}

}
