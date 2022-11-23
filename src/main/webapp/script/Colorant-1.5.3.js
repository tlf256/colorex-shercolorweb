//Global Variables
 var sendingTinterCommand = "false";
 var ws_tinter = new WSWrapper("tinter");
 var platform = navigator.platform;

$(function(){
    //Initial method calls
    warningCheck();
    warningTab();
    modifyDisplayAmts();

    //Creating percent values foreach row
    $('span.hfive').each(function () { 
        var original = $(this).text();
        $(this).text(parseFloat(original).toFixed(2) + '%');
    });
    
    $('.progress-bar').each(function(){
    	console.log($(this).css('background-color'));
    	if($(this).css('background-color') === 'rgb(255, 255, 255)'){
    		$(this).parent().css('border','.5px solid lightgray');
    	}
    });
    

    //Routine for adding/subtracting quart values
    $('td.actions').on( 'click' , 'button', function(){
        var rowVal = $(this).parent().parent().siblings('[id^=quarts]').attr('id').substring(6);
        var idSubstr = $(this).attr('id').substring(0, 5);
        var curRowAmt = parseInt($('#ounces' + rowVal).text().split("/")[0]);
        var maxRowAmt = parseInt($('#ounces' + rowVal).text().split("/")[1]);
        var position = $('#key', this).attr('value');
        var clrnt = null;
		if(!platform.startsWith("Win")){
			clrnt = $('#color' + rowVal).text().split(" ")[0];
		}
        console.log("COLORANT CODE: " + clrnt);
        
        var str = {
        		"updateType" : idSubstr,
        		"selectedRowPosition" : position,
        		"reqGuid" : $('#reqGuid').val()
        		};
        //move in to position
        move($('#key', this).attr('value'), clrnt);
        if(idSubstr == "addqt"){
            console.log("in add function");
            if((curRowAmt + 32.0) <= maxRowAmt){
                $.ajax({	
                    url : "clrntLevelsUpdateAction.action",
                    dataType : "json",
                    type: "POST",
                    data : str,
                    success : function(data){
                    	if(data.sessionStatus === "expired"){
                    		//Handle expired sessions here
                    		window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
                    	}
                    	else{
                    		//Execute AJAX success here
                    		updateRow(data, rowVal);
                            modifyDisplayAmts();
                            warningCheck();
                            warningTab();
                    	}
                    },
                    error: function(textStatus, errorThrown ) {
                        console.log("Failed in add function here");
                        console.log(textStatus + "" + errorThrown);
                        $('#addmsg').text(i18n['colorant.errorAddingQuart']);
                        $('#addModal').modal('show');
                    }
                });
            }
            else{
            	FMXShowTinterErrorModal(i18n['colorant.colorantLevelError'], i18n['colorant.cannotAddOneFullQuart'], null);
               // $('#addmsg').text('Cannot add one full quart, please use Set Full.');
               // $('#addModal').modal('show');
            } 
        }
        else if(idSubstr == "subqt"){
            console.log("in sub function");
            if((curRowAmt - 32.0) >= 0){
                $.ajax({	
                    url : "clrntLevelsUpdateAction.action",
                    dataType : "json",
                    type: "POST",
                    data : str,
                    success : function(data){
                    	if(data.sessionStatus === "expired"){
                    		window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
                    	}
                    	else{
                    		updateRow(data, rowVal);
                            modifyDisplayAmts();
                            warningCheck();
                            warningTab();
                    	}
                    },
                    error: function(textStatus, errorThrown ) {
                        console.log("Failed in sub function");
                        console.log(textStatus + "" + errorThrown);
                        $('#submsg').text(i18n['colorant.errorSubtractingQuart']);
                        $('#subModal').modal('show');
                    }
                });
            }
            else{
                $('#submsg').text(i18n['colorant.lessThanOneQuart']);
                $('#subModal').modal('show');
            } 
        }
        else if(idSubstr == "setfl"){ 
            if(curRowAmt != null && curRowAmt < maxRowAmt){
                console.log("in full function");
                $.ajax({	
                    url : "clrntLevelsUpdateAction.action",
                    dataType : "json",
                    type: "POST",
                    data : str,
                    success : function(data){
                    	if(data.sessionStatus === "expired"){
                    		window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
                    	}
                    	else{
                    		updateRow(data, rowVal);
                            modifyDisplayAmts();
                            warningCheck();
                            warningTab();
                    	}
                    },
                    error: function(textStatus, errorThrown ) {
                        console.log("Failed in set one full function");
                        console.log(textStatus + "" + errorThrown);
                        $('#addmsg').text(i18n['colorant.errorSettingClrntFull']);
                        $('#addModal').modal('show');
                    }
                });
            }
            else{
                $('#addmsg').text(i18n['global.containerFull']);
                $('#addModal').modal('show');
            } 
        }
        else if(idSubstr = "moveT"){
        	//move function is called first for any button press
        }
        else console.log("Condition failed");
    });

    //Function to update the added/subtracted row
    function updateRow(data, rowVal) {
        var colorList = [];
        var tdList = [];
        colorList = data.tinter.canisterList;
        $('#row' + rowVal + ' td').each(function() { tdList.push($(this)) });
        tdList[0].text(colorList[rowVal].position);
        tdList[1].text(colorList[rowVal].clrntCode + " - " + colorList[rowVal].clrntName);
        tdList[2].text(colorList[rowVal].currentClrntAmount.toFixed(1) + "/" + colorList[rowVal].maxCanisterFill.toFixed(1));
        tdList[3].text((colorList[rowVal].currentClrntAmount/32).toFixed(1) + "/" + (colorList[rowVal].maxCanisterFill/32).toFixed(1));
        var $buttons = tdList[3].children('div');
        tdList[4].append($buttons);
        var percent = ((colorList[rowVal].currentClrntAmount/colorList[rowVal].maxCanisterFill) * 100).toFixed(2);
        console.log(percent);
        tdList[5].find('.progress-bar').css("width", percent + "%");
        tdList[5].find('span.hfive').text(percent + "%");
    }

    //Add warning label if percentage is at or below fillAlarmLevel
    function warningCheck(){
        $('span.hfive').each(function (i, value){
            var flag = false;
            var curId = $(this).attr('id');
            var curAmt = parseInt($('#ounces' + curId).text().split('/')[0]);
            var alarmamt = parseInt($(this).siblings('.fillAlarm').attr('value'));
            var $warningspan = $(this).siblings('#warningdiv').find('#warning');
            if(curAmt <= alarmamt){
                $warningspan.removeClass('d-none');
                console.log("passed condition");
            }
            else flag = true;

            if(flag){
                if(!$warningspan.hasClass('d-none')){
                    $warningspan.addClass('d-none');
                }
            }
        });
    }
    
    //Add notification tab to table if any warnings exist in table
    function warningTab() {
        var counter = 0;
        $('.warning:not(.d-none)').each(function(){
            counter++;
        });

        if(counter > 0){
            $('#warnCount').text(counter);
            if($('.nav-tabs').hasClass('d-none')){
                $('.nav-tabs').removeClass('d-none');
            }
        }
        else if(!$('.nav-tabs').hasClass('d-none')){
            $('.nav-tabs').addClass('d-none');
        }
    }

    //On click "Set All Full", if all colorants are full, display message.
    $('div').on('click', '#setallfull', function () {
        var rows = [];
        $('tr').each(function() { rows.push($(this)) });
        var count = 0;
        console.log(rows.length);
        $('span#hfive').each(function(){
            if (parseInt($(this).text()) == 100.0) {
                console.log("one passed condition");
                counter++;
            }
        });
        if (count == rows.length) {
            console.log("modal shown for all 100%");
        }
    }); 

    //Modify display values of double currentclrntamounts to show as integers

    function modifyDisplayAmts(){
        $('td[id^=ounces],[id^=quarts]').each(function(){
            var textArr = [];
            textArr = $(this).text().split('/');
            var leftInt = parseInt(textArr[0]);
            var rightInt = parseInt(textArr[1]);
            if (textArr != null && leftInt >= 0 && rightInt >= 0) {
                $(this).text(leftInt + ".0" + " / " + rightInt + ".0");
            } else {
                $('#subModal').modal('show');
            }
        });
    }

});
function move(position, clrntCode){
	var tinterModel = $("#colorantLevels_tinterModel").val();
	if(tinterModel.startsWith("FM X")){ // only rotate FM XTinter.
		var cmd = "MoveToFill";
		if(!platform.startsWith("Win")){
			cmd = "Move";
		}
		$("#moveInProgressModal").modal('show');
		rotateIcon();
		var shotList = [];
		shotList.push(new Colorant(clrntCode, 1, position,0));
		var tintermessage = new TinterMessage(cmd,shotList,null,null,null);  
		var json = JSON.stringify(tintermessage);
		sendingTinterCommand = "true";
		if(ws_tinter!=null && ws_tinter.isReady=="false") {
			console.log("WSWrapper connection has been closed (timeout is defaulted to 5 minutes). Make a new WSWrapper.")
			ws_tinter = new WSWrapper("tinter");
		}
		ws_tinter.send(json);
	}
}
function moveComplete(myGuid, curDate,return_message){
	sendTinterEvent(myGuid, curDate, return_message, null);
    waitForShowAndHide("#moveInProgressModal");
	if(return_message.errorNumber == 0 && (platform.startsWith("Win") && return_message.commandRC == 0
		|| !platform.startsWith("Win") && return_message.commandRC == 2)){
		// show success message in alert area
		$("#tinterAlertList").empty();
		$("#tinterAlertList").append("<li>" + i18n["colorant.moveComplete"] + "</li>");
		if($("#tinterAlert").hasClass("d-none")) $("#tinterAlert").removeClass("d-none");
		if($("#tinterAlert").hasClass("alert-danger")) $("#tinterAlert").removeClass("alert-danger");
		if(!$("#tinterAlert").hasClass("alert-success")) $("#tinterAlert").addClass("alert-success");
	} else {
		
		$("#tinterAlertList").empty();
		$("#tinterAlertList").append("<li>" + i18n["colorant.purgeFailedColon"] + return_message.errorMessage + "</li>");
		if($("#tinterAlert").hasClass("d-none")) $("#tinterAlert").removeClass("d-none");
		if(!$("#tinterAlert").hasClass("alert-danger")) $("#tinterAlert").addClass("alert-danger");
		if($("#tinterAlert").hasClass("alert-success")) $("#tinterAlert").removeClass("alert-success");
		//Show a modal with error message to make sure the user is forced to acknowledge it.
	
		FMXShowTinterErrorModal(null,null,return_message);
		
	}
}


