<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>


<%@ taglib prefix="s" uri="/struts-tags" %>

<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title>Display Dealer's Customers</title>
			<!-- JQuery -->
		<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.13/css/dataTables.bootstrap.min.css"/>
		<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
		<link rel="stylesheet" href="css/bootstrapxtra.css" type="text/css">
		<link rel="stylesheet" href="js/smoothness/jquery-ui.min.css" type="text/css">
		<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/buttons/1.2.4/css/buttons.bootstrap.min.css"/>
		<link rel="stylesheet" href="css/joblist_datatable.css" type="text/css">
		<link rel="stylesheet" href="css/font-awesome.css" type="text/css">
			
		<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
		<script type="text/javascript" src="https://cdn.datatables.net/1.10.13/js/jquery.dataTables.min.js"></script>
		<script type="text/javascript" src="https://cdn.datatables.net/1.10.13/js/dataTables.bootstrap.min.js"></script>
		<script type="text/javascript" src="https://cdn.datatables.net/buttons/1.2.4/js/dataTables.buttons.min.js"></script>
		<script type="text/javascript" src="https://cdn.datatables.net/buttons/1.2.4/js/buttons.bootstrap.min.js"></script>
		<script type="text/javascript" src="//cdn.datatables.net/buttons/1.2.4/js/buttons.flash.min.js"></script>
		<script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/jszip/2.5.0/jszip.min.js"></script>
		<script type="text/javascript" src="//cdn.datatables.net/buttons/1.2.4/js/buttons.html5.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/jquery-ui.min.js"></script>
		<script type="text/javascript" src="//cdn.datatables.net/buttons/1.2.4/js/buttons.print.min.js"></script>
		<s:set var="thisGuid" value="reqGuid" /> 
		
		<script type="text/javascript" src="script/DisplayDealerCustomers.js"></script>  	
	</head>
	
	<!-- including Header -->
	<s:include value="Header.jsp"></s:include>
	
	<body>
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
		
			<div>
				<table>
					<thead>
						<tr>
							<th>Dealer Id</th>
							<th>Dealer Name</th>
							<th>Home Store</th>
							<th>Comments</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td><s:property value="custWebDealerDto.customerId" escapeHtml="true" /></td>
							<td><s:property value="custWebDealerDto.dealerName" escapeHtml="true" /></td>
							<td><s:property value="custWebDealerDto.homeStore" escapeHtml="true" /></td>
							<td><s:property value="custWebDealerDto.comments" escapeHtml="true" /></td>
						</tr>
					</tbody>
				</table>
			</div>
<!-- 
	listCustDealerCustDto is:
	customerId
	dlrCustId
	dlrCustName
	dlrCustAddress
	dlrCustCity
	dlrCustState
	dlrCustCountry
	dlrCustZip
	dlrCustPhoneNbr
 --> 				<div class="row">
				<div class="col-sm-1">
				</div>
				<div class="col-sm-10">
					<table id="customer_table" class="table table-striped table-bordered">
						<thead>
							<tr>
								<th>Cust Id</th>
								<th>Customer Name</th>
								<th>Address</th>
								<th>City</th>
								<th>State</th>
								<th>Country</th>
								<th>Zip</th>
								<th>Phone Nbr</th>
							</tr>
						</thead>
						<tbody>
							<s:iterator var="cust" value="listCustDealerCustDto" status="outer">
								<tr>
									<td><s:property value="#cust.dlrCustId" /></td>
									<td><s:property value="#cust.dlrCustName" /></td>
									<td><s:property value="#cust.dlrCustAddress" /></td>
									<td><s:property value="#cust.dlrCustCity" /></td>
									<td><s:property value="#cust.dlrCustState" /></td>
									<td><s:property value="#cust.dlrCustCountry" /></td>
									<td><s:property value="#cust.dlrCustZip" /></td>
									<td><s:property value="#cust.dlrCustPhoneNbr" /></td>
								</tr>
							</s:iterator>
						</tbody>
					</table>
				</div>
				<div class="col-sm-1">
				</div>
			</div>
			
<br>
			<s:form id="mainForm" action="selectJobAction" validate="true"  theme="bootstrap">
				<div class="row">
            		<div class="col-sm-2">
 						<s:hidden name="reqGuid" value="%{reqGuid}"/>
  						<s:hidden name="lookupCustomerId" value="custWebDealerDto.customerId"/>
  						<s:hidden name="lookupDlrCustId"/>
					</div>

					<div class="col-sm-8">
						<s:if test="hasActionMessages()">
						      <s:actionmessage/>
						</s:if>
					</div>
				</div>
				<br>
				<br>	
				<div class="row">
				
						<div class="col-sm-8">
						</div>
						<div class="col-sm-2">
			    			<s:submit cssClass="btn btn-secondary" value="Cancel" action="userCancelAction"/>
			    		</div>
			    	
		    	</div>
			</s:form>
		</div>
		<br>
		<br>
  		<!-- Including footer -->
		<s:include value="Footer.jsp"></s:include>
		
	</body>
</html>