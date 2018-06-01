
var sessionTinterInfo=null;



function Ecal(mycolorant,mymodel,myserial,myuploaddate,myuploadtime,myfilename){
	
	this.colorantId=mycolorant;
	this.tinterModel=mymodel;
	this.tinterSerial=myserial;
	this.uploaddate=myuploaddate;
	this.uploadtime=myuploadtime;
	this.filename=myfilename;
	
	
}
function getColorantIds(){
	var data = getModels();
	var colorantIds = [];
	for(i = 0; i< data.length; i++){    
	    if(colorantIds.indexOf(data[i].colorantId) === -1){
	        colorantIds.push(data[i].Id);        
	    }        
	}
	return colorantIds;
}

function getModels(){
	var callist=null;
	
	 $.getJSON({
		url: "getEcalsByCustomer",
		context: document.body,
		data: { 
			reqGuid : reqGuid, //without this guid you will get a login exception and you won't even get an error
			customerId: "DEFAULT"
			},
			async: false,
		type: "POST",
			 dataType: "json",
			    success: function (cals) {
			    	if(cals.sessionStatus === "expired"){
                		window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
                	}
                	else{
                		callist = cals;
                	}
			    },
				error: function(){
					callist="error";
					alert("Could not find calibration files");
				}
	});
	 
	 for (var i = 0;  i < 10; i++){
			setTimeout(null, 300);
		}
	
	return callist;
}

function getGDataFromServer(colorantid){
	var gdataobj=null;
	if(reqGuid !=null){
		$.ajax({
			url : "selectGData",
			context : document.body,
			data : {
				reqGuid : reqGuid, //without this guid you will get a login exception and you won't even get an error
				colorantid: colorantid
			},
			async : false,
			type: "POST",
			dataType: "json",
			success: function (obj) {
				console.log("gdata received");
				if(obj.sessionStatus === "expired"){
            		window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
            	}
            	else{
            		gdataobj = obj;
            	}
			},
			error: function(error){
				gdataobj="error";
				console.log(error);
				alert("Could not find gdata for " + colorantid);
			}
		});
	}
	else {
		alert("Not logged in.  ReqGuid not found");
	}

	return gdataobj;
}

function getCalFromServer(uFilename){
	var calobj=null;
	if(reqGuid !=null){
		$.ajax({
			url : "selectECal",
			context : document.body,
			data : {
				reqGuid : reqGuid, //without this guid you will get a login exception and you won't even get an error
				filename: uFilename
			},
			async : false,
			type: "POST",
			dataType: "json",
			success: function (cal) {
				//console.log(calobj);
				console.log(encodeURI(calobj));
				if(cal.sessionStatus === "expired"){
            		window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
            	}
            	else{
            		calobj = cal;
            	}
			},
			error: function(error){
				calobj="error";
				console.log(error);
				alert("Could not find calibration template for " + uFilename);
			}
		});
	}
	else {
		alert("Not logged in.  ReqGuid not found");
	}

	return calobj;
}



function Calibration_Download(filename){
	this.filename = filename
	this.data = null;
	var cal = getCalFromServer(filename);
	if(cal !=null && cal.filename != null && cal.data != null){
		this.filename = cal.filename;
		this.data=cal.data;
	}
	else{
		alert("Could not find Calibration for colorant:" + mycolorant + " and model:" + mymodel);
	}
	
}
function GData(mycolorant){
	this.colorantid = mycolorant;
	this.data = null;
	
	
	var gdata_file = getGDataFromServer(mycolorant);
	if(gdata_file !=null && gdata_file.data != null){
	
		this.data=gdata_file.data;
	}
	else{
		alert("Could not find Default GData for colorant:" + mycolorant  );
	}
}

function Calibration(mycolorant,mymodel,myserial){
	
	this.filename=null;
	this.data=null;
	
	/*
	var date = new Date();
	var month = date.getMonth() + 1;
	if (month.length == 1){
		month = "0" + month;
	}
	var day = date.getDate();
	if(day.length == 1){
		day = "0" + day;
	}
	var time = date.getHours() + "" + date.getMinutes();
	var fulldate = date.getFullYear()+ "" + month + "" + day;
	*/
	/* no way to know which date time to use for file for Ecal, so we are just going to download the template
	if (myserial.length != 0) {
		var filename = mycolorant+"_"+mymodel+"_"+myserial+"_"+fulldate +"_" + time;
		var uFilename= filename.toUpperCase() + ".zip";
		uFilename = uFilename.replace(/\s+/g, '');
		var cal = getCalFromServer(uFilename);
		if(cal.filename != null){
			this.filename = cal.filename;
			this.data=cal.data;
		}
	
	}
	*/
	if(this.data == null){
		var filename = mycolorant+"_"+mymodel;
		var uFilename= filename.toUpperCase() + ".zip";
		uFilename = uFilename.replace(/\s+/g, '');
		var cal = getCalFromServer(uFilename);
		if(cal !=null && cal.filename != null && cal.data != null){
			this.filename = cal.filename;
			this.data=cal.data;
		}
		else{
			// handle alert in config  -- alert("Could not find Default Calibration for colorant:" + mycolorant + " and model:" + mymodel);
		}
	}	
}


