package org.gamestartschool.codemage.python.experiments;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class ListenerRemover {

	public static void removeListenerForEvent(final Listener listener, final Class<? extends Event> eventClass, Plugin plugin) {
		try {
			Method method = eventClass.getDeclaredMethod("getHandlerList");
			method.setAccessible(true);
			HandlerList handlerList = (HandlerList) method.invoke(null);
			System.out.println("pre-"+HandlerList.getRegisteredListeners(plugin));
			handlerList.unregister(listener);
			System.out.println("pos-"+HandlerList.getRegisteredListeners(plugin));
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
