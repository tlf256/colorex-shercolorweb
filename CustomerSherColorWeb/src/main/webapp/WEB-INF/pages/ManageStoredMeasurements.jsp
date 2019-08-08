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
		<title>Manage Stored Measurements</title>
			<!-- JQuery -->
		<link rel=StyleSheet href="css/bootstrap.min.css" type="text/css">
		<link rel=StyleSheet href="css/bootstrapxtra.css" type="text/css">
		<link rel=StyleSheet href="js/smoothness/jquery-ui.css" type="text/css">
		<link rel=StyleSheet href="css/joblist_datatable.css" type="text/css">
		<link rel=StyleSheet href="css/CustomerSherColorWeb.css" type="text/css"> 
		<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
		<script type="text/javascript" src="https://use.fontawesome.com/releases/v5.3.1/js/all.js" data-auto-replace-svg="nest"></script>
		<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="js/jquery.dataTables.min-1.10.16.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/jquery-ui.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/bootstrap.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/CustomerSherColorWeb.js"></script>
		<script type="text/javascript" charset="utf-8"	src="script/WSWrapper.js"></script>
		<script type="text/javascript" charset="utf-8"	src="script/spectro.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/moment.min.js"></script>
		<s:set var="thisGuid" value="reqGuid" />
		<script type="text/javascript" src="script/DisplayStoredMeasurements.js"></script>
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
	    </style>
	    <script type="text/javascript">
		var measurementTable;
		//$(document).ready(function() {	
		//});
		var ws_coloreye = new WSWrapper('coloreye');
		var measurementsJSON;
		var returnLoadMeasurements_message;
		var reqGuid = "${reqGuid}";
		var new_measurements_table;
		var fullMeasurementsInfo = "${fullMeasurementsInfo}";
		var rgbList;
		var dateList = [];
		var isDupe = false;
			
		function RetrieveAllStoredMeasurements() {
		  	console.log("RetrieveAllStoredMeasurements")
			var clreyemodel = $('#spectroModel').val();
		 	var spectromessage = new SpectroMessage('RetrieveAllStoredMeasurements',clreyemodel);
		    var json = JSON.stringify(spectromessage);
		    ws_coloreye.send(json);
		}
		  	
	 	function RecdMessage() {
			console.log("Received Message");
		  	//parse the spectro
		  	console.log("Message is " + ws_coloreye.wsmsg);
		  	console.log("isReady is " + ws_coloreye.isReady + " BTW");
		  	var return_message=JSON.parse(ws_coloreye.wsmsg);
		  	switch (return_message.command) {
		  	case 'RetrieveAllStoredMeasurements':
		  		if (return_message.errorMessage!="") {
		  			$("#errmsg").text(return_message.errorMessage);
		  			DisplayError();
		  		} else {
		  				
		  			measurementsJSON = return_message.storedMeasurements;
		  			returnLoadMeasurements_message = return_message;

		  			//console.log("measurementsJSON @ RETRIEVEALLSTOREDMEASUREMENTS RESPONSE: " + measurementsJSON);
		  			
		  			// TODO : Wire AJAX to a new action that specifically changes the json to include the rgbHex
		  			// values so that the dialog can utilize the Color Chip column as well
		  			var jsonIN = JSON.stringify(returnLoadMeasurements_message);
		  			$.ajax({
		  				url: "spectroObtainAllRGBColorsAction.action",
		  				type: "POST",
		  				data: {	
		  					reqGuid : "${reqGuid}",
		  					newStoredMeasurements : jsonIN
		  				},
		  				datatype : "html",
		  				async: true,
		  				success: function (data) {
		  					if(data.sessionStatus === "expired"){
		  	               		window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
		  	            } else{
		  	                	console.log("Returned from LOAD/SAVE ACTION successfully!");
		  	                	//console.log("AJAX DATA for OBTAINALLRGBCOLORS: " + data);
		  	                	
		  	                	var ajaxResponseHTML = $.parseHTML(data);
		  	                	//var jsonOUT = $(ajaxResponseHTML).find('#fullMeasurementsInfo').attr("value");
		  	                	var rgbColors = $(ajaxResponseHTML).find('#hexList').attr("value");
		  	                	rgbList = rgbColors.split(", ");
		  	                	//console.log("RGB HEXCODE LIST: " + rgbList);
		  	               		}
		  				},
		  				error: function(err){
		  					alert("failure: " + err);
		  				}
		  			});
		  				$("#newStoredMeasurements").attr("value",ws_coloreye.wsmsg);
		  			}
		  			break;
		  		default:
		  		//Not an response we expected...
		  		$("#errmsg").text("Unexpected call to " + return_message.command);
		  		  	DisplayError();
		  	}
		  	  	
		}
		  	
		function SaveNewMeasurementsCallback(jsonOUT) {
		  	var json = JSON.parse(jsonOUT);
		  	
		  	var measurements_table = $('#measurement_table').DataTable();
		  	for (i = 0; i < json.length; i++){
		  		
		  		var measuredCurve = json[i].measuredCurve;
			  	var sampleCurve = measuredCurve.toString();
			  	
		  		measurements_table.row.add([
		  			'<span class="chip p-0" style="background:' + json[i].rgbHex + '"></span>',
		  			json[i].sampleName,
		  			moment(json[i].sampleDateTime).format('DD/MMM/YY hh:mm:ss A'),
		  			json[i].sampleDescr,
		  			json[i].model,
		  			json[i].serialNbr,
		  			'<button type="button" id="deleterow" class="btn btn-danger dltrow" title="Delete job"><i class="far fa-trash-alt"></i></button>',
		  			sampleCurve,
		  			json[i].rgbHex
		  		]).draw();
		  	}
		  	
		}
		  	
		$(document).on("click", "#loadMeasurementsButton", function(event){
			var measurementTable =  $('#measurement_table').DataTable();
			var dates = [];
			console.log("NUMBER OF ROWS: " + measurementTable.data().count()/9);
			for (index = 0; index < measurementTable.data().count()/9; index++){
				dates.push(measurementTable.row(index).data()[2]);
  				console.log("DATE AT INDEX " + index + ": " + dates[index]);
			}
  			dateList = dates;
  			console.log("All DATES: " + dateList);
			var isDupe = false;
		  	new_measurements_table = $('#new_spectro_measurements_table')
		  	.DataTable(
		  		{
		  			"scrollY" : "500px",
		  			scrollCollapse : true,
		  			data : measurementsJSON,
		  			dom : 'rt',
		  			columns : [
		  				{
		  					//data: 'addMeasurement'
		  					"render" : function(
		  						data, type, row,meta) {
		  						isDupe = false;
		  						var dateTime = moment(row.sampleDateTime).format('DD/MMM/YY hh:mm:ss A');
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
		  							//return '<span style="width: 40px; height: 40px; border-radius: 50%; border: .5px solid black; float: left; "></span>';
		  							return '<span class="chip p-0" style="background:' + rgbList[meta.row] + '"></span>';
		  							//return '<span></span>';
		  					}
		  				},
		  				{
		  					//data: 'sampleName'
		  					"render" : function(
		  							data, type, row,meta) {
		  						if (isDupe == true) {
		  							return '<input type="text" id="sampleName" name="sampleName" style="width:95%" value="DUPLICATE" disabled>';
		  						} else {
		  							return '<input type="text" id="sampleName" name="sampleName" style="width:95%" value="' + row.sampleName + '">';
		  						}
		  							
		  					}
		  				},
		  				{
		  					//data: 'sampleDateTime'
		  					"render" : function(
		  							data, type, row,meta) {
		  						var dateTime = moment(row.sampleDateTime).format('DD/MMM/YY hh:mm:ss A');
		  						
		  							return dateTime ;
		  					}
		  				},
		  				{
		  					//data: 'sampleDescr'
		  					"render" : function(
		  							data, type, row,meta) {
		  						if (isDupe == true) {
		  							return '<input type="text" id="sampleDescr" name="sampleDescr" style="width:95%" value="This is a duplicate of an existing entry" disabled>';
		  						} else {
		  							return '<input type="text" id="sampleDescr" name="sampleDescr" style="width:95%" value="">';
		  						}
		  						
		  					}
		  				},
		  				{
		  					data: 'spectroModel'
		  				},
		  				{
		  					data: 'spectroSerialNbr'
		  				}
		  			]
		  		});
		  		
		$('#addNewMeasurementsModal').modal('show');
		  	
		setTimeout(function () {
			$($.fn.dataTable.tables( true ) ).DataTable().columns.adjust().draw();
		},200);
		  	
		});
		  	
		$(document).on("click", "#saveNewMeasurements", function(event){
		  		
			var inputCheckbox = $('#new_spectro_measurements_table :input[type="checkbox"]').map(function(){return $(this).prop('checked');}).get();
		  	var inputTextFields = $('#new_spectro_measurements_table :text').map(function(){return $(this).val();}).get();
		  	var textFieldIndex = 0;
		  	for (i = 0; i < measurementsJSON.length; i++){
		  		
		  		measurementsJSON[i].addMeasurement = inputCheckbox[i];
		  		measurementsJSON[i].sampleName = inputTextFields[textFieldIndex];
		  		measurementsJSON[i].sampleDescr = inputTextFields[textFieldIndex + 1];
		  		textFieldIndex = textFieldIndex + 2;
		  	}
		  	
		  	//console.log("measurementsJSON object after editting new measurements: " + JSON.stringify(measurementsJSON));
		  		$('#new_spectro_measurements_table').DataTable().destroy();
		  		$('#addNewMeasurementsModal').modal('hide');
	
		  		returnLoadMeasurements_message.storedMeasurements = measurementsJSON;
		  		var jsonIN = JSON.stringify(returnLoadMeasurements_message);
		  		
		  		$.ajax({
		  		url: "spectroLoadNewStoredMeasurementsAction.action",
		  		type: "POST",
		  		data: {	
		  			reqGuid : "${reqGuid}",
		  			newStoredMeasurements : jsonIN,
		  			fullMeasurementsInfo : "${fullMeasurementsInfo}"
		  		},
		  		datatype : "html",
		  		async: true,
		  		success: function (data) {
		  			if(data.sessionStatus === "expired"){
		          		window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
		         	}
		          	else{
		          		console.log("Returned from LOAD/SAVE ACTION successfully!");
		          		var ajaxResponseHTML = $.parseHTML(data);
		          		//console.log("Ajax Response HTML: " + ajaxResponseHTML);
		          		var jsonOUT = $(ajaxResponseHTML).find('#fullMeasurementsInfo').attr("value");
		          		//location.reload(); 
		          		$('#dltmsg').addClass('d-none');
		  				$('#editmsg').addClass('d-none');
		          		SaveNewMeasurementsCallback(jsonOUT);
		         		}
		  		},
		  		error: function(err){
		  			alert("failure: " + err);
		  		}
		  	});
		  		
		});
		  	
		$(document).on("click", "#cancelLoadMeasurements", function(event){
		  		
		      	$('#new_spectro_measurements_table').DataTable().destroy();
		  		$('#addNewMeasurementsModal').modal('hide');
		  		$('#dltmsg').addClass('d-none');
		  		$('#editmsg').addClass('d-none');
		  		
		});
		
		
		$(document).ready(function() {
			
			//console.log("in docready");
			RetrieveAllStoredMeasurements();
			setTimeout(function () {
		  	      $($.fn.dataTable.tables( true ) ).DataTable().columns.adjust().draw();
		  	 },200);
			
			var custWebSpectroRemote = "${custWebSpectroRemote}";
			//console.log("This custWebSpectroRemote is: " + custWebSpectroRemote);

			var measurementTable =  $('#measurement_table').DataTable();
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
			
			$('#measurement_table tbody').on('click', 'tr', function(event){
				editRow = $(this).closest('tr');
		    	console.log("Table Row Clicked!!!");
		    	var chip = $(this).closest('span');
		    	var chipInfo = chip.css('background');
		    	colorChip = editRow.closest('.chip').css("background");
		    	colorName = measurementTable.row(editRow).data()[1];
		    	rowDateTime = measurementTable.row(editRow).data()[2];
		    	description = measurementTable.row(editRow).data()[3];
		    	spectroModel = measurementTable.row(editRow).data()[4];
		    	rowSerialNbr = measurementTable.row(editRow).data()[5];
		    	rowMeasuredCurve = measurementTable.row(editRow).data()[7];
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
			
			$('#savebtn').on('click', function(){
				colorName = $("#editSampleName").val();
				description = $("#editSampleDescription").val();
				$.ajax({
					url:"updateSpectroRemoteAction.action",
					data: {
						"reqGuid" : reqGuid,
						"measurementDate" : rowDateTime,
						"measurementSerialNbr" : rowSerialNbr,
						"measurementModel" : spectroModel,
						"measurementName" : colorName,
						"measurementDescription" : description,
						"measurementMeasuredCurve" : rowMeasuredCurve,
						"measurementRgbHex" : rowRgbHex,
					},
					type: "POST",
					//dataType:"json",
					datatype : "html",
		  			async: true,
					success:function(data){
						console.log("Update was successful");
						
					},
					error:function(request, status, error){
						console.log("Error: " + status + " (" + error + ")");	
					}
					
				});
				
				var updatedRow = measurementTable.row(editRow).data();
				updatedRow[1] = colorName;
				updatedRow[3] = description;
				$("#measurement_table").dataTable().fnUpdate(updatedRow,editRow,undefined,false);
				$('#dltmsg').addClass('d-none');
				$('#editmsg').removeClass('d-none');
				$('#editmsg').text("The color ( " + colorName + " ) has been updated successfully");
				
			});
			
			$('#measurement_table tbody').on('click', '.dltrow', function(event){
				
			    	deleteRow = $(this).closest('tr');
			    	console.log("Table Row Clicked!!!");
			    	colorName = measurementTable.row(deleteRow).data()[1];
			    	rowDateTime = measurementTable.row(deleteRow).data()[2];
			    	rowSerialNbr = measurementTable.row(deleteRow).data()[5];
			    	console.log("The row data used to delete the measurement is: " + rowDateTime + ", " + rowSerialNbr);
			    	$('#deletemodal').modal().show();
			    	event.stopPropagation();
			    });
			
			$('#yesbtn').on('click', function(){
				$.ajax({
					url:"deleteSpectroRemoteAction.action",
					data: {
						"reqGuid" : reqGuid,
						"measurementDate" : rowDateTime,
						"measurementSerialNbr" : rowSerialNbr
					},
					type: "POST",
					datatype : "html",
			  		async: true,
					success:function(data){
						console.log("Deletion was successful");
						
					},
					error:function(request, status, error){
						console.log("Error: " + status + " (" + error + ")");
					}
					
				});
				measurementTable.row(deleteRow).remove().draw();
				$('#editmsg').addClass('d-none');
				$('#dltmsg').removeClass('d-none');
				$('#dltmsg').text("The color ( " + colorName + " ) has been deleted successfully");
				
			});
			
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
			<div class="col-sm-1"></div>
			<div class="col-sm-10 mb-2	">
				<h1>Manage Stored Measurements</h1>
			</div>
			<div class="col-sm-1"></div>
			</div>
			<div class="row">
				<div class="col-sm-1"></div>
				<div class="col-sm-10">
					<h6 id="dltmsg" class="text-success d-none"></h6>
					<h6 id="editmsg" class="text-success d-none"></h6>
					<table id="measurement_table" class="table table-striped table-bordered" style="width:100%">
						<thead>
							<tr>
								<th></th>								
								<th>Color Name</th>
								<th>Date/Time</th>
								<th>Description</th>
								<th>Spectro Model</th>
								<th>Spectro Serial Number</th>
								<th>Delete</th>
								<th style="display:none"></th>
								<th style="display:none"></th>
							</tr>
						</thead>
						<tbody>
							<s:iterator var="measurement" value="custWebSpectroRemote" status="outer">
								<tr class="border-bottom-1 border-dark">
									<td><span class="chip p-0" style="background: <s:property value="#measurement.rgbHex"/>;"></span></td>
									<td><s:property value="#measurement.sampleName" /></td>
									<td ><s:date name="#measurement.sampleDateTime" format="dd/MMM/yy hh:mm:ss a"/></td>
									<td><s:property value="#measurement.sampleDescr" /></td>
									<td><s:property value="#measurement.model" /></td>
									<td><s:property value="#measurement.serialNbr" /></td>
									<td>
										<button type="button" id="deleterow" class="btn btn-danger dltrow" title="Delete job">
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
			<div class="row">
				
				<div class="col-sm-12">
					<div class="row">
	            		<div class="col-sm-1">
	 						<s:hidden name="reqGuid" value="%{reqGuid}"/>
	 						<s:hidden name="newStoredMeasurements" id="newStoredMeasurements" value=""/>
	 						<s:hidden name="fullMeasurementsInfo" id="fullMeasurementsInfo" value="%{fullMeasurementsInfo}"/>
	 						<s:hidden name="custWebSpectroRemote" id="custWebSpectroRemote" value="%{custWebSpectroRemote}"/>
	 						<s:hidden name="hexList" id="hexList" value="%{hexList}"/>
						</div>
						<div class="col-sm-4"></div>

						<div class="col-sm-2">
						</div>
					</div>
				</div>
			
			</div>
			<div class="row">
				<div class="col-sm-1"></div>
				<div class="col-6"></div>
				<div class="col-2">
					<button type="button" id="loadMeasurementsButton" class="btn btn-secondary mb-5 mt-2 mx-1">Load Measurements</button>
					<!-- <s:submit id="loadMeasurementsButton" cssClass="btn btn-secondary mb-5 mt-2 mx-1" value="Load Measurements"/> -->
				</div>
				<div class= "col-2">
					<s:submit cssClass="btn btn-secondary mb-5 mt-2 mx-1" value="Cancel" action="userCancelAction"/>
				</div>
				<div class="col-sm-1"></div>
			</div>
			</s:form>
			</div>
		
		<!-- Modal for loading new spectro samples -->	
		<div class="row">
			<div class="modal fade" aria-labelledby="addNewMeasurementsModal" aria-hidden="true"  id="addNewMeasurementsModal" role="dialog">
				<div class="modal-dialog modal-dialog-cu" role="document">
					<div class="modal-content">
						<div class="modal-header">
							<h5 class="modal-title">Load New Measurements</h5>
							<button type="button" class="close" data-dismiss="modal" aria-label="Close" ><span aria-hidden="true">&times;</span></button>
						</div>
						<div class="modal-body modal-body-cu">
							<table id="new_spectro_measurements_table" class="table table-striped table-bordered" style="width: 100%">
								<thead>
									<tr>
										<th>Add</th>
										<th></th>
										<th>Color Name</th>
										<th>Date/Time</th>
										<th>Description</th>
										<th>Spectro Model</th>
										<th>Spectro Serial Number</th>
									</tr>
								</thead>
								<tbody></tbody>
								<tfoot></tfoot>
							</table> 
						</div>
						<div class="modal-footer">
							<button type="button" id="saveNewMeasurements" class="btn btn-primary mb-5 mt-2 mx-1">Save</button>
							<button type="button" id="cancelLoadMeasurements" class="btn btn-secondary mb-5 mt-2 mx-1">Cancel</button>
						</div>
					</div>
				</div>
			</div>				
		</div>
		
		<!--  Confirmation Modal for spectro sample deletion -->
		<div class="row">
		<div class="modal fade" tabindex="-1" role="dialog" id="deletemodal">
		  <div class="modal-dialog" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title text-danger">Delete Stored Measurement</h5>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      <div class="modal-body">
		        <h6>Are you sure you want to delete this measurement?</h6>
		      </div>
		      <div class="modal-footer">
		      <s:form>
			        <%-- <s:submit id="deletebtn" class="btn btn-danger" action="deleteCustomer" value="Yes"></s:submit> --%>
			        <button type="button" id="yesbtn" class="btn btn-danger" data-dismiss="modal">Yes</button>
			        <button type="button" id="nobtn" class="btn btn-secondary" data-dismiss="modal">No</button>
		       </s:form>
		      </div>
		    </div>
		  </div>
		</div>
		</div>
		
		<!-- Edit modal for updating a spectro sample name and description -->
		<div class="row">
		<div class="modal fade" tabindex="-1" role="dialog" id="editmodal">
		  <div class="modal-dialog" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title">Edit Stored Measurement</h5>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      <div class="modal-body" style="height: 50%">
		        <div class="row">
					<div class="col-6">
						<span style="font-weight: bold">Color Name:</span>
						<input type="text" id="editSampleName" name="editSampleName" style="width:95%" value="">
						<br><br>
						<span style="font-weight: bold">Description:</span>
						<textarea type="text" id="editSampleDescription" name="editSampleDescription" style="width:95%; height: 45%"></textarea>
					</div>
					<div class="col-6">
					<span style="font-weight:bold">Date/Time: </span><p id="spectroDateInfo"></p>
					<span style="font-weight:bold">Model: </span><p id="spectroModelInfo"></p>
					<span style="font-weight:bold">Serial Number: </span><p id="spectroSerialNumber"></p>
					<span class="chip p-0" id="spectroColorChip"></span>
					
					</div>		        
		        </div>
		      </div>
		      <div class="modal-footer">
		      <s:form>
			        <button type="button" id="savebtn" class="btn btn-primary" data-dismiss="modal">Save</button>
			        <button type="button" id="cancelbtn" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
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