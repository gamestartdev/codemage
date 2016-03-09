package org.gamestartschool.codemage.ddp;

public interface ISpellMeteorMethodCaller {
	void spellMessage(String id, String message);
	void spellSetFocus(int row, int column);
	void spellStatus(String spellId, String status);
	void spellException(String exception, String playername);
}
