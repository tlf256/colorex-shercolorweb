<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!doctype html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Printer Config</title>
<!-- JQuery -->
<link rel=StyleSheet href="css/bootstrap.min.css" type="text/css">
<link rel=StyleSheet href="css/bootstrapxtra.css" type="text/css">
<link rel=StyleSheet href="js/smoothness/jquery-ui.css"type="text/css">
<link rel=StyleSheet href="css/CustomerSherColorWeb.css" type="text/css">
<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
<s:set var="thisGuid" value="reqGuid" />

<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
<script type="text/javascript" charset="utf-8"src="js/jquery-ui.js"></script>
<script type="text/javascript" charset="utf-8"	src="js/popper.min.js"></script>
<script type="text/javascript" charset="utf-8" src="js/bootstrap.min.js"></script>
<script type="text/javascript" charset="utf-8"	src="js/moment.min.js"></script>
<script type="text/javascript" charset="utf-8" src="script/CustomerSherColorWeb.js"></script>
<script type="text/javascript" charset="utf-8" src="script/WSWrapper.js"></script>
<script type="text/javascript" charset="utf-8" src="script/Printer.js"></script>
<script type="text/javascript">

function CreatePrinters(printerList){
	var s = $("#selectPrinterModel");
	 for(var i in printerList) {
		$('<option />', {value: printerList[i], text: printerList[i]}).appendTo(s);
	 }
}
function ConfigClick(){
	var model = $("#selectPrinterModel").val();
	var printOnDispense = $("#autoPrintCheck").prop('checked');
	var serial = "12345";
	var numLabels = $("#numLabels").val();
	config = new PrinterConfig(model,serial,printOnDispense,numLabels);
	setPrinterConfig(config);
}
function FillForm(printerConfig){

	modelBox = $('#selectPrinterModel');
	modelBox.val(printerConfig.model);
	//$('#selectPrinterModel option[value=' +printerConfig.model+']').prop('selected', true);
	
	$("#printerSerialNbr").val(printerConfig.serial);
	
	$("#autoPrintCheck").prop('checked',printerConfig.printOnDispense) ;
	$("#numLabels").val(printerConfig.numLabels);
	
}

