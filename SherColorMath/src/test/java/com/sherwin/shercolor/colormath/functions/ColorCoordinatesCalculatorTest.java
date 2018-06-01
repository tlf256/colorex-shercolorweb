package com.sherwin.shercolor.colormath.functions;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;


import com.sherwin.shercolor.colormath.domain.ColorCoordinates;



public class ColorCoordinatesCalculatorTest {
	

	ColorCoordinatesCalculator target = new ColorCoordinatesCalculatorImpl();

	@Test
	public void curveDoubleTest1(){ //SW6106 - Kilim Beige
		double[] curve = {8.5454D,9.8891D,13.0628D,20.4314D,31.6782D,39.3078D,41.9917D,43.7609D,45.9493D,47.5962D,48.1183D,48.3551D,48.7558D,49.5168D,50.8593D,52.5787D,54.5094D,56.5196D,58.3763D,59.9248D,61.3052D,62.8414D,64.1409D,64.8976D,65.1403D,65.0295D,64.8936D,64.6806D,64.5444D,64.4543D,64.3829D,64.3577D,64.229D,64.0975D,63.898D,63.7873D,63.8011D,63.9099D,63.9851D,63.8023D};
		ColorCoordinates colorCoords = target.getColorCoordinates(curve);
		System.out.println("SW6016 XYZ using double (moussa) is " + colorCoords.getCieX() + " " + colorCoords.getCieY() + " " + colorCoords.getCieZ() + " " + "RGB is " + colorCoords.getRgbRed() + " " + colorCoords.getRgbGreen() + " " + colorCoords.getRgbBlue() + " " + colorCoords.getRgbHex());
		assertNotNull(colorCoords);
	}
	
	@Test
	public void curveDoubleTest2(){ //SW6106 - Kilim Beige
		double[] curve = new double[40];
		ColorCoordinates colorCoords = target.getColorCoordinates(curve);
		System.out.println("SW6016 XYZ using double (moussa) NULL ARRAY is " + colorCoords.getCieX() + " " + colorCoords.getCieY() + " " + colorCoords.getCieZ() + " " + "RGB is " + colorCoords.getRgbRed() + " " + colorCoords.getRgbGreen() + " " + colorCoords.getRgbBlue() + " " + colorCoords.getRgbHex());
		assertNotNull(colorCoords);
	}
	
	@Test
	public void curveBigDecimalTest1(){ //SW6106 - Kilim Beige
		BigDecimal[] curve = {new BigDecimal(8.5454),new BigDecimal(9.8891),new BigDecimal(13.0628),new BigDecimal(20.4314),new BigDecimal(31.6782),new BigDecimal(39.3078),new BigDecimal(41.9917),new BigDecimal(43.7609),new BigDecimal(45.9493),new BigDecimal(47.5962),new BigDecimal(48.1183),new BigDecimal(48.3551),new BigDecimal(48.7558),new BigDecimal(49.5168),new BigDecimal(50.8593),new BigDecimal(52.5787),new BigDecimal(54.5094),new BigDecimal(56.5196),new BigDecimal(58.3763),new BigDecimal(59.9248),new BigDecimal(61.3052),new BigDecimal(62.8414),new BigDecimal(64.1409),new BigDecimal(64.8976),new BigDecimal(65.1403),new BigDecimal(65.0295),new BigDecimal(64.8936),new BigDecimal(64.6806),new BigDecimal(64.5444),new BigDecimal(64.4543),new BigDecimal(64.3829),new BigDecimal(64.3577),new BigDecimal(64.229),new BigDecimal(64.0975),new BigDecimal(63.898),new BigDecimal(63.7873),new BigDecimal(63.8011),new BigDecimal(63.9099),new BigDecimal(63.9851),new BigDecimal(63.8023)};
		ColorCoordinates colorCoords = target.getColorCoordinates(curve, "D65");
		System.out.println("SW6016 XYZ using BigDec (brianp) is " + colorCoords.getCieX() + " " + colorCoords.getCieY() + " " + colorCoords.getCieZ() + " " + "RGB is " + colorCoords.getRgbRed() + " " + colorCoords.getRgbGreen() + " " + colorCoords.getRgbBlue() + " " + colorCoords.getRgbHex());
		assertNotNull(colorCoords);
	}
	
	@Test
	public void curveBigDecimalTest2(){ //SW6106 - Kilim Beige
		BigDecimal[] curve = new BigDecimal[40];
		//this should throw an error.  Let's give it a go, shall we?
		boolean errflag = false;
		try {
			ColorCoordinates colorCoords = target.getColorCoordinates(curve, "D65");
			System.out.println("SW6016 XYZ using BigDec (brianp) NULL ARRAY is " + colorCoords.getCieX() + " " + colorCoords.getCieY() + " " + colorCoords.getCieZ() + " " + "RGB is " + colorCoords.getRgbRed() + " " + colorCoords.getRgbGreen() + " " + colorCoords.getRgbBlue() + " " + colorCoords.getRgbHex());
		} catch (Exception e) {
			errflag = true;
			assertNotNull(e);
			System.out.println(e.getMessage());
		}
		assertTrue(errflag);
	}
	


}
