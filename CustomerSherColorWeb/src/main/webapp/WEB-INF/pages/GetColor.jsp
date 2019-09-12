<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>

<%@ taglib prefix="s" uri="/struts-tags" %>
 
<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title>Choose Color</title>
			<!-- JQuery -->
		<link rel=StyleSheet href="css/bootstrap.min.css" type="text/css">
		<link rel=StyleSheet href="css/bootstrapxtra.css" type="text/css">
		<link rel=StyleSheet href="js/smoothness/jquery-ui.css" type="text/css">
		<link rel=StyleSheet href="css/CustomerSherColorWeb.css" type="text/css"> 	
		<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
		<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/jquery-ui.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/bootstrap.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/CustomerSherColorWeb.js"></script>
		<script type="text/javascript" charset="utf-8"	src="script/GetColorAutoComplete-1.3.1.js"></script>
		<script type="text/javascript" charset="utf-8">
			$(function(){
				$("[id^=selectedCoTypes]").change(function(){
					if(this.checked) {
						$('.form-control-feedback, .help-block').remove();
						$('.has-feedback').removeClass('has-error has-feedback');
						$('#partialColorNameOrId').val('');
				    }
				});
			});
		</script>
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
				</div>
			</div>
			<br>
			<div class="row">
				<div class="col-sm-2">
				</div>
			</div>

<%-- 			this guid is <s:property value="thisGuid"/> --%>
<%-- 			this guid is <s:property value="%{reqGuid}"/> --%>
<!-- 			<br> -->
<%-- 			this sess is <s:property value="#session"/> --%>
<!-- 			<br> -->
<%-- 			jf obj is <s:property value="#session[reqGuid].jobFieldList"/> --%>
			
			<div class="row">
				<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
				</div>
				<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
					<s:iterator value="#session[reqGuid].jobFieldList" status="stat">
						<strong><s:property value="screenLabel"/>:</strong><br>
					</s:iterator>
				</div>
				<div class="col-lg-4 col-md-4 col-sm-7 col-xs-8">
					<s:iterator value="#session[reqGuid].jobFieldList" status="stat">
						<s:property value="enteredValue"/><br>
					</s:iterator>	
				</div>
				<div class="col-lg-4 col-md-4 col-sm-1 col-xs-0">
				</div>
			</div>
<br>
			<s:form action="colorUserNextAction" validate="true" focusElement="partialColorNameOrId" theme="bootstrap">
				<div class="row">
						<div class="col-lg-2 col-md-2 col-sm-1">
						</div>
	            		<div class="col-lg-8 col-md-8 col-sm-10">
	            			<div class="form-group">
	            				<strong>Color Type:</strong>
	            				<div class="controls">
	            					<s:iterator value="cotypes" status="i">
		            					<div class="form-check">
		            					  <s:if test="%{#i.index == 0}">
		            					  	<input class="form-check-input" type="radio" name="selectedCoTypes" value='<s:property value="key"/>' id="selectedCoTypes-<s:property value="%{#i.index}"/>" checked>
		            					  </s:if>
		            					  <s:else>
		            					  	<input class="form-check-input" type="radio" name="selectedCoTypes" value='<s:property value="key"/>' id="selectedCoTypes-<s:property value="%{#i.index}"/>">
		            					  </s:else>
										  <label class="form-check-label font-weight-normal" for="selectedCoTypes-<s:property value="%{#i.index}"/>">
										    <s:property value="value"/>
										  </label>
										</div>
		            				</s:iterator>
	            				</div>
	            			</div>
 	            			<%-- <s:radio label="Color Type" name="selectedCoTypes" list="cotypes" value="defaultCoTypeValue" /> --%>
						</div>
						<div class="col-lg-2 col-md-2 col-sm-1">
						</div>
				</div>
				<br>
	<%-- 			<div class="row">
					<div class="col-sm-3">
					</div>
					<div class="col-sm-6">
						<s:fielderror/>	
					</div>
				</div>--%>
				<div class="row">
						<div class="col-lg-2 col-md-2 col-sm-1">
							<s:hidden name="reqGuid" id="reqGuid" value="%{reqGuid}"/>
							<s:hidden name="colorData" id="colorData" value=""/>
						</div>
						<div class="col-lg-8 col-md-8 col-sm-10">
							<s:textfield name="partialColorNameOrId" id="partialColorNameOrId" label="Enter Color Name or Number" placeholder="Choose color type, then enter color name or number here" size="30" maxlength="30" cssStyle="font-size: 16px;" autofocus="autofocus"  />
						</div>
						<div class="col-lg-2 col-md-2 col-sm-1">
						</div>
				</div>
				<div class="row">
						<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
						</div>	
						<div class="col-lg-1 col-md-1 col-sm-1 col-xs-3">
							<s:submit cssClass="btn btn-primary" value="Next" action="colorUserNextAction"/>
						</div>
						<div class="col-lg-7 col-md-7 col-sm-9 col-xs-9">	
							<s:submit cssClass="btn btn-secondary pull-right" value="Cancel" action="userCancelAction"/>
						</div>
						<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">	
			    		</div>
		    	</div>
			</s:form>
		</div>
		
				<br>
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