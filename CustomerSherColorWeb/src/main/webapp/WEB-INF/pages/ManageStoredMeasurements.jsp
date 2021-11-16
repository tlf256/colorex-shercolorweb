<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<!-- 6/24/2019 BXW: This page has been created to mimic the Update Remote Measurements spectro utility
							menu item on the Store/Customer PCs. -->		
		<title><s:text name="global.manageRemoteMeasurements"/></title>
			<!-- JQuery -->
		<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
		<link rel="stylesheet" href="css/bootstrapxtra.css" type="text/css">
		<link rel="stylesheet" href="js/smoothness/jquery-ui.min.css" type="text/css">
		<link rel="stylesheet" href="css/CustomerSherColorWeb.css" type="text/css"> 
		<link rel="stylesheet" href="css/font-awesome.css" type="text/css">
		<link rel="stylesheet" href="css/joblist_datatable.css" type="text/css">
		<link rel="stylesheet" href="css/CustomerSherColorWeb.css" type="text/css"> 
		<script type="text/javascript" src="https://use.fontawesome.com/releases/v5.3.1/js/all.js" data-auto-replace-svg="nest"></script>
		<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
 		<script type="text/javascript" charset="utf-8" src="js/jquery.dataTables.min-1.10.16.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/jquery-ui.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/bootstrap.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.4.6.js"></script>
		<script type="text/javascript" charset="utf-8"	src="script/WSWrapper.js"></script>
		<script type="text/javascript" charset="utf-8"	src="script/spectro.js"></script>
		<s:set var="thisGuid" value="reqGuid" />
		<script type="text/javascript" charset="utf-8"	src="js/moment.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/moment-with-locales.min.js"></script>
 		<script type="text/javascript" src="https://cdn.datatables.net/plug-ins/1.10.21/sorting/datetime-moment.js"></script>
		<script type="text/javascript" src="script/displaystoredmeasurements.js"></script>
		<style>
	        .sw-bg-main {
	            background-color: ${sessionScope[thisGuid].rgbHex};
	        }
	        
	        .modal-dialog-cu {
       			max-width: 90%;
        		max-height: 90%;
        
    		}
    		.modal-body-cu{
       			max-width: 100%;
        		max-height: 100%;
        		overflow: scroll; 
    		}
    		.chip {
		    width: 40px;
		    height: 40px;
		    border-radius: 50%;
		    border: .5px solid black;
		    float: left;
			}
			.no-display {
				display: none;
			}
	    </style>
	    
	    <script type="text/javascript">
	    var hexList = [];
	    var returnLoadMeasurements_message;
	    var measurementList;
	    var newMeasurementsList;
	    var deleteRow;
	    var editRow;
		var colorChip;
		var colorName;
		var rowDateTime;
		var description;
		var spectroModel;
		var rowSerialNbr;
		var rowMeasuredCurve;
		var rowRgbHex;
	    
	    </script>
	    
	    <script type="text/javascript" charset="utf-8">
	    
	    $(document).on({
			'keypress blur': function(){
					try{
						if(event.key == ">" || event.key == "<"){
							//console.log("< or > keypress");
							throw '<s:text name="global.noLtOrGt"/>';
						}
						if($(this).val().includes(">") || $(this).val().includes("<")){
							throw '<s:text name="global.invalidEntryLtGt"/>';
						}
						$(document).find('#errortxt').remove();
						$('input:submit').attr('disabled', false);
					} catch(msg){
						if(event.type=="keypress"){
							event.preventDefault();
						}
						if(!$(document).find('#errortxt').is(':visible')){
							$(this).parent().append('<div id="errortxt" class="text-danger mt-2"></div>');
						}
						$(document).find('#errortxt').text(msg);
						if(event.type=="blur"){
							$(this).focus();
							$('input:submit').attr('disabled', true);
						}
					}
				}
		}, '.validateEntry');
  
	    var ws_coloreye = new WSWrapper('coloreye');
	  	
	    function DisplayMsg(status,msg) {
	    	if (status == 1) {
	    		$('#successmsg').text(msg);
	    		$('#successmsg').removeClass('d-none');
		    	$('#errmsg').addClass('d-none');
	    	} else if (status == 2) {
	    		$('#errmsg').text(msg);
	    		$('#errmsg').removeClass('d-none');
		    	$('#successmsg').addClass('d-none');
	    	} else {
	    		$('#errmsg').addClass('d-none');
		    	$('#successmsg').addClass('d-none');
	    	}
	    }
	    
	    function RetrieveAllStoredMeasurements() {
			var clreyemodel = $('#spectroModel').val();
		 	var spectromessage = new SpectroMessage('RetrieveAllStoredMeasurements',clreyemodel);
		    var json = JSON.stringify(spectromessage);
		    ws_coloreye.send(json);
		}
		  	
	 	function RecdMessage() {
			//console.log("Received Message");
		  	//parse the spectro
		  	//console.log("Message is " + ws_coloreye.wsmsg);
		  	//console.log("isReady is " + ws_coloreye.isReady + " BTW");
		  	var return_message=JSON.parse(ws_coloreye.wsmsg);
		  	switch (return_message.command) {
		  	case 'RetrieveAllStoredMeasurements':
		  		if (return_message.errorMessage!="") {
		  			$('#notConnectedWarning').removeClass('d-none');
		  			$('#loadMeasurementsButton').prop("title",'<s:text name="manageStoredMeasurements.ci62NotDetected2"/>');
		  		} else {
		  			myGuid = "${reqGuid}";
		  			measurementsJSON = return_message.storedMeasurements;
		  			measurementList = measurementsJSON;
		  			returnLoadMeasurements_message = return_message;
		  			//console.log("measurementsJSON @ RETRIEVEALLSTOREDMEASUREMENTS RESPONSE: " + measurementsJSON);
		  			
		  			// TODO : Wire AJAX to a new action that specifically changes the json to include the rgbHex
		  			// values so that the dialog can utilize the Color Chip column as well
		  			var jsonIN = JSON.stringify(returnLoadMeasurements_message);		  			
		  			$.ajax({
		  				url: "spectroObtainAllRGBColorsAction.action",
		  				type: "POST",
		  				data: {
		  					reqGuid : myGuid,
		  					measurementJson : jsonIN
		  				},
		  				dataType: "json",
		  				async: true,
		  				success: function (data) {
		  					if(data.sessionStatus === "expired"){
		  	               		window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
		  	            	} else{		  	                	
		  	      				hexList = data.hexList;		  	                	
		  	                	$('#loadMeasurementsButton').prop('disabled',false);
		  			  			$('#loadMeasurementsButton').prop("title",'<s:text name="manageStoredMeasurements.loadNewMeasurements"/>');

		  	            	}
		  				},
		  				error: function(err){
		  					alert('<s:text name="global.failurePlusErr"><s:param>'+ JSON.stringify(err) +'</s:param></s:text>');
		  					console.log(JSON.stringify(err));
		  				}
		  			});
		  			}
		  			break;
		  		default:
		  		//Not an response we expected...
		  		  	DisplayMsg(2,'<s:text name="global.unexpectedCallToErr"><s:param>'+ return_message.command +'</s:param></s:text>');
		  	}
		  	  	
		}
	 	
	 	function buildMeasurementLoadTable(rgbList,measurementsJson) {
	 		var langUrl = null;
	 		var dateFormat = null;
	 		// translate datatable text like the search bar, and assign format pattern to check for dupes 
	 		switch("${session['WW_TRANS_I18N_LOCALE']}"){
				case("es_ES"):
					langUrl = "//cdn.datatables.net/plug-ins/1.10.21/i18n/Spanish.json";
					dateFormat = "DD-MMM-YYYY H:mm:ss";
					break;
				case("zh_CN"):
					langUrl = "//cdn.datatables.net/plug-ins/1.10.21/i18n/Chinese.json";
					dateFormat = "YYYY-M-D H:mm:ss";
					break;
				default:
					dateFormat = "MMM D, YYYY h:mm:ss A";					
			}
			
	 		var measurementTable =  $('#measurement_table').DataTable();
			var dates = [];
			for (index = 0; index < measurementTable.data().count()/9; index++){
				dates.push(measurementTable.row(index).data()[2]);
			}
  			dateList = dates;
			var isDupe = false;
		  	new_measurements_table = $('#new_spectro_measurements_table')
		  	.DataTable(
		  		{
		  			"emptyTable" : '<s:text name="displayStoredMeasurements.noMeasurementsAvailable"/>',
		  			"scrollY" : '50vh',
		  			scrollCollapse : true,
		  			data : measurementsJson,
		  			dom: 'ifBrtp',
		  			"pagingType": "full",
		  			"scrollX": true,
		  			"paginate": false,
		  			"order": [ 3, "desc" ],
		  			"ordering": true,
		  			"language": {
		  	        	"url" : langUrl
		  	        },
		  			columns : [
		  				{
		  					//data: 'addMeasurement'
		  					"render" : function(
		  						data, type, row,meta) {
		  						isDupe = false;
		  						// To compare dates, we need to parse the spanish dates with the locale method added. 
		  						// Default is english, and chinese date uses only numbers, so don't need locale call for those 
		  						if ("${session['WW_TRANS_I18N_LOCALE']}" == "es_ES"){
		  							var dateTime = moment(row.sampleDateTime, "MM/DD/YY H:mm:ss").locale('es').format(dateFormat);
		  						} else {
			  						var dateTime = moment(row.sampleDateTime, "MM/DD/YY H:mm:ss").format(dateFormat);
		  						}
		  						for (index = 0; index < dateList.length; index++) {
		  							if(dateList[index] == dateTime) {
			  							dateList.splice(index,1);
			  							isDupe = true;
			  							break;
			  						} 
		  						}
		  						if (isDupe == true) {
		  							return '<input class="addMeasurement" id="addMeasurement" type="checkbox" name="addMeasurement" disabled>';
		  						} else {
		  							return '<input class="addMeasurement" id="addMeasurement" checked="checked" type="checkbox" name="addMeasurement" value=' + row.addMeasurement +'>';
		  						}
		  						
		  					}
		  				},
		  				{
		  					//rgbList Chip
		  					"render" : function(
		  							data, type, row, meta) {
		  							return '<span class="chip p-0" style="background:' + rgbList[meta.row] + '"></span>';
		  					}
		  				},
		  				{
		  					//data: 'sampleName'
		  					"render" : function(
		  							data, type, row,meta) {
		  						if (isDupe == true) {
		  							return '<input type="text" class="sampleName" name="sampleName" style="width:95%" value="<s:text name="manageStoredMeasurements.duplicate"/>" disabled>';
		  						} else if (row.sampleName === '<00>') {
		  							return '<input type="text" class="sampleName validateEntry" name="sampleName" style="width:95%" value="" maxlength="30">'
		  						} else {
		  							return '<input type="text" class="sampleName validateEntry" name="sampleName" style="width:95%" value="' + row.sampleName + '" maxlength="30">';
		  						}
		  							
		  					}
		  				},
		  				{
		  					//data: 'sampleDateTime'
		  					"render" : function(
	  							data, type, row,meta) {
		  						var dateTime = null;
		  						// update date formatting so it matches how struts displays datetime for these locales 
		  						switch("${session['WW_TRANS_I18N_LOCALE']}"){
			  						case("es_ES"):
			  							dateTime = moment(row.sampleDateTime, "MM/DD/YY H:mm:ss").locale('es').format('DD-MMM-YYYY H:mm:ss');
			  							break;
			  						case("zh_CN"):
			  							dateTime = moment(row.sampleDateTime, "MM/DD/YY H:mm:ss").format('YYYY-M-D H:mm:ss');
			  							break;
			  						default:
			  							dateTime = moment(row.sampleDateTime, "MM/DD/YY H:mm:ss").format('MMM D, YYYY h:mm:ss A');
			  					}
	  							return dateTime;
		  					}
		  				},
		  				{
		  					//data: 'sampleDescr'
		  					"render" : function(
		  							data, type, row,meta) {
		  						if (isDupe == true) {
		  							return '<input type="text" class="sampleDescr" name="sampleDescr" style="width:95%" value="<s:text name="manageStoredMeasurements.duplicateOfExistingEntry"/>" disabled>';
		  						} else {
		  							return '<input type="text" class="sampleDescr validateEntry" name="sampleDescr" style="width:95%" value="" maxlength="50">';
		  						}
		  						
		  					}
		  				},
		  				{
		  					data: 'spectroModel'
		  				},
		  				{
		  					data: 'spectroSerialNbr'
		  				},
		  				{
		  					className: "no-display",
		  					"render" : function(
		  							data, type, row,meta) {
		  						return '<input type="hidden" class="rowId" name="rowId" style="display:none" value="' + meta.row + '">';
		  					}
		  				}
		  			]
		  		});
	 		
	 	}
	 	
	 	
		  
	 	function SaveNewMeasurementsCallback(newMeasurements) {
		  	var savedColors = "";
		  	var measurements_table = $('#measurement_table').DataTable();
		  	for (i = 0; i < newMeasurements.length; i++){
		  		var measuredCurve = newMeasurements[i].measuredCurve;
			  	var sampleCurve = measuredCurve.toString();

			  	if (i < (newMeasurements.length - 1)){
			  		savedColors += newMeasurements[i].sampleName + ", ";
			  	} else {
			  		savedColors += newMeasurements[i].sampleName;
			  	}
			  	// format date for locale so it translates and sorts properly 
			  	var datetime = null;
			  	switch("${session['WW_TRANS_I18N_LOCALE']}"){
					case("es_ES"):
						datetime = moment(newMeasurements[i].sampleDateTime).locale('es').format('DD-MMM-YYYY H:mm:ss');
						break;
					case("zh_CN"):
						datetime = moment(newMeasurements[i].sampleDateTime).format('YYYY-M-D H:mm:ss');
						break;
					default:
						datetime = moment(newMeasurements[i].sampleDateTime).format('MMM D, YYYY h:mm:ss A');
				}
		  		measurements_table.row.add([
		  			'<span class="chip p-0" style="background:' + newMeasurements[i].RGBHex + '"></span>',
		  			newMeasurements[i].sampleName,
		  			datetime,
		  			newMeasurements[i].sampleDescr,
		  			newMeasurements[i].model,
		  			newMeasurements[i].serialNbr,
		  			'<button type="button" id="deleterow" class="btn btn-danger dltrow" title="%{getText(\'manageStoredMeasurements.deleteStoredMeasurement\')}"><i class="far fa-trash-alt"></i></button>',
		  			sampleCurve,
		  			newMeasurements[i].RGBHex
		  		]).draw();
		  	}
		  	DisplayMsg(1,'<s:text name="manageStoredMeasurements.successfulSave"><s:param>'+ savedColors +'</s:param></s:text>');
		}
	 	
		
	 	
	 	$(document).ready(function() {
	 		// need to grab locale out of the session in jsp before building datatable in displaystoredmeasurements.js
	 		var userLocale = "${session['WW_TRANS_I18N_LOCALE']}";
	 		displayStoredMeasurementsTable(userLocale);
	 		
	 		// Retrieve all the Ci62 Measurements
	 		RetrieveAllStoredMeasurements();
	 		// Draw all tables
			setTimeout(function () {
		  	      $($.fn.dataTable.tables( true ) ).DataTable().columns.adjust().draw();
		  	 },200);
	 		
			$(document).on("click", "#loadMeasurementsButton", function(event){
				
				buildMeasurementLoadTable(hexList,measurementsJSON);
		 		$('#addNewMeasurementsModal').modal('show');
			  	
				setTimeout(function () {
					$($.fn.dataTable.tables( true ) ).DataTable().columns.adjust().draw();
				},200);
		 		
		 	});
		 	
		 	$(document).on("click", "#saveNewMeasurements", function(event){
		  		
				var inputCheckbox = $('#new_spectro_measurements_table :input[type="checkbox"]').map(function(){return $(this).prop('checked');}).get();
			  	var inputTextFields = $('#new_spectro_measurements_table :text').map(function(){return $(this).val();}).get();
			  	var rowIds = $('#new_spectro_measurements_table :input[type="hidden"]').map(function(){return $(this).val();}).get();
			  	var textFieldIndex = 0;
			  	for (i = 0; i < measurementList.length; i++){
			  		measurementList[rowIds[i]].addMeasurement = inputCheckbox[i];
			  		measurementList[rowIds[i]].sampleName = inputTextFields[textFieldIndex];
			  		measurementList[rowIds[i]].sampleDescr = inputTextFields[textFieldIndex + 1];
			  		textFieldIndex = textFieldIndex + 2;
			  	}
			  	
			  	//console.log("measurementsJSON object after editting new measurements: " + JSON.stringify(measurementsJSON));
			  		$('#new_spectro_measurements_table').DataTable().destroy();
			  		$('#addNewMeasurementsModal').modal('hide');
		
			  		returnLoadMeasurements_message.storedMeasurements = measurementsJSON;
			  		var jsonIN = JSON.stringify(returnLoadMeasurements_message);
			  		$.ajax({
			  		url: "spectroSaveNewMeasurementsAction.action",
			  		type: "POST",
			  		data: {	
			  			reqGuid : "${reqGuid}",
			  			measurementJson : jsonIN,
			  			hexList : JSON.stringify(hexList)
			  		},
			  		datatype : "json",
			  		async: true,
			  		success: function (data) {
			  			if(data.sessionStatus === "expired"){
			          		window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
			         	}
			          	else{
			          		
			  				DisplayMsg(0,"");
			          		SaveNewMeasurementsCallback(data.newMeasurements);
			         		}
			  		},
			  		error: function(err){
			  			DisplayMsg(2,'<s:text name="manageStoredMeasurements.unableToSaveMeas"/>')
			  		}
			  	});
			  		
			});
	 		
			$(document).on("click", "#cancelLoadMeasurements", function(event){
		  		
		      	$('#new_spectro_measurements_table').DataTable().destroy();
		  		$('#addNewMeasurementsModal').modal('hide');
		  		DisplayMsg(0,"");
		  		
			});
		
			$('#measurement_table tbody').on('click', '.dltrow', function(event){
			
	    		deleteRow = $(this).closest('tr');
	    		//console.log("Table Row Clicked!!!");
	    		colorName = measurementTable.row(deleteRow).data()[1];   		
	    		
	    		if ("${session['WW_TRANS_I18N_LOCALE']}" == "es_ES"){
					// parse in spanish and then reformat in english
					rowDateTime = moment(measurementTable.row(deleteRow).data()[2], 'DD-MMM-YYYY H:mm:ss', 'es').locale('en').format('DD/MMM/YY hh:mm:ss A');
				} else {
					// default is english, and the chinese date is all numbers so don't need to change the date's locale
					rowDateTime = moment(measurementTable.row(deleteRow).data()[2]).locale('en').format('DD/MMM/YY hh:mm:ss A');
				}
	    		rowSerialNbr = measurementTable.row(deleteRow).data()[5];
	    		//console.log("The row data used to delete the measurement is: " + rowDateTime + ", " + rowSerialNbr);
	    		$('#deletemodal').modal().show();
	    		event.stopPropagation();
	    	});
		
			$('#confirmDelete').on('click', function(){
				$.ajax({
					url:"deleteSpectroRemoteAction.action",
					data: {
						reqGuid : "${reqGuid}",
						measurementDate : rowDateTime,
						measurementSerialNbr : rowSerialNbr
					},
					type: "POST",
					datatype : "json",
		  			async: true,
					success:function(data){
						DisplayMsg(1,'<s:text name="manageStoredMeasurements.successfulDelete"><s:param>'+ colorName +'</s:param></s:text>');
					
					},
					error:function(err){
						DisplayMsg(2,'<s:text name="manageStoredMeasurements.unableToDeleteColorName"><s:param>'+ colorName +'</s:param></s:text>');
					}
					
				});
				measurementTable.row(deleteRow).remove().draw();
				
			
			});
			
			$('#measurement_table tbody').on('click', 'tr', function(event){
				editRow = $(this).closest('tr');
		    	var chip = $(this).closest('span');
		    	var chipInfo = chip.css('background');
		    	colorChip = editRow.closest('.chip').css("background");
		    	colorName = measurementTable.row(editRow).data()[1];
		    	rowDateTime = measurementTable.row(editRow).data()[2];
		    	description = measurementTable.row(editRow).data()[3];
		    	spectroModel = measurementTable.row(editRow).data()[4];
		    	rowSerialNbr = measurementTable.row(editRow).data()[5];
		    	rowMeasuredCurve = measurementTable.row(editRow).data()[7];
		    	/* for spanish locale, struts formats 0.00, 1.00 into 0,00, 1,00
		    	so change it back to default format before sending to action  
		    	(but if record was added from the modal it didn't pass through struts
		    	formatting and looks like 0.00,1.00) */
		    	if ("${session['WW_TRANS_I18N_LOCALE']}" == "es_ES" && !rowMeasuredCurve.includes(".")){
		    		console.log(rowMeasuredCurve);
		    		rowMeasuredCurve = rowMeasuredCurve.replace(/,/g, ".");
			    	rowMeasuredCurve = rowMeasuredCurve.replace(/. /g, ", ");
			    	console.log(rowMeasuredCurve);
		    	}
		    	rowRgbHex = measurementTable.row(editRow).data()[8];
		    	$("#editSampleName").val(colorName);
		    	$("#editSampleDescription").val(description);
		    	$("#spectroDateInfo").text(rowDateTime);
		    	$("#spectroModelInfo").text(spectroModel);
		    	$("#spectroSerialNumber").text(rowSerialNbr);
		    	$('#spectroColorChip').css("backgroundColor",rowRgbHex);
				$('#editmodal').modal().show();
		    	event.stopPropagation();
			});
			
			$('#confirmEdit').on('click', function(){
				colorName = $("#editSampleName").val();
				description = $("#editSampleDescription").val();
				
				if ("${session['WW_TRANS_I18N_LOCALE']}" == "es_ES"){
					// parse in spanish and then reformat in english
					rowDateTime = moment(measurementTable.row(editRow).data()[2], 'DD-MMM-YYYY H:mm:ss', 'es').locale('en').format('DD/MMM/YY hh:mm:ss A');
				} else {
					// default is english, and the chinese date is all numbers so don't need to change the date's locale
					rowDateTime = moment(measurementTable.row(editRow).data()[2]).locale('en').format('DD/MMM/YY hh:mm:ss A');
				}
	    		
				$.ajax({
					url:"updateSpectroRemoteAction.action",
					data: {
						reqGuid : "${reqGuid}",
						measurementDate : rowDateTime,
						measurementSerialNbr : rowSerialNbr,
						measurementModel : spectroModel,
						measurementName : colorName,
						measurementDescription : description,
						measurementMeasuredCurve : rowMeasuredCurve,
						measurementRgbHex : rowRgbHex,
					},
					type: "POST",
					dataType:"json",
		  			async: true,
					success:function(data){
						var updatedRow = measurementTable.row(editRow).data();
						updatedRow[1] = colorName;
						updatedRow[3] = description;
						$("#measurement_table").dataTable().fnUpdate(updatedRow,editRow,undefined,false);
						DisplayMsg(1,'<s:text name="manageStoredMeasurements.successfulUpdate"><s:param>'+ colorName +'</s:param></s:text>');
					},
					error:function(err){
						DisplayMsg(2,'<s:text name="manageStoredMeasurements.unableToUpdateColorName"><s:param>'+ colorName +'</s:param></s:text>');
					}
					
				});
				
				
				
			});
		 	
	 	});
	    
	    </script>
	    
	</head>
	<body>
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
			<div class="col-sm-1"></div>
			<div class="col-sm-10 mb-2	">
				<h1><s:text name="global.manageRemoteMeasurements"/></h1>
			</div>
			<div class="col-sm-1"></div>
			</div>
			<div class="row">
				<div class="col-sm-1"></div>
				<div class="col-sm-10">
					<h6 id="successmsg" class="text-success d-none"></h6>
					<h6 id="errmsg" class="text-danger d-none"></h6>
					<div id="notConnectedWarning" class="alert-warning p-2 d-none"><s:text name="manageStoredMeasurements.ci62NotDetected1"/></div>
