package com.sherwin.shercolor.colormath.functions;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;


import com.sherwin.shercolor.colormath.domain.ColorCoordinates;

//import com.sherwin.shercolor.common.domain.CdsColorStand;

public class ColorSpaceConverterTest {
	

	ColorSpaceConverter target = new ColorSpaceConverterImpl();
	

	//CdsColorStandDao colorStandDao;
	

	ColorCoordinatesCalculator colorCoordsCalc = new ColorCoordinatesCalculatorImpl();
	
	@Test
	public void curveToXyzTest2andahalf(){ //SW6106 - Kilim Beige
		BigDecimal[] curve = {new BigDecimal(8.5454),new BigDecimal(9.8891),new BigDecimal(13.0628),new BigDecimal(20.4314),new BigDecimal(31.6782),new BigDecimal(39.3078),new BigDecimal(41.9917),new BigDecimal(43.7609),new BigDecimal(45.9493),new BigDecimal(47.5962),new BigDecimal(48.1183),new BigDecimal(48.3551),new BigDecimal(48.7558),new BigDecimal(49.5168),new BigDecimal(50.8593),new BigDecimal(52.5787),new BigDecimal(54.5094),new BigDecimal(56.5196),new BigDecimal(58.3763),new BigDecimal(59.9248),new BigDecimal(61.3052),new BigDecimal(62.8414),new BigDecimal(64.1409),new BigDecimal(64.8976),new BigDecimal(65.1403),new BigDecimal(65.0295),new BigDecimal(64.8936),new BigDecimal(64.6806),new BigDecimal(64.5444),new BigDecimal(64.4543),new BigDecimal(64.3829),new BigDecimal(64.3577),new BigDecimal(64.229),new BigDecimal(64.0975),new BigDecimal(63.898),new BigDecimal(63.7873),new BigDecimal(63.8011),new BigDecimal(63.9099),new BigDecimal(63.9851),new BigDecimal(63.8023)};

		//INVALID CIESTDS value.  Let's see what that does.
		try {
			ColorCoordinates colorCoords = target.CurveToXYZLAB(curve, "BOBO");
			System.out.println("SW6016 XYZ using BiGDec and invalid cie stds of BOBO (brianp) is " + colorCoords.getCieX() + " " + colorCoords.getCieY() + " " + colorCoords.getCieZ() + " L=" + colorCoords.getCieL() + " a=" + colorCoords.getCieA() + " b=" + colorCoords.getCieB() + " C=" + colorCoords.getCieC() + " h=" + colorCoords.getCieH()  + " " + "RGB is " + colorCoords.getRgbRed() + " " + colorCoords.getRgbGreen() + " " + colorCoords.getRgbBlue() + " " + colorCoords.getRgbHex());
			assertNotNull(colorCoords);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			assertNotNull(e);
		}
	}
	
	@Test
	public void xyzToRgbTest1 (){
		int[] RGB = target.XYZtoRGB(74.4822D, 77.7904D, 71.1914D);  //ICI-GLIDDEN 673
		System.out.println("ICI 673 RGB is " + RGB[0] + " " + RGB[1] + " " + RGB[2]);
		assertNotNull(RGB);
	}
	
	@Test
	public void xyzToRgbTest2 (){
		int[] RGB = target.XYZtoRGB(54.3793D, 57.2902D, 60.8645D);  //BEN MOORE
		System.out.println("Ben Moore RGB is " + RGB[0] + " " + RGB[1] + " " + RGB[2]);
		assertNotNull(RGB);
	}
	

	
	
