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
		<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.16/css/dataTables.bootstrap4.min.css"/>
		<link rel=StyleSheet href="css/bootstrap.min.css" type="text/css">
		<link rel=StyleSheet href="css/bootstrapxtra.css" type="text/css">
		<link rel=StyleSheet href="js/smoothness/jquery-ui.css" type="text/css">
		<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/buttons/1.5.1/css/buttons.bootstrap4.min.css"/>
		<link rel=StyleSheet href="css/joblist_datatable.css" type="text/css">
		<link rel=StyleSheet href="css/CustomerSherColorWeb.css" type="text/css">
		<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
		<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
		<script type="text/javascript" src="https://cdn.datatables.net/1.10.16/js/jquery.dataTables.min.js"></script>
		<script type="text/javascript" src="https://cdn.datatables.net/1.10.16/js/dataTables.bootstrap4.min.js"></script>
		<script type="text/javascript" src="https://cdn.datatables.net/buttons/1.5.1/js/dataTables.buttons.min.js"></script>
		<script type="text/javascript" src="https://cdn.datatables.net/buttons/1.5.1/js/buttons.bootstrap4.min.js"></script> 
		<script type="text/javascript" src="https://cdn.datatables.net/buttons/1.5.1/js/buttons.colVis.min.js"></script>
		<script type="text/javascript" src="https://cdn.datatables.net/buttons/1.5.2/js/buttons.colVis.min.js"></script>
		<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jszip/3.1.3/jszip.min.js"></script>
		<script type="text/javascript" src="https://cdn.datatables.net/buttons/1.5.1/js/buttons.html5.min.js"></script>
		<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.32/pdfmake.min.js"></script>
		<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.32/vfs_fonts.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/jquery-ui.js"></script>
		<script type="text/javascript" src="https://cdn.datatables.net/buttons/1.5.1/js/buttons.print.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/bootstrap.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.4.6.js"></script>
		<s:set var="thisGuid" value="reqGuid" />
		<script type="text/javascript" src="script/displayjobs-1.4.7.js"></script>
	</head>
	<body>
		<div class="modal fade" tabindex="-1" role="dialog" id="searchmodal">
		  <div class="modal-dialog modal-lg" role="document">
		    <div class="modal-content">
		      <div class="modal-header bg-light">
		        <h5 class="modal-title"><s:text name="displayJobs.existingJobSearch"/></h5>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      <s:form action="listJobsAction">
			      <div class="modal-body">
			      	<div class="row">
			      		<div class="col-sm-1"></div>
			      		<div class="col-sm-5">
			      			<s:textfield id="cntrlnbr" name="js.controlNbr" label="Control Number" labelposition="top" 
			      				placeholder="%{getText('displayJobs.allControlNumbers')}" labelSeparator=":"></s:textfield>
			      		</div>
			      		<div class="col-sm-5"></div>
			      		<div class="col-sm-1"></div>
			      	</div>
			        <div class="row">
			      		<div class="col-sm-1"></div>
			      		<div class="col-sm-5">
			      			<s:textfield id="fdate" name="js.fromDate" label="From Date" labelposition="top" 
			      				placeholder="%{getText('displayJobs.allDates')}" labelSeparator=":"></s:textfield>
			      		</div>
			      		<div class="col-sm-5">
			      			<s:textfield id="tdate" name="js.toDate" label="To Date" labelposition="top" 
			      				placeholder="%{getText('displayJobs.allDates')}" labelSeparator=":"></s:textfield>
			      		</div>
			      		<div class="col-sm-1"></div>
			      	</div>
			      	<div class="row">
			      		<div class="col-sm-1"></div>
			      		<div class="col-sm-5">
			      			
			      		</div>
			      		<div class="col-sm-5"></div>
			      		<div class="col-sm-1"></div>
			      	</div>
			      	<div class="row">
			      		<div class="col-sm-1"></div>
			      		<div class="col-sm-5">
			      			<s:textfield id="clrid" name="js.colorId" label="Color ID" labelposition="top" 
			      				placeholder="%{getText('displayJobs.allColorIds')}" labelSeparator=":"></s:textfield>
			      		</div>
			      		<div class="col-sm-5">
			      			<s:textfield id="clrnm" name="js.colorName" label="Color Name" labelposition="top" 
			      				placeholder="%{getText('displayJobs.allColorNames')}" labelSeparator=":"></s:textfield>
			      		</div>
			      		<div class="col-sm-1"></div>
			      	</div>
			      	<div class="row">
			      		<div class="col-sm-1"></div>
			      		<div class="col-sm-5">
			      			
			      		</div>
			      		<div class="col-sm-5">
			      		
			      		</div>
			      		<div class="col-sm-1"></div>
			      	</div>
			      	<s:if test="roomByRoomList != null && !roomByRoomList.isEmpty">
			      		<div class="row" id="roomuse">
				      		<div class="col-sm-1"></div>
				      		<div class="col-sm-5">
				      			<s:select list="roomByRoomList" id="roomlist" onchange="toggleOther(this.value)" label="Room Use"
				      				name="js.roomUse" labelposition="top" labelSeparator=":" emptyOption="true"></s:select>
				      		</div>
				      		<div class="col-sm-5">
				      			<s:textfield style="display:none;"></s:textfield>
				      		</div>
				      		<div class="col-sm-1"></div>
				      	</div>
			      	</s:if>
			      </div>
			      <div class="modal-footer">
			      	<s:hidden id="guid" name="reqGuid" value="%{reqGuid}"/>
			      	<s:hidden name="js.customerId" value="%{sessionMap[reqGuid].customerId}"/>
			      	<s:submit class="btn btn-primary" value="%{getText('displayJobs.search')}"></s:submit>
				    <button type="button" id="cancelBtn" class="btn btn-secondary" data-dismiss="modal"><s:text name="global.cancel"/></button>
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
<br>
			
			<div class="row">
				<div class="col-lg-0 col-md-0">
				</div>
				<div class="col-lg-12 col-md-12">
					<h3 id="title"></h3>
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
			<s:form id="mainForm" action="startNewJob" validate="true"  theme="bootstrap">
				<div class="row">
            		<div class="col-sm-1">
 						<s:hidden id="guid" name="reqGuid" value="%{reqGuid}"/>
 						<s:hidden id="controlNbr" name="lookupControlNbr" value=""/>
 						<s:hidden name="compare" id="compareColors" value="%{compare}"/>
					</div>

					<div class="col-sm-10">
						<button type="button" id="newSearchBtn" class="btn btn-primary" onclick="jobSearch()">
							<s:text name="displayJobs.newSearch" />
						</button>
						<s:submit cssClass="btn btn-secondary pull-right mb-5 mt-2" value="%{getText('global.cancel')}" action="userCancelAction"/>
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