<!--  ------------- Spectro Measurement Table ---------------------------------------------------------------------------------- -->
					<table id="measurement_table" class="table table-striped table-bordered" style="width:100%">
						<thead>
							<tr>
								<th></th>								
								<th><s:text name="global.colorName"/></th>
								<th><s:text name="global.dateTime"/></th>
								<th><s:text name="global.description"/></th>
								<th><s:text name="manageStoredMeasurements.spectroModel"/></th>
								<th><s:text name="manageStoredMeasurements.spectroSerialNumber"/></th>
								<th><s:text name="global.delete"/></th>
								<th style="display:none"></th>
								<th style="display:none"></th>
							</tr>
						</thead>
						<tbody>
							<s:iterator var="measurement" value="measurementTable" status="outer">
								<tr class="border-bottom-1 border-dark">
									<td><span class="chip p-0" style="background: <s:property value="#measurement.rgbHex"/>;"></span></td>
									<td><s:property value="#measurement.sampleName" /></td>
									<td ><s:date name="#measurement.sampleDateTime" /></td> 
									<td><s:property value="#measurement.sampleDescr" /></td>
									<td><s:property value="#measurement.model" /></td>
									<td><s:property value="#measurement.serialNbr" /></td>
									<td>
										<button type="button" id="deleterow" class="btn btn-danger dltrow" title="%{getText('manageStoredMeasurements.deleteRemoteMeasurements')}">
											<i class="far fa-trash-alt"></i>
										</button>
									</td>
									<td style="display:none"><s:property value="#measurement.measuredCurve" /></td>
									<td style="display:none"><s:property value="#measurement.rgbHex"/></td>
								</tr>
							</s:iterator>
						</tbody>
					</table>
				</div>
				<div class="col-sm-1"></div>
			</div>
			
			<s:form id="mainForm" action="spectroManageStoredMeasurementsAction" validate="true"  theme="bootstrap">
			<div class="form-row">
				<s:hidden name="reqGuid" value="%{reqGuid}" />
			</div>
			
			
			<div class="row">
				<div class="col-sm-1"></div>
				<div class="col-6"></div>
				<div class="col-2">
					<button type="button" id="loadMeasurementsButton" class="btn btn-secondary mb-5 mt-2 mx-1" title="%{getText('manageStoredMeasurements.currentLoadingRemoteMeas')}"><s:text name="manageStoredMeasurements.loadMeasurements"/></button>
					<script>$('#loadMeasurementsButton').prop('disabled',true);</script>
				</div>
				<div class= "col-2">
					<s:submit cssClass="btn btn-secondary mb-5 mt-2 mx-1" value="%{getText('global.cancel')}" action="userCancelAction"/>
				</div>
				<div class="col-sm-1"></div>
			</div>
			</s:form>
		</div>
		
