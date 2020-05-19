<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>


<%@ taglib prefix="s" uri="/struts-tags" %>
<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title>Lookup Existing Jobs</title>
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
		<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.4.2.js"></script>
		<s:set var="thisGuid" value="reqGuid" />
		<script type="text/javascript" src="script/displayjobs-1.4.2.js"></script>
		
	</head>
	<body>
		<div class="modal fade" tabindex="-1" role="dialog" id="deletemodal">
		  <div class="modal-dialog" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title text-danger">Delete Job</h5>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      <div class="modal-body">
		        <h6>Are you sure you want to delete this job?</h6>
		      </div>
		      <div class="modal-footer">
		      <s:form>
			        <button type="button" id="yesbtn" class="btn btn-danger" data-dismiss="modal">Yes</button>
			        <button type="button" id="nobtn" class="btn btn-secondary" data-dismiss="modal">No</button>
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
				</div>
			</div>
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
								<th>Job #</th>
								<s:iterator value="#session[reqGuid].jobFieldList" status="stat">
									<th><s:property value="screenLabel" /></th>
								</s:iterator>
								<th>Color #</th>
								<th>Color Name</th>
								<th>Chip</th>
								<th>Product</th>
								<th>Sz Code</th>
								<th>Qty Disp</th>
								<th style="">Clrnt System</th>
								<th style="">Formula: OZ/32/64/128</th>
								<th>Delete</th>
								<!-- For the data sheets -->
								<th style="display:none">Formula: OZ/32/64/128</th>
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
													<td style="padding: 0px 10px 0px 0px"><s:property value="#job.formulaDisplay"/></td>
												</tr>
											</tbody>
										</table>
									</td>
									<td>
										<button type="button" id="deleterow" class="btn btn-danger dltrow" title="Delete job" aria-label="delete">
											<i class="fa fa-trash-o" aria-hidden="true"></i>
										</button>
									</td>
									<td style="display:none"><s:property value="#job.formulaDisplay"/></td>
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
					</div>

					<div class="col-sm-10">
						<s:submit cssClass="btn btn-secondary pull-right mb-5 mt-2" value="Cancel" action="userCancelAction"/>
					</div>
					<div class="col-sm-1">
					</div>
				</div>
			</s:form>
		</div>
		<br>
		<br>
		<script>
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