	@Test
	public void curveToRgbTest1(){ //SW6106 - Kilim Beige
		double[] curve = {8.5454D,9.8891D,13.0628D,20.4314D,31.6782D,39.3078D,41.9917D,43.7609D,45.9493D,47.5962D,48.1183D,48.3551D,48.7558D,49.5168D,50.8593D,52.5787D,54.5094D,56.5196D,58.3763D,59.9248D,61.3052D,62.8414D,64.1409D,64.8976D,65.1403D,65.0295D,64.8936D,64.6806D,64.5444D,64.4543D,64.3829D,64.3577D,64.229D,64.0975D,63.898D,63.7873D,63.8011D,63.9099D,63.9851D,63.8023D};
		ColorCoordinates colorCoords = colorCoordsCalc.getColorCoordinates(curve);
		System.out.println("SW6016 XYZ using double (moussa) is " + colorCoords.getCieX() + " " + colorCoords.getCieY() + " " + colorCoords.getCieZ() + " L=" + colorCoords.getCieL() + " a=" + colorCoords.getCieA() + " b=" + colorCoords.getCieB() + " C=" + colorCoords.getCieC() + " h=" + colorCoords.getCieH() + " " + "RGB is " + colorCoords.getRgbRed() + " " + colorCoords.getRgbGreen() + " " + colorCoords.getRgbBlue() + " " + colorCoords.getRgbHex());
		assertNotNull(colorCoords);
	}
	
	
	@Test
	public void curveToXyzTest2(){ //SW6106 - Kilim Beige
		BigDecimal[] curve = {new BigDecimal(8.5454),new BigDecimal(9.8891),new BigDecimal(13.0628),new BigDecimal(20.4314),new BigDecimal(31.6782),new BigDecimal(39.3078),new BigDecimal(41.9917),new BigDecimal(43.7609),new BigDecimal(45.9493),new BigDecimal(47.5962),new BigDecimal(48.1183),new BigDecimal(48.3551),new BigDecimal(48.7558),new BigDecimal(49.5168),new BigDecimal(50.8593),new BigDecimal(52.5787),new BigDecimal(54.5094),new BigDecimal(56.5196),new BigDecimal(58.3763),new BigDecimal(59.9248),new BigDecimal(61.3052),new BigDecimal(62.8414),new BigDecimal(64.1409),new BigDecimal(64.8976),new BigDecimal(65.1403),new BigDecimal(65.0295),new BigDecimal(64.8936),new BigDecimal(64.6806),new BigDecimal(64.5444),new BigDecimal(64.4543),new BigDecimal(64.3829),new BigDecimal(64.3577),new BigDecimal(64.229),new BigDecimal(64.0975),new BigDecimal(63.898),new BigDecimal(63.7873),new BigDecimal(63.8011),new BigDecimal(63.9099),new BigDecimal(63.9851),new BigDecimal(63.8023)};
		ColorCoordinates colorCoords = target.CurveToXYZLAB(curve, "D65");
		System.out.println("SW6016 XYZ using BiGDec (brianp) is " + colorCoords.getCieX() + " " + colorCoords.getCieY() + " " + colorCoords.getCieZ() + " L=" + colorCoords.getCieL() + " a=" + colorCoords.getCieA() + " b=" + colorCoords.getCieB() + " C=" + colorCoords.getCieC() + " h=" + colorCoords.getCieH()  + " " + "RGB is " + colorCoords.getRgbRed() + " " + colorCoords.getRgbGreen() + " " + colorCoords.getRgbBlue() + " " + colorCoords.getRgbHex());
		assertNotNull(colorCoords);
	}
	

	
	@Test
	public void curveToRgbTest3(){ //SW6248 - Storm Cloud
		double[] curve = {7.2754,8.6559,11.4615,17.2487,24.4675,28.0888,28.7354,28.6466,28.4797,28.223,27.9469,27.6419,27.3288,26.9506,26.549,26.1316,25.5867,25.0029,24.5819,24.2308,23.6878,23.182,22.9215,22.7052,22.3842,22.0384,21.8844,21.7479,21.7086,21.7169,21.6972,21.5592,21.2907,20.9577,20.6001,20.3684,20.3985,20.5927,20.8351,20.7876};
		ColorCoordinates colorCoords = colorCoordsCalc.getColorCoordinates(curve);
		System.out.println("SW6248 XYZ using double (moussa) is " + colorCoords.getCieX() + " " + colorCoords.getCieY() + " " + colorCoords.getCieZ() + " L=" + colorCoords.getCieL() + " a=" + colorCoords.getCieA() + " b=" + colorCoords.getCieB() + " C=" + colorCoords.getCieC() + " h=" + colorCoords.getCieH() + " " + "RGB is " + colorCoords.getRgbRed() + " " + colorCoords.getRgbGreen() + " " + colorCoords.getRgbBlue() + " " + colorCoords.getRgbHex());
		assertNotNull(colorCoords);
	}
	
