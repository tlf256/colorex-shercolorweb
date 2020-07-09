 //Global Variables
"use strict"; 
var sendingTinterCommand = "false";
 var ws_tinter = new WSWrapper("tinter");
 var tinterErrorList;
 var _rgbArr = [];
 
 var shotList = [];

 
 
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

				var $clone = $("#progress-0")
								.removeClass('d-none')
								.clone();
				$clone.attr("id","progress-" + count);
				var $bar = $clone.children(".progress-bar");
				$bar.attr("id","bar-" + count);
				$bar.attr("aria-valuenow",pct);
				$bar.css("width", pct);
				$clone.css("display", "block");
				var color_rgb = getRGB(color);
//				change color of text based on background color
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
    //dispense
	let cmd = "Dispense";
	let tintermessage = new TinterMessage(cmd,shotList,null,null,null);
	let json = JSON.stringify(tintermessage);
	
    sendingTinterCommand = "true";
    if(ws_tinter && ws_tinter.isReady=="false") {
        console.log("WSWrapper connection has been closed (timeout is defaulted to 5 minutes). Make a new WSWrapper.");
        ws_tinter = new WSWrapper("tinter");
    }
    $("#dispError").text("");
    $("#dispenseStatus").text("Last Dispense: In Progress ");
    rotateIcon();
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
		//$("#progress-message").text(return_message.errorMessage);
			buildProgressBars(return_message);
		
		} else {
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
		    	return_message.errorMessage = "Tinter Driver busy.  Please re-initialize tinter and retry command."
			    }
		FMXDispenseComplete(return_message);
		}
		
}
function FMXShowTinterErrorModal(myTitle, mySummary, my_return_message){
    $("#tinterErrorList").empty();
    $("#tinterErrorListModal").modal('show');
    $("#abort-message").hide();
    startSessionTimeoutTimers();
    if(my_return_message.statusMessages!=null && my_return_message.statusMessages[0]!=null){
    	if(my_return_message.statusMessages.length > 0){
    		buildProgressBars(my_return_message);  // on an abort, for example, we will have a progress update to do.
    	}
    	/*
        my_return_message.errorList.forEach(function(item){
            $("#tinterErrorList").append( '</li>' + item.message + '</li>');
        });
        */
    } 
    $("#tinterErrorList").append('<li class="alert alert-danger">' + my_return_message.errorMessage + '</li>');
    
    if(myTitle!=null) $("#tinterErrorListTitle").text(myTitle);
    else $("#tinterErrorListTitle").text("Tinter Error");
    if(mySummary!=null) $("#tinterErrorListSummary").text(mySummary);
    else $("#tinterErrorListSummary").text("");
  
}
function showTinterErrorModal(myTitle, mySummary, my_return_message){
    $("#tinterErrorList").empty();  
    if(my_return_message.statusMessages!=null && my_return_message.statusMessages[0]!=null){
        my_return_message.statusMessages.forEach(function(item){
            $("#tinterErrorList").append('<li class="alert alert-danger">' + item.message + '</li>');
        });
    } else {
        $("#tinterErrorList").append('<li class="alert alert-danger">' + my_return_message.errorMessage + '</li>');
    }
    if(myTitle!=null) $("#tinterErrorListTitle").text(myTitle);
    else $("#tinterErrorListTitle").text("Tinter Error");
    if(mySummary!=null) $("#tinterErrorListSummary").text(mySummary);
    else $("#tinterErrorListSummary").text("");
    $("#tinterErrorListModal").modal('show');
}
function FMXDispenseComplete(return_message){
	buildProgressBars(return_message);
	$("#abort-message").hide();
    if((return_message.errorNumber == 0 && return_message.commandRC == 0) || (return_message.errorNumber == -10500 && return_message.commandRC == -10500)){
        // save a dispense (will bump the counter)
        getSessionTinterInfo($("#reqGuid").val(),warningCheck);
        $("#dispenseStatus").text("Last Dispense: Complete");
        $('#progressok').removeClass('d-none');
        $('#tinterInProgressTitle').text('Tinter Progress');
        $('#tinterInProgressMessage').text('');
        $("#tinterProgressList").empty();
		tinterErrorList = [];
		if(return_message.statusMessages!=null && return_message.statusMessages[0]!=null){
	        buildProgressBars(return_message);
/* DJM dont think we need this for FMX			return_message.errorList.forEach(function(item){
			//	$("#tinterProgressList").append("<li>" + item.message + "</li>");
				tinterErrorList.push(item.message);
			});
			*/
		} else {
		
			tinterErrorList.push(return_message.errorMessage);
			$("#tinterProgressList").append("<li>" + return_message.errorMessage + "</li>");
		}
    } else {
  	  if(return_message.errorNumber == 4226){
	    	return_message.errorMessage = "Tinter Driver busy.  Please re-initialize tinter and retry command."
		}
        $("#dispenseStatus").text("Last Dispense: "+return_message.errorMessage);
        waitForShowAndHide("#tinterInProgressModal");
        console.log('hide done');
        //Show a modal with error message to make sure the user is forced to read it.
        FMXShowTinterErrorModal("Dispense Error",null,return_message);
    }
    sendingTinterCommand = "false";
}

