<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- JQuery -->
<link rel=StyleSheet href="css/bootstrap.min.css" type="text/css">
<link rel=StyleSheet href="css/bootstrapxtra.css" type="text/css">
<link rel=StyleSheet href="js/smoothness/jquery-ui.css" type="text/css">
<link rel=StyleSheet href="css/CustomerSherColorWeb.css" type="text/css">
<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
<script type="text/javascript" charset="utf-8" src="js/jquery-3.2.1.min.js"></script>
<script type="text/javascript" charset="utf-8" src="js/jquery-ui.js"></script>
<script type="text/javascript" charset="utf-8" src="js/bootstrap.min.js"></script>
<script type="text/javascript" charset="utf-8" src="script/CustomerForm.js"></script>
<script type="text/javascript" charset="utf-8" src="script/cps.js"></script>
<title>Customer Form</title>
</head>
<body>
<div class="modal fade" tabindex="-1" role="dialog" id="warn_modal" data-backdrop="static">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title text-warning">Warning</h5>
      </div>
      <div class="modal-body">
        <p id="">
        	You are attempting to create a SherColor Web account using a Sher-Link account number. Do you wish to proceed?
        </p>
      </div>
      <div class="modal-footer">
      <s:form>
	        <button type="button" id="yes_btn" class="btn btn-warning" onclick="proceedYes()">Yes</button>
	        <button type="button" id="no_btn" class="btn btn-secondary" onclick="proceedNo()">No</button>
       </s:form>
      </div>
    </div>
  </div>
