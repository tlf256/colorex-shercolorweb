<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>

<%@ taglib prefix="s" uri="/struts-tags" %>
 
<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title><s:text name="displaySpectroLog.searchSpectroLog"/></title>
		<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
		<link rel="stylesheet" href="css/bootstrapxtra.css" type="text/css">
		<link rel="stylesheet" href="js/smoothness/jquery-ui.min.css" type="text/css">
		<link rel="stylesheet" href="css/CustomerSherColorWeb.css" type="text/css"> 	
		<link href="https://cdn.datatables.net/1.11.3/css/jquery.dataTables.min.css" rel="StyleSheet">
  		<link rel="stylesheet" href="font-awesome-4.7.0/css/font-awesome.min.css" type="text/css">
		<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="js/jquery-ui.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/popper.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="js/bootstrap.min.js"></script>
 		<script src="https://cdn.datatables.net/1.11.3/js/jquery.dataTables.min.js"></script>
 		<script type="text/javascript" charset="utf-8" src="js/dataTables.buttons.min.js"></script>
 		<script type="text/javascript" charset="utf-8" src="js/buttons.bootstrap4.min.js"></script> 
 		<script type="text/javascript" charset="utf-8" src="js/moment.min.js"></script>
  		<script type="text/javascript" charset="utf-8" src="js/moment-with-locales.min.js"></script>
	 	<script type="text/javascript" charset="utf-8" src="js/datetime-moment.js"></script> 
		<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.5.2.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/DisplaySpectroLog-1.5.2.js"></script>
	</head>
	<body>
		<!-- including Header -->
		<s:include value="Header.jsp"></s:include>
		<s:set var="thisGuid" value="reqGuid" />
		
				
		<div class="container-fluid">
			<div class="row">
				<div class="col-sm-12">
					<div id="spectroLogTableContainer" class="row mt-5 justify-content-center"></div>
				</div>
			</div>
	        <div class="row" id="loadingMsg" style="visibility: hidden;">
		      	<div class="col-sm-3"></div>
		      	<div class="col-sm-6" style="text-align: center;">
					<h3><s:text name="global.loading"/></h3>
		      	</div>
		      	<div class="col-sm-3"></div>
		    </div>
		</div>
		
		<!-- Search Spectro Log Modal -->
		<div class="modal fade mt-5" tabindex="-1" role="dialog" id="spectroSearchModal" data-backdrop="static" >
		  <div class="modal-dialog modal-lg" role="document" style="max-height: 100vh; max-width: 80vh;">
		    <div class="modal-content">
		      <div class="modal-header bg-light">
		        <h5 class="modal-title"><s:text name="displaySpectroLog.searchSpectroLog"/></h5>
		        <button type="button" id="modalCloseBtn" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      <s:form id="spectroSearchForm" action="listSpectroEventsAction">
			      <div class="modal-body" style="max-height: 70vh; overflow-y: auto;">
			        <div class="row">
			      		<div class="col-sm-1"></div>
			      		<div class="col-sm-5 text-danger" id="searchError"></div>
			      		<div class="col-sm-5"></div>
			      		<div class="col-sm-1"></div>
			      	</div>
			        <div class="row">
			      		<div class="col-sm-1"></div>
			      		<div class="col-sm-5">
			      			<strong><s:text name="displaySpectroLog.fromDate"/><s:text name="global.colonDelimiter"/></strong>
			      			<s:textfield class="srchinpt" id="fdate" placeholder="%{getText('displaySpectroLog.anyDate')}" autocomplete="off"></s:textfield>
			      		</div>
			      		<div class="col-sm-5">
			      			<strong><s:text name="displaySpectroLog.toDate"/><s:text name="global.colonDelimiter"/></strong>
			      			<s:textfield class="srchinpt" id="tdate" placeholder="%{getText('displaySpectroLog.anyDate')}" autocomplete="off"></s:textfield>
			      		</div>
			      		<div class="col-sm-1"></div>
			      	</div>
			      	<div class="row" id="spectroFunctionList">
				    	<div class="col-sm-3"></div>
				      	<div class="col-sm-6">
				      		<strong><s:text name="displaySpectroLog.functions"/><s:text name="global.colonDelimiter"/></strong>
				      		<s:select list="spectroCommands" id="spectroCommands" headerKey="-1" headerValue="--" >
				      		</s:select>
				      	</div>
				    	<div class="col-sm-3"></div>
				    </div>
			      </div>
			      <div class="modal-footer">
			      	<s:hidden id="guid" name="reqGuid" value="%{reqGuid}"/>
			      	<s:hidden name="customerId" value="%{sessionMap[reqGuid].customerId}"/>
			      	<s:hidden name="spectroModel" value="%{sessionMap[reqGuid].spectroInfo.model}"/>
			      	<button id="searchbtn" type="button" class="btn btn-primary"><s:text name="displaySpectroLog.searchSpectroLog"/></button>
			      	<s:submit cssClass="btn btn-secondary pull-right" id="cancelBtn" value="%{getText('global.cancel')}" action="userCancelAction"/>
			      </div>
		      </s:form>
		    </div>
		  </div>
		</div>
		<!-- Including footer -->
		<s:include value="Footer.jsp"></s:include>
	</body>
</html>