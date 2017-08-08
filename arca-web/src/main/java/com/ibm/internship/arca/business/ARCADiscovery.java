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
		discovery.setUsernameAndPassword("cb6a0bee-a980-467f-b04e-8964964a4257", "cpqBG4Pkd4xm");
		String environmentId = "6d3902ae-ce4b-4865-b9be-ca576af22b74";
		String collectionId = "def0dc2b-6491-4531-bcb5-aacf83a406fb";


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
				}
				if(entry.getKey().equals("body")){
					body = entry.getValue().toString() ;
				}
				if(entry.getKey().equals("sourceUrl")){
					 url = entry.getValue().toString() ;
				}
			}
			DiscoveryDocument DD = new DiscoveryDocument(id,body,url) ;
			al.add(DD);
		}
	
		return al;
	}
}
