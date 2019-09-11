<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- JQuery -->
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.16/css/dataTables.bootstrap4.min.css"/>
<link rel=StyleSheet href="css/bootstrap.min.css" type="text/css">
<link rel=StyleSheet href="css/bootstrapxtra.css" type="text/css">
<link rel=StyleSheet href="js/smoothness/jquery-ui.css" type="text/css">
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/buttons/1.5.1/css/buttons.bootstrap4.min.css"/>
<link rel=StyleSheet href="css/joblist_datatable.css" type="text/css">
<link rel=StyleSheet href="css/CustomerSherColorWeb.css" type="text/css">
<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
<script type="text/javascript" charset="utf-8" src="js/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="https://cdn.datatables.net/1.10.16/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="https://cdn.datatables.net/1.10.16/js/dataTables.bootstrap4.min.js"></script>
<script type="text/javascript" src="https://cdn.datatables.net/buttons/1.5.1/js/dataTables.buttons.min.js"></script>
<script type="text/javascript" src="https://cdn.datatables.net/buttons/1.5.1/js/buttons.bootstrap4.min.js"></script> 
<script type="text/javascript" src="https://cdn.datatables.net/buttons/1.5.1/js/buttons.colVis.min.js"></script>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jszip/3.1.3/jszip.min.js"></script>
<script type="text/javascript" src="https://cdn.datatables.net/buttons/1.5.1/js/buttons.html5.min.js"></script>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.32/pdfmake.min.js"></script>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.32/vfs_fonts.js"></script>
<script type="text/javascript" charset="utf-8"	src="js/jquery-ui.js"></script>
<script type="text/javascript" src="https://cdn.datatables.net/buttons/1.5.1/js/buttons.print.min.js"></script>
<script type="text/javascript" charset="utf-8"	src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="https://use.fontawesome.com/releases/v5.3.1/js/all.js" data-auto-replace-svg="nest"></script>
<script type="text/javascript" src="script/CustomerDetail.js"></script>
<title>Customer Detail</title>
</head>
<body>
<div class="modal fade" tabindex="-1" role="dialog" id="deletemodal">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">Delete Customer?</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <p id="transinfo" class="text-danger">This action cannot be undone!</p>
      </div>
      <div class="modal-footer">
      <s:form>
	        <s:submit id="deletebtn" class="btn btn-danger" action="deleteCustomer" value="Delete"></s:submit>
	        <button type="button" id="cancelbtn" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
       </s:form>
      </div>
    </div>
  </div>
</div>
<div class="modal fade" tabindex="-1" role="dialog" id="updatemodal">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">Create or Update Customer?</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <p id="transinfo" class="text-danger">This action cannot be undone!</p>
      </div>
      <div class="modal-footer">
      	<s:form>
	        <s:submit id="createupdatebtn" class="btn btn-primary" action="createOrUpdateCustomer" value="Save"></s:submit>
	        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
        </s:form>
      </div>
    </div>
  </div>
</div>
<div class="modal fade" tabindex="-1" role="dialog" id="inactivemodal">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title text-danger">Warning!</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <p id="transinfo">Customer has transaction history and cannot be deleted.<br>Set to inactive?</p>
      </div>
      <div class="modal-footer">
      <s:form>
	        <s:submit id="yesbtn" class="btn btn-danger" action="setCustomerInactive" value="Yes"></s:submit>
	        <button type="button" id="nobtn" class="btn btn-secondary" data-dismiss="modal">No</button>
       </s:form>
      </div>
    </div>
  </div>
</div>
<div class="modal fade" tabindex="-1" role="dialog" id="download_modal">
  <div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">Download EULA</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <div class="container-fluid">
        	<div class="row">
			<div class="col-lg-2 col-md-2"></div>
			<div class="col-lg-8 col-md-8">
				<table id="eulapdf" class="table table-striped table-bordered">
					<tr>
						<th>Eula PDF</th>
					</tr>
					<tr>
						<td>
							
						</td>
					</tr>
				</table>
			</div>
			<div class="col-lg-2 col-md-2"></div>
		</div>
        </div>
      </div>
      <div class="modal-footer">
      <s:form>
      	<%-- <s:submit id="uploadeula" class="btn btn-secondary" action="uploadEula" value="Upload"></s:submit> --%>
	    <button type="button" id="cancelbtn" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
       </s:form>
      </div>
    </div>
  </div>
