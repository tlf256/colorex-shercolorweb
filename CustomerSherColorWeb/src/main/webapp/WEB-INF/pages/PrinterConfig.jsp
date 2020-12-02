<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!doctype html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="printerConfig.printerConfig"/></title>
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
<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.4.5.js"></script>
<script type="text/javascript" charset="utf-8" src="script/WSWrapper.js"></script>
<script type="text/javascript" charset="utf-8" src="script/printer-1.4.6.js"></script>
<script type="text/javascript">

/*
 *  Create dropdown for Printers based on Printer Drivers installed on Windows
 */
function CreatePrinters(printerList){
	var s = $("#selectPrinterModel");
	 for(var i in printerList) {
		$('<option />', {value: printerList[i], text: printerList[i]}).appendTo(s);
	 }
}
/*
 *  Save Configuration to SWDeviceHandler and CustWebDevices
 */
function ConfigClick(){
	var model = $("#selectPrinterModel").val();
	var printOnDispense = $("#autoPrintCheck").prop('checked');
	var serial = "Default";
	var numLabels = $("#numLabels").val();
	config = new PrinterConfig(model,serial,printOnDispense,numLabels);
	setPrinterConfig(config);
	$("#frmSubmit").submit();
	
}
/*
 *fill form based on what was read from SWDeviceHandler
 */
function FillForm(printerConfig){

	modelBox = $('#selectPrinterModel');
	modelBox.val(printerConfig.model);
		
	//$("#printerSerialNbr").val(printerConfig.serial);
	
	$("#autoPrintCheck").prop('checked',printerConfig.printOnDispense) ;
	$("#numLabels").val(printerConfig.numLabels);
	
}