function ParsePrinterMessage() {

	try {
		if (ws_printer != null && ws_printer.wsmsg != null){
			 
			var return_message = JSON.parse(ws_printer.wsmsg);

			switch (return_message.command) {
			case 'GetPrinters':
				if (return_message.errorNumber != 0) {
					// save a dispense (will bump the counter)
					$("#printerResponseModal").modal('show');
					$("#printerResponseMessage").text(
							"Get Printer Result: " + return_message.errorMessage);
					console.log(return_message);
				}
				else{
					CreatePrinters(return_message.printerList);
					}
				break;
			case 'SetConfig':
				if (return_message.errorNumber != 0) {
					// save a dispense (will bump the counter)
					$("#printerResponseModal").modal('show');
					$("#printerResponseMessage").text(
							"Set Printer Config Result: " + return_message.errorMessage);
					console.log(return_message);
					//waitForShowAndHide("#tinterInProgressModal");
				}
				
				break;
			case 'GetConfig':
				if (return_message.errorNumber != 0) {
					// save a dispense (will bump the counter)
					$("#printerResponseModal").modal('show');
					$("#printerResponseMessage").text(
							"Get Printer Result: " + return_message.errorMessage);
					console.log(return_message);
				}
				else{
					CreatePrinters(return_message.printerList);
					FillForm(return_message.printerConfig);
					}
				break;
			default:
				//Not an response we expected...
				console
						.log("Message from different command is junk, throw it out");
			} // end switch statement
		} else {
			$("#printerResponseModal").modal('show'); //switch to pdf popup as before

			/* DJM switch to this if 
			if(ws_printer && ws_printer.wserrormsg!=null && ws_printer.wserrormsg != ""){
			$("#printerInProgressMessage").text(
					ws_printer.wserrormsg);
			}
			else {
				$("#printerInProgressMessage").text(
						"Unknown error communicating with SWDeviceHandler");
			}
			 */
		}

	} catch (error) {
		console.log("Caught error is = " + error);
		console.log("Message is junk, throw it out");
		//console.log("Junk Message is " + ws_tinter.wsmsg);
	}
}
function RecdMessage() {
	console.log("Received Message");
	//parse the spectro

	if (ws_printer) {
		ParsePrinterMessage();
	}
}
$(document).ready(function() {
	
		
		getPrinterConfig();
	
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
						</div>
					</div>
					<br>
					<div class="row">
						<div class="col-xl-3 col-lg-2 col-md-0 col-sm-0">
						</div>
						<div class="col-xl-6 col-lg-10 col-md-12 col-sm-12">
							<div class="card card-body bg-light mb-3">
									<div class="d-flex flex-row justify-content-around">
									<div class="p-2 mr-3" style="width: 10rem;">
										<span class="badge badge-secondary" style="font-size: 1.2rem;">Printer Config</span>
										<h5 class="text-primary mt-3"><strong><s:property value="tinter.model"/></strong></h5>
									</div>
									<div class="p-2">
										
									</div>
									<div class="p-2 ml-3">
										<s:url var="dispenseURL" action="printerSaveAction"><s:param name="reqGuid" value="%{thisGuid}"/></s:url>
										<button class="btn btn-primary mr-4" id="dispense">Configure</button>
									</div>
									<div class="p-2">
										<s:url var="cancelURL" action="userCancelAction"><s:param name="reqGuid" value="%{thisGuid}"/></s:url>
										<a href='<s:property value="cancelURL"/>' class="btn btn-secondary" id="cancel">Done</a>
									</div>
									
								</div>
							</div>	
							
							<br>
						</div>
						<div class="col-xl-3 col-lg-0 col-md-0 col-sm-0">
						</div>
					</div>
				</div>
				
				<br>
				<div class="container center-form-parent">



		<s:set var="thisGuid" value="reqGuid" />

		<s:form class="form-sw-centered" id="frmSubmit" action="printerSaveAction"
			validate="true" theme="bootstrap">
			<div class="text-center mb-4">

				<h1 class="h3 mb-3 font-weight-normal">Configure Printer</h1>
				
			</div>

			<div class="form-label-group">

				<label class="sw-label" for="selectPrinterModel">Printer Model</label>
				<select id="selectPrinterModel" name="printerModel" >
					<option value="-1">Select Printer</option>
					<option value="Zebra GK420d">Zebra GK420d</option>
				 </select>


			</div>
			
			<div class="form-label-group">
				<label class="sw-label" for="tSerialNbr">Serial Number</label>
				<s:textfield id="printerSerialNbr" name="printerSerialNbr"></s:textfield>

				<p style="color: red; font-weight: bold" id="SNValidationError"></p>

				<br>

				<s:actionerror />
			</div>
			

		<div class="form-label-group">

				<label class="sw-label" for="autoPrintCheck">Auto Print on Dispense</label>
				<input type="checkbox" id="autoPrintCheck" name="autoPrintCheck" >
				<label class="sw-label" for="selectPrinterModel">Number of Labels</label>
				<select id="numLabels" name="numLabels" >
					<option value="1">1</option>
					<option value="2">2</option>
					<option value="3">3</option>
					<option value="4">4</option>
					<option value="5">5</option>
					
				 </select>

		</div>
			
			<div class="form-row">

				<input type="button" class="btn btn-lg btn-primary btn-block"
					id="btn_printerConfig" data-toggle="modal" onclick="ConfigClick()"
					 value="Configure" />

				<s:submit cssClass="btn btn-lg btn-secondary btn-block"
					value="Cancel" action="userCancelAction" />


			</div>
			<div class="form-row">
					<!-- save default colorantList for submission in case we need to come back here due to error -->

					<s:hidden name="reqGuid" />
				
				</div>
			<div id="hidden_modellist" class="col-md-2"></div>
		</s:form>
	</div>
				<!-- Printer Response Modal Window -->
			<div class="modal" aria-labelledby="printerResponseModal"
				aria-hidden="true" id="printerResponseModal" role="dialog">
				<div class="modal-dialog" role="document">
					<div class="modal-content">
						<div class="modal-header">
							<!-- 	<i id="spinner" class="fa fa-refresh mr-3 mt-1 text-muted" style="font-size: 1.5rem;"></i> -->
							<h5 class="modal-title" id="printerResponseTitle">Label
								Printer Response</h5>
							<button type="button" class="close" data-dismiss="modal"
								aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
						</div>
						<div class="modal-body">
							<p id="printerResponseMessage" font-size="4"></p>
						</div>
						<div class="modal-footer"></div>
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