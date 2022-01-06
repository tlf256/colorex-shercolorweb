<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>


<%@ taglib prefix="s" uri="/struts-tags" %>
<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title><s:text name="welcome.tintQueue"/></title>
			<!-- JQuery -->
		<link rel="stylesheet" href="css/dataTables.bootstrap4.min.css" type="text/css">
		<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
		<link rel="stylesheet" href="css/bootstrapxtra.css" type="text/css">
		<link rel="stylesheet" href="js/smoothness/jquery-ui.min.css" type="text/css">
		<link rel="stylesheet" href="css/buttons.bootstrap4.min.css" type="text/css">
		<link rel="stylesheet" href="css/joblist_datatable.css" type="text/css">
		<link rel="stylesheet" href="css/CustomerSherColorWeb.css" type="text/css">
		<link rel="stylesheet" href="font-awesome-4.7.0/css/font-awesome.min.css" type="text/css">
		<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/jquery-ui.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/popper.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/bootstrap.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="js/jquery.dataTables.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="js/dataTables.bootstrap4.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="js/dataTables.buttons.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="js/buttons.bootstrap4.min.js"></script> 
		<script type="text/javascript" charset="utf-8" src="js/buttons.colVis.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="js/buttons.html5.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="js/buttons.print.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="js/jszip.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="js/pdfmake.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="js/vfs_fonts.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/moment.min.js"></script>
  		<script type="text/javascript" charset="utf-8"	src="js/moment-with-locales.min.js"></script>
	 	<script type="text/javascript" charset="utf-8" src="js/datetime-moment.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.4.6.js"></script>
		<s:set var="thisGuid" value="reqGuid" />
		<script type="text/javascript" src="script/displayjobs-1.4.10.js"></script>
	</head>
	<body>
	<!-- including Header -->
		<s:include value="Header.jsp"></s:include>
		
		<div class="container-fluid">
			<div class="row">
				<div class="col-sm-3">
				</div>
				<div class="col-sm-6">
				</div>
				<div class="col-sm-3">
					<s:set var="thisGuid" value="reqGuid" />
					<s:hidden name="filter" id="filter" value="%{filter}"/>
				</div>
			</div>
