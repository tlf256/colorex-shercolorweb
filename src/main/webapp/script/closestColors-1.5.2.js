/**
 * 
 */
$(document).ready(function(){
	$('#btnNxtIntExt').on('click', function(){
		//setTimeout(hideIntExtModal, 1000);
		hideIntExtModal();
		$('#intExtForm').submit();
		
		$('#intExtModal').on('hidden.bs.modal', function(){
			var displayMessage = i18n['closestColors.loadingColors'];
			displayWaitModal(displayMessage, '#waitModal');
		});
	});
});

function showIntExtModal(){
	$('#intExtModal').modal('show');
}

function hideIntExtModal(){
	waitForShowAndHide('#intExtModal')
}

function showProcessModal(){
	$('#processReqModal').modal('show');
	rotateSpinner('#processReqModal');
}