package com.sherwin.shercolor.swdevicehandler.domain;

import com.google.gson.Gson;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
public class CorobCalibrationTest  {
	
	@Test
	public void testCalUpload(){		
			//String json = "{\"id\":\"d70cf8c2-1e3d-4f49-8e5f-f411a147f372\",\"messageName\":\"TinterMessage\",\"command\":\"CalUpload\",\"configuration\":{\"colorantSystem\":\"CCE\",\"model\":\"Corob D600\",\"serial\":\"S12343\",\"port\":null,\"canisterLayout\":[{\"pump\":1,\"code\":\"W1\"},{\"pump\":2,\"code\":\"N1\"},{\"pump\":3,\"code\":\"R4\"},{\"pump\":4,\"code\":\"R3\"},{\"pump\":5,\"code\":\"G2\"},{\"pump\":6,\"code\":\"NA\"},{\"pump\":7,\"code\":\"B1\"},{\"pump\":8,\"code\":\"Y3\"},{\"pump\":9,\"code\":\"L1\"},{\"pump\":10,\"code\":\"R2\"},{\"pump\":11,\"code\":\"Y1\"},{\"pump\":12,\"code\":\"NA\"}],\"canisterMap\":null},\"shotList\":null,\"status\":0,\"javaMessage\":\"\",\"commandRC\":0,\"errorNumber\":0,\"errorSeverity\":0,\"errorMessage\":0,\"errorList\":null}";
			//TinterMessage msg = new Gson().fromJson(json,TinterMessage.class);
			CorobCalibration cCal = new CorobCalibration();
			//Configuration config = msg.getConfiguration();
			String filename="CCE_Corob D600_S12345";
			String uFilename= filename.toUpperCase() + ".zip";
			uFilename = uFilename.replace(" ", "");   // (/\s+/g, '');
			cCal.setFilename(uFilename);
			String exceptionMessage = cCal.fromDisk();
			
		
	}
}
