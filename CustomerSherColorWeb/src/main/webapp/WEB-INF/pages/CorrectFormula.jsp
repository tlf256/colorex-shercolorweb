<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>


<%@ taglib prefix="s" uri="/struts-tags" %>

<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title><s:text name="correctFormula.formulaCorrection" /></title>
			<!-- JQuery -->
		<link rel=StyleSheet href="css/bootstrap.min.css" type="text/css">
		<link rel=StyleSheet href="css/bootstrapxtra.css" type="text/css">
		<link rel=StyleSheet href="js/smoothness/jquery-ui.css" type="text/css">
		<link rel=StyleSheet href="css/CustomerSherColorWeb.css" type="text/css">
		<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
		<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/jquery-ui.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/popper.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/bootstrap.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/moment.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.4.5.js"></script>
		<script type="text/javascript" charset="utf-8"	src="script/WSWrapper.js"></script>
		<script type="text/javascript" charset="utf-8"	src="script/tinter-1.4.4.js"></script>
		<s:set var="thisGuid" value="reqGuid" />
		<style type="text/css">
		.popover-danger {
			background-color: #d9534f;
			border-color: #d43f3a;
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
		.popover-body{
			color: white;
		}
		</style>
	<script type="text/javascript">
	// global vars for tinter
	var sendingDispCommand = "false";
	var shotList = [];
	var processingDispense = false;
	// global vars for correction
	var method;

	var skippedCont = [];
	<s:iterator value="skippedCont">
	skippedCont.push(<s:property value="top"/>);
	</s:iterator>

	var discardedCont = [];
	<s:iterator value="discardedCont">
	discardedCont.push(<s:property value="top"/>);
	</s:iterator>
	
	var _rgbArr = [];
	
	<s:iterator value="tinter.canisterList" status="i">
	booya=0;
			_rgbArr["<s:property value="clrntCode"/>"]="<s:property value="rgbHex"/>";  //for colored progress bars
		</s:iterator>
		
	function sessionTinterInfoCallback(){
		if(sessionTinterInfo.tinterOnFile===true){
			
			// load colorant into dropdown menu for Manual Add
			console.log("loading colorant dropdown");
			
			$("#clrntList").empty();
			sessionTinterInfo.canisterList.sort(function(a,b){
				var nameA = a.clrntName;
				var nameB = b.clrntName;
				if (nameA < nameB) return -1;
				if (nameA > nameB) return 1;
				return 0;
			});
			sessionTinterInfo.canisterList.forEach(function(can){
				if(can.clrntCode!="NA"){
					var link = '<li class="dropdown-item"><a class="dropdown-item" href="#" onclick="addManClrnt(\''+can.clrntCode+'-'+can.clrntName+'\')">'+can.clrntCode+'-'+can.clrntName+'</a></li>';
					$("#clrntList").append(link);
				}
			});
		} else {
			// no tinter on file
			$("#tinterAlertList").empty();
		}
	}

	function startNewCycleClick(){
		// first container may have been skipped or discarded, check for it
		checkAutoProcessNextCont();

		// prev function call may or may not reload the screen, if not then set to midcycle and update the buttons.
		$("#mainForm_corrStatus").val("MIDCYCLE");
		updateButtonDisplay();
	}
	
	function addStepClick(){
		$("#formulaAdditions > tbody").empty();
		$("#addIngredients").show();
		$("#currentCont").text($("#mainForm_nextUnitNbr").val());
		$('#pct').text('');
		$('#percentPrompt').toggle();
		$("#addStep").hide();
		$("#acceptContainer").hide();
		$("#skipContainer").hide();
		$("#mainForm_displayFormulaAction").hide(); //Leave button
		$('html, body').animate({ 
			   scrollTop: $(document).height()-$(window).height()}, 
			   1400, 
			   "easeOutQuint"
		);
	}

	function acceptContainerClick(){
		// Update current cycle & container steps to ACCEPTED
		var curDate = new Date();
		// ajax call to convert correctionList to dispenseItems
        var str = { "reqGuid" : $('#mainForm_reqGuid').val(), "jsDateString" : curDate.toString(), "cycle" : $("#mainForm_currCycle").val(), "nextUnitNbr": $("#mainForm_nextUnitNbr").val(), "stepStatus" : "ACCEPTED"};
        var jsonIN = JSON.stringify(str);
        console.log(jsonIN);
        $.ajax({	
            url : "postCorrectionStatusAction.action",
            type: "POST",
            contentType : "application/json; charset=utf-8",
            dataType: "json",
            async: true,
            data : jsonIN,
            success : function(data){
				console.log(data);
				if(data.sessionStatus === "expired"){
            		window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
            	}
            	else{
            		if(data.errorMessage==null){
    					console.log("Write Successful!");
    					if(data.mergeCorrWithStartingForm == true){
    						mergeCorrWithStartingForm($("#mainForm_currCycle").val());
    					} else {
    						reloadScreen()
    					}
    				} else {
    					// Error Tell the user
    					console.log("Write Failed!" + data.errorMessage);
    					$("#tinterErrorList").empty();
    					$("#tinterErrorList").append('<li class="alert alert-danger"><s:text name="correctFormula.failedDbUpdAcceptContClick" /></li>');
    					$("#tinterErrorListTitle").text('<s:text name="correctFormula.internalDatabaseError" />');
    					$("#tinterErrorListSummary").text('<s:text name="correctFormula.pleaseRetry" />');
    					$("#tinterErrorListModal").modal('show');
    				}
            	}
            },
            error: function(textStatus, errorThrown ) {
                console.log("JSON Write Correction failed here");
                console.log(textStatus + "" + errorThrown);
            }
        });
	}

	function dispenseAcceptedClick(){
		method="DISPENSE ACCEPTED";
		// if dispenseItemList avail on display then it is the accepted formula
	<s:iterator value="dispenseItemList">
		shotList.push(new Colorant("<s:property value="clrntCode"/>",<s:property value="shots"/>,<s:property value="position"/>,<s:property value="uom"/>));
	</s:iterator>
		console.log(shotList);
		$("#reason").val('<s:text name="correctFormula.dispenseSameAsContainer"><s:param>'+$("#mainForm_acceptedContNbr").val()+'</s:param></s:text>');
		$("#mainForm_stepStatus").val("ACCEPTED");
		// start dispense process (preDispenseCheck --> decrementColorantLevels --> dispense --> recdMessage)
		preDispenseCheck();
	}

	function mistintContainerClick(){
		// mark open steps for this container as discarded.
		var curDate = new Date();
		// ajax call to post correction status to db
        var str = { "reqGuid" : $('#mainForm_reqGuid').val(), "jsDateString" : curDate.toString(), "cycle" : $("#mainForm_currCycle").val(), "nextUnitNbr": $("#mainForm_nextUnitNbr").val(), "stepStatus" : "DISCARDED"};
        var jsonIN = JSON.stringify(str);
        console.log(jsonIN);
        $.ajax({	
            url : "postCorrectionStatusAction.action",
            type: "POST",
            contentType : "application/json; charset=utf-8",
            dataType: "json",
            async: true,
            data : jsonIN,
            success : function(data){
				console.log(data);
				if(data.sessionStatus === "expired"){
            		window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
            	}
            	else{
            		if(data.errorMessage==null){
    					//console.log("Write Successful!");
    					mistintClickCallback();
    				} else {
    					// Error Tell the user
    					console.log("Write Failed!" + data.errorMessage);
    					$("#tinterErrorList").empty();
    					$("#tinterErrorList").append('<li class="alert alert-danger"><s:text name="correctFormula.failedDbUpdPostCorrStatus" /></li>');
    					$("#tinterErrorListTitle").text('<s:text name="correctFormula.internalDatabaseError" />');
    					$("#tinterErrorListSummary").text('<s:text name="correctFormula.pleaseRetry" />');
    					$("#tinterErrorListModal").modal('show');
    				}
            	}
            },
            error: function(textStatus, errorThrown ) {
                console.log("JSON Write Correction failed here");
                console.log(textStatus + "" + errorThrown);
            }
        });
	}

	function mistintClickCallback(){
		method="MISTINT CONTAINER";
		//Build mistint step info and save to DB
		var curDate = new Date();
		// ajax call to save mistint step to db
        var str = { "reqGuid" : $('#mainForm_reqGuid').val(), "jsDateString" : curDate.toString(), "cycle" : $("#mainForm_currCycle").val(), "nextUnitNbr": $("#mainForm_nextUnitNbr").val(),"reason" : "Failed Correction for Container #"+$("#mainForm_nextUnitNbr").val(), "stepStatus": "DISCARDED", "stepMethod":method, "shotList" : []};
        //var str = { "reqGuid" : $('#mainForm_reqGuid').val(), "jsDateString" : curDate.toString(), "cycle" : $("#mainForm_currCycle").val(), "nextUnitNbr": $("#mainForm_nextUnitNbr").val(), "stepStatus" : "DISCARDED"};
        var jsonIN = JSON.stringify(str);
        console.log(jsonIN);
        $.ajax({	
            url : "saveCorrectionStepAction.action",
            type: "POST",
            contentType : "application/json; charset=utf-8",
            dataType: "json",
            async: true,
            data : jsonIN,
            success : function(data){
				console.log(data);
				if(data.sessionStatus === "expired"){
            		window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
            	}
            	else{
            		if(data.errorMessage==null){
    					//console.log("Write Successful!");
    					if(data.mergeCorrWithStartingForm == true){
    						mergeCorrWithStartingForm($("#mainForm_currCycle").val());
    					} else {
    						reloadScreen()
    					}
    				} else {
    					// Error Tell the user
    					console.log("Write Failed!" + data.errorMessage);
    					$("#tinterErrorList").empty();
    					$("#tinterErrorList").append('<li class="alert alert-danger"><s:text name="correctFormula.failedDbUpdSaveCorr" /></li>');
    					$("#tinterErrorListTitle").text('<s:text name="correctFormula.internalDatabaseError" />');
    					$("#tinterErrorListSummary").text('<s:text name="correctFormula.pleaseRetry" />');
    					$("#tinterErrorListModal").modal('show');
    				}
            	}
            },
            error: function(textStatus, errorThrown ) {
                console.log("JSON Write Correction failed here");
                console.log(textStatus + "" + errorThrown);
            }
        });
	}
	
	function skipContainerClick(){
		$("#skipConfirmModal").modal('show');
	}


	function skipConfirmOKClick(){
		method="SKIP CONTAINER";
		//Build correction step info and save to DB
		var curDate = new Date();
		// ajax call to save discarded step to db
        var str = { "reqGuid" : $('#mainForm_reqGuid').val(), "jsDateString" : curDate.toString(), "cycle" : $("#mainForm_currCycle").val(), "nextUnitNbr": $("#mainForm_nextUnitNbr").val(),"reason" : $("#skipConfirmInput").val(), "stepStatus": "SKIPPED", "stepMethod":method, "shotList" : []};
        //var str = { "reqGuid" : $('#mainForm_reqGuid').val(), "jsDateString" : curDate.toString(), "cycle" : $("#mainForm_currCycle").val(), "nextUnitNbr": $("#mainForm_nextUnitNbr").val(), "stepStatus" : "DISCARDED"};
        var jsonIN = JSON.stringify(str);
        console.log(jsonIN);
        $.ajax({	
            url : "saveCorrectionStepAction.action",
            type: "POST",
            contentType : "application/json; charset=utf-8",
            dataType: "json",
            async: true,
            data : jsonIN,
            success : function(data){
				console.log(data);
				if(data.sessionStatus === "expired"){
            		window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
            	}
            	else{
            		if(data.errorMessage==null){
    					//console.log("Write Successful!");
    					waitForShowAndHide("#skipConfirmModal");
    					reloadScreen();
    				} else {
    					// Error Tell the user
    					console.log("Write Failed!" + data.errorMessage);
    					$("#tinterErrorList").empty();
    					$("#tinterErrorList").append('<li class="alert alert-danger"><s:text name="correctFormula.failedDbUpdSkipConfirm" /></li>');
    					$("#tinterErrorListTitle").text('<s:text name="correctFormula.internalDatabaseError" />');
    					$("#tinterErrorListSummary").text('<s:text name="correctFormula.pleaseRetry" />');
    					$("#tinterErrorListModal").modal('show');
    				}
            	}
            },
            error: function(textStatus, errorThrown ) {
                console.log("JSON Write Correction failed here");
                console.log(textStatus + "" + errorThrown);
            }
        });
	}
	

	function checkAutoProcessNextCont(){
		// if middle of cycle, check and possibly auto process next container if it has been skpped or discarded
		var currentCont = $("#mainForm_nextUnitNbr").val();
		var autoProcessed = false;
		var breakException = {};
		try {
			skippedCont.forEach(function(item){
				if(currentCont==item){
					autoSkip();
					throw breakException;
				}
			});
			discardedCont.forEach(function(item){
				if(currentCont==item){
					autoDiscard();
					throw breakException;
				}
			});
		} catch (e) {
			if (e !== breakException) console.log(e);
		}
	}

	function autoSkip(){
		method="AUTO SKIP";
		var curDate = new Date();
		// ajax call to save mistint step to db
        var str = { "reqGuid" : $('#mainForm_reqGuid').val(), "jsDateString" : curDate.toString(), "cycle" : $("#mainForm_currCycle").val(), "nextUnitNbr": $("#mainForm_nextUnitNbr").val(),"reason" : "Previously Skipped. Cannot make any more corrections.", "stepStatus": "PREVIOUSLY SKIPPED", "stepMethod":method, "shotList" : []};
        var jsonIN = JSON.stringify(str);
        console.log(jsonIN);
        $.ajax({	
            url : "saveCorrectionStepAction.action",
            type: "POST",
            contentType : "application/json; charset=utf-8",
            dataType: "json",
            async: true,
            data : jsonIN,
            success : function(data){
				console.log(data);
				if(data.sessionStatus === "expired"){
            		window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
            	}
            	else{
            		if(data.errorMessage==null){
    					//console.log("Write Successful!");
    					if(data.mergeCorrWithStartingForm == true){
    						mergeCorrWithStartingForm($("#mainForm_currCycle").val());
    					} else {
    						reloadScreen()
    					}
    				} else {
    					// Error Tell the user
    					console.log("Write Failed!" + data.errorMessage);
    					$("#tinterErrorList").empty();
    					$("#tinterErrorList").append('<li class="alert alert-danger"><s:text name="correctFormula.failedDbUpdAutoSkip" /></li>');
    					$("#tinterErrorListTitle").text('<s:text name="correctFormula.internalDatabaseError" />');
    					$("#tinterErrorListSummary").text('<s:text name="correctFormula.pleaseRetry" />');
    					$("#tinterErrorListModal").modal('show');
    				}
            	}
            },
            error: function(textStatus, errorThrown ) {
                console.log("JSON Write Correction failed here");
                console.log(textStatus + "" + errorThrown);
            }
        });
	}

	function autoDiscard(){
		method="AUTO DISCARD";
		var curDate = new Date();
		// ajax call to save discard step to db
        var str = { "reqGuid" : $('#mainForm_reqGuid').val(), "jsDateString" : curDate.toString(), "cycle" : $("#mainForm_currCycle").val(), "nextUnitNbr": $("#mainForm_nextUnitNbr").val(),"reason" : "Previously Discarded. Cannot make any more corrections.", "stepStatus": "PREVIOUSLY DISCARDED", "stepMethod":method, "shotList" : []};
        var jsonIN = JSON.stringify(str);
        console.log(jsonIN);
        $.ajax({	
            url : "saveCorrectionStepAction.action",
            type: "POST",
            contentType : "application/json; charset=utf-8",
            dataType: "json",
            async: true,
            data : jsonIN,
            success : function(data){
				console.log(data);
				if(data.sessionStatus === "expired"){
            		window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
            	}
            	else{
            		if(data.errorMessage==null){
    					//console.log("Write Successful!");
    					if(data.mergeCorrWithStartingForm == true){
    						mergeCorrWithStartingForm($("#mainForm_currCycle").val());
    					} else {
    						reloadScreen()
    					}
    				} else {
    					// Error Tell the user
    					console.log("Write Failed!" + data.errorMessage);
    					$("#tinterErrorList").empty();
    					$("#tinterErrorList").append('<li class="alert alert-danger"><s:text name="correctFormula.failedDbUpdAutoDiscard" /></li>');
    					$("#tinterErrorListTitle").text('<s:text name="correctFormula.internalDatabaseError" />');
    					$("#tinterErrorListSummary").text('<s:text name="correctFormula.pleaseRetry" />');
    					$("#tinterErrorListModal").modal('show');
    				}
            	}
            },
            error: function(textStatus, errorThrown ) {
                console.log("JSON Write Correction failed here");
                console.log(textStatus + "" + errorThrown);
            }
        });
	}

	function mergeCorrWithStartingForm(myCycle){
		var curDate = new Date();
		// ajax call to merge Correction Cycle formula with Starting Formula
        var str = { "reqGuid" : $('#mainForm_reqGuid').val(), "jsDateString" : curDate.toString(), "cycle" : myCycle};
        var jsonIN = JSON.stringify(str);
        console.log(jsonIN);
        $.ajax({	
            url : "mergeCorrWithStartingFormAction.action",
            type: "POST",
            contentType : "application/json; charset=utf-8",
            dataType: "json",
            async: true,
            data : jsonIN,
            success : function(data){
				console.log(data);
				if(data.sessionStatus === "expired"){
            		window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
            	}
            	else{
            		if(data.errorMessage==null){
    					//console.log("Write Successful!");
    					reloadScreen()
    				} else {
    					// Error Tell the user
    					console.log("Write Failed!" + data.errorMessage);
    					$("#tinterErrorList").empty();
    					$("#tinterErrorList").append('<li class="alert alert-danger"><s:text name="correctFormula.failedDbUpdMerge" /></li>');
    					$("#tinterErrorListTitle").text('<s:text name="correctFormula.internalDatabaseError" />');
    					$("#tinterErrorListSummary").text('<s:text name="correctFormula.pleaseRetry" />');
    					$("#tinterErrorListModal").modal('show');
    				}
            	}
            },
            error: function(textStatus, errorThrown ) {
                console.log("JSON Merge Correction failed here");
                console.log(textStatus + "" + errorThrown);
            }
        });
		
	}
		
	function cancelAddClick(){
		$("#addIngredients").hide();
		
		updateButtonDisplay();
	}

	function addManClrnt(myClrnt){
		method="MANUAL ADDITION";
		var newRow = "";
		newRow = newRow + '<tr>';
		newRow = newRow + '<td id="clrntString">'+myClrnt+'</td>'
		newRow = newRow + '<td><input type="text" class="form-control number-only" value="0"></td>'
		newRow = newRow + '<td><input type="text" class="form-control number-only" value="0"></td>'
		newRow = newRow + '<td><input type="text" class="form-control number-only" value="0"></td>'
		newRow = newRow + '<td><input type="text" class="form-control number-only" value="0"></td>'
		newRow = newRow + '</tr>';
		$("#formulaAdditions > tbody:last-child").append(newRow);
		$('html,body').animate({scrollTop: $("#formulaAdditions > tbody:last-child").offset().top -= 80});
		rebuildColorantList();
	}

	function percentAddClick(){
		$("#percentPrompt").show();
		$("#pct").val("");
		$("#pct").focus();
	}
	
	function percentConfirmClick(){
		method="PERCENT ADDITION";
		$("#formulaAdditions > tbody").empty();
		var mydata = {reqGuid:$("#mainForm_reqGuid").val(), percentOfFormula:parseInt($("#pct").val())};
		var jsonIn = JSON.stringify(mydata);
		console.log("in percentAddClick and jsonIn is");
		console.log(jsonIn);
		$.ajax({
			url: "percentOfFormulaAction.action",
			contentType : "application/json; charset=utf-8",
			type: "POST",
			data: jsonIn,
			datatype : "json",
			async: true,
			success: function (data) {
				if(data.sessionStatus === "expired"){
            		window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
            	}
            	else{
            		// walk through result formula
    				var newRow = "";
    				data.ingredientList.forEach(function(item){
    					console.log(item);
    					newRow = "";
    					newRow = newRow + '<tr>';
    					newRow = newRow + '<td id="clrntString">'+item.tintSysId+"-"+item.name+'</td>'
    					newRow = newRow + '<td><input type="text" class="form-control number-only" value="'+item.increment[0]+'"></td>'
    					newRow = newRow + '<td><input type="text" class="form-control number-only" value="'+item.increment[1]+'"></td>'
    					newRow = newRow + '<td><input type="text" class="form-control number-only" value="'+item.increment[2]+'"></td>'
    					newRow = newRow + '<td><input type="text" class="form-control number-only" value="'+item.increment[3]+'"></td>'
    					newRow = newRow + '</tr>';
    					$("#formulaAdditions > tbody:last-child").append(newRow);
    					$('#pct').text('');
    					$('#percentPrompt').toggle();
    				});
    				rebuildColorantList();
            	}
			},
			error: function(err){
				alert('<s:text name="global.failurePlusErr"><s:param>' + err + '</s:param></s:text>');
			}
		});
	}
	
	function rebuildColorantList() {
		$("#clrntList").empty();
		sessionTinterInfo.canisterList.sort(function(a,b){
			var nameA = a.clrntName;
			var nameB = b.clrntName;
			if (nameA < nameB) return -1;
			if (nameA > nameB) return 1;
			return 0;
		});
		sessionTinterInfo.canisterList.forEach(function(can){
			var clrntIdentifier = can.clrntCode + '-' + can.clrntName;
			var clrntAdded = false;
			// check if already added into the table
			$('#formulaAdditions > tbody tr').each(function(){
	            var clrntInRow = $(this).find('#clrntString').text();
	            if (clrntInRow == clrntIdentifier){
	            	clrntAdded = true;
	            } 
			});
			if(can.clrntCode!="NA" && !clrntAdded){
				var link = '<li class="dropdown-item"><a class="dropdown-item" href="#" onclick="addManClrnt(\''+clrntIdentifier+'\')">'+clrntIdentifier+'</a></li>';
				$("#clrntList").append(link);
			}
		});
	}
	
	function validateInput(inputTextArray) {
	    var result = true;
	    inputTextArray.each(function(){
	        if(this.value  && this.value  !== ""){
	            if(this.value.match(/^[0-9]\d{0,1}$/) != null){
	                console.log("content was valid");
	            }
	            else {
	            	$('html,body').animate({scrollTop: $(this).offset().top -= 80});
		            result = false;
				  console.log("Invalid entries detected.");
	              $(this).attr("data-toggle", "popover");
	              $(this).attr("data-placement","bottom");
	              $(this).attr("data-content", '<s:text name="global.positiveNbr" />');
	              $(this).popover({trigger : 'manual'});
	              $(this).popover('toggle');
	              $(this).next('.popover').addClass('popover-danger');
	            }
	        }
	    });
	    
	    return result;
	}

	function dispenseAddClick(){
		// Walk formula additions and build json to send to server for dispense shot setup
		console.log("in dispenseAddClick");
		shotList = [];
		var invalidFlag = false;
		var correctionList = [];
		$('#formulaAdditions > tbody tr').each(function(){
            var rowClrntString = $(this).find('#clrntString').text();
            var rowIncrArray = [];
            
            //validate row inputs from user, display error if invalid
            console.log($(this).find('input'));
            if(validateInput($(this).find('input'))){
            	$(this).find('input').each(function(){
                    var num = parseInt(this.value) || 0;
                    rowIncrArray.push(num);
                });
            	correctionList.push({clrntString: rowClrntString, incrArray: rowIncrArray});
            }
            else{invalidFlag = true;}
		});
		console.log(correctionList);
		invalidFlag = validateReason(invalidFlag);
		var tmpInvalidFlag = invalidFlag;
		invalidFlag = validateCorrectionList(invalidFlag,correctionList);
		
		// ajax call to convert correctionList to dispenseItems
		if(!invalidFlag && !tmpInvalidFlag){
			var str = { "reqGuid" : $('#mainForm_reqGuid').val(), "correctionList" : correctionList};
	        var jsonIN = JSON.stringify(str);
	        console.log(jsonIN);
	        $.ajax({	
	            url : "correctionConvertIncrementsAction.action",
	            type: "POST",
	            contentType : "application/json; charset=utf-8",
	            dataType: "json",
	            async: true,
	            data : jsonIN,
	            success : function(data){
	            	console.log(data);
	            	if(data.sessionStatus === "expired"){
                		window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
                	}
                	else{
                		//Building shotList of Colorants to dispense for row
    					data.dispenseItemList.forEach(function(item){
    						shotList.push(new Colorant(item.clrntCode, item.shots, item.position, item.uom));
    					});
    					console.log(shotList); // Before removing potential Zero Shot colorants
						
    					shotList = removeZeroShots(shotList);
    					
    					console.log(shotList); // After removing potential Zero Shot colorants
    					
    					// start dispense process (productFillLevelCheck --> preDispenseCheck --> decrementColorantLevels --> dispense --> recdMessage)
    					productFillLevelCheck();
                	}
	            },
	            error: function(textStatus, errorThrown ) {
	                console.log("JSON dispense failed here");
	                console.log(textStatus + " " + errorThrown);
	            }
	        });
		}
		else{
			console.log('Dispense failed due to invalid input.');
		}
	}
	
	function validateCorrectionList(invalidFlag, correctionList){
		// validate correction list values
		if(correctionList === undefined || correctionList.length == 0){
			$('html,body').animate({scrollTop: $('#formulaAdditions').offset().top -= 80});
			console.log("Invalid entries detected.");
			$('#formulaAdditions').attr("data-toggle", "popover");
			$('#formulaAdditions').attr("data-placement","top");
			$('#formulaAdditions').attr("data-content", '<s:text name="correctFormula.additionSet" />');
			$('#formulaAdditions').popover({trigger : 'manual'});
			$('#formulaAdditions').popover('toggle');
			$('.popover').addClass('popover-danger');
			return true;
		}else{
			// make sure colorants in the list aren't all zeroed out
			var allZeroes = true;
			correctionList.forEach(function(row){
				row.incrArray.forEach(function(item){
					if (item != 0){
						allZeroes = false;
					}
				});
			});
			if (allZeroes){
				$('html,body').animate({scrollTop: $('#formulaAdditions').offset().top -= 80});
				console.log("Invalid entries detected.");
				$('#formulaAdditions').attr("data-toggle", "popover");
				$('#formulaAdditions').attr("data-placement","top");
				$('#formulaAdditions').attr("data-content", '<s:text name="correctFormula.additionSetNotAllZero" />');
				$('#formulaAdditions').popover({trigger : 'manual'});
				$('#formulaAdditions').popover('toggle');
				$('.popover').addClass('popover-danger');
				return true;
			}else{
				return false;
			}
		}
	}
	
	function validateReason(invalidFlag){
		//validate reason text, check for length
		if($('#reason').val().length < 2 || $('#reason').val().length > 100){
			$('html,body').animate({scrollTop: $('#reason').offset().top -= 80});
			console.log("Invalid entries detected.");
			$('#reason').attr("data-toggle", "popover");
			$('#reason').attr("data-placement","top");
			$('#reason').attr("data-content", '<s:text name="correctFormula.reasonMustBeGtTwoChar" />');
			$('#reason').popover({trigger : 'manual'});
			$('#reason').popover('toggle');
			$('.popover').addClass('popover-danger');
			return true;
		}else{return false;}
	}
</script>
<script type="text/javascript"> // dispense checks
	function productFillLevelCheck(){
		// Check Colorant Load in can and see if space avail in can
		var ozEntered = 0.0;
		//shotList is the colorant they want to add
		shotList.forEach(function(item){
			console.log(item);
			ozEntered += (item.shots / item.uom);
		});
		console.log("oz entered is");
		console.log(ozEntered);
		var ozAvail = $("#clrntSpaceAvail").text();
		console.log("oz avail is");
		console.log(ozAvail);
		if(ozEntered > ozAvail){
			console.log("show product fill warning or error");
			if($("#maxLoadType").val()=="ACTUAL_ENFORCE"){
				$("#tinterErrorList").empty();
				$("#tinterErrorList").append('<li class="alert alert-danger"><s:text name="correctFormula.maxColorantLoadIs"><s:param>' + $("#maxClrntLoad").text() + '</s:param></s:text></li>');
				$("#tinterErrorList").append('<li class="alert alert-danger"><s:text name="correctFormula.currentClrntLoadProduct"><s:param>' + $("#currClrntLoad").text() + '</s:param></s:text></li>');
				$("#tinterErrorList").append('<li class="alert alert-danger"><s:text name="correctFormula.exceedMaxLimit"><s:param>' + ozEntered + '</s:param></s:text></li>');
				$("#tinterErrorListTitle").text('<s:text name="correctFormula.exceedsLimitError" />');
				$("#tinterErrorListSummary").text('<s:text name="correctFormula.mustReduceColorantAmt" />');
				$("#tinterErrorListModal").modal('show');
			} else {
				$("#fillWarningList").empty();
				if($("#maxLoadType").val()=="ACTUAL_WARN") {
					$("#fillWarningList").append('<li class="alert alert-warning"><s:text name="correctFormula.maxColorantLoadIs"><s:param>' + $("#maxClrntLoad").text() + '</s:param></s:text></li>');
					$("#fillWarningList").append('<li class="alert alert-warning"><s:text name="correctFormula.currentClrntLoadProduct"><s:param>' + $("#currClrntLoad").text() + '</s:param></s:text></li>');
					$("#fillWarningList").append('<li class="alert alert-warning"><s:text name="correctFormula.exceedMaxLimit"><s:param>' + ozEntered + '</s:param></s:text></li>');
					$("#fillWarningListTitle").text('<s:text name="correctFormula.warningExceedsProductLimit" />');
				} else {
					$("#fillWarningList").append('<li class="alert alert-warning"><s:text name="correctFormula.estMaxClrnt"><s:param>' + $("#maxClrntLoad").text() + '</s:param></s:text></li>');
					$("#fillWarningList").append('<li class="alert alert-warning"><s:text name="correctFormula.currentClrntLoadContainer"><s:param>' + $("#currClrntLoad").text() + '</s:param></s:text></li>');
					$("#fillWarningList").append('<li class="alert alert-warning"><s:text name="correctFormula.exceedMaxLimit"><s:param>' + ozEntered + '</s:param></s:text></li>');
					$("#fillWarningListTitle").text('<s:text name="correctFormula.warningExceedsContainerLimit" />');
				}
				$("#fillWarningListSummary").text('<s:text name="correctFormula.clickContinue2" />');
				$('#fillWarningListOK').focus();
				$('#fillWarningListModal').modal('show');
			}
		} else {
			console.log("product fill OK");
			preDispenseCheck();
		}
	}

	function preDispenseCheck(){
		$("#tinterInProgressTitle").text('<s:text name="global.colorantLevelCheckInProgress" />');
		$("#tinterInProgressMessage").text('<s:text name="global.pleaseWaitClrntLevelCheck" />');
		$("#tinterInProgressModal").modal('show');
		rotateIcon();
		// Get SessionTinter, this is async ajax call so the rest of the logic is in the callback below
		getSessionTinterInfo($("#mainForm_reqGuid").val(),preDispenseCheckCallback);
	}
	
	function preDispenseCheckCallback(){
		// comes from getSessionTinterInfo

		// check if purge required...
		var dateFromString = new Date(sessionTinterInfo.lastPurgeDate);
		var today = new Date();
		if (dateFromString.getFullYear()<today.getFullYear() || dateFromString.getMonth()<today.getMonth() || dateFromString.getDate()<today.getDate()){
			$("#tinterErrorList").empty();
			$("#tinterErrorList").append('<li class="alert alert-danger"><s:text name="global.tinterPurgeIsRequiredLastDoneOnDate"><s:param>' + moment(dateFromString).format('ddd MMM DD YYYY') + "</s:param></s:text></li>");
			waitForShowAndHide("#tinterInProgressModal");
			$("#tinterErrorListModal").modal('show');
			$("#tinterErrorListTitle").text('<s:text name="global.purgeRequired" />');
			$("#tinterErrorListSummary").text('<s:text name="global.saveGoHomeToPurge" />');	
		} else {
			// Check Levels
			console.log("about to check levels");
			// Check for STOP! because there is not enough colorant in the tinter
			var stopList = checkDispenseColorantEmpty(shotList, sessionTinterInfo.canisterList);
			if(stopList[0]!=null){
				$("#tinterErrorList").empty();
				stopList.forEach(function(item){
					$("#tinterErrorList").append('<li class="alert alert-danger">' + item + "</li>");
				});
				//Show it in a modal they can't go on
            	waitForShowAndHide("#tinterInProgressModal");
				$("#tinterErrorListModal").modal('show');
				$("#tinterErrorListTitle").text('<s:text name="global.colorantLevelTooLow" />');
				$("#tinterErrorListSummary").text('<s:text name="global.saveFillGoHomeToUpdateClrnts" />');
				
			} else {
				var warnList = checkDispenseColorantLow(shotList, sessionTinterInfo.canisterList);
				if(warnList[0]!=null){
					$("#tinterWarningList").empty();
					warnList.forEach(function(item){
						$("#tinterWarningList").append('<li class="alert alert-warning">'+item+'</li>');
					});
					//Show in modal, they can say OK to continue
	            	waitForShowAndHide("#tinterInProgressModal");
					$("#tinterWarningListModal").modal('show');
					$("#tinterWarningListTitle").text('<s:text name="global.lowColorantLevels" />');
					
				} else {
					//OK to verify
	            	waitForShowAndHide("#tinterInProgressModal");
					$("#positionContainerModal").modal('show');
				}
			} // end colorant level checks
		} // end purge check
	}

	function decrementColorantLevels(){
		console.log("Calling decrementColorantLevels");
		decrementColorantForDispense($("#mainForm_reqGuid").val(), shotList, decrementCallback);
	}

	function decrementCallback(myPassFail){
		console.log("checking decrement pass/fail " + myPassFail);
		if(myPassFail===true){
			dispense();
		} else {
			// Error Tell the user
			console.log("Write Failed!" + data.errorMessage);
			$("#tinterErrorList").empty();
			$("#tinterErrorList").append('<li class="alert alert-danger"><s:text name="correctFormula.failedDbUpdDecrementCallback" /></li>');
			$("#tinterErrorListTitle").text('<s:text name="correctFormula.internalDatabaseError" />');
			$("#tinterErrorListSummary").text('<s:text name="correctFormula.pleaseRetry" />');
			$("#tinterErrorListModal").modal('show');
		}
	}
	</script>
	<script type="text/javascript"> //dispense
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
	function FMXDispenseProgress(){
		console.log('before dispense progress send');
		
		rotateIcon();
		var cmd = "DispenseProgress";
		var shotList = null;
		var configuration = null;
		var tintermessage = new TinterMessage(cmd,null,null,null,null);  
		var json = JSON.stringify(tintermessage);
		sendingTinterCommand = "true";
		ws_tinter.send(json);
	}
	function dispense(){
		var cmd = "Dispense";
    
    	var tintermessage = new TinterMessage(cmd,shotList,null,null,null);  

    	var json = JSON.stringify(tintermessage);
		sendingDispCommand = "true";
		if(ws_tinter!=null && ws_tinter.isReady=="false") {
    		console.log("WSWrapper connection has been closed (timeout is defaulted to 5 minutes). Make a new WSWrapper.");
    		ws_tinter = new WSWrapper("tinter");
		}
		// Send to tinter
    	ws_tinter.send(json);
	}
	function dispenseProgressResp(return_message){
		
		//$("#progress-message").text(return_message.errorMessage);
		$("#abort-message").show();
		$('#progressok').addClass('d-none');  //hide ok button
		if (return_message.errorMessage.indexOf("done") == -1 && (return_message.errorNumber == 1 ||
				 return_message.status == 1)) {
			$("#tinterProgressList").empty();
			 tinterErrorList = [];
			if(return_message.statusMessages!=null && return_message.statusMessages[0]!=null){
			//keep updating modal with status
				if(return_message.statusMessages.length > 0){
				   buildProgressBars(return_message);
				}
			
			} 
			if(return_message.errorList!=null && return_message.errorList[0]!=null){
				// show errors
				return_message.errorList.forEach(function(item){
						$("#tinterProgressList").append("<li>" + item.message + "</li>");
						tinterErrorList.push(item.message);
					});
			}
			if(return_message.errorMessage !=null) {
				tinterErrorList.push(return_message.errorMessage);
				$("#tinterProgressList").append("<li>" + return_message.errorMessage + "</li>");
			}
			console.log(return_message);
			setTimeout(function(){
				FMXDispenseProgress();
			}, 500);  //send progress request after waiting 200ms.  No need to slam the SWDeviceHandler
			
		}
		else if (return_message.errorMessage.indexOf("done") > 0 || return_message.errorNumber != 0){
			  if(return_message.errorNumber == 4226){
			    	return_message.errorMessage = '<s:text name="global.tinterDriverBusyReinitAndRetry" />';
				    }
			FMXDispenseComplete(return_message);
			
			}
			
	}
	function FMXShowTinterErrorModal(myTitle, mySummary, my_return_message){
	    $("#tinterErrorList").empty();
	    $("#tinterErrorListModal").modal('show');
	    $("#abort-message").hide();
	    processingDispense = false; // allow user to start another dispense after tinter error
	    
		if(my_return_message.statusMessages!=null && my_return_message.statusMessages[0]!=null){
			//keep updating modal with status
				if(my_return_message.statusMessages.length > 0){
				   buildProgressBars(return_message);
				}
			
		} 

	    if(my_return_message.errorNumber == 4226){
	    	my_return_message.errorMessage = '<s:text name="global.tinterDriverBusyReinitAndRetry" />';
		}
	    $("#tinterErrorList").append('<li class="alert alert-danger">' + my_return_message.errorMessage + '</li>');
	    
	    if(myTitle!=null) $("#tinterErrorListTitle").text(myTitle);
	    else $("#tinterErrorListTitle").text('<s:text name="global.tinterError" />');
	    if(mySummary!=null) $("#tinterErrorListSummary").text(mySummary);
	    else $("#tinterErrorListSummary").text("");
	  
	}
	function showTinterErrorModal(myTitle, mySummary, my_return_message){
		$("#tinterErrorList").empty();
		if(my_return_message.errorList!=null && my_return_message.errorList[0]!=null){
			my_return_message.errorList.forEach(function(item){
				$("#tinterErrorList").append('<li class="alert alert-danger">' + item.message + '</li>');
			});
		} else {
			$("#tinterErrorList").append('<li class="alert alert-danger">' + my_return_message.errorMessage + '</li>');
		}
		if(myTitle!=null) $("#tinterErrorListTitle").text(myTitle);
		else $("#tinterErrorListTitle").text('<s:text name="global.tinterError" />');
		if(mySummary!=null) $("#tinterErrorListSummary").text(mySummary);
		else $("#tinterErrorListSummary").text("");
		$("#tinterErrorListModal").modal('show');
	}
	function FMXDispenseComplete(return_message){
		
		buildProgressBars(return_message);
		 $("#abort-message").hide();
			
	    if((return_message.errorNumber == 0 && return_message.commandRC == 0) || (return_message.errorNumber == -10500 && return_message.commandRC == -10500)){
	        // save a dispense (will bump the counter)

	        $("#tinterInProgressDispenseStatus").text("");
	        $("#dispenseStatus").text('<s:text name="global.lastDispenseComplete" />');
	        rotateIcon();
	        //$('#progressok').removeClass('d-none');
	        $('#tinterInProgressTitle').text('<s:text name="global.tinterProgress" />');
	        $('#tinterInProgressMessage').text('');
	        $("#tinterProgressList").empty();
			tinterErrorList = [];
			$(".progress-wrapper").empty();
		
			writeDispense(return_message); // will also send tinter event
			waitForShowAndHide("#tinterInProgressModal");
	    } else {
	        $("#tinterInProgressDispenseStatus").text('<s:text name="correctFormula.lastDispensePlusError"><s:param>'+return_message.errorMessage+'</s:param></s:text>');
	        $("#dispenseStatus").text('<s:text name="correctFormula.lastDispensePlusError"><s:param>'+return_message.errorMessage+'</s:param></s:text>');
	        waitForShowAndHide("#tinterInProgressModal");
	        console.log('hide done');
	        //Show a modal with error message to make sure the user is forced to read it.
	        FMXShowTinterErrorModal("Dispense Error",null,return_message);
	    }
	    sendingTinterCommand = "false";
	}
	function writeDispense(return_message) {
		//Build correction step info and save to DB
		var curDate = new Date();
		// ajax call to convert correctionList to dispenseItems
        var str = { "reqGuid" : $('#mainForm_reqGuid').val(), "jsDateString" : curDate.toString(), "cycle" : $("#mainForm_currCycle").val(), "nextUnitNbr": $("#mainForm_nextUnitNbr").val(),"reason" : $("#reason").val(), "stepStatus": $("#mainForm_stepStatus").val(), "stepMethod":method, "shotList" : shotList};
        var jsonIN = JSON.stringify(str);
        console.log(jsonIN);
        $.ajax({	
            url : "saveCorrectionStepAction.action",
            type: "POST",
            contentType : "application/json; charset=utf-8",
            dataType: "json",
            async: true,
            data : jsonIN,
            success : function(data){
            	processingDispense = false;
				console.log(data);
				if(data.sessionStatus === "expired"){
            		window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
            	}
            	else{
            		if(data.errorMessage==null){
    					console.log("Write Successful!");
    					if(data.mergeCorrWithStartingForm == true){
    						// end of cycle, merge Correction with orig (this will refresh screen)
    						mergeCorrWithStartingForm($("#mainForm_currCycle").val());
    					} else {
    						// not end of cycle just refresh page
    						reloadScreen()
    					}
    					sendingDispCommand = "false";
						// send tinter event (no blocking here)
						var curDate = new Date();
						var myGuid = $( "#mainForm_reqGuid" ).val();
						var teDetail = new TintEventDetail("ORDER NUMBER", $("#controlNbr").text(), 0);
						var tedArray = [teDetail];
						sendTinterEvent(myGuid, curDate, return_message, tedArray);
    					
    					//alert("Write Successful");
    				} else {
    					// Error Tell the user
    					console.log("Write Failed!" + data.errorMessage);
    					$("#tinterErrorList").empty();
    					$("#tinterErrorList").append('<li class="alert alert-danger"><s:text name="correctFormula.failedDbUpdWriteDispense" /></li>');
    					$("#tinterErrorListTitle").text('<s:text name="correctFormula.internalDatabaseError" />');
    					$("#tinterErrorListSummary").text('<s:text name="correctFormula.pleaseRetry" />');
    					$("#tinterErrorListModal").modal('show');
    				}
            	}
            },
            error: function(textStatus, errorThrown ) {
                console.log("JSON Write Correction failed here");
                console.log(textStatus + " " + errorThrown);
				// Error Tell the user
				console.log("Write Failed! " + data.errorMessage);
				$("#tinterErrorList").empty();
				$("#tinterErrorList").append('<li class="alert alert-danger"><s:text name="correctFormula.failedToCallAction" /></li>');
				$("#tinterErrorListTitle").text('<s:text name="correctFormula.internalDatabaseError" />');
				$("#tinterErrorListSummary").text('<s:text name="correctFormula.pleaseRetry" />');
				$("#tinterErrorListModal").modal('show');
            }
        });
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
	function RecdMessage() {
		console.log("Received Message");
		//parse the spectro
		console.log("isReady is " + ws_tinter.isReady + " BTW");
		if(ws_tinter.wserrormsg!=null && ws_tinter.wserrormsg!=""){
			if(sendingDispCommand == "true"){
				// received an error from WSWrapper so we won't get any JSON result
				// Since we are sending a dispense command, show as dispense error
				//Show a modal with error message to make sure the user is forced to read it.
				$("#tinterSocketError").text(ws_tinter.wserrormsg);
				
				waitForShowAndHide("#tinterInProgressModal");
	            
					$("#tinterSocketErrorModal").modal('show');
				
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
					case 'Dispense':
					case 'DispenseProgress':
					case 'Abort':
						var tinterModel = sessionTinterInfo.model; 
						if(tinterModel !=null && tinterModel.startsWith("FM X")){ //only FM X series has purge in progress % done
							dispenseProgressResp(return_message);
						}
						else if ((return_message.errorNumber == 0 && return_message.commandRC == 0) || (return_message.errorNumber == -10500 && return_message.commandRC == -10500)){
							// save a dispense (will bump the counter)
							writeDispense(return_message); // will also send tinter event
							waitForShowAndHide("#tinterInProgressModal");
						} else {
							
							waitForShowAndHide("#tinterInProgressModal");
							//Show a modal with error message to make sure the user is forced to read it.
							showTinterErrorModal("Dispense Error",null,return_message);
							processingDispense = false; // allow user to start another dispense after tinter error
							sendingDispCommand = "false";
							// send tinter event (no blocking here)
							var curDate = new Date();
							var myGuid = $( "#mainForm_reqGuid" ).val();
							var teDetail = new TintEventDetail("ORDER NUMBER", $("#controlNbr").text(), 0);
							var tedArray = [teDetail];
							sendTinterEvent(myGuid, curDate, return_message, tedArray);
						}
						break;
					default:
						//Not an response we expected...
						console.log("Message from different command is junk, throw it out");
				} // end switch statement
			} else {
				console.log("Message is junk, throw it out");
			}
		}
	}
	</script>
	
	<script type="text/javascript"> // document functions
	function reloadScreen(){
		document.getElementById("mainForm").submit();
	}
	

	
	
	//Add collapse classes for duplicate cycle rows
	$(function(){
		
		
		$(document).on("shown.bs.modal", "#skipConfirmModal", function(event){
	        $("#skipConfirmInput").val("");
	        $("#skipConfirmInputError").text("");
	        $("#skipConfirmInput").focus();
	    });
	    
		$(document).on("click", "#skipConfirmOK", function(event){
			event.preventDefault();
			// verify reason input
			var value = $("#skipConfirmInput").val();
			if (value.length>0) {
				skipConfirmOKClick();
			} else {
		        $("#skipConfirmInputError").text('<s:text name="correctFormula.mustEnterAReason" />');
		        $("#skipConfirmInput").select();
			}
		});
		
		$(document).on("click", "#tinterWarningListOK", function(event){
			event.preventDefault();
			event.stopPropagation();
			waitForShowAndHide("#tinterWarningListModal");
			$("#positionContainerModal").modal('show');
		});
		
		$(document).on("click", "#fillWarningListOK", function(event){
			event.preventDefault();
			waitForShowAndHide("#fillWarningListModal");
			preDispenseCheck();
		});
		
		$(document).on("shown.bs.modal", "#positionContainerModal", function(event){
			$("#startDispenseButton").focus();
	    });
		
		$(document).on("click", "#startDispenseButton", function(event){
			if (processingDispense == false) {
				processingDispense = true
				event.preventDefault();
				event.stopPropagation();
				waitForShowAndHide("#positionContainerModal");
				$("#tinterInProgressModal").modal('show');
				rotateIcon();
				$("#tinterInProgressTitle").text('<s:text name="global.dispenseInProgress" />');
				$("#tinterInProgressMessage").text('<s:text name="global.pleaseWaitTinterDispense" />');
				
				
				// Call decrement colorants which will call dispense
				decrementColorantLevels();
			}
		});
		
		// keep < and > characters out of reason input
		$(document).on({
			'keypress blur':function(){
				try{
					if(event.key == ">" || event.key == "<"){
						throw '<s:text name="global.noLtOrGt" />';
					}
					if($(this).val().includes(">") || $(this).val().includes("<")){
						throw '<s:text name="global.invalidEntryLtGt" />';
					}
					$(document).find('#errortxt').remove();
					$(':button').attr('disabled', false);
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
						$(':button').attr('disabled', true);
					}
				}
			}
		}, '#reason');
		
		//Hide unnecessary rows onload
		var dupText = [];
		var cycle = <s:property value="%{cycle}"/>;
		var dupArr = $('.cycles').map(function() {
            return $(this).text();
        }).get();
		dupArr.forEach(function(val, i){
			if($.inArray(val, dupText) === -1){
				console.log("val="+val);
				console.log("cycle="+cycle);
				if($('td[id='+ val +']').length > 1 && val != cycle){
					dupText.push(val);
					$('tr[id='+ val +']').first().attr('style','background-color: rgba(0,0,0,.05);');
					$('td[id='+ val +']').first().each(function(){
						$(this).prepend("<a href='javascript:void(0);' title='%{getText(\'correctFormula.clickToExpandRows\')}' class='slider' style='color: green;'><i id='icon' class='fa fa-plus-circle' style='font-size: 1.4rem;'></i></a>&nbsp;");
						$('tr[id='+ val +']:not(:first)').hide();
					});
				}
			} 
		});
		
		
		//toggle hide/show of collapse rows onclick of plus icon
		$('a.slider').click(function(){
			var idee = $(this).parent().attr('id');
			$('tr[id='+ idee +']:not(:first)').toggle();
			
			if($(this).find('#icon').hasClass('fa-plus-circle')){
				$(this).find('#icon').attr('class','fa fa-minus-circle');
			}else if($(this).find('#icon').hasClass('fa-minus-circle')){
				$(this).find('#icon').attr('class','fa fa-plus-circle');
			}
		});
		
		//Popover closing functionality	
	    $('#reason,#formulaAdditions,#percentAdd,#manualAdd,#cancelAdd').on( 'click' , function(event){
	    	$('.popover').each(function(){
	            $(this).popover('toggle');
	            $('input[data-toggle="popover"]').each(function(){
	            	$(this).removeAttr('data-placement');
	                $(this).removeAttr('data-content');
	                $(this).removeAttr('data-toggle');
	            });
	    	});
	    });
		
		$('#percentCancel').click(function(){
			$('#pct').text('');
			$('#percentPrompt').toggle();
		});
	});
	
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
		
		$('#tinterInProgressModal').one('hide.bs.modal',function(){
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
			<div class="row mt-4 mb-3">
				<div class="col-lg-1 col-md-1 col-sm-0 col-xs-0">
					<s:set var="thisGuid" value="reqGuid" />
				</div>
				<div class="col-lg-10 col-md-10 col-sm-12 col-xs-12">
					<div class="card card-body bg-light">
						<table id="origWrapper" class="table table-sm">
							<tr><td><span class="badge badge-secondary mr-1 mb-1 mt-1" style="font-size: 1.2rem;"><s:text name="correctFormula.colorCorrection"></s:text></span></td></tr>
							<tr>
								<td style="width: 20%;">
 									<h5 class="text-primary"><strong><s:text name="correctFormula.startingFormula"></s:text></strong></h5>
									<p><strong><s:text name="global.jobNumber"></s:text> </strong><span id="controlNbr">${sessionScope[thisGuid].controlNbr}</span></p>
									<p><strong><s:text name="correctFormula.containersColon"></s:text> </strong><span id="quantityDispensed">${sessionScope[thisGuid].quantityDispensed}</span></p>
								</td>
								<td style="width: 50%;">
									<table id="origFormula" class="table">
										<thead>
										<tr>
										<th>${sessionScope[thisGuid].displayFormula.clrntSysId}*COLORANT</th>
										<th>${sessionScope[thisGuid].displayFormula.incrementHdr[0]}</th>
										<th>${sessionScope[thisGuid].displayFormula.incrementHdr[1]}</th>
										<th>${sessionScope[thisGuid].displayFormula.incrementHdr[2]}</th>
										<th>${sessionScope[thisGuid].displayFormula.incrementHdr[3]}</th>
										</tr>
										</thead>
										<tbody>
							   			<s:iterator value="displayFormula.ingredients">
								   			<tr>
								   			<td><s:property value="tintSysId"/>-<s:property value="name"/></td>
											<s:iterator value="increment" status="stat">
													<td><s:property value="top"/></td>
											</s:iterator>
								   			</tr>	
									    </s:iterator>
									    </tbody>
									</table>
								</td>
								<td style="width: 30%;" class="pl-5">
 									<h5 class="text-primary"><strong><s:text name="correctFormula.colorantFillLevels"></s:text></strong></h5>
									<p><strong><s:text name="correctFormula.maximumLoadOzColon"></s:text> </strong><span id="maxClrntLoad"><s:property value="maxClrntLoad"/></span></p>
									<p><strong><s:text name="correctFormula.currentLoadOzColon"></s:text> </strong><span id="currClrntLoad"><s:property value="currClrntLoad"/></span></p>
									<p><strong><s:text name="correctFormula.availableFillOzColon"></s:text> </strong><span id="clrntSpaceAvail"><s:property value="clrntSpaceAvail"/></span></p>
									<s:hidden name="maxLoadType" value="%{maxLoadType}"/>
								</td>
							</tr>
						</table>
					</div>	
				</div>
				<div class="col-lg-1 col-md-1 col-sm-0 col-xs-0">
				</div>
			</div>
			<s:form id="mainForm" action="formulaUserCorrectAction" validate="true"  theme="bootstrap">
				<!-- Correction Attempts -->
				<div class="row mb-3">
					<div class="col-lg-1 col-md-1 col-sm-0 col-xs-0">
 						<s:hidden name="reqGuid" value="%{reqGuid}"/>
 						<s:hidden name="jsDateString" value=""/>
						<s:hidden name="sessionHasTinter" value="%{sessionHasTinter}"/>
 						<s:hidden name="currCycle" value="%{cycle}"/>
 						<s:hidden name="nextUnitNbr" value="%{nextUnitNbr}"/>
 						<s:hidden name="lastStep" value="%{lastStep}"/>
 						<s:hidden name="corrStatus" value="%{corrStatus}"/>
 						<s:hidden name="stepStatus" value="OPEN"/>
 						<s:hidden name="acceptedContNbr" value="%{acceptedContNbr}"/>
 						<s:hidden name="mergeCorrWithStartingForm" value="%{mergeCorrWithStartingForm}"/>
					</div>
					<div class="col-lg-10 col-md-10 col-sm-12 col-xs-12">
						<div class="card card-body bg-light">
						<h5 class="text-primary"><strong><s:text name="correctFormula.correctionAttempts"></s:text></strong></h5>
							<table id="correctionAttempts" class="table table-bordered">
								<thead>
									<tr>
										<th><s:text name="correctFormula.cycle"></s:text></th>
										<th><s:text name="correctFormula.containerNbr"></s:text></th>
										<th><s:text name="correctFormula.step"></s:text></th>
										<th><s:text name="correctFormula.reason"></s:text></th>
										<th><s:text name="correctFormula.ingredients"></s:text></th>
										<th><s:text name="correctFormula.dispensed"></s:text></th>
										<th><s:text name="correctFormula.status"></s:text></th>
									</tr>
								</thead>
								<tbody>
									<s:iterator var="corr" value="correctionHistory" status="outer">
										<tr id="<s:property value="#corr.cycle" />">
											<td class="cycles" id="<s:property value="#corr.cycle" />"><s:property value="#corr.cycle" /></td>
											<td><s:property value="#corr.unitNbr" /></td>
											<td><s:property value="#corr.step" /></td>
											<td><s:property value="#corr.reason" /></td>
											<td>
											<table class="table">
									   			<s:iterator var="clrnt" value="ingredients">
										   			<tr>
										   			<td class="clrntCol"><s:property value="tintSysId"/>-<s:property value="name"/></td>
													<s:iterator value="increment" status="stat">
															<td class="incrCols"><s:property value="top"/></td>
													</s:iterator>
										   			</tr>	
											    </s:iterator>
											</table>
											</td>
											<td><s:property value="#corr.jsDateTime" /></td>
											<td><s:property value="#corr.status" /></td>
										</tr>
									</s:iterator>
								</tbody>
							</table>
						</div>
					</div>
					<div class="col-lg-1 col-md-1 col-sm-0 col-xs-0">
					</div>
				</div>
				<!-- Alerts -->
				<div class="row mb-3">
            		<div class="col-lg-1 col-md-1 col-sm-0 col-xs-0">
					</div>

					<div class="col-lg-10 col-md-10 col-sm-12 col-xs-12">
						<s:if test="hasActionMessages()">
						      <s:actionmessage/>
						</s:if>
					</div>
				</div>
				<!-- Start Choose Action -->
				<div class="row mb-3">	
						<div class="col-lg-1 col-md-1 col-sm-0 col-xs-0"></div>
						<div class="col-lg-10 col-md-10 col-sm-12 col-xs-12">
							<div class="card card-body bg-light">
								<h5 id="currentPrompt" class="text-primary"><strong>
									<s:text name="correctFormula.currentlyCorrectingContainer">
										<s:param><s:property value="%{nextUnitNbr}"/></s:param>
										<s:param>${sessionScope[thisGuid].quantityDispensed}</s:param>
									</s:text></strong>
								</h5>
							<br>
								<div class="d-flex">
									<div class="p-2">
										<button type="button" class="btn btn-primary" id="startNewCycle" onclick="startNewCycleClick()" autofocus="autofocus">
											<s:text name="correctFormula.startNewCycle"></s:text>
										</button>
										<button type="button" class="btn btn-primary" id="addStep" onclick="addStepClick()" autofocus="autofocus">
											<s:text name="correctFormula.addColorant"></s:text>
										</button>
									</div>
									<div class="p-2">
										<button type="button" class="btn btn-secondary" id="acceptContainer" onclick="acceptContainerClick()">
											<s:text name="correctFormula.acceptContainer"></s:text>
										</button>
										<button type="button" class="btn btn-secondary" id="mistintContainer" onclick="mistintContainerClick()">
											<s:text name="correctFormula.mistintContainer"></s:text>
										</button>
										<button type="button" class="btn btn-secondary" id="dispenseAccepted" onclick="dispenseAcceptedClick()">
											<s:text name="correctFormula.dispenseAccepted"></s:text>
										</button>
										<button type="button" class="btn btn-secondary" id="skipContainer" onclick="skipContainerClick()">
											<s:text name="correctFormula.skipContainer"></s:text>
										</button>
									</div>
									<div class="p-2 ml-auto">
										<s:submit cssClass="btn btn-secondary pull-right" value="%{getText('correctFormula.leave')}" action="displayFormulaAction"/>
									</div>
								</div>
			    			</div>
			    		</div>
						<div class="col-lg-1 col-md-1 col-sm-0 col-xs-0"></div>
		    	</div>
		    	<br>
				<!-- Start Add Ingredients -->
				<div class="row" id="addIngredients" data-toggle="collapse">
					<div class="col-lg-1 col-md-1 col-sm-0 col-xs-0">
					</div>
					<div class="col-lg-10 col-md-10 col-sm-12 col-xs-12">
						<div class="card card-body bg-light">
							<h5 class="text-primary"><strong>
								<s:text name="correctFormula.addColorantToContainerNbr">
									<s:param><span id="currentCont">1</span></s:param>
								</s:text></strong>
							</h5>
							<p><strong><s:text name="correctFormula.reasonColon"></s:text></strong>
								<s:textfield class="form-control btn-block" id="reason" placeholder="%{getText('correctFormula.enterCorrectionReason')}" maxlength="100" />
							</p>
							<table>
							<tr>
							<td style="width: 20%;">
								<table id="addColorantActions" class="table">
								<tr><td>
									<button type="button" class="btn btn-secondary btn-block dropdown-toggle" id="manualAdd" data-toggle="dropdown">
										<s:text name="correctFormula.manualAddition"></s:text>
										<span class="caret"></span>
									</button>
									<ul class="dropdown-menu" id="clrntList">
<!-- 											<li><a href="#">B1-Black</a></li> -->
<!-- 											<li><a href="#">G2-New Green</a></li> -->
<!-- 											<li><a href="#">L1-Blue</a></li> -->
									</ul>
								</td></tr>
								<tr><td>
									<button type="button" class="btn btn-secondary btn-block" id="percentAdd" onclick="percentAddClick()">
										<s:text name="correctFormula.percentAddition"></s:text>
									</button>
								</td></tr>
								<tr><td>
									<button type="button" class="btn btn-primary btn-block" id="dispenseAdd" onclick="dispenseAddClick()">
										<s:text name="correctFormula.dispenseAddition"></s:text>
									</button>
								</td></tr>
								<tr><td>
									<button type="button" class="btn btn-secondary btn-block" id="cancelAdd" onclick="cancelAddClick()">
										<s:text name="correctFormula.cancelAddition"></s:text>
									</button>
								</td></tr>
								</table>
							</td>
							<td style="width: 15%;">
							<div id="percentPrompt" data-toggle="collapse" class="p-4">
								<div class="form-group">
									<s:textfield class="number-only form-control input-sm" id="pct" placeholder="%{getText('correctFormula.enterPercent')}" />
								</div>
								<button type="button" class="btn btn-secondary btn-sm" id="percentOk" onclick="percentConfirmClick()">
									<span class="fa fa-check"></span>
								</button>
								<button type="button" class="btn btn-secondary btn-sm" id="percentCancel">
									<span class="fa fa-remove"></span>
								</button>
							</div>
							</td>
							<td style="width: 65%;vertical-align:top">
								<table id="formulaAdditions" class="table">
									<thead>
									<tr>
									<th>${sessionScope[thisGuid].displayFormula.clrntSysId}*COLORANT</th>
									<th>${sessionScope[thisGuid].displayFormula.incrementHdr[0]}</th>
									<th>${sessionScope[thisGuid].displayFormula.incrementHdr[1]}</th>
									<th>${sessionScope[thisGuid].displayFormula.incrementHdr[2]}</th>
									<th>${sessionScope[thisGuid].displayFormula.incrementHdr[3]}</th>
									</tr>
									</thead>
									<tbody>
	<!-- 								<tr> -->
	<!-- 									<td><input type="text" class="form-control"></td> -->
	<!-- 									<td><input type="text" class="form-control" value="0"></td> -->
	<!-- 									<td><input type="text" class="form-control" value="0"></td> -->
	<!-- 									<td><input type="text" class="form-control" value="0"></td> -->
	<!-- 									<td><input type="text" class="form-control" value="0"></td> -->
	<!-- 								</tr> -->
									</tbody>
								</table>
							</td>
							</tr>
							</table>
						</div>
					</div>
					<div class="col-lg-1 col-md-1 col-sm-0 col-xs-0">
					</div>
				</div>	<!-- End Add Ingredients -->
				<br>

			    <!-- Position Container Modal Window -->
			    <div class="modal fade" aria-labelledby="positionContainerModal" aria-hidden="true"  id="positionContainerModal" role="dialog">
			    	<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								<h5 class="modal-title"><s:text name="global.prepareforDispense"></s:text></h5>
								<button type="button" class="close" data-dismiss="modal" aria-label="%{getText('global.close)}" ><span aria-hidden="true">&times;</span></button>
							</div>
							<div class="modal-body">
								<p font-size="4"><s:text name="global.positionContainerandClickStartDispensewhenReady"></s:text></p>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-primary" id="startDispenseButton"><s:text name="global.startDispense"></s:text></button>
							</div>
						</div>
					</div>
				</div>			    

			    <!-- Tinter In Progress Modal Window -->
			    <div class="modal fade" aria-labelledby="tinterInProgressModal" aria-hidden="true"  id="tinterInProgressModal" role="dialog" data-backdrop="static" data-keyboard="false">
			    	<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								<i id="spinner" class="fa fa-refresh mr-3 mt-1 text-muted" style="font-size: 1.5rem;"></i>
								<h5 class="modal-title" id="tinterInProgressTitle"><s:text name="global.dispenseInProgress"></s:text></h5>
								<button type="button" class="close" data-dismiss="modal" aria-label="%{getText('global.close)}" ><span aria-hidden="true">&times;</span></button>
							</div>
							<div class="modal-body">
								<p id="dispenseStatus" font-size="4"></p>
								<p id="tinterInProgressMessage" font-size="4"></p>
								<p id="abort-message" font-size="4" style="display:none;color:purple;font-weight:bold"><s:text name="global.pressF4ToAbort"></s:text></p>
								<ul class="list-unstyled" id="tinterProgressList"></ul> 
								
								<div class="progress-wrapper"></div>
							</div>
							<div class="modal-footer">
							</div>
						</div>
					</div>
				</div>			    

			    <!-- Dispense Error Modal Window -->
			    <div class="modal fade" aria-labelledby="tinterSocketErrorModal" aria-hidden="true"  id="tinterSocketErrorModal" role="dialog">
			    	<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								<h5 class="modal-title"><s:text name="global.dispenseError"></s:text></h5>
								<button type="button" class="close" data-dismiss="modal" aria-label="%{getText('global.close)}" ><span aria-hidden="true">&times;</span></button>
							</div>
							<div class="modal-body">
								<p id="tinterSocketError" font-size="4"></p>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-primary" id="tinterSocketErrorButton" data-dismiss="modal" aria-label="%{getText('global.close)}" >
									<s:text name="global.close"></s:text>
								</button>
							</div>
						</div>
					</div>
				</div>			    

			    <!-- Tinter Error List Modal Window -->
			    <div class="modal fade" aria-labelledby="tinterErrorListModal" aria-hidden="true"  id="tinterErrorListModal" role="dialog">
			    	<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								<h5 class="modal-title" id="tinterErrorListTitle"><s:text name="global.tinterError"></s:text></h5>
								<button type="button" class="close" data-dismiss="modal" aria-label="%{getText('global.close)}" ><span aria-hidden="true">&times;</span></button>
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
								<button type="button" class="btn btn-primary" id="tinterErrorListOK" data-dismiss="modal" aria-label="%{getText('global.close)}" >
									<s:text name="global.ok"></s:text>
								</button>
							</div>
						</div>
					</div>
				</div>			    

			    <!-- Product Fill Warning List Modal Window -->
			    <div class="modal fade" aria-labelledby="fillWarningListModal" aria-hidden="true"  id="fillWarningListModal" role="dialog">
			    	<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								<h5 class="modal-title" id="fillWarningListTitle"><s:text name="correctFormula.productFillError"></s:text></h5>
								<button type="button" class="close" data-dismiss="modal" aria-label="%{getText('global.close)}" ><span aria-hidden="true">&times;</span></button>
							</div>
							<div class="modal-body">
								<div>
									<ul class="p-0" id="fillWarningList" style="list-style: none;">
									</ul>
								</div>
								<p id="fillWarningListSummary" font-size="4"><s:text name="correctFormula.clickContinue1"></s:text></p>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-primary" id="fillWarningListOK" data-dismiss="modal" aria-label="%{getText('global.close)}" >
									<s:text name="global.continue"></s:text>
								</button>
								<button type="button" class="btn btn-secondary" id="fillWarningListCancel" data-dismiss="modal" aria-label="%{getText('global.close)}" >
									<s:text name="global.cancel"></s:text>
								</button>
							</div>
						</div>
					</div>
				</div>			    

			    <!-- Tinter Warning List Modal Window -->
			    <div class="modal fade" aria-labelledby="tinterWarningListModal" aria-hidden="true"  id="tinterWarningListModal" role="dialog">
			    	<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								<h5 class="modal-title" id="tinterWarningListTitle"><s:text name="global.tinterError"></s:text></h5>
								<button type="button" class="close" data-dismiss="modal" aria-label="%{getText('global.close)}"><span aria-hidden="true">&times;</span></button>
							</div>
							<div class="modal-body">
								<div>
									<ul class="p-0" id="tinterWarningList" style="list-style: none;">
									</ul>
								</div>
								<p id="tinterWarningListSummary" font-size="4"><s:text name="global.clickOKtoContinue"></s:text></p>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-primary" id="tinterWarningListOK" data-dismiss="modal" aria-label="%{getText('global.close)}" >
									<s:text name="global.ok"></s:text>
								</button>
								<button type="button" class="btn btn-secondary" id="tinterWarningListCancel" data-dismiss="modal" aria-label="%{getText('global.close)}" >
									<s:text name="global.cancel"></s:text>
								</button>
							</div>
						</div>
					</div>
				</div>			    

			    <!-- Mistint Container Confirmation Modal Window -->
			    <div class="modal fade" aria-labelledby="mistintConfirmModal" aria-hidden="true"  id="mistintConfirmModal" role="dialog">
			    	<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								<h5 class="modal-title" id="mistintConfirmTitle"><s:text name="correctFormula.confirmMistint"></s:text></h5>
								<button type="button" class="close" data-dismiss="modal" aria-label="%{getText('global.close)}"><span aria-hidden="true">&times;</span></button>
							</div>
							<div class="modal-body">
								<p id="mistintConfirmText" font-size="4"><s:text name="correctFormula.containerWillBeDiscarded"></s:text></p>
								<p id="mistintConfirmSummary" font-size="4"><s:text name="correctFormula.clickOKtoConfirmMistintingthisContainer"></s:text></p>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-primary" id="mistintConfirmOK" data-dismiss="modal" aria-label="%{getText('global.close)}" >
									<s:text name="global.ok"></s:text>
								</button>
								<button type="button" class="btn btn-secondary" id="mistintConfirmCancel" data-dismiss="modal" aria-label="%{getText('global.close)}" >
									<s:text name="global.cancel"></s:text>
								</button>
							</div>
						</div>
					</div>
				</div>			    

			    <!-- Skip Container Confirmation Modal Window -->
			    <div class="modal fade" aria-labelledby="skipConfirmModal" aria-hidden="true"  id="skipConfirmModal" role="dialog">
			    	<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								<h5 class="modal-title" id="skipConfirmTitle">
									<s:text name="correctFormula.confirmSkippingThisContainer">
										<s:param><s:property value="%{nextUnitNbr}"/></s:param>
									</s:text>
								</h5>
								<button type="button" class="close" data-dismiss="modal" aria-label="%{getText('global.close)}" ><span aria-hidden="true">&times;</span></button>
							</div>
							<div class="modal-body">
								<p id="skipConfirmText" font-size="4"><s:text name="correctFormula.skippingAContainer"></s:text></p>
								<s:text name="correctFormula.reasonColon"></s:text>
								<input type="text" class="form-control" id="skipConfirmInput" autofocus="autofocus">
								<strong id="skipConfirmInputError" style="color: red"></strong>
								<p id="skipConfirmSummary" font-size="4"><s:text name="correctFormula.enterDescription"></s:text></p>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-primary" id="skipConfirmOK" data-dismiss="modal" aria-label="%{getText('global.close)}" >
									<s:text name="global.ok"></s:text>
								</button>
								<button type="button" class="btn btn-secondary" id="skipConfirmCancel" data-dismiss="modal" aria-label="%{getText('global.close)}" >
									<s:text name="global.cancel"></s:text>
								</button>
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
			</s:form>
		</div>
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
		$(document).ready(function(){
			// init which buttons user can see
			updateButtonDisplay();
			// if middle of cycle, check and possibly auto process next container if it has been skpped or discarded 
			if($("#mainForm_corrStatus").val()=="MIDCYCLE"){
				checkAutoProcessNextCont();
			}

			if($("#mergeCorrWithStartingForm").val()=="true") {
				var prevCycle = <s:property value="%{cycle}"/> - 1;
				$("#mainForm_currCycle").val(prevCycle)
				mergeCorrWithStartingForm();
			}

			// hide add step container
			$("#addIngredients").hide();
			if($("#mainForm_sessionHasTinter").val()=="true"){
				//init comms to device handler for tinter
				ws_tinter = new WSWrapper("tinter");
			}

			$(".number-only").keydown(function (e) {
				// Allow: backspace, delete, tab, escape, enter and .
				if ($.inArray(e.keyCode, [46, 8, 9, 27, 13, 110, 190]) !== -1 ||
					// Allow: Ctrl+A, Command+A
					(e.keyCode === 65 && (e.ctrlKey === true || e.metaKey === true)) || 
					// Allow: home, end, left, right, down, up
					(e.keyCode >= 35 && e.keyCode <= 40)) {
						// let it happen, don't do anything
						return;
				}
				// Ensure that it is a number and stop the keypress
				if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105)) {
					e.preventDefault();
				}
			});
			
			$('html, body').animate({ 
				   scrollTop: $(document).height()-$(window).height()}, 
				   1400, 
				   "easeOutQuint"
			);
		});

		function updateButtonDisplay(){
			$("#mainForm_displayFormulaAction").show(); //Leave button
			
			if($("#mainForm_sessionHasTinter").val()=="true"){
				if($("#mainForm_corrStatus").val()=="NONE"){
					$("#currentPrompt").text('<s:text name="correctFormula.startCorrectingContainer"><s:param><s:property value="%{nextUnitNbr}"/>' +
							'</s:param><s:param>${sessionScope[thisGuid].quantityDispensed}</s:param></s:text>');
					$("#startNewCycle").hide();
					$("#addStep").show();
					$("#acceptContainer").hide();
					$("#mistintContainer").hide();
					$("#dispenseAccepted").hide();
					$("#skipContainer").hide();
				} 
				if($("#mainForm_corrStatus").val()=="NEWCYCLE"){
					var prevCycle = <s:property value="%{cycle}"/> - 1;
					$("#currentPrompt").text('<s:text name="correctFormula.correctionCycleCompleted"><s:param>' + prevCycle + '</s:param></s:text>');
					$("#startNewCycle").show();
					$("#addStep").hide();
					$("#acceptContainer").hide();
					$("#mistintContainer").hide();
					$("#dispenseAccepted").hide();
					$("#skipContainer").hide();
				}
				if($("#mainForm_corrStatus").val()=="MIDUNIT"){
					$("#currentPrompt").text('<s:text name="correctFormula.currentlyCorrectingContainer"><s:param><s:property value="%{nextUnitNbr}"/>' + 
							'</s:param><s:param>${sessionScope[thisGuid].quantityDispensed}</s:param></s:text>');
					$("#startNewCycle").hide();
					$("#addStep").show();
					$("#acceptContainer").show();
					$("#mistintContainer").show();
					$("#dispenseAccepted").hide();
					$("#skipContainer").hide();
				}
				if($("#mainForm_corrStatus").val()=="MIDCYCLE"){
					if($("#mainForm_acceptedContNbr").val()>0){
						$("#currentPrompt").text('<s:text name="correctFormula.chooseAction"><s:param><s:property value="%{nextUnitNbr}"/>' + 
								'</s:param><s:param>${sessionScope[thisGuid].quantityDispensed}</s:param></s:text>');
						$("#startNewCycle").hide();
						$("#addStep").hide();
						$("#acceptContainer").hide();
						$("#mistintContainer").hide();
						$("#dispenseAccepted").show();
						$("#skipContainer").show();
					} else {
						$("#currentPrompt").text('<s:text name="correctFormula.startCorrectingContainer"><s:param><s:property value="%{nextUnitNbr}"/>' +
								'</s:param><s:param>${sessionScope[thisGuid].quantityDispensed}</s:param></s:text>');
						$("#startNewCycle").hide();
						$("#addStep").show();
						$("#acceptContainer").hide();
						$("#mistintContainer").hide();
						$("#dispenseAccepted").hide();
						$("#skipContainer").hide();
					}
				}
				// go get tinter info to load colorant dropdown
				getSessionTinterInfo($("#mainForm_reqGuid").val(),sessionTinterInfoCallback);
			} else {
				$("#currentPrompt").text("");
				$("#addStep").hide();
				$("#acceptContainer").hide();
				$("#mistintContainer").hide();
				$("#skipContainer").hide();
				$("#dispenseAccepted").hide();
			}
		}

		</script>
  		
  		<!-- Including footer -->
		<s:include value="Footer.jsp"></s:include>
		
	</body>
</html>