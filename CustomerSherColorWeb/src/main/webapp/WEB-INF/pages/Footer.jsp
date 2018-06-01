<%@ taglib uri="/struts-tags" prefix="s" %>
<!-- Session Expiration Modal -->
<%-- <s:url var="currentAction" forceAddSchemeHostAndPort="true" includeParams="all" escapeAmp="false"/> --%>
<%-- <s:hidden name="currentActionURL" value="%{currentAction}" /> --%>
<input type="hidden" name="sherLinkURL" value="${sessionScope[thisGuid].sherLinkURL}" id="sherLinkURL">

<div class="modal fade" aria-labelledby="sessionModal" aria-hidden="true"  id="sessionModal" role="dialog">
  <div class="modal-dialog modal-sm">
    <div class="modal-content">
      <div class="modal-header">
      	<h5 class="modal-title">Session Expiration</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close" ><span aria-hidden="true">&times;</span></button>
      </div>
      <div class="modal-body">
        <p id="sessionModalBody">Your session will expire in 5 minutes, if you would like to extend your session please click Extend.</p>
      </div>
      <div class="modal-footer">
<%--       	<button id="sessionModalButton" type="button" class="btn btn-success active" onclick="window.location='<s:property value="currentActionURL"/>';" data-dismiss="modal" aria-label="Close" >Extend</button> --%>
        <button id="sessionModalButton" type="button" class="btn btn-success active" onclick="window.location.reload();" data-dismiss="modal" aria-label="Close" >Extend</button>
        <button type="button" class="btn btn-secondary" data-dismiss="modal" aria-label="Close" >Close</button>
      </div>
    </div>
  </div>
</div>
  
 <!-- Footer -->
<div id="footer" class="footer">
	<div class="container text-center">
		<p class="text-muted">
 			<a href="javascript:HF_openSherwin()"><font>www.sherwin-williams.com</font></a> |
			<a href="javascript:HF_openPrivacy()"><font>Privacy Statement</font></a> |
			<a href="javascript:HF_openLegal()"><font>Legal Notice</font></a> |
			&copy; <span id="currentYear"></span> The Sherwin-Williams Company
  		</p>
	</div>
</div>
