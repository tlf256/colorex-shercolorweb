package com.sherwin.shercolor.swdevicehandler.domain;

import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class ErrorMessages {
	HashMap<Long,String> map = new HashMap<Long,String>();
	HashMap<Long,String> rc_map = new HashMap<Long,String>();
	
	Locale locale = new Locale("en", "US");
	ResourceBundle resourceBundle = ResourceBundle.getBundle("SWCorobErrors",locale);
	public static final long WARN_TEXT_IN_BUFFER = -10500;
	public static final long ERR_TEXT_IN_BUFFER = -3084;
	static Logger logger = LogManager.getLogger(ErrorMessages.class);
	
	public String getInternationalErrorMessage(long errorCode) {
		String returnString;
		try {
			System.out.println("in getInternationalErrorMessage");
			String hexErrorCode = String.format("%x",errorCode);
			returnString = resourceBundle.getString(hexErrorCode);
			System.out.println("return message is " + returnString);
			
		} catch (Exception e) {
			//odds are error is because we cannot get error from the resourceBundle.
			e.printStackTrace();
			logger.error(e.getMessage());
			returnString = "";
		}
		return returnString;
	}
	public ErrorMessages(){
		rc_map.put((long) 1, "Tinter Error.");
		rc_map.put((long)-1, "Tinter Error. Check comm cable, calibration files.");
		rc_map.put((long) 2, "Command not recognized");
		rc_map.put((long) 3, "Comm Error. Check Cable, STOP Button");
		rc_map.put((long) 4, "Command Refused. Tinter Error");
		rc_map.put((long) 5, "Tinter Process Timed Out");
		rc_map.put((long) 6, "Tinter Stirring Error");
		rc_map.put((long) 7, "Tinter Solenoid Error.");
		rc_map.put((long) 8, "Can sensor not detecting can");
		rc_map.put((long) 9, "Error, Can too big");
		rc_map.put((long) 10, "Error, Door Open");
		rc_map.put((long) 11, "Upper Shelf Error");
		rc_map.put((long) 12, "Can punch error");
		rc_map.put((long) 13, "Tinter Nozzle Error");
		rc_map.put((long) 14, "Tinter Dispense Error");
		rc_map.put((long) 15, "Tinter Hardware Error");
		
		rc_map.put((long) 16, "Comm port not found, Insert USB to Serial adapter cable and detect again.");
		
		map.put((long)0x100,"Communication error with motor-pump unit");
		map.put((long)0x200,"Check canister level");
		map.put((long)0x300,"Process error on motor-pump unit");
		map.put((long)0x0400,"Single recirc_mapulation/agitation conflict");
		map.put((long)0x0500,"Single agitation/recirculation conflict");
		map.put((long)0x0600," Recirculation dual circuit single recirculation conflict");
		map.put((long)0x0700," Valve in short circuit");
		map.put((long)0x0800," Valve disconnected");
		map.put((long)0x0900," TPIC damaged");
		map.put((long)0x0A00," Agitation in short circuit");
		map.put((long)0x0B00," Agitation is disconnected");
		map.put((long)0x0C00," Amount to be dispensed=0 in the circuit");
		map.put((long)0xD00,"Ramp quantity to be dispensed in the circuit too small");
		map.put((long)0xE00,"Motor-pump unit disabled by the machine");
		map.put((long)0xF00,"Motor-pump unit not configured");
		map.put((long)0x1100,"Encoder on motor-pump unit out of order");
		map.put((long)0x1200,"No pump zero signal on motor-pump unit");
		map.put((long)0x1300,"Motor-pump unit not initialized");
		map.put((long)0x1700,"Inverter blocked on motor-pump unit");
		map.put((long)0x1800,"No voltage on motor-pump unit eprom");
		map.put((long)0x1900,"No voltage on motor-pump unit inverter");
		map.put((long)0x1A00,"Wrong commands on motor-pump unit");
		map.put((long)0x1B00,"Wrong dispensing tables on motor-pump unit");
		map.put((long)0x1C00,"No mains power supply to motor-pump unit");
		map.put((long)0x1D00,"CME error on motor-pump unit");
		map.put((long)0x1E00,"System error on motor-pump unit");
		map.put((long)0x1F00,"ILLEGAL OPERATION on motor-pump unit");
		map.put((long)0x2000,"W_DOG on motor-pump unit");
		map.put((long)0x2200,"Error in pump suction (inlet valve closed)");
		map.put((long)0x2300,"Error in pump delivery (outlet valve closed)");
		map.put((long)0x2400,"The stirring has been switched off");
		map.put((long)0x2500,"The stirring contactor is off");
		map.put((long)0x2600,"The stirring thermal magnetic switch is off");
		map.put((long)0x2700,"Error in flowmeter status");
		map.put((long)0x2800,"Pump overpressure in delivery");
		map.put((long)0x2900,"Pump overpressure in suction");
		map.put((long)0x2A00,"Overtemperature");
		map.put((long)0x2B00,"Minimum level in canister reached");
		map.put((long)0x2C00,"Delivery pressure sensor disconnected");
		map.put((long)0x2D00,"Suction pressure sensor disconnected");
		map.put((long)0x2E00,"Temperature sensor disconnected");
		map.put((long)0x2F00,"Level sensor in canister disconnected");
		map.put((long)0x3000,"Valve in short circuit");
		map.put((long)0x3100,"Valve disconnected");
		map.put((long)0x3200,"Stirring in short circuit");
		map.put((long)0x3300,"Stirring disconnected");
		map.put((long)0x4400,"Check valve signal not received");
		//Bellows machine errors

		map.put((long)0x7B00,"Error in positioning the carriage");
		map.put((long)0x7C00,"Blocked motor");
		map.put((long)0x7D00,"Bad stroke");
		map.put((long)0x7E00,"Error in command timeout");
		map.put((long)0x7F00,"Status error on pumping group");
		map.put((long)0,"Success");

		map.put((long)0x4000," Cap handling error");
		map.put((long)0x4001," Cap handling error, x4001 occurring in stores causing startup timeout; cause is nozzle cap too closing past home so cap is really tight");
		map.put((long)0x4100,"Communication error (circuits) between Trigen1 and Trigen2");
		map.put((long)0x4200,"Communication error (stirrings) between Trigen1 and Trigen2");
		map.put((long)0x4300,"Address error on Trigen2")	;								// per Pete, cap is actually closing past sensor");
		map.put((long)0x7000,"Unknown error");
		map.put((long)0x7002,"CPU Error");
		map.put((long)0x7003,"CME Error");
		map.put((long)0x7004,"Illegal operation error");
		map.put((long)0x7005,"CORTOS system error (dispenser OS error");
		map.put((long)0x7006,"Watchdog error");
		map.put((long)0x7007,"Line 485 Busy");

		map.put((long)0x7008,"Unknown serious error");
		map.put((long)0x7010,"Error while creating a task");
		map.put((long)0x7011,"Priority error");
		map.put((long)0x7012,"Error on a semaphore");
		map.put((long)0x7013,"Overflow semaphore error");
		map.put((long)0x7014,"Error while deleting a task");
		map.put((long)0x7015,"Error while deleting an idle task");
		map.put((long)0x7016,"Error resources not available");
		map.put((long)0x7020,"Bus Error");
		map.put((long)0x7021,"Address Error");
		map.put((long)0x7022,"Division by zero");
		map.put((long)0x7023,"Privilege violation");
		map.put((long)0x7024,"Error line 1010 on dispenser");
		map.put((long)0x7025,"Error line 1111 on dispenser");
		map.put((long)0x7026,"Format error");
		map.put((long)0x7027,"Spurious interrupt");
		map.put((long)0x7028,"Hardware breakpoint");

		map.put((long)0x8000," Photocell disengaged");
		map.put((long)0x8002,"TOP button depressed");

		map.put((long)0x8001,"Machine encoder is Out Of Order");
		map.put((long)0x8003,"System power failure during dispensing");
		map.put((long)0x8004,"Serious communication error");

		map.put((long)0x8005,"Error booster is missing");
		map.put((long)0x8006,"agitators -stirrings switched off (thermal magnetic switches)");

		map.put((long)0x8007,"Too many single process activated");
		map.put((long)0x8008,"Machine pump zero signal error");
		map.put((long)0x8009,"Encoder 1 is missing on the shelf");
		map.put((long)0x800a,"No power supply on the shelf");
		map.put((long)0x800b,"Encoder 2 is missing on the shelf");
		map.put((long)0x800c,"Reference Plane not reached");
		map.put((long)0x800d,"2 Sensors of fast shelf are inversed");
		map.put((long)0x800E,"Release the STOP button,");

		map.put((long)0x800f,"Formula does not exist");
		map.put((long)0x8010,"Punch error");
		map.put((long)0x8011,"Error: Lid of can is missing");
		map.put((long)0x8012,"Single recirculation in progress");
		map.put((long)0x8013,"Single agitation in progress");
		map.put((long)0x8014,"Module i/o configuration error");
		map.put((long)0x8015,"Shelf stroke deceleration,");
		map.put((long)0x8016,"Machine secondary pump zero signal error,");

		map.put((long)0x8500,"Error on cap movement");
		map.put((long)0x8501,"Error cap closed during dispensing");
		map.put((long)0x9000,"Error: configuration is missing");
		map.put((long)0x9001,"CRC error in program file");
		map.put((long)0x9002,"General configuration error");
		map.put((long)0x9003,"Error while receiving XMODEM");
		map.put((long)0x9004,"Error on dispenser address");

		// 05/05/2014 DJM - MORE OBSCURE CPS ERRORS
		map.put((long)0x9100,"Error in the configuration file, in the command @GEN");
		map.put((long)0x9120,"Error in the configuration file, in the command @TEM");
		map.put((long)0x9130,"Error in the configuration file, in the command @SGA");
		map.put((long)0x9150,"Error in the configuration file, in the command @TAP");
		map.put((long)0x9160,"Error in the configuration file, in the command @TA3");
		map.put((long)0x9170,"Error in the configuration file, in the command @CIR");
		map.put((long)0x9180,"Error in the configuration file, in the command @MOT");
		map.put((long)0x91A0,"Error in the configuration file, in the command @AGI");
		map.put((long)0x91B0,"Error in the configuration file, in the command @AG2");
		map.put((long)0x91C0,"Error in the configuration file, in the command @ILE");
		map.put((long)0x91D0,"Error in the configuration file, in the command @IVE");
		map.put((long)0x91E0,"Error in the configuration file, in the command @VEL");
		map.put((long)0x91F0,"Error in the configuration file, in the command @MEX");
		map.put((long)0x9200,"Error in the configuration file, in the command @IND");
		map.put((long)0x9210,"Error in the configuration file, in the command @LIN");
		map.put((long)0x9220,"Error in the configuration file, in the command @IDI");
		map.put((long)0x9230,"Error in the configuration file, in the command @IAN");

		map.put((long)0xD000,"Dispensing interrupted, check last can");
				map.put((long)0xD001,"Valves air pressure is low");
		map.put((long)0xD002,"Valves air pressure is low during dispensing");
		map.put((long)0xD030,"Timer list not created");
		map.put((long)0xD031,"No free timer");
		map.put((long)0xF000,"Error in positioning the carriage");
		map.put((long)0xF100,"Blocked motor");
		map.put((long)0xF200,"Bad stroke");
		map.put((long)0xF701,"Line 485 is busy");
		map.put((long)0xF900,"Error in command timeout");
		map.put((long)0xFA00,"Status error on pumping group");
		map.put((long)0x8017,"CBSafe: relay contact damaged");
		map.put((long)0x8018,"CBSafe: incorrect wiring");
		map.put((long)0x8019,"CBSafe damaged or disconnected");
		map.put((long)0x801A,"Punching not completed");
		map.put((long)0x801B,"The buttons have been released during punching");
		map.put((long)0x801C,"Shelf upper position sensor obscured before the end of punching");
		map.put((long)0x801D,"Impossible to detect humidity value");
		map.put((long)0x801E,"Impossible to detect temperature value");
		map.put((long)0x8502,"Inlet compressed air missing");
		map.put((long)0x8503,"Automatic washing - Water exhausted");
		map.put((long)0x8504,"Lavaggio automatico - Vaschetta di scarico piena");
		map.put((long)0xE000,"Carriage overtravel sensor obscured");
		map.put((long)0xE001,"The motor is not engaged");
		map.put((long)0xE002,"Optic fork not obscured in recharge");
		map.put((long)0xE003,"Incorrect circuit selected");
		map.put((long)0xE004,"With motor engaged, the optic fork is not obscured in recharge");
		map.put((long)0xE005,"With motor not engaged, the optic fork is not obscured in recharge");
		map.put((long)0xE006,"The optic fork is obscured, with motor not engaged");

		//DJM 05/6/14 more Corob errors from ErrorList.xlsx
		map.put((long)0x47,"Unknown command");
		map.put((long)0x48,"Error in command parameter");
		map.put((long)0x49,"Error: pulses arrive when carriage is moving");
		map.put((long)0x46,"No BT signal");
		map.put((long)0x45,"Serial stop");
		map.put((long)0x43,"Can missing on the table when dispensing");
		map.put((long)0x41,"Frequency out of allowed range");
		map.put((long)0x40,"Too long pump pulse");
		map.put((long)0x3F,"Too short pump pulse");
		map.put((long)0x3C,"Too long command");
		map.put((long)0x36,"Reset flag not set");
		map.put((long)0x33,"Paste level too low");
		map.put((long)0x30,"Frequency error when dispensing");
		map.put((long)0x2E,"Supply voltage error when dispensing");
		map.put((long)0x2D,"Empty formula");
		map.put((long)0x2C,"Too small paste amount");
		map.put((long)0x2B,"Max no. of open valves not defined");
		map.put((long)0x2A,"Dispensing already in progress");
		map.put((long)0x29,"Too many outputs");
		map.put((long)0x28,"Too many inputs");
		map.put((long)0x27,"Valve amount error (SW-limit exceeding)");
		map.put((long)0x26,"Valve amount error (card ID error)");
		map.put((long)0x25,"No more space for tasks");
		map.put((long)0x20,"Board supply voltage out of range");
		map.put((long)0x1F,"Board temperature out of range");
		map.put((long)0x1D,"Impossible to write on EEprom (LONG)");
		map.put((long)0x1C,"Impossible to write on EEprom (INT)");
		map.put((long)0x1B,"Impossible to write on EEprom (CHAR)");
		map.put((long)0x4A,"Undefined signal (HSI0)");
		map.put((long)0x4B,"Undefined signal (HSI1)");
		map.put((long)0x4C,"Undefined signal (HSI2)");
		map.put((long)0x4D,"Undefined signal (HSI3)");
		map.put((long)0x50,"Solenoid activation timeout");
		map.put((long)0x51,"System parameters out of range");
		map.put((long)0x1389,"Position sensor not ON when starting to move carriage");
		map.put((long)0x138E,"Not in zero sensor");
		map.put((long)0x138D,"Position sensor does not change back ON when carriage is moved left");
		map.put((long)0x138C,"Position sensor not ON when stopping carriage");
		map.put((long)0x138B,"Position sensor does not change OFF when moving carriage");
		map.put((long)0x138A,"Position sensor does not change back ON when moving carriage");
		map.put((long)0x1390,"Punching sensor active");
		map.put((long)0x1395,"No pulse arrives");
		map.put((long)0x170C,"Timeout - going to zero sensor");
		map.put((long)0x1712,"Dispensing not allowed in base state");
		map.put((long)0x177A,"Pump error, after positioning");
		map.put((long)0x1785,"Not on backward sensor or unknown pump AFTER TOZERO");
		map.put((long)0x1711,"Impossible to continue dispensing");
		map.put((long)0x1710,"Unknown ramp type");
		map.put((long)0x170F,"Wrong parameter for pump number");
		map.put((long)0x170E,"Braking ramps length greater than driving distance");
		map.put((long)0x170D,"Timeout - from zero sensor");
		map.put((long)0x15E1,"IO-board: no inputs");
		map.put((long)0x15E0,"IO-board: no outputs");
		map.put((long)0x1397,"Wrong pulse number");
		map.put((long)0x1396,"Pulses arriving when motor stops");
		map.put((long)0x1394,"No pulses when motor starts");
		map.put((long)0x1392,"Pump count error");
		map.put((long)0x1B58,"Paste level sensor not activated");
		map.put((long)0x1B5B,"Nozzle sensor not activated");
		map.put((long)0x1B5A,"Valve does not stay open");
		map.put((long)0x1B59,"Punch sensor not activated");
		map.put((long)0x1789,"Canister position sensor broken or base sensor not ON");
		map.put((long)0x1788,"Base sensor not ON when sw assumes it should be");
		map.put((long)0x1787,"Base and position sensor both ON");
		map.put((long)0x1786,"Not on backward sensor when reading canister position sensor");
		map.put((long)0x177E,"Nozzle open");
		map.put((long)0x177D,"Nozzle closed");
		map.put((long)0x177C,"Cannot find BASE sensor");
		map.put((long)0x177B,"No punch position sensor defined");
		map.put((long)0x1775,"Backward sensor does not change ON when carriage drives to left");
		map.put((long)0x1773,"Valve does not open");
		map.put((long)0x1772,"Cannot locate carriage position on backward sensor");
		map.put((long)0x1771,"Valve open");
		map.put((long)0x1713,"COVER open");
		map.put((long)0x1B5C,"Paste not defined (pump not defined in EEprom configuration).");
		map.put((long)0x9005,"Watchdog timeout");
		map.put((long)0x9009,"Valve opening main solenoid timeout");
		map.put((long)0xA001,"Timeout error in dispensing start");
		map.put((long)0xA006,"Timeout error in version query");
		map.put((long)0xA00A,"Timeout error in pulse test command");
		map.put((long)0xA00C,"Error in reading the peripheral board zero sensor");
		map.put((long)0xA003,"Timeout error in peripheral board response to the reset");
		map.put((long)0xA002,"Timeout error in peripheral board configuration");
		map.put((long)0x9024,"Motor runs in an unsteady way (zero sensor)");
		map.put((long)0x9023,"Motor runs in an unsteady way");
		map.put((long)0x9022,"Wrong encoder pulses frequency");
		map.put((long)0x9021,"CPU hardware reset (RST input pin)");
		map.put((long)0x9020,"CPU hardware reset (HST input pin)");
		map.put((long)0x9015,"Autocap never detected by sensor");
		map.put((long)0x9014,"Autocap always detected by sensor");
		map.put((long)0x9011,"Pulse sensor faulty");
		map.put((long)0x9010,"Zero sensor faulty");
		map.put((long)0x9008,"Autocap opening solenoid timeout");
		map.put((long)0x9007,"Valve opening solenoid timeout");
		map.put((long)0x9006,"Clip solenoid timeout");
		map.put((long)0xA005,"Timeout error in valve opening command");
		map.put((long)0xA00B,"Error in pulse reading test for peripheral board");
		map.put((long)0xA009,"Peripheral board configuration error");
		map.put((long)0xA008,"Timeout error in configuration command");
		map.put((long)0xA007,"Peripheral board version error");
		map.put((long)0xFB00,"Humidity value too low");
		map.put((long)0xF42D,"Check valve signal not received");
		map.put((long)0x9025,"Motor runs too slowly");
		map.put((long)0x9030,"Length of the data field in the message different than expected");
		map.put((long)0xF52D,"Error - circuit with Check valve is empty");
		map.put((long)0xF52F,"Number of encoder steps exceeded in shelf lowering movement");
		map.put((long)0xF52E,"Number of encoder steps exceeded in shelf raising movement");
		map.put((long)0x9034,"Encoder frequency too low during pulse reading test or during alignment");
		map.put((long)0x9033,"Error in pulse reading test - 0 sensor not working");
		map.put((long)0x9032,"OFF-line button depressed during bar positioning");
		map.put((long)0x9031,"The CPU does not move the autocap anymore");
		map.put((long)0x029,"CRC error in the message");
		map.put((long)0x9028,"First 2 characters of CRC are corrupted");
		map.put((long)0x9027,"String ending character ETX not found in the message");
		map.put((long)0x9026,"String beginning character SOH not found in the message");
		map.put((long)0x801F,"No signal from check valves");
		map.put((long)0xD003,"Pressure level exceeded in a circuit");
		map.put((long)0xF530,"Punch error: cap free sensor active in spite of the end of punching");
		map.put((long)0xF531,"Punch error: no signal from cap free sensor");
		map.put((long)0xF532,"During Autotest, the humidity value is always higher than the minimum threshold");
		map.put((long)0xF533,"During Autotest, the humidity value is always lower than the maximum threshold");
		map.put((long)0xA00D,"Peripheral board formula error");
		map.put((long)0x4500,"Error in turntable movement toward a circuit");
		map.put((long)0xF53E,"Error while searching the unlocked position of the valve");
		map.put((long)0xF53D,"Error while searching the closed position of the valve");
		map.put((long)0xF53C,"Error while searching the disengaged position of the valve");
		map.put((long)0xF53B,"Errors related to the pumping group: optic fork not detected or motor blocked");
		map.put((long)0xF53A,"Error in pumping group movement");
		map.put((long)0xF539,"Error: circuit misplaced");
		map.put((long)0xF538,"Error in the self-recognition procedure on the turntable");
		map.put((long)0xF537,"Timeout error in the self-recognition procedure on the turntable");
		map.put((long)0xF536,"Error: mismatch between circuits in conf. file and those detected on turntable");
		map.put((long)0xF535,"Error while searching the reference on turntable");
		map.put((long)0xF534,"Error in turntable movement");
		map.put((long)0x8021,"Temperature too low in the machine");
		map.put((long)0x8020,"Error in sensor heating process for humidifier");
		map.put((long)0x4600,"Circuit error: mismatch between position on turntable and machine conf. file");
		map.put((long)0xF53F,"Error while searching the open position on the valve");
		map.put((long)0xF544,"Error on 4GTTD board: one or more circuits detected are in the wrong position");
		map.put((long)0xF543,"Error on 4GTTD board: detected one circuit not stored on EEprom");
		map.put((long)0xF542,"Error on 4GTTD board: one circuit stored on EEprom was not detected");
		map.put((long)0xF541,"Error in writing on EEprom of the 4GTTD board");
		map.put((long)0xF540,"Error on the turntable motor group");
		map.put((long)0xFC91,"Error in managing Statistics");
		map.put((long)0xF545,"Failed to jump to boot loader firmware");
		map.put((long)0x4700,"Failed to update firmware");
		map.put((long)0xFC92,"Warning level in canister reached");
		map.put((long)0xF300,"Command on line 485 refused");
		map.put((long)0xFC96,"Can too big");
		map.put((long)0xFC95,"Syntax error");
		map.put((long)0xFC94,"Configuration error");
		map.put((long)0xFC93,"Can missing or too small");
		map.put((long)0xFB01,"Signal lost from can presence photocell");
		map.put((long)0xD000," Dispensing interrupted, check last can");
	}

	public HashMap<Long, String> getMap() {
		return map;
	}

	public HashMap<Long, String> getRC_map() {
		return rc_map;
	}
	public Locale getLocale() {
		return locale;
	}
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	
	
}
