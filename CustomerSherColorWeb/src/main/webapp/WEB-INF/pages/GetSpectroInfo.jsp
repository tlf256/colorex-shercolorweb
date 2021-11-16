<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>


<%@ taglib prefix="s" uri="/struts-tags" %>

<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title><s:text name="global.colorEyeInformation"/></title>
			<!-- JQuery -->
		<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
		<link rel="stylesheet" href="css/bootstrapxtra.css" type="text/css">
		<link rel="stylesheet" href="js/smoothness/jquery-ui.min.css" type="text/css">
		<link rel="stylesheet" href="css/CustomerSherColorWeb.css" type="text/css"> 
		<link rel="stylesheet" href="css/font-awesome.min.css" type="text/css">
		<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/jquery-ui.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/bootstrap.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.4.6.js"></script>
		<script type="text/javascript" charset="utf-8"	src="script/GetProductAutoComplete.js"></script>
		<script type="text/javascript" charset="utf-8"	src="script/WSWrapper.js"></script>
		<script type="text/javascript" charset="utf-8"	src="script/spectro.js"></script>
		<s:set var="thisGuid" value="reqGuid" />
		<style>
	        .sw-bg-main {
	            background-color: ${sessionScope[thisGuid].rgbHex};
	        }
	    </style>
	    <script type="text/javascript">
	    
	    var ws_coloreye = new WSWrapper('coloreye');
	    
	  	function InitializeGetInfoScreen() {
  		    console.log("InitializeGetInfoScreen");
  		  	
			$(".goodcal").hide();
			$(".badcal").hide();
			$(".info").hide();
			$(".error").hide()
			GetCalStatus();
			//$(".error").hide();
  		}
	  	
  	  	function GetCalStatus() {
  		  	console.log("GetCalStatusMinUntilCalExpiration")
	    	var clreyemodel = $('#spectroModel').val();
			var spectromessage = new SpectroMessage('GetCalStatusMinUntilCalExpiration',clreyemodel);
		    var json = JSON.stringify(spectromessage);
		    ws_coloreye.send(json);
  		}
  	  	
  	  	function GetSerialNumber() {
  		  	console.log("GetSerialNumber")
	    	var clreyemodel = $('#spectroModel').val();
			var spectromessage = new SpectroMessage('GetSerialNumber',clreyemodel);
		    var json = JSON.stringify(spectromessage);
		    ws_coloreye.send(json);
  		}
  	  	
  	  	function GetWhiteTileSN() {
  		  	console.log("GetWhiteTileSN")
	    	var clreyemodel = $('#spectroModel').val();
			var spectromessage = new SpectroMessage('GetCalPlaqueSerial',clreyemodel);
		    var json = JSON.stringify(spectromessage);
		    ws_coloreye.send(json);
  		}
  	  	
  	  	function GetCheckTileSN() {
  		  	console.log("GetCheckTileSN")
	    	var clreyemodel = $('#spectroModel').val();
			var spectromessage = new SpectroMessage('GetCheckPlaqueSerial',clreyemodel);
		    var json = JSON.stringify(spectromessage);
		    ws_coloreye.send(json);
  		}
  	  	
  	  	function GetVersion() {
  		  	console.log("GetVersion")
	    	var clreyemodel = $('#spectroModel').val();
			var spectromessage = new SpectroMessage('GetVersion',clreyemodel);
		    var json = JSON.stringify(spectromessage);
		    ws_coloreye.send(json);
  		}
  	  	
  	  	function DisplayError() {
		  	console.log("DisplayError")
	  		$(".info").hide();
	  		$(".error").show();
		}
	  	
	  	function DisplayInfo() {
		  	console.log("DisplayInfo")
	  		$(".info").show();
	  		$(".error").hide();
		}
	  	
	  	function DisplayBadCal() {
		  	console.log("DisplayBadCal")
		    $(".goodcal").hide();
	  		$(".badcal").show();
	  		$(".error").hide();
	  		GetSerialNumber();
		}
	  	
	  	function DisplayGoodCal() {
		  	console.log("DisplayGoodCal")
		    $(".goodcal").show();
	  		$(".badcal").hide();
	  		$(".error").hide();
	  		GetSerialNumber();
		}
	  	
	  	function ChangeDegreeType() {
	  		var daButton = document.getElementById("degreeTypeButton");
	  		if (daButton.innerText==="(C)") {
	  			$("#degreeTypeButton").text("(F)");
	  			var tempInC = document.getElementById('calIntTemp').textContent;
	  			tempInC = parseFloat(tempInC);
	  			var tempInF	= tempInC * 1.8;
	  			tempInF = tempInF + 32;
	  			$("#calIntTemp").text(tempInF.toString());
	  		} else {
	  			$("#degreeTypeButton").text("(C)");
	  			var tempInF = document.getElementById('calIntTemp').textContent;
	  			tempInF = parseFloat(tempInF)
	  			var tempInC	= tempInF - 32;
	  			tempInC = tempInC / 1.8;
	  			$("#calIntTemp").text(tempInC);
	  		}
	  	}
	  	
 	  	
  	  	function RecdMessage() {
  		  	console.log("Received Message");
  		  	//parse the spectro
  		  	console.log("Message is " + ws_coloreye.wsmsg);
  		  	console.log("isReady is " + ws_coloreye.isReady + "BTW");
  			var return_message=JSON.parse(ws_coloreye.wsmsg);
  			switch (return_message.command) {
  			case 'GetCalStatusMinUntilCalExpiration':
					if (return_message.errorMessage!="") {
						$("#errmsg").text(return_message.errorMessage);
						DisplayError();
					} else {
						if (return_message.responseMessage.match(/^OK/)) {
							var goodMsg = return_message.responseMessage.split(" ");
							$("#calIntTemp").text(goodMsg[1]);
							var rmnTime = goodMsg[2];
							var theHrs = Math.floor(parseInt(rmnTime)/60);
							var theMin = parseInt(rmnTime) % 60;
							theHrs = theHrs.toString();
							theMin = theMin.toString();
							$("#calRemainTime").text(theHrs.concat(":",theMin));
	  						DisplayGoodCal();
						} else {
							DisplayBadCal();
						}
					}
					break;
  			
  				case 'GetSerialNumber':
  					if (return_message.errorMessage!="") {
  						$("#errmsg").text(return_message.errorMessage);
  						DisplayError();
  					} else {
						$("#spectroSN").text(return_message.responseMessage);
	  					GetWhiteTileSN();
  					}
  					break;
  				case 'GetCalPlaqueSerial':
  					if (return_message.errorMessage!="") {
  						$("#errmsg").text(return_message.errorMessage);
  						DisplayError();
  					} else {
						$("#whiteTileSN").text(return_message.responseMessage);
	  					GetCheckTileSN();
  					}
  					break;
  				case 'GetCheckPlaqueSerial':
  					if (return_message.errorMessage!="") {
  						$("#errmsg").text(return_message.errorMessage);
  						DisplayError();
  					} else {
						$("#checkTileSN").text(return_message.responseMessage);
	  					GetVersion();
  					}
  					break;
  				case 'GetVersion':
  					if (return_message.errorMessage!="") {
  						$("#errmsg").text(return_message.errorMessage);
  						DisplayError();
  					} else {
						$("#versionNbr").text(return_message.responseMessage);
	  					DisplayInfo();
  					}
  					break;
  				
				default:
					//Not an response we expected...
					$("#errmsg").text('<s:text name="global.unexpectedCallToErr"><s:param>'+ return_message.command +'</s:param></s:text>');
	  		  		DisplayError();
  			}
  		  	
  	  	}
  	  	
  		$(document).on("click", "#degreeTypeButton", function(event){
  			//event.preventDefault();
  			ChangeDegreeType();
  		});
	  	
		$(document).ready(function() {	
			console.log("in docready");
			InitializeGetInfoScreen();
		});
	    </script>
	</head>
	
	<body>
		<!-- including Header -->
		<s:include value="Header.jsp"></s:include>
		
		<div class="container-fluid">
			<div class="row">
				<div class="col-sm-3">
				</div>
				<div class="col-sm-6">
				</div>
				<div class="col-sm-3">
					<s:set var="thisGuid" value="reqGuid" />
				</div>
			</div>
			<br>
			<div class="row">
				<div class="col-sm-2">
				</div>
			</div>



			<s:form action="calStatusUserNextAction" validate="true"  theme="bootstrap">
				<div class="row">
					<div class="col-sm-2">
					<s:hidden name="spectroModel" id="spectroModel" value="%{#session[reqGuid].spectroModel}"/>
					</div>
					<div class="col-sm-2">
					</div>
					<div class="col-sm-4">
						
					</div>
				</div>
				<div class="row">
					<div class="col-sm-3">
					
					</div>
					<div class="col-sm-3">
						<div class="goodcal"><strong><s:text name="getSpectroInfo.calibrationStatus"/></strong></div>
						<div class="badcal"><strong><s:text name="getSpectroInfo.calibrationStatus"/></strong></div>
						
					</div>
					<div class="col-sm-3">
						<div class="goodcal"><p class="text-success"><strong><s:text name="global.ok"/></strong></p></div>
						<div class="badcal"><p class="text-danger"><strong><s:text name="global.expired"/></strong></p></div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-3">
					</div>
					<div class="col-sm-3">
						<h2 class="error" id="errmsg"></h2>
						<div class="goodcal"><strong><s:text name="getSpectroInfo.calibrationInternalTemperature"/> <button type="button" id="degreeTypeButton" class="btn btn-secondary btn-xs">(C)</button></strong></div>
						<div class="badcal"></div>
					</div>
					<div class="col-sm-3">
						<h2 class="error"></h2>
						<div class="goodcal" id="calIntTemp"></div>
						<div class="badcal"><s:text name="getSpectroInfo.unitNotReady"/></div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-3">
					</div>
					<div class="col-sm-3">
						<h2 class="error"></h2>
						<div class="goodcal"><strong><s:text name="getSpectroInfo.remainingCalibTime"/></strong></div>
						<div class="badcal"></div>
					</div>
					<div class="col-sm-3">
						<h2 class="error"></h2>
						<div class="goodcal" id="calRemainTime"></div>
						<div class="badcal"><s:text name="getSpectroInfo.pleaseCalibrateColorEye"/></div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-3">
					<s:hidden name="spectroModel" id="spectroModel" value="%{#session[reqGuid].spectroModel}"/>
					</div>
					<div class="col-sm-3">
						<div class="info"><strong><s:text name="getSpectroInfo.colorEyeSn"/></strong></div>
						
					</div>
					<div class="col-sm-3">
						<div class="info" id="spectroSN"></div>
						
					</div>
				</div>
				<div class="row">
					<div class="col-sm-3">
					</div>
					<div class="col-sm-3">
						<h2 class="error" id="errmsg"></h2>
						<div class="info"><strong><s:text name="getSpectroInfo.whiteTileSn"/></strong></div>
					</div>
					<div class="col-sm-3">
						<h2 class="error"></h2>
						<div class="info" id="whiteTileSN"></div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-3">
					</div>
					<div class="col-sm-3">
						<h2 class="error"></h2>
						<div class="info"><strong><s:text name="getSpectroInfo.checkTileSn"/></strong></div>
					</div>
					<div class="col-sm-3">
						<h2 class="error"></h2>
						<div class="info" id="checkTileSN"></div>
					</div>
				</div>
				
				<div class="row">
					<div class="col-sm-3">
					</div>
					<div class="col-sm-3">
						<h2 class="error"></h2>
						<div class="info"><strong><s:text name="getSpectroInfo.version"/></strong></div>
					</div>
					<div class="col-sm-3">
						<h2 class="error"></h2>
						<div class="info" id="versionNbr"></div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-2">
					</div>
					<div class="col-sm-2">
					</div>
					<div class="col-sm-4">
						
					</div>
				</div>
				<div class="row">
					<div class="col-sm-3">
					</div>
					<div class="col-sm-3">
						<div class="goodcal"><s:text name="getSpectroInfo.unitReady"/></div>
					</div>
					<div class="col-sm-3">
					
					</div>
				</div>
				<div class="row">
					<div class="col-sm-2">
					</div>
					<div class="col-sm-2">
						
					</div>
					<div class="col-sm-6">
					
					</div>
				</div>

				<br>
				<br>
				<div class="row">
            		<div class="col-sm-2">
					</div>

					<div class="col-sm-8">
					</div>
				</div>
				<div class="row">
            		<div class="col-sm-2">
					</div>
					<div class="col-sm-8">
						<s:hidden name="reqGuid" id="reqGuid" value="%{reqGuid}"/>
					</div>
				</div>
					
				<div class="row">
						<div class="col-sm-2">
						</div>	
						<div class="col-sm-2">
							
						</div>
						<div class="col-sm-2">	
							<s:submit cssClass="btn btn-secondary" value="%{getText('global.cancel')}" action="userCancelAction"/>
   						</div>
						<div class="col-sm-2">
						</div>
						<div class="col-sm-2">
			    			
			    		</div>
		    	</div>
			</s:form>
		</div>
		
		<br>
		<br>
		<br>
		<script>
		<!--
		  function HF_openSherwin() {
		    var popupWin = window.open("http://www.sherwin-williams.com", "Sherwin", "resizable=yes,toolbar=yes,menubar=yes,statusbar=yes,directories=no,location=yes,scrollbars=yes,width=800,height=600,left=10,top=10");
		    popupWin.focus();
		  }  
		  function HF_openLegal() {
		    var popupWin = window.open("http://www.sherwin-williams.com/terms/", "legal", "resizable=no,toolbar=no,menubar=yes,statusbar=no,directories=no,location=no,scrollbars=yes,width=800,height=600,left=10,top=10");
		    popupWin.focus();
		  }
		  function HF_openPrivacy() {
		    var popupWin = window.open("http://privacy.sherwin-williams.com/", "privacy", "resizable=yes,toolbar=no,menubar=yes,statusbar=no,directories=no,location=no,scrollbars=yes,width=640,height=480,left=10,top=10");
		    popupWin.focus();
		  }
		//-->
		</script>
  
		<!-- Including footer -->
		<s:include value="Footer.jsp"></s:include>
		
	</body>
</html>