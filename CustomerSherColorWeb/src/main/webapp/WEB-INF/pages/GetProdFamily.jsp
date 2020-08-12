<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!doctype html>

<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title>Better Performance With Different Base</title>
			<!-- JQuery -->
		<link rel=StyleSheet href="css/bootstrap.min.css" type="text/css">
		<link rel=StyleSheet href="css/bootstrapxtra.css" type="text/css">
		<link rel=StyleSheet href="js/smoothness/jquery-ui.css" type="text/css">
		<link rel=StyleSheet href="css/CustomerSherColorWeb.css" type="text/css"> 
		<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
		<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/jquery-ui.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/bootstrap.min.js"></script>
		<script type="text/javascript" src="https://cdn.datatables.net/1.10.16/js/jquery.dataTables.min.js"></script>
		<script type="text/javascript" src="https://cdn.datatables.net/1.10.16/js/dataTables.bootstrap4.min.js"></script>
		<script type="text/javascript" src="https://cdn.datatables.net/buttons/1.5.1/js/dataTables.buttons.min.js"></script>
		<script type="text/javascript" src="https://cdn.datatables.net/buttons/1.5.1/js/buttons.bootstrap4.min.js"></script> 
		<script type="text/javascript" src="https://cdn.datatables.net/buttons/1.5.1/js/buttons.colVis.min.js"></script>
		<script type="text/javascript" src="https://cdn.datatables.net/buttons/1.5.1/js/buttons.html5.min.js"></script>
		<script type="text/javascript" src="https://cdn.datatables.net/buttons/1.5.1/js/buttons.print.min.js"></script>
		<style type="text/css">
			.table-hover tbody tr:hover{
				background: #FFFF99;
				cursor: pointer;
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
				</div>
			</div>
			<br>
			<br>
			<div class="row">
				<div class="col-sm-2">
				</div>
			</div>
			<s:form id="GetProdFamily" action="GetProductFamilyAction" validate="true" focusElement="prodFambly" theme="bootstrap">
				<div class="row">
					<div class="col-sm-2">
						<s:hidden name="reqGuid" value="%{reqGuid}"/>
						<s:hidden id="selectedProdFamily" name="selectedProdFamily" />
					</div>
					<div class="col-sm-2">
						<s:set var="reqGuid" value="reqGuid" />
					</div>
				</div>
				<div class="row">
					<div class="col-sm-1">
					</div>
					<div class="col-sm-10">
						<h5>Better Performance Found in Different Base</h5>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-1">
					</div>
					<div class="col-sm-10">
					</div>
				</div>
				<br>
				<div id="prodFamily" class="row">
					<div class="col-sm-1"></div>
					<div class="col-sm-10">
					<table id="prodFamily_table" class="table table-striped table-bordered">
						<caption style="caption-side:top;">Choose Product</caption>
						<thead>
							<tr>
								<th></th>
								<th>Product</th>
								<th>Quality</th>
								<th>Base</th>
								<th>Delta-E</th>
								<th>Contrast Ratio</th>
								<th>Comment</th>
								<th>Formula</th>
							</tr>
						</thead>
						<tbody>
							<s:iterator var="set" value="colorProdFamilies.entrySet()" status="i">
								<tr class="border-bottom-1 border-dark">
									<td><input type="radio" class="prodFamRadio" name="prodFamily" /></td>
									<s:iterator var="item" value="#set.value.split('-')">
										<td class="prodDetail">
											<s:property />
										</td>
									</s:iterator>
									<td>
										<s:iterator value="#session[reqGuid].formResponse.getFormulas().get(#i.index)">
											<div class="row">
												<div class="col-sm-5">
													<strong><s:property value="clrntSysId"/>*COLORANT</strong>
												</div>
												<div class="col-sm-1" align="center">
													<strong><s:property value="incrementHdr[0]"/></strong>
												</div>
												<div class="col-sm-1" align="center">
													<strong><s:property value="incrementHdr[1]"/></strong>
												</div>
												<div class="col-sm-1" align="center">
													<strong><s:property value="incrementHdr[2]"/></strong>
												</div>
												<div class="col-sm-1" align="center">
													<strong><s:property value="incrementHdr[3]"/></strong>
												</div>
											</div>
										</s:iterator>
										<s:iterator value="bothFormulas.get(#i.index).ingredients" status="i">
											<div class="row">
												<div class="col-sm-5">
													<s:property value="tintSysId"/>-<s:property value="name"/> 
												</div>
												<s:iterator value="increment" status="stat">
													<div class="col-sm-1" align="center">
														<s:property value="top"/>
													</div>
												</s:iterator>
											</div>
										</s:iterator>
									</td>
								</tr>
							</s:iterator>
						</tbody>
					</table>
					</div>
					<div class="col-sm-1"></div>
				</div>
				<br>
				<div class="row">
					<div class="col-sm-1">
					</div>	
					<div class="col-sm-10">
						<s:submit cssClass="btn btn-primary ml-3" id="submitNext" value="Next" action="prodFamilyUserNextAction"/>
		    			<s:submit cssClass="btn btn-secondary pull-right mr-3" value="Cancel" action="userCancelAction"/>
		    		</div>
		    	</div>
			</s:form>
			<br>
			<script>
			$(document).ready(function() {
				var prodTable = $('#prodFamily_table').DataTable({
					dom: 'rtp',
			        "language": {
			        	"emptyTable" : "No products found"
			        },
			        "ordering": true,
			        "order": [ 5, 'asc' ],
			        "paginate": false,
			        "pagingType": "full",
			    });
				
				$('#prodFamily_table').addClass('table-hover');
				
				$('#prodFamily_table tbody').on('click','tr',function(event){
			    	var prodNbr = prodTable.row(this).data()[1];
			    	var index = prodTable.row(this).index();
			    	console.log("row index " + index);
			    	console.log("prod number " + prodNbr);
			    	$("#selectedProdFamily").val(prodNbr);
			    	$(".prodFamRadio:eq("+index+")").prop('checked', true);
			    });
			});

			</script>
	
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