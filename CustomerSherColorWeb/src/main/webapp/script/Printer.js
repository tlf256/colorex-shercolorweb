



function getPdfFromServer(myGuid){
	var pdfobj=null;
	if(reqGuid !=null){
		$.ajax({
			url : "formulaUserPrintAsJsonAction",
			context : document.body,
			data : {
				reqGuid : myGuid //without this guid you will get a login exception and you won't even get an error
				//filename: uFilename
			},
			async : false,
			type: "POST",
			dataType: "json",
			success: function (pdfResp) {
				//console.log(calobj);
				console.log(encodeURI(pdfResp));
				if(cal.sessionStatus === "expired"){
            		window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
            	}
            	else{
            		pdfobj = pdfResp;
            	}
			},
			error: function(error){
				pdfobj="error";
				console.log(error);
				alert("Could not generate pdf for " + printing);
			}
		});
	}
	else {
		alert("Not logged in.  ReqGuid not found");
	}

	return pdfobj;
}

function pdf(myguid){

	this.data = null;

	
	var pdf_file = getPdfFromServer(myguid);
	if(pdf_file !=null && pdf_file.data != null){
	
		this.data=pdf_file.data;
	}
	else{
		alert("Could not find Default pdf for colorant:" + mycolorant  );
	}
}



function Configuration(mymodel,myserial){
		
	
	this.model=mymodel;
	this.serial=myserial;
	this.port=null;
	
	
}


function Error(num,message){
	this.num=num;
	this.message=message;
}
function PrinterMessage(mymodel,myserial, mydata){
	console.log("In PrinterMessage");
	console.log(myconfig);
	this.id=createUUID();
	this.messageName="PrinterMessage";
	this.data=data;
	this.model=mymodel;
	this.serial=myserial
	//returned items
	
	this.errorCode=0;
	this.errorMessage=0;
	
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

