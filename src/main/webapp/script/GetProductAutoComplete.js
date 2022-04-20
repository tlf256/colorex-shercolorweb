$(document).ready(function() {
	
	// Assume field CustomerName with id="colorName" and listColors.action is spec'd in struts.xml 
	// request.term in line #15 keyword does not change.

	$("#partialProductNameOrId").autocomplete({
		minLength : 3,
		delay : 500,
		autoFocus : 'true',
		source : function(request, response){
			$.ajax({	
				url : "listProducts.action",
				dataType : "json",
				data : {"partialProductNameOrId" : request.term, "reqGuid" : $('#reqGuid').val()  },
//				success : function(data){
//					response: ($.map(data, function(v,i){
//                        return {
//                            label: v.label,
//                            value: v.value
//                        }}))
//				}
				success : function(data){
					if(data.sessionStatus === "expired"){
                		window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
                	}
                	else{
                		response(data.slice(0,100));
                	}
				}
			});
		}
	});
	
	autocompleteForceProd();
});


// check if the color has a restricted product list
function autocompleteForceProd(){
	$.ajax({	
		url : "checkForceProd.action",
		dataType : "json",
		data : {
			"reqGuid" : $('#reqGuid').val()  
			},
		success : function(data){
			if(data.sessionStatus === "expired") {
        		window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
        	}
        	else {
        		// color has restricted products, display these in dropdown
        		if (data != null && data.forceProd != null && data.forceProd != "") {
	        		$( "#partialProductNameOrId" ).autocomplete( "option", "minLength", 0 );
	        		$( "#partialProductNameOrId" ).focus(function() {
	        			$(this).autocomplete("search", "");
	        		});
	        		$( "#partialProductNameOrId" ).focus();
        		}	
        	}
		}
	});
	
}