<!-- -- Modal for loading new spectro samples ------------------------------------------------------------------------------------------------------- -->	
		<div class="row">
			<div class="modal fade" aria-labelledby="addNewMeasurementsModal" aria-hidden="true"  id="addNewMeasurementsModal" role="dialog">
				<div class="modal-dialog modal-dialog-cu" role="document">
					<div class="modal-content">
						<div class="modal-header">
							<h5 class="modal-title"><s:text name="manageStoredMeasurements.loadNewMeasurements"/></h5>
							<button type="button" class="close" data-dismiss="modal" aria-label="Close" ><span aria-hidden="true">&times;</span></button>
						</div>
						<div class="modal-body modal-body-cu">
							<table id="new_spectro_measurements_table" class="table table-striped table-bordered" style="width: 100%">
								<thead>
									<tr>
										<th><s:text name="manageStoredMeasurements.add"/></th>
										<th></th>
										<th><s:text name="global.colorName"/></th>
										<th><s:text name="global.dateTime"/></th>
										<th><s:text name="global.description"/></th>
										<th><s:text name="manageStoredMeasurements.spectroModel"/></th>
										<th><s:text name="manageStoredMeasurements.spectroSerialNumber"/></th>
										<th style="display:none">
									</tr>
								</thead>
								<tbody></tbody>
								<tfoot></tfoot>
							</table> 
						</div>
						<div class="modal-footer">
							<button type="button" id="saveNewMeasurements" class="btn btn-primary mb-5 mt-2 mx-1"><s:text name="global.save"/></button>
							<button type="button" id="cancelLoadMeasurements" class="btn btn-secondary mb-5 mt-2 mx-1"><s:text name="global.cancel"/></button>
						</div>
					</div>
				</div>
			</div>				
		</div>
		
