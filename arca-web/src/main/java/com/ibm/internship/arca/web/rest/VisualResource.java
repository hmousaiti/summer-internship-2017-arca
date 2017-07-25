package com.ibm.internship.arca.web.rest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.google.gson.JsonObject;
import com.ibm.internship.arca.business.ARCADiscovery;
import com.ibm.internship.arca.business.VisualRecognitionBusiness;
import com.ibm.internship.arca.business.dto.DiscoveryDocument;



@Path("visual")
public class VisualResource {
	
	@POST
	@Path("/analyze")
	@Consumes({MediaType.MULTIPART_FORM_DATA})
	@Produces(MediaType.APPLICATION_JSON)
	public Response analyzeImage(@FormDataParam("file") InputStream fileInputStream, @FormDataParam("file") FormDataContentDisposition fileMetaData)
	{
		try {
			System.err.println(fileInputStream);
			System.err.println(fileMetaData);
			File imageTmpFile = this.createTmpFileForInputStream(fileInputStream);
			
			VisualRecognitionBusiness handler = new VisualRecognitionBusiness();
			String topVRClass = handler.getTopVisualClass(imageTmpFile);
			
			
			ARCADiscovery discovery = new ARCADiscovery();
			List<DiscoveryDocument> documents = discovery.search(topVRClass);
			
			return Response.status(Response.Status.OK).entity(documents).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
		}
	}
	
	private File createTmpFileForInputStream(InputStream fileInputStream) throws IOException {
		File tmpFile = File.createTempFile("arca-image-", ".jpg");
		tmpFile.deleteOnExit();	
		Files.copy(fileInputStream, tmpFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		
		return tmpFile;
	}
}
		

