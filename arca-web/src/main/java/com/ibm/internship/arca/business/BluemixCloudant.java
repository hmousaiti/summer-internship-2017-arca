package com.ibm.internship.arca.business;

import java.util.List;

import javax.ws.rs.WebApplicationException;

import com.cloudant.client.api.Database;
/*import com.ibm.bluemix.services.business.logic.IBluemixCloudant;
import com.ibm.bluemix.services.business.logic.impl.EventRulesEnum;
import com.ibm.mea.build.web.rest.dto.AlertSelector;*/

public class BluemixCloudant {
	
	
	private Database getDB(){
		Database db = null;
		try {
			db = CloudantClientMgr.getDB();
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(e.getMessage());
		}
		return db;
	}
	
	class Payload {
		private String batt;
		private double lon;
		private double lat;

		public String getBatt() {
			return batt;
		}
		
		public double getLong() {
			return lon;
		}
		
		public double getLat() {
			return lat;
		}
		public void setBatt(String batt) {
			this.batt = batt;
		}
	}
	
	class Doc{
		private String _id;
		private Payload payload;
		public String get_id() {
			return _id;
		}
		public void set_id(String _id) {
			this._id = _id;
		}
		public Payload getPayload() {
			return payload;
		}
		public void setPayload(Payload payload) {
			this.payload = payload;
		}
	}
	
	public double getLongitude () {
		BluemixCloudant b = new BluemixCloudant();
//		System.err.println(b.getUserConfig("double-vision-cam").getCameraId());
		double lon = 0;
		try{
//			String selector = "\"selector\": {\"$and\": [{ \"userConfig\": " + true + " },{ \"cameraId\": " + camId + " }]}";
			String selector = "{\"selector\": {\"payload.batt\": {\"$gt\": 0}},\"fields\": [\"_id\",\"payload.batt\"],\"sort\": [{\"payload.batt\": \"desc\"}]}";
			
			List<Doc> docs = b.getDB().findByIndex(selector, Doc.class);
			lon = docs.get(docs.size()-1).getPayload().getLong();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return lon;
	}
	
	public double getLatitude () {
		BluemixCloudant b = new BluemixCloudant();
//		System.err.println(b.getUserConfig("double-vision-cam").getCameraId());
		double lon = 0;
		try{
//			String selector = "\"selector\": {\"$and\": [{ \"userConfig\": " + true + " },{ \"cameraId\": " + camId + " }]}";
			String selector = "{\"selector\": {\"payload.batt\": {\"$gt\": 0}},\"fields\": [\"_id\",\"payload.batt\"],\"sort\": [{\"payload.batt\": \"desc\"}]}";
			
			List<Doc> docs = b.getDB().findByIndex(selector, Doc.class);
			lon = docs.get(docs.size()-1).getPayload().getLat();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return lon;
	}
}
