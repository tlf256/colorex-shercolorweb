<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>


<%@ taglib prefix="s" uri="/struts-tags" %>

<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title><s:text name="global.chooseProduct"/></title>
			<!-- JQuery -->
		<link rel=StyleSheet href="css/bootstrap.min.css" type="text/css">
		<link rel=StyleSheet href="css/bootstrapxtra.css" type="text/css">
		<link rel=StyleSheet href="js/smoothness/jquery-ui.css" type="text/css">
		<link rel=StyleSheet href="css/CustomerSherColorWeb.css" type="text/css"> 
		<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
		<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/jquery-ui.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/bootstrap.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.4.6.js"></script>
		<script type="text/javascript" charset="utf-8"	src="script/GetProductAutoComplete.js"></script>
		<s:set var="thisGuid" value="reqGuid" />
		
		<style>
	        .sw-bg-main {
	            background-color: ${sessionScope[thisGuid].rgbHex};
	        }
	        .chip {
	          position: relative;
			  display: -webkit-box;
			  display: -ms-flexbox;
			  display: flex;
			  -webkit-box-orient: vertical;
			  -webkit-box-direction: normal;
			  -ms-flex-direction: column;
			  flex-direction: column;
			  min-width: 10px;
			  min-height: 10px;
			  height: 52px;
			  width: 52px;
			  border-radius: 50%;
			  border: 1px solid rgba(0, 0, 0, 0.125);
			}
	    </style>
	    <script type="text/javascript">
	    	$(function(){
	    		var colorCompany;
	    		var colorID;
	    		
	    		// internationalize CUSTOM, MANUAL, and MATCH; otherwise leave color company and ID untranslated
		    	switch("${sessionScope[thisGuid].colorComp}"){
		    		case "CUSTOM":
		    			colorCompany = '<s:text name="processColorAction.custom"/>';
		    			break;
		    		default:
		    			colorCompany = "${sessionScope[thisGuid].colorComp}";
		    	}
		    	switch("${sessionScope[thisGuid].colorID}"){
		    		case "MANUAL":
		    			colorID = '<s:text name="processColorAction.manual"/>';
		    			break;
		    		case "MATCH":
		    			colorID = '<s:text name="processColorAction.match"/>';
		    			break;
		    		default:
		    			colorID = "${sessionScope[thisGuid].colorID}";
		    	}
		    	$("#colorComp").text(colorCompany);
		    	$("#colorID").text(colorID);
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

			<div class="row">
				<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
				</div>
				<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
					<s:iterator value="#session[reqGuid].jobFieldList" status="stat">
						<strong><s:property value="screenLabel"/><s:text name="global.colonDelimiter"/></strong><br>
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

			<s:form action="productUserNextAction" validate="true" focusElement="partialProductNameOrId" theme="bootstrap">
				<div class="row">
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
					</div>
					<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
						<strong><s:text name="global.colorCompanyColon"/></strong>
					</div>
					<div class="col-lg-4 col-md-4 col-sm-7 col-xs-8">
						<span id="colorComp"></span>
					</div>
					<div class="col-lg-4 col-md-4 col-sm-1 col-xs-0">
					</div>
				</div>
				<div class="row">
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
					</div>
					<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
						<strong><s:text name="global.colorIdColon"/></strong>
					</div>
					<div class="col-lg-4 col-md-4 col-sm-7 col-xs-8">
						<span id="colorID"></span>
					</div>
					<div class="col-lg-4 col-md-4 col-sm-1 col-xs-0">
					</div>
				</div>
				<div class="row">
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
					</div>
					<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
						<strong><s:text name="global.colorNameColon"/></strong>
					</div>
 					<div class="col-lg-3 col-md-4 col-sm-4 col-xs-6 mb-1">
						<s:property value="#session[reqGuid].colorName" /><br>
						<div class="chip sw-bg-main mt-1"></div>
				 		<s:if test="%{#session[reqGuid].closestSwColorId != null && #session[reqGuid].closestSwColorId != ''}">
						 	<em><s:text name="global.closestSWColorIs">
								<s:param>${sessionScope[thisGuid].closestSwColorId}</s:param>
								<s:param>${sessionScope[thisGuid].closestSwColorName}</s:param>
							</s:text></em> 
						</s:if>
					</div>
					<div class="col-lg-5 col-md-4 col-sm-4 col-xs-2">
					</div>
				</div>
				<div class="row">
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
					</div>
					<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
						<strong><s:text name="getProduct.interiorBasesColon"/></strong>
					</div>
					<div class="col-lg-3 col-md-4 col-sm-4 col-xs-6">
						${sessionScope[thisGuid].intBases}<br>
					</div>
					<div class="col-lg-5 col-md-4 col-sm-4 col-xs-2">
					</div>
				</div>
				<div class="row">
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
					</div>
					<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
						<strong><s:text name="getProduct.exteriorBasesColon"/></strong>
					</div>
					<div class="col-lg-3 col-md-4 col-sm-4 col-xs-6">
						${sessionScope[thisGuid].extBases}<br>
					</div>
					<div class="col-lg-5 col-md-4 col-sm-4 col-xs-2">
					</div>
				</div>

				<br>
				<br>
				<div class="row">
            		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
					</div>
					<div class="col-lg-8 col-md-8 col-sm-10 col-xs-12">
						<s:if test="hasActionMessages()">
						      <s:actionmessage/>
						</s:if>
					</div>
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
					</div>
				</div>
				<div class="row">
            		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
					</div>
					<div class="col-lg-8 col-md-8 col-sm-10 col-xs-12">
						<s:hidden name="reqGuid" id="reqGuid" value="%{reqGuid}"/>
						<s:textfield name="partialProductNameOrId" id="partialProductNameOrId" label="%{getText('global.product')}" placeholder="%{getText('getProduct.scanUPC')}" size="30" maxlength="20" cssStyle="font-size: 16px;" autofocus="autofocus" />
					</div>
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
					</div>
				</div>
				<div class="row">
						<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
						</div>
						<div class="col-lg-1 col-md-1 col-sm-1 col-xs-3">
							<s:submit cssClass="btn btn-primary pull-left" value="%{getText('global.next')}" action="productUserNextAction"/>
						</div>
						<div class="col-lg-7 col-md-7 col-sm-9 col-xs-9">
							<s:submit cssClass="btn btn-secondary" value="%{getText('global.back')}" action="productUserBackAction"/>
							<s:submit cssClass="btn btn-secondary pull-right" value="%{getText('global.cancel')}" action="userCancelAction"/>
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