</div>
<!-- including Header -->
<s:include value="Header.jsp"></s:include>
<div class="container-fluid">
	<div class="row">
		<div class="col-sm-3"></div>
		<div class="col-sm-6">
			<s:set var="updateMode" value="updateMode" />
		</div>
		<div class="col-sm-3"></div>
	</div>
	<div class="row">
		<div class="col-lg-2 col-md-2"></div>
		<div class="col-lg-8 col-md-8"></div>
		<div class="col-lg-2 col-md-2"></div>
	</div>
	<div class="row">
		<div class="col-lg-2 col-md-2"></div>
		<div class="col-lg-8 col-md-8">
			<div id="custDetailError" class="text-danger"></div>
		</div>
		<div class="col-lg-2 col-md-2"></div>
	</div>
	<br>
	<br>
	<div class="row">
		<div class="col-lg-2 col-md-2"></div>
		<div class="col-lg-8 col-md-8">
		<h3>Customer Information</h3><br>
		<s:if test="edited">
			<div class="text-danger">You must submit this form for changes to take effect</div>
		</s:if>
		<s:form>
			<s:if test="updateMode">
				<s:submit id="edit" action="updateDetail" class="btn btn-primary mb-1 mt-5" value="Edit"></s:submit>
				<s:if test="sessionMap['CustomerDetail'].history">
					<button type="button" id="inactivate" class="btn btn-danger mb-1 mt-5">Delete</button>
				</s:if>
				<s:else>
					<button type="button" id="delete" class="btn btn-danger mb-1 mt-5">Delete</button>
				</s:else>
			</s:if>
			<s:else>
				<s:submit id="editmode" action="editDetail" class="btn btn-primary mb-1 mt-5" value="Edit Mode"></s:submit>
			</s:else>
		</s:form>
		<table id="customer_detail" class="table table-striped table-bordered">
			<tr>
				<td><strong>Customer ID</strong></td>
				<td>
					<s:property value="sessionMap['CustomerDetail'].customerId" />
				</td>
			</tr>
			<tr>
				<td><strong>Customer Name</strong></td>
				<td id="customername">
					<s:property value="sessionMap['CustomerDetail'].swuiTitle" />
				</td>
			</tr>
			<tr>
				<td><strong>SherColor Database ID</strong></td>
				<td>
					<s:property value="sessionMap['CustomerDetail'].cdsAdlFld" />
				</td>
			</tr>
			<tr>
				<td><strong>Colorant System</strong></td>
				<td>
					<s:iterator var="clrntlist" value="sessionMap['CustomerDetail'].clrntList" status="i">
						<s:property value="#clrntlist" />
						<s:if test="!#i.last">/</s:if>
					</s:iterator>
				</td>
			</tr>
			<tr>
				<td><strong>Active</strong></td>
				<td>
					<s:property value="sessionMap['CustomerDetail'].active" />
				</td>
			</tr>
		</table>
		</div>
		<div class="col-lg-2 col-md-2"></div>
	</div>
	<br>
	<s:if test="sessionMap['CustomerDetail'].eula != null">
		<div class="row">
			<div class="col-lg-2 col-md-2"></div>
			<div class="col-lg-8 col-md-8">
				<table id="eula_detail" class="table table-striped table-bordered">
					<tr>
						<th>EULA</th>
						<th>Effective Date</th>
						<th>Expiration Date</th>
					</tr>
					<tr>
						<td>
							<s:property value="sessionMap['CustomerDetail'].eula.website" />
						</td>
						<td>
							<s:property value="sessionMap['CustomerDetail'].eula.effectiveDate" />
						</td>
						<td>
							<s:property value="sessionMap['CustomerDetail'].eula.expirationDate" />
						</td>
					</tr>
				</table>
			</div>
			<div class="col-lg-2 col-md-2"></div>
		</div>
	</s:if>
	<br>
	<s:if test="sessionMap['CustomerDetail'].eulaHistList != null">
		<div class="row">
			<div class="col-lg-2 col-md-2"></div>
			<div class="col-lg-8 col-md-8">
				<table id="eulaHist_detail" class="table table-striped table-bordered">
					<tr>
						<th>EULA History</th>
						<th>Type</th>
						<th>User</th>
						<th>Date</th>
					</tr>
					<s:iterator var="eula" value="sessionMap['CustomerDetail'].eulaHistList" status="i">
					<tr>
						<td>
							<s:property value="#eula.website" />
						</td>
						<td>
							<s:property value="#eula.actionType" />
						</td>
						<td>
							<s:property value="#eula.actionUser" />
						</td>
						<td>
							<s:property value="#eula.actionTimeStamp" />
						</td>
					</tr>
					</s:iterator>
				</table>
			</div>
			<div class="col-lg-2 col-md-2"></div>
		</div>
	</s:if>
	<br>
	<s:if test="!sessionMap['CustomerDetail'].loginList.isEmpty">
		<div class="row">
		<div class="col-lg-2 col-md-2"></div>
		<div class="col-lg-8 col-md-8">
		<table id="loginTrans_detail" class="table table-striped table-bordered">
			<tr>
				<th>Login ID</th>
				<th>Employee Name</th>
				<th>Comment</th>
			</tr>
			<s:iterator var="login" value="sessionMap['CustomerDetail'].loginList" status="i">
			<tr>
				<td>
					<s:property value="#login.keyField" />
				</td>
				<td>
					<s:property value="#login.masterAcctName" />
				</td>
				<td>
					<s:property value="#login.acctComment" />
				</td>
			</tr>
			</s:iterator>
		</table>
		</div>
		<div class="col-lg-2 col-md-2"></div>
	</div>
	</s:if>
	<br>
	<s:if test="!sessionMap['CustomerDetail'].jobFieldList.isEmpty">
		<div class="row">
		<div class="col-lg-2 col-md-2"></div>
		<div class="col-lg-8 col-md-8">
		<table id="job_detail" class="table table-striped table-bordered">
			<tr>
				<th>Number</th>
				<th>Screen Label</th>
				<th>Field Default</th>
				<th>Entry Required</th>
				<th>Active</th>
			</tr>
			<s:iterator var="jobFields" value="sessionMap['CustomerDetail'].jobFieldList">
			<tr>
				<td>
					<s:property value="#jobFields.seqNbr" />
				</td>
				<td>
					<s:property value="#jobFields.screenLabel" />
				</td>
				<td>
					<s:property value="#jobFields.fieldDefault" />
				</td>
				<td>
					<s:property value="#jobFields.entryRequired" />
				</td>
				<td>
					<s:property value="#jobFields.active" />
				</td>
			</tr>
			</s:iterator>
		</table>
		</div>
		<div class="col-lg-2 col-md-2"></div>
	</div>
	</s:if>
	<br>
	<s:form>
	<div class="row">
       	<div class="col-lg-2 col-md-2">
			<%-- <s:hidden name="lookupCustomerId" /> --%>
			<%-- <s:hidden name="updateMode" /> --%>
		</div>
		<div class="col-lg-8 col-md-8">
			<s:if test="edited || newCustomer">
				<button type="button" id="submitbtn" class="btn btn-primary mb-5 mt-2">Submit</button>
			</s:if>
			<s:submit cssClass="btn btn-secondary pull-right mb-5 mt-2" value="Cancel" action="resetAction"/>
		</div>
		<div class="col-lg-2 col-md-2"></div>
	</div>
	</s:form>
</div>
<br>
<br>
<!-- Including footer -->
<s:include value="Footer.jsp"></s:include>
</body>
</html>