package com.ibm.internship.arca.business;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.org.lightcouch.CouchDbException;

public class CloudantClientMgr {

	private static CloudantClient cloudant = null;
	private static Database db = null;

	private static String user = "d9b85c29-0603-47d2-9a52-2280c97f1d53-bluemix";
	private static String password = "69711d6b727f33c99bea63ae544c6f802c9bc1e20d16b684dd3592f9afbde376";
	public static String DB_NAME = "nodered";
	
	private static void initClient() {
		if (cloudant == null) {
			synchronized (CloudantClientMgr.class) {
				if (cloudant != null) {
					return;
				}
				cloudant = createClient();
			}
		}
	}

	private static CloudantClient createClient() {
		try {
			System.out.println("Connecting to Cloudant : " + user);
			CloudantClient client = ClientBuilder.account(user)
					.username(user)
					.password(password)
					.build();
			return client;
		} catch (CouchDbException e) {
			throw new RuntimeException("Unable to connect to repository", e);
		}
	}

	public static Database getDB() {
		if (cloudant == null) {
			initClient();
		}
		if (db == null) {
			try {
				db = cloudant.database(DB_NAME, true);
			} catch (Exception e) {
				throw new RuntimeException("DB Not found", e);
			}
		}
		return db;
	}

	private CloudantClientMgr() {
	}
}