function dispenseComplete(return_message){
	return_message.command = "Dispense";
	var teDetail = new TintEventDetail("DISPENSE USER", sessionTinterInfo.lastPurgeUser, 0);
    var tedArray = [teDetail];
    let curDate = new Date();
    sendTinterEvent($('#reqGuid').val(), curDate, return_message, tedArray); 
    if((return_message.errorNumber == 0 && return_message.commandRC == 0) || (return_message.errorNumber == -10500 && return_message.commandRC == -10500)){
        // save a dispense (will bump the counter)
        getSessionTinterInfo($("#reqGuid").val(),warningCheck);
        $("#dispenseStatus").text("Last Dispense: Complete");
        $('#progressok').removeClass('d-none');
        $('#tinterInProgressTitle').text('Tinter Progress');
        $('#tinterInProgressMessage').text('');
    } else {
        $("#dispenseStatus").text("Last Dispense: "+return_message.errorMessage);
        waitForShowAndHide("#tinterInProgressModal");
        console.log('hide done');
        //Show a modal with error message to make sure the user is forced to read it.
        showTinterErrorModal("Dispense Error",null,return_message);
    }
    sendingTinterCommand = "false";
    startSessionTimeoutTimers();
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
    
    //Send the tinter event
    if(ws_tinter){
    	console.log("Message is " + ws_tinter.wsmsg);
        console.log("isReady is " + ws_tinter.isReady + "BTW");
    	var return_message=JSON.parse(ws_tinter.wsmsg);
     
    }

    if(ws_tinter && ws_tinter.wserrormsg!=null && ws_tinter.wserrormsg!=""){
        if(sendingTinterCommand == "true"){
            // received an error from WSWrapper so we won't get any JSON result
            // Since we are sending a dispense command, show as dispense error
            $("#dispenseStatus").text("Last Dispense: "+ws_tinter.wserrormsg);
            //Show a modal with error message to make sure the user is forced to read it.
            $("#tinterSocketError").text(ws_tinter.wserrormsg);
            $('#progressok').removeClass('d-none');
            waitForShowAndHide("#tinterInProgressModal");
            startSessionTimeoutTimers();
        	console.log('hide done');
        	$("#tinterSocketErrorModal").modal('show');
        } else {
            console.log("Received unsolicited error " + ws_tinter.wserrormsg);
            // so far this only happens when SWDeviceHandler is not running and we created a new WSWrapper when 
            // page intially loaded.  For now wait until they do a dispense to show the error (no everybody has a tinter)
        }
    } else {
        // is result (wsmsg) JSON?
    	let isTintJSON = false;
        try{
            if(ws_tinter && ws_tinter.wsmsg!=null){
                isTintJSON = true;
            }
        }
        catch(error){
            console.log("Caught error is = " + error);
            console.log("Message is junk, throw it out");
            //console.log("Junk Message is " + ws_tinter.wsmsg);
        }
        if(isTintJSON){
            console.log("in istintJSON return message = ");
            console.log(return_message);
            switch (return_message.command) {
                case 'Dispense':
                case 'DispenseProgress':
                case 'Abort':
                	var tinterModel = $("#tinterModel").val();
					if(tinterModel !=null && tinterModel.startsWith("FM X")){ //only FM X series has purge in progress % done
						dispenseProgressResp(return_message);
					}
					else{
						dispenseComplete(return_message);
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

    //Clearing inputs
    $('.table-bordered input:not([type=hidden])').val('');
}

//pre Dispense
function preDispenseRoutine() {
    shotList = [];
    let inputFound = false;
    let invalidInput = false;
    let numClrntsDispensed = 0;

    //Validate form input values and show popovers if necessary, add shotList values
    $('.myinputs').each(function(){
        if($(this).val() != ""){
            inputFound = true;
            return false;
        }
    });
    
    if(inputFound){
        $('table.table-bordered tr').each(function(){
            let rowInputFound = false;
            const $rowInputs = $(this).find('input');
            
            $rowInputs.each(function(){
                if($(this).val() !== ""){
                    rowInputFound = true;
                    console.log('row input found: ' + rowInputFound);
                    numClrntsDispensed++; 
                    return false;
                }
            });
            
            if(rowInputFound){
            	if(numClrntsDispensed > 0 && numClrntsDispensed < 9){
            		if(validateInput($rowInputs)){
	                    //Setting up utility vars
	                    let rowColorCode = $(this).find('#code').text();
	                    let rowPosition = parseInt($(this).find('#pos').text());
	                    let rowIncrList = [];
	        
	                    $rowInputs.each(function(){
	                    	let num = parseInt(this.value) || 0;
	                        rowIncrList.push(num);
	                    });
	                    
	                    if(rowIncrList.length === 4){
	                    	let str = {"incrementArray" : rowIncrList, "reqGuid" : $('#reqGuid').val() };
	                    	let jsonIN = JSON.stringify(str);
	                        $.ajax({	
	                            url : "dispenseConvertIncrementsAction.action",
	                            type: "POST",
	                            contentType : "application/json; charset=utf-8",
	                            dataType: "json",
	                            async: false,
	                            data : jsonIN,
	                            success : function(data){
	                            	console.log(encodeURI(data));
	                                //Building shotList of Colorants to dispense for row
	                                shotList.push(new Colorant(rowColorCode, data.shots, rowPosition, data.UOM));
	                                console.log(shotList);
	                            },
	                            error: function(textStatus, errorThrown ) {
	                                console.log("JSON dispense failed here");
	                                console.log(textStatus + "" + errorThrown);
	                            }
	                        });
	                    }else {console.log("Row increments empty or incomplete.");}
            		}else {console.log("Invalid inputs found"); invalidInput = true;}
                }else {popupInputError($rowInputs[0],"Cannot dispense more than 8 colorants per dispense. Please re-try with less than 8 colorants.");}
            }
        });
    }
    else{
        console.log("No values given, dispense not executed.");
        $('#tinterInProgressTitle').text('Qty Error');
        $('#tinterInProgressMessage').text('No values entered. Please enter values to dispense colorant.');
        $('#progressok').removeClass('d-none');
		$(".progress-wrapper").empty();
        $("#tinterInProgressModal").modal('show');
    }
    

    if(!invalidInput && shotList.length > 0 && (numClrntsDispensed > 0 && numClrntsDispensed < 9)){
        //Begin predispense Check
        preDispenseCheck();
    }
    else{
        console.log("ShotList empty or more than 8 colorants selected to dispense, dispense not executed.");
    }
}

function validateInput(inputTextArray) {
    let result = true;
    inputTextArray.each(function(){
        if(this.value  && this.value  !== ""){
            if(this.value.match(/^[0-9]\d{0,1}$/) === null){
            	popupInputError(this,"Input must be a positive whole number between 0-99, please re-enter.");
            	result = false;
            }
        }
    });
    
    return result;
}

function popupInputError(input, outputMsg){
	if($('input[data-toggle="popover"]').length === 0){
		console.log("Invalid entries detected.");
		$(input).attr("data-toggle", "popover");
		$(input).attr("data-placement","bottom");
		$(input).attr("data-content", outputMsg);
		$(input).popover({trigger : 'manual'});
		$(input).popover('toggle');
		$('.popover').addClass('popover-danger');
		$('html,body').animate({scrollTop: $(input).offset().top -= 80});
	}
}

function preDispenseCheck(){
    $("#tinterInProgressTitle").text("Colorant Level Check In Progress");
    $("#tinterInProgressMessage").text("Please wait while we Check the Colorant Levels for your tinter...");
	$(".progress-wrapper").empty();
    $("#tinterInProgressModal").modal('show');
    // TODO Get SessionTinter, this is async ajax call so the rest of the logic is in the callback below
    getSessionTinterInfo($("#reqGuid").val(),preDispenseCheckCallback);
}

function preDispenseCheckCallback(){
    let preDispenseCheckFlag = false;
    // comes from getSessionTinterInfo

    // check if purge required...
    let dateFromString = new Date(sessionTinterInfo.lastPurgeDate);
    let today = new Date();
    if (dateFromString.getFullYear()<today.getFullYear() || dateFromString.getMonth()<today.getMonth() || dateFromString.getDate()<today.getDate()){
        $("#tinterErrorList").empty();
        $("#tinterErrorList").append('<li class="alert alert-danger">Tinter Purge is Required. Last done on ' + moment(dateFromString).format('ddd MMM DD YYYY') + "</li>");
        waitForShowAndHide("#tinterInProgressModal");
        $("#tinterErrorListTitle").text("Purge Required");
        $("#tinterErrorListSummary").text("Go to the SherColor Home page to perform Tinter Purge. ");
        $("#tinterErrorListModal").modal('show');
        
        preDispenseCheckFlag = true;
    } else {
        // Check Levels
        console.log("about to check levels");
        // Check for STOP! because there is not enough colorant in the tinter
        let stopList = checkDispenseColorantEmpty(shotList, sessionTinterInfo.canisterList);
        if(stopList[0]!=null){
            $("#tinterErrorList").empty();
            stopList.forEach(function(item){
                $("#tinterErrorList").append('<li class="alert alert-danger">' + item + "</li>");
            });
            //Show it in a modal they can't go on
            waitForShowAndHide("#tinterInProgressModal");
            $("#tinterErrorListTitle").text("Colorant Level Too Low");
            $("#tinterErrorListSummary").text("Fill your empty canister and go to the SherColor Home page to update Colorant Levels. ");
            $("#tinterErrorListModal").modal('show');
            
            preDispenseCheckFlag = true;
        } else {
            let warnList = checkDispenseColorantLow(shotList, sessionTinterInfo.canisterList);
            if(warnList[0]!=null){
                $("#tinterWarningList").empty();
                warnList.forEach(function(item){
                    $("#tinterWarningList").append('<li class="alert alert-warning">'+item+'</li>');
                });
                //Show in modal, they can say OK to continue
                waitForShowAndHide("#tinterInProgressModal");
                console.log('hide done');
                $("#tinterWarningListTitle").text("Low Colorant Levels");
                $("#tinterWarningListModal").modal('show');
            
                
                preDispenseCheckFlag = true;
            } else {
                //OK to verify
            }
        } // end colorant level checks
    } // end purge check

    //Validate predispenseCheck pass, if fail, notify.
    if(!preDispenseCheckFlag){
        //Dispensing if shotList contains values
        if(shotList.length > 0){
            $("#tinterInProgressModal").modal('show');
        	$('#tinterInProgressTitle').text('Dispense in Progress');
        	stopSessionTimeoutTimers(timeoutWarning, timeoutExpire);
            decrementColorantForDispense($('#reqGuid').val(), shotList, decrementCallback);
        }else{ 
            console.log("Shotlist empty, dispense not executed.");
            $('#tinterInProgressTitle').text('Tinter Progress');
            $('#tinterInProgressMessage').text('No values entered. Please enter values to dispense colorant.');
            $('#progressok').removeClass('d-none');
        }
    }
    else{console.log("Predispense Check failed with errors. Dispense not executed.")}
}

function decrementCallback(myPassFail){
    console.log("checking decrement pass/fail " + myPassFail);
    if(myPassFail===true){
        dispense(shotList);
    } else {
        //TODO show error on decrement, 
        waitForShowAndHide("#tinterInProgressModal");
        
        console.log('hide done');
    }
}





function warningCheck() {
    sessionTinterInfo.canisterList.forEach(function(value){
        if (value.currentClrntAmount <= value.fillAlarmLevel) {
            if ($('#warning' + value.clrntCode).hasClass('d-none')) {
                $('#warning' + value.clrntCode).removeClass('d-none');
            }
        }else{
            if (!$('#warning' + value.clrntCode).hasClass('d-none')) {
                $('#warning' + value.clrntCode).addClass('d-none');
            }
        }
    });
}

//Used to rotate loader icon in modals
function rotateIcon(){
	let n = 0;
	let status = document.getElementById('dispenseStatus');
	console.log(status);
	$('#spinner').removeClass('d-none');
	if(status){
		let interval = setInterval(function(){
	    	n += 1;
	    	if(n >= 60000 || status.textContent.indexOf("Complete") >= 0){
	            $('#spinner').addClass('d-none');
	        	clearInterval(interval);
	        }else{
	        	$('#spinner').css("transform","rotate(" + n + "deg)");
	        }
		},5);
	}
}
//Document ON-Load
$(function(){
    getSessionTinterInfo($("#reqGuid").val(),warningCheck);

    //Popover closing functionality
    $('table.table-bordered').on( 'click' , function(event){
    	$('.popover').each(function(){
            $(this).popover('toggle');
            $('input[data-toggle="popover"]').each(function(){
            	$(this).removeAttr('data-placement');
                $(this).removeAttr('data-content');
                $(this).removeAttr('data-toggle');
            });
    	});
    });
    
    
    // Handle/validate form input on click
    $('div #dispense').on( 'click' , function(){
        //Starts chain of functions to do all predispense checks and subsequently dispense if all is well.
        preDispenseRoutine();
    });
    
    //Handle warning notification on-click
    $('#tinterWarningListOK').click(function(event){
    	//Dispensing if shotList contains values
        if(shotList.length > 0){
        	stopSessionTimeoutTimers(timeoutWarning, timeoutExpire);
            decrementColorantForDispense($('#reqGuid').val(), shotList, decrementCallback);
        }else{ 
            console.log("Shotlist empty, dispense not executed.");
            $('#tinterInProgressTitle').text('Qty Error');
            $('#tinterInProgressMessage').text('No values entered. Please enter values to dispense colorant.');
            $('#progressok').removeClass('d-none');
        }
		$(".progress-wrapper").empty();
        $("#tinterInProgressModal").modal('show');
    });
    
    $('#progressok').click(function(){
    	$('#progressok').addClass('d-none');
    });
  
    jQuery(document).on("keydown",fkey);
});