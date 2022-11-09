/**
 * 
 */
$(document).ready(function(){
	$('#closestColorsNxt').on('click', function(){
		
		$('#intExtForm').submit();
		
		var displayMessage = i18n['closestColors.loadingColors'];
		pleaseWaitModal_show(displayMessage, null);
		
	});
});