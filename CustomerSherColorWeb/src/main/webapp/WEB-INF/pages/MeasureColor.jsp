<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>


<%@ taglib prefix="s" uri="/struts-tags" %>

<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title><s:text name="measureColor.measureColor"/></title>
			<!-- JQuery -->
		<link rel=StyleSheet href="css/bootstrap.min.css" type="text/css">
		<link rel=StyleSheet href="css/bootstrapxtra.css" type="text/css">
		<link rel=StyleSheet href="js/smoothness/jquery-ui.css" type="text/css">
		<link rel=StyleSheet href="css/CustomerSherColorWeb.css" type="text/css"> 
		<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
		<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/jquery-ui.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/bootstrap.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.4.6.js"></script>
		<script type="text/javascript" src="script/spectro.js"></script>
		<script type="text/javascript" src="script/WSWrapper.js"></script>
		<script>

		  	var ws_coloreye = new WSWrapper('coloreye');
		  	
		  	function InitializeMeasureScreen() {
	  		    console.log("InitializeMeasureScreen");
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
	  		  	checkWsIsReady();
  	    		var clreyemodel = $('#spectroModel').val();
				var spectromessage = new SpectroMessage('SWMeasure',clreyemodel);
			    var json = JSON.stringify(spectromessage);
			    ws_coloreye.send(json);
		  		$(".calibrate").hide();
		  		$('.init').hide();
		  		$(".swmeasure").show();
	  		}
	  	  	
	  	  	function GoodMeasure(measCurve) {
	  		  	console.log("GoodMeasure")
	  		  	console.log("meascurve = " + measCurve);
	  		  	document.getElementById("measuredCurve").value = measCurve;
	  		  $("#measure-color-form").submit();
	  		}
 	  	
	  	  	function DisplayError() {
				$('#measureColorModal').modal('hide');
	  		  	console.log("DisplayError");
		  		$(".error").show();
		  		$(".cancel").removeClass('d-none');
	  		}
	  	  	
	  	  	function RecdError() {
	  	  		$("#errmsg").text(return_message.errorMessage);
		  		DisplayError();
	  	  	}
	  	  	
	  	  	function calibrate(){
	  	  		$(".calibrate").removeClass('d-none');
	  	  		$('.init').hide();
	  	  		setTimeout(function(){
					$("#calibrateForm").submit();
	  	  		}, 1000);
	  	  	}
	  	  	
	  	  	function cancelMeasure(){
	  	  		$("#errmsg").text('<s:text name="measureColor.colormeasurementterminated"/>');
		  		DisplayError();
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
	  	  	
	  	  
	  	  	function RecdMessage() {
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

	  	  //function parses url to get value of specified param name
	  	  $.urlParam = function(name){
	  	  var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
	  	      if (results==null){
	  	         return null;
	  	      } else{
	  	         return results[1] || 0;
	  	      }
	  	  }
				
			$(document).ready(function() {	
				console.log("in docready");

				var measure = $.urlParam('measure');
				console.log('measure is ' + measure);
			    
			    if(measure != null && measure == "true"){
			    	$('#measureModalTitle').text('Measure First Sample');
			    }
				
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
			<s:hidden name="compare" id="compareColors" value="%{compare}"/>
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
						<s:hidden name="compare" id="compareColors" value="%{compare}"/>
					</div>
				</div>
				<br>
				<br>
				<div class="row">
					<div class="col-sm-3"></div>
					<div class="col-sm-6">
						<h2 class="calibrate d-none"><s:text name="measureColor.initializingCalibration"/></h2>
						<h2 class="init"><s:text name="measureColor.commWithColorEye"/></h2>
					</div>
					<div class="col-sm-3"></div>
				</div>
				<div class="row">
					<div class="col-sm-3"></div>
					<div class="col-sm-6">
						
					</div>
					<div class="col-sm-3"></div>
				</div>
				<div class="row">
					<div class="col-sm-3"></div>
					<div class="col-sm-6">
	            		<h2 class="error" id="errmsg"></h2>
					</div>
					<div class="col-sm-3"></div>
				</div>
				<br>
				<br>
				<br>
				<div class="row">
					<div class="col-sm-3">
					</div>
					<div class="col-sm-4">
					</div>
					<div class="col-sm-5">
						<div class="cancel d-none">
							<s:submit cssClass="btn btn-secondary center-block" value="%{getText('global.cancel')}" action="userCancelAction"/>
						</div>
					</div>
		    	</div>
			</div>
		</s:form>
		<div class="modal fade modal-xl" tabindex="-1" role="dialog" id="measureColorModal" data-backdrop="static">
		  <div class="modal-dialog modal-lg">
		    <div class="modal-content">
		      <div class="modal-header bg-light">
		      	<s:if test="compare">
		      		<h2 class="modal-title ml-3">Measure Second Sample</h2>
		      	</s:if>
		      	<s:else>
		      		<h2 class="modal-title ml-3" id="measureModalTitle"><s:text name="measureColor.measureColor"/></h2>
		      	</s:else>
		        <button type="button" class="close" data-dismiss="modal" aria-label="%{getText('global.close')}" onclick="cancelMeasure()">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      <div class="modal-body">
		        <div class="container-fluid">
					<div class="row">
						<div class="col-sm-1"></div>
					</div>
					<div class="row">
						<div class="col-sm-1"></div>
	            		<div class="col-sm-10">
	            			<h3 class="swmeasure"><s:text name="measureColor.positionColorEye"/></h3>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-2"></div>
	            		<div class="col-sm-10"></div>
					</div>
					<div class="row">
						<div class="col-sm-1"></div>
	            		<div class="col-sm-10">
	            			<h3 class="swmeasure"><s:text name="measureColor.pressFirmly"/></h3>
 	            		</div>
					</div>
					<div class="row">
						<div class="col-sm-1"></div>
	            		<div class="col-sm-10"></div>
					</div>
					<div class="row">
						<div class="col-sm-1"></div>
	            		<div class="col-sm-10">
	            			<h3 class="swmeasure"></h3>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-1"></div>
	            		<div class="col-sm-10"></div>
					</div>
					<br>
					<div class="row">
						<div class="col-sm-1"></div>
	            		<div class="col-sm-10">
	            			<h5 class="swmeasure"><s:text name="measureColor.statusLightsShouldChangeRedToGreen"/></h5>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-1"></div>
	            		<div class="col-sm-10">
	            			<h5 class="swmeasure"></h5>
						</div>
					</div>
				</div>
		      </div>
		      <div class="modal-footer">
			       <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="cancelMeasure()"><s:text name="global.close"/></button>
			  </div>
		    </div>
		  </div>
		</div>
		<br>
		<br>
		<br>

		<!-- Including footer -->
		<s:include value="Footer.jsp"></s:include>
		
	</body>
</html>