<br>
			<div class="row">
				<div class="col-lg-0 col-md-0">
				</div>
			<div class="col-lg-12 col-md-12">
				<h1 class="pl-1"><s:text name="welcome.tintQueue"/></h1>
			</div>
			</div>
			<div class="row">
				<div class="col-lg-0 col-md-0">
				</div>
				<div class="col-lg-12 col-md-12">
					<h3 id="title"></h3>
					<s:if test="hasActionMessages()">
					      <s:actionmessage cssClass="alert-danger"/>
					</s:if>
					<table id="job_table" class="table table-striped table-bordered">
						<thead>
							<tr>
								<th><s:text name="displayJobs.jobNbrPound"/></th>
								<th><s:text name="global.dateTime"/></th>
								<s:iterator value="#session[reqGuid].jobFieldList" status="stat">
									<th><s:property value="screenLabel" /></th>
								</s:iterator>
								<th><s:text name="displayJobs.colorNbr"/></th>
								<th><s:text name="global.colorName"/></th>
								<th><s:text name="displayJobs.chip"/></th>
								<th><s:text name="global.product"/></th>
								<th><s:text name="displayJobs.szCode"/></th>
								<s:if test="%{accountIsDrawdownCenter==true}">
									<th><s:text name="displayJobs.canType"/></th>
								</s:if>
								<th><s:text name="displayJobs.qtyDisp"/></th>
								<th><s:text name="displayJobs.qtyOrdered"/></th>
								<th style=""><s:text name="displayJobs.clrntSystem"/></th>
								<th style=""><s:text name="displayJobs.formulaHdr"/></th>
							</tr>
						</thead>
						<tbody>
							<s:iterator var="job" value="jobHistory" status="outer">
								<tr class="border-bottom-1 border-dark">
									<td><s:property value="#job.controlNbr" /></td>
									<td><s:date name="#job.jobCreationDate" /></td>
									<s:iterator var="fld" value="#session[reqGuid].jobFieldList" status="inner">
										<td class="idNumber"><span style="word-break: break-word; width: 100px"><s:property value="#job.jobFieldList[#inner.count-1].enteredValue" /></span></td>
									</s:iterator>
									<td><s:property value="#job.colorId" /></td>
									<td><s:property value="#job.colorName" /></td>
									<td bgcolor="<s:property value="#job.rgbhex"/>"> </td>
									<td><s:property value="#job.prodNbr" /></td>
									<td><s:property value="#job.sizeCode"/></td>
									<s:if test="%{accountIsDrawdownCenter==true}">
										<td><s:property value="#job.canType"/></td>
									</s:if>
									<td><s:property value="#job.quantityDispensed" /></td>
									<td><s:property value="#job.quantityOrdered"/></td>
									<!-- What needs to be included in the table (but won't be shown) -->
									<td style=""><s:property value="#job.clrntSysId"/></td>
									<td style="padding: 0px 0px 0px 0px; width: 100px">
										<table>
											<thead>
												<tr style="display:none">
												<th></th>
												</tr>
											</thead>
											<tbody>
												<tr>
													<td id="formulaList" style="padding: 0px 10px 0px 0px"><s:property value="#job.formulaDisplay"/></td>
												</tr>
											</tbody>
										</table>
									</td>
								</tr>
							</s:iterator>
						</tbody>
					</table>
				</div>
				<div class="col-lg-0 col-md-0">
				</div>
			</div>
<br>
			<s:form id="mainForm" action="selectJobAction" validate="true"  theme="bootstrap">
				<div class="row">
            		<div class="col-sm-1">
 						<s:hidden id="guid" name="reqGuid" value="%{reqGuid}"/>
 						<s:hidden id="controlNbr" name="lookupControlNbr" value=""/>
 						<s:hidden name="compare" id="compareColors" value="%{compare}"/>
					</div>

					<div class="col-sm-10">
						<%-- <s:if test="match != true">
							<button type="button" id="newSearchBtn" class="btn btn-primary" onclick="showSearchModal()"><s:text name="displayJobs.newSearch" /></button>
						</s:if> --%>
						<s:submit cssClass="btn btn-secondary pull-right mb-5 mt-2" value="%{getText('global.cancel')}" action="userCancelAction"/>
					</div>
					<div class="col-sm-1">
					</div>
				</div>
			</s:form>
			<s:form action="listJobsAction" validate="true" theme="bootstrap">
				<s:hidden name="exportColList" value="%{exportColList}" />
				<s:hidden name="displayTintQueue" value="%{displayTintQueue}"/>
			</s:form>
		</div>
		<br>
		<br>
		<br>
		<script>
		// update action if user is here to copy existing job fields
		if ("${copyJobFields}" == "true"){
			$("#mainForm").prop("action", "displayJobFieldsAction");
		}
		<!--
		  function HF_openSherwin() {
		    var popupWin = window.open("http://www.sherwin-williams.com", "Sherwin", "resizable=yes,toolbar=yes,menubar=yes,statusbar=yes,directories=no,location=yes,scrollbars=yes,width=800,height=600,left=10,top=10");
		    popupWin.focus();
		  }  
		  function HF_openLegal() {
		    var popupWin = window.open("http://www.sherwin-williams.com/terms/", "legal", "resizable=no,toolbar=no,menubar=yes,statusbar=no,directories=no,location=no,scrollbars=yes,width=800,height=600,left=10,top=10");
		    popupWin.focus();
		  }
		  function HF_openPrivacy() {
		    var popupWin = window.open("http://privacy.sherwin-williams.com/", "privacy", "resizable=yes,toolbar=no,menubar=yes,statusbar=no,directories=no,location=no,scrollbars=yes,width=640,height=480,left=10,top=10");
		    popupWin.focus();
		  }
		//-->
		</script>
  		
  		<!-- Including footer -->
		<s:include value="Footer.jsp"></s:include>
		
	</body>
</html>