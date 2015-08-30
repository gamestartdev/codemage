package org.gamestartschool.codemage.ddp;

import java.text.SimpleDateFormat;
import java.util.Date;

final class SpellExecutionFromBrowserObserver implements ISpellObserver {
	private String minecraftPlayerId;

	public SpellExecutionFromBrowserObserver(String minecraftPlayerId) {
		this.minecraftPlayerId = minecraftPlayerId;
	}

	@Override
	public void requestCodeExecutionFromBrowser(ISpell spell) {
		System.out.println(minecraftPlayerId + " is running code!: \n" + spell.getCode());
		String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
		spell.setSpellMessage("starting...");
		try {
			Thread.sleep(500);
			spell.setSpellMessage("running...");
			Thread.sleep(1500);
			spell.setSpellMessage("finished at " + dateString);
			spell.setStatus(false);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}