$(document).ready(function() {

	//Disable autoFocus for Custom Manual or Custom Match selections
	$('input[type=radio]').change(function() {
        if(this.value.match(/^CUSTOM/)) {
        	$( "#partialColorNameOrId" ).autocomplete( "option", "autoFocus", false );
        	console.log("false");
        }
        else{
        	$( "#partialColorNameOrId" ).autocomplete( "option", "autoFocus", true );
        	console.log("true");
        }
    });
	
	// Assume field CustomerName with id="colorName" and listColors.action is spec'd in struts.xml 
	// request.term in line #15 keyword does not change.
	$("#partialColorNameOrId").autocomplete({
		minLength : 3,
		delay : 500,
		autoFocus : true,
		source : function(request, response){
			$.ajax({	
				url : "listColors.action",
				dataType : "json",
				data : {"partialColorNameOrId" : request.term, "selectedCoType" : $('input:radio[name=selectedCoTypes]:checked').val(), "reqGuid" : $('#reqGuid').val() },
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
                		$("#colorData").attr("value",encodeURIComponent(JSON.stringify(data)));
                		//console.log(data);
                		console.log($("#colorData").attr("value"));
                		response(data.slice(0,100));
                	}
				}
			});
		}
	});
});