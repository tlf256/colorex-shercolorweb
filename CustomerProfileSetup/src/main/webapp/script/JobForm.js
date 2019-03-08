/**
 * 
 */
$(document).ready(function(){	

	$("#locField").focus();
	$("#locField").select();
	
	var valid;
	
	$(".scrnlbl").on({
		"change":function(){
			try{
				var scrnlbl = $.trim($(this).val());
				if(scrnlbl.length > 15){
					throw "Screen label cannot be greater than 15 characters";
				}
				valid = true;
				$(this).removeClass("border-danger");
				$("#jobformerror").text("");
				$("#formerror").text("");
			}catch(msg){
				valid = false;
				$(this).addClass("border-danger");
				$(this).focus();
				$("#jobformerror").text(msg);
				$("html, body").animate({
					scrollTop: $(document.body).offset().top
				}, 2000);
			}
		}
	});
	
	$(".flddefault").on("change", function(){
		try{
			var flddflt = $.trim($(this).val());
			if(flddflt.length > 15){
				throw "Default value cannot be greater than 15 characters";
			}
			valid = true;
			$(this).removeClass("border-danger");
			$("#jobformerror").text("");
			$("#formerror").text("");
		}catch(msg){
			valid = false;
			$(this).addClass("border-danger");
			$(this).focus();
			$("#jobformerror").text(msg);
			$("html, body").animate({
				scrollTop: $(document.body).offset().top
			}, 2000);
		}
	});
	
	$(".required").on("change", function(){
		try{
			if($(this).is(":checked") && $(this).parent().parent().find("input:first").val()==""){
				throw "Screen Label is required";
			}
			valid = true;
			$(this).parent().parent().find("input:first").removeClass("border-danger");
			$("#jobformerror").text("");
			$("#formerror").text("");
		}catch(msg){
			valid = false;
			$(this).parent().parent().find("input:first").addClass("border-danger");
			$("#jobformerror").text(msg);
			$("html, body").animate({
				scrollTop: $(document.body).offset().top
			}, 2000);
		}
	});
	
	$("#next-btn").click(function(){
		try{
			if(valid===false){
				throw "Please fix form error(s):";
			}
			$(".scrnlbl").each(function(i){
				if(!$(this).val() && $(".flddefault").eq(i).val()!=""){
					$(this).addClass("border-danger");
					throw "Please enter a value for Screen Label";
				}else{
					$(this).removeClass("border-danger");
				}
			});
			$("#formerror").text("");
		}catch(msg){
			event.preventDefault();
			$("#formerror").text(msg);
			$("html, body").animate({
				scrollTop: $(document.body).offset().top
			}, 1500);
		}
	});
	
});