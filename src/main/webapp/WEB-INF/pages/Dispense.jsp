<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!doctype html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="global.dispenseColorants"/></title>
<!-- JQuery -->
<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
<link rel="stylesheet" href="css/bootstrapxtra.css" type="text/css">
<link rel="stylesheet" href="js/smoothness/jquery-ui.min.css" type="text/css">
<link rel="stylesheet" href="css/CustomerSherColorWeb.css" type="text/css">
<link rel="stylesheet" href="font-awesome-4.7.0/css/font-awesome.min.css" type="text/css">
<s:set var="thisGuid" value="reqGuid" />
<s:set var="thisUOM" value="UOM" />
<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
<script type="text/javascript" charset="utf-8"src="js/jquery-ui.min.js"></script>
<script type="text/javascript" charset="utf-8"	src="js/popper.min.js"></script>
<script type="text/javascript" charset="utf-8" src="js/bootstrap.min.js"></script>
<script type="text/javascript" charset="utf-8"	src="js/moment.min.js"></script>
<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.5.2.js"></script>
<script type="text/javascript" charset="utf-8" src="script/WSWrapper.js"></script>
<script type="text/javascript" charset="utf-8" src="script/tinter-1.4.8.js"></script>
<script type="text/javascript" charset="utf-8" src="script/dispense-1.5.2.js"></script>
<script type="text/javascript">
var processingDispense = false;
	<s:iterator value="tinter.canisterList" status="i">
//DJM not sure what this shotList is here for, but no way I'm deleting it now :) 
		shotList.push(new Colorant("<s:property value="clrntCode"/>",<s:property value="shots"/>,<s:property value="position"/>,<s:property value="thisUOM"/>));	
		_rgbArr["<s:property value="clrntCode"/>"]="<s:property value="rgbHex"/>";  //for colored progress bars
	</s:iterator>
	
	function writeDispense(return_message){
		dispenseComplete(return_message);
	}

	//Document ON-Load
	$(function() {
		var platform = navigator.platform;
		if(platform.startsWith("Win")){
			$('#abort-message').html('<s:text name="global.pressF4ToAbort"/>');
		} else {
			$('#abort-message').html('<s:text name="global.pressAkeyToAbort"/>');
		}
		getSessionTinterInfo($("#reqGuid").val(), warningCheck);
		jQuery(document).on("keydown",fkey);
		//Popover closing functionality
		$('table.table-bordered').on('click', function(event) {
			$('.popover').each(function() {
				$(this).popover('toggle');
				$('input[data-toggle="popover"]').each(function() {
					$(this).removeAttr('data-placement');
					$(this).removeAttr('data-content');
					$(this).removeAttr('data-toggle');
				});
			});
		});


		// Handle/validate form input on click
		$('div #dispense').on('click', function() {
			if (processingDispense == false) {
				processingDispense = true;
			//Starts chain of functions to do all predispense checks and subsequently dispense if all is well.
				preDispenseRoutine();
			}
		});

		//Handle warning notification on-click
		$('#tinterWarningListOK').click(function(event) {
			//Dispensing if shotList contains values
			if (shotList.length > 0) {
				stopSessionTimeoutTimers(timeoutWarning, timeoutExpire);
				decrementColorantForDispense($('#reqGuid').val(), shotList, decrementCallback);
			} else {
				console.log("Shotlist empty, dispense not executed.");
				$('#tinterInProgressTitle').text(i18n['dispense.qtyError']);
				$('#tinterInProgressMessage').text(i18n['dispense.noValuesEntered']);
				$('#progressok').removeClass('d-none');
			}
			$(".progress-wrapper").empty();
			$("#tinterInProgressModal").modal('show');
		});

		$('#progressok').click(function() {
			$('#progressok').addClass('d-none');
		});

		jQuery(document).on("keydown", fkey);

	});
</script>
<style type="text/css">
input {
	padding: 3px 3px !important;
}
h4 {
	font-size: 16px;
	margin-top: 5px;
	margin-bottom: 5px;
}
.chip {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    border: .5px solid black;
    float: left;
    margin-right: 15px;
}
.popover-danger {
	background-color: #d9534f;
	border-color: #d43f3a;
	color: white;
}
.popover-body {
	color: white;
}

.popover .arrow:after {
	border-bottom-color: #d9534f;
	border-top-color: #d9534f;
}
.popover .arrow {
	border-bottom-color: #d9534f;
	border-top-color: #d9534f;
}

