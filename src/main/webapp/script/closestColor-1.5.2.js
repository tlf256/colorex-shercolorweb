/**
 * 
 */
$(document).ready(function(){
	$('#btnNxtIntExt').on('click', function(){
		waitForShowAndHide('#intExtModal');
		$('#intExtForm').submit();
	});
	
	$('#intExtModal').on('hidden.bs.modal', function(){
		setTimeout(showProcessModal, 500);
	});
});

function showIntExtModal(){
	$('#intExtModal').modal('show');
}

function showProcessModal(){
	$('#processReqModal').modal('show');
	rotateSpinner('#processReqModal');
}