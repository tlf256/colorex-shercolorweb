var timeoutWarning, timeoutExpire;

function startSessionTimeoutTimers(){
	//Session Timeout Message after 25 minutes
	var t1 = setTimeout(function(){  
		$('#sessionModalBody').text(i18n['global.yourSessionExpiresInFiveMin']);
//		$('#sessionModalButton').attr('onclick','window.location="' + $('#currentActionURL').val() + '";');
		if (typeof formSubmitting !== 'undefined' && typeof setFormSubmitting() === 'function'){
			$('#sessionModalButton').attr('onclick','setFormSubmitting(); window.location.reload();');
		} else {
			$('#sessionModalButton').attr('onclick','window.location.reload();');
		}	
		$('#sessionModalButton').text(i18n['global.extend']);
		$('#sessionModal').modal('show');
	},1500000);
//	},30000); //testing timer 30 seconds

	//Session Expiration Message after 30 minutes
	var t2 = setTimeout(function(){ 
		$('#sessionModalBody').text(i18n['footer.yourSessionExpiredClickLogin']);
		if (typeof formSubmitting !== 'undefined' && typeof setFormSubmitting() === 'function'){
			$('#sessionModalButton').attr('onclick','setFormSubmitting(); window.location="' + $('#sherLinkURL').val() + '";');
		} else {
			$('#sessionModalButton').attr('onclick','window.location="' + $('#sherLinkURL').val() + '";');
		}
		$('#sessionModalButton').text(i18n['global.login']);
		if($('#sessionModal').css('display') === 'none'){$('#sessionModal').modal('show');}
	},1800000);
//	},60000); //testing 1 minute

	timeoutWarning = t1;
	timeoutExpire = t2;
}

function stopSessionTimeoutTimers(timeoutWarning, timeoutExpire) {
	window.clearTimeout(timeoutWarning);
	window.clearTimeout(timeoutExpire);
}


$( function() { //document onload
	
	startSessionTimeoutTimers();
	
	//Current Date
	$('#currentYear').text((new Date()).getFullYear());
	
	//Allows for select all text on focus for inputs
	$('input').on('focusin',function(){
    	$(this).select();
    });

});

//Use this method for hiding any BS4 Modal - will hide modal if transition takes longer than 2 seconds
function waitForShowAndHide(showString){
	let counter = 0;
	
	if($(showString).data('bs.modal')){
		let intervId = setInterval(function(){
			if(($(showString).data('bs.modal')._isShown && !$(showString).data('bs.modal')._isTransitioning) || (counter >= 2000)){
				$(showString).modal('hide');
				counter = 0;
				clearInterval(intervId);
		    }else {
	        	counter += 100;
			}
		}, 100);
	}
}

//Will return an object containing browser name & version
function getBrowser(){
	var ua= navigator.userAgent, tem, 
    M= ua.match(/(opera|chrome|safari|firefox|msie|trident(?=\/))\/?\s*(\d+)/i) || [];
    if(/trident/i.test(M[1])){
        tem=  /\brv[ :]+(\d+)/g.exec(ua) || [];
        return {name:'IE',version:(tem[1] || '')};
    }
    if(M[1]=== 'Chrome'){
        tem= ua.match(/\b(OPR|Edge)\/(\d+)/);
        if(tem!= null) return {name:tem[1].replace('OPR', 'Opera'),version:tem[2]};
    }
    M= M[2]? [M[1], M[2]]: [navigator.appName, navigator.appVersion, '-?'];
    if((tem= ua.match(/version\/(\d+)/i))!= null) M.splice(1, 1, tem[1]);
    return {name:M[0],version:M[1]};
}