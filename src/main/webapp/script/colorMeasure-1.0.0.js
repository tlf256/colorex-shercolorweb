	var ws_coloreye = new WSWrapper('coloreye');
    var clreyemodel = "${sessionScope[reqGuid].spectro.model}";
    var clreyeserial = "${sessionScope[reqGuid].spectro.serialNbr}";
  	
  	
  	
  	
  
  	
  	
  	

  	function DisplayError() {
		$('#measureColorModal').modal('hide');
	  	console.log("DisplayError");
  		$(".error").show();
  		$(".cancel").removeClass('d-none');
	}
  	
  	
  	
  	
  	
  	
  	
  	function checkWsIsReady(){
  		var coloreyeStatus;
  		var interval = setInterval(function(){
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
  	
  
  	function RecdMeasureMessage() {
	  	console.log("Received Message");
	  	//parse the spectro
	  	console.log("Message is " + ws_coloreye.wsmsg);
	  	console.log("isReady is " + ws_coloreye.isReady + "BTW");
	  	
	  	if (ws_coloreye.wserrormsg != "") {
  		  	$("#errmsg").text('<s:text name="global.webSocketErrorPlusErr"><s:param>' + ws_coloreye.wserrormsg + '</s:param></s:text>');
	  		DisplayError();
	  		return;
	  	}
	  	
	  	var return_message=JSON.parse(ws_coloreye.wsmsg);
	  	var myGuid = "${reqGuid}";
	  	sendSpectroEvent(myGuid, return_message);
		switch (return_message.command) {
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
					var thisCurve = return_message.curve;
					console.log("curvepointCnt = " + thisCurve.curvePointCnt);
					var curveString = "";
					for (var i = 0; i < thisCurve.curvePointCnt; i++) {
					    var counter = thisCurve.curve[i];
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
  	
  	