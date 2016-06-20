package org.gamestartschool.codemage.ddp.example;

import java.net.URISyntaxException;
import org.gamestartschool.codemage.ddp.CodeMageDDP;

public class Main {

	static String meteorIp = "localhost";
	static int meteorPort = 3000;
	static String meterUsername = "admin2";
	static String meteorPassword = "asdf";

	static String minecraftPlayerId1 = "GameStartSchool";
	static String minecraftPlayerId2 = "denrei";

	public static void main(String[] args) throws InterruptedException, URISyntaxException {

		CodeMageDDP ddp = new CodeMageDDP(meteorIp, meteorPort);
		ddp.connect(meterUsername, meteorPassword);
	}
}
