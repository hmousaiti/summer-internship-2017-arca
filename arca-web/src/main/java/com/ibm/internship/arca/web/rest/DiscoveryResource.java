package com.ibm.internship.arca.web.rest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ibm.internship.arca.business.ARCADiscovery;

@Path("discovery")
public class DiscoveryResource {

  @GET
  @Produces(MediaType.APPLICATION_JSON) 
  public Response search(@QueryParam("query") String query) {
	  ARCADiscovery discovery = new ARCADiscovery();
	  return Response.status(Response.Status.OK).entity(discovery.search(query)).build();
  }

}
