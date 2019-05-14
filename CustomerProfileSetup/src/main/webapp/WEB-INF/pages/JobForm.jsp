<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
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
<script type="text/javascript" charset="utf-8" src="script/JobForm.js"></script>
<title>Job Field Form</title>
</head>
<body>
<!-- including Header -->
<s:include value="Header.jsp"></s:include>
<div class="container-fluid">
	<br>
	<div class="row">
		<div class="col-md-3 col-sm-2"></div>
		<div class="col-sm-2">
			<strong>Customer ID:</strong>
		</div>
		<div class="col-sm-2">
			${sessionScope["CustomerDetail"].customerId}
		</div>
	</div>
	<div class="row">
		<div class="col-md-3 col-sm-2"></div>
		<div class="col-sm-2">
			<strong>Customer Name:</strong>
		</div>
		<div class="col-sm-2">
			${sessionScope["CustomerDetail"].swuiTitle}
		</div>
	</div>
	<br>
	<div class="row">
		<div class="col-md-3 col-sm-2"></div>
		<div class="col-md-6 col-sm-2">
			<h3>Enter Job Field Information</h3>
		</div>
		<div class="col-md-6 col-sm-2"></div>
	</div>
	<br>
	<s:form id="jobForm" action="getJobInfo" method="post">
	<div class="row">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1">
			<!--<s:hidden name="updateMode" value="%{updateMode}" />-->
		</div>
		<div class="col-lg-6 col-md-6 col-sm-4 col-xs-4">
			<div id="formerror" class="text-danger"></div>
			<div id="jobformerror" class="text-danger"></div>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
	</div>
	<div class="row" id="columnHead">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
			<strong>Screen Label</strong>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
			<strong>Default Value</strong>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1">
			<strong>Required</strong>
		</div>
	</div>
	<br>
	<div class="row" id="row0">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1">
			<strong class="float-right mt-1">1</strong>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
			<s:textfield name="job.screenLabel" class="scrnlbl" id="locField" value="Location Name" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
			<s:textfield name="job.fieldDefault" class="flddefault" id="locDefault" value="Main Campus" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1">
			<input type="checkbox" id="locReq" name="job.entryRequired" class="font-weight-normal required mt-2 ml-2" value="0" checked="checked"></input>
		</div>
	</div>
	<div class="row" id="row1">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1">
			<strong class="float-right mt-1">2</strong>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
			<s:textfield name="job.screenLabel" class="scrnlbl" id="bldgField" value="Building Code" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
			<s:textfield name="job.fieldDefault" class="flddefault" id="bldgCode" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1">
			<input type="checkbox" id="bldgReq" name="job.entryRequired" class="font-weight-normal required mt-2 ml-2" value="1"></input>
		</div>
	</div>
	<div class="row" id="row2">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1">
			<strong class="float-right mt-1">3</strong>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
			<s:textfield name="job.screenLabel" class="scrnlbl" id="floorField" value="Floor #" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
			<s:textfield name="job.fieldDefault" class="flddefault" id="floor" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1">
			<input type="checkbox" id="floorReq" name="job.entryRequired" class="font-weight-normal required mt-2 ml-2" value="2"></input>
		</div>
	</div>
	<div class="row" id="row3">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1">
			<strong class="float-right mt-1">4</strong>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
			<s:textfield name="job.screenLabel" class="scrnlbl" id="roomField" value="Room #" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
			<s:textfield name="job.fieldDefault" class="flddefault" id="room" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1">
			<input type="checkbox" id="roomReq" name="job.entryRequired" class="font-weight-normal required mt-2 ml-2" value="3"></input>
		</div>
	</div>
	<div class="row" id="row4">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1">
			<strong class="float-right mt-1">5</strong>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
			<s:textfield name="job.screenLabel" class="scrnlbl" id="surfaceField" value="Surface Type" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
			<s:textfield name="job.fieldDefault" class="flddefault" id="surface" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1">
			<input type="checkbox" id="surfaceReq" name="job.entryRequired" class="font-weight-normal required mt-2 ml-2" value="4"></input>
		</div>
	</div>
	<div class="row" id="row5">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1">
			<strong class="float-right mt-1">6</strong>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
			<s:textfield name="job.screenLabel" class="scrnlbl" id="commentField" value="Comment" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
			<s:textfield name="job.fieldDefault" class="flddefault" id="comment" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1">
			<input type="checkbox" id="commentReq" name="job.entryRequired" class="font-weight-normal required mt-2 ml-2" value="5"></input>
		</div>
	</div>
	<div class="row" id="row6">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1">
			<strong class="float-right mt-1">7</strong>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
			<s:textfield name="job.screenLabel" class="scrnlbl" id="jobField" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
			<s:textfield name="job.fieldDefault" class="flddefault" id="jobDefault" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1">
			<input type="checkbox" id="jobRequired" name="job.entryRequired" class="font-weight-normal required mt-2 ml-2" value="6"></input>
		</div>
	</div>
	<div class="row" id="row7">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1">
			<strong class="float-right mt-1">8</strong>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
			<s:textfield name="job.screenLabel" class="scrnlbl" id="jobField1" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
			<s:textfield name="job.fieldDefault" class="flddefault" id="jobDefault1" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1">
			<input type="checkbox" id="jobRequired1" name="job.entryRequired" class="font-weight-normal required mt-2 ml-2" value="7"></input>
		</div>
	</div>
	<div class="row" id="row8">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1">
			<strong class="float-right mt-1">9</strong>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
			<s:textfield name="job.screenLabel" class="scrnlbl" id="jobField2" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
			<s:textfield name="job.fieldDefault" class="flddefault" id="jobDefault2" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1">
			<input type="checkbox" id="jobRequired2" name="job.entryRequired" class="font-weight-normal required mt-2 ml-2" value="8"></input>
		</div>
	</div>
	<div class="row" id="row9">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1">
			<strong class="float-right mt-1">10</strong>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
			<s:textfield name="job.screenLabel" class="scrnlbl" id="jobField3" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-2 col-xs-2">
			<s:textfield name="job.fieldDefault" class="flddefault" id="jobDefault3" />
		</div>
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1">
			<input type="checkbox" id="jobRequired3" name="job.entryRequired" class="font-weight-normal required mt-2 ml-2" value="9"></input>
		</div>
	</div>
	<br>
	<br>
	<div class="row" id="jobinfo_btn">
		<div class="col-lg-3 col-md-3 col-sm-1 col-xs-1"></div>
		<div class="col-lg-6 col-md-6 col-sm-4 col-xs-4">
			<s:submit cssClass="btn btn-primary pull-right ml-2" id="next-btn" value="Next" />
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