package com.focusmedica.aqrshell.app;

public class AppConfig {
	// Server user login url

	// Server user register url
	public static String URL_REGISTER = "http://pantherpublishers.com/distribution/udshell/v3/index.php";

	private static String dbPath;

	public static void setDatabaseName(String db){
		dbPath = db;
	}

	public static String getDatabaseName(){
		return dbPath;
	}
}


