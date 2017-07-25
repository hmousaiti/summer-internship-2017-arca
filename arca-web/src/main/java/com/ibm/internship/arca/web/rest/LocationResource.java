package com.ibm.internship.arca.web.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.JsonObject;
import com.ibm.internship.arca.business.BluemixCloudant;

@Path("location")
public class LocationResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLocation(@Context HttpServletRequest req) {
		BluemixCloudant BC = new BluemixCloudant();
		JsonObject json = new JsonObject();
		json.addProperty("long", BC.getLongitude());
		json.addProperty("lat", BC.getLatitude());
		return Response.status(Response.Status.OK).entity(json.toString()).build();
	}
}
