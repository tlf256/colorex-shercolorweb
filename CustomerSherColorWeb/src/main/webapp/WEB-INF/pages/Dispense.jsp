<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!doctype html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Dispense Colorants</title>
<!-- JQuery -->
<link rel=StyleSheet href="css/bootstrap.min.css" type="text/css">
<link rel=StyleSheet href="css/bootstrapxtra.css" type="text/css">
<link rel=StyleSheet href="js/smoothness/jquery-ui.css"type="text/css">
<link rel=StyleSheet href="css/CustomerSherColorWeb.css" type="text/css">
<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
<s:set var="thisGuid" value="reqGuid" />
<s:set var="thisUOM" value="UOM" />
<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
<script type="text/javascript" charset="utf-8"src="js/jquery-ui.js"></script>
<script type="text/javascript" charset="utf-8"	src="js/popper.min.js"></script>
<script type="text/javascript" charset="utf-8" src="js/bootstrap.min.js"></script>
<script type="text/javascript" charset="utf-8"	src="js/moment.min.js"></script>
<script type="text/javascript" charset="utf-8" src="script/CustomerSherColorWeb.js"></script>
<script type="text/javascript" charset="utf-8" src="script/WSWrapper.js"></script>
<script type="text/javascript" charset="utf-8" src="script/tinter-1.3.1.js"></script>
<script type="text/javascript" charset="utf-8" src="script/Dispense.js"></script>
<script type="text/javascript">
var shotList = [];
<s:iterator value="tinter.canisterList" status="i">
	shotList.push(new Colorant("<s:property value="clrntCode"/>",<s:property value="shots"/>,<s:property value="position"/>,<s:property value="thisUOM"/>));	
</s:iterator>
</script>
<style type="text/css">
input {
	padding: 3px 3px !important;
}
h4 {
	font-size: 16px;
	margin-top: 5px;
	margin-bottom: 5px;
}
.chip {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    border: .5px solid black;
    float: left;
    margin-right: 15px;
}
.popover-danger {
	background-color: #d9534f;
	border-color: #d43f3a;
	color: white;
}
.popover-body {
	color: white;
}

.popover .arrow:after {
	border-bottom-color: #d9534f;
	border-top-color: #d9534f;
}
.popover .arrow {
	border-bottom-color: #d9534f;
	border-top-color: #d9534f;
}

