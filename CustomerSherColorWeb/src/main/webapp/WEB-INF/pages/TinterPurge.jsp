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
		<script type="text/javascript" charset="utf-8" src="script/CustomerSherColorWeb.js"></script>
		<script type="text/javascript" charset="utf-8"	src="script/WSWrapper.js"></script>
		<script type="text/javascript" charset="utf-8"	src="script/tinter-1.3.1.js"></script>
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
    
	// now build the dispense formula object
	ws_tinter = new WSWrapper("tinter");
	var sendingTinterCommand = "false";
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
		if (return_message.errorMessage.indexOf("Done") == -1 && (return_message.errorNumber == 1 ||
				 return_message.status == 1)) {
			//keep updating modal with status
			//$("#progress-message").text(return_message.errorMessage);
			$("#tinterErrorList").empty();
			initErrorList = [];
			if(return_message.errorList!=null && return_message.errorList[0]!=null){
				return_message.errorList.forEach(function(item){
					$("#tinterErrorList").append("<li>" + item.message + "</li>");
					initErrorList.push(item.message);
				});
			} else {
				initErrorList.push(return_message.errorMessage);
				$("#tinterErrorList").append("<li>" + return_message.errorMessage + "</li>");
			}
			console.log(return_message);
			//setTimeout(function(){
				FMXPurgeProgress();
		//	}, 200);  //send progress request after waiting 200ms.  No need to slam the SWDeviceHandler
			
		}
		else{
			purgeComplete(myGuid, curDate,return_message, tedArray);
			}
			
    }
    function purgeComplete(myGuid, curDate,return_message, tedArray){
        return_message.command = "PurgeAll";
    	sendTinterEvent(myGuid, curDate, return_message, tedArray);
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
			showTinterErrorModal(null,null,return_message);
		}
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

	});

    

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
							<s:if test="%{#sessionScope[thisGuid].tinter.model=='FM XPROTINT' || #sessionScope[thisGuid].tinter.model=='FM XSMART'  }">
					
								<p class="lead">1. Thoroughly Clean Nozzle with the Cleaning Tool</p>
								<p class="lead">2. Clean and Moisten Humidifier Sponge Before Purging</p>
								<p class="lead">3. Position Container and click Purge button to start purge colorants</p>
								<p></p>
								</s:if>
								<p class="lead" id="lastPurgeText">Last purge was done on <s:property value="lastPurgeDate"/> by <s:property value="lastPurgeUser"/></p>
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
				<s:if test="%{#sessionScope[thisGuid].tinter.model=='FM XPROTINT' || #sessionScope[thisGuid].tinter.model=='FM XSMART'  }">
				
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
								<ul class="list-unstyled" id="tinterErrorList">
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
  
		<!-- Including footer -->
		<s:include value="Footer.jsp"></s:include>
		
	</body>
