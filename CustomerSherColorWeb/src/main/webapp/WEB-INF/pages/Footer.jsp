<%@ taglib uri="/struts-tags" prefix="s" %>
<!-- Session Expiration Modal -->
<%-- <s:url var="currentAction" forceAddSchemeHostAndPort="true" includeParams="all" escapeAmp="false"/> --%>
<%-- <s:hidden name="currentActionURL" value="%{currentAction}" /> --%>
<input type="hidden" name="sherLinkURL" value="${sessionScope[thisGuid].sherLinkURL}" id="sherLinkURL">

<script type="text/javascript">
var i18n = [];
i18n['basicFieldValidator.yourDataInputIsInvalid'] = '<s:text name="basicFieldValidator.yourDataInputIsInvalid"/>';
i18n['basicFieldValidator.pleaseEnterData'] = '<s:text name="basicFieldValidator.pleaseEnterData"/>';
i18n['compareColors.chooseFirstSample'] = '<s:text name="compareColors.chooseFirstSample" />';
i18n['colorant.cannotAddOneFullQuart'] = '<s:text name="colorant.cannotAddOneFullQuart"/>';
i18n['colorant.colorantLevelError'] = '<s:text name="colorant.colorantLevelError"/>';
i18n['colorant.errorAddingQuart'] = '<s:text name="colorant.errorAddingQuart"/>';
i18n['colorant.errorSettingClrntFull'] = '<s:text name="colorant.errorSettingClrntFull"/>';
i18n['colorant.errorSubtractingQuart'] = '<s:text name="colorant.errorSubtractingQuart"/>';
i18n['colorant.lessThanOneQuart'] = '<s:text name="colorant.lessThanOneQuart"/>';
i18n['colorant.moveComplete'] = '<s:text name="colorant.moveComplete"/>';
i18n['colorant.purgeFailedColon'] = '<s:text name="colorant.purgeFailedColon"/>';
i18n['dispense.cannotDispenseMoreThanEight'] = '<s:text name="dispense.cannotDispenseMoreThanEight"/>';
i18n['dispense.fillEmptyCanister'] = '<s:text name="dispense.fillEmptyCanister"/>';
i18n['dispense.goHomeToPurge'] = '<s:text name="dispense.goHomeToPurge"/>';
i18n['dispense.noValuesEntered'] = '<s:text name="dispense.noValuesEntered"/>';
i18n['dispense.qtyError'] = '<s:text name="dispense.qtyError"/>';
i18n['displayFormula.tinterPurgeIsRequiredLastDoneOn'] = '<s:text name="displayFormula.tinterPurgeIsRequiredLastDoneOn"/>';
i18n['displayJobs.noJobsAvailable'] = '<s:text name="displayJobs.noJobsAvailable"/>';
i18n['displayStoredMeasurements.noMeasurementsAvailable'] = '<s:text name="displayStoredMeasurements.noMeasurementsAvailable"/>';
i18n['footer.yourSessionExpiredClickLogin'] = '<s:text name="footer.yourSessionExpiredClickLogin"/>';
i18n['global.colorantLevelCheckInProgress'] = '<s:text name="global.colorantLevelCheckInProgress"/>';
i18n['global.colorantLevelTooLow'] = '<s:text name="global.colorantLevelTooLow"/>';
i18n['global.containerFull'] = '<s:text name="global.containerFull"/>';
i18n['global.cookieBanner'] = '<s:text name="global.cookieBanner"/>';
i18n['global.dispenseError'] = '<s:text name="global.dispenseError"/>';
i18n['global.dispenseInProgress'] = '<s:text name="global.dispenseInProgress"/>';
i18n['global.extend'] = '<s:text name="global.extend"/>';
i18n['global.failure'] = '<s:text name="global.failure"/>';
i18n['global.login'] = '<s:text name="global.login"/>';
i18n['global.lastDispense'] = '<s:text name="global.lastDispense"/>';
i18n['global.lastDispenseComplete'] = '<s:text name="global.lastDispenseComplete"/>';
i18n['global.lastDispenseInProgress'] = '<s:text name="global.lastDispenseInProgress"/>';
i18n['global.login'] = '<s:text name="global.login"/>';
i18n['global.lowColorantLevels'] = '<s:text name="global.lowColorantLevels"/>';
i18n['global.notLoggedInReqGuidNotFound'] = '<s:text name="global.notLoggedInReqGuidNotFound"/>';
i18n['global.ok'] = '<s:text name="global.ok"/>';
i18n['global.pleaseWaitClrntLevelCheck'] = '<s:text name="global.pleaseWaitClrntLevelCheck"/>';
i18n['global.positiveNbr'] = '<s:text name="global.positiveNbr"/>';
i18n['global.purgeRequired'] = '<s:text name="global.purgeRequired"/>';
i18n['global.tinterDriverBusyReinitAndRetry'] = '<s:text name="global.tinterDriverBusyReinitAndRetry"/>';
i18n['global.tinterError'] = '<s:text name="global.tinterError"/>';
i18n['global.tinterProgress'] = '<s:text name="global.tinterProgress"/>';
i18n['global.yourSessionExpiresInFiveMin'] = '<s:text name="global.yourSessionExpiresInFiveMin"/>';
i18n['printer.couldNotFindDefaultPdf'] = '<s:text name="printer.couldNotFindDefaultPdf"/>';
i18n['printer.couldNotGeneratePdf'] = '<s:text name="printer.couldNotGeneratePdf"/>';
i18n['tinter.clrntIsLowWarning'] = '<s:text name="tinter.clrntIsLowWarning"/>';
i18n['tinter.couldNotFindCalibFiles'] = '<s:text name="tinter.couldNotFindCalibFiles" />';
i18n['tinter.couldNotFindCalibForColorant'] = '<s:text name="tinter.couldNotFindCalibForColorant"/>';
i18n['tinter.couldNotFindCalibTemplate'] = '<s:text name="tinter.couldNotFindCalibTemplate"/>';
i18n['tinter.couldNotFindDefaultGdata'] = '<s:text name="tinter.couldNotFindDefaultGdata"/>';
i18n['tinter.couldNotFindGdata'] = '<s:text name="tinter.couldNotFindGdata"/>';
i18n['tinter.errorClrntEmpty'] = '<s:text name="tinter.errorClrntEmpty"/>';
i18n['tinter.notEnoughClrntToDisp'] = '<s:text name="tinter.notEnoughClrntToDisp"/>';
i18n['tinterResponse.dispenseComplete'] = '<s:text name="tinterResponse.dispenseComplete"/>';
i18n['tinterResponse.errorDuringDispense'] = '<s:text name="tinterResponse.errorDuringDispense"/>';
i18n['tinterResponse.notDetectedRunColorLink'] = '<s:text name="tinterResponse.notDetectedRunColorLink"/>';
i18n['tinterResponse.notValidCommand'] = '<s:text name="tinterResponse.notValidCommand"/>';
i18n['tinterResponse.noWritebackConfigureColorLink'] = '<s:text name="tinterResponse.noWritebackConfigureColorLink"/>';
i18n['tinterResponse.softwareCurrentlyRunning'] = '<s:text name="tinterResponse.softwareCurrentlyRunning"/>';
i18n['tinterResponse.unableToPurge'] = '<s:text name="tinterResponse.unableToPurge"/>';
i18n['tinterResponse.unableToSaveConfiguration'] = '<s:text name="tinterResponse.unableToSaveConfiguration"/>';
i18n['tinterResponse.unableToSaveConfigureColorLink'] = '<s:text name="tinterResponse.unableToSaveConfigureColorLink"/>';
i18n['wSWrapper.undefinedConnectionError'] = '<s:text name="wSWrapper.undefinedConnectionError"/>';



