package com.ibm.internship.arca.web.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ibm.internship.arca.business.BluemixCloudant;
//import com.ibm.mea.build.web.rest.Constants;
//import com.ibm.bluemix.services.business.logic.IBluemixCloudant;
//import com.ibm.bluemix.services.business.logic.impl.BusinessLogicFactory;
//import com.ibm.mea.build.web.rest.Constants;
import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.util.GsonSingleton;


@Path("conversation")
public class ConversationResource {

	  private static String API_VERSION;
	  private static String PASSWORD = "1mLdrkTUXaBs";
	  private static String URL = "https://gateway.watsonplatform.net/conversation/api";
	  private static String USERNAME = "aacd82b6-67ec-47b2-b380-b04c983c8052";
	  private static String workspaceId = "3e1c7471-53f9-4cd4-905c-36e4294499e3";
	  private static BluemixCloudant BC = new BluemixCloudant();
	
	  
	
	private MessageRequest buildMessageFromPayload(InputStream body) {
	    StringBuilder sbuilder = null;
	    BufferedReader reader = null;
	    try {
	      reader = new BufferedReader(new InputStreamReader(body, "UTF-8"));
	      sbuilder = new StringBuilder();
	      String str = reader.readLine();
	      while (str != null) {
	        sbuilder.append(str);
	        str = reader.readLine();
	        if (str != null) {
	          sbuilder.append("\n");
	        }
	      }
	      return GsonSingleton.getGson().fromJson(sbuilder.toString(), MessageRequest.class);
	    } catch (IOException e) {
	    	e.printStackTrace();
	    } finally {
	      try {
	        reader.close();
	      } catch (IOException e) {
	    	  e.printStackTrace();
	      }
	    }
	    return null;
	  }
	
