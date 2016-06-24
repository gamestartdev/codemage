package org.gamestartschool.codemage.python;

import org.gamestartschool.codemage.ddp.ISpellMeteorMethodCaller;

public class PrintHelper {
	
	private static ISpellMeteorMethodCaller methodCaller;
	
	public static void setMethodCaller(ISpellMeteorMethodCaller caller)
	{
		methodCaller = caller;
	}
	
	public static void onPyPrint(String spellname, Object... args)
	{
		
		String str = "";
		for(Object obj : args)
		{
			str += obj.toString();
		}
		
	}
}
