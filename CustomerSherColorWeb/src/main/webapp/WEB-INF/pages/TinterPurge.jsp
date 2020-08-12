<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>


<%@ taglib prefix="s" uri="/struts-tags" %>

<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title>Tinter Purge</title>
			<!-- JQuery -->
		<link rel=StyleSheet href="css/bootstrap.min.css" type="text/css">
		<link rel=StyleSheet href="css/bootstrapxtra.css" type="text/css">
		<link rel=StyleSheet href="js/smoothness/jquery-ui.css" type="text/css">
		<link rel=StyleSheet href="css/CustomerSherColorWeb.css" type="text/css"> 
		<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
		<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/jquery-ui.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/bootstrap.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/moment.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.4.2.js"></script>
		<script type="text/javascript" charset="utf-8"	src="script/WSWrapper.js"></script>
		<script type="text/javascript" charset="utf-8"	src="script/tinter-1.4.4.js"></script>
		<s:set var="thisGuid" value="reqGuid" />
		<style>
	        .sw-bg-main {
	            background-color: ${sessionScope[thisGuid].rgbHex};
	        }
	        
	        #verifyScanInputError {
  				font-weight: bold;
  				color: red;
			}
	        
	    </style>
	    
	<script type="text/javascript">
	 //Global Variables
	"use strict"; 
	var ws_tinter = new WSWrapper("tinter");
	var sendingTinterCommand = "false";
	var _rgbArr = [];
	var dispenseErrorList = [];
	<s:iterator value="canList" status="i">
	_rgbArr["<s:property value="clrntCode"/>"]="<s:property value="rgbHex"/>";  //for colored progress bars
	</s:iterator>

    function fkey(e){
    	if(sendingTinterCommand == "true"){
        e = e || window.event;
        
        if (e.code === 'F4') {
        	abort();
            console.log(e);
            e.preventDefault();
        }
    }
}
	function getRGB(colorantCode){
		var rgb = "";
		if(colorantCode != null){
			rgb = _rgbArr[colorantCode];
		}
		return rgb;
	}
	function buildProgressBars(return_message){
		var count = 1;
		var keys=[];
		$(".progress-wrapper").empty();
		keys = Object.keys(return_message.statusMessages);
		if (keys !=null && keys.length > 0) {			
			return_message.statusMessages.forEach(function(item){
				var colorList = item.message.split(" ");
				var color= colorList[0];
				var pct = colorList[1];
				//fix bug where we are done, but not all pumps report as 100%
				if (return_message.errorMessage.indexOf("done") > 1 && (return_message.errorNumber == 0 &&
						 return_message.status == 0)) {
					  pct = "100%";
				  }
				//$("#tinterProgressList").append("<li>" + item.message + "</li>");
				
				var $clone = $("#progress-0").clone();
				$clone.attr("id","progress-" + count);
				var $bar = $clone.children(".progress-bar");
				$bar.attr("id","bar-" + count);
				$bar.attr("aria-valuenow",pct);
				$bar.css("width", pct);
				$clone.css("display", "block");
				var color_rgb = getRGB(color);
	//change color of text based on background color
				switch(color){
				case "WHT":
				case "TW":
				case "W1":
					$bar.children("span").css("color", "black");
					$bar.css("background-color", "#efefef");
					break;
				case "OY":
				case "Y1":
				case "YGS":
					$bar.children("span").css("color", "black");
					$bar.css("background-color", color_rgb);
					break;
				default:
					$bar.css("background-color", color_rgb);
					$bar.children("span").css("color", "white");
					break;
				}
				
				
				$bar.children("span").text(color + " " + pct);
				console.log("barring " + item.message);
				//console.log($clone);
				
				$clone.appendTo(".progress-wrapper");
				
				count++;
			});
		}
	}
	function FMXPurgeProgress(){
		console.log('before purge status modal show');
		$("#PurgeInProgressModal").modal('show');
		rotateIcon();
		var cmd = "PurgeProgress";
		var shotList = null;
		var configuration = null;
    	var tintermessage = new TinterMessage(cmd,null,null,null,null);  
    	var json = JSON.stringify(tintermessage);
		sendingTinterCommand = "true";
    	ws_tinter.send(json);
	}
	function purge(){
		var cmd = "PurgeAll";
		
		var shotList = null;
    	var tintermessage = new TinterMessage(cmd,shotList,null,null,null);  
    	var json = JSON.stringify(tintermessage);
		sendingTinterCommand = "true";
		if(ws_tinter!=null && ws_tinter.isReady=="false") {
    		console.log("WSWrapper connection has been closed (timeout is defaulted to 5 minutes). Make a new WSWrapper.")
    		ws_tinter = new WSWrapper("tinter");
		}
    	ws_tinter.send(json);
	}
	function dispenseProgressResp(myGuid, curDate,return_message, tedArray){
		//$("#progress-message").text(return_message.errorMessage);
		$("#abort-message").show();
		if (return_message.errorMessage.indexOf("Done") == -1 && (return_message.errorNumber == 1 ||
				 return_message.status == 1)) {
			//keep updating modal with status
			//$("#progress-message").text(return_message.errorMessage);
			$("#tinterProgressList").empty();
			dispenseErrorList = [];
			if(return_message.statusMessages!=null && return_message.statusMessages[0]!=null){
				return_message.statusMessages.forEach(function(item){
					buildProgressBars(return_message);
						//$("#tinterProgressList").append("<li>" + item.message + "</li>");
					dispenseErrorList.push(item.message);
				});
			} else {
				dispenseErrorList.push(return_message.errorMessage);
				$("#tinterProgressList").append("<li>" + return_message.errorMessage + "</li>");
			}
			console.log(return_message);
			//setTimeout(function(){
				FMXPurgeProgress();
		//	}, 200);  //send progress request after waiting 200ms.  No need to slam the SWDeviceHandler
			
		}
		else{
			purgeComplete(myGuid, curDate,return_message, tedArray, "fmx");
			$(".progress-wrapper").empty();
			}
			
    }
	function FMXShowTinterErrorModal(myTitle, mySummary, my_return_message){
	    $("#tinterErrorList").empty();
	    $("#tinterErrorListModal").modal('show');
	    $("#abort-message").hide();
	    
	    if(my_return_message.statusMessages!=null && my_return_message.statusMessages[0]!=null){
	    	if(my_return_message.statusMessages.length > 0){
	    		buildProgressBars(my_return_message);  // on an abort, for example, we will have a progress update to do.
	    	}
	    	/*
	        my_return_message.errorList.forEach(function(item){
	            $("#tinterErrorList").append( '</li>' + item.message + '</li>');
	        });
	        */
	    } 
	    if(my_return_message.errorNumber == 4226){
	    	my_return_message.errorMessage = "Tinter Driver busy.  Please re-initialize tinter and retry command."
				    }
	    $("#tinterErrorList").append('<li class="alert alert-danger">' + my_return_message.errorMessage + '</li>');
	    
	    if(myTitle!=null) $("#tinterErrorListTitle").text(myTitle);
	    else $("#tinterErrorListTitle").text("Tinter Error");
	    if(mySummary!=null) $("#tinterErrorListSummary").text(mySummary);
	    else $("#tinterErrorListSummary").text("");
	  
	}
	function showTinterErrorModal(myTitle, mySummary, my_return_message){
		$("#tinterErrorList").empty();
		if(my_return_message.errorList!=null && my_return_message.errorList[0]!=null){
			my_return_message.errorList.forEach(function(item){
				$("#tinterErrorList").append("<li>" + item.message + "</li>");
			});
		} else {
			$("#tinterErrorList").append("<li>" + my_return_message.errorMessage + "</li>");
		}
		if(myTitle!=null) $("#tinterErrorListTitle").text(myTitle);
		else $("#tinterErrorListTitle").text("Tinter Error");
		if(mySummary!=null) $("#tinterErrorListSummary").text(mySummary);
		else $("#tinterErrorListSummary").text("");
		$("#tinterErrorListModal").modal('show');
	}
    function purgeComplete(myGuid, curDate,return_message, tedArray, fmx){
    	 $("#abort-message").hide();
         
		if((return_message.errorNumber == 0 && return_message.commandRC == 0) || (return_message.errorNumber == -10500 && return_message.commandRC == -10500)){
			// show purge
			//var displayDate = (curDate.getMonth()+1) + "/" + curDate.getDate() + "/" + curDate.getFullYear();
			//var displayTime = curDate.getHours() + ":" + curDate.getMinutes() + ":" + curDate.getSeconds();
			//$("#lastPurgeText").text("Last purge was done on " + displayDate + " at " + displayTime + " by " + $("#tinterPurgeAction_currUser").val())
			$("#lastPurgeText").text("Last purge was done on " + moment(curDate).format('ddd MMM DD YYYY') + " at " + moment(curDate).format('LT') + " by " + $("#tinterPurgeAction_currUser").val())
            waitForShowAndHide("#purgeInProgressModal");
			// show success message in alert area
			$("#tinterAlertList").empty();
			$("#tinterAlertList").append("<li>Purge Successful</li>");
			if($("#tinterAlert").hasClass("d-none")) $("#tinterAlert").removeClass("d-none");
			if($("#tinterAlert").hasClass("alert-danger")) $("#tinterAlert").removeClass("alert-danger");
			if(!$("#tinterAlert").hasClass("alert-success")) $("#tinterAlert").addClass("alert-success");
		} else {
			waitForShowAndHide("#purgeInProgressModal");
			$("#tinterAlertList").empty();
			$("#tinterAlertList").append("<li>Purge Failed:" + return_message.errorMessage + "</li>");
			if($("#tinterAlert").hasClass("d-none")) $("#tinterAlert").removeClass("d-none");
			if(!$("#tinterAlert").hasClass("alert-danger")) $("#tinterAlert").addClass("alert-danger");
			if($("#tinterAlert").hasClass("alert-success")) $("#tinterAlert").removeClass("alert-success");
			//Show a modal with error message to make sure the user is forced to acknowledge it.
			if(fmx = "fmx"){
				FMXShowTinterErrorModal(null,null,return_message);
			}
			else{				
				showTinterErrorModal(null,null,return_message);
			}
		}
		 return_message.command = "PurgeAll";
	    sendTinterEvent(myGuid, curDate, return_message, tedArray);
    }
    function abort(){
    	console.log('before abort');
    	
    	
    	var cmd = "Abort";
    	var shotList = null;
    	var configuration = null;
    	var tintermessage = new TinterMessage(cmd,null,null,null,null);  
    	var json = JSON.stringify(tintermessage);

    	ws_tinter.send(json);
    }
	function openNozzle(){
		var cmd = "OpenNozzle";
		$("#cleanNozzleButton").prop('disabled', true);
		var shotList = null;
    	var tintermessage = new TinterMessage(cmd,shotList,null,null,null);  
    	var json = JSON.stringify(tintermessage);
		sendingTinterCommand = "true";
		if(ws_tinter!=null && ws_tinter.isReady=="false") {
    		console.log("WSWrapper connection has been closed (timeout is defaulted to 5 minutes). Make a new WSWrapper.")
    		ws_tinter = new WSWrapper("tinter");
		}
    	ws_tinter.send(json);
	}

	function closeNozzle(){
		var cmd = "CloseNozzle";
		
		var shotList = null;
    	var tintermessage = new TinterMessage(cmd,shotList,null,null,null);  
    	var json = JSON.stringify(tintermessage);
		sendingTinterCommand = "true";
		if(ws_tinter!=null && ws_tinter.isReady=="false") {
    		console.log("WSWrapper connection has been closed (timeout is defaulted to 5 minutes). Make a new WSWrapper.")
    		ws_tinter = new WSWrapper("tinter");
		}
    	ws_tinter.send(json);
	}

	function RecdMessage() {
		console.log("Received Message");
		//parse the spectro
		console.log("isReady is " + ws_tinter.isReady + "BTW");
		if(ws_tinter.wserrormsg!=null && ws_tinter.wserrormsg!=""){
			if(sendingTinterCommand == "true"){
				// received an error from WSWrapper so we won't get any JSON result
				// Since we are sending a tinter command, show as tinter error
				//Show a modal with error message to make sure the user is forced to read it.
				waitForShowAndHide("#purgeInProgressModal");
				$("#tinterSocketErrorModal").modal('show');
				$("#tinterSocketError").text(ws_tinter.wserrormsg);
				
			} else {
				console.log("Received unsolicited error " + ws_tinter.wserrormsg);
				// so far this only happens when SWDeviceHandler is not running and we created a new WSWrapper when 
				// page intially loaded.  For now wait until they do a dispense to show the error (no everybody has a tinter)
			}
		} else {
			// is result (wsmsg) JSON?
			var isTintJSON = false;
			try{
				if(ws_tinter!=null && ws_tinter.wsmsg!=null){
					var return_message=JSON.parse(ws_tinter.wsmsg);
					isTintJSON = true;
				}
			}
			catch(error){
                console.log("Caught error is = " + error);
				console.log("Message is junk, throw it out");
				//console.log("Junk Message is " + ws_tinter.wsmsg);
			}
			if(isTintJSON){
				var return_message=JSON.parse(ws_tinter.wsmsg);
				switch (return_message.command) {
					case 'PurgeAll':
					case 'PurgeProgress':
					case 'DispenseProgress':
			    		sendingTinterCommand = "false";
						// log tinter event...
						var curDate = new Date();
						var myGuid = $( "#tinterPurgeAction_reqGuid" ).val();
						var teDetail = new TintEventDetail("PURGE USER", $("#tinterPurgeAction_currUser").val(), 0);
						var tedArray = [teDetail];
						var tinterModel = $("#tinterPurgeAction_tinterModel").val();
						if(tinterModel !=null && tinterModel.startsWith("FM X")){ //only FM X series has purge in progress % done
							 if(return_message.errorNumber == 4226){
							    	return_message.errorMessage = "Tinter Driver busy.  Please re-initialize tinter and retry command."
							    }
							dispenseProgressResp(myGuid, curDate,return_message, tedArray); 
						}
						else{  
							 
							purgeComplete(myGuid, curDate,return_message, tedArray);
						}
						break;
					
					
					case 'OpenNozzle':
			    		sendingTinterCommand = "false";
						// log tinter event...
						var curDate = new Date();
						var myGuid = $( "#tinterPurgeAction_reqGuid" ).val();
						var teDetail = new TintEventDetail("NOZZLE USER", $("#tinterPurgeAction_currUser").val(), 0);
						var tedArray = [teDetail];
						sendTinterEvent(myGuid, curDate, return_message, tedArray); 
						if((return_message.errorNumber == 0 && return_message.commandRC == 0) || (return_message.errorNumber == -10500 && return_message.commandRC == -10500)){
							// Enable Done button
							$("#cleanNozzleButton").prop('disabled', false);
						} else {
							waitForShowAndHide("#cleanNozzleModal");
							//Show a modal with error message to make sure the user is forced to read it.
							showTinterErrorModal("Open Nozzle Cover Error",null,return_message);
						}
						break;
					case 'CloseNozzle':
			    		sendingTinterCommand = "false";
						// log tinter event...
						var curDate = new Date();
						var myGuid = $( "#tinterPurgeAction_reqGuid" ).val();
						var teDetail = new TintEventDetail("NOZZLE USER", $("#tinterPurgeAction_currUser").val(), 0);
						var tedArray = [teDetail];
						sendTinterEvent(myGuid, curDate, return_message, tedArray); 
						if((return_message.errorNumber == 0 && return_message.commandRC == 0) || (return_message.errorNumber == -10500 && return_message.commandRC == -10500)){
							waitForShowAndHide("#closeNozzleInProgressModal");
						} else {
							waitForShowAndHide("#closeNozzleInProgressModal");
							//Show a modal with error message to make sure the user is forced to read it.
							showTinterErrorModal(null,null,return_message);
						}
						break;
					default:
						//Not an response we expected...
						console.log("Message from different command is junk, throw it out");
				}
			} else {
				console.log("Message is junk, throw it out");
			}
		}
	}

 

