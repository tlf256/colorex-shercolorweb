

	/* -------------- Change Product --------- */
    

	var newSalesNbr = "";
	var promptForVinylSafe = false;
	var makeVinylSafe = null;
	var userIllum = null;
	var prodFam = null;
	
	
	
	function lookupNewProduct() { 
    	var checked = $('input[name=radioProdChoice]:checked').val();
    	$("#lookupProductNext").blur();
    	$('#partialProductNameOrId').blur();
    	
    	// user hasn't selected a menu item yet, do a product lookup
    	if (checked === undefined){
	    	// hide any action messages that the user clicked through
	    	$('.changeProdError').addClass('d-none');
	    	$("#actionMessageDisplay").empty();
	    	
	    	// get sales number
	    	var partialProductNameOrId = $("#partialProductNameOrId").val();
	    	if (partialProductNameOrId == salesNbr){
	    		// user picked same product so don't go any further
				$("#changeProductModal").modal('hide');
	    	} else {
	    		// check if user is clicking through vinyl warning
	    		var checkVSOverride = true;
	    		if ($("#unavailableForVinylWarning").is(":visible")){
	    			checkVSOverride = false;
	    			$("#unavailableForVinylWarning").addClass("d-none");
	    		}
	    		$("#prodChangeStatusMsg").text(i18n['productChange.checkingOptions']);
	    		
	    		// look up product number, size code, projected curve, vinyl safe status
		    	$.ajax({
					url : "lookupProductOptions.action",
					type : "POST",
					data : {
						reqGuid : myGuid,
						partialProductNameOrId : partialProductNameOrId,
						checkVSOverride : checkVSOverride
					},
					datatype : "json",
					async : true,
					success : function(data) {
						if (data.sessionStatus === "expired") {
							window.location.href = "./invalidLoginAction.action";
						} else {
							$("#prodChangeStatusMsg").text("");
							// in case they are re-doing product lookup, reset the options available and add them back in down below
							$(".nondefaultOptions").addClass("d-none");
							$(".form-check-input").prop("checked", false);
							
							// no action errors, field errors, or action messages
							if (validateMsgs(data) === true){
						        // show menu if they need to override a vinyl safe warning
							    if(data.checkVSOverride){
						    		$("#unavailableForVinylWarning").removeClass("d-none");
						    		$("#lookupProductNext").focus();
					            
						    	// validation passed, build and show menu
							    } else {
							    	newSalesNbr = data.salesNbr;
							    	// manual adjustment is the default option
							    	$("#radioManualAdjustment").prop("checked", true);
							    	
							    	// If either TSF is 0 or they are both the same, we can't adjust tint strength
									if (data.oldTintStrength != 0 && data.newTintStrength != 0 && data.oldTintStrength != data.newTintStrength){
										
										// check for Adjust By Tint Strength And Size option
										if (data.oldSizeCode != data.newSizeCode){
											$(".form-check-input").prop("checked", false);
											$("#radioTintStrengthSize").prop("checked", true);
											$("#optionTintStrengthSize").removeClass("d-none");
											$("#prodChangeTableHeader").text(i18n['productChange.changeInTsAndSize']);
										// otherwise we are only adjusting by tint strength
										} else {
											$(".form-check-input").prop("checked", false);
											$("#radioTintStrength").prop("checked", true);
											$("#optionTintStrength").removeClass("d-none");
											$("#prodChangeTableHeader").text(i18n['productChange.changeInTintStrength']);
										}
										// fill in table values
										$("#oldProdNbr").text(data.oldProdNbr);
										$("#newProdNbr").text(data.newProdNbr);
										$("#oldSizeCode").text(data.oldSizeCode);
										$("#newSizeCode").text(data.newSizeCode);
										$("#oldTintStrength").text(data.oldTintStrength);
										$("#newTintStrength").text(data.newTintStrength);
									}
							    	
									// check for projected curve 
									if (data.ableToReformulate){
										$(".form-check-input").prop("checked", false);
										$("#radioReformulate").prop("checked", true);
										$("#optionReformulate").removeClass("d-none");
										if (data.requireVinylPrompt == true){
											promptForVinylSafe = true;
										}
									}
									// check for color eye reading
									if (data.ableToRematch){
										$(".form-check-input").prop("checked", false);
										$("#radioRematch").prop("checked", true);
										$("#optionRematch").removeClass("d-none");
										if (data.requireVinylPrompt == true){
											promptForVinylSafe = true;
										}
									}
									// check if it's a size change
									if (data.oldProdNbr == data.newProdNbr && data.oldSizeCode != data.newSizeCode){
										// add size code change to radio button menu and set as default
										$(".form-check-input").prop("checked", false);
										$("#radioAdjustSize").prop("checked", true);
										$("#optionAdjustSize").removeClass("d-none");
										
										// fill in table values for size change
										$("#prodChangeTableHeader").text(i18n["productChange.changeInProductSize"]);
										$("#oldProdNbr").text(data.oldProdNbr);
										$("#newProdNbr").text(data.newProdNbr);
										$("#oldSizeCode").text(data.oldSizeCode);
										$("#newSizeCode").text(data.newSizeCode);
										$("#oldTintStrength").text(data.oldTintStrength);
										$("#newTintStrength").text(data.newTintStrength);
										// if there was no db entry, blank it out
										if (data.oldTintStrength == 0){ $("#oldTintStrength").text("--"); }
										if (data.newTintStrength == 0){ $("#newTintStrength").text("--"); }
									}
									
									// only show menu if secondary menus aren't visible (if so, they did multiple async prod lookups 
									// and already moved on to other menus, so ignore the ajax response
									if (!$("#prodChangeTable").is(":visible") && !$("#vinylSafePrompt").is(":visible") && !$("#prodFamilyRow").is(":visible")){
										// display radio button menu 			
										$("#changeProductMenu").removeClass("d-none");
										$("#lookupProductNext").focus();
									}
								}
							}
						}
					},
					error : function(err) {
						alert(i18n['global.failureColon'] + err);
					}
				});
	    	}
    	} else {
    		$("#lookupProductNext").focus();
    		// otherwise check which was selected and route accordingly
	    	switch (checked){
	    	case "doNotAdjust":
	    		// update session and reload
	    		updateProductNoAdjustment(true, null);
	    		break;
	    	case "manualAdjustment":
	    		// update session, go to edit formula page
	    		updateProductNoAdjustment(false, manuallyAdjust);
	    		break;
	    	case "adjustSize":
	    		// update the formula math
	    		adjustFormulaToSize();
	    		break;
	    	case "reformulate":
	    		// reformulate with the color curve
	    		updateProductReformulate();
	    		break;
    		case "rematch":
    			// rematch with the color eye reading
    			updateProductRematch();
    			break;
    		case "tintStrength":
    			// scale formula to new tint strength
    			adjustTintStrength();
    			break;
    		case "tintStrengthSize":
    			// scale formula to new tint strength and size 
    			adjustTintStrengthSize();
    			break;
    		}
    	}
    }
	
	
	// display errors and warnings to override
	function validateMsgs(data){
		var validationPassed = false;
		
		if (data.message === "Unknown Error"){
	    	$("#changeProdCancel").click();
	    	alert(i18n['productChange.somethingWentWrong']);
	    
		// make them redo search to resolve any errors first	
	    } else if (data.actionErrors && data.actionErrors.length !== 0) {
			var div = $('<div class="alert alert-danger changeProdError"></div>');
            div.text(data.actionErrors[0]);
            $("#actionMessageDisplay").append(div);
		
        } else if (data.fieldErrors && Object.keys(data.fieldErrors).length !== 0) {
			$.each(data.fieldErrors, function(fieldName, fieldError) {
	            $.each(fieldError, function(index, errorMsg){
	            	var div = $('<div class="alert alert-danger changeProdError"></div>');
	                div.text(errorMsg);
	                $("#" + fieldName).parent().append(div);
	            });
	        });
		
        // let them click through action messages
	    } else if (data.actionMessages && data.actionMessages.length !== 0) {
	    	data.actionMessages.forEach(function(item) {
	    		var div = $('<div class="alert alert-warning changeProdError"></div>');
                div.text(item);
                $("#actionMessageDisplay").append(div);
	    	});
	    	var p = $('<br><p class="changeProdError" style="font-weight:bold"></p>');
	    	p.text(i18n['processProductAction.clickNext']);
	    	$("#actionMessageDisplay").append(p);
	   
	    } else {
	    	validationPassed = true;
	    }
		
	    return validationPassed;
	}
    
    
	// go to edit formula page if they want to manually adjust
    function manuallyAdjust(){
		$("#formulaUserPrintAction_formulaUserEditAction").click();
    }
    
    
    function updateProductNoAdjustment(reload, callback){
    	$('.changeProdError').addClass('d-none');
    	$("#actionMessageDisplay").empty();
    	$.ajax({
			url : "updateProductNoAdjustment.action",
			type : "POST",
			data : {
				reqGuid : myGuid,
				salesNbr : newSalesNbr
			},
			datatype : "json",
			async : true,
			success : function(data) {
				if (data.sessionStatus === "expired") {
					window.location.href = "./invalidLoginAction.action";
				} else {
					$("#changeProductMenu").addClass("d-none");
					
					// no action errors, field errors, or action messages
					if (validateMsgs(data) === true){
						if (reload){
							// reload the page to show updated product, without having the save prompt
							setFormSubmitting();
							window.location= "displayUpdatedFormulaAction.action?reqGuid="+myGuid;
						}
						if (typeof callback == 'function'){
							callback();
		    			}
					}
				}
			},
			error : function(err) {
				alert(i18n['global.failureColon'] + err);
			}
		});
    }
    
    
    function adjustFormulaToSize(){
    	var numErrors = $(".changeProdError").filter(":visible").length;
    	$("#changeProductMenu").addClass("d-none");
    	
    	// visible table and no errors means user is giving final confirmation, so do the ajax call and update
    	if ($("#prodChangeTable").is(":visible")){
    		$('#prodChangeTable').addClass('d-none');
    		if (numErrors == 0){
    			sizeChangeAjax();
    		}
    	// table is not visible
    	} else {
    		// user just got here, show the table, make them click one more time to confirm
    		if (numErrors == 0){
	    		$('#prodChangeTable').removeClass('d-none');
	    	// user is overriding warnings, so hide warnings and do ajax call again
	    	} else {
	    		// hide action messages that the user clicked through
	        	$('.changeProdError').addClass('d-none');
	        	$("#actionMessageDisplay").empty();
	        	sizeChangeAjax();
    		}
    	}
    }    
    
    
    function sizeChangeAjax(){
    	$("#prodChangeStatusMsg").text(i18n['productChange.sizeStatus']);
		$.ajax({
			url : "updateProductSizeChange.action",
			type : "POST",
			data : {
				reqGuid : myGuid,
				salesNbr : newSalesNbr
			},
			datatype : "json",
			async : true,
			success : function(data) {
				if (data.sessionStatus === "expired") {
					window.location.href = "./invalidLoginAction.action";
				} else {
					$("#changeProductMenu").addClass("d-none");
					$("#prodChangeStatusMsg").text("");

					// no errors or warnings
					if (validateMsgs(data) === true){
				    	// set up the changes not yet saved message and reload page with the updated session info
				    	setFormSubmitting();
						window.location= "displayUpdatedFormulaAction.action?reqGuid="+myGuid;
					}
				}
			},
			error : function(err) {
				alert(i18n['global.failureColon'] + err);
			}
		});
   }
    
    
    function updateProductReformulate(){
    	$('.changeProdError').addClass('d-none');
    	$("#actionMessageDisplay").empty();
    	
    	// we need to prompt for vinyl safe
    	if (promptForVinylSafe == true && makeVinylSafe == null){
    		// show vinyl safe prompt
    		$("#changeProductMenu").addClass("d-none");
    		$("#lookupProductNext").addClass("d-none");
    		$("#vinylSafeYes").removeClass("d-none");
    		$("#vinylSafeYes").focus();
    		$("#vinylSafeNo").removeClass("d-none");
    		$("#vinylSafePrompt").removeClass("d-none");
    	} else  {
    		$("#changeProductMenu").addClass("d-none");
			$("#vinylSafePrompt").addClass("d-none");
			$("#vinylSafeYes").addClass("d-none");
    		$("#vinylSafeNo").addClass("d-none");
    		$("#lookupProductNext").removeClass("d-none");
			
			reformulateAjax();
    	}		
    }
    
    
    
	function reformulateAjax(){
		$("#prodChangeStatusMsg").text(i18n['productChange.reformulatingStatus']);
		$.ajax({
			url : "updateProductReformulate.action",
			type : "POST",
			data : {
				reqGuid : myGuid,
				salesNbr : newSalesNbr, 
				requireVinylPrompt : promptForVinylSafe,
				makeVinylSafe : makeVinylSafe
			},
			datatype : "json",
			async : true,
			success : function(data) {
				if (data.sessionStatus === "expired") {
					window.location.href = "./invalidLoginAction.action";
				} else {
					$("#prodChangeStatusMsg").text("");
										
					// no errors or warnings
					if (validateMsgs(data) === true){
				    	// reset the flags once any action messages are dealt with and we've gotten the updated formula
				    	promptForVinylSafe = false;
				    	makeVinylSafe = null;
				    	// set up the changes not yet saved message and reload page with the updates
				    	setFormSubmitting();
						window.location="displayUpdatedFormulaAction.action?reqGuid="+myGuid;
					}
				}
			},
			error : function(err) {
				alert(i18n['global.failureColon'] + err);
			}
		});
    }
    
        
    	
    function updateProductRematch(){
    	$('.changeProdError').addClass('d-none');
    	$("#actionMessageDisplay").empty();
    	
    	// we need to prompt for vinyl safe
    	if (promptForVinylSafe == true && makeVinylSafe == null){
    		$("#changeProductMenu").addClass("d-none");
    		$("#lookupProductNext").addClass("d-none");
    		$("#vinylSafeYes").removeClass("d-none");
    		$("#vinylSafeYes").focus();
    		$("#vinylSafeNo").removeClass("d-none");
    		$("#vinylSafePrompt").removeClass("d-none"); 
        } else  {
        	// we got their vinyl choice so clear that menu
        	if (makeVinylSafe != null){
	        	$("#vinylSafeYes").addClass("d-none");
	    		$("#vinylSafeNo").addClass("d-none");
	    		$("#vinylSafePrompt").addClass("d-none"); 
	    		$("#lookupProductNext").removeClass("d-none");
	    		$("#lookupProductNext").focus();
        	}
    		
        	// check if they need the illuminants option prompt
        	if ($("#userIllumMenu").is(":visible")){
        		userIllum = $('input[name=radioIllumChoice]:checked').val();
       			if (userIllum != null){
        			rematchAjax(null);
       			}
        	} else if ($("#prodFamilyRow").is(":visible")){
       			// check which product radio button is clicked
        		prodFam = $('input[name=prodFamily]:checked').val();
       			if (prodFam != null){
        			rematchAjax(prodFam);
       			}
        	} else {	
        		if (userIllum == null){
	        		// set up prompt for illum choice 
	        		$("#changeProductMenu").addClass("d-none");
	        		$("#userIllumMenu").removeClass("d-none");
	        		$("#radioDaylight").prop("checked", true);
        		} else {
        			// user is clicking through action messages
        			rematchAjax(prodFam);
        		}
        	}
        }    		
    }
    
    
    
    function rematchAjax(prodFam){
    	$("#userIllumMenu").addClass("d-none");
		$("#prodChangeStatusMsg").text(i18n['productChange.rematchingStatus']);
		$("#prodFamilyRow").addClass("d-none");
		$("#changeProdDialogModal").removeClass("modal-xl");
		$.ajax({
			url : "updateProductRematch.action",
			type : "POST",
			data : {
				reqGuid : myGuid,
				salesNbr : newSalesNbr, 
				requireVinylPrompt : promptForVinylSafe,
				makeVinylSafe : makeVinylSafe,
				userIllum : userIllum,
				selectedProdFam : prodFam
			},
			datatype : "json",
			async : true,
			success : function(data) {
				if (data.sessionStatus === "expired") {
					window.location.href = "./invalidLoginAction.action";
				} else {
					$("#prodChangeStatusMsg").text("");
					
					// no errors or warnings
					if (validateMsgs(data) === true){
						// we need to show the better performance in different base table	
						if (data.formulas != null && Object.keys(data.formulas).length > 0){
					    	$("#bestPerformanceFormula").empty();
					    	$("#prodEnteredFormula").empty();
					    	
					    	$("#betterPerfRadio").prop("checked", true);
							$("#betterPerformanceRow").find(".prodDetail").text(data.colorProdFamilies.bestPerformance.prodNbr);
							$("#betterPerformanceRow").find(".quality").text(data.colorProdFamilies.bestPerformance.quality);
							$("#betterPerformanceRow").find(".base").text(data.colorProdFamilies.bestPerformance.base);
							$("#betterPerformanceRow").find(".deltaE").text(data.colorProdFamilies.bestPerformance.deltaE);
							$("#betterPerformanceRow").find(".contrastRatio").text(data.colorProdFamilies.bestPerformance.contrastRatio);
							$("#betterPerformanceRow").find(".comment").text(data.colorProdFamilies.bestPerformance.comment);
							
							$("#productEnteredRow").find(".prodDetail").text(data.colorProdFamilies.prodEntered.prodNbr);
							$("#productEnteredRow").find(".quality").text(data.colorProdFamilies.prodEntered.quality);
							$("#productEnteredRow").find(".base").text(data.colorProdFamilies.prodEntered.base);
							$("#productEnteredRow").find(".deltaE").text(data.colorProdFamilies.prodEntered.deltaE);
							$("#productEnteredRow").find(".contrastRatio").text(data.colorProdFamilies.prodEntered.contrastRatio);
							$("#productEnteredRow").find(".comment").text(data.colorProdFamilies.prodEntered.comment);
							
							$("#prodFamilyTable").find(".deltaE").each(function(){
								var deltaEVal = parseFloat($(this).text());
								if (!isNaN(deltaEVal) && deltaEVal > 1){
									$(this).css("color","red");
									$(this).css("font-weight","bold");
								}
							});
							
							$("#betterPerfRadio").val(data.colorProdFamilies.bestPerformance.salesNbr);
							$("#prodEnteredRadio").val(data.colorProdFamilies.prodEntered.salesNbr);
							
							data.formulas.bestPerformance.ingredients.forEach(function(item) {
					    		var tr = $('<tr></tr>');
					    		var td = $('<td></td>');
				                td.text(item.tintSysId);
				                tr.append(td);
				                
					    		item.increment.forEach(function(inc){
					    			td = $('<td></td>');
					    			td.text(inc);
					    			tr.append(td);
					    		});
					    		$("#bestPerformanceFormula").append(tr);
					    	});
							
							data.formulas.prodEntered.ingredients.forEach(function(item) {
					    		var tr = $('<tr></tr>');
					    		var td = $('<td></td>');
				                td.text(item.tintSysId);
				                tr.append(td);
				                
					    		item.increment.forEach(function(inc){
					    			td = $('<td></td>');
					    			td.text(inc);
					    			tr.append(td);
					    		});
					    		$("#prodEnteredFormula").append(tr);
					    	});
							$("#changeProdDialogModal").addClass("modal-xl");
							$("#prodFamilyRow").removeClass("d-none");
							$("#lookupProductNext").focus();
					    	
					    } else {
					    	// reset the flags once any action messages are dealt with and we've gotten the updated formula
					    	promptForVinylSafe = false;
					    	makeVinylSafe = null;
					    	prodFam = null;
					    	userIllum = null;
					    	// set up the changes not yet saved message and reload page with the updates
					    	setFormSubmitting();
							window.location="displayUpdatedFormulaAction.action?reqGuid="+myGuid;
						}
					}
				}
			},
			error : function(err) {
				alert(i18n['global.failureColon'] + err);
			}
		});
    }
    
    
    
    function adjustTintStrength(){
    	var numErrors = $(".changeProdError").filter(":visible").length;
    	$("#changeProductMenu").addClass("d-none");
    	
    	// visible table and no errors means user is giving final confirmation, so do the ajax call and update
    	if ($("#prodChangeTable").is(":visible")){
    		$('#prodChangeTable').addClass('d-none');
    		if (numErrors == 0){
    			tintStrengthAjax();
    		}
    	// table is not visible
    	} else {
    		// user just got here, show the table, make them click one more time to confirm
    		if (numErrors == 0){
	    		$('#prodChangeTable').removeClass('d-none');
	    		
	    	// user is overriding warnings, so hide warnings and do ajax call again
	    	} else {
	    		// hide action messages that the user clicked through
	        	$('.changeProdError').addClass('d-none');
	        	$("#actionMessageDisplay").empty();
	        	tintStrengthAjax();
    		}
    	}
    }
    

    function tintStrengthAjax(){
    	var oldTintStrength = $("#oldTintStrength").text();
		var newTintStrength = $("#newTintStrength").text();
		$("#prodChangeStatusMsg").text(i18n['productChange.tintStrengthStatus']);
		
		$.ajax({
			url : "updateProductTintStrength.action",
			type : "POST",
			data : {
				reqGuid : myGuid,
				salesNbr : newSalesNbr,
				oldTintStrength : oldTintStrength,
				newTintStrength : newTintStrength
			},
			datatype : "json",
			async : true,
			success : function(data) {
				if (data.sessionStatus === "expired") {
					window.location.href = "./invalidLoginAction.action";
				} else {
					$("#prodChangeStatusMsg").text("");
					
					// no errors or warnings
					if (validateMsgs(data) === true){
				    	// set up the changes not yet saved message and reload page with the updated session info
				    	setFormSubmitting();
						window.location= "displayUpdatedFormulaAction.action?reqGuid="+myGuid;
					}
				}
			},
			error : function(err) {
				alert(i18n['global.failureColon'] + err);
			}
		});
    }
    
    
    
    function adjustTintStrengthSize(){
    	var numErrors = $(".changeProdError").filter(":visible").length;
    	$("#changeProductMenu").addClass("d-none");
    	
    	// visible table and no errors means user is giving final confirmation, so do the ajax call and update
    	if ($("#prodChangeTable").is(":visible")){
    		$('#prodChangeTable').addClass('d-none');
    		if (numErrors == 0){
    			tintStrengthSizeAjax();
    		}
    	// table is not visible
    	} else {
    		// user just got here, show the table, make them click one more time to confirm
    		if (numErrors == 0){
	    		$('#prodChangeTable').removeClass('d-none');
	    		
	    	// user is overriding warnings, so hide warnings and do ajax call again
	    	} else {
	    		// hide action messages that the user clicked through
	        	$('.changeProdError').addClass('d-none');
	        	$("#actionMessageDisplay").empty();
	        	tintStrengthSizeAjax();
    		}
    	}
    }
    
    
    function tintStrengthSizeAjax(){
    	var oldTintStrength = $("#oldTintStrength").text();
		var newTintStrength = $("#newTintStrength").text();
		var oldSizeCode = $("#oldSizeCode").text();
		var newSizeCode = $("#newSizeCode").text();
		$("#prodChangeStatusMsg").text(i18n['productChange.tintStrengthSizeStatus']);
	
		$.ajax({
			url : "updateProductTintStrengthSize.action",
			type : "POST",
			data : {
				reqGuid : myGuid,
				salesNbr : newSalesNbr,
				oldTintStrength : oldTintStrength,
				newTintStrength : newTintStrength,
				oldSizeCode : oldSizeCode,
				newSizeCode : newSizeCode
			},
			datatype : "json",
			async : true,
			success : function(data) {
				if (data.sessionStatus === "expired") {
					window.location.href = "./invalidLoginAction.action";
				} else {
					$("#prodChangeStatusMsg").text("");
					
					// no errors or warnings
					if (validateMsgs(data) === true){
				    	// set up the changes not yet saved message and reload page with the update session info
				    	setFormSubmitting();
						window.location= "displayUpdatedFormulaAction.action?reqGuid="+myGuid;
					}
				}
			},
			error : function(err) {
				alert(i18n['global.failureColon'] + err);
			}
		});
    }
    
    
 
	$(document).ready(function() {
		// pop up product change modal for custom manual, custom match, and saved measure; 
		// otherwise route user to product screen if using SW or competitive color
		if (colorComp == "CUSTOM"){
			$("#changeProductBtn").attr('onclick', "$('#changeProductModal').modal('show'); return false;");
		}
		
		$('#changeProductModal').on('shown.bs.modal', function () {
			$('#partialProductNameOrId').focus();
		});
		
		$("#partialProductNameOrId").keypress(function(event){
			// submit search if enter key pressed
			if(event.keyCode === 13){
				lookupNewProduct();
				event.preventDefault();
			}
		});
		
		// resets the modal any time they relaunch it or click on product search bar
		$("#partialProductNameOrId").on('focus', function(){
			// remove errors and reset button click to product lookup in case they are redoing a search
			$(".form-check-input").prop("checked", false);
			$('#changeProductMenu').addClass('d-none'); 
			$('.changeProdError').addClass('d-none');
			$('#prodChangeTable').addClass('d-none');
			$("#prodFamilyRow").addClass("d-none");
			$("#userIllumMenu").addClass("d-none");
			$("#vinylSafePrompt").addClass("d-none");
			$("#vinylSafeYes").addClass("d-none");
    		$("#vinylSafeNo").addClass("d-none");
    		$("#lookupProductNext").removeClass("d-none");
    		$("#changeProdDialogModal").removeClass("modal-xl");
    		makeVinylSafe = null;
    		prodFam = null;
    		userIllum = null;
		});
		
		$("#changeProductMenu").find(".form-check-input").keypress(function(event){
			// let user hit enter key to submit radio button option
			if(event.keyCode === 13){
				lookupNewProduct();
				event.preventDefault();
			}
		});
		
		$("#userIllumMenu").find(".form-check-input").keypress(function(event){
			// let user hit enter key to submit radio button option
			if(event.keyCode === 13){
				lookupNewProduct();
				event.preventDefault();
			}
		});
		
		$("#prodFamilyTable").find(".prodFamRadio").keypress(function(event){
			// let user hit enter key to submit prod family option
			if(event.keyCode === 13){
				lookupNewProduct();
				event.preventDefault();
			}
		});
		
		// reset change product modal if they cancel out
		$('#changeProdCancel').on('click', function() {
			$('#changeProductMenu').addClass('d-none'); 
			$('.changeProdError').addClass('d-none');
			$('#prodChangeTable').addClass('d-none');
			$(".nondefaultOptions").addClass("d-none");
			$(".form-check-input").prop("checked", false);
			$("#vinylSafePrompt").addClass("d-none");
			$("#vinylSafeYes").addClass("d-none");
    		$("#vinylSafeNo").addClass("d-none");
    		$("#lookupProductNext").removeClass("d-none");
    		$("#userIllumMenu").addClass("d-none");
    		$("#prodFamilyRow").addClass("d-none");
    		$("#changeProdDialogModal").removeClass("modal-xl");
    		makeVinylSafe = null;
    		prodFam = null;
    		userIllum = null;
		});
		
		$("#vinylSafeYes").on('click', function() {
			makeVinylSafe = true;
			var checked = $('input[name=radioProdChoice]:checked').val();
	    	switch (checked){
	    	case "reformulate":
	    		updateProductReformulate();
	    		break;
    		case "rematch":
    			updateProductRematch();
    			break;
    		}
		});
		
		$("#vinylSafeNo").on('click', function() {
			makeVinylSafe = false;
			var checked = $('input[name=radioProdChoice]:checked').val();
	    	switch (checked){
	    	case "reformulate":
	    		updateProductReformulate();
	    		break;
    		case "rematch":
    			updateProductRematch();
    			break;
    		}
		});
		
		$("#changeProductModal").draggable({
		    handle: ".modal-header"
		}); 
		/*
		// don't let user change the product if the job is already dispensed 
		var qtyDispensed = parseInt($.trim($("#qtyDispensed").text()));
		if (isNaN(qtyDispensed))
			qtyDispensed = 0;
		if (qtyDispensed > 0) {
			$("#changeProductBtn").hide();
		}*/
		
		$('#prodFamilyTable tr').click(function() {
		    $(this).find('input[type=radio]').prop('checked', true);
		});
		
	});
	
	
	
 
    