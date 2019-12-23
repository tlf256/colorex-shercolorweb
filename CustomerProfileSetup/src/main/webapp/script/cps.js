/**
 * 
 */
$(document).ready(function(){
	$(document).on({
		"keyup":function(){
			var length = $(this).val().length;
			var maxlimit = 150;
			var remaining;
			if(length > maxlimit){
				$(this).val($(this).val().substring(0, maxlimit));
				remaining = maxlimit - length + 1;
			}else{
				remaining = maxlimit - length;
			}
			$("#charcount").text(remaining + " characters remaining");
		},
		"change":function(){
			$(this).attr("placeholder", "150 Character Limit");
		}
		
	}, ".acctcmmnt");
	
	//character counter for account comment field
	/*$(document).on("keyup", "#acctcomm", function(){
		var length = $(this).val().length;
		var maxlimit = 150;
		var remaining;
		if(length > maxlimit){
			$(this).val($(this).val().substring(0, maxlimit));
			remaining = maxlimit - length + 1;
		}else{
			remaining = maxlimit - length;
		}
		$("#charcount").text(remaining + " characters remaining");
	});*/
});
	
	
	