//     $("nozzleCleanX").click(function(){
//         $("#cleanNozzleVid").get(0).pause();
//     });
    

	//Used to rotate loader icon in modals
	function rotateIcon(){
		let n = 0;
		$('#spinner').removeClass('d-none');
		let interval = setInterval(function(){
	    	n += 1;
	    	if(n >= 60000){
	            $('#spinner').addClass('d-none');
	        	clearInterval(interval);
	        }else{
	        	$('#spinner').css("transform","rotate(" + n + "deg)");
	        }
		},5);
		
		$('#purgeInProgressModal').one('hide.bs.modal',function(){
			$('#spinner').addClass('d-none');
        	if(interval){clearInterval(interval);}
		});
	}

	$(function(){
		$(document).on("shown.bs.modal", "#purgeInProgressModal", function(event){
			purge();
	    });
	    
	    $(document).on("shown.bs.modal", "#cleanNozzleModal", function(event){
	        $("#cleanNozzleVid").get(0).play();
			openNozzle();
	    });

	    $(document).on("hidden.bs.modal", "#cleanNozzleModal", function(event){
	        $("#cleanNozzleVid").get(0).pause();
			closeNozzle();
	    });
	    
	    $(document).on("click", "#cleanNozzleButton", function(event){
	        $("#cleanNozzleVid").get(0).pause();
			event.preventDefault();
			event.stopPropagation();
			waitForShowAndHide("#cleanNozzleModal");
			$("#closeNozzleInProgressModal").modal('show');
			
			//closeNozzle();  now done on hidden.bs.modal 
		});
	    
		//localhostConfig will be set if they have returned to landing page and have a tinter attached
		if($("#tinterPurgeAction_hasAutoNozzle").val()=="false"){ 
			$('#tinterCleanNozzle').hide();
		}
		
		$('#purgeInProgressModal').on('show.bs.modal',function(){
			rotateIcon();
		});
		//capture F4 key to abort
		jQuery(document).on("keydown",fkey);
	});
    
