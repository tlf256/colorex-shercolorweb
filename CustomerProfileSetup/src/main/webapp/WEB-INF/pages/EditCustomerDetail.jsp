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
<script type="text/javascript" src="script/EditCustomerDetail.js"></script>
<script type="text/javascript" src="script/cps.js"></script>
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
	<s:form id="editcustdetail" theme="bootstrap" method="post" enctype="multipart/form-data">
	<div class="row">
		<div class="col-lg-2 col-md-2"></div>
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
						<div class="form-check">
							<s:iterator var="list" value="sessionMap['CustomerDetail'].clrntList" status="i">
								<label for="<s:property value='#list' />" class="form-check-label font-weight-normal">
									<s:property value="#list" />
								</label>
								<input type="checkbox" id="<s:property value='#list' />" name="cust.clrntList" class="clrntid form-check-input" 
									checked="checked" value="<s:property value='#list' />"></input>
							</s:iterator>
						<s:if test="'CCE' not in sessionMap['CustomerDetail'].clrntList">
							<label id="ccelabel" for="CCE" class="form-check-label font-weight-normal">CCE</label>
							<input type="checkbox" id="CCE" name="cust.cce" class="clrntid form-check-input ml-0" value="CCE" />
						</s:if>
						<s:if test="'BAC' not in sessionMap['CustomerDetail'].clrntList">
							<label id="baclabel" for="BAC" class="form-check-label font-weight-normal">BAC</label>
							<input type="checkbox" id="BAC" name="cust.bac" class="clrntid form-check-input ml-0" value="BAC" />
						</s:if>
						<s:if test="'844' not in sessionMap['CustomerDetail'].clrntList">
							<label id="efflabel" for="844" class="form-check-label font-weight-normal">844</label>
							<input type="checkbox" id="844" name="cust.eff" class="clrntid form-check-input ml-0" value="844" />
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
							<input type="radio" id="<s:property value='sessionMap["CustomerDetail"].clrntList[0]' />default" name="cust.defaultClrntSys" class="clrntdefault form-check-input" 
								checked="checked" value="<s:property value='sessionMap["CustomerDetail"].clrntList[0]' />"></input>
							<s:if test="%{sessionMap['CustomerDetail'].clrntList[0]!='CCE'}">
								<label id="" for="CCEdefault" class="form-check-label font-weight-normal">
									CCE
								</label>
								<input type="radio" id="CCEdefault" name="cust.defaultClrntSys" class="clrntdefault form-check-input ml-0" value="CCE" />
							</s:if>
							<s:if test="%{sessionMap['CustomerDetail'].clrntList[0]!='BAC'}">
								<label id="" for="BACdefault" class="form-check-label font-weight-normal">
									BAC
								</label>
								<input type="radio" id="BACdefault" name="cust.defaultClrntSys" class="clrntdefault form-check-input ml-0" value="BAC" />
							</s:if>
							<s:if test="%{sessionMap['CustomerDetail'].clrntList[0]!='844'}">
								<label id="" for="844default" class="form-check-label font-weight-normal">
									844
								</label>
								<input type="radio" id="844default" name="cust.defaultClrntSys" class="clrntdefault form-check-input ml-0" value="844" />
							</s:if>
						</div>
					</div>
					
				</td>
			</tr>
			<tr>
				<td><strong>Active</strong></td>
				<td>
					<div class="form-check-inline">
						<s:if test="sessionMap['CustomerDetail'].active">
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
	<s:if test="sessionMap['CustomerDetail'].profile == null || (sessionMap['CustomerDetail'].profile != null && !sessionMap['CustomerDetail'].updateMode)">
		<div class="row">
			<div class="col-lg-2 col-md-2">
				<s:hidden id="acctType" value="%{sessionMap['CustomerDetail'].accttype}"></s:hidden>
			</div>
			<div class="col-lg-8 col-md-8">
				<table id="custprofile" class="table table-striped table-bordered">
					<tr>
						<td class="align-middle"><strong>Customer Type</strong></td>
						<td id="custtype">
							<s:select list="sessionMap['CustomerDetail'].custTypeList" id="typelist" name="cust.custType" onchange="toggleProfileInput(this.value)"
								value="sessionMap['CustomerDetail'].profile.custType"></s:select>
						</td>
					</tr>
					<tr class="rmbyrm" id="rmbyrm">
						<td><strong>Use Room By Room</strong></td>
						<td>
							<s:if test="sessionMap['CustomerDetail'].profile == null || sessionMap['CustomerDetail'].profile.useRoomByRoom">
								<input type="radio" id="rbryes" name="cust.useRoomByRoom" class="mt-1 mb-1 rbr" value="true" checked /> Yes
							<input type="radio" id="rbrno" name="cust.useRoomByRoom" class="mt-1 mb-1 ml-4 rbr" value="false" /> No
							</s:if>
							<s:else>
								<input type="radio" id="rbryes" name="cust.useRoomByRoom" class="mt-1 mb-1 rbr" value="true" /> Yes
								<input type="radio" id="rbrno" name="cust.useRoomByRoom" class="mt-1 mb-1 ml-4 rbr" value="false" checked /> No
							</s:else>
						</td>
					</tr>
					<tr class="locid" id="locid">
						<td><strong>Use Locator ID</strong></td>
						<td>
							<s:if test="sessionMap['CustomerDetail'].profile == null || sessionMap['CustomerDetail'].profile.useLocatorId">
								<input type="radio" id="locyes" name="cust.useLocatorId" class="mt-1 mb-1 loc" value="true" checked /> Yes
								<input type="radio" id="locno" name="cust.useLocatorId" class="mt-1 mb-1 ml-4 loc" value="false" /> No
							</s:if>
							<s:else>
								<input type="radio" id="locyes" name="cust.useLocatorId" class="mt-1 mb-1 loc" value="true" /> Yes
								<input type="radio" id="locno" name="cust.useLocatorId" class="mt-1 mb-1 ml-4 loc" value="false" checked /> No
							</s:else>
						</td>
					</tr>
				</table>
			</div>
			<div class="col-lg-2 col-md-2"></div>
		</div>
	<br>
	</s:if>
	<s:if test="!sessionMap['CustomerDetail'].uploadedEula && sessionMap['CustomerDetail'].updateMode && 
		sessionMap['CustomerDetail'].eulaHistList == null">
		<div class="row">
			<div class="col-lg-2 col-md-2"></div>
			<div class="col-lg-8 col-md-8">
				<table id="uploadEula" class="table table-striped table-bordered">
					<tr>
						<th colspan="3">Upload EULA</th>
					</tr>
					<tr>
						<td colspan="3" class="align-middle">
							<s:file class="bg-light border-secondary form-control-file" id="eulafile" name="eulafile" accept="pdf" />
						</td>
					</tr>
					<tr>
						<th>Effective Date</th>
						<th>Expiration Date</th>
						<th>EULA Text</th>
					</tr>
					<tr>
						<td class="align-middle">
							<s:textfield id="effDate" name="effDate"></s:textfield>
						</td>
						<td class="align-middle">
							<s:textfield id="expDate" name="expDate"></s:textfield>
						</td>
						<td class="align-middle w-50">
							<s:textarea id="eulatext" name="eulaText"></s:textarea>
						</td>
					</tr>
				</table>
			</div>
			<div class="col-lg-2 col-md-2"></div>
		</div>
	<br>
	</s:if>
	
	<s:if test="sessionMap['CustomerDetail'].eulaHistToActivate != null">
		<div class="row">
		<div class="col-lg-2 col-md-2"></div>
		<div class="col-lg-8 col-md-8">
			<table id="eulaHist_detail" class="table table-striped table-bordered">
				<tr>
					<th>EULA History</th>
					<th>Type</th>
					<th>User</th>
					<th>Date</th>
					<th>Acceptance Code</th>
					<th>Activate</th>
				</tr>
				<tr>
					<td>
						<s:property value="sessionMap['CustomerDetail'].eulaHistToActivate.website" />
					</td>
					<td>
						<s:property value="sessionMap['CustomerDetail'].eulaHistToActivate.actionType" />
					</td>
					<td>
						<s:property value="sessionMap['CustomerDetail'].eulaHistToActivate.actionUser" />
					</td>
					<td>
						<s:property value="sessionMap['CustomerDetail'].eulaHistToActivate.actionTimeStamp" />
					</td>
					<td>
						<div class="form-inline">
							<s:textfield id="acceptcode" name="cust.acceptCode" cssStyle="width:100px"
							value="%{sessionMap['CustomerDetail'].eulaHistToActivate.acceptanceCode}" readonly="true"></s:textfield>
							<button type="button" id="edt2" class="btn btn-primary pull-right ml-4">
								<i class="far fa-edit"></i>
							</button>
						</div>
					</td>
					<td>
						<div class="form-check">
							<input class="ml-2 mt-2" type="checkbox" id="activateEula" name="cust.activateEula" value="true" checked="checked" />
						</div>
					</td>
				</tr>
			</table>
		</div>
		<div class="col-lg-2 col-md-2"></div>
		</div>
	<br>
	</s:if>
	<s:else>
		<s:if test="sessionMap['CustomerDetail'].eulaHistList == null">
			<div class="row">
				<div class="col-lg-2 col-md-2"></div>
				<div class="col-lg-8 col-md-8">
					<table id="newEula" class="table table-striped table-bordered">
						<tr>
							<th colspan="2">Activate EULA</th>
						</tr>
						<tr>
							<td>
								<s:select label="Activate Eula" list="sessionMap['CustomerDetail'].eulaList" id="eulalist" name="cust.website" headerValue="None"></s:select>
							</td>
							<td>
								<s:textfield label="Acceptance Code" name="cust.acceptCode" id="acceptcode" />
							</td>
						</tr>
					</table>
				</div>
				<div class="col-lg-2 col-md-2"></div>
			</div>
		</s:if>
	<br>
	</s:else>
	
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
			<s:iterator var="logintrans" value="sessionMap['CustomerDetail'].loginList" status="i">
			<tr class="loginrow">
				<td>
					<s:textfield class="kyfld" id="kyfld%{#i.index}" name="login.keyField" value="%{#logintrans.keyField}" readonly="true"></s:textfield>
				</td>
				<td>
					<s:textfield class="mstraccntnm" id="mstraccntnm%{#i.index}" name="login.masterAcctName" value="%{#logintrans.masterAcctName}" readonly="true"></s:textfield>
				</td>
				<td>
					<s:textfield class="acctcomm" id="acctcmmnt%{#i.index}" name="login.acctComment" value="%{#logintrans.acctComment}" readonly="true"></s:textfield>
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
					<s:textfield class="acctcomm" id="comment0" name="login.acctComment" />
				</td>
				<td>
					<button type="button" id="deleteloginrow" class="btn btn-danger dltrow" title="Delete row">
						<i class="far fa-trash-alt"></i>
					</button>
					<s:if test="sessionMap['CustomerDetail'].loginList.size!=10">
						<button type="button" id="addloginrow" class="btn btn-secondary addrow" title="Add row">
							<i class="far fa-plus-square"></i>
						</button>
					</s:if>
				</td>
			</tr>
		</table>
		</div>
		<div class="col-lg-2 col-md-2">
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
			<s:iterator var="jobFields" value="sessionMap['CustomerDetail'].jobFieldList" status="j">
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
					<s:if test="#jobFields.entryRequired">
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
					<s:if test="#jobFields.active">
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
					<button type="button" id="editjobrow<s:property value='%{#j.index}'/>" class="btn btn-primary edtrow ml-2 mt-2" title="Edit row">
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
					<input class="ml-2 mt-4" id="jobreq" type="checkbox" name="job.entryRequired" title="entry required" />
					</div>
				</td>
				<td>
					<div class="form-check">
					<input class="mt-4" id="jobactv" type="checkbox" name="job.active" title="active" />
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
		</div>
		<div class="col-lg-8 col-md-8">
			<s:if test="sessionMap['CustomerDetail'].updateMode">
				<s:submit cssClass="btn btn-primary mb-5 mt-2" id="submitchng" value="Accept Changes" action="displayUpdate" />
			</s:if>
			<s:else>
				<s:submit cssClass="btn btn-primary mb-5 mt-2" id="submitchng" value="Accept Changes" action="displayEdit" />
			</s:else>
			<s:submit cssClass="btn btn-secondary pull-right mb-5 mt-2" value="Cancel" action="cancelUpdate"/>
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