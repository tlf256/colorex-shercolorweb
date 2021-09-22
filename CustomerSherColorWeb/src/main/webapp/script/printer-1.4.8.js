
var ws_printer = new WSWrapper("printer");

function printOnDispenseGetJson(myguid,printJsonIN) {
	if (printerConfig && printerConfig.model) {
		var myPdf = new pdf(myguid,printJsonIN);
		$("#printerInProgressMessage").text('<s:text name="displayFormula.printerInProgress"/>');
		var numLabels = null;
		numLabels = encodeURI(printerConfig.numLabels);
		print(myPdf, numLabels, myPrintLabelType, myPrintOrientation);
	}
}

function getPdfFromServer(myGuid, jsonIN){
	console.log("JSON IN BEFORE SENDING: " + jsonIN);
	var pdfobj=null;
	if(myGuid !=null){
		$.ajax({
			url : "formulaUserPrintAsJsonAction",
			contentType : "application/json; charset=utf-8",
			data : jsonIN,
			async : false,
			type: "POST",
			dataType: "json",
			success: function (pdfResp) {
				//console.log(calobj);
				console.log(encodeURI(pdfResp));
				if(pdfResp.sessionStatus === "expired"){
            		window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
            	}
            	else{
            		pdfobj = pdfResp;
            	}
			},
			error: function(error){
				pdfobj="error";
				console.log(error);
				alert(i18n['printer.couldNotGeneratePdf']);
			}
		});
	}
	else {
		alert(i18n['global.notLoggedInReqGuidNotFound']);
	}

	return pdfobj;
}

function pdf(myguid, jsonIN){

	this.data = null;

	
	var pdf_file = getPdfFromServer(myguid, jsonIN);
	if(pdf_file !=null && pdf_file.data != null){
	
		this.data=pdf_file.data;
	}
	else{
		alert(i18n['printer.couldNotFindDefaultPdf']+ myguid);
	}
}



function PrinterConfig(mymodel,myserial,myPrintOnDispense,myLabels){
		
	
	this.model=mymodel;
	this.serial=myserial;
	this.port=null;
	this.printOnDispense=myPrintOnDispense;
	this.numLabels=myLabels;
	
	
}


function Error(num,message){
	this.num=num;
	this.message=message;
}
function PrinterMessage(mycommand, mydata, myconfig, mynumlabels,mylabeltype,myprintorientation){
	console.log("In PrinterMessage");
	console.log(myconfig);
	this.id=createUUID();
	this.messageName="PrinterMessage";
	this.command=mycommand;
	this.data=mydata;
	this.labelType = mylabeltype;
	this.printOrientation = myprintorientation;
	if(mynumlabels){
		this.numLabels=mynumlabels;
	}
	else if(myconfig && myconfig.numLabels){
		this.numLabels=myconfig.numLabels;
	}
	else{
		this.numLabels=1;
	}
	this.printerConfig = myconfig;
	//returned items
	
	this.errorCode=0;
	this.errorMessage=0;
	this.printerList=null;
	
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

function print(myPdf,myNumLabels,myLabelType,myPrintOrientation) {

	
	var printermessage = new PrinterMessage("Print",myPdf.data,null,myNumLabels,myLabelType,myPrintOrientation);

	var json = JSON.stringify(printermessage);
	
	if (ws_printer != null && ws_printer.isReady == "false") {
		console
				.log("WSWrapper connection has been closed (timeout is defaulted to 5 minutes). Make a new WSWrapper.");
		ws_printer = new WSWrapper("printer");
	}
	
	// Send to tinter
	ws_printer.send(json);

}
function getPrinters() {

	
	var printermessage = new PrinterMessage("GetPrinters",null,null,null,null,null);

	var json = JSON.stringify(printermessage);
	
	if (ws_printer != null && ws_printer.isReady == "false") {
		console
				.log("WSWrapper connection has been closed (timeout is defaulted to 5 minutes). Make a new WSWrapper.");
		ws_printer = new WSWrapper("printer");
	}
	
	// Send to tinter
	ws_printer.send(json);

}
/*
 * get config from SWDeviceHandler
 */
function getPrinterConfig() {

	
	var printermessage = new PrinterMessage("GetConfig",null,null,null,null,null);

	var json = JSON.stringify(printermessage);
	if (ws_printer != null && ws_printer.isReady == "false") {
		console
				.log("WSWrapper connection has been closed (timeout is defaulted to 5 minutes). Make a new WSWrapper.");
		ws_printer = new WSWrapper("printer");
	}
	
	// Send to tinter
	ws_printer.send(json);

}
/*
 * set config at SWDeviceHandler
 */
function setPrinterConfig(myconfig) {

		
		var printermessage = new PrinterMessage("SetConfig",null,myconfig,null,null,null);

		var json = JSON.stringify(printermessage);
		
		if (ws_printer != null && ws_printer.isReady == "false") {
			console
					.log("WSWrapper connection has been closed (timeout is defaulted to 5 minutes). Make a new WSWrapper.");
			ws_printer = new WSWrapper("printer");
		}
		
		// Send to tinter
		ws_printer.send(json);
}