	@Test
	public void curveToRgbTest4(){ //SW6248 - Storm Cloud
		BigDecimal[] curve = {new BigDecimal(7.2754),new BigDecimal(8.6559),new BigDecimal(11.4615),new BigDecimal(17.2487),new BigDecimal(24.4675),new BigDecimal(28.0888),new BigDecimal(28.7354),new BigDecimal(28.6466),new BigDecimal(28.4797),new BigDecimal(28.223),new BigDecimal(27.9469),new BigDecimal(27.6419),new BigDecimal(27.3288),new BigDecimal(26.9506),new BigDecimal(26.549),new BigDecimal(26.1316),new BigDecimal(25.5867),new BigDecimal(25.0029),new BigDecimal(24.5819),new BigDecimal(24.2308),new BigDecimal(23.6878),new BigDecimal(23.182),new BigDecimal(22.9215),new BigDecimal(22.7052),new BigDecimal(22.3842),new BigDecimal(22.0384),new BigDecimal(21.8844),new BigDecimal(21.7479),new BigDecimal(21.7086),new BigDecimal(21.7169),new BigDecimal(21.6972),new BigDecimal(21.5592),new BigDecimal(21.2907),new BigDecimal(20.9577),new BigDecimal(20.6001),new BigDecimal(20.3684),new BigDecimal(20.3985),new BigDecimal(20.5927),new BigDecimal(20.8351),new BigDecimal(20.7876)};
		ColorCoordinates colorCoords = target.CurveToXYZLAB(curve,"D65");
		System.out.println("SW6248 XYZ using BigDec (brianp) is " + colorCoords.getCieX() + " " + colorCoords.getCieY() + " " + colorCoords.getCieZ() + " L=" + colorCoords.getCieL() + " a=" + colorCoords.getCieA() + " b=" + colorCoords.getCieB() + " C=" + colorCoords.getCieC() + " h=" + colorCoords.getCieH()  + " " + "RGB is " + colorCoords.getRgbRed() + " " + colorCoords.getRgbGreen() + " " + colorCoords.getRgbBlue() + " " + colorCoords.getRgbHex());
		assertNotNull(colorCoords);
	}
	
//	@Test
//	public void curveToXyzTest5(){ //SW6106 - Kilim Beige
//		CdsColorStand colorStand = colorStandDao.readByEffectiveVersion("SHERWIN-WILLIAMS", "6106", new Date(), "2DK");
//		BigDecimal[] curve = colorStand.getCurve();
//		ColorCoordinates colorCoords = target.CurveToXYZLAB(curve, "D65");
//		System.out.println("SW6016 (from db read) XYZ using BigDec (brianp) is " + colorCoords.getCieX() + " " + colorCoords.getCieY() + " " + colorCoords.getCieZ() + " L=" + colorCoords.getCieL() + " a=" + colorCoords.getCieA() + " b=" + colorCoords.getCieB() + " C=" + colorCoords.getCieC() + " h=" + colorCoords.getCieH()  + " " + "RGB is " + colorCoords.getRgbRed() + " " + colorCoords.getRgbGreen() + " " + colorCoords.getRgbBlue() + " " + colorCoords.getRgbHex());
//		assertNotNull(colorCoords);
//	}
//	
//	@Test
//	public void curveToRgbTest6(){ //SW6106 - Kilim Beige
//		CdsColorStand colorStand = colorStandDao.readByEffectiveVersion("SHERWIN-WILLIAMS", "6106", new Date(), "2DK");
//		double[] curve= {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
//		for(int i=0;i<40;i++){
//			curve[i] = colorStand.getCurve()[i].doubleValue();
//		}
//		ColorCoordinates colorCoords = colorCoordsCalc.getColorCoordinates(curve);
//		System.out.println("SW6016 (from db read) XYZ using double (moussa) is " + colorCoords.getCieX() + " " + colorCoords.getCieY() + " " + colorCoords.getCieZ() + " L=" + colorCoords.getCieL() + " a=" + colorCoords.getCieA() + " b=" + colorCoords.getCieB() + " C=" + colorCoords.getCieC() + " h=" + colorCoords.getCieH()  + " " + "RGB is " + colorCoords.getRgbRed() + " " + colorCoords.getRgbGreen() + " " + colorCoords.getRgbBlue() + " " + colorCoords.getRgbHex());
//		assertNotNull(colorCoords);
//	}
	
	

}
