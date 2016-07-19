package org.gamestartschool.codemage.python;

import org.gamestartschool.codemage.ddp.ISpellMeteorMethodCaller;

public class PrintHelper {
	
	private static ISpellMeteorMethodCaller methodCaller = null;
	
	public static void setMethodCaller(ISpellMeteorMethodCaller caller)
	{
		methodCaller = methodCaller == null ? caller : methodCaller;
	}
	
	public static void onPyPrint(String spellId, String print)
	{
		System.out.println(print);
		methodCaller.spellPrint(print, spellId);
	}
}
