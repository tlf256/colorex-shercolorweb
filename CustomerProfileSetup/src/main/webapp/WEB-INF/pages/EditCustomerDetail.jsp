<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
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
<script type="text/javascript" src="script/EditCustomerDetail.js"></script>
<title>Edit Customer Detail</title>
</head>
<body>
<!-- including Header -->
<s:include value="Header.jsp"></s:include>
<div class="container-fluid">
<br>
<br>
	<div class="row">
		<div class="col-sm-3"></div>
		<div class="col-sm-6"></div>
		<div class="col-sm-3"></div>
	</div>
	<div class="row">
		<div class="col-sm-3"></div>
		<div class="col-sm-6"></div>
		<div class="col-sm-3"></div>
	</div>
	<s:form id="custdetail" theme="bootstrap">
	<div class="row">
		<div class="col-lg-2 col-md-2">
			<s:set var="updateMode" value="updateMode" />
		</div>
		<div class="col-lg-8 col-md-8">
			<h3>Edit Customer Information</h3><br>
			<div id="formerror" class="text-danger"></div>
			<div id="custediterror" class="text-danger"></div><br>
		<table id="customer_detail" class="table table-striped table-bordered">
			<tr>
				<td><strong>Customer ID</strong></td>
				<td id="customerid">
					<s:property value="sessionMap['CustomerDetail'].customerId" />
				</td>
			</tr>
			<tr>
				<td><strong>Customer Name</strong></td>
				<td>
					<div class="form-inline">
						<s:textfield id="customername" name="cust.swuiTitle"
						value="%{sessionMap['CustomerDetail'].swuiTitle}" readonly="true"></s:textfield>
						<button type="button" id="edt" class="btn btn-primary ml-4 mr-4">
							<i class="far fa-edit"></i>
						</button>
					</div>
				</td>
			</tr>
			<tr>
				<td><strong>SherColor Database ID</strong></td>
				<td>
					<div class="form-inline">
						<s:textfield id="cdsadlfld" name="cust.cdsAdlFld"
						value="%{sessionMap['CustomerDetail'].cdsAdlFld}" readonly="true"></s:textfield>
						<button type="button" id="edt1" class="btn btn-primary pull-right ml-4 mr-4">
							<i class="far fa-edit"></i>
						</button>
					</div>
				</td>
			</tr>
			<tr>
				<td><strong>Colorant System</strong></td>
				<td>
					<div class="form-check-inline">
						<div class="form-check" id="clrnts">
							<s:iterator var="list" value="sessionMap['CustomerDetail'].clrntList" status="i">
								<label for="<s:property value='#list' />" class="form-check-label font-weight-normal">
									<s:property value="#list" />
								</label>
								<input type="checkbox" id="<s:property value='#list' />" name="cust.clrntList" class="form-check-input" 
									checked="checked" value="<s:property value='#list' />"></input>
							</s:iterator>
							<s:if test="'CCE' not in sessionMap['CustomerDetail'].clrntList">
								<label id="ccelabel" for="CCE" class="form-check-label font-weight-normal">CCE</label>
								<input type="checkbox" id="CCE" name="cust.cce" class="form-check-input ml-0" value="CCE" />	
							</s:if>
							<s:if test="'BAC' not in sessionMap['CustomerDetail'].clrntList">
								<label id="baclabel" for="BAC" class="form-check-label font-weight-normal">BAC</label>
								<input type="checkbox" id="BAC" name="cust.bac" class="form-check-input ml-0" value="BAC" />
							</s:if>
							<s:if test="'884' not in sessionMap['CustomerDetail'].clrntList">
								<label id="eeflabel" for="884" class="form-check-label font-weight-normal">884</label>
								<input type="checkbox" id="884" name="cust.eef" class="form-check-input ml-0" value="884" />
							</s:if>
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td><strong>Default Colorant System</strong></td>
				<td>
					
					<div class="form-check-inline">
						<div class="form-check" id="dfltclrnt">
							<label for="<s:property value='sessionMap["CustomerDetail"].clrntList[0]' />default" class="form-check-label font-weight-normal">
								<s:property value="sessionMap['CustomerDetail'].clrntList[0]" />
							</label>
							<input type="radio" id="<s:property value='sessionMap["CustomerDetail"].clrntList[0]' />default" name="cust.defaultClrntSys" class="form-check-input" 
								checked="checked" value="sessionMap['CustomerDetail'].clrntList[0]"></input>
							<s:if test="%{sessionMap['CustomerDetail'].clrntList[0]!='CCE'}">
								<label id="" for="CCEdefault" class="form-check-label font-weight-normal">
									CCE
								</label>
								<input type="radio" id="CCEdefault" name="cust.defaultClrntSys" class="form-check-input ml-0" value="CCE" />
							</s:if>
							<s:if test="%{sessionMap['CustomerDetail'].clrntList[0]!='BAC'}">
								<label id="" for="BACdefault" class="form-check-label font-weight-normal">
									BAC
								</label>
								<input type="radio" id="BACdefault" name="cust.defaultClrntSys" class="form-check-input ml-0" value="BAC" />
							</s:if>
							<s:if test="%{sessionMap['CustomerDetail'].clrntList[0]!='884'}">
								<label id="" for="884default" class="form-check-label font-weight-normal">
									884
								</label>
								<input type="radio" id="884default" name="cust.defaultClrntSys" class="form-check-input ml-0" value="884" />
							</s:if>
						</div>
					</div>
					
				</td>
			</tr>
			<tr>
				<td><strong>Active</strong></td>
				<td>
					<div class="form-check-inline">
						<s:if test="sessionMap['CustomerDetail'].active==true">
						<div class="form-check">
							<input class="form-check-input truefalse" type="checkbox" id="custactv" name="cust.active" value="true" checked="checked" />
						</div>
						</s:if>
						<s:else>
						<div class="form-check">
							<input class="form-check-input truefalse" type="checkbox" id="custactv" name="cust.active" value="true" />
						</div>
						</s:else>
					</div>
				</td>
			</tr>
		</table>
		</div>
		<div class="col-lg-2 col-md-2"></div>
	</div>
	<br>
	<s:if test="sessionMap['CustomerDetail'].accttype!='natlWdigits'">
		<div class="row">
		<div class="col-lg-2 col-md-2"></div>
		<div class="col-lg-8 col-md-8">
		<table id="loginTrans_detail" class="table table-striped table-bordered">
			<tr>
				<th>Login ID</th>
				<th>Employee Name</th>
				<th>Comment</th>
				<th>Edit Row</th>
			</tr>
			<s:iterator var="logintrans" value="sessionMap['LoginDetail'].loginList" status="i">
			<tr class="loginrow">
				<td>
					<s:textfield class="kyfld" id="kyfld%{#i.index}" name="login.keyField" value="%{#logintrans.keyField}" readonly="true"></s:textfield>
				</td>
				<td>
					<s:textfield class="mstraccntnm" id="mstraccntnm%{#i.index}" name="login.masterAcctName" value="%{#logintrans.masterAcctName}" readonly="true"></s:textfield>
				</td>
				<td>
					<s:textfield class="acctcmmnt" id="acctcmmnt%{#i.index}" name="login.acctComment" value="%{#logintrans.acctComment}" readonly="true"></s:textfield>
				</td>
				<td>
					<button type="button" id="editloginrow<s:property value='%{#i.index}'/>" class="btn btn-primary edtrow" title="Edit row">
						<i class="far fa-edit"></i>
					</button>
					<%-- <s:if test="sessionMap['CustomerDetail'].accttype=='natlWOdigits' || #i.index!=0"> --%>
						<button type="button" id="deleteloginrow<s:property value='%{#i.index}'/>" class="btn btn-danger deleterow" title="Delete row">
							<i class="far fa-trash-alt"></i>
						</button>
					<%-- </s:if> --%>
				</td>
			</tr>
			</s:iterator>
			<tr id="newloginrow" class="cloned-loginrow">
				<td>
					<s:textfield class="kyfld" id="keyfld0" name="login.keyField" />
				</td>
				<td>
					<s:textfield class="mstraccntnm" id="master0" name="login.masterAcctName" />
				</td>
				<td>
					<s:textfield class="acctcmmnt" id="comment0" name="login.acctComment" />
				</td>
				<td>
					<button type="button" id="deleteloginrow" class="btn btn-danger dltrow" title="Delete row">
						<i class="far fa-trash-alt"></i>
					</button>
					<s:if test="sessionMap['LoginDetail'].loginList.size!=10">
						<button type="button" id="addloginrow" class="btn btn-secondary addrow" title="Add row">
							<i class="far fa-plus-square"></i>
						</button>
					</s:if>
				</td>
			</tr>
		</table>
		</div>
		<div class="col-lg-2 col-md-2">
			<%-- <s:if test="sessionMap['LoginDetail'].loginList.size gt 0 && sessionMap['LoginDetail'].loginList.size lt 10">
			<button type="button" title="Add login" id="addnewloginrow" class="btn btn-secondary">
				<i class="far fa-plus-square"></i>
			</button>
			</s:if> --%>
		</div>
	</div>
	<br>
	</s:if>
	<div class="row">
		<div class="col-lg-2 col-md-2"></div>
		<div class="col-lg-8 col-md-8">
		<table id="job_detail" class="table table-striped table-bordered">
			<tr>
				<th>Number</th>
				<th>Screen Label</th>
				<th>Field Default</th>
				<th>Required</th>
				<th>Active</th>
				<th>Edit Row</th>
			</tr>
			<s:iterator var="jobFields" value="sessionMap['JobDetail'].jobFieldList" status="j">
			<tr class="jobrow">
				<td>
					<s:property value="#jobFields.seqNbr" />
				</td>
				<td>
					<s:textfield class="scrnlbl" id="scrnlbl%{#j.index}" name="job.screenLabel" value="%{#jobFields.screenLabel}" readonly="true" />
				</td>
				<td>
					<s:textfield class="flddflt" id="flddflt%{#j.index}" name="job.fieldDefault" value="%{#jobFields.fieldDefault}" readonly="true" />
				</td>
				<td>
					<s:if test="#jobFields.entryRequired==true">
					<div class="form-check">
						<input class="ml-2 mt-4" type="checkbox" id="jobreq<s:property value='%{#j.index}'/>" name="job.entryRequired" value="<s:property value='%{#j.index}'/>" checked="checked" />
					</div>
					</s:if>
					<s:else>
					<div class="form-check">
						<input class="ml-2 mt-4" type="checkbox" id="jobreq<s:property value='%{#j.index}'/>" name="job.entryRequired" value="<s:property value='%{#j.index}'/>" title="entry required" />
					</div>
					</s:else>
				</td>
				<td>
					<s:if test="#jobFields.active==true">
					<div class="form-check">
						<input class="mt-4" type="checkbox" id="jobactv<s:property value='%{#j.index}'/>" name="job.active" value="<s:property value='%{#j.index}'/>" checked="checked" />
					</div>
					</s:if>
					<s:else>
					<div class="form-check">
						<input class="mt-4" type="checkbox" id="jobactv<s:property value='%{#j.index}'/>" name="job.active" value="<s:property value='%{#j.index}'/>" title="active" />
						</div>
					</s:else>
				</td>
				<td>
					<button type="button" id="editjobrow<s:property value='%{#j.index}'/>" class="btn btn-primary edtrow" title="Edit row">
						<i class="far fa-edit"></i>
					</button>
				</td>
			</tr>
			</s:iterator>
			<tr class="cloned-jobrow d-none" id="newjobrow">
				<td></td>
				<td>
					<s:textfield class="scrnlbl" id="joblabel" name="job.screenLabel" />
				</td>
				<td>
					<s:textfield class="flddflt" id="jobfield" name="job.fieldDefault" />
				</td>
				<td>
					<div class="form-check">
					<input class="ml-2 mt-4" id="jobreq" type="checkbox" name="job.entryRequired" value="true" title="entry required" />
					</div>
				</td>
				<td>
					<div class="form-check">
					<input class="mt-4" id="jobactv" type="checkbox" name="job.active" value="true" title="active" />
					</div>
				</td>
				<td></td>
			</tr>
		</table>
		</div>
		<div class="col-lg-2 col-md-2"></div>
	</div>
	<br>
	<div class="row">
       	<div class="col-lg-2 col-md-2">
			<%-- <s:hidden name="lookupCustomerId" /> --%>
			<%-- <s:hidden name="updateMode" />
			<script type="text/javascript">
				function savevalue(value){
					$("input:hidden[name='updateMode']").val(value);
				}
			</script> --%>
		</div>
		<div class="col-lg-8 col-md-8">
			<s:if test="updateMode">
				<s:submit cssClass="btn btn-primary mb-5 mt-2" id="submitchng" value="Accept Changes" action="displayUpdate" />
				<s:submit cssClass="btn btn-secondary pull-right mb-5 mt-2" value="Cancel Update" action="displayUpdateDetail"/>
			</s:if>
			<s:else>
				<s:submit cssClass="btn btn-primary mb-5 mt-2" id="submitchng" value="Accept Changes" action="displayEdit" />
				<s:submit cssClass="btn btn-secondary pull-right mb-5 mt-2" value="Cancel Edit" action="displayDetail"/>
			</s:else>
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