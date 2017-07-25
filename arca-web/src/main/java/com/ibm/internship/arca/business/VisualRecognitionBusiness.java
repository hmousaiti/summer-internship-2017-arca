package com.ibm.internship.arca.business;

import java.io.File;
import java.util.List;

import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyImagesOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassification;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassifier.VisualClass;

public class VisualRecognitionBusiness {

	public String getTopVisualClass(File myFile) {
		
		VisualRecognition service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20);
		 service.setEndPoint("https://gateway-a.watsonplatform.net/visual-recognition/api");
		 
		 
		 String my_api="37119c77d01600ecfec9d2e95541048856fdb067";
		 String my_class="DashboardWarnings_1224689620";
		 
		 service.setApiKey(my_api);
		 ClassifyImagesOptions options = new ClassifyImagesOptions.Builder().classifierIds(my_class).images(myFile).build();
		 VisualClassification result = service.classify(options).execute();
		 
		 List<VisualClass> classes = result.getImages().get(0).getClassifiers().get(0).getClasses();
		 VisualClass temp;
		 temp=classes.get(0);
		 for (VisualClass visualClass : classes) {
			 if(temp.getScore()<visualClass.getScore())
				 temp=visualClass;
			 
		}
		return temp.getName();
	}

}
