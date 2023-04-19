<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>


<%@ taglib prefix="s" uri="/struts-tags" %>
<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title><s:text name="displayJobs.lookupExistingJobs"/></title>
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
		<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.5.2.js"></script>
		<s:set var="thisGuid" value="reqGuid" />
		<script type="text/javascript" src="script/displayjobs-2.0.1.js"></script>
	</head>
	<body>
		<div class="modal fade" tabindex="-1" role="dialog" id="searchmodal" data-backdrop="static" >
		  <div class="modal-dialog modal-lg" role="document" style="max-height: 90vh; overflow-y: initial !important">
		    <div class="modal-content">
		      <div class="modal-header bg-light">
		        <h5 class="modal-title"><s:text name="displayJobs.jobHistorySearch"/></h5>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      <s:form id="jobSearchForm" action="listJobsAction">
			      <div class="modal-body" style="max-height: 70vh; overflow-y: auto;">
			        <div class="row">
			      		<div class="col-sm-1"></div>
			      		<div class="col-sm-5 text-danger" id="searchError"></div>
			      		<div class="col-sm-5"></div>
			      		<div class="col-sm-1"></div>
			      	</div>
			      	<br>
			      	<div class="row">
			      		<div class="col-sm-1"></div>
			      		<div class="col-sm-5">
			      			<strong><s:text name="displayJobs.controlNumber"/><s:text name="displayJobs.forwardSlashScanCanLabel"/><s:text name="global.colonDelimiter"/></strong>
			      			<s:textfield class="srchinpt" id="cntrlnbr" placeholder="%{getText('displayJobs.allControlNumbers')}"></s:textfield>
			      		</div>
			      		<div class="col-sm-5"></div>
			      		<div class="col-sm-1"></div>
			      	</div>
			        <div class="row">
			      		<div class="col-sm-1"></div>
			      		<div class="col-sm-5">
			      			<strong><s:text name="displayJobs.fromDate"/><s:text name="global.colonDelimiter"/></strong>
			      			<s:textfield class="srchinpt" id="fdate" name="thc.fromDate" placeholder="%{getText('displayJobs.allDates')}" autocomplete="off"></s:textfield>
			      		</div>
			      		<div class="col-sm-5">
			      			<strong><s:text name="displayJobs.toDate"/><s:text name="global.colonDelimiter"/></strong>
			      			<s:textfield class="srchinpt" id="tdate" name="thc.toDate" placeholder="%{getText('displayJobs.allDates')}" autocomplete="off"></s:textfield>
			      		</div>
			      		<div class="col-sm-1"></div>
			      	</div>
			      	<div class="row">
			      		<div class="col-sm-1"></div>
			      		<div class="col-sm-5">
			      			<strong><s:text name="global.colorIdColon"/></strong>
			      			<s:textfield class="srchinpt" id="clrid" name="thc.colorId" placeholder="%{getText('displayJobs.allColorIds')}"></s:textfield>
			      		</div>
			      		<div class="col-sm-5">
			      			<strong><s:text name="global.colorNameColon"/></strong>
			      			<s:textfield class="srchinpt" id="clrnm" name="thc.colorName" placeholder="%{getText('displayJobs.allColorNames')}"></s:textfield>
			      		</div>
			      		<div class="col-sm-1"></div>
			      	</div>
			      	<div class="row">
			      		<div class="col-sm-1"></div>
			      		<div class="col-sm-5">
			      			<strong><s:text name="global.salesNumberColon"/></strong>
			      			<s:textfield class="srchinpt" id="slsnbr" name="thc.salesNbr" placeholder="%{getText('displayJobs.allSalesNumbers')}"></s:textfield>
			      		</div>
			      		<div class="col-sm-5">
			      			<strong><s:text name="global.productNumberColon"/></strong>
			      			<s:textfield class="srchinpt" id="prdnbr" name="thc.prodNbr" placeholder="%{getText('displayJobs.allProductNumbers')}"></s:textfield>
			      		</div>
			      		<div class="col-sm-1"></div>
			      	</div>
			      	<s:if test="%{sessionMap[reqGuid].allRooms != null && !sessionMap[reqGuid].allRooms.isEmpty}">
			      		<div class="row" id="roomuse">
				      		<div class="col-sm-1"></div>
				      		<div class="col-sm-5">
				      			<strong><s:text name="displayJobs.roomUse"/> <s:text name="global.colonDelimiter"/></strong>
				      			<s:select list="%{sessionMap[reqGuid].allRooms}" id="roomlist" onchange="toggleOther(this.value)" name="thc.roomUse" listKey="roomUse" 
				      			listValue="roomUse" headerKey="" headerValue="--" value="%{roomByRoom}"></s:select>
				      		</div>
				      		<div class="col-sm-5">
				      			<s:textfield class="d-none srchinpt" id="other" name="" style="margin-top:30px" placeholder="%{getText('displayJobs.allRooms')}"></s:textfield>
				      		</div>
				      		<div class="col-sm-1"></div>
				      	</div>
			      	</s:if>
		      		<s:iterator value="#session[reqGuid].jobFieldList" status="stat">
				      	<div class="row">
				      		<div class="col-sm-1"></div>
				      		<div class="col-sm-5">
				      			<strong><s:property value="screenLabel" /><s:text name="global.colonDelimiter"/></strong>
				      			<s:textfield id="jbfld%{#stat.count}" class="jobfield srchinpt" name="thc.jobField%{#stat.count}" placeholder="%{getText('displayJobs.allValues')}"></s:textfield>
				      		</div>
				      		<div class="col-sm-5"></div>
				      		<div class="col-sm-1"></div>
				      	</div>
			      	</s:iterator>
			      </div>
			      <div class="modal-footer">
			      	<s:hidden id="guid" name="reqGuid" value="%{reqGuid}"/>
			      	<s:hidden name="thc.customerId" value="%{sessionMap[reqGuid].customerId}"/>
			      	<s:hidden id="linenbr" name="thc.lineNbr" value=""/>
			      	<s:hidden id="controlnbr" name="thc.controlNbr" value=""/>
			      	<s:hidden id="copyjf" name="copyJobFields" value="%{copyJobFields}"/>
			      	<button id="searchbtn" type="button" class="btn btn-primary" onclick="validate()"><s:text name="displayJobs.search"/></button>
			      	<s:submit cssClass="btn btn-secondary pull-right" value="%{getText('global.cancel')}" action="userCancelAction"/>
				    <%-- <button type="button" id="cancelBtn" class="btn btn-secondary" data-dismiss="modal"><s:text name="global.cancel"/></button> --%>
			      </div>
		      </s:form>
		    </div>
		  </div>
		</div>
		<div class="modal fade" tabindex="-1" role="dialog" id="deletemodal">
		  <div class="modal-dialog" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title text-danger"><s:text name="global.deleteJob"/></h5>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      <div class="modal-body">
		        <h6><s:text name="displayJobs.areYouSureJobDelete"/></h6>
		      </div>
		      <div class="modal-footer">
		      <s:form>
			        <button type="button" id="yesbtn" class="btn btn-danger" data-dismiss="modal"><s:text name="global.yes"/></button>
			        <button type="button" id="nobtn" class="btn btn-secondary" data-dismiss="modal"><s:text name="global.no"/></button>
		       </s:form>
		      </div>
		    </div>
		  </div>
		</div>
		
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
			<s:if test="compareColors">
				<div class="row">
					<div class="col-xl-0 col-lg-0 col-md-0 col-sm-0">
					</div>
					<div class="col-xl-12 col-lg-12 col-md-12 col-sm-12">
						<div class="card card-body bg-light mt-4">
						   <div class="row">
								<div class="col-lg-4 col-md-4 col-sm-3 col-xs-4">
									<span class="badge badge-secondary" style="font-size: 1.2rem;"><s:text name="compareColors.selectStandardColor"/></span>
								</div>
								<div class="col-lg-3 col-md-3 col-sm-1 col-xs-0"></div>
								<div class="col-lg-5 col-md-5 col-sm-7 col-xs-8"></div>
							</div>
						</div>
					</div>
					<div class="col-xl-0 col-lg-0 col-md-0 col-sm-0">
					</div>
				</div>
			</s:if>
