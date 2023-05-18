	let calibrate_step = "start";		
	let ws_coloreye = new WSWrapper('coloreye');
    let count = 0;
    let newCol = "";
    let oldCol = "";
    let clreyemodel = "";
    let clreyeserial = "";
    
    function InitializeModelAndSerial(clmod, clser){
	console.log("Setting model & serial number");
	clreyemodel = clmod;
	clreyeserial = clser;
	}

	function InitializeMeasureScreen() {
	    console.log("InitializeMeasureScreen");
  		$(".error").hide();
	}
	
	function GetCalStatusMinUntilCalExpiration() {
	  	console.log("GetCalStatusMinUntilCalExpiration");
		let spectromessage = new SpectroMessage('GetCalStatusMinUntilCalExpiration',clreyemodel,clreyeserial);
	    let json = JSON.stringify(spectromessage);
	    ws_coloreye.send(json);
	}
	
	function SWMeasure() {
	  	console.log("SWMeasure");
	  	checkWsIsReady();
		let spectromessage = new SpectroMessage('SWMeasure',clreyemodel,clreyeserial);
	    let json = JSON.stringify(spectromessage);
	    ws_coloreye.send(json);
  		$(".calibrate").hide();
  		$('.init').hide();
  		$(".swmeasure").show();
	}
	
	function setCount(countvar, newColtext, oldColtext) {
	  	count = countvar;
	  	newCol = newColtext;
	  	oldCol = oldColtext;
	}
	
	function GoodMeasure(measCurve) {
	  	console.log("GoodMeasure");
	  	console.log("meascurve = " + measCurve);
	  if(count == 2){
			  alert("Now go do corrections");
			  $('#measureColorModal').modal('hide');
			  cancelMeasure();
		  }
	  else if(count == 1){
			  SWMeasure();
			  count++;
			  $('#measureModalTitle').text(newCol);
	      }
	  else{
		document.getElementById("measuredCurve").value = measCurve;
	    $("#measure-color-form").submit();
		  }
	}
	
	
	
	function InitializeCalibrationScreen() {
	    console.log("InitializeCalibrationScreen");
		$(".whitecal").hide();
		$(".blackcal").hide();
		$(".greenmeas").hide();
		$('#closeModal').hide();
		$('#spectroCalModal').modal('show');
	}

	function GetCalSteps() {
	  	console.log("GetCalSteps");
	  	calibrate_step = "GetCalSteps";
		let spectromessage = new SpectroMessage('GetCalSteps',clreyemodel,clreyeserial);
		let json = JSON.stringify(spectromessage);
		ws_coloreye.send(json);
	}
	  
	function CalibrateWhite() {
		$('#calcrcl')
			.removeClass('d-none')
			.css('background-color', 'white');
		console.log("CalibrateWhite");
	  	calibrate_step = "CalibrateWhite";
		let spectromessage = new SpectroMessage('CalibrateWhite',clreyemodel,clreyeserial);
	    let json = JSON.stringify(spectromessage);
	    ws_coloreye.send(json);
		$(".pleasewait").hide();
		$(".blackcal").hide();
		$(".greenmeas").hide();
		$(".whitecal").show();
		$('#closeModal').show();
	}

	function CalibrateBlack() {
		$('#calcrcl')
			.removeClass('d-none')
			.css('background-color', 'black');
	  	console.log("CalibrateBlack");
	  	calibrate_step = "CalibrateBlack";
		let spectromessage = new SpectroMessage('CalibrateBlack',clreyemodel,clreyeserial);
	    let json = JSON.stringify(spectromessage);
	    ws_coloreye.send(json);
	    $(".pleasewait").hide();
		$(".whitecal").hide();
		$(".greenmeas").hide();
		$(".blackcal").show();
		$('#closeModal').show();
	}

	function MeasureGreen() {
		$('#calcrcl')
			.removeClass('d-none')
			.css('background-color', 'green');
	  	console.log("MeasureGreen");
	  	calibrate_step = "MeasureGreen";
		let spectromessage = new SpectroMessage('MeasureGreen',clreyemodel,clreyeserial);
	    let json = JSON.stringify(spectromessage);
	    ws_coloreye.send(json);
	    $(".pleasewait").hide();
		$(".whitecal").hide();
		$(".blackcal").hide();
		$(".greenmeas").show();
		$('#closeModal').show();
	}

	function DisplayError() {
		$('#spectroCalModal').modal('hide');
		$('#measureColorModal').modal('hide');
	  	console.log("DisplayError");
	  	calibrate_step = "DisplayError";
	  	
		$(".calsuccess").addClass('d-none');
		$(".calincomplete").addClass('d-none');
		$(".error").removeClass('d-none');
		$(".done").removeClass('d-none');
		$(".error").show();
  		$(".cancel").removeClass('d-none');
	}
	
	function RecdError() {
  		$("#errmsg").text(return_message.errorMessage);
  		DisplayError();
  	}
  	
  	function calibrate(){
		if(count==0){
	  		$(".calibrate").removeClass('d-none');
	  		$('.init').hide();
	  		setTimeout(function(){
				$("#calibrateForm").submit();
	  		}, 1000);
	  	}
	  	else{
			InitializeCalibrationScreen();
	
			console.log("docready, between check and calibrate, isReady is " + ws_coloreye.isReady);
				
			//send the calibrate white message.
			GetCalSteps();
		}
  	}
  	
  	function cancelMeasure(){
  		$("#errmsg").text('<s:text name="measureColor.colormeasurementterminated"/>');
  		DisplayError();
  	}
  	
  	function checkWsIsReady(){
  		let coloreyeStatus;
  		let interval = setInterval(function(){
  			console.log("ws ready state: " + coloreyeStatus);
			if($('#measureColorModal').is(':visible')){
  				coloreyeStatus = ws_coloreye.isReady;
  	  			if(coloreyeStatus === "false"){
  	  				$('#measureColorModal').modal('hide');
  	  				$("#errmsg").text('<s:text name="measureColor.connectionTimeout"/>');
  	  				DisplayError();
  	  			}
			} else {
				clearInterval(interval);
			}
  		}, 1000);
  	}
  	
  	function setMeasureModalTitle(standard, sample, measureStandard, measureSample, measureColor){
	  		let modalTitle = "";
			
			if(standard != null && standard == "true"){
				console.log("color standard measure");
				modalTitle = measureStandard;
		    } else if(sample != null && sample == "true"){
		    	console.log("color sample measure");
		    	modalTitle = measureSample;
		    } else if(count == 1){
		    	console.log("color eye correction");
		    	modalTitle = oldCol;
		    }
		    else{
		    	console.log("color measure");
		    	modalTitle = measureColor;
		    }
			
			console.log("setting modal title to " + modalTitle);
			
			$('#measureModalTitle').text(modalTitle);
	  	  }
		  	  	  	
	function CalibrateSuccess() {
		$('#spectroCalModal').modal('hide');
		console.log("CalibrateSuccess");
	  	calibrate_step = "CalibrateSuccess";
	  	
		$(".calincomplete").addClass('d-none');
		$(".error").addClass('d-none');
		$(".calsuccess").removeClass('d-none');
		$(".done").removeClass('d-none');
	}
	
	function CalibrateIncomplete() {
		console.log("CalibrateIncomplete");
	  	calibrate_step = "CalibrateIncomplete";
	  	
		$(".calsuccess").addClass('d-none');
		$(".error").addClass('d-none');
		$(".calincomplete").removeClass('d-none');
		$(".done").removeClass('d-none');
	}
	  	
	function RecdSpectroMessage() {
		console.log("Received Message");
	  	//parse the spectro
	  	console.log("Message is " + ws_coloreye.wsmsg);
	  	console.log("isReady is " + ws_coloreye.isReady + "BTW");
	  	
	  	if(ws_coloreye.wsmsg.includes("\"errorCode\":-4") && count != 0){
		eyeaddDisabler();
		return;
		}
	  	
	  	if (ws_coloreye.wserrormsg != "") {
		  	$("#errmsg").text('<s:text name="global.webSocketErrorPlusErr"><s:param>' + ws_coloreye.wserrormsg + '</s:param></s:text>');
	  		DisplayError();
	  		return;
	  	}
	  		
		let return_message=JSON.parse(ws_coloreye.wsmsg);
		let myGuid = "${reqGuid}";
	  	sendSpectroEvent(myGuid, return_message);
		switch (return_message.command) {
			case 'GetCalSteps':
				//TO DO: Evaluate the responseMssage and process in appropriate order.
				//       In our case for now, we know it's white then black.  Do that
				//       temporarily until everything is working procedurally.
				CalibrateWhite();
				break;
			case 'CalibrateWhite':
				if (return_message.responseMessage=="true") {
					CalibrateBlack();
				} else {
					$("#errmsg").text(return_message.errorMessage);
	  		  		DisplayError();
				}
				break;
			case 'CalibrateBlack':
				if (return_message.responseMessage=="true") {
					MeasureGreen();
				} else {
					$("#errmsg").text(return_message.errorMessage);
	  		  		DisplayError();
				}
				break;
			case 'MeasureGreen':
				if (return_message.responseMessage=="true") {
					CalibrateSuccess();						
				} else {
					$("#errmsg").text(return_message.errorMessage);
	  		  		DisplayError();
				}
				break;
			case 'GetCalStatusMinUntilCalExpiration':
				if (return_message.responseMessage.match(/^OK/)) {
					$('#measureColorModal').modal('show');
					SWMeasure();
				} else {
					calibrate();
				}
				break;
			case 'SWMeasure':
				if (return_message.responseMessage=="") {
					let thisCurve = return_message.curve;
					console.log("curvepointCnt = " + thisCurve.curvePointCnt);
					let curveString = "";
					for (let i = 0; i < thisCurve.curvePointCnt; i++) {
					    let counter = thisCurve.curve[i];
					    console.log("curve is "+ counter);
					    if (i==0) {
					    	curveString = counter;
					    } else {
					    	curveString = curveString + "," + counter;
					    }
					    console.log("curveString is " + curveString);
					}
					console.log("thisCurve is " + thisCurve);
					GoodMeasure(curveString);
				} else {
					$("#errmsg").text(return_message.errorMessage);
	  		  		DisplayError();
				}
				break;
			default:
				//Not an response we expected...
				$("#errmsg").text('<s:text name="global.unexpectedCallToErr"><s:param>' + return_message.command + '</s:param></s:text>');
		  		DisplayError();
		}
	  	
	}