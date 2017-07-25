package com.ibm.internship.arca.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ibm.internship.arca.business.dto.DiscoveryDocument;
import com.ibm.watson.developer_cloud.discovery.v1.Discovery;
import com.ibm.watson.developer_cloud.discovery.v1.model.query.QueryRequest;
import com.ibm.watson.developer_cloud.discovery.v1.model.query.QueryResponse;


public class ARCADiscovery {

	public List<DiscoveryDocument> search(String query){
		Discovery discovery = new Discovery("2017-07-19");
		discovery.setEndPoint("https://gateway.watsonplatform.net/discovery/api");
		discovery.setUsernameAndPassword("3fe186af-7bf8-4762-972f-08310e91c454", "luBPJjOBv2YK");
		String environmentId = "da5029bc-ba1a-4b46-9fca-bca6e2454ecf";
		String collectionId = "2a1ee576-6a22-4898-8a84-fcb42770cddf";


		QueryRequest.Builder queryBuilder = new QueryRequest.Builder(environmentId, collectionId);
		queryBuilder.query("body :+ "+query);
		QueryResponse queryResponse = discovery.query(queryBuilder.build()).execute();

		System.err.println(queryResponse.getResults().size());
		List <DiscoveryDocument> al = new ArrayList ();
		for (int i=0; i<queryResponse.getResults().size(); i++) {
			String id ="";
			String body="";
			String url ="";
			for (Map.Entry<String, Object> entry : queryResponse.getResults().get(i).entrySet()) {
				if(entry.getKey().equals("id")){
					id = entry.getValue().toString() ;
					System.out.println(id);
				}
				if(entry.getKey().equals("body")){
					body = entry.getValue().toString() ;
					System.out.println(body);
				}
				if(entry.getKey().equals("sourceUrl")){
					 url = entry.getValue().toString() ;
					 System.out.println(url);
				}	
				
				
			//	System.err.println(entry.getKey());
				//build doc object
				
				
			}
			DiscoveryDocument DD = new DiscoveryDocument(id,body,url) ;
			al.add(DD);
		}
	
		return al;
	}
}