function Colorant(code,shots,pos,uom){
	this.code=code;
	this.shots=shots;
	this.uom=uom;
	this.position=pos;
	
}
    /* String clrntCode;
	
	short clrntShots;   // colorant shot array
	short clrntUom;   // colorant shot uom array
	
	short clrntTankPos;   // colorant tank position array
	short clrntPumpPos;   // colorant pump position array
	
	long clrntTankCap;   // colorant tank capacity in ounces NOT times 1000
	long clrntCalcAmt;   // colorant amount calculated by the GTI 
	// in ounces times 1000
	long clrntActAmt ;   // colorant amount actually in tinter (in case 
	// tinter say "can't dispense" 
	// in ounces times 1000
	long clrntLowAlrm;   // Amount where Progress will begin reporting 
	
}*/

function Canister(pos,color){
	this.pump=pos;
	this.code=color;
	//this.tankCap=0;
	//this.calcAmt=0;
	//this.actAmt=0;
	//this.lowAlarmAmt=0;
}

function Configuration(mycolorantSystem,mymodel,myserial,canister_layout){
		
	this.colorantSystem=mycolorantSystem;
	this.model=mymodel;
	this.serial=myserial;
	this.port=null;
	this.canisterLayout=canister_layout;
	this.canisterMap=null;
	
}
function GData(mycolorantId){
	this.colorantId=mycolorantId;
	this.data = null;
	if(this.data == null){
		var gdata_file = getGDataFromServer(mycolorantId);
		if(gdata_file !=null && gdata_file.data != null){
		
			this.data=gdata_file.data;
		}
		else{
			alert("Could not find Default GData for colorant:" + mycolorant  );
		}
		
	}
	console.log(this.gdata);
	
}

