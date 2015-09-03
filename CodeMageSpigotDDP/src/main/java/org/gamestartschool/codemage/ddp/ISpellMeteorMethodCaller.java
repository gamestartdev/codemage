package org.gamestartschool.codemage.ddp;

interface ISpellMeteorMethodCaller {
	void spellMessage(String id, String message);
	void spellSetFocus(int row, int column);
	void spellStatus(String spellId, String status);
}
