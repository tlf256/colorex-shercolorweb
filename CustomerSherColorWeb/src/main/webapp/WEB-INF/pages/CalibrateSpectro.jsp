<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>


<%@ taglib prefix="s" uri="/struts-tags" %>

<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title>Calibrate Color Eye</title>
			<!-- JQuery -->
		<link rel=StyleSheet href="css/bootstrap.min.css" type="text/css">
		<link rel=StyleSheet href="css/bootstrapxtra.css" type="text/css">
		<link rel=StyleSheet href="js/smoothness/jquery-ui.css" type="text/css">
		<link rel=StyleSheet href="css/CustomerSherColorWeb.css" type="text/css"> 
		<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
		<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/jquery-ui.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/bootstrap.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/CustomerSherColorWeb.js"></script>
		<script type="text/javascript" src="script/spectro.js"></script>
		<script type="text/javascript" src="script/WSWrapper.js"></script>
		<script>
			var calibrate_step = "start";		
		  	var ws_coloreye = new WSWrapper('coloreye');
		  	
		  	function InitializeCalibrationScreen() {
	  		    console.log("InitializeCalibrationScreen");
				$(".whitecal").hide();
				$(".blackcal").hide();
				$(".greenmeas").hide();
				$(".error").hide();
				$(".calsuccess").hide();
	  		}
		  	
	  	  	function GetCalSteps() {
	  		  	console.log("GetCalSteps")
	  		  	calibrate_step = "GetCalSteps";
  	    		var clreyemodel = $('#spectroModel').val();
				var spectromessage = new SpectroMessage('GetCalSteps',clreyemodel);
			    var json = JSON.stringify(spectromessage);
			    ws_coloreye.send(json);
	  		}
	  	  
	  	  	function CalibrateWhite() {
	  		  	console.log("CalibrateWhite")
	  		  	calibrate_step = "CalibrateWhite";
  	    		var clreyemodel = $('#spectroModel').val();
				var spectromessage = new SpectroMessage('CalibrateWhite',clreyemodel);
			    var json = JSON.stringify(spectromessage);
			    ws_coloreye.send(json);
		  		$(".pleasewait").hide();
		  		$(".blackcal").hide();
		  		$(".greenmeas").hide();
		  		$(".calsuccess").hide();
		  		$(".error").hide();
		  		$(".whitecal").show();
	  		}
	  	  	
	  	  	function CalibrateBlack() {
	  		  	console.log("CalibrateBlack")
	  		  	calibrate_step = "CalibrateBlack";
  	    		var clreyemodel = $('#spectroModel').val();
				var spectromessage = new SpectroMessage('CalibrateBlack',clreyemodel);
			    var json = JSON.stringify(spectromessage);
			    ws_coloreye.send(json);
			    $(".pleasewait").hide();
		  		$(".whitecal").hide();
		  		$(".greenmeas").hide();
		  		$(".calsuccess").hide();
		  		$(".error").hide();
		  		$(".blackcal").show();
	  		}
	  	  	
	  	  	function MeasureGreen() {
	  		  	console.log("MeasureGreen")
	  		  	calibrate_step = "MeasureGreen";
  	    		var clreyemodel = $('#spectroModel').val();
				var spectromessage = new SpectroMessage('MeasureGreen',clreyemodel);
			    var json = JSON.stringify(spectromessage);
			    ws_coloreye.send(json);
			    $(".pleasewait").hide();
		  		$(".whitecal").hide();
		  		$(".blackcal").hide();
		  		$(".calsuccess").hide();
		  		$(".error").hide();
		  		$(".greenmeas").show();
	  		}
	  	  	
	  	  	function DisplayError() {
	  		  	console.log("DisplayError")
	  		  	calibrate_step = "DisplayError";
	  		  	
			    $(".pleasewait").hide();
		  		$(".whitecal").hide();
		  		$(".blackcal").hide();
		  		$(".greenmeas").hide();
		  		$(".calsuccess").hide();
		  		$(".error").show();
	  		}
	  	  	
 
  	  	
	  	  	function CalibrateSuccess() {
	  		  	console.log("CalibrateSuccess")
	  		  	calibrate_step = "CalibrateSuccess";
	  		  	
			    $(".pleasewait").hide();
		  		$(".whitecal").hide();
		  		$(".blackcal").hide();
		  		$(".greenmeas").hide();
		  		$(".calsuccess").show();
		  		$(".error").hide();
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
  					default:
  						//Not an response we expected...
  						$("#errmsg").text("Unexpected call to " + return_message.command);
		  		  		DisplayError();
	  			}
	  		  	
	  	  	}
				
			$(document).ready(function() {	
				console.log("in docready");
			
				//this loads on startup!  
				var cntr = 1;
				InitializeCalibrationScreen();

				console.log("docready, between check and calibrate, isReady is " + ws_coloreye.isReady);
				
				//send the calibrate white message.
				GetCalSteps();
				
				//set a timer
	
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

<%-- 			this guid is <s:property value="thisGuid"/> --%>
<%-- 			this guid is <s:property value="%{reqGuid}"/> --%>
<!-- 			<br> -->
<%-- 			this sess is <s:property value="#session"/> --%>
<!-- 			<br> -->
<%-- 			jf obj is <s:property value="#session[reqGuid].jobFieldList"/> --%>
			

<br>
			<s:form action="colorUserNextAction" validate="true" theme="bootstrap">
				<div class="row">
	            
						<div class="col-sm-2">
						<s:hidden name="spectroModel" id="spectroModel" value="%{#session[reqGuid].spectroModel}"/>
						</div>
	            		<div class="col-sm-8">
	            			<h2 class="pleasewait"></h2>
 	            			<h2 class="whitecal">1. Remove the plastic cap from the white tile on the Calibration base.</h2>
 	            			<h2 class="blackcal">1. Replace the plastic cap on the white tile.</h2>
 	            			<h2 class="greenmeas">1. Flip the base and remove the plastic cap from the green tile.</h2>
 	            			<h2 class="error">A Calibration Error has occurred:</h2>
 	            			<h2 class="calsuccess">Successful Calibration</h2>
						</div>
					
				</div>
				
				<div class="row">
	            	
						<div class="col-sm-2">
						<s:hidden name="reqGuid" id="reqGuid" value="%{reqGuid}"/>
						</div>
	            		<div class="col-sm-8">
 	            			
						</div>
					
				</div>
				
				<div class="row">
	            
						<div class="col-sm-2">
						</div>
	            		<div class="col-sm-8">
	            			<h2 class="pleasewait"></h2>
 	            			<h2 class="whitecal">2. Position the target window on top of the white tile.</h2>
 	            			<h2 class="blackcal">2. Position the target window on top of the black opening.</h2>
 	            			<h2 class="greenmeas">2. Position the target window on top of the green tile.</h2>
 	            			<h2 class="error"></h2>
 	            		</div>
				
				</div>

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
	            			<h2 class="pleasewait">Connecting to color eye, please wait...</h2>
 	            			<h2 class="whitecal">3. Press the instrument firmly down until the next prompt appears.</h2>
 	            			<h2 class="blackcal">3. Press the instrument firmly down until the next prompt appears.</h2>
 	            			<h2 class="greenmeas">3. Press the instrument firmly down until the next prompt appears.</h2>
 	            			<h2 class="error" id="errmsg"></h2>
						</div>
					
				</div>
				
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
	            			<h2 class="pleasewait"></h2>
 	            			<h2 class="whitecal">Note: the two status lights on the instrument should change</h2>
 	            			<h2 class="blackcal">Note: the two status lights on the instrument should change</h2>
 	            			<h2 class="greenmeas">Note: the two status lights on the instrument should change</h2>
						</div>
					
				</div>
			
				<div class="row">
	           
						<div class="col-sm-2">
						</div>
	            		<div class="col-sm-8">
	            			<h2 class="pleasewait"></h2>
 	            			<h2 class="whitecal">from red to green on successful measurement.</h2>
 	            			<h2 class="blackcal">from red to green and the calibration light should change</h2>
 	            			<h2 class="greenmeas">from red to green on successful measurement.</h2>
 	            			
						</div>
					
				</div>
				<div class="row">
	            	
						<div class="col-sm-2">
						</div>
	            		<div class="col-sm-8">
	            			<h2 class="pleasewait"></h2>
 	            			<h2 class="whitecal"></h2>
 	            			<h2 class="blackcal"> to yellow on successful measurement.</h2>
 	            			<h2 class="greenmeas"></h2>
						</div>
					
				</div>
			
				<div class="row">
				
						<div class="col-sm-2">
						</div>	
						<div class="col-sm-2">
							<div class="calsuccess">
								<s:submit cssClass="btn btn-primary" value="Next" action="goodCalibrateAction"/>
							</div>
						</div>
						<div class="col-sm-2">	
						</div>
						<div class="col-sm-2">

						</div>
						<div class="col-sm-2">
			    			<s:submit cssClass="btn btn-secondary" value="Done" action="userCancelAction"/>
			    		</div>
			    	</div>
		    	
			</s:form>
		</div>
		
				<br>
		<br>
		<br>

  
		<!-- Including footer -->
		<s:include value="Footer.jsp"></s:include>
		
	</body>
</html>