<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title><s:text name="editFormula.editFormula"/></title>
			<!-- JQuery -->
		<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
		<link rel="stylesheet" href="css/bootstrapxtra.css" type="text/css">
		<link rel="stylesheet" href="js/smoothness/jquery-ui.min.css" type="text/css">
		<link rel="stylesheet" href="css/CustomerSherColorWeb.css" type="text/css">
		<link rel="stylesheet" href="font-awesome-4.7.0/css/font-awesome.min.css" type="text/css">
		<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/jquery-ui.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/popper.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/bootstrap.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.5.3.js"></script>
		<script type="text/javascript" charset="utf-8"	src="script/tinter-1.5.0.js"></script>
		<s:set var="thisGuid" value="reqGuid" />
		<script>
		function moveDown(selector){
			let key;
			if($(selector).length > 0){
				key = selector;
			}else{key = 'table-row';}
			
	
			$('html, body').animate({ 
				scrollTop: $(key).offset().top -= 80}, 
				   1400, 
				   "easeOutQuint"
			);
		}
		
		function isNumber(evt) {
		    evt = (evt) ? evt : window.event;
		    var charCode = (evt.which) ? evt.which : evt.keyCode;
		    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
		        return false;
		    }
		    return true;
		}
		
		function validatePercentInput(){
			if($('#pct').val().match(/^[1-9]\d{0,3}/) === null){
				console.log("Invalid entries detected.");
				return false;
			}else{return true;}
		}
		
		function validateSizeSelection() {

			//console.log("STARTING CONVERSION CONTAINER SIZE: " + $("select[id='startingSizeList'] option:selected").val());
			//console.log("ENDING CONVERSION CONTAINER SIZE: " + $("select[id='endingSizeList'] option:selected").val());
			
			if($("select[id='startingSizeList'] option:selected").val() == -1 || 
			   $("select[id='endingSizeList'] option:selected").val() == -1) {	
					$('#sizeAddBtn').prop("disabled", true);
			} else {
					$('#sizeAddBtn').prop("disabled", false);
			}
		}
		
		
		function addWarningPopoverForClearedColorant(selector,colorantItem){
			var safeTintSysId = encodeURIComponent(colorantItem.tintSysId.toString());
			var safeName = encodeURIComponent(colorantItem.name.toString());
			$(selector).attr("data-toggle", "popover");
			$(selector).attr("data-placement","left");
			$(selector).attr("data-content", '<s:text name="editFormula.colorantRemoved"><s:param>'+ safeTintSysId +'-'+ safeName.replace(/%20/g, " ") +'</s:param></s:text>');
			$(selector).popover({trigger : 'manual'});
			$(selector).popover('toggle');
			$('.popover').addClass('popover-warning');
			$(document).on('click', function (e) {
				$('.popover').each(function(){
	            	$(this).popover('toggle');
		            $('input[data-toggle="popover"]').each(function(){
		            	$(this).removeAttr('data-placement');
		                $(this).removeAttr('data-content');
		                $(this).removeAttr('data-toggle');
		            });
		    	});
			});
		}
		
		//Borrowed from CorrectFormula.jsp - hence the naming
		function percentConfirmClick(){
			if(validatePercentInput()){
			
				var mydata = {reqGuid:'${thisGuid}', percentOfFormula:parseInt($("#pct").val())};
				var jsonIn = JSON.stringify(mydata);
				console.log("in percentAddClick and jsonIn is");
				console.log(jsonIn);
				$.ajax({
					url: "percentAdjustmentAction.action",
					contentType : "application/json; charset=utf-8",
					type: "POST",
					data: jsonIn,
					datatype : "json",
					async: true,
					success: function (data) {
						
						//console.log(data);		
						if(data.sessionStatus === "expired"){
		            		window.location.href = "./invalidLoginAction.action";
		            	}
		            	else{
		            		
							// walk through result formula
							data.displayFormula.ingredients.forEach(function(item, i){
								//console.log(item);
								if(item.increment.every(allZero)){
									
									//clear zero increment colorant
									clearColorant(i);
									addWarningPopoverForClearedColorant('#form_ingredientList_' + i + '__selectedColorant',item);
								}else{
									for (let x = 0; x < item.increment.length; x++) {
										var safeTintSysId = encodeURIComponent(item.tintSysId.toString());
										var safeName = encodeURIComponent(item.name.toString());
										var name = safeName.replace(/%20/g, " ");
										console.log("tintSysId: " + safeTintSysId + " name: " + name);
										//console.log("$('#form_ingredientList_'" + i + "'__increments_'" + x + "'_').val(" + item.increment[x] +")");
										$('#form_ingredientList_' + i + '__selectedColorant option:selected').attr('selected',false);
										$('#form_ingredientList_' + i + '__selectedColorant option[value="'+ safeTintSysId + '-'+ name + '"]').attr('selected',true);
										$('#form_ingredientList_' + i + '__increments_' + x + '_').val(item.increment[x]);
									}
								}
							});
							
							//Show adjustment info alert
							var pct = $('#pct').val()
							$('#adj-info').text('<s:text name="editFormula.pctOfFormula"><s:param>'+ pct +'</s:param></s:text>');
							$('#adj_info_row').show();
							
							//clear remaining colorants
							for (var i = data.displayFormula.ingredients.length; i < 8; i++) {
								clearColorant(i);
							}
							
							$('#pct').val('');
							moveDown('#source_row');
		            	}
					},
					error: function(err){
						//TODO - handle this error more gracefully for client
						console.error('Action call failed: ' + err);
					}
				});
			}
		}
		
		function showScaleBySizeModal() {
			$('#invalidConversionWarning').hide();
			$('#scaleFormulaBySizeModal').modal('show');
		}
		
		function sizeConfirmClick(){
			//sList = '${sizeList}';
			//console.log("SIZE LIST: " + sList)
			var mydata = {reqGuid:'${thisGuid}',
					startingContainer:$("select[id='startingSizeList'] option:selected").val(),
					endingContainer:$("select[id='endingSizeList'] option:selected").val()};
			var jsonIn = JSON.stringify(mydata);
			console.log("SIZE ADJUSTMENT ACTION JSON: " + jsonIn);
			$.ajax({
				url: "sizeAdjustmentAction.action",
				contentType : "application/json; charset=utf-8",
				type: "POST",
				data: jsonIn,
				datatype : "json",
				async: true,
				success: function (data) {
					
					//console.log(data);		
					if(data.sessionStatus === "expired"){
	            		window.location.href = "./invalidLoginAction.action";
	            	}
	            	else{
	            		
						// walk through result formula
						data.displayFormula.ingredients.forEach(function(item, i){
							//console.log(item);
							if(item.increment.every(allZero)){
								
								//clear zero increment colorant
								clearColorant(i);
								addWarningPopoverForClearedColorant('#form_ingredientList_' + i + '__selectedColorant',item);
							}else{
								for (let x = 0; x < item.increment.length; x++) {
									var safeTintSysId = encodeURIComponent(item.tintSysId.toString());
									var safeName = encodeURIComponent(item.name.toString());
									var name = safeName.replace(/%20/g, " ");
									console.log("tintSysId: " + safeTintSysId + " name: " + name);
									//console.log("$('#form_ingredientList_'" + i + "'__increments_'" + x + "'_').val(" + item.increment[x] +")");
									$('#form_ingredientList_' + i + '__selectedColorant option:selected').attr('selected',false);
									$('#form_ingredientList_' + i + '__selectedColorant option[value="'+ safeTintSysId + '-'+ name + '"]').attr('selected',true);
									$('#form_ingredientList_' + i + '__increments_' + x + '_').val(item.increment[x]);
								}
							}
						});
						
						//Show adjustment info alert
						var sz = $("select[id='endingSizeList'] option:selected").text();
						$('#adj-info').text('<s:text name="editFormula.sizeOfFormula"><s:param>'+ sz +'</s:param></s:text>');
						$('#adj_info_row').show();
						
						//clear remaining colorants
						for (var i = data.displayFormula.ingredients.length; i < 8; i++) {
							clearColorant(i);
						}
						$('#scaleFormulaBySizeModal').modal('hide');
						$('#invalidConversionWarning').hide();
						moveDown('#source_row');
	            	}
				},
					error: function(err){
						//TODO - handle this error more gracefully for client
						$('#invalidConversionWarning').show();
						console.error('Action call failed: ' + err);
					}
				});
			
		}
		
		function clearColorant(index){
			var safeIndex = encodeURIComponent(index);
			$('#form_ingredientList_' + safeIndex + '__selectedColorant option:selected').attr('selected',false);
			$('#form_ingredientList_' + safeIndex + '__selectedColorant option[value="-1"]').attr('selected',true);
			for (var x = 0; x < 4; x++) {
				$('#form_ingredientList_' + safeIndex + '__increments_' + x + '_').val('0');
			}
		} 
		
		
		//test to clear zeroed out colorants
		function allZero(element, index, array) {
			  return element === 0;
		}
		
		
		// let user override the warning message 
		function setOverride(){
			$("input[name='userWarningOverride']").val("true"); 
		}
		
		
		$(function(){
			moveDown('#source_row');
			
			$('#adjustByPctModal').on('shown.bs.modal',function(){
				$('#pct').focus();
			});
			
			//Hide Adjust By % button if formula is empty
			if($('#scaleByPercentVisible').val() === 'false'){
				$('#scaleByPct').hide();
			}
			
			//validate colorId and colorName fields
			//prevent special characters < or > from being entered
			$(document).on({
				'keypress blur':function(){
					try{
						if(event.key == ">" || event.key == "<"){
							throw '<s:text name="global.noLtOrGt"/>';
						}
						if($(this).val().includes("<") || $(this).val().includes(">")){
							throw '<s:text name="global.invalidEntryLtGt"/>';
						}
						$('input[name^="color"]').each(function(){
							$(this).parents().find('#errortxt').remove();
							$(this).removeClass('border-danger');
						});
						$('input:submit').attr('disabled', false);
					} catch(msg){
						if(event.type=="keypress"){
							event.preventDefault();
						}
						if(!$(document).find('#errortxt').is(':visible')){
							if($(this).is('input[name="colorId"]')){
								$('.errormsg').eq(0).append('<div id="errortxt" class="text-danger"></div>');
							} else {
								$('.errormsg').eq(1).append('<div id="errortxt" class="text-danger"></div>');
							}
						}
						$(this).parents('.row').find('#errortxt').text(msg)
						$(this).addClass('border-danger');
						if(event.type=="blur"){
							$(this).focus();
							$('input:submit').attr('disabled', true);
						}
					}
				}
			}, '[name="colorId"], [name="colorName"]');
			
		});
		
		$(function(){
    		var colorCompany;
    		var colorID;
    		var sourceDesc;
    		
    		// internationalize CUSTOM and the source description
	    	switch("${sessionScope[thisGuid].colorComp}"){
	    		case "CUSTOM":
	    			colorCompany = '<s:text name="processColorAction.custom"/>';
	    			break;
	    		default:
	    			colorCompany = "${sessionScope[thisGuid].colorComp}";
	    	}
	    	switch("${sessionScope[thisGuid].displayFormula.sourceDescr}"){
	    		case "CUSTOM MANUAL MATCH":
	    			sourceDesc = '<s:text name="editFormula.customManualMatch"/>';
	    			break;
	    		case "CUSTOM MANUAL VINYL SAFE MATCH":
	    			sourceDesc = '<s:text name="editFormula.customManualVinyl"/>';
	    			break;
	    		default:
	    			sourceDesc = "${sessionScope[thisGuid].displayFormula.sourceDescr}";
    		}
	    	$("#colorComp").text(colorCompany);
	    	$("#colorID").text(colorID);
	    	$("#sourceDesc").text(sourceDesc);
    	});
		
		</script>
		
		<s:if test="hasActionMessages()">
			<script>
				$(function(){ $("#actionMsgModal").modal('show'); });
			</script>
		</s:if>
		
		<style>
	        .sw-bg-main {
	            background-color: ${sessionScope[thisGuid].rgbHex};
	        }
	        .table-sm>tbody>tr>td{
	        	padding-bottom: 1px;
	        }
	        .popover-danger {
				background-color: #dc3545;
				border-color: #d43f3a;
				color: white;
			}
			.popover-warning {
				background-color: #ffc107;
				border-color: #ffc107;
			}
			.popover-warning .popover-body{
				color: black;
			}
			
			.popover-danger .arrow:after {
				border-right-color: #dc3545;
			}
			.popover-danger .arrow {
				border-bottom-color: #dc3545;
				border-top-color: #dc3545;
			}
			.popover-warning .arrow:after {
				border-left-color: #ffc107;
			}
			.popover-warning .arrow {
				border-bottom-color: #ffc107;
				border-top-color: #ffc107;
			}
			.popover-body{
				color: white;
			}
			.chip {
			  position: relative;
			  display: -webkit-box;
			  display: -ms-flexbox;
			  display: flex;
			  -webkit-box-orient: vertical;
			  -webkit-box-direction: normal;
			  -ms-flex-direction: column;
			  flex-direction: column;
			  min-width: 10px;
			  min-height: 10px;
			  height: 52px;
			  width: 52px;
			  border-radius: 50%;
			  border: 1px solid rgba(0, 0, 0, 0.125);
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
				</div>
			</div>
<br>
			<s:if test="%{#session[reqGuid].controlNbr != null && #session[reqGuid].controlNbr > 0}">
				<div class="row" id="controlNbrDisplay">
			</s:if>
			<s:else>
				<div class="row" id="controlNbrDisplay" hidden="true">
			</s:else> 
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
					</div>
					<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
						<strong>Job Number:</strong>
					</div>
					<div class="col-lg-4 col-md-6 col-sm-7 col-xs-8" id="controlNbr">
						${sessionScope[thisGuid].controlNbr}
					</div>
					<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0">
					</div>
				</div>
				<div class="row">
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
					</div>
					<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
					<s:iterator value="#session[reqGuid].jobFieldList" status="stat">
						<strong><s:property value="screenLabel"/><s:text name="global.colonDelimiter"/></strong><br>
					</s:iterator>
					</div>
					<div class="col-lg-4 col-md-6 col-sm-7 col-xs-8">
					<s:iterator value="#session[reqGuid].jobFieldList" status="stat">
						<s:property value="enteredValue" /><br>
					</s:iterator>	
					</div>
					<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0">
					</div>
				</div>
<br>
			<s:form action="MfUserNextAction" validate="true"  theme="bootstrap" id="form">
				<div class="row">
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
					</div>
					<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4 mb-1">
						<strong><s:text name="global.colorCompanyColon"/></strong>
					</div>
					<div class="col-lg-4 col-md-6 col-sm-7 col-xs-8">
						<span id="colorComp"></span>
					</div>
					<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0">
					</div>
				</div>
				<div class="row">
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
					</div>
					<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
						<strong><s:text name="global.colorIdColon"/></strong>
					</div>
					<div class="col-lg-2 col-md-4 col-sm-4 col-xs-8">
						<s:textfield name="colorId" size="20" maxlength="10" />
					</div>
					<div class="col-lg-6 col-md-4 col-sm-4 col-xs-0 errormsg">
					</div>
				</div>
				<div class="row">
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
					</div>
					<div class="col-lg-2 col-md-2 col-sm-3 col-xs-3">
						<strong><s:text name="global.colorNameColon"/></strong>
					</div>
					<div class="col-lg-2 col-md-3 col-sm-4 col-xs-6">
						<s:textfield name="colorName" size="20" maxlength="30" />
					</div>
					<div class="col-lg-6 col-md-5 col-sm-4 col-xs-3 errormsg">
					</div>
				</div>
				<div class="row">
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
					</div>
					<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
						<strong><s:text name="global.notesColon"/></strong>
					</div>
					<div class="col-lg-2 col-md-4 col-sm-4 col-xs-8 mb-1">
						<s:textfield name="colorNotes" size="20" maxlength="35" />
						<div class="chip sw-bg-main"></div>
					</div>
					<div class="col-lg-6 col-md-4 col-sm-4 col-xs-0 errormsg">
					</div>
				</div>
				<div class="row">
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
					</div>
					<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
						<strong><s:text name="global.salesNumberColon"/></strong>
					</div>
					<div class="col-lg-4 col-md-6 col-sm-7 col-xs-8">
						${sessionScope[thisGuid].salesNbr}<br>
					</div>
					<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0">
					</div>
				</div>
				<div class="row">
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
					</div>
					<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
						<strong><s:text name="global.productNumberColon"/></strong>
					</div>
					<div class="col-lg-4 col-md-6 col-sm-7 col-xs-8">
						${sessionScope[thisGuid].prodNbr} - ${sessionScope[thisGuid].sizeText}
					</div>
					<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0">
					</div>
				</div>
				<div class="row">
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
					</div>
					<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
						<strong><s:text name="global.productDescrColon"/></strong>
					</div>
					<div class="col-lg-4 col-md-6 col-sm-7 col-xs-8">
						${sessionScope[thisGuid].intExt} ${sessionScope[thisGuid].quality} ${sessionScope[thisGuid].composite} ${sessionScope[thisGuid].finish}<br>
						${sessionScope[thisGuid].base}<br>
					</div>
					<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0">
					</div>
				</div>
				<br>
				<div class="row mt-4" id="pctRow">
					<div class="col-lg-2 col-md-2 col-sm-2 col-xs-0">
					</div>
					<div class="col-lg-3 col-md-3 col-sm-3 col-xs-12">
						<%-- <div class="form-group" id="select">
						    <label for="editType">Edit Type:</label>
						    <select class="form-control" id="editType" onchange="editTypeSelected(this);">
						      <option selected disabled>Manual or Percent</option>
						      <option value="manual">Manual Formula</option>
						      <option value="percent">Percent of Formula</option>
						    </select>
						    <small class="form-text text-muted">Select your Edit Type from the dropdown.</small>
						 </div> --%>
					</div>
					<div class="col-lg-7 col-md-7 col-sm-7 col-xs-0">	
			    	</div>
				</div>
				<div id="adj_info_row" class="row mt-3" style="display: none;">
					<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0">
					</div>
					<div class="col-lg-2 col-md-8 col-sm-10 col-xs-12 text-center">
					<div id="adj-info" class="alert alert-info" role="alert">
					</div>
					</div>
					<div class="col-lg-6 col-md-2 col-sm-1 col-xs-0">	
			    	</div>
				</div>
				<div id="source_row" class="row mb-3">
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
					</div>
					<div class="col-lg-6 col-md-8 col-sm-10 col-xs-12 text-center">
						<span id="sourceDesc" style="text-decoration: underline">${sessionScope[thisGuid].displayFormula.sourceDescr}</span><br>
					</div>
					<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0">
					</div>
				</div>

				<s:if test="hasActionErrors()">
					<div class="row">
	            		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
						</div>
						<div class="col-lg-6 col-md-8 col-sm-10 col-xs-12">
							<s:actionerror escape="false"/>
						</div>
						<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0">	
			    		</div>
					</div>
				</s:if>
				<s:if test="hasActionMessages()">
					<div class="warningModalWrapper">
						<div class="modal fade" aria-labelledby="actionMsgModal" aria-hidden="true"  id="actionMsgModal" role="dialog">
					    	<div class="modal-dialog" role="document">
								<div class="modal-content">
									<div class="modal-header">
										<h5 class="modal-title"><s:text name="editFormula.overrideWarning"/></h5>
										<button type="button" class="close" data-dismiss="modal" aria-label="%{getText('global.close')}" ><span aria-hidden="true">&times;</span></button>
									</div>
									<div class="modal-body">
										<s:actionmessage style="color:black; background-color:#FAFF98; border-color:#FAFF98"/>
										<s:if test="%{userFillCheckMsg==true}"> 
										    <div class="alert alert-danger" role="alert">
											    <s:text name="processManualFormulaAction.userFillCheckMsg"/>
											</div>
										</s:if>
									</div>
									<div class="modal-footer">
										<s:submit cssClass="btn btn-primary" id="overrideWarning" value="%{getText('global.yes')}" onclick="setOverride();" action="MfUserNextAction"/>
										<button type="button" class="btn btn-secondary" id="cancelWarning" data-dismiss="modal" aria-label="%{getText('global.close')}" ><s:text name="global.close"/></button>
									</div>
								</div>
							</div>
						</div>
						<s:hidden name="userWarningOverride" value="false" /> 
						<s:iterator value="previousWarningMessages" status="stat">
							<s:hidden name="previousWarningMessages[%{#stat.index}]"/>
						</s:iterator>
					</div>
				</s:if>
				
			    <div id="table_row" class="row mb-3">
						<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
						</div>	
						<div id="table-row" class="col-lg-6 col-md-8 col-sm-10 col-xs-12">
							<table class="table table-sm">
							    <thead>
							      <tr>
							        <th class="bg-light" style="width: 30%;"><s:hidden name="reqGuid" value="%{reqGuid}"/>
										<Strong>${sessionScope[thisGuid].displayFormula.clrntSysId}*<s:text name="global.colorant"/></Strong>
									</th>
							        <th class="bg-light text-center" style="width: 17.5%;"><Strong>${sessionScope[thisGuid].displayFormula.incrementHdr[0]}</Strong></th>
							        <th class="bg-light text-center" style="width: 17.5%;"><Strong>${sessionScope[thisGuid].displayFormula.incrementHdr[1]}</Strong></th>
							        <th class="bg-light text-center" style="width: 17.5%;"><Strong>${sessionScope[thisGuid].displayFormula.incrementHdr[2]}</Strong></th>
							        <th class="bg-light text-center" style="width: 17.5%;"><Strong>${sessionScope[thisGuid].displayFormula.incrementHdr[3]}</Strong></th>
							      </tr>
							    </thead>
							    <tbody>
							    	<s:iterator value="ingredientList" status="outerStat">
							    		<tr>
											<td class=""><s:select headerKey="-1" headerValue="%{getText('global.selectColorant')}"
													list="availClrntNameId"
													name="ingredientList[%{#outerStat.index}].selectedColorant" /></td>
											<s:iterator value="increments" status="stat">
												<td><s:textfield name="ingredientList[%{#outerStat.index}].increments[%{#stat.index}]" style="text-align:center"/></td>
											</s:iterator>
										</tr>
								    </s:iterator>
							    </tbody>
							  </table>
						</div>
						<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0">	
			    		</div>
		    	</div>
<br>
		    	<div class="d-flex flex-row justify-content-around mb-5">
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
						
					</div>	
					<div class="col-lg-4 col-md-4 col-sm-2 col-xs-6">
						<s:submit cssClass="btn btn-primary" value="%{getText('global.next')}" action="MfUserNextAction"/>
						<button type="button" onclick="$('#scaleFormulaByPctModal').modal('show');" class="btn btn-secondary ml-3" id="scaleByPct"><s:text name="editFormula.scaleByPct"/></button>
						<button type="button" onclick="showScaleBySizeModal();" class="btn btn-secondary ml-3" id="scaleBySz"><s:text name="editFormula.scaleBySize"/></button>
					</div>
					<div class="col-lg-2 col-md-3 col-sm-4 col-xs-4">	
						<s:submit cssClass="btn btn-secondary pull-right" value="%{getText('global.cancel')}" action="userCancelAction"/>
					</div>
					<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0">	
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
		<s:hidden id="scaleByPercentVisible" name="scaleByPercentVisible" value="%{scaleByPercentVisible}"/>
		<!-- Adjust By Percent Modal -->
	    <div class="modal fade" aria-labelledby="scaleFormulaByPctModal" aria-hidden="true"  id="scaleFormulaByPctModal" role="dialog">
	    	<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title"><s:text name="editFormula.scaleByPct"/></h5>
						<button type="button" class="close" data-dismiss="modal" aria-label="%{getText('global.close')}" ><span aria-hidden="true">&times;</span></button>
					</div>
					<div class="modal-body">
						<p id="skipConfirmText" font-size="4"><s:text name="editFormula.enterDesiredPct"/></p>
						<input type="text" class="number-only form-control mt-1 mb-2" id="pct" size="10" placeholder="100" maxlength="3" style="font-size: 16px;" autofocus="autofocus" onkeypress="return isNumber(event);">
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary" id="percentAddBtn" data-dismiss="modal" onclick="percentConfirmClick();"><s:text name="editFormula.calculate"/></button>
						<button type="button" class="btn btn-secondary" id="skipConfirmCancel" data-dismiss="modal" aria-label="%{getText('global.cancel')}" ><s:text name="global.cancel"/></button>
					</div>
				</div>
			</div>
		</div>
		
		<!-- Adjust By Size Modal -->
	    <div class="modal fade" aria-labelledby="scaleFormulaBySizeModal" aria-hidden="true"  id="scaleFormulaBySizeModal" role="dialog">
	    	<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title"><s:text name="editFormula.scaleBySize"/></h5>
						<button type="button" class="close" data-dismiss="modal" aria-label="%{getText('global.close')}" ><span aria-hidden="true">&times;</span></button>
					</div>
					<div class="modal-body">						
						<h6><s:text name="editFormula.startingSize"/></h6>
						<s:select id="startingSizeList" list="sizeList" listKey="miscCode" listValue="miscName"
						 headerKey="-1" headerValue="%{getText('editFormula.startingSizeSelect')}"/>
						 
						 <span class="fa fa-arrow-down fa-2x"></span>
						 <br><br>
						 
						 <h6><s:text name="editFormula.endingSize"/></h6>
						 <s:select id="endingSizeList" list="sizeList" listKey="miscCode" listValue="miscName" 
						 headerKey="-1" headerValue="%{getText('editFormula.endingSizeSelect')}" onchange="validateSizeSelection()"/>
						 <div id="invalidConversionWarning" class="alert alert-danger">
							<s:text name="editFormula.invalidSizeConversion" />
						 </div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary" id="sizeAddBtn" onclick="sizeConfirmClick();" disabled><s:text name="editFormula.calculate" /></button>
						<button type="button" class="btn btn-secondary" id="skipConfirmCancel" data-dismiss="modal" aria-label="%{getText('global.cancel')}" ><s:text name="global.cancel"/></button>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>