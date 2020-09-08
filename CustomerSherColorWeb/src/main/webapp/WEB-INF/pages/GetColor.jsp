<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>

<%@ taglib prefix="s" uri="/struts-tags" %>
 
<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title><s:text name="getColor.chooseColor"/></title>
			<!-- JQuery -->
		<link rel=StyleSheet href="css/bootstrap.min.css" type="text/css">
		<link rel=StyleSheet href="css/bootstrapxtra.css" type="text/css">
		<link rel=StyleSheet href="js/smoothness/jquery-ui.css" type="text/css">
		<link rel=StyleSheet href="css/CustomerSherColorWeb.css" type="text/css"> 	
		<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.21/css/dataTables.bootstrap4.min.css"/>
  		<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
		<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/jquery-ui.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/bootstrap.min.js"></script>
		<script type="text/javascript" src="https://cdn.datatables.net/1.10.21/js/jquery.dataTables.min.js"></script>
		<script type="text/javascript" src="https://cdn.datatables.net/1.10.21/js/dataTables.bootstrap4.min.js"></script>
 		<script type="text/javascript" src="https://cdn.datatables.net/v/dt/dt-1.10.21/datatables.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.4.2.js"></script>
		<script type="text/javascript" charset="utf-8"	src="script/getcolorautocomplete-1.4.4.js"></script>
		<style>
		.chip {
		    width: 40px;
		    height: 40px;
		    border-radius: 50%;
		    border: .5px solid black;
		    float: left;
			}
		.dataTables_filter {
			float: left !important;
			display: inline;
			}
		.table-hover tbody tr:hover{
			background: #FFFF99;
			cursor: pointer;
			}
		.ui-autocomplete {
		    max-height: 300px;
		    overflow-y: auto;
		    overflow-x: hidden;
		}
		</style>
		<script type="text/javascript" charset="utf-8">
			$(function(){
				var selectedValue;
				$("[id^=selectedCoTypes]").change(function(){
					selectedValue = $("[id^=selectedCoTypes]:checked").val();
					//console.log("selected value - " + selectedValue);
					if (selectedValue === "COMPET"){
						$('#colorCompanies').removeClass('d-none');
					} else {
						$('#colorCompanies').addClass('d-none');
					}
					if (selectedValue === "SAVEDMEASURE"){
						// show table and hide the bottom elements except for cancel button
						$('#measuresRow').removeClass('d-none');
						$('#measuresTable').DataTable().columns.adjust().draw();
						$('#colorEntry').addClass('d-none');
						document.getElementById("nextBtnDiv").style.visibility = "hidden";
					} else {
						$('#measuresRow').addClass('d-none');
						$('#colorEntry').removeClass('d-none');
						document.getElementById("nextBtnDiv").style.visibility = "visible";
					}
					
					if(this.checked) {
						$('.form-control-feedback, .help-block').remove();
						$('.has-feedback').removeClass('has-error has-feedback');
						$('#partialColorNameOrId').val('');
				    }
					
				});
				
				//validate partialColorNameOrId if custom manual or custom match
				//prevent special characters < or > from being entered
				$(document).on({
					'keypress blur': function(){
						if(selectedValue == "CUSTOM" || selectedValue == "CUSTOMMATCH"){
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
					}
				}, '#partialColorNameOrId');
				
			});
			
			
			$(document).ready(function() {
				displaySavedMeasurementsTable();
			});
			
			function displaySavedMeasurementsTable() {
				var measuresTable;
				var selectedRow;
				var measurementColorChip;
				var measurementColorName;
				var measurementDateTime;
				var measurementDescription;
				var measurementSpectroModel;
				var measurementSpectroSerial;
				var measurementCurve;
				var measurementRgbHex;
				
				// internationalize the datatable; null here defaults to english, otherwise update url
				var langUrl = null;
				console.log("${session['WW_TRANS_I18N_LOCALE']}");
				
				switch("${session['WW_TRANS_I18N_LOCALE']}"){
				case("es_ES"):
					langUrl = "//cdn.datatables.net/plug-ins/1.10.21/i18n/Spanish.json";
					break;
				}
				
				measuresTable = $('#measuresTable').DataTable({
					"paginate": false,
			        "scrollY" : 400,
			        "language" : {
			        	"url" : langUrl
			        }
				});
				
				$("#measuresTable").addClass("table-hover");
				
				$('#measuresTable tbody').on('click', 'tr', function(event){
					selectedRow = $(this).closest('tr');
			    	var chip = $(this).closest('span');
			    	measurementColorChip = selectedRow.closest('.chip').css("background");
			    	measurementColorName = selectedRow.find('.name').text();
			    	measurementDateTime = selectedRow.find('.dateTime').text();
			    	measurementDescription = selectedRow.find('.description').text();
			    	measurementSpectroModel = selectedRow.find('.spectroModel').text();
			    	measurementSpectroSerial = selectedRow.find('.spectroSerial').text();
			    	measurementCurve = selectedRow.find('.curve').text();
			    	measurementRgbHex = selectedRow.find('.rgbHex').text();
			    	$("#measurementName").text(measurementColorName);
			    	$("#measurementDescription").text(measurementDescription);
			    	$("#spectroDateInfo").text(measurementDateTime);
			    	$("#spectroModelInfo").text(measurementSpectroModel);
			    	$("#spectroSerialNumber").text(measurementSpectroSerial);
			    	$("#measuredCurve").val(measurementCurve);
			    	$("#measuredName").val(measurementColorName);
			    	$('#spectroColorChip').css("backgroundColor", measurementRgbHex);
			    	$('#measurementModal').modal().show();
				});
			}
					
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
			
			<div class="row">
				<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
				</div>
				<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
					<s:iterator value="#session[reqGuid].jobFieldList" status="stat">
						<strong><s:property value="screenLabel"/><s:text name="global.colonDelimiter"/></strong><br>
					</s:iterator>
				</div>
				<div class="col-lg-4 col-md-4 col-sm-7 col-xs-8">
					<s:iterator value="#session[reqGuid].jobFieldList" status="stat">
						<s:property value="enteredValue" /><br>
					</s:iterator>	
				</div>
				<div class="col-lg-4 col-md-4 col-sm-1 col-xs-0">
				</div>
			</div>
<br>
			<s:form action="colorUserNextAction" validate="true" focusElement="partialColorNameOrId" theme="bootstrap">
				<div class="row">
						<div class="col-lg-2 col-md-2 col-sm-1">
						</div>
	            		<div class="col-lg-8 col-md-8 col-sm-10">
	            			<div class="form-group">
	            				<strong><s:text name="getColor.colorTypeColon"/></strong>
	            				<div class="controls">
	            					<s:iterator value="cotypes" status="i">
		            					<div class="form-check">
		            					  <s:if test="%{#i.index == 0}">
		            					  	<input class="form-check-input" type="radio" name="selectedCoTypes" value='<s:property value="key"/>' id="selectedCoTypes-<s:property value="%{#i.index}"/>" checked>
		            					  </s:if>
		            					  <s:else>
		            					  	<input class="form-check-input" type="radio" name="selectedCoTypes" value='<s:property value="key"/>' id="selectedCoTypes-<s:property value="%{#i.index}"/>">
		            					  </s:else>
										  <label class="form-check-label font-weight-normal" for="selectedCoTypes-<s:property value="%{#i.index}"/>">
										    <s:property value="value"/>
										  </label>
										</div>
		            				</s:iterator>
	            				</div>
	            			</div>
 	            			<%-- <s:radio label="Color Type" name="selectedCoTypes" list="cotypes" value="defaultCoTypeValue" /> --%>
						</div>
						<div class="col-lg-2 col-md-2 col-sm-1">
						</div>
				</div>
				<div class="row mt-sm-2 d-none" id="colorCompanies">
					<div class="col-lg-2 col-md-2 col-sm-2">
					</div>
					<div class="col-lg-4 col-md-4 col-sm-6">
						<s:select label="%{getText('getColor.companyName')}" id="companiesList" list="colorCompanies"  />
					</div>
					<div class="col-lg-6 col-md-6 col-sm-4">
					</div>
				</div>
				<div class="row mt-sm-2 d-none" id="measuresRow">
					<div class="col-lg-1 col-md-1 col-sm-1 col-xs-0">
					</div>
											
					<div id="table-wrapper" class="col-lg-10 col-md-12">
						<div>
							<div style="text-align: center">
								<h5><s:text name="getColor.chooseColor"/></h5>
							</div>
							<table id="measuresTable" class="table table-striped table-bordered">
								<thead>
									<tr>
										<th></th>								
										<th><s:text name="global.colorName"/></th>
										<th><s:text name="global.dateTime"/></th>
										<th><s:text name="global.description"/></th>
										<th style="display:none"></th>
										<th style="display:none"></th>
										<th style="display:none"></th>
										<th style="display:none"></th>
									</tr>
								</thead>
							    <tbody>
									<s:iterator var="measurement" value="savedMeasurements" status="outer">
										<tr class="border-bottom-1 border-dark">
											<td><span class="chip p-0" style="background: <s:property value="#measurement.rgbHex"/>;"></span></td>
											<td class="name"><s:property value="#measurement.sampleName" /></td>
											<td class="dateTime"><s:date name="#measurement.sampleDateTime" format="dd/MMM/yy hh:mm:ss a"/></td>
											<td class="description"><s:property value="#measurement.sampleDescr" /></td>
											<td class="spectroModel" style="display:none"><s:property value="#measurement.model" /></td>
											<td class="spectroSerial" style="display:none"><s:property value="#measurement.serialNbr" /></td>
											<td class="curve" style="display:none"><s:property value="%{curvesList[#outer.index]}" /></td>
											<td class="rgbHex" style="display:none"><s:property value="#measurement.rgbHex"/></td>
										</tr>
									</s:iterator>
							    </tbody>
							</table>	
						</div>
			    	</div>   	
    	
					<div class="col-lg-1 col-md-1 col-sm-1 col-xs-0">
					</div>
				</div>
				<br>
				
	<%-- 			<div class="row">
					<div class="col-sm-3">
					</div>
					<div class="col-sm-6">
						<s:fielderror/>	
					</div>
				</div>--%>
				<div>
					<div class="row" id="colorEntry">
						<div class="col-lg-2 col-md-2 col-sm-1">
							<s:hidden name="reqGuid" id="reqGuid" value="%{reqGuid}"/>
							<s:hidden name="colorData" id="colorData" value=""/>
						</div>
						<div class="col-lg-8 col-md-8 col-sm-10">
							<s:textfield name="partialColorNameOrId" id="partialColorNameOrId" label="%{getText('getColor.enterColorNameOrNumber')}" 
								placeholder="%{getText('getColor.chooseColorType')}" size="30" maxlength="30" 
								cssStyle="font-size: 16px;" autofocus="autofocus"  />
						</div>
						<div class="col-lg-2 col-md-2 col-sm-1">
						</div>
					</div>
					<div class="row">
						<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
						</div>	
						<div class="col-lg-1 col-md-1 col-sm-1 col-xs-3" id="nextBtnDiv">
							<s:submit cssClass="btn btn-primary" value="%{getText('global.next')}" action="colorUserNextAction"/>
						</div>
						<div class="col-lg-7 col-md-7 col-sm-9 col-xs-9">	
							<s:submit cssClass="btn btn-secondary pull-right" value="%{getText('global.cancel')}" action="userCancelAction"/>
						</div>
						<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">	
			    		</div>
		    		</div>
		    	</div>
		    	
				<!-- Modal for choosing a Ci62 remote measurement -->
				<div class="modal fade" tabindex="-1" role="dialog" id="measurementModal">
				  <div class="modal-dialog" role="document">
				    <div class="modal-content">
				      <div class="modal-header" style="padding: 20px">
				        <h5 class="modal-title"><s:text name="getColor.remoteMeasurement"/></h5>
				        <button type="button" class="close" data-dismiss="modal" aria-label="%{getText('global.close')}">
				          <span aria-hidden="true">&times;</span>
				        </button>
				      </div>
				      <div class="modal-body" style="padding: 20px">
				        <div class="row">
							<div class="col-6">
								<span style="font-weight: bold"><s:text name="global.colorNameColon"/></span>
								<p id="measurementName"></p>
								<span style="font-weight: bold"><s:text name="global.descriptionColon"/></span>
								<p id="measurementDescription"></p>
								<span class="chip p-0" id="spectroColorChip"></span>
							</div>
							<div class="col-6">
								<span style="font-weight:bold"><s:text name="global.dateTimeColon"/></span><p id="spectroDateInfo"></p>
								<span style="font-weight:bold"><s:text name="global.modelColon"/></span><p id="spectroModelInfo"></p>
								<span style="font-weight:bold"><s:text name="global.serialNumberColon"/></span><p id="spectroSerialNumber"></p>
								<s:hidden name="measuredCurve" id="measuredCurve" value=""/>
								<s:hidden name="measuredName" id="measuredName" value=""/>
							</div>		        
				        </div>
				      </div>
				      <div class="modal-footer">
				      		<s:submit cssClass="btn btn-primary" action="colorUserNextAction" value="%{getText('global.next')}" autofocus="autofocus"/>
					        <button type="button" id="cancelBtn" class="btn btn-secondary" data-dismiss="modal"><s:text name="global.cancel"/></button>
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
</html>