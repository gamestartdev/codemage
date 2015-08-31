package org.gamestartschool.codemage.ddp;

public interface ISpell {
	public String getCode();
	public String getName();
	public void setSpellMessage(String string);
	public void addObserver(ISpellObserver observer);
	public void setStatus(boolean status);
	public boolean getStatus();
}