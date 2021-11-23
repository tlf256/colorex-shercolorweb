<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
	<title>Customer Lookup</title>
		<!-- JQuery -->
	<link rel="stylesheet" href="css/dataTables.bootstrap4.min.css" type="text/css">
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
	<script type="text/javascript" src="script/LookupCustomer.js"></script>
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
		</div>
	</div>
<br>		
	<div class="row">
		<div class="col-lg-0 col-md-0"></div>
		<div class="col-lg-12 col-md-12">
			<s:if test="hasActionMessages()">
			   <s:actionmessage cssClass="alert-danger"/>
			</s:if>
			<table id="customer" class="table table-striped table-bordered">
				<thead>
					<tr>
						<th>Customer ID</th>
						<th>Seq Nbr</th>
						<th>Customer Name</th>
						<th>Colorant System</th>
						<th>Cds Adl Fld</th>
					</tr>
				</thead>
				<tbody>
					<s:iterator var="customer" value="reqObjList">
						<tr>
							<td><s:property value="#customer.getCustomerId" /></td>
							<td><s:property value="#customer.getSeqNbr" /></td>
							<td><s:property value="#customer.getSwuiTitle" /></td>
							<td><s:property value="#customer.getClrntSysId" /></td>
							<td><s:property value="#customer.getCdsAdlFld" /></td>
						</tr>
					</s:iterator>
				</tbody>
			</table>
		</div>
		<div class="col-lg-0 col-md-0">
		</div>
	</div>
	<div class="row">
    	<div class="col-sm-1"></div>
		<div class="col-sm-10"></div>
		<div class="col-sm-1"></div>
	</div>	
<br>
	<s:form id="mainForm" action="" validate="true"  theme="bootstrap">
	<div class="row">
    	<div class="col-sm-1"></div>
		<div class="col-sm-10">
			<s:submit cssClass="btn btn-secondary pull-right mb-5 mt-2" value="Cancel" action="userCancelAction"/>
		</div>
		<div class="col-sm-1"></div>
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