var jobTable;

$(document).ready(function() {

    
    $('#GetPctForm_userCancelAction').on('click',function(event){
    	//window.alert("row clicked ");
    	
    	//window.alert("job number clicked is " + lookupControlNbr);
    	 $('#percentOfFormula').val(0);
    	document.forms[0].submit();
    });

});


