package org.gamestartschool.codemage.ddp.example;

public class Main {

//	static String meteorIp = "localhost";
//	static int meteorPort = 3000;
//	static String meterUsername = "admin2";
//	static String meteorPassword = "asdf";
//
//	static String minecraftPlayerId1 = "GameStartSchool";
//	static String minecraftPlayerId2 = "denrei";
//
//	public static void main(String[] args) throws InterruptedException, URISyntaxException {
//
//		CodeMageDDP ddp = new CodeMageDDP(meteorIp, meteorPort);
//		ddp.connect(meterUsername, meteorPassword);
//		runWoodenSwordSwingCodeForPlayer(ddp, minecraftPlayerId2);
//		System.out.println("Shutting down in 3...");
//		Thread.sleep(1000);
//		System.out.println("2...");
//		Thread.sleep(1000);
//		System.out.println("1...");
//		Thread.sleep(1000);
//		ddp.disconnect();
//		System.out.println("Done!");
//	}
//
//	public static String runWoodenSwordSwingCodeForPlayer(CodeMageDDP ddp, String minecraftPlayerId)
//			throws InterruptedException {
//		IUser user = ddp.getUser(minecraftPlayerId);
//		List<IEnchantment> enchantments = user.getEnchantments();
//
//		for (IEnchantment e : enchantments) {
//			if (EDummyEnchantmentTrigger.PRIMARY.equals(e.getTrigger())
//					&& EDummyEnchantmentBinding.WOODEN_SWORD.equals(e.getBinding())) {
//				for (ISpell spell : e.getSpells()) {
//					return spell.getCode();
//					
////					SpellExecutionFromBrowserObserver observer = new SpellExecutionFromBrowserObserver(minecraftPlayerId);
////					spell.setStatus(false);
////					spell.addObserver(observer);
//				}
//			}
//		}
//		
//		Thread.sleep(1000);
//		System.out.println("3...");
//
//		Thread.sleep(1000);
//		System.out.println("2...");
//		Thread.sleep(1000);
//		System.out.println("1...");
//
//		Thread.sleep(1000);
//		System.out.println("SWING!");
//		for (IEnchantment e : enchantments) {
//			if (EDummyEnchantmentTrigger.PRIMARY.equals(e.getTrigger())
//					&& EDummyEnchantmentBinding.WOODEN_SWORD.equals(e.getBinding())) {
//				for (ISpell spell : e.getSpells()) {
//					spell.setStatus(true);
//				}
//			}
//		}
//		return "";
//	}
}
