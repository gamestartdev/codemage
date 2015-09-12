package org.gamestartschool.codemage.python.experiments;

import org.bukkit.event.Listener;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;

public class ListenerClassGenerator {
	
	private static Class<?> generateListenerClass() throws CannotCompileException, NotFoundException {
		ClassPool pool = ClassPool.getDefault();
		CtClass cc = pool.makeClass("CodeMageListener"+ System.currentTimeMillis());
		CtClass[] interfaces = new CtClass[]{ pool.getCtClass("org.bukkit.event.Listener") };
		System.out.println(interfaces);
		cc.setInterfaces(interfaces);
		CtMethod mthd = CtNewMethod.make("public void onPlayerInteract(org.bukkit.event.player.PlayerInteractEvent event) { System.out.println(\"IMHERE\"); }", cc);
		cc.addMethod(mthd);

		ClassFile ccFile = cc.getClassFile();
		ConstPool constpool = ccFile.getConstPool();

		AnnotationsAttribute attr = new AnnotationsAttribute(constpool, AnnotationsAttribute.visibleTag);
		Annotation annot = new Annotation("org.bukkit.event.EventHandler", constpool);
//		annot.addMemberValue("value", new IntegerMemberValue(ccFile.getConstPool(), 0));
		attr.addAnnotation(annot);
		mthd.getMethodInfo().addAttribute(attr);

		return cc.toClass();
	}
	
	public static Listener generateListener() {
		try {
			return (Listener)generateListenerClass().newInstance();
		} catch (InstantiationException | IllegalAccessException | CannotCompileException | NotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
