package com.ibm.internship.arca.business.dto;

public class DiscoveryDocument {

	private String id;
	
	private String body;
	
	private String url;
	
	public DiscoveryDocument(String id,String body,String url){
		this.id= id ;
		this.body = body ;
		this.url = url ;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
