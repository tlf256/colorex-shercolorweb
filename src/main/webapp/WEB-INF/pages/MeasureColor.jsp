<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>


<%@ taglib prefix="s" uri="/struts-tags" %>

<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title><s:text name="measureColor.measureColor"/></title>
			<!-- JQuery -->
		<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
		<link rel="stylesheet" href="css/bootstrapxtra.css" type="text/css">
		<link rel="stylesheet" href="js/smoothness/jquery-ui.min.css" type="text/css">
		<link rel="stylesheet" href="css/CustomerSherColorWeb.css" type="text/css"> 
		<link rel="stylesheet" href="font-awesome-4.7.0/css/font-awesome.min.css" type="text/css">
		<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/jquery-ui.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/bootstrap.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.5.3.js"></script>
		<script type="text/javascript" src="script/spectro-1.5.2.js"></script>
		<script type="text/javascript" src="script/WSWrapper.js"></script>
		<script type="text/javascript" src="script/spectroCalibrate-1.0.0.js"></script>
		<script>
		
			$(document).ready(function() {	
				ws_coloreye.receiver = RecdSpectroMessage;
				console.log("in docready");
				InitializeModelAndSerial("${sessionScope[reqGuid].spectro.model}", "${sessionScope[reqGuid].spectro.serialNbr}");
				
				var standard = $('#measureStandard').val();
				var sample = $('#measureSample').val();

				console.log('standard: ' + standard);
				console.log('sample: ' + sample);
				
				setMeasureModalTitle(standard, sample, '<s:text name="compareColors.measureStandard"/>', '<s:text name="compareColors.measureSample"/>', '<s:text name="measureColor.measureColor"/>');
				
				//this loads on startup!  
				InitializeMeasureScreen();
				
				//Get the calibration status to initialize connection.
				GetCalStatusMinUntilCalExpiration();
				
			});
			
		</script>
	</head>
	
	<body>
		<!-- including Header -->
		<s:include value="Header.jsp"></s:include>
		<s:set var="thisGuid" value="reqGuid" />
		<s:form id="calibrateForm" action="spectroCalibrateRedirectAction">
			<s:hidden name="reqGuid" id="reqGuid" value="%{reqGuid}"/>
			<s:hidden name="measureStandard" id="measureStandardCal" value="%{measureStandard}"/>
			\<s:hidden name="measureSample" id="measureSampleCal" value="%{measureSample}"/>
			<s:hidden name="closestColors" id="closestColorsCal" value="%{closestColors}"/>
			<s:hidden name="compareColors" id="compareColorsCal" value="%{compareColors}"/>
		</s:form>
		<s:form id="measure-color-form" action="MeasureColorNextAction" validate="true"  theme="bootstrap" method="post">
			<div class="container-fluid">
				<div class="row">
					<div class="col-sm-3"></div>
					<div class="col-sm-6"></div>
					<div class="col-sm-3">
						<s:hidden name="measuredCurve" id="measuredCurve" value=""/>
						<s:hidden name="reqGuid" id="reqGuid" value="%{reqGuid}"/>
						<s:hidden name="spectroModel" id="spectroModel" value="%{#session[reqGuid].spectroModel}"/>
						<s:hidden name="measureStandard" id="measureStandard" value="%{measureStandard}"/>
						<s:hidden name="measureSample" id="measureSample" value="%{measureSample}"/>
						<s:hidden name="closestColors" id="closestColors" value="%{closestColors}"/>
						<s:hidden name="compareColors" id="compareColors" value="%{compareColors}"/>
					</div>
				</div>
				<br>
				<br>
				<div class="row">
					<div class="col-sm-3"></div>
					<div class="col-sm-6">
						<h2 class="calibrate d-none"><s:text name="measureColor.initializingCalibration"/></h2>
						<h2 class="init"><s:text name="measureColor.commWithColorEye"/></h2>
					</div>
					<div class="col-sm-3"></div>
				</div>
				<div class="row">
					<div class="col-sm-3"></div>
					<div class="col-sm-6">
						
					</div>
					<div class="col-sm-3"></div>
				</div>
				<div class="row">
					<div class="col-sm-3"></div>
					<div class="col-sm-6">
	            		<h2 class="error" id="errmsg"></h2>
					</div>
					<div class="col-sm-3"></div>
				</div>
				<br>
				<br>
				<br>
				<div class="row">
					<div class="col-sm-3">
					</div>
					<div class="col-sm-4">
					</div>
					<div class="col-sm-5">
						<div class="cancel d-none">
							<s:submit cssClass="btn btn-secondary center-block" value="%{getText('global.cancel')}" action="userCancelAction"/>
						</div>
					</div>
		    	</div>
			</div>
		</s:form>
		<div class="modal fade modal-xl" tabindex="-1" role="dialog" id="measureColorModal" data-backdrop="static">
		  <div class="modal-dialog modal-lg">
		    <div class="modal-content">
		      <div class="modal-header bg-light">
		      	<s:if test="compare">
		      		<h2 class="modal-title ml-3"><s:text name="compareColors.measureSecondSample"></s:text></h2>
		      	</s:if>
		      	<s:else>
		      		<h2 class="modal-title ml-3" id="measureModalTitle"></h2>
		      	</s:else>
		        <button type="button" class="close" data-dismiss="modal" aria-label="%{getText('global.close')}" onclick="cancelMeasure()">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      <div class="modal-body">
		        <div class="container-fluid">
					<div class="row">
						<div class="col-sm-1"></div>
					</div>
					<div class="row">
						<div class="col-sm-1"></div>
	            		<div class="col-sm-10">
	            			<h3 class="swmeasure"><s:text name="measureColor.positionColorEye"/></h3>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-2"></div>
	            		<div class="col-sm-10"></div>
					</div>
					<div class="row">
						<div class="col-sm-1"></div>
	            		<div class="col-sm-10">
	            			<h3 class="swmeasure"><s:text name="measureColor.pressFirmly"/></h3>
 	            		</div>
					</div>
					<div class="row">
						<div class="col-sm-1"></div>
	            		<div class="col-sm-10"></div>
					</div>
					<div class="row">
						<div class="col-sm-1"></div>
	            		<div class="col-sm-10">
	            			<h3 class="swmeasure"></h3>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-1"></div>
	            		<div class="col-sm-10"></div>
					</div>
					<br>
					<div class="row">
						<div class="col-sm-1"></div>
	            		<div class="col-sm-10">
	            			<h5 class="swmeasure"><s:text name="measureColor.statusLightsShouldChangeRedToGreen"/></h5>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-1"></div>
	            		<div class="col-sm-10">
	            			<h5 class="swmeasure"></h5>
						</div>
					</div>
				</div>
		      </div>
		      <div class="modal-footer">
			       <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="cancelMeasure()"><s:text name="global.close"/></button>
			  </div>
		    </div>
		  </div>
		</div>
		<br>
		<br>
		<br>

		<!-- Including footer -->
		<s:include value="Footer.jsp"></s:include>
		
	</body>
</html>