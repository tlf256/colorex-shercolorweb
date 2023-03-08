/**
 * Script to define a javascript Spectro message - needed to pass data to SWDeviceHandler service
 */
let requestDates = [];

function SpectralCurve(){
	this.curve=[];
	this.curvePointCnt=0;
}

function SpectroConfig(myModel, mySerno, myPort){
	console.log("In SpectroConfig, model is " + myModel + " and serno is " + mySerno);
	this.model=myModel;
	this.serial=mySerno;
	this.port=myPort;
}

function SpectroMessage(command,model,serno){
	console.log("In SpectroMessage, model is " + model + " and serno is " + serno);
	this.id=createUUID();
	this.model=model;
	this.command=command;
	this.responseMessage="";
	this.rc=0;
	this.errorCode=0;
	this.errorMessage="";
	this.messageName="SpectroMessage";
	this.curve=new SpectralCurve();
	this.spectroConfig=new SpectroConfig(model,serno,"USB");
	this.deltaE=0;
	requestDates.push(new Date().toString());
}

/***
 * @param myGuid - Session Guid from page (i.e. reqGuid) 
 * @param myMessage - SpectroMessage object
 * @returns
 */
function sendSpectroEvent(myGuid, myMessage) {
	/* use global variable if null sent for myGuid*/
	if(myGuid == null){
		if(reqGuid != null){
			myGuid=reqGuid;
		}
	}
	var responseDate = new Date().toString();
	var requestDate = requestDates.shift();
	var mydata = {reqGuid:myGuid, spectroMessage: myMessage, requestDate: requestDate, responseDate: responseDate};
	var jsonIn = JSON.stringify(mydata);
	console.log("Logging Spectro Event" + jsonIn);
	$.ajax({
		url: "logSpectroEventAction.action",
		contentType: "application/json; charset=utf-8",
		type: "POST",
		data: jsonIn,
		datatype: "json",
		success: function (data) {
			if (data.sessionStatus === "expired") {
        		window.location.href = "./invalidLoginAction.action";
			}
		}
	});
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