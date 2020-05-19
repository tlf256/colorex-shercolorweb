<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>


<%@ taglib prefix="s" uri="/struts-tags" %>

<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title>Measure Color</title>
			<!-- JQuery -->
		<link rel=StyleSheet href="css/bootstrap.min.css" type="text/css">
		<link rel=StyleSheet href="css/bootstrapxtra.css" type="text/css">
		<link rel=StyleSheet href="js/smoothness/jquery-ui.css" type="text/css">
		<link rel=StyleSheet href="css/CustomerSherColorWeb.css" type="text/css"> 
		<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
		<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/jquery-ui.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/bootstrap.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.4.2.js"></script>
		<script type="text/javascript" src="script/spectro.js"></script>
		<script type="text/javascript" src="script/WSWrapper.js"></script>
		<script>

		  	var ws_coloreye = new WSWrapper('coloreye');
		  	
		  	function InitializeMeasureScreen() {
	  		    console.log("InitializeMeasureScreen");
	  		  	$(".calibrate").hide();
	  	  		$(".swmeasure").hide();
	  	  		$(".cancel").hide();
	  	  		$(".error").hide();
	  		}
		  	
	  	  	function GetCalStatusMinUntilCalExpiration() {
	  		  	console.log("GetCalStatusMinUntilCalExpiration")
  	    		var clreyemodel = $('#spectroModel').val();
				var spectromessage = new SpectroMessage('GetCalStatusMinUntilCalExpiration',clreyemodel);
			    var json = JSON.stringify(spectromessage);
			    ws_coloreye.send(json);
	  		}
	  	  
	  	  	function SWMeasure() {
	  		  	console.log("SWMeasure")
  	    		var clreyemodel = $('#spectroModel').val();
				var spectromessage = new SpectroMessage('SWMeasure',clreyemodel);
			    var json = JSON.stringify(spectromessage);
			    ws_coloreye.send(json);
		  		$(".calibrate").hide();
		  		$(".swmeasure").show();
		  		$(".cancel").show();
	  		}
	  	  	
	  	  	function GoodMeasure(measCurve) {
	  		  	console.log("GoodMeasure")
	  		  	console.log("meascurve = " + measCurve);
	  		  	document.getElementById("measuredCurve").value = measCurve;
	  		  $("#measure-color-form").submit();
	  		}
 	  	
	  	  	function DisplayError() {
		  	  	$('#spectroCalModal').modal().hide();
				$('.modal-backdrop').remove();
	  		  	console.log("DisplayError")
		  		$(".swmeasure").hide();
		  		$(".calsuccess").hide();
		  		$(".error").show();
		  		$(".cancel").show();
	  		}
	  	  	
	  	  	function RecdError() {
	  	  		$("#errmsg").text(return_message.errorMessage);
		  		DisplayError();
	  	  	}
	  	  	
	  	  	function calibrate(){
	  	  		$(".swmeasure").hide();
	  	  		$(".calibrate").show();
	  	  		setTimeout(function(){
					$("#calibrateForm").submit();
	  	  		}, 1000);
	  	  	}
	  	  	
	  	  
	  	  	function RecdMessage() {
	  		  	console.log("Received Message");
	  		  	//parse the spectro
	  		  	console.log("Message is " + ws_coloreye.wsmsg);
	  		  	console.log("isReady is " + ws_coloreye.isReady + "BTW");
	  		  	if (ws_coloreye.wserrormsg != "") {
		  		  	$("#errmsg").text("WebSocketError " + ws_coloreye.wserrormsg);
	  		  		DisplayError();
	  		  		return;
	  		  	}
	  			var return_message=JSON.parse(ws_coloreye.wsmsg);
	  			switch (return_message.command) {
	  				case 'GetCalStatusMinUntilCalExpiration':
	  					if (return_message.responseMessage.match(/^OK/)) {
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
  						$("#errmsg").text("Unexpected call to " + return_message.command);
		  		  		DisplayError();
	  			}
	  		  	
	  	  	}
				
			$(document).ready(function() {	
				console.log("in docready");
			
				//this loads on startup!  
				InitializeMeasureScreen();
				
				//Get the calibration status to initialize connection.
				GetCalStatusMinUntilCalExpiration();
				
			});

		</script>
	</head>
	
	<body>
		<!-- including Header -->
		<s:include value="Header.jsp"></s:include>
		<s:set var="thisGuid" value="reqGuid" />
		<s:form id="calibrateForm" action="spectroCalibrateRedirectAction">
			<s:hidden name="reqGuid" id="reqGuid" value="%{reqGuid}"/>
		</s:form>
		<s:form id="measure-color-form" action="MeasureColorNextAction" validate="true"  theme="bootstrap" method="post">
			<div class="container-fluid">
				<div class="row">
					<div class="col-sm-3"></div>
					<div class="col-sm-6"></div>
					<div class="col-sm-3">
						<s:hidden name="measuredCurve" id="measuredCurve" value=""/>
						<s:hidden name="reqGuid" id="reqGuid" value="%{reqGuid}"/>
						<s:hidden name="spectroModel" id="spectroModel" value="%{#session[reqGuid].spectroModel}"/>
					</div>
				</div>
				<br>
				<br>
				<div class="row">
					<div class="col-sm-3"></div>
					<div class="col-sm-6">
						<h2 class="calibrate">Initializing calibration...</h2>
						<h2 class="swmeasure">1. Position the Color Eye target window on top of the Sample.</h2>
					</div>
					<div class="col-sm-3"></div>
				</div>
				<div class="row">
					<div class="col-sm-3"></div>
					<div class="col-sm-6">
						<h2 class="swmeasure">2. Press the instrument firmly down until the next prompt appears.</h2>
					</div>
					<div class="col-sm-3"></div>
				</div>
				<div class="row">
					<div class="col-sm-3"></div>
					<div class="col-sm-6">
						<h2 class="swmeasure"></h2>
	            		<h2 class="error" id="errmsg"></h2>
					</div>
					<div class="col-sm-3"></div>
				</div>
				<div class="row">
					<div class="col-sm-3"></div>
					<div class="col-sm-6">
						<h2 class="swmeasure">Note: the two status lights on the instrument should change</h2>
					</div>
					<div class="col-sm-3"></div>
				</div>
				<div class="row">
					<div class="col-sm-3"></div>
					<div class="col-sm-6">
						<h2 class="swmeasure">from red to green on successful measurement.</h2>
					</div>
					<div class="col-sm-3"></div>
				</div>
				<div class="row">
					<div class="col-sm-3"></div>
					<div class="col-sm-6">
						<h2 class="swmeasure"></h2>
					</div>
					<div class="col-sm-3"></div>
				</div>
				<br>
				<br>
				<div class="row">
					<div class="col-sm-3">
					</div>
					<div class="col-sm-4">
					</div>
					<div class="col-sm-5">
						<div class="cancel">
							<s:submit cssClass="btn btn-secondary center-block" value="Cancel" action="userCancelAction"/>
						</div>
					</div>
		    	</div>
			</div>
		</s:form>
		
		<br>
		<br>
		<br>

		<!-- Including footer -->
		<s:include value="Footer.jsp"></s:include>
		
	</body>
</html>