function Error(num,message){
	this.num=num;
	this.message=message;
}
function TinterMessage(command,shotList,myconfig,mycalibration,mygdata){
	console.log("In TinterMessage");
	console.log(myconfig);
	this.id=createUUID();
	this.messageName="TinterMessage";
	this.command=command;
	this.configuration=myconfig;
	this.calibration=mycalibration
	this.gdata=mygdata;
	
	this.lastInitDate=null;
	
	//config items
	//this.colorantSystem="";
	//this.model="";
	//this.serial="";
	//this.canisterLayout=null;
	
	
	this.shotList=shotList
	
	//returned items
	this.status = 0;
	this.javaMessage="";
	this.commandRC=0;
	this.errorNumber=0;
	this.errorSeverity=0;
	this.errorMessage=0;
	this.errorList=null;
}
function createUUID() {
    // http://www.ietf.org/rfc/rfc4122.txt
    var s = [];
    var hexDigits = "0123456789abcdef";
    for (var i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
    s[8] = s[13] = s[18] = s[23] = "-";

    var uuid = s.join("");
    return uuid;
}

function TintEventDetail(myType, myName, myQty){
	this.type = myType;
	this.name = myName;
	this.qty = myQty;
}


/***
* @param myGuid - Session Guid from page (i.e. reqGuid) 
* @param myMessage - TinterMessage object
* @param teDetail - array of TintEventDetail
* @param myConfig - Configuration object (canister_layout not required) 
* @returns
*/
function sendTinterEvent(myGuid, myDate, myMessage, teDetail){
	//var mydata = {reqGuid:myGuid, tinterMessage:myMessage, tintEventDetail:teDetail};
	var mydata = {reqGuid:myGuid, eventDate:myDate.toString(), tintEventDetail:teDetail, tinterMessage:myMessage};
	var jsonIn = JSON.stringify(mydata);
	console.log("Logging Tinter Event " + jsonIn);
	$.ajax({
		url: "logTinterEventAction.action",
		contentType : "application/json; charset=utf-8",
		type: "POST",
		data: jsonIn,
		datatype : "json",
		success: function (data) {
			if(data.sessionStatus === "expired"){
        		window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
        	}
        	else{
        		//Handle success after checking expiration
        	}
		}
	});
}

function SessionTinterInfo(myClrntSysId, myModel, mySerial, myLastPurgeDate, myLastPurgeUser, myEcalOnFile, myTinterOnFile, myLastInitErrorList, myCanisterList){
	this.clrntSysId=myClrntSysId;
	this.model=myModel;
	this.serialNbr=mySerial;
	this.lastPurgeDate=myLastPurgeDate;
	this.lastPurgeUser=myLastPurgeUser;
	this.ecalOnFile=myEcalOnFile;
	this.tinterOnFile=myTinterOnFile;
	this.lastInitErrorList=myLastInitErrorList;
	this.canisterList=myCanisterList;
}
function SessionTinterCanister(myPos, myClrntCode, myClrntName, myMaxCanFill, myFillAlarm, myFillStop, myCurrentAmt, myRgbHex){
	this.position=myPos;
	this.clrntCode=myClrntCode;
	this.clrntName=myClrntName;
	this.maxCanisterFill=myMaxCanFill; //double
	this.fillAlarmLevel=myFillAlarm; //double
	this.fillStopLevel=myFillStop; //double
	this.currentClrntAmount=myCurrentAmt; //double
	this.rgbHex=myRgbHex;
}
	
function saveConfigToSession(myGuid, myclrnt, mymodel, myserial) {
	$.ajax({
		url: "stampSessionTinterAction.action",
		//contentType : "text/html; charset=utf-8",
		type: "POST",
		data: {
			reqGuid: myGuid,
			tinterClrnt: myclrnt,
			tinterModel: mymodel,
			tinterSerial: myserial
		},
		datatype : "json",
		async: false,
		success: function (data) {
			if(data.sessionStatus === "expired"){
        		window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
        	}
        	else{
        		//Check result of stamp and if tinter is on DB
    			if(data.tinter.tinterOnFile===false){
    				sessionTinterInfo = new SessionTinterInfo(null,null,null,null,null,null,data.tinter.tinterOnFile,null,null );
    			} else {
    				// Loop through canister list
    				var canList = [];
    				data.tinter.canisterList.forEach(function(listItem){
    					var addCan = new SessionTinterCanister(listItem.position,listItem.clrntCode,listItem.clrntName,listItem.maxCanisterFill,listItem.fillAlarmLevel,listItem.fillStopLevel,listItem.currentClrntAmount,listItem.rgbHex);
    					canList.push(addCan);
    				});
    				// fill in sessionTinterInfo
    				sessionTinterInfo = new SessionTinterInfo(data.tinter.clrntSysId,data.tinter.model,data.tinter.serialNbr,data.tinter.lastPurgeDate,data.tinter.lastPurgeUser,data.tinter.ecalOnFile,data.tinter.tinterOnFile,data.tinter.lastInitErrorList,canList );
    			}
    			//alert("Success: " + data.reqGuid + " tinterOnFile is " + data.tinter.tinterOnFile);
    			console.log(sessionTinterInfo);
        	}
		},
		error: function(err){
			alert("failure: " + err);
		}
	});
}

function saveInitErrorsToSession(myGuid, myInitErrorList){
	console.log("insideSaveInitErrors");
	console.log(myInitErrorList);
	var mydata = {reqGuid:myGuid, initErrorList:myInitErrorList};
	var jsonIn = JSON.stringify(mydata);
	$.ajax({
		url: "stampSessionTinterInitAction.action",
		contentType : "application/json; charset=utf-8",
		type: "POST",
		data: jsonIn,
		datatype : "json",
		async: false,
		success: function (data) {
			if(data.sessionStatus === "expired"){
        		window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
        	}
        	else{
        		//Handle success after checking expiration
        	}
		},
		error: function(err){
			alert("failure: " + err);
		}
	});
	
}

function getSessionTinterInfo(myGuid, callback) {
	$.ajax({
		url: "getSessionTinterAction.action",
		type: "POST",
		data: {
			reqGuid: myGuid
		},
		datatype : "json",
		async: true,
		success: function (data) {
			if(data.sessionStatus === "expired"){
        		window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
        	}
        	else{
        		//Check result of stamp and if tinter is on DB
    			if(data.tinter.tinterOnFile===false){
    				sessionTinterInfo = new SessionTinterInfo(null,null,null,null,null,null,data.tinter.tinterOnFile,null,null );
    			} else {
    				// Loop through canister list
    				var canList = [];
    				data.tinter.canisterList.forEach(function(listItem){
    					var addCan = new SessionTinterCanister(listItem.position,listItem.clrntCode,listItem.clrntName,listItem.maxCanisterFill,listItem.fillAlarmLevel,listItem.fillStopLevel,listItem.currentClrntAmount,listItem.rgbHex);
    					canList.push(addCan);
    				});
    				// fill in sessionTinterInfo
    				sessionTinterInfo = new SessionTinterInfo(data.tinter.clrntSysId,data.tinter.model,data.tinter.serialNbr,data.tinter.lastPurgeDate,data.tinter.lastPurgeUser,data.tinter.ecalOnFile,data.tinter.tinterOnFile,data.tinter.lastInitErrorList,canList );
    			}
    			//alert("Success: " + data.reqGuid + " tinterOnFile is " + data.tinter.tinterOnFile);
    			console.log(sessionTinterInfo);
    			if(typeof callback == 'function'){
    				callback();
    			}
        	}
		},
		error: function(err){
			alert("failure: " + err);
		}
	});
}

/***
 * Function to check if there is enough colorant in the tinter for the colorants being dispensed
 * @param myShotList - Array of Colorant objects that represents what is being dispensed
 * @param mySessionCanList - Array of SessionTinterCanister objects that represents what is in the tinter
 * @returns Array of messages (one per colorant), empty array if all ok
 */
function checkDispenseColorantEmpty(myShotList, mySessionCanList){
	var messageList = [];
	mySessionCanList.forEach(function(myCan){
		myShotList.forEach(function(myShot){
			if(myShot.code==myCan.clrntCode){
				var adjLevel = myCan.currentClrntAmount - (myShot.shots / myShot.uom);
				console.log("empty check adj=" + adjLevel + " stop is " + myCan.fillStopLevel);
				if (adjLevel<=myCan.fillStopLevel) {
					messageList.push("Not enough " + myCan.clrntCode + "-" + myCan.clrntName + " available to dispense.");
				}
			}
		});
	});
	return messageList;
}

/***
 * Function to check if any colorants being dispensed are currently low
 * @param myShotList - Array of Colorant objects that represents what is being dispensed
 * @param mySessionCanList - Array of SessionTinterCanister objects that represents what is in the tinter 
 * @returns Array of messages (one per colorant), empty array if all ok
 */
function checkDispenseColorantLow(myShotList, mySessionCanList){
	var messageList = [];
	mySessionCanList.forEach(function(myCan){
		myShotList.forEach(function(myShot){
			if(myShot.code==myCan.clrntCode){
				console.log("low check " + myCan.clrntCode + " curr=" + myCan.currentClrntAmount + " alarm=" + myCan.fillAlarmLevel);
				if(myCan.currentClrntAmount<=myCan.fillAlarmLevel){
					messageList.push("Warning " + myCan.clrntCode + "-" + myCan.clrntName + " is low.");
				}
			}
		});
	});
	return messageList;
}

/***
 * Function to check if any of the colorants are empty
 * @param mySessionCanList - Array of SessionTinterCanister objects
 * @returns Array of messages (one per colorant), empty array if all ok
 */
function checkColorantEmpty(mySessionCanList){
	var messageList = [];
	mySessionCanList.forEach(function(myCan){
		if(myCan.currentClrntAmount<=myCan.fillStopLevel && myCan.clrntCode!="NA"){
			messageList.push("Error " + myCan.clrntCode + "-" + myCan.clrntName + " is empty.");
		}
	});
	return messageList;
}
		
/***
 * Function to check if any of the colorants are low
 * @param mySessionCanList - Array of SessionTinterCanister objects
 * @returns Array of messages (one per colorant), empty array if all ok
 */
function checkColorantLow(mySessionCanList){
	var messageList = [];
	mySessionCanList.forEach(function(myCan){
		if(myCan.currentClrntAmount<=myCan.fillStopLevel && myCan.clrntCode!="NA"){
			messageList.push("Error " + myCan.clrntCode + "-" + myCan.clrntName + " is empty.");
		} else {
			if(myCan.currentClrntAmount<=myCan.fillAlarmLevel && myCan.clrntCode!="NA"){
				messageList.push("Warning " + myCan.clrntCode + "-" + myCan.clrntName + " is low.");
			}
		}
	});
	return messageList;
}
		

function decrementColorantForDispense(myGuid, myShotList, callback) {
	console.log("inside dec colorant and shotList is ");
	console.log(myShotList);
	var mydata = {reqGuid:myGuid, shotList:myShotList};
	var jsonIn = JSON.stringify(mydata);
	$.ajax({
		url: "decrementColorantLevelsAction.action",
		contentType : "application/json; charset=utf-8",
		type: "POST",
		data: jsonIn,
		datatype : "json",
		async: true,
		success: function (data) {
			if(data.sessionStatus === "expired"){
        		window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
        	}
        	else{
        		//Check result of decrement
    			var passfail = data.adjustedColorantLevels;
    			console.log("decrement passfail is " + passfail);
    			if(typeof callback == 'function'){
    				callback(passfail);
    			}
        	}
		},
		error: function(err){
			alert("failure: " + err);
		}
	});
}


