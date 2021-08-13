

	/* -------------- Change Product --------- */
    
	var newSalesNbr = "";
	var promptForVinylSafe = false;
	var makeVinylSafe = null;
	var userIllum = null;
	
	
	
	function lookupNewProduct() { 
    	var checked = $('input[name=radioProdChoice]:checked').val();
    	//console.log("checked: " + checked);
    	$("#lookupProductNext").blur();
    	$('#partialProductNameOrId').blur();
    	
    	// user hasn't selected a menu item yet, do a product lookup
    	if (checked === undefined){
	    	// hide any action messages that the user clicked through
	    	$('.changeProdError').addClass('d-none');
	    	
	    	// get sales number
	    	var partialProductNameOrId = $("#partialProductNameOrId").val();
	    	if (partialProductNameOrId == salesNbr){
	    		// user picked same product so don't go any further
				$("#changeProductModal").modal('hide');
	    	} else {
		    	// look up product number, size code, projected curve
	    		$("#prodChangeStatusMsg").text(i18n['productChange.checkingOptions']);
		    	$.ajax({
					url : "lookupProductOptions.action",
					type : "POST",
					data : {
						reqGuid : myGuid,
						partialProductNameOrId : partialProductNameOrId
					},
					datatype : "json",
					async : true,
					success : function(data) {
						if (data.sessionStatus === "expired") {
							window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
						} else {
							//console.log(data);
							$("#prodChangeStatusMsg").text("");
							// in case they are re-doing product lookup, reset the options available and add them back in down below
							$(".nondefaultOptions").addClass("d-none");
							$(".form-check-input").prop("checked", false);
							
							
							/* check validation */
							
							if (data.message === "Unknown Error"){
						    	$("#changeProdCancel").click();
						    	alert(i18n['productChange.somethingWentWrong']);
						    
					    	// make them redo search to resolve any errors first
							} else if (data.fieldErrors && Object.keys(data.fieldErrors).length !== 0) {
								$.each(data.fieldErrors, function(fieldName, errorMsg) {
						            var div = $('<div class="alert alert-danger changeProdError"></div>');
					                div.text(errorMsg);
					                $("#" + fieldName).parent().append(div);
						        });
							
							// then let them click through action messages
						    } else if (data.actionMessages && data.actionMessages.length !== 0) {
								var div = $('<div class="alert alert-warning changeProdError"></div>');
				                div.text(data.actionMessages[0]);
				                $("#actionMessageDisplay").append(div);
						    	
				            // validation passed, build and show menu
						    } else {
						    	newSalesNbr = data.salesNbr;
						    	// manual adjustment is the default option
						    	$("#radioManualAdjustment").prop("checked", true);
						    	
								// check for projected curve 
								if (data.ableToReformulate){
									$(".form-check-input").prop("checked", false);
									$("#radioReformulate").prop("checked", true);
									$("#optionReformulate").removeClass("d-none");
									//console.log("need to prompt for vinyl safe: " + data.requireVinylPrompt);
									if (data.requireVinylPrompt == true){
										promptForVinylSafe = true;
									}
								}
								/*
								// check for color eye reading
								if (data.ableToRematch){
									$(".form-check-input").prop("checked", false);
									$("#radioRematch").prop("checked", true);
									$("#optionRematch").removeClass("d-none");
									//console.log("need to prompt for vinyl safe: " + data.requireVinylPrompt);
									if (data.requireVinylPrompt == true){
										promptForVinylSafe = true;
									}
								}
								*/
								// check if it's a size change
								if (data.oldProdNbr == data.newProdNbr && data.oldSizeCode != data.newSizeCode){
									// add size code change to radio button menu and set as default
									$(".form-check-input").prop("checked", false);
									$("#radioAdjustSize").prop("checked", true);
									$("#optionAdjustSize").removeClass("d-none");
									
									// fill in table values for size change
									$("#oldProdNbr").text(data.oldProdNbr);
									$("#newProdNbr").text(data.newProdNbr);
									$("#oldSizeCode").text(data.oldSizeCode);
									$("#newSizeCode").text(data.newSizeCode);
								}
								
								// if table is visible they did multiple async prod lookups and already moved on to other menus
								if (!$("#sizeChangeTable").is(":visible")){
									// display radio button menu 			
									$("#changeProductMenu").removeClass("d-none");
									$("#lookupProductNext").focus();
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
	    		updateProductReformulate();
	    		break;
/*    		case "rematch":
    			updateProductRematch();
    			break;*/
    		}
    	}
    }
    
    
    // check the radio button and conditionally do this
    function manuallyAdjust(){
		$("#formulaUserPrintAction_formulaUserEditAction").click();
    }
    
    
    function updateProductNoAdjustment(reload, callback){
    	$('.changeProdError').addClass('d-none');
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
					window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
				} else {
					//console.log(data);
					$("#changeProductMenu").addClass("d-none");
					
					if (data.message === "Unknown Error"){
				    	$("#changeProdCancel").click();
				    	alert(i18n['productChange.somethingWentWrong']);
				    
				    // make them redo search to resolve any errors first	
				    } else if (data.actionErrors && data.actionErrors.length !== 0) {
						var div = $('<div class="alert alert-danger changeProdError"></div>');
		                div.text(data.actionErrors[0]);
		                $("#actionMessageDisplay").append(div);
					
		            // then let them click through action messages
				    } else if (data.actionMessages && data.actionMessages.length !== 0) {
						data.actionMessages.forEach(function(item) {
				    		console.log(item);
				    		var div = $('<div class="alert alert-warning changeProdError"></div>');
			                div.text(item);
			                $("#actionMessageDisplay").append(div);
				    	});
				    	var p = $('<br><p class="changeProdError" style="font-weight:bold"></p>');
				    	p.text(i18n['processProductAction.clickNext']);
				    	$("#actionMessageDisplay").append(p);
					
				    } else {
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
    	if ($("#sizeChangeTable").is(":visible")){
    		$('#sizeChangeTable').addClass('d-none');
    		if (numErrors == 0){
    			sizeChangeAjax();
    		}
    	// table is not visible
    	} else {
    		// user just got here, show the table, make them click one more time to confirm
    		if (numErrors == 0){
	    		$('#sizeChangeTable').removeClass('d-none');
	    	// user is overriding warnings, so hide warnings and do ajax call again
	    	} else {
	    		// hide action messages that the user clicked through
	        	$('.changeProdError').addClass('d-none');
	        	sizeChangeAjax();
    		}
    	}
    }    
    
    
    function sizeChangeAjax(){
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
					window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
				} else {
					//console.log(data);
					$("#changeProductMenu").addClass("d-none");

					if (data.message === "Unknown Error"){
				    	$("#changeProdCancel").click();
				    	alert(i18n['productChange.somethingWentWrong']);
				    
				    // make them redo search to resolve any errors first	
				    } else if (data.actionErrors && data.actionErrors.length !== 0) {
						var div = $('<div class="alert alert-danger changeProdError"></div>');
		                div.text(data.actionErrors[0]);
		                $("#actionMessageDisplay").append(div);
		                
		            // then let them click through action messages
				    } else if (data.actionMessages && data.actionMessages.length !== 0) {
				    	data.actionMessages.forEach(function(item) {
				    		console.log(item);
				    		var div = $('<div class="alert alert-warning changeProdError"></div>');
			                div.text(item);
			                $("#actionMessageDisplay").append(div);
				    	});
				    	var p = $('<br><p class="changeProdError" style="font-weight:bold"></p>');
				    	p.text(i18n['processProductAction.clickNext']);
				    	$("#actionMessageDisplay").append(p);
				   	
				    // no errors or warnings, reload to update session info on page
					} else {
						// set up the changes not yet saved message and reload page with the updates
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
					window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
				} else {
					//console.log(data);
					$("#prodChangeStatusMsg").text("");
					if (data.message === "Unknown Error"){
				    	$("#changeProdCancel").click();
				    	alert(i18n['productChange.somethingWentWrong']);
				    
					// make them redo search to resolve any errors first	
				    } else if (data.fieldErrors && Object.keys(data.fieldErrors).length !== 0) {
						$.each(data.fieldErrors, function(fieldName, errorMsg) {
				            var div = $('<div class="alert alert-danger changeProdError"></div>');
			                div.text(errorMsg);
			                $("#" + fieldName).parent().append(div);
				        });
					
		            // let them click through action messages
				    } else if (data.actionMessages && data.actionMessages.length !== 0) {
				    	data.actionMessages.forEach(function(item) {
				    		console.log(item);
				    		var div = $('<div class="alert alert-warning changeProdError"></div>');
			                div.text(item);
			                $("#actionMessageDisplay").append(div);
				    	});
				    	var p = $('<br><p class="changeProdError" style="font-weight:bold"></p>');
				    	p.text(i18n['processProductAction.clickNext']);
				    	$("#actionMessageDisplay").append(p);
				    	
				    } else {
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
    
        
/*    	
    function updateProductRematch(){
    	$('.changeProdError').addClass('d-none');
    	
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
	    		$("#lookupProductNext").addClass("d-none");
	    		//** set makeVinylSafe back to null after return from ajax and pick product
        	}
    		
        	// check if they need the illuminants option prompt
        	if ($("#userIllumMenu").is(":visible")){
        		userIllum = $('input[name=radioIllumChoice]:checked').val();
       			//console.log("user picked " + userIllum);
       			if (userIllum != null){
        			rematchAjax();
       			}
        	} else {	
        		// set up prompt for illum choice 
        		$("#changeProductMenu").addClass("d-none");
        		$("#userIllumMenu").removeClass("d-none");
        		$("#radioDaylight").prop("checked", true);
        	}
        }    		
    }
    
    
    
    function rematchAjax(){
    	$.ajax({
			url : "updateProductRematch.action",
			type : "POST",
			data : {
				reqGuid : myGuid,
				salesNbr : newSalesNbr, 
				requireVinylPrompt : promptForVinylSafe,
				makeVinylSafe : makeVinylSafe,
				userIllum : userIllum
			},
			datatype : "json",
			async : true,
			success : function(data) {
				if (data.sessionStatus === "expired") {
					window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
				} else {
					//console.log(data);
					$("#userIllumMenu").addClass("d-none");
					
					if (data.message === "Unknown Error"){
				    	$("#changeProdCancel").click();
				    	alert(i18n['productChange.somethingWentWrong']);
				    
					// make them redo search to resolve any errors first	
				    } else if (data.fieldErrors && Object.keys(data.fieldErrors).length !== 0) {
						$.each(data.fieldErrors, function(fieldName, errorMsg) {
				            var div = $('<div class="alert alert-danger changeProdError"></div>');
			                div.text(errorMsg);
			                $("#" + fieldName).parent().append(div);
				        });
					
		            // let them click through action messages
				    } else if (data.actionMessages && data.actionMessages.length !== 0) {
				    	data.actionMessages.forEach(function(item) {
				    		console.log(item);
				    		var div = $('<div class="alert alert-warning changeProdError"></div>');
			                div.text(item);
			                $("#actionMessageDisplay").append(div);
				    	});
				    	var p = $('<br><p class="changeProdError" style="font-weight:bold"></p>');
				    	p.text(i18n['processProductAction.clickNext']);
				    	$("#actionMessageDisplay").append(p);
				    	
				    //** add conditional to check if we need to show table	
				    	
				    } else {
				    	// reset the flags once any action messages are dealt with and we've gotten the updated formula
				    	promptForVinylSafe = false;
				    	makeVinylSafe = null;
				    	// set up the changes not yet saved message and reload page with the updates
				    	//setFormSubmitting();
						//window.location= "displayUpdatedFormulaAction.action?reqGuid="+myGuid);
						
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
					}
				}
			},
			error : function(err) {
				alert(i18n['global.failureColon'] + err);
			}
		});
    }
*/    

 
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
			$('#sizeChangeTable').addClass('d-none');
			$("#userIllumMenu").addClass("d-none");
			
			$("#vinylSafePrompt").addClass("d-none");
			$("#vinylSafeYes").addClass("d-none");
    		$("#vinylSafeNo").addClass("d-none");
    		$("#lookupProductNext").removeClass("d-none");
		});
		
		$("#changeProductMenu").find(".form-check-input").keypress(function(event){
			// let user hit enter key to submit radio button option
			if(event.keyCode === 13){
				lookupNewProduct();
				event.preventDefault();
			}
		});
		
		// reset change product modal if they cancel out
		$('#changeProdCancel').on('click', function() {
			$('#changeProductMenu').addClass('d-none'); 
			$('.changeProdError').addClass('d-none');
			$('#sizeChangeTable').addClass('d-none');
			$(".nondefaultOptions").addClass("d-none");
			$(".form-check-input").prop("checked", false);
			$("#vinylSafePrompt").addClass("d-none");
			$("#vinylSafeYes").addClass("d-none");
    		$("#vinylSafeNo").addClass("d-none");
    		$("#lookupProductNext").removeClass("d-none");
    		//$("#userIllumMenu").addClass("d-none");
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
		
		// don't let user change the product if the job is already dispensed 
		var qtyDispensed = parseInt($.trim($("#qtyDispensed").text()));
		if (isNaN(qtyDispensed))
			qtyDispensed = 0;
		if (qtyDispensed > 0) {
			$("#changeProductBtn").hide();
		}
	});
	
	
	
 
    