</script>
</head>
	
	<body>
		<!-- including Header -->
		<s:include value="Header.jsp"></s:include>  
		
		<div class="container-fluid">
			<s:form action="tinterPurgeAction" validate="true"  theme="bootstrap">
				<div class="row mt-5 mb-2">
					<div class="col-sm-3">
 						<s:hidden name="reqGuid" value="%{reqGuid}"/>  
			 	    	<s:hidden name="hasAutoNozzle" value="%{autoNozzleCover}" />
			 	    	<s:hidden name="currUser" value="%{currentUser}" />
			 	    	<input id="tinterPurgeAction_tinterModel" type="hidden" name="tinterModel" value="${sessionScope[thisGuid].tinter.model}" />
					</div>
					<div class="col-sm-6">
						<div class="card card-body bg-light">
							<div><span class="badge badge-secondary mb-1" style="font-size: 1.2rem;">Purge Tinter</span></div>
							<h4 class="text-primary"><strong>${sessionScope[thisGuid].tinter.model}</strong></h4>
						</div>	
					</div>
					<div class="col-sm-3">
					</div>
				</div>
				<!-- do not display nozzle instructions for x - series -->
				<div class="row">
						<div class="col-sm-3">
						</div>
						<div class="col-sm-6">
							<div class="card card-body bg-light mb-3">
						        <s:if test="%{tinter.model != null && tinter.model.startsWith('FM X')}"> 
									<p class="lead">1. Thoroughly Clean Nozzle with the Cleaning Tool</p>
									<p class="lead">2. Clean and Moisten Humidifier Sponge Before Purging</p>
									<p class="lead">3. Position Container and click Purge button to start purge colorants</p>
									<p></p>
								</s:if>
								<p class="lead" id="lastPurgeText">Last purge was done on <s:property value="lastPurgeDate" escapeHtml="true"/> by <s:property value="lastPurgeUser" escapeHtml="true"/></p>
								<p></p>
							</div>
						</div>
						<div class="col-sm-3">
						</div>
					</div>
					<div class="row">
						<div class="col-sm-3">
							
						</div>
						<div class="col-sm-6">
							<div class="alert d-none" id="tinterAlert">
								<ul class="list-unstyled mb-0" id="tinterAlertList">
								</ul>
							 <!-- Put text here -->
							</div>
						</div>
						<div class="col-sm-3">
		
						</div>
					</div>
				
				
				<div class="row">
					<div class="col-sm-3">
					</div>
					<div class="col-sm-2">	
						<button type="button" class="btn btn-primary" id="tinterCleanNozzle" data-toggle="modal" data-target="#cleanNozzleModal">Clean Nozzle</button>
  						</div>
					<div class="col-sm-2">	
						<button type="button" class="btn btn-primary center-block" id="tinterPurge" data-toggle="modal" data-target="#purgeInProgressModal" autofocus="autofocus">Purge</button>
  						</div>
					<div class="col-sm-2">	
		    			<s:submit cssClass="btn btn-secondary pull-right" value="Done" action="userCancelAction"/>
  						</div>
  						<div class="col-sm-3">
					</div>
		    	</div>
				<br>	
				<br>	
				<s:if test="%{tinter.model != null && tinter.model.startsWith('FM X')}"> 
					<div class="row">
						<div class="col-sm-3">
						</div>
						<div class="col-sm-6">
							<p class="bg-warning text-center"><strong>Nozzle Cleaning Tool can be ordered through Color Resources Help Desk 800-232-5192</strong></p>
						</div>
						<div class="col-sm-3">
						</div>
					</div>
			    </s:if>
			    <!-- Clean Nozzle Modal Window -->
			    <div class="modal fade" aria-labelledby="cleanNozzleModal" aria-hidden="true"  id="cleanNozzleModal" role="dialog" data-backdrop="static" data-keyboard="false">
			    	<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								<h5 class="modal-title">Clean Nozzle</h5>
								<button type="button" class="close" data-dismiss="modal" aria-label="Close"  id="cleanNozzleX"><span aria-hidden="true">&times;</span></button>
							</div>
							<div class="modal-body">
								<video id="cleanNozzleVid" width="320" height="240" loop muted>