function FMXShowTinterErrorModal(myTitle, mySummary, my_return_message){
	
	 $("#tinterErrorList").empty();
	 var counter = 0;
	 var id = setInterval(checkTinterCommand, 200);
   
	 function checkTinterCommand(){
			if(sendingTinterCommand == "false" || counter > 10000){
				clearInterval(id);
				 $("#tinterErrorListModal").modal('show');
			}
			else {
				counter +=200;
			}
		}
   
    
    if(my_return_message != null && my_return_message.errorList!=null && my_return_message.errorList[0]!=null){
        my_return_message.errorList.forEach(function(item){
            $("#tinterErrorList").append( '</li>' + item.message + '</li>');
        });
    } 
    if(my_return_message != null){
    	$("#tinterErrorList").append('<li class="alert alert-danger">' + my_return_message.errorMessage + '</li>');
    }
    if(myTitle!=null) $("#tinterErrorListTitle").text(myTitle);
    else $("#tinterErrorListTitle").text(i18n['global.tinterError']);
    if(mySummary!=null) $("#tinterErrorListSummary").text(mySummary);
    else $("#tinterErrorListSummary").text("");
  
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
function RecdMessage() {
	
	var initErrorList = [];
	console.log("Received Message");
	var curDate = new Date();
	var myGuid = $('#reqGuid').val();
	//console.log("Message is " + ws_tinter.wsmsg);
	if(ws_tinter && ws_tinter.wserrormsg!=null && ws_tinter.wserrormsg != ""){
		if(sendingTinterCommand == "true"){
			// received an error from WSWrapper so we won't get any JSON result (probably no SWDeviceHandler)
			// If we are sending a ReadConfig command don't show any error (localhost has no devices)
			// If we are sending a Detect command, show as detect error
			//TODO Show a modal with error message to make sure the user is forced to read it.
//				$("#detectError").text(ws_tinter.wserrormsg);
//				$("#detectErrorModal").modal('show');
		} else {
			console.log("Received unsolicited error " + ws_tinter.wserrormsg);
			// so far this only happens when SWDeviceHandler is not running on localhost and when we created a new
			// ws wrapper it raises an error while the page is intially loaded.  For now wait until the command is 
			// sent to show the error (not everybody has a tinter)
		}
	} else {
		// is result (wsmsg) JSON?
		var isTintJSON = false;
		try{
			if(ws_tinter && ws_tinter.wsmsg!=null){
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
			switch (return_message.command) {
				case 'MoveToFill':
				case 'Move':
					sendingTinterCommand = "false";
					moveComplete(myGuid,curDate,return_message,null);
					break;
				default:
					//Not an response we expected...
					console.log("Message from different command is junk, throw it out");
			}
		} else {
			console.log("Message is junk, throw it out");
		}
	}
}