@media only screen and (max-width: 768px) {
    /* For mobile phones: */
    .colorcol {
    	padding-left: 0px;
    }
    .chip {
    	width: 30px;
    	height: 30px;
    	margin-right: 10px;
    }
      .sw-bg-main {
	            background-color: ${sessionScope[thisGuid].rgbHex};
	        }
}
input[type=number]::-webkit-inner-spin-button, 
input[type=number]::-webkit-outer-spin-button { 
    -webkit-appearance: none;
    -moz-appearance: none;
    appearance: none;
    margin: 0; 
}
input[type=number] {
    -moz-appearance:textfield;
}
</style>
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
							<s:hidden value="%{thisGuid}" id="reqGuid"></s:hidden>
						</div>
					</div>
					<br>
					<div class="row">
						<div class="col-xl-1 col-lg-0 col-md-0 col-sm-0">
						</div>
						<div class="col-xl-10 col-lg-12 col-md-12 col-sm-12">
							<div class="card card-body bg-light mb-3">
									<div class="d-flex flex-row justify-content-around">
									<div class="p-2 mr-3" style="width: 10rem;">
										<span class="badge badge-secondary" style="font-size: 1.2rem;">Dispense</span>
										<h5 class="text-primary mt-3"><strong><s:property value="tinter.model"/></strong></h5>
									</div>
									<div class="p-2">
										<div class="alert alert-warning text-center">
										  	<p class="m-0"><strong>Note:</strong> Only enter whole positive numbers into fields ranging from 0-99 and dispense a maximum of 8 Colorants per dispense.</p>
										</div>
									</div>
									<div class="p-2 ml-3">
										<s:url var="dispenseURL" action="executeDispenseColorantsAction"><s:param name="reqGuid" value="%{thisGuid}"/></s:url>
										<button class="btn btn-primary mr-4" id="dispense">Dispense</button>
									</div>
									<div class="p-2">
										<s:url var="cancelURL" action="userCancelAction"><s:param name="reqGuid" value="%{thisGuid}"/></s:url>
										<a href='<s:property value="cancelURL"/>' class="btn btn-secondary" id="cancel">Done</a>
									</div>
								</div>
							</div>	
							  <table class="table table-bordered table-sm">
							    <thead>
							      <tr>
							        <th class="bg-light" style="width: 60%;">Color</th>
							        <th class="bg-light text-center" id="oz" style="width: 10%;"><s:property value="incrHdr[0]"/></th>
							        <th class="bg-light text-center" id="three-two" style="width: 10%;"><s:property value="incrHdr[1]"/></th>
							        <th class="bg-light text-center" id="six-four" style="width: 10%;"><s:property value="incrHdr[2]"/></th>
							        <th class="bg-light text-center" id="one-two-eight" style="width: 10%;"><s:property value="incrHdr[3]"/></th>
							      </tr>
							    </thead>
							    <tbody>
							    <s:iterator value="sortedCanList" status="i">
							    	<s:if test="%{clrntCode != 'NA'}">
								      <tr id="<s:property value="%{#i.index}"/>">
								        <td id="col1row<s:property value="%{#i.index}"/>">
								        	<div class="col-xl-3 colorcol" style="max-width: 0%;">
											 	<div class="chip m-2 mr-3" style="background: <s:property value="rgbHex"/>;"></div>
										        <strong id="code"><s:property value="clrntCode"/></strong>
										        <p><s:property value="clrntName"/></p>
											</div>
											<div class="col-xl-2" style="padding-left: 0px;">
												<div id="warningdiv" style="margin-top: 5px;"><span class="badge badge-warning d-none" id="warning<s:property value="clrntCode"/>">Warning - Colorant Low</span></div>
												<strong id="pos" class="d-none"><s:property value="position"/></strong>
											</div>
											<div class="col-xl-9">
											</div>
								        </td>
								        <td id="col2row<s:property value="%{#i.index}"/>"><s:textfield name="oz%{#i.index}" style="text-align:center" class="myinputs" type="number" min="0" max="99"/></td>
								        <td id="col3row<s:property value="%{#i.index}"/>"><s:textfield name="32_%{#i.index}" style="text-align:center" class="myinputs" type="number" min="0" max="99"/></td>
								        <td id="col4row<s:property value="%{#i.index}"/>"><s:textfield name="64_%{#i.index}" style="text-align:center" class="myinputs" type="number" min="0" max="99"/></td>
							        	<td id="col5row<s:property value="%{#i.index}"/>"><s:textfield name="128_%{#i.index}" style="text-align:center" class="myinputs" type="number" min="0" max="99"/></td>
								      </tr>
							     	</s:if>
							     </s:iterator>
							    </tbody>
							  </table>
							<br>
						</div>
						<div class="col-xl-1 col-lg-0 col-md-0 col-sm-0">
						</div>
					</div>
				</div>
				<br>
				<br>
				<br>
				<br>
				
				<!-- Tinter In Progress Modal Window -->
				    <div class="modal fade" aria-labelledby="tinterInProgressModal" aria-hidden="true"  id="tinterInProgressModal" role="dialog" data-backdrop="static" data-keyboard="false">
				    	<div class="modal-dialog" role="document">
							<div class="modal-content">
								<div class="modal-header">
									<i id="spinner" class="fa fa-refresh mr-3 mt-1 text-muted" style="font-size: 1.5rem;"></i>
									<h5 class="modal-title" id="tinterInProgressTitle">Dispense In Progress</h5>
									<button type="button" class="close" data-dismiss="modal" aria-label="Close" ><span aria-hidden="true">&times;</span></button>
								</div>
								<div class="modal-body">
									<p id="dispenseStatus" font-size="4"></p>
									<p id="tinterInProgressMessage" font-size="4"></p>
								</div>
								<div class="modal-footer">
									<button id="progressok" type="button" class="btn btn-primary d-none" data-dismiss="modal" aria-label="Close" >OK</button>
								</div>
							</div>
						</div>
					</div> 
				
				<!-- Tinter Error List Modal Window -->
				    <div class="modal fade" aria-labelledby="tinterErrorListModal" aria-hidden="true"  id="tinterErrorListModal" role="dialog">
				    	<div class="modal-dialog">
							<div class="modal-content">
								<div class="modal-header">
									<h5 class="modal-title" id="tinterErrorListTitle">Tinter Error</h5>
									<button type="button" class="close" data-dismiss="modal" aria-label="Close" ><span aria-hidden="true">&times;</span></button>
								</div>
								<div class="modal-body">
									<div>
										<ul class="p-0" id="tinterErrorList" style="list-style: none;">
										</ul>
									</div>
									<p id="tinterErrorListSummary" font-size="4"></p>
								</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-primary" id="tinterErrorListOK" data-dismiss="modal" aria-label="Close" >OK</button>
								</div>
							</div>
						</div> 
					</div>			    

				    <!-- Tinter Warning List Modal Window -->
				    <div class="modal fade" aria-labelledby="tinterWarningListModal" aria-hidden="true"  id="tinterWarningListModal" role="dialog">
				    	<div class="modal-dialog">
							<div class="modal-content">
								<div class="modal-header">
									<h5 class="modal-title" id="tinterWarningListTitle">Tinter Error</h5>
									<button type="button" class="close" data-dismiss="modal" aria-label="Close" ><span aria-hidden="true">&times;</span></button>
								</div>
								<div class="modal-body">
									<div>
										<ul class="p-0" id="tinterWarningList" style="list-style: none;">
										</ul>
									</div>
									<p id="tinterWarningListSummary" font-size="4">Click OK to continue.</p>
								</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-primary" id="tinterWarningListOK" data-dismiss="modal" aria-label="Close" >OK</button>
								</div>
							</div>
						</div>
					</div>
					
					 <!-- Dispense Error Modal Window -->
				    <div class="modal fade" aria-labelledby="tinterSocketErrorModal" aria-hidden="true"  id="tinterSocketErrorModal" role="dialog">
				    	<div class="modal-dialog">
							<div class="modal-content">
								<div class="modal-header">
									<h5 class="modal-title">Dispense Error</h5>
									<button type="button" class="close" data-dismiss="modal" aria-label="Close" ><span aria-hidden="true">&times;</span></button>
								</div>
								<div class="modal-body">
									<p id="tinterSocketError" font-size="4"></p>
								</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-primary" id="tinterSocketErrorButton" data-dismiss="modal" aria-label="Close" >Close</button>
								</div>
							</div>
						</div>
					</div>	
		
	<!-- Including footer -->
	<s:include value="Footer.jsp"></s:include>
</body>
</html>