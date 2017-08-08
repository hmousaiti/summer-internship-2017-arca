package com.ibm.internship.arca.business;

import java.util.ArrayList;
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
		private long timestamp;
		public long getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}
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
	
	public class location{
		
		private double longitude;
		private double latitude;
		
		public double getLongitude() {
			return longitude;
		}
		public void setLongitude(double longitude) {
			this.longitude = longitude;
		}
		public double getLatitude() {
			return latitude;
		}
		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}
		
		
	}
	
	public location getLocation () {
		BluemixCloudant b = new BluemixCloudant();
//		System.err.println(b.getUserConfig("double-vision-cam").getCameraId());
		location loc = new location();
		try{
//			String selector = "\"selector\": {\"$and\": [{ \"userConfig\": " + true + " },{ \"cameraId\": " + camId + " }]}";
			String selector = "{\"selector\": {\"timestamp\": {\"$gt\": 0}},\"fields\": [\"_id\",\"timestamp\"],\"sort\": [{\"timestamp\": \"desc\"}]}";
			
			List<Doc> docs = b.getDB().findByIndex(selector, Doc.class);
			loc.setLongitude(docs.get(docs.size()-1).getPayload().getLong());
			loc.setLatitude(docs.get(docs.size()-1).getPayload().getLat());
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return loc;
	}
	
	
	public List<location> getPath (long from, long to) {
		BluemixCloudant b = new BluemixCloudant();
//		System.err.println(b.getUserConfig("double-vision-cam").getCameraId());
		List<location> path = new ArrayList<location>();
		location loc = new location();
		try{
//			String selector = "\"selector\": {\"$and\": [{ \"userConfig\": " + true + " },{ \"cameraId\": " + camId + " }]}";
			String selector = "{\"selector\": {\"timestamp\": {\"$gt\": 0}},\"fields\": [\"_id\",\"timestamp\"],\"sort\": [{\"timestamp\": \"desc\"}]}";
			
			List<Doc> docs = b.getDB().findByIndex(selector, Doc.class);
			
			
			for (int i = docs.size() - 1; i >= 0; i--)
			{
				if (docs.get(i).getTimestamp() > from && docs.get(i).getTimestamp() < to)
				{
					loc.setLongitude(docs.get(i).getPayload().getLong());
					loc.setLatitude(docs.get(i).getPayload().getLat());
					
					path.add(loc);
					loc = new location();
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return path;
	}
}
	
	