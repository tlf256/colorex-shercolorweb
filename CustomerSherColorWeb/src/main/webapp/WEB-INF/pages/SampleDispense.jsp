<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title><s:text name="sampleDispense.sampleDispense"/></title>
	
<link rel=StyleSheet href="css/bootstrap.min.css" type="text/css">
<link rel=StyleSheet href="css/bootstrapxtra.css" type="text/css">
<link rel=StyleSheet href="js/smoothness/jquery-ui.css" type="text/css">
<link rel=StyleSheet href="css/CustomerSherColorWeb.css" type="text/css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
<script type="text/javascript" charset="utf-8" src="js/jquery-ui.js"></script>
<script type="text/javascript" charset="utf-8" src="js/bootstrap.min.js"></script>
<script type="text/javascript" charset="utf-8" src="js/moment.min.js"></script>
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

	$(function() {	
		// set default sample fill from can type
		updateSampleFill();
		// convert shots to decimal ounces
		calculatePartialOunces();
		
		// warn user if tinter has no can types profiled for it
		var canTypesLength = $("select[id='canTypesList']").children('option').length;
		if (canTypesLength == 0){ 
			$("#canTypesErrorText").removeClass('d-none');
			$("#dispenseSampleButton").prop('disabled', true);
		}
	});

	
	function updateSampleFill(){
		var selectedIndex = $("select[id='canTypesList'] option:selected").index();
		var factoryFill = "${factoryFill}";
		// get the sample size for this can type
		var sampleSize = $("#canTypeSampleSizes li").eq(selectedIndex).text();
		var sampleSizeFloat = parseFloat(sampleSize);
		
		if (!isNaN(sampleSizeFloat)){
			// calculate amount of base in the sample:   Sample Size / 128 * Factory Fill
			var productSampleFill = sampleSizeFloat / 128 * factoryFill;
			$("#sampleFill").val(productSampleFill);
		} else {
			console.log("problem parsing the sample size");
		}
	}
	
	function calculatePartialOunces(){
		// calculate amount of each colorant:   Sample Size / 128 * ((Number of Shots / Shot Size) * Gallon Conversion) 
		var selectedIndex = $("select[id='canTypesList'] option:selected").index();
		var sampleSize = $("#canTypeSampleSizes li").eq(selectedIndex).text();
		var sampleSizeFloat = parseFloat(sampleSize);
		var sizeConversion = "${sizeConversion}";
		var dispenseFloor = "${dispenseFloor}";
		var dispenseFloorFloat = parseFloat(dispenseFloor);
		var sizeConvFloat = parseFloat(sizeConversion);
		
		// re-enable dispense and remove error text when user updates dropdown, then re-check colorant amounts
		$("#dispenseFloorErrorText").addClass('d-none');
		$("#dispenseSampleButton").prop('disabled', false);
		
		if (!isNaN(sampleSizeFloat) && !isNaN(sizeConvFloat)){
			$(".formulaRow").each(function(){
				var numShots = $(this).find('.numShots').text();
				var shotSize = $(this).find('.shotSize').text();
				var shotsToOunces = Number(numShots) / Number(shotSize);
				if (!isNaN(shotsToOunces)){
					var partialOunces = sampleSizeFloat / 128 * (shotsToOunces * sizeConvFloat);
					$(this).find('.partialOunces').text(partialOunces);
					// warn user if colorant amount is lower than tinter dispense floor, make them choose a larger can type
					if (!isNaN(dispenseFloorFloat) && partialOunces < dispenseFloorFloat){
						$("#dispenseFloorErrorText").removeClass('d-none');
						$("#dispenseSampleButton").prop('disabled', true);
					}
				} else {
					console.log("problem calculating shots to ounces");
				}
			});
		} else {
			console.log("problem parsing the sample size or conversion size");
		}
	}

</script>
</head>

<body>