	private MessageResponse getWatsonResponse(MessageRequest conversationRequest, String id) throws Exception {

	    //ConversationService service = new ConversationService(API_VERSION != null ? API_VERSION : ConversationService.VERSION_DATE_2016_09_20);
		ConversationService service = new ConversationService("2017-08-06");
		//if (USERNAME != null || PASSWORD != null) {
	      service.setUsernameAndPassword(USERNAME, PASSWORD);
	    //}
	  //  if (URL != null) {
	      service.setEndPoint(URL);
	   // }
	    MessageResponse conversationResponse = service.message(id, conversationRequest).execute();
	    String responsetxt= "";
	    if(conversationResponse.getInputText().isEmpty()
	    		&& conversationResponse.getContext().containsKey(Constants.CONTEXT_USERNAME_PROPERTY_KEY))
	    {
	    	String username = conversationResponse.getContext().get(Constants.CONTEXT_USERNAME_PROPERTY_KEY).toString();
	    	 responsetxt =  conversationResponse.getOutput().get("text").toString();
	    	 responsetxt = responsetxt.substring(1, responsetxt.length()-2);
	    	 responsetxt += " "+username + "?";
	    	 conversationResponse.getContext().put(Constants.CONTEXT_SHOW_LOCATION_PROPERTY_KEY, "FALSE");
	    	 conversationResponse.getContext().put(Constants.CONTEXT_SHOW_PATH_PROPERTY_KEY, "FALSE");
	    }
	    
	    if((!conversationResponse.getContext().containsKey(Constants.CONTEXT_ACTION_PROPERTY_NAME)
	    		&&conversationResponse.getContext().get(Constants.CONTEXT_SHOW_LOCATION_PROPERTY_KEY).equals("TRUE"))||
	    	(conversationResponse.getContext().containsKey(Constants.CONTEXT_ACTION_PROPERTY_NAME)
	    		&& !conversationResponse.getContext().get(Constants.CONTEXT_ACTION_PROPERTY_NAME).equals(Constants.CONTEXT_ACTION_RETREIVE_LOCATION)
	    		&& conversationResponse.getContext().containsKey(Constants.CONTEXT_SHOW_LOCATION_PROPERTY_KEY)
	    		&&conversationResponse.getContext().get(Constants.CONTEXT_SHOW_LOCATION_PROPERTY_KEY).equals("TRUE")))
	    {
	    	conversationResponse.getContext().remove(Constants.CONTEXT_SHOW_LOCATION_PROPERTY_KEY);
	    	conversationResponse.getContext().put(Constants.CONTEXT_SHOW_LOCATION_PROPERTY_KEY, "FALSE");
	    	conversationResponse.getContext().remove(Constants.CONTEXT_LAT_PROPERTY_KEY);
	    	conversationResponse.getContext().remove(Constants.CONTEXT_LONG_PROPERTY_KEY);
	    	
	    }
	    
	    if((!conversationResponse.getContext().containsKey(Constants.CONTEXT_ACTION_PROPERTY_NAME)
	    		&&conversationResponse.getContext().get(Constants.CONTEXT_SHOW_PATH_PROPERTY_KEY).equals("TRUE")) ||
		    	(conversationResponse.getContext().containsKey(Constants.CONTEXT_ACTION_PROPERTY_NAME)
		    		&& !conversationResponse.getContext().get(Constants.CONTEXT_ACTION_PROPERTY_NAME).equals(Constants.CONTEXT_ACTION_RETREIVE_PATH)
		    		&& conversationResponse.getContext().containsKey(Constants.CONTEXT_SHOW_PATH_PROPERTY_KEY)
		    		&&conversationResponse.getContext().get(Constants.CONTEXT_SHOW_PATH_PROPERTY_KEY).equals("TRUE")))
		    {
		    	conversationResponse.getContext().remove(Constants.CONTEXT_SHOW_PATH_PROPERTY_KEY);
		    	conversationResponse.getContext().put(Constants.CONTEXT_SHOW_PATH_PROPERTY_KEY, "FALSE");
		    	conversationResponse.getContext().remove("PATH");
		    	
		    }	
	    
	    if(conversationResponse.getContext().containsKey(Constants.CONTEXT_ACTION_PROPERTY_NAME)
	    		&& conversationResponse.getContext().get(Constants.CONTEXT_ACTION_PROPERTY_NAME).equals(Constants.CONTEXT_ACTION_RETREIVE_LOCATION))
	    {
	    	conversationResponse.getContext().remove(Constants.CONTEXT_ACTION_PROPERTY_NAME);
	    	responsetxt = conversationResponse.getOutput().get("text").toString();
			responsetxt = responsetxt.substring(1, responsetxt.length()-2);
			double lng = BC.getLocation().getLongitude();
			double lat = BC.getLocation().getLatitude();
	    	//responsetxt = responsetxt + " longitude: " + lng + " and latitude: " + lat + ",Press the below button to show map";
	    	conversationResponse.getContext().put(Constants.CONTEXT_SHOW_LOCATION_PROPERTY_KEY, "TRUE");
	    	conversationResponse.getContext().put(Constants.CONTEXT_LONG_PROPERTY_KEY, lng);
	    	conversationResponse.getContext().put(Constants.CONTEXT_LAT_PROPERTY_KEY, lat);
	    }
	    if(conversationResponse.getContext().containsKey(Constants.CONTEXT_ACTION_PROPERTY_NAME)
	    		&& conversationResponse.getContext().get(Constants.CONTEXT_ACTION_PROPERTY_NAME).equals(Constants.CONTEXT_ACTION_RETREIVE_PATH))
	    {
	    	conversationResponse.getContext().remove(Constants.CONTEXT_ACTION_PROPERTY_NAME);
	    	
			JSONObject obj = new JSONObject();
			JSONObject locationArray = new JSONObject();
			
			long from = Long.parseLong(conversationResponse.getContext().get(Constants.CONTEXT_FROM_DATE_PROPERTY_KEY).toString());
			long to = Long.parseLong(conversationResponse.getContext().get(Constants.CONTEXT_TO_DATE_PROPERTY_KEY).toString());
			
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
		    
		    conversationResponse.getContext().put(Constants.CONTEXT_SHOW_PATH_PROPERTY_KEY, "TRUE");
		    conversationResponse.getContext().put("PATH", locationArray);
		    conversationResponse.getContext().remove(Constants.CONTEXT_TO_DATE_PROPERTY_KEY);
		    conversationResponse.getContext().remove(Constants.CONTEXT_FROM_DATE_PROPERTY_KEY);
	    	responsetxt = conversationResponse.getOutput().get("text").toString();
			responsetxt = responsetxt.substring(1, responsetxt.length()-2);
	    	responsetxt = responsetxt+ " " + path.size() + " locations.";
	    }
	    
	    
	    
	    if(!responsetxt.isEmpty())
	      conversationResponse.getOutput().put("text", responsetxt);
	    return conversationResponse;
	}
	
	
	
	@POST @Path("converse")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response postMessage(InputStream body) {
		HashMap<String, Object> errorsOutput = new HashMap<String, Object>();
	    MessageRequest request = buildMessageFromPayload(body);

	    if (request == null) {
	      throw new IllegalArgumentException("");
	    }

	    MessageResponse response = null;

	    try {
	      response = getWatsonResponse(request, workspaceId);  	
	    } catch (Exception e) {
	      e.printStackTrace();
	      return Response.status(Status.BAD_REQUEST).entity(e).build();
	      //return Response.ok(new Gson().toJson(errorsOutput, HashMap.class)).type(MediaType.APPLICATION_JSON).build();
	    }
	    return Response.ok(new Gson().toJson(response, MessageResponse.class)).type(MediaType.APPLICATION_JSON).build();
	}
}
