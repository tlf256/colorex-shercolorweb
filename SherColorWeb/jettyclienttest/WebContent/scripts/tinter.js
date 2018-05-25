
var configuration=null;  //global config.

function Colorant(code,shots,pos,uom){
	this.code=code;
	this.shots=shots;
	this.uom=uom;
	this.tank=pos;
	
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

function getCalFromServer(uFilename){
	var calobj=null;
	
	 $.getJSON({
		url: "/ShercolorCal/selectECal",
		context: document.body,
		data: { 
	  
			filename: uFilename
			},
			async: false,
		type: "POST",
			 dataType: "json",
			    success: function (cal) {
			    	calobj = cal;
			    	
			    },
				error: function(){
					calobj="error";
					alert("Could not find calibration template for " + uFilename);
				}
	});
	 
	while(calobj == null){
		setTimeout(null, 300);
	}
	
	return calobj;
}

function putECal(msg){
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
	$.ajax({
		url: "/ShercolorCal/uploadEcal",
		context: document.body,
		//processData: false,
		traditional: true,
		data: { 
			customerid: "12345",
			filename: msg.calibration.filename,
			colorantid: msg.configuration.colorantSystem,
			tintermodel:msg.configuration.model,
			tinterserial:msg.configuration.serial,
			uploaddate:fulldate,
			uploadtime: time,
			data: (msg.calibration.data)
		},
		async: true,
		type: "POST",
		dataType: "json",
		success: function (cal) {
			complete = true;
			alert("Cal Upload complete for " + msg.calibration.filename);

		},
		error: function(event){
			calobj="error";
			alert("Could not upload calibration template for " + msg.calibration.filename + " " + event.statusText);
			console.log(event);
		}
	});



}


function Calibration(mycolorant,mymodel,myserial){
	
	this.filename=null;
	this.data=null;
	
	var url=null;
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
	if (myserial.length != 0) {
		var filename = mycolorant+"_"+mymodel+"_"+myserial+"_"+"20170920_1330";
		var uFilename= filename.toUpperCase() + ".zip";
		uFilename = uFilename.replace(/\s+/g, '');
		var cal = getCalFromServer(uFilename);
		this.filename = cal.filename;
		this.data=cal.data;
	
	}
	if(this.data == null){
		var filename = mycolorant+"_"+mymodel;
		var uFilename= filename.toUpperCase() + ".zip";
		uFilename = uFilename.replace(/\s+/g, '');
		var cal = getCalFromServer(uFilename);
		this.filename = cal.filename;
		this.data=cal.data;
		//url = "/BlobTest/getCalTemplate";
	}
	
}

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

function config_tinter(mycolorantid,mymodel,myserial){
	var canister1 = new Canister(1,"W1");
	var canister2 = new Canister(2,"N1");
	var canister3 = new Canister(3,"R4");
	var canister4 = new Canister(4,"R3");
	var canister5 = new Canister(5,"G2");
	var canister6 = new Canister(6,"NA");
	var canister7 = new Canister(7,"B1");
	var canister8 = new Canister(8,"Y3");
	var canister9 = new Canister(9,"L1");
	var canister10 = new Canister(10,"R2");
	var canister11 = new Canister(11,"Y1");
	var canister12 = new Canister(12,"NA");
	var canister_layout = [canister1,canister2,canister3,canister4,canister5,canister6,canister7,canister8,canister9,canister10,canister11,canister12];
	var command = "Config"
	configuration = new Configuration(mycolorantid,mymodel,myserial,canister_layout);
	
	calibration = new Calibration(mycolorantid,mymodel,myserial);
	var shotList=null;
	configmessage = new TinterMessage(command,shotList,configuration,calibration);

	//this.colorantSystem=configuration.colorant;
//	this.model=configuration.model;
//	this.serial=configuration.serial;
//	this.CanisterLayout=configuration.canisterLayout;


	var json = JSON.stringify(configmessage);
	console.log(json);
	ws_tinter.send(json);
};

function Error(num,message){
	this.num=num;
	this.message=message;
}
function TinterMessage(command,shotList,myconfig,mycalibration){
	console.log(configuration);
	this.id=createUUID();
	this.messageName="TinterMessage";
	this.command=command;
	this.configuration=myconfig;
	this.calibration=mycalibration
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

/*
ConfigMessage(model,serial,canister_list)
#114]CCE Corob D6000           CL      CN       PM        SZ       AmtCal  AmtAct  LA       MID
CCE_COROB D600                 W1       1        1       512       512000  512000  64000
CCE_COROB D600                 N1       2        2       512       512000  512000  64000
CCE_COROB D600                 R4       3        3       192       192000  192000  32000
CCE_COROB D600                 R3       4        4       192       192000  192000  32000
CCE_COROB D600                 G2       5        5       192       192000  192000  32000
CCE_COROB D600                 NA       6        6       192       192000  192000  32000
CCE_COROB D600                 B1       7        7       512       512000  512000  64000
CCE_COROB D600                 Y3       8        8       512       512000  512000  64000
CCE_COROB D600                 L1       9        9       192       192000  192000  32000
CCE_COROB D600                 R2       10       10      192       192000  192000  32000
CCE_COROB D600                 Y1       11       11      192       192000  192000  32000
CCE_COROB D600                 NA       12       12      192       192000  192000  32000

*/