<!-- including Header -->
	<s:include value="Header.jsp"></s:include>

	<div class="container-fluid">
		<div class="row">
			<div class="col-sm-3"></div>
			<div class="col-sm-6"></div>
			<div class="col-sm-3">
				<s:set var="thisGuid" value="reqGuid" />
			</div>
		</div>
		<br>
		<div class="row">
			<div class="col-sm-2"></div>
		</div>

		<s:if test="%{#session[reqGuid].controlNbr != null && #session[reqGuid].controlNbr > 0}">
		<div class="row" id="controlNbrDisplay">
		</s:if>
		<s:else>
		<div class="row" id="controlNbrDisplay" hidden="true">
		</s:else>
 
			<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
			<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
				<strong><s:text name="global.jobNumber"/></strong>
			</div>
			<div class="col-lg-4 col-md-6 col-sm-7 col-xs-8" id="controlNbr">
				${sessionScope[thisGuid].controlNbr}</div>
			<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0"></div>
		</div>
	</div>
	<div class="row">
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
		<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
			<s:iterator value="#session[reqGuid].jobFieldList" status="stat">
				<strong><s:property value="screenLabel" /><s:text name="global.colonDelimiter"/></strong>
				<br>
			</s:iterator>
		</div>
		<div class="col-lg-4 col-md-6 col-sm-7 col-xs-8">
			<s:iterator value="#session[reqGuid].jobFieldList" status="stat">
				<s:property value="enteredValue" />
				<br>
			</s:iterator>
		</div>
		<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0"></div>
	</div>
	<br>

	<div class="row">
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
		<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
			<strong><s:text name="global.colorCompanyColon"/></strong>
		</div>
		<div class="col-lg-4 col-md-6 col-sm-7 col-xs-8">
			${sessionScope[thisGuid].colorComp}</div>
		<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0"></div>
	</div>
	<div class="row">
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
		<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
			<strong><s:text name="global.colorIdColon"/></strong>
		</div>
		<div class="col-lg-4 col-md-6 col-sm-7 col-xs-8">
			<s:property value="#session[reqGuid].colorID" /></div>
		<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0"></div>
	</div>
	<div class="row">
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
		<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
			<strong><s:text name="global.colorNameColon"/></strong>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-4 col-xs-6 mb-1">
			<s:property value="#session[reqGuid].colorName" /><br>
			<div class="chip sw-bg-main mt-1"></div>
			<s:if test="%{#session[reqGuid].closestSwColorId != null && #session[reqGuid].closestSwColorId != ''}">
				<em>
					<s:text name="global.closestSWColorIs">
	 					<s:param><s:property value="#session[reqGuid].closestSwColorId" /></s:param>
						<s:param><s:property value="#session[reqGuid].closestSwColorName" /></s:param>
					</s:text>
				</em>
			</s:if>
		</div>
		<div class="col-lg-5 col-md-5 col-sm-4 col-xs-2"></div>
	</div>
	<div class="row">
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
		<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
			<strong><s:text name="global.salesNumberColon"/></strong>
		</div>
		<div class="col-lg-4 col-md-6 col-sm-7 col-xs-8">
			${sessionScope[thisGuid].salesNbr}<br>
		</div>
		<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0"></div>
	</div>
	<div class="row">
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
		<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
			<strong><s:text name="global.productNumberColon"/></strong>
		</div>
		<div class="col-lg-4 col-md-6 col-sm-7 col-xs-8">
			${sessionScope[thisGuid].prodNbr} -
			${sessionScope[thisGuid].sizeText}</div>
		<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0"></div>
	</div>
	<div class="row">
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
		<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
			<strong><s:text name="global.productDescrColon"/></strong>
		</div>
		<div class="col-lg-4 col-md-6 col-sm-7 col-xs-8">
			${sessionScope[thisGuid].intExt} ${sessionScope[thisGuid].quality}
			${sessionScope[thisGuid].composite} ${sessionScope[thisGuid].finish}<br>
			${sessionScope[thisGuid].base}<br>
		</div>
		<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0"></div>
	</div>
	
	<s:if test="%{accountUsesRoomByRoom==true}">
		<div class="row">
			<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
			<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
				<strong><s:text name="displayFormula.roomByRoomColon"/></strong>
			</div>
			<div class="col-lg-3 col-md-6 col-sm-7 col-xs-8">
				${sessionScope[thisGuid].roomByRoom}
			</div>
			<div class="col-lg-5 col-md-2 col-sm-1 col-xs-0"></div>
		</div>
	</s:if>
	<div class="row">
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
		<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
			<strong><s:text name="sampleDispense.factoryFillColon"/></strong>
		</div>
		<div class="col-lg-3 col-md-6 col-sm-7 col-xs-8">
			<s:text name="%{factoryFill}"/>
		</div>
		<div class="col-lg-5 col-md-2 col-sm-1 col-xs-0"></div>
	</div>
	<br>
	
	<s:form action="" validate="true" theme="bootstrap">
	<s:hidden name="reqGuid" value="%{reqGuid}"  id="reqGuid"/>
					
	<div class="row mt-4">
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
		<div class="col-lg-5 col-md-8 col-sm-10 col-xs-12">
			<s:select id="canTypesList" onchange="updateSampleFill(); calculatePartialOunces();" list="canTypesList" autofocus="true"
				listKey="canType" listValue="canType" label="%{getText('sampleDispense.canType')}"/>
			<ul class="d-none" id="canTypeSampleSizes">
			<s:iterator value="canTypesList" status="outerStat">
				<li class="sampleSize"><s:property value="sampleSize" /></li>
			</s:iterator>
			</ul>
			<div id="canTypesErrorText" style="color:red" class="d-none">
				<s:text name="sampleDispense.noCanTypesProfiledForTinter"/>
			</div>
		</div>
		<div class="col-lg-5 col-md-2 col-sm-1 col-xs-0"></div>
	</div>
	<div class="row mt-3">
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
		<div class="col-lg-5 col-md-8 col-sm-10 col-xs-12">
			<s:textfield id="sampleFill" readonly="true" label="%{getText('sampleDispense.productSampleFill')}"/>
		</div>
		<div class="col-lg-5 col-md-2 col-sm-1 col-xs-0"></div>
	</div>
	<div class="row mt-2">
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
		<div class="col-lg-5 col-md-8 col-sm-10 col-xs-12">
			<input type="checkbox" id="includeBaseCheckBox" value="includeBase">
			<label for="includeBaseCheckBox"><s:text name="sampleDispense.includeBaseInDispense"/></label>
		</div>
		<div class="col-lg-5 col-md-2 col-sm-1 col-xs-0"></div>
	</div>
	
	<br>
	<div class="row mt-2">
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
		<div class="col-lg-5 col-md-8 col-sm-10 col-xs-12">
			<table class="table">
				<thead>
					<tr>
						<th class="bg-light"><strong>${sessionScope[thisGuid].displayFormula.clrntSysId}*<s:text name="global.colorant"/></strong></th>
						<th class="bg-light"><strong>${sessionScope[thisGuid].displayFormula.incrementHdr[0]}</strong></th>
					</tr>
				</thead>
				<tbody>
					<s:iterator value="displayFormula.ingredients" status="i">
						<tr id="<s:property value="%{#i.index}"/>" class="formulaRow">
							<td class="col-lg-5 col-md-6 col-sm-4 col-xs-4">
									<s:property value="tintSysId" />-<s:property value="name" /></td>
							<td class="numShots d-none"><s:property value="shots" /></td>
							<td class="shotSize d-none"><s:property value="shotSize" /></td>
							<td class="partialOunces"></td>
						</tr>
					</s:iterator>
				</tbody>
			</table>
			<div id="dispenseFloorErrorText" style="color:red" class="d-none">
				<s:text name="sampleDispense.colorantLowerThanDispenseFloor"/>
			</div>
		</div>
	</div>
	<br>
		
	<div class="row mt-2" id="dispenseInfoRow">
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
		<s:if test="%{siteHasTinter==true}">
			<div class="col-lg-6 col-md-8 col-sm-10 col-xs-12">
				<strong><s:text name="displayFormula.qtyDispensedColon"/></strong> 
				<span class="dispenseInfo badge badge-secondary" style="font-size: .9rem;" id="qtyDispensed">
					${sessionScope[thisGuid].quantityDispensed}</span>
				<strong class="dispenseInfo pull-right" id="dispenseStatus"></strong>
			</div>
		</s:if>
		<s:else>
			<div class="col-lg-6 col-md-8 col-sm-10 col-xs-12">
				<strong><s:text name="displayFormula.qtyDispensedColon"/></strong> 
				<span class="dispenseInfo d-none badge badge-secondary" style="font-size: .8rem;" id="qtyDispensed">
					${sessionScope[thisGuid].quantityDispensed}</span>
				<strong class="dispenseInfo d-none pull-right" id="dispenseStatus"></strong>
			</div>	
		</s:else>
		<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0"></div>
	</div>
	<br>
	
	<div class="d-flex flex-row justify-content-around mt-3">
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0 p-2"></div>
		<div class="col-lg-6 col-md-8 col-sm-10 col-xs-12 p-2">
			<s:submit cssClass="btn btn-primary" autofocus="autofocus" id="dispenseSampleButton" value="%{getText('global.dispenseSample')}"
					onclick="return false;" />
			<button type="button" class="btn btn-secondary" id="drawdownLabelPrint"
					onclick=""><s:text name="global.drawdownLabel"/></button>
			<button type="button" class="btn btn-secondary" id="sampleCanLabelPrint"
					onclick=""><s:text name="sampleDispense.canLabel"/></button>
			<button type="button" class="btn btn-secondary" id="storeLabelPrint"
					onclick=""><s:text name="global.storeLabel"/></button>
			<s:submit cssClass="btn btn-secondary pull-right" value="%{getText('sampleDispense.done')}"
                   	action="userCancelAction" />
        </div>
		<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0 p-2"></div>
	</div>
	</s:form>

</body>
</html>