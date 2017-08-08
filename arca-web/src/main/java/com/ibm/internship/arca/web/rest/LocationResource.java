package com.ibm.internship.arca.web.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.gson.JsonObject;
import com.ibm.internship.arca.business.BluemixCloudant;
//import com.ibm.internship.arca.business.BluemixCloudant.location;

@Path("location")
public class LocationResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLocation(@Context HttpServletRequest req) {
		BluemixCloudant BC = new BluemixCloudant();
		JsonObject json = new JsonObject();
		json.addProperty("long", BC.getLocation().getLongitude());
		json.addProperty("lat", BC.getLocation().getLatitude());
		return Response.status(Response.Status.OK).entity(json.toString()).build();
	}
	@GET
	@Path("/path")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPath(@Context HttpServletRequest req, @QueryParam("from") long from,  @QueryParam("to") long to) {
		BluemixCloudant BC = new BluemixCloudant();
		JSONObject obj = new JSONObject();
		JSONObject locationArray = new JSONObject();
		List<BluemixCloudant.location> path = BC.getPath(from, to); 
	    JSONArray list = new JSONArray();
	    
	    for (int i = 0; i < path.size(); i++)
		{
	    	obj.put("id", i);
	    	obj.put("long", path.get(i).getLongitude());
	    	obj.put("lat", path.get(i).getLatitude());	
			list.add(obj);		
			obj = new JSONObject();
		}    
	    
	    locationArray.put("path", list);
	    locationArray.put("size", path.size());
	    
		return Response.status(Response.Status.OK).entity(locationArray.toString()).build();
	}
	
}