<!-- -- Confirmation Modal for spectro sample deletion -------------------------------------------------- -->
		<div class="row">
		<div class="modal fade" tabindex="-1" role="dialog" id="deletemodal">
		  <div class="modal-dialog" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title text-danger"><s:text name="manageStoredMeasurements.deleteRemoteMeasurement"/></h5>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      <div class="modal-body">
		        <h6><s:text name="manageStoredMeasurements.areYouSureMeasureDelete"/></h6>
		      </div>
		      <div class="modal-footer">
			  	<button type="button" id="confirmDelete" class="btn btn-danger" data-dismiss="modal"><s:text name="global.yes"/></button>
			  	<button type="button" id="cancelDelete" class="btn btn-secondary" data-dismiss="modal"><s:text name="global.no"/></button>
		      </div>
		    </div>
		  </div>
		</div>
		</div>
		
<!-- -- Edit modal for updating a spectro sample name and description - ------------------------------------------------------------------------------------------------------->
		<div class="row">
		<div class="modal fade" tabindex="-1" role="dialog" id="editmodal">
		  <div class="modal-dialog" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title"><s:text name="manageStoredMeasurements.editRemoteMeasurement"/></h5>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      <div class="modal-body" style="height: 50%">
		        <div class="row">
					<div class="col-6">
						<span style="font-weight: bold"><s:text name="global.colorNameColon"/></span>
						<input type="text" id="editSampleName" class="validateEntry" name="editSampleName" maxlength="30" style="width:95%" value="">
						<br><br>
						<span style="font-weight: bold"><s:text name="global.descriptionColon"/></span>
						<textarea type="text" id="editSampleDescription" class="validateEntry" name="editSampleDescription" maxlength="50" style="width:95%; height: 45%"></textarea>
					</div>
					<div class="col-6">
					<span style="font-weight:bold"><s:text name="global.dateTimeColon"/> </span><p id="spectroDateInfo"></p>
					<span style="font-weight:bold"><s:text name="global.modelColon"/> </span><p id="spectroModelInfo"></p>
					<span style="font-weight:bold"><s:text name="global.serialNumberColon"/> </span><p id="spectroSerialNumber"></p>
					<span class="chip p-0" id="spectroColorChip"></span>

					</div>		        
		        </div>
		      </div>
		      <div class="modal-footer">
		      <s:form>
			        <button type="button" id="confirmEdit" class="btn btn-primary" data-dismiss="modal"><s:text name="global.save"/></button>
			        <button type="button" id="cancelEdit" class="btn btn-secondary" data-dismiss="modal"><s:text name="global.cancel"/></button>
		       </s:form>
		      </div>
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