<br>
			
			<div class="row">
				<div class="col-lg-0 col-md-0">
				</div>
				<div class="col-lg-12 col-md-12">
					<h6 id="dltmsg" class="text-danger d-none"></h6>
					<s:if test="hasActionMessages()">
					      <s:actionmessage cssClass="alert-danger"/>
					</s:if>
					<table id="job_table" class="table table-striped table-bordered">
						<thead>
							<tr>
								<th><s:text name="displayJobs.jobNbrPound"/></th>
								<s:iterator value="#session[reqGuid].jobFieldList" status="stat">
									<th><s:property value="screenLabel" /></th>
								</s:iterator>
								<th><s:text name="displayJobs.colorNbr"/></th>
								<th><s:text name="global.colorName"/></th>
								<th><s:text name="global.notes"/></th>
								<th><s:text name="displayJobs.chip"/></th>
								<th><s:text name="global.product"/></th>
								<th><s:text name="displayJobs.szCode"/></th>
								<s:if test="%{accountIsDrawdownCenter==true}">
									<th><s:text name="displayJobs.canType"/></th>
								</s:if>
								<th><s:text name="displayJobs.qtyDisp"/></th>
								<th style=""><s:text name="displayJobs.clrntSystem"/></th>
								<th style=""><s:text name="displayJobs.formulaHdr"/></th>
								<th><s:text name="global.delete"/></th>
							</tr>
						</thead>
						<tbody>
							<s:iterator var="job" value="jobHistory" status="outer">
								<tr class="border-bottom-1 border-dark">
									<td><s:property value="#job.controlNbr" /></td>
									<s:iterator var="fld" value="#session[reqGuid].jobFieldList" status="inner">
										<td class="idNumber"><span style="word-break: break-word; width: 100px"><s:property value="#job.jobFieldList[#inner.count-1].enteredValue" /></span></td>
									</s:iterator>
									<td><s:property value="#job.colorId" /></td>
									<td><s:property value="#job.colorName" /></td>
									<td><s:property value="#job.colorNotes" /></td>
									<td bgcolor="<s:property value="#job.rgbhex"/>"> </td>
									<td><s:property value="#job.prodNbr" /></td>
									<td><s:property value="#job.sizeCode"/></td>
									<s:if test="%{accountIsDrawdownCenter==true}">
										<td><s:property value="#job.canType"/></td>
									</s:if>
									<td><s:property value="#job.quantityDispensed" /></td>
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
									<td>
										<button type="button" id="deleterow" class="btn btn-danger dltrow" title="%{getText('global.deleteJob')}" aria-label="delete">
											<i class="fa fa-trash-o" aria-hidden="true"></i>
										</button>
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
 						<s:hidden name="compareColors" id="compareColors" value="%{compareColors}"/>
					</div>

					<div class="col-sm-10">
						<s:submit id="cancelNewSearch" cssClass="btn btn-secondary pull-right mb-5 mt-2" value="%{getText('global.cancel')}" action="userCancelAction"/>
					</div>
					<div class="col-sm-1">
					</div>
				</div>
			</s:form>
			<s:form action="listJobsAction" validate="true" theme="bootstrap">
				<s:hidden name="exportColList" value="%{exportColList}" />
			</s:form>
		</div>
		<br>
		<br>
		<br>
		<script>
		// update action if user is here to copy existing job fields
		var copyJobFields = $('#copyjf').val();
		if (copyJobFields == "true"){
			console.log("copyJobFields is " + copyJobFields);
			$("#mainForm").prop("action", "displayJobFieldsAction");
		}
 
		var search = ${search};
		if(search){
			console.log("search is " + search);
			showSearchModal();
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