<%-- 									<source src="<s:url action='getNozzleVideoAction'/>" type="video/mp4">>Browser does not support video. Sorry --%>
									<source src="video/nozzle_open.mp4" type="video/mp4">Browser does not support video. Sorry
									</source>
								</video>
								<p class="cleanNozzleText h5 pt-3">1. Clean the nozzle with the Cleaning Tool.</p>
								<p class="cleanNozzleText h5">2. Clean and moisten the Humidifier Sponge.</p>
								<p class="cleanNozzleText h5">3. Remove all the cleaning tools from the nozzle area before closing it.</p>
								<br/>
								<p class="cleanNozzleText h5 bg-warning text-center pb-3" >Clicking the Done button will close the Nozzle</p>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-primary center-block" id="cleanNozzleButton">Done</button>
							</div>
						</div>
					</div>
				</div>			    

			    <!-- Close Nozzle in Progress Modal Window -->
			    <div class="modal fade" aria-labelledby="closeNozzleInProgressModal" aria-hidden="true"  id="closeNozzleInProgressModal" role="dialog" data-backdrop="static" data-keyboard="false">
			    	<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								<h5 class="modal-title">Closing Nozzle Cover</h5>
								<button type="button" class="close" data-dismiss="modal" aria-label="Close" ><span aria-hidden="true">&times;</span></button>
							</div>
							<div class="modal-body">
								<p font-size="4">Please wait while Nozzle Cover closes...</p>
							</div>
							<div class="modal-footer">
							</div>
						</div>
					</div>
				</div>			    

			    <!-- Purge in Progress Modal Window -->
			    <div class="modal fade" aria-labelledby="purgeInProgressModal" aria-hidden="true"  id="purgeInProgressModal" role="dialog" data-backdrop="static" data-keyboard="false">
			    	<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								<i id="spinner" class="fa fa-refresh mr-3 mt-1 text-muted" style="font-size: 1.5rem;"></i>
								<h5 class="modal-title">Purge All In Progress</h5>
								<button type="button" class="close" data-dismiss="modal" aria-label="Close" ><span aria-hidden="true">&times;</span></button>
							</div>
							<div class="modal-body">
							
								<p id="progress-message" font-size="4">Please wait while the tinter performs Purge All Colorants...</p>
								<p id="abort-message" font-size="4" style="display:none; color:purple;font-weight:bold"> Press F4 to abort </p>
								<div class="progress-wrapper "></div>
								<ul class="list-unstyled" id="tinterProgressList">
										</ul>
							</div>
							<div class="modal-footer">
							</div>
						</div>
					</div>
				</div>			    

			    <!-- Tinter Socket Error Modal Window -->
			    <div class="modal fade" aria-labelledby="tinterSocketErrorModal" aria-hidden="true"  id="tinterSocketErrorModal" role="dialog">
			    	<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								<h5 class="modal-title">Tinter Error</h5>
								<button type="button" class="close" data-dismiss="modal" aria-label="Close" ><span aria-hidden="true">&times;</span></button>
							</div>
							<div class="modal-body">
								<p id="tinterSocketError" font-size="4"></p>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-primary" id="tinterSocketErrorButton" data-dismiss="modal" aria-label="Close" >Close</button>
							</div>
						</div>
					</div>
				</div>			    
			    <!-- Tinter ErrorList Modal Window -->
			    <div class="modal fade" aria-labelledby="tinterErrorListModal" aria-hidden="true"  id="tinterErrorListModal" role="dialog">
			    	<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								<h5 class="modal-title" id="tinterErrorListTitle">Tinter Error</h5>
								<button type="button" class="close" data-dismiss="modal" aria-label="Close" ><span aria-hidden="true">&times;</span></button>
							</div>
							<div class="modal-body">
								<div class="progress-wrapper "></div>
								<div>
									<ul class="list-unstyled" id="tinterErrorList">
									</ul>
								</div>
								<p id="tinterErrorListSummary" font-size="4"></p>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-primary" id="tinterErrorListOK" data-dismiss="modal" aria-label="Close" >OK</button>
							</div>
						</div>
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
		<!-- dummy div to clone -->
		<div id="progress-0" class="progress" style="display:none; margin:10px;">
	        <div id="bar-0" class="progress-bar" role="progressbar" aria-valuenow="0"
					 aria-valuemin="0" aria-valuemax="100" style=" width: 0%; background-color: blue">
					 <span></span>
	  		</div>
	  		<br/>
		</div>
		<!-- Including footer -->
		<s:include value="Footer.jsp"></s:include>
		
	</body>