</div>
<!-- including Header -->
<s:include value="Header.jsp"></s:include>
<div class="container-fluid">
	<div class="row">
		<div class="col-sm-2"></div>
		<div class="col-sm-2"></div>
		<div class="col-sm-2"></div>
	</div>
	<div class="row">
		<div class="col-sm-2"></div>
		<div class="col-sm-2"></div>
		<div class="col-sm-2"></div>
	</div>
	<br>
	<div class="row">
		<div class="col-md-3 col-sm-1"></div>
		<div class="col-md-6 col-sm-4">
			<h3>Enter Customer Information</h3>
		</div>
		<div class="col-md-3 col-sm-1"></div>
	</div>
	<br>
	<s:form id="customerInfo" action="getCustomerInfo" method="post">
	<div class="row">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-6 col-md-6 col-sm-4 col-xs-4">
			<strong>* Denotes Required Field</strong>
			<div id="formerror" class="text-danger errorfld"></div>
			<div id="validationerror" class="text-danger errorfld"></div>
			<s:actionmessage/>
			<s:actionerror/>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<br>
	<div class="row" id="acctype">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-6 col-md-6 col-sm-4 col-xs-4">
		<strong>Account Type *</strong>
			<div class="custom-radio ml-1">
			<input type="radio" id="selectedAccttype-1" name="customer.accttype" class="mt-2 mb-1" onchange="showHideInput(this.value)" value="natlWOdigits" />
				Internal SW Account<br>
			<input type="radio" id="selectedAccttype-3" name="customer.accttype" class="mt-2 mb-1" onchange="showHideInput(this.value)" value="intnatlWOdigits" />
				International SW Account<br>
			<input type="radio" id="selectedAccttype-0" name="customer.accttype" class="mt-1 mb-1" onchange="showHideInput(this.value)" value="natlWdigits" />
				Enter 9 Digit National Account Number<br>
			<input type="radio" id="selectedAccttype-2" name="customer.accttype" class="mt-2 mb-1" onchange="showHideInput(this.value)" value="intnatlWdigits" />
				Enter 7 Digit International Account Number<br>
			<input type="radio" id="selectedAccttype-4" name="customer.accttype" class="mt-2 mb-1" onchange="showHideInput(this.value)" value="intnatlCostCntr" />
				Enter 6 Digit Cost Center<br>
			</div>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<br>
	<div class="row ntlacct">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-6 col-md-6 col-sm-4 col-xs-4">
			<div id="ntlaccterror" class="text-danger errorfld"></div>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<div class="row ntlacct" id="ntlacct">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
			<s:textfield label="Account Number" name="customer.ntlacctnbr" id="ntlacctnbr" requiredLabel="true" class="req acctnbr" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2"></div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<div class="row intntlacct">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-6 col-md-6 col-sm-4 col-xs-4">
			<div id="intntlaccterror" class="text-danger errorfld"></div>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<div class="row intntlacct" id="intntlacct">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
			<s:textfield label="Account Number" name="customer.intntlacctnbr" id="intntlacctnbr" requiredLabel="true" class="req acctnbr" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2"></div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<div class="row costcntr">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-6 col-md-6 col-sm-4 col-xs-4">
			<div id="costcntrerror" class="text-danger errorfld"></div>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<div class="row costcntr" id="costcntr">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
			<s:textfield label="Account Number" name="customer.costCenter" id="cstcntr" requiredLabel="true" class="req acctnbr" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2"></div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<div class="row" id="cont">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
			<a href="#" onclick="validateAcctNbr()">Continue</a>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2"></div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<div class="row cstmrnm">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-6 col-md-6 col-sm-4 col-xs-4">
			<div id="swuititlerror" class="text-danger errorfld"></div>
			<div id="cdsadlflderror" class="text-danger errorfld"></div>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	 <div class="row cstmrnm" id="cstmrnm">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-4 col-md-4 col-sm-2 col-xs-2">
			<s:textfield label="Customer Name" name="customer.swuiTitle" id="swuititle" requiredLabel="true" class="req" />
			<s:textfield label="Additional Info (SherCust Database ID)" name="customer.cdsAdlFld" id="cdsadlfld" />
		</div>
		<div class="col-lg-5 col-md-5 col-sm-3 col-xs-3"></div>
	</div>
	<div class="row clrnt">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-6 col-md-6 col-sm-4 col-xs-4">
			<div id="clrntsyserror" class="text-danger errorfld"></div>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<div class="row clrnt" id="clrnt">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
		<strong>Colorant System *</strong><br>
		<div class="form-check">
			<input type="checkbox" id="CCE" name="customer.cce" class="form-check-input clrntid" value="CCE"></input>
			<label class="form-check-label font-weight-normal" for="CCE">CCE</label><br>
			<input type="checkbox" id="BAC" name="customer.bac" class="form-check-input clrntid" value="BAC"></input>
			<label class="form-check-label font-weight-normal" for="BAC">BAC</label><br>
			<input type="checkbox" id="844" name="customer.eff" class="form-check-input clrntid" value="844"></input>
			<label class="form-check-label font-weight-normal" for="884">844</label><br>
		</div>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
			<strong>Default *</strong><br>
			<div class="custom-radio ml-1">
				<input type="radio" id="CCEdefault" name="customer.defaultClrntSys" class="mt-1 mb-1 clrntdefault" value="CCE" /><br>
				<input type="radio" id="BACdefault" name="customer.defaultClrntSys" class="mt-2 mb-1 clrntdefault" value="BAC" /><br>
				<input type="radio" id="844default" name="customer.defaultClrntSys" class="mt-2 mb-1 clrntdefault" value="844" /><br>
			</div>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<br>
	<div class="row custtype" id="">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-1">
		</div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-1"></div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-1"></div>
	</div>
	<div class="row custtype" id="">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
			<s:select label="Customer Type" list="sessionMap['CustomerDetail'].custTypeList" id="typelist" name="customer.custType" 
				onchange="toggleProfileInput(this.value)"></s:select>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2"></div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<div class="row rmbyrm" id="">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-1">
			<strong>Use Room By Room?</strong>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-1"></div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-1"></div>
	</div>
	<div class="row rmbyrm mb-3" id="">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-1 col-md-1 col-sm-1 col-xs-1">
			<input type="radio" id="rbryes" name="customer.useRoomByRoom" class="mt-1 mb-1 rbr" value="true" checked /> Yes
		</div>
		<div class="col-lg-1 col-md-1 col-sm-1 col-xs-1">
			<input type="radio" id="rbrno" name="customer.useRoomByRoom" class="mt-1 mb-1 rbr" value="false" /> No
		</div>
		<div class="col-lg-7 col-md-7 col-sm-1 col-xs-1"></div>
	</div>
	<div class="row locid" id="">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-1">
			<strong>Use Locator ID?</strong>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-1"></div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-1"></div>
	</div>
	<div class="row locid mb-3" id="">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-1 col-md-1 col-sm-1 col-xs-1">
			<input type="radio" id="locyes" name="customer.useLocatorId" class="mt-1 mb-1 loc" value="true" checked /> Yes
		</div>
		<div class="col-lg-1 col-md-1 col-sm-1 col-xs-1">
			<input type="radio" id="locno" name="customer.useLocatorId" class="mt-1 mb-1 loc" value="false" /> No
		</div>
		<div class="col-lg-7 col-md-7 col-sm-1 col-xs-1"></div>
	</div>
	<div class="row eula">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-6 col-md-6 col-sm-4 col-xs-4">
			<div id="eulaerror" class="text-danger errorfld"></div>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<div class="row eula" id="eula">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
			<s:select label="Activate EULA" list="sessionMap['CustomerDetail'].eulaList" id="eulalist" name="customer.website" headerValue="None"></s:select>
			<s:textfield label="Acceptance Code" name="customer.acceptCode" id="acceptcode" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2"></div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<div class="row prodaccess">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-6 col-md-6 col-sm-4 col-xs-4">
			<div id="prodcomperror" class="text-danger errorfld"></div>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<div class="row prodaccess">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-1">
			<strong>Restrict Product Access?</strong>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-1"></div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-1"></div>
	</div>
	<div class="row prodaccess mb-3">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-1 col-md-1 col-sm-1 col-xs-1">
			<input type="radio" id="restrictyes" name="restrictProdComp" class="mt-1 mb-1 rbr" onchange="toggleTextArea(this.value)" value="true" /> Yes
		</div>
		<div class="col-lg-1 col-md-1 col-sm-1 col-xs-1">
			<input type="radio" id="restrictno" name="restrictProdComp" class="mt-1 mb-1 rbr" onchange="toggleTextArea(this.value)" value="false" checked /> No
		</div>
		<div class="col-lg-7 col-md-7 col-sm-1 col-xs-1"></div>
	</div>
	<div class="row prodcomps">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-6 col-md-6 col-sm-4 col-xs-1">
			<s:textarea class="" id="prodcomps" name="customer.prodComps" label="Enter Prod Comp(s)" 
				placeholder="List primary first, separated by comma"></s:textarea>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<br>
	<br>
	<br>
	<div class="row" id="nextform">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-6 col-md-6 col-sm-4 col-xs-4">
			<button class="btn btn-primary pull-right ml-2" id="loginnext-btn" onclick="validate()">Next</button>
			<s:submit cssClass="btn btn-secondary pull-right" id="cancel-btn" value="Cancel" action="resetAction" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<br>
	</s:form>
</div>
<br>
<br>
<br>
<!-- Including footer -->
<s:include value="Footer.jsp"></s:include>
</body>
</html>