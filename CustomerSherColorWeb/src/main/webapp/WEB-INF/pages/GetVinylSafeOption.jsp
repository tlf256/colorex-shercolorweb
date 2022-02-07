<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>


<%@ taglib prefix="s" uri="/struts-tags" %>

<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title><s:text name="getVinylSafeOption.vinylSafeFormula"/></title>
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
					${sessionScope[thisGuid].colorComp}
				</div>
			</div>
			<div class="row">
				<div class="col-sm-2">
				</div>
				<div class="col-sm-2">
					<strong><s:text name="global.colorIdColon"/></strong>
				</div>
				<div class="col-sm-6">
					${sessionScope[thisGuid].colorID}
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
	
			<s:form action="GetVinylSafeAction" validate="true" theme="bootstrap">						
				<div class="row">
 					<div class="col-sm-2">
					</div>
            		<div class="col-sm-8">
						<s:text name="getVinylSafeOption.canBeUsedForVinylSafe"/>
					</div>
				</div>
				<br>
				
				<div class="row">
 					<div class="col-sm-2">
					</div>
            		<div class="col-sm-8">
            			<strong><s:text name="getVinylSafeOption.createVinylSafe"/></strong>
					</div>
				</div>
				
				<div class="row">
 					<div class="col-sm-2">
					</div>
            		<div class="col-sm-4 d-none">
						<s:hidden name="reqGuid" value="%{reqGuid}"/>
						<s:checkbox id="makeVinylSafeFormulaCk" name="makeVinylSafeFormula" label=""/>
					</div>
				</div>
				<br>				
					
				<div class="row mt-3">
					<div class="col-sm-2">
					</div>	
					<div class="col-sm-1">
						<s:submit cssClass="btn btn-primary" value="%{getText('global.yes')}" onclick="chooseYes();" action="vsUserNextAction"/>
					</div>	
					<div class="col-sm-1">
						<s:submit cssClass="btn btn-primary" value="%{getText('global.no')}" onclick="chooseNo();" action="vsUserNextAction"/>
					</div>
					<div class="col-sm-1">	
						<s:submit cssClass="btn btn-secondary" value="%{getText('global.back')}" action="vsUserBackAction"/>
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
		 $(window).on('load', function () {
			 $( "#makeVinylSafeFormulaCk" ).focus();
		 });
		 
		 function chooseYes(){
			 $("#makeVinylSafeFormulaCk").prop("checked", true);
		 }
		 
		 function chooseNo(){
			 $("#makeVinylSafeFormulaCk").prop("checked", false);
		 }
		

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