@media only screen and (max-width: 768px) {
    /* For mobile phones: */
    .colorcol {
    	padding-left: 0px;
    }
    .chip {
    	width: 30px;
    	height: 30px;
    	margin-right: 10px;
    }
      .sw-bg-main {
	            background-color: ${sessionScope[thisGuid].rgbHex};
	        }
}
input[type=number]::-webkit-inner-spin-button, 
input[type=number]::-webkit-outer-spin-button { 
    -webkit-appearance: none;
    -moz-appearance: none;
    appearance: none;
    margin: 0; 
}
input[type=number] {
    -moz-appearance:textfield;
}
</style>
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
							<s:hidden value="%{thisGuid}" id="reqGuid"></s:hidden>
							<s:hidden value="%{tinter.model}" id="tinterModel"></s:hidden>
						</div>
					</div>
					<br>
					<div class="row">
						<div class="col-xl-1 col-lg-0 col-md-0 col-sm-0">
						</div>
						<div class="col-xl-10 col-lg-12 col-md-12 col-sm-12">
							<div class="card card-body bg-light mb-3">
									<div class="d-flex flex-row justify-content-around">
									<div class="p-2 mr-3" style="width: 10rem;">
										<span class="badge badge-secondary" style="font-size: 1.2rem;"><s:text name="global.dispense"/></span>
										<h5 class="text-primary mt-3"><strong><s:property value="tinter.model" escapeHtml="true"/></strong></h5>
									</div>
									<div class="p-2">
										<div class="alert alert-warning text-center">
										  	<p class="m-0"><strong><s:text name="global.noteColon"/></strong> <s:text name="dispense.onlyWholePositiveNbrs"/></p>
										</div>
									</div>
									<div class="p-2 ml-3">
										<s:url var="dispenseURL" action="executeDispenseColorantsAction"><s:param name="reqGuid" value="%{thisGuid}"/></s:url>
										<button class="btn btn-primary mr-4" id="dispense"><s:text name="global.dispense"/></button>
									</div>
									<div class="p-2">
										<s:url var="cancelURL" action="userCancelAction"><s:param name="reqGuid" value="%{thisGuid}"/></s:url>
										<a href='<s:property value="cancelURL"/>' class="btn btn-secondary" id="cancel"><s:text name="global.done"/></a>
									</div>
								</div>
							</div>	
							  <table class="table table-bordered table-sm">
							    <thead>
							      <tr>
							        <th class="bg-light" style="width: 60%;"><s:text name="colorantLevels.color"/></th>
							        <th class="bg-light text-center" id="oz" style="width: 10%;"><s:property value="incrHdr[0]"/></th>
							        <th class="bg-light text-center" id="three-two" style="width: 10%;"><s:property value="incrHdr[1]"/></th>
							        <th class="bg-light text-center" id="six-four" style="width: 10%;"><s:property value="incrHdr[2]"/></th>
							        <th class="bg-light text-center" id="one-two-eight" style="width: 10%;"><s:property value="incrHdr[3]"/></th>
							      </tr>
							    </thead>
							    <tbody>
							    <s:iterator value="sortedCanList" status="i">
							    	<s:if test="%{clrntCode != 'NA'}">
								      <tr id="<s:property value="%{#i.index}"/>">
								        <td id="col1row<s:property value="%{#i.index}"/>">
								        	<div class="col-xl-3 colorcol" style="max-width: 0%;">
											 	<div class="chip m-2 mr-3" style="background: <s:property value="rgbHex"/>;"></div>
										        <strong id="code"><s:property value="clrntCode"/></strong>
										        <p><s:property value="clrntName"/></p>
											</div>
											<div class="col-xl-2" style="padding-left: 0px;">
												<div id="warningdiv" style="margin-top: 5px;"><span class="badge badge-warning d-none" id="warning<s:property value="clrntCode"/>"><s:text name="global.warningClrntLow"/></span></div>
												<strong id="pos" class="d-none"><s:property value="position"/></strong>
											</div>
											<div class="col-xl-9">
											</div>
								        </td>
								        <td id="col2row<s:property value="%{#i.index}"/>"><s:textfield name="oz%{#i.index}" style="text-align:center" class="myinputs" type="number" min="0" max="99"/></td>
								        <td id="col3row<s:property value="%{#i.index}"/>"><s:textfield name="32_%{#i.index}" style="text-align:center" class="myinputs" type="number" min="0" max="99"/></td>
								        <td id="col4row<s:property value="%{#i.index}"/>"><s:textfield name="64_%{#i.index}" style="text-align:center" class="myinputs" type="number" min="0" max="99"/></td>
							        	<td id="col5row<s:property value="%{#i.index}"/>"><s:textfield name="128_%{#i.index}" style="text-align:center" class="myinputs" type="number" min="0" max="99"/></td>
								      </tr>
							     	</s:if>
							     </s:iterator>
							    </tbody>
							  </table>
							<br>
						</div>
						<div class="col-xl-1 col-lg-0 col-md-0 col-sm-0">
						</div>
					</div>
				</div>
				<br>
				<br>
				<br>
				<br>
				
				<!-- Tinter In Progress Modal Window -->
				    <div class="modal fade" aria-labelledby="tinterInProgressModal" aria-hidden="true"  id="tinterInProgressModal" role="dialog" data-backdrop="static" data-keyboard="false">
				    	<div class="modal-dialog" role="document">
							<div class="modal-content">
								<div class="modal-header">
									<i id="spinner" class="fa fa-refresh mr-3 mt-1 text-muted" style="font-size: 1.5rem;"></i>
									<h5 class="modal-title" id="tinterInProgressTitle"><s:text name="global.dispenseInProgress"/></h5>
									<button type="button" class="close" data-dismiss="modal" aria-label="Close" ><span aria-hidden="true">&times;</span></button>
								</div>
								<div class="modal-body">
									<p id="dispenseStatus" font-size="4"></p>
									<p id="tinterInProgressMessage" font-size="4"></p>
									<p id="abort-message" font-size="4" style="display:none;color:purple;font-weight:bold"></p>
									<ul class="list-unstyled" id="tinterProgressList"></ul> 
								
									<div class="progress-wrapper "></div>
					        	</div>
								<div class="modal-footer">
									<button id="progressok" type="button" class="btn btn-primary d-none" data-dismiss="modal" aria-label="Close" ><s:text name="global.ok"/></button>
								</div>
							</div>
						</div>
					</div> 
				
				<!-- Tinter Error List Modal Window -->
				    <div class="modal fade" aria-labelledby="tinterErrorListModal" aria-hidden="true"  id="tinterErrorListModal" role="dialog">
				    	<div class="modal-dialog">
							<div class="modal-content">
								<div class="modal-header">
									<h5 class="modal-title" id="tinterErrorListTitle"><s:text name="global.tinterError"/></h5>
									<button type="button" class="close" data-dismiss="modal" aria-label="Close" ><span aria-hidden="true">&times;</span></button>
								</div>
								<div class="modal-body">
									
										
									<div class="progress-wrapper "></div>
										
								
					
									<div>
										<ul class="p-0" id="tinterErrorList" style="list-style: none;">
										</ul>
									</div>
									<p id="tinterErrorListSummary" font-size="4"></p>
								</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-primary" id="tinterErrorListOK" data-dismiss="modal" aria-label="Close" ><s:text name="global.ok"/></button>
								</div>
							</div>
						</div> 
					</div>			    

				    <!-- Tinter Warning List Modal Window -->
				    <div class="modal fade" aria-labelledby="tinterWarningListModal" aria-hidden="true"  id="tinterWarningListModal" role="dialog">
				    	<div class="modal-dialog">
							<div class="modal-content">
								<div class="modal-header">
									<h5 class="modal-title" id="tinterWarningListTitle"><s:text name="global.tinterError"/></h5>
									<button type="button" class="close" data-dismiss="modal" aria-label="Close" ><span aria-hidden="true">&times;</span></button>
								</div>
								<div class="modal-body">
									<div>
										<ul class="p-0" id="tinterWarningList" style="list-style: none;">
										</ul>
									</div>
									<p id="tinterWarningListSummary" font-size="4"><s:text name="dispense.clickOK"/></p>
								</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-primary" id="tinterWarningListOK" data-dismiss="modal" aria-label="Close" ><s:text name="global.ok"/></button>
								</div>
							</div>
						</div>
					</div>
					
					 <!-- Dispense Error Modal Window -->
				    <div class="modal fade" aria-labelledby="tinterSocketErrorModal" aria-hidden="true"  id="tinterSocketErrorModal" role="dialog">
				    	<div class="modal-dialog">
							<div class="modal-content">
								<div class="modal-header">
									<h5 class="modal-title"><s:text name="global.dispenseError"/></h5>
									<button type="button" class="close" data-dismiss="modal" aria-label="Close" ><span aria-hidden="true">&times;</span></button>
								</div>
								<div class="modal-body">
									<p id="tinterSocketError" font-size="4"></p>
								</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-primary" id="tinterSocketErrorButton" data-dismiss="modal" aria-label="Close" ><s:text name="global.close"/></button>
								</div>
							</div>
						</div>
					</div>	
					<!-- dummy div to clone -->
					<div id="progress-0" class="progress" style="margin:10px;">
				        <div id="bar-0" class="progress-bar" role="progressbar" aria-valuenow="0"
								 aria-valuemin="0" aria-valuemax="100" style="width: 0%; background-color: blue">
								 <span></span>
				  		</div>
				  		<br/>
					</div>
	<!-- Including footer -->
	<s:include value="Footer.jsp"></s:include>
</body>
</html>