function ParsePrinterMessage() {

	try {
		if (ws_printer != null && ws_printer.wsmsg != null && ws_printer.wsmsg != ""){
			 
			var return_message = JSON.parse(ws_printer.wsmsg);

			switch (return_message.command) {
			case 'GetPrinters':
				if (return_message.errorNumber != 0) {
					// save a dispense (will bump the counter)
					$("#printerResponseModal").modal('show');
					$("#printerResponseMessage").text(
							"%{getText('printerConfig.getPrinterResultWithErrorMsg')}" + return_message.errorMessage);
					console.log(return_message);
				}
				else{
					CreatePrinters(return_message.printerList);
					}
				break;
			case 'GetConfig':
				if (return_message.errorNumber != 0) {
					// save a dispense (will bump the counter)
					$("#printerResponseModal").modal('show');
					$("#printerResponseMessage").text(
							"%{getText('printerConfig.getPrinterResultWithErrorMsg')}" + return_message.errorMessage);
					console.log(return_message);
				}
				else{
					CreatePrinters(return_message.printerList);
					FillForm(return_message.printerConfig);
					}
				break;
			case 'SetConfig':
				if (return_message.errorNumber != 0) {
					// save a dispense (will bump the counter)
					$("#printerResponseModal").modal('show');
					$("#printerResponseMessage").text(
							"%{getText('printerConfig.setPrinterResultWithErrorMsg')}" + return_message.errorMessage);
					console.log(return_message);
					//waitForShowAndHide("#tinterInProgressModal");
				}
				
				break;
			default:
				//Not an response we expected...
				console
						.log("Message from different command is junk, throw it out");
			} // end switch statement
		} else {
			$("#configError").text(ws_printer.wserrormsg);
			$("#configErrorModal").modal('show'); //switch to pdf popup as before
			
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
	else{
			$("#configError").text(ws_printer.wserrormsg);
			$("configErrorModal").modal('show');
		
		}
}
$(document).ready(function() {
	    if($("#siteHasPrinter").val() == "true"){
			getPrinterConfig();
		}
	    else{
	    	getPrinters();  //just get list of printers for combo box.
		    }	
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
						<div class="col-xl-3 col-lg-2 col-md-0 col-sm-0">
						</div>
						<div class="col-xl-6 col-lg-10 col-md-12 col-sm-12">
							<div class="card card-body bg-light mb-3">
									<div class="d-flex flex-row justify-content-around">


		<s:set var="thisGuid" value="reqGuid" />

		<s:form class="form-sw-centered" id="frmSubmit" action="printerSaveAction"
			validate="true" theme="bootstrap">
			<div class="text-center mb-4">

				<h1 class="h3 mb-3 font-weight-normal"><s:text name="printerConfig.configurePrinter"/></h1>
				
			</div>

			<div class="form-label-group">

				<label class="sw-label" for="selectPrinterModel"><s:text name="printerConfig.printerModel"/></label>
				<select id="selectPrinterModel" name="printerModel" >
					<option value="-1"><s:text name="printerConfig.selectPrinter"/></option>
				
				 </select>


			</div>
			
		<div class="form-label-group">
				<label class="sw-label" for="autoPrintCheck"><s:text name="printerConfig.autoPrintOnDispense"/></label>
				<input type="checkbox" id="autoPrintCheck" name="autoPrintCheck" >
				<label class="sw-label" for="selectPrinterModel"><s:text name="printerConfig.numberOfLabels"/></label>
				<select id="numLabels" name="numLabels" >
					<option value="1">1</option>
					<option value="2">2</option>
					<option value="3">3</option>
					<option value="4">4</option>
					<option value="5">5</option>
					<option value="6">6</option>
					<option value="7">7</option>
					<option value="8">8</option>
					<option value="9">9</option>
					<option value="10">10</option>
					
				 </select>
				
		</div>
			
			<div class="form-row">

				<s:submit class="btn btn-lg btn-primary btn-block"
					id="btn_printerConfig" data-toggle="modal" onclick="ConfigClick()"
					 value="%{getText('global.save')}" />

				<s:submit cssClass="btn btn-lg btn-secondary btn-block"
					value="%{getText('global.cancel')}" action="userCancelAction" />


			</div>
			<div class="form-row">
					<!-- save default colorantList for submission in case we need to come back here due to error -->

					<s:hidden name="reqGuid" />
				
				</div>
			<div id="hidden_modellist" class="col-md-2"></div>
			<s:hidden id="siteHasPrinter" name="siteHasPrinter"/>
		</s:form>
	</div>
	</div>
	</div>
	</div>
				<!-- Printer Response Modal Window -->
			<div class="modal" aria-labelledby="printerResponseModal"
				aria-hidden="true" id="printerResponseModal" role="dialog">
				<div class="modal-dialog" role="document">
					<div class="modal-content">
						<div class="modal-header">
							<!-- 	<i id="spinner" class="fa fa-refresh mr-3 mt-1 text-muted" style="font-size: 1.5rem;"></i> -->
							<h5 class="modal-title" id="printerResponseTitle">
							   <s:text name="printerConfig.labelPrinterResponse"/>
							</h5>
							<button type="button" class="close" data-dismiss="modal"
								aria-label="%{getText('global.close')}">
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
			<!-- Config Error Modal Window -->
	<div class="modal fade" aria-labelledby="configErrorModal"
		aria-hidden="true" id="configErrorModal" role="dialog">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title"><s:text name="global.configurationError"/></h5>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<p id="configError" ></p>
				</div>
				<div class="modal-footer">
					<s:url var="troubleshootURL" action="swdhTroubleshootAction"
						escapeAmp="false">
						<s:param name="reqGuid" value="%{thisGuid}" />
					</s:url>
					<s:url var="installURL" action="swdhInstallAction"
						escapeAmp="false">
						<s:param name="reqGuid" value="%{thisGuid}" />
					</s:url>
					<a href="<s:property value="troubleshootURL"/>"
						class="btn btn-primary"><s:text name="global.troubleshoot"/></a> <a
						href="<s:property value="installURL"/>" class="btn btn-success"><s:text name="global.install"/></a>
					<button type="button" class="btn btn-secondary"
						id="configErrorButton" data-dismiss="modal" aria-label="Close"><s:text name="global.close"/></button>
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