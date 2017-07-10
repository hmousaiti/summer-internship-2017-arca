package com.ibm.internship.arca.web.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.JsonObject;


@Path("user")
public class UserResource {

  @GET @Path("me") 
  @Produces(MediaType.APPLICATION_JSON) 
  public Response getUser(@Context HttpServletRequest req) {
	  JsonObject json = new JsonObject();
	  json.addProperty("currentUser", req.getRemoteUser());
	  return Response.status(Response.Status.OK).entity(json.toString()).build();
  }
  
}
