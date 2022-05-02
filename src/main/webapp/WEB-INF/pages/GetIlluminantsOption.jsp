<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>


<%@ taglib prefix="s" uri="/struts-tags" %>

<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title><s:text name="getIlluminantsOption.primaryLightSource"/></title>
			<!-- JQuery -->
		<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
		<link rel="stylesheet" href="css/bootstrapxtra.css" type="text/css">
		<link rel="stylesheet" href="js/smoothness/jquery-ui.min.css" type="text/css">
		<link rel="stylesheet" href="css/CustomerSherColorWeb.css" type="text/css"> 	
		<link rel="stylesheet" href="font-awesome-4.7.0/css/font-awesome.min.css" type="text/css">
		<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/jquery-ui.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/bootstrap.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.4.6.js"></script>
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
				<div class="col-sm-2">
				</div>
				<div class="col-sm-2">
					<strong><s:text name="global.colorCompanyColon"/></strong>
				</div>
				<div class="col-sm-6">
					<span id="colorComp"></span>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-2">
				</div>
				<div class="col-sm-2">
					<strong><s:text name="global.colorIdColon"/></strong>
				</div>
				<div class="col-sm-6">
					<span id="colorID"></span>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-2">
				</div>
				<div class="col-sm-2">
					<strong><s:text name="global.colorNameColon"/></strong>
				</div>
				<div class="col-sm-6">
					${sessionScope[thisGuid].colorName}
				</div>
			</div>
			<div class="row mb-1">
				<div class="col-sm-2">
				</div>
				<div class="col-sm-2">
				</div>
				<div class="col-sm-4">
					<div class="chip sw-bg-main mt-1"></div>
					<s:if test="%{#session[reqGuid].closestSwColorId != null && #session[reqGuid].closestSwColorId != ''}">
						<em><s:text name="global.closestSWColorIs">
							<s:param>${sessionScope[thisGuid].closestSwColorId}</s:param>
							<s:param>${sessionScope[thisGuid].closestSwColorName}</s:param>
						</s:text></em>
					</s:if>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-2">
				</div>
				<div class="col-sm-2">
					<strong><s:text name="global.salesNumberColon"/></strong>
				</div>
				<div class="col-sm-6">
					${sessionScope[thisGuid].salesNbr} ${sessionScope[thisGuid].quality} ${sessionScope[thisGuid].composite} ${sessionScope[thisGuid].finish}
				</div>
			</div>
						<div class="row">
				<div class="col-sm-2">
				</div>
				<div class="col-sm-2">
					
				</div>
				<div class="col-sm-6">
					${sessionScope[thisGuid].prodNbr} ${sessionScope[thisGuid].intExt} ${sessionScope[thisGuid].klass} 
				</div>
			</div>

			<br>
			<br>
			<br>
	
			<s:form action="GetLightSourceAction" validate="true" theme="bootstrap">
				<div class="row">
	            
  						<div class="col-sm-2">
						</div>
	            		<div class="col-sm-4">
						</div>					
				</div>
				<div class="row">
  						<div class="col-sm-2">
						</div>
	            		<div class="col-sm-4">
   								<s:hidden name="reqGuid" value="%{reqGuid}"/>
   								<div class="form-group">
			           				<strong><s:text name="getIlluminantsOption.choosePrimaryLightSource"/></strong>
			           				<div class="controls">
			           					<s:iterator value="lightSources" status="i">
			            					<div class="form-check">
			            					  <s:if test="%{#i.index == 0}">
			            					  	<input class="form-check-input" type="radio" name="selectedLightSources" value='<s:property value="key"/>' id="selectedLightSources-<s:property value="%{#i.index}"/>" checked>
			            					  </s:if>
			            					  <s:else>
			            					  	<input class="form-check-input" type="radio" name="selectedLightSources" value='<s:property value="key"/>' id="selectedLightSources-<s:property value="%{#i.index}"/>">
			            					  </s:else>
											  <label class="form-check-label font-weight-normal" for="selectedLightSources-<s:property value="%{#i.index}"/>">
											    <s:property value="value"/>
											  </label>
											</div>
			            				</s:iterator>
			           				</div>
			           			</div>
   								<%-- <s:radio label="Choose Primary Light Source" name="selectedLightSources" list="lightSources" value="defaultLightSourceValue" /> --%>
						</div>
				</div>
				
					
				<div class="row">				
						<div class="col-sm-2">
						</div>	
						<div class="col-sm-2">
							<s:submit cssClass="btn btn-primary" value="%{getText('global.next')}" action="lsUserNextAction" autofocus="autofocus"/>
						</div>
						<div class="col-sm-2">	
							<s:submit cssClass="btn btn-secondary" value="%{getText('global.back')}" action="lsUserBackAction"/>
   						</div>
						<div class="col-sm-2">
						</div>
						<div class="col-sm-2">
			    			<s:submit cssClass="btn btn-secondary" value="%{getText('global.cancel')}" action="userCancelAction"/>
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