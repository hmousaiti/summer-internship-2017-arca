package com.ibm.internship.arca.web.rest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

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
