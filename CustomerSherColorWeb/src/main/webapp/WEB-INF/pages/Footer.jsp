<%@ taglib uri="/struts-tags" prefix="s" %>
<!-- Session Expiration Modal -->
<%-- <s:url var="currentAction" forceAddSchemeHostAndPort="true" includeParams="all" escapeAmp="false"/> --%>
<%-- <s:hidden name="currentActionURL" value="%{currentAction}" /> --%>
<input type="hidden" name="sherLinkURL" value="${sessionScope[thisGuid].sherLinkURL}" id="sherLinkURL">

<script type="text/javascript">
var i18n = [];
i18n['colorant.cannotAddOneFullQuart'] = '<s:text name="colorant.cannotAddOneFullQuart"/>';
i18n['colorant.colorantLevelError'] = '<s:text name="colorant.colorantLevelError"/>';
i18n['colorant.errorAddingQuart'] = '<s:text name="colorant.errorAddingQuart"/>';
i18n['colorant.errorSettingClrntFull'] = '<s:text name="colorant.errorSettingClrntFull"/>';
i18n['colorant.errorSubtractingQuart'] = '<s:text name="colorant.errorSubtractingQuart"/>';
i18n['colorant.lessThanOneQuart'] = '<s:text name="colorant.lessThanOneQuart"/>';
i18n['colorant.moveComplete'] = '<s:text name="colorant.moveComplete"/>';
i18n['colorant.purgeFailedColon'] = '<s:text name="colorant.purgeFailedColon"/>';
i18n['footer.yourSessionExpiredClickLogin'] = '<s:text name="footer.yourSessionExpiredClickLogin"/>';
i18n['global.containerFull'] = '<s:text name="global.containerFull"/>';
i18n['global.extend'] = '<s:text name="global.extend"/>';
i18n['global.login'] = '<s:text name="global.login"/>';
i18n['global.tinterError'] = '<s:text name="global.tinterError"/>';
i18n['global.yourSessionExpiresInFiveMin'] = '<s:text name="global.yourSessionExpiresInFiveMin"/>';

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
