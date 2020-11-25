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

function displayCookieBanner(){
	var cookieBanner = sessionStorage.getItem("cookieBanner");
	//console.log("displayCookieBanner: cookie banner before request " + cookieBanner);
	
	if(cookieBanner == null){
		console.log("cookieBanner is null");
		var reqGuid = $("[name='reqGuid']").val();
		//console.log("reqGuid = " + reqGuid);
		$.ajax({
			url:"displayCookieBanner.action",
			dataType:"json",
			data:{"reqGuid":reqGuid},
			success:function(data){
				//console.log("displayCookieBanner: cookieBanner = " + data.cookieBanner);
				cookieBanner = data.cookieBanner;
				sessionStorage.setItem("cookieBanner", cookieBanner);
				//console.log("displayCookieBanner: cookie banner after ajax " + cookieBanner);
				if(cookieBanner){
					createCookieBanner();
				}
			},
			error:function(request, status){
				console.log(request + " " + status);
			}
		});
	} else {
		console.log("cookieBanner is not null");
		console.log("sessionStorage cookieBanner = " + cookieBanner);
		if(cookieBanner === "true"){
			console.log("displaying cookie banner");
			createCookieBanner();
		} else {
			var banner = $(document).find('#banner');
			//console.log("displayCookieBanner: cookie banner element = " + banner);
			if(banner != null){
				console.log("removing cookie banner");
				banner.remove();
			}
		}
	}
	
}

function createCookieBanner(){
	var banner = 
		'<div class="row" id="banner" style="background-color:blue;color:white;padding:10px;">'
		+ '<div class="col-sm-1"></div>'
		+ '<div class="col-sm-9">'
			+ '<h6>This site uses cookies and other tracking technologies to improve your browsing experience, analyze site traffic, and improve our marketing efforts. '
			+ 'By clicking OK, you <a href="javascript:HF_openPrivacy()">consent to our use of these technologies</a>.</h6>'
		+ '</div>'
		+ '<div class="col-sm-1">'
			+ '<button type="button" class="btn btn-secondary" id="consent">OK</button>'
		+ '</div>'
		+ '<div class="col-sm-1"></div>'
		+ '</div>';
		
		var div = $(document).find('#cookieBanner');
		
		$(div).prepend(banner);	
}

$(document).ready(function(){
	displayCookieBanner();
	
	$(document).on('click', '#consent', function(){
		//console.log("cookieBanner button has been clicked");
		sessionStorage.removeItem("cookieBanner");
		var cookieBanner;
		//console.log("document ready: sessionStorage cookieBanner: " + sessionStorage.getItem("cookieBanner"));
		var reqGuid = $("[name='reqGuid']").val();
		//console.log("document ready: reqGuid = " + reqGuid);
		//console.log("executing createConsentCookie");
		
		$.ajax({
			url:"createConsentCookie.action",
			dataType:"json",
			data:{"reqGuid":reqGuid},
			success:function(data){
				//console.log("document ready: cookieBanner = " + data.cookieBanner);
				cookieBanner = data.cookieBanner;
				sessionStorage.setItem("cookieBanner", cookieBanner);
				//console.log("document ready: cookie banner after ajax " + cookieBanner);
				displayCookieBanner();
			},
			error:function(request, status){
				console.log(request + " " + status);
			}
		}); // end ajax
		
	}); // end click event
	
});