// english version of santint error for logging
var log_english = [];
log_english['tinterResponse.dispenseComplete'] = 'Dispense job complete';
log_english['tinterResponse.errorDuringDispense'] = 'Error during dispense';
log_english['tinterResponse.notDetectedRunColorLink'] = 'Tinter software not detected, please run ColorLink 3 and re-initialize tinter';
log_english['tinterResponse.notValidCommand'] = 'Message command not recognized as a valid tinter command';
log_english['tinterResponse.noWritebackConfigureColorLink'] = 'Error during dispense: No writeback file found, please configure ColorLink';
log_english['tinterResponse.softwareCurrentlyRunning'] = 'Tinter software is currently running';
log_english['tinterResponse.unableToPurge'] = 'Error during dispense: Unable to purge, no configuration on file';
log_english['tinterResponse.unableToSaveConfiguration'] = 'Error during configuration: Unable to save configuration to disk';
log_english['tinterResponse.unableToSaveConfigureColorLink'] = 'Error during dispense: Unable to save Flink, please configure ColorLink';


</script>

<div class="modal fade" aria-labelledby="sessionModal" aria-hidden="true"  id="sessionModal" role="dialog">
  <div class="modal-dialog modal-sm">
    <div class="modal-content">
      <div class="modal-header">
      	<h5 class="modal-title"><s:text name="footer.sessionExpiration"/></h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="%{getText('global.close')}" ><span aria-hidden="true">&times;</span></button>
      </div>
      <div class="modal-body">
        <p id="sessionModalBody"><s:text name="global.yourSessionExpiresInFiveMin"/></p>
      </div>
      <div class="modal-footer">
<%--       	<button id="sessionModalButton" type="button" class="btn btn-success active" onclick="window.location='<s:property value="currentActionURL"/>';" data-dismiss="modal" aria-label="Close" >Extend</button> --%>
        <button id="sessionModalButton" type="button" class="btn btn-success active" onclick="window.location.reload();" data-dismiss="modal" aria-label="%{getText('global.extend')}" ><s:text name="global.extend"/></button>
        <button type="button" class="btn btn-secondary" data-dismiss="modal" aria-label="%{getText('global.close')}" ><s:text name="global.close"/></button>
      </div>
    </div>
  </div>
</div>
<!-- cookie banner -->
<div id="cookieBanner"></div>
 <!-- Footer -->
<div id="footer" class="footer">
	<div class="container text-center">
		<p class="text-muted">
			<a href="javascript:HF_openSherwin()"><font>www.sherwin-williams.com</font></a> |
			<a href="javascript:HF_openPrivacy()"><font><s:text name="footer.privacyStatement"/></font></a> |
			<a href="javascript:HF_openLegal()"><font><s:text name="footer.legalNotice"/></font></a> |
			&copy; <s:text name="footer.theSherwinWilliamsCompany"><s:param><span id="currentYear"></span></s:param>
					</s:text><!--<span id="currentYear"></span> The Sherwin-Williams Company-->
  		</p>
	</div>
</div>
