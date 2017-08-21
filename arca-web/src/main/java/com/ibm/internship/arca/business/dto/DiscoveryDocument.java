package com.ibm.internship.arca.business.dto;

public class DiscoveryDocument {

//	private String id;
	
	private String body;
	
	private String url;
	
	private String title;
	
	private String imageURL;
	
	public DiscoveryDocument(){
	}

//	public String getId() {
//		return id;
//	}

//	public void setId(String id) {
//		this.id = id;
//	}

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
}
