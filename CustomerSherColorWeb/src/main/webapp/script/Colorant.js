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
        var str = {"updateType" : idSubstr,"selectedRowPosition" : $('#key', this).attr('value'), "reqGuid" : $('#reqGuid').val() };
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
                        $('#addmsg').text('Error occurred while adding quart, please contact the Help Desk.');
                        $('#addModal').modal('show');
                    }
                });
            }
            else{
                $('#addmsg').text('Cannot add one full quart, please use Set Full.');
                $('#addModal').modal('show');
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
                        $('#submsg').text('Error occurred while subtracting quart, please contact the Help Desk');
                        $('#subModal').modal('show');
                    }
                });
            }
            else{
                $('#submsg').text('Colorant is less than one quart, cannot subtract one full quart.');
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
                        $('#addmsg').text('Error occurred while attempting to set colorant full, please contact the Help Desk.');
                        $('#addModal').modal('show');
                    }
                });
            }
            else{
                $('#addmsg').text('Container is full.');
                $('#addModal').modal('show');
            } 
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