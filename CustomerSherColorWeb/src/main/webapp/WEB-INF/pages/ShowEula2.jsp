<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%-- <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> --%>

<!doctype html>
<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>End User License Agreement</title>
		<link rel=StyleSheet href="css/bootstrap.min.css" type="text/css">
		<link rel=StyleSheet href="css/bootstrapxtra.css" type="text/css">
		<link rel=StyleSheet href="css/CustomerSherColorWeb.css" type="text/css"> 
		<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
		<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="js/jquery-ui.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/popper.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/bootstrap.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/moment.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/CustomerSherColorWeb.js"></script>

	
	<style>
		.dropdown-submenu {
		    position: relative;
		}
		.dropdown-submenu>.dropdown-menu {
		    top: 0;
		    left: 100%;
		    margin-top: -6px;
		    margin-left: -1px;
		    -webkit-border-radius: 0 6px 6px 6px;
		    -moz-border-radius: 0 6px 6px;
		    border-radius: 0 6px 6px 6px;
		}
		.dropdown-submenu:hover>.dropdown-menu {
	 	   display: block;
		}
		.dropdown-submenu>a:after {
		    display: block;
		    content: " ";
		    float: right;
		    width: 0;
		    height: 0;
		    border-color: transparent;
		    border-style: solid;
		    border-width: 5px 0 5px 5px;
		    border-left-color: #ccc;
		    margin-top: 5px;
		    margin-right: -10px;
		}
		.dropdown-submenu:hover>a:after {
		    border-left-color: #fff;
		}
		.dropdown-submenu.pull-left {
		    float: none;
		}
		.dropdown-submenu.pull-left>.dropdown-menu {
		    left: -100%;
		    margin-left: 10px;
		    -webkit-border-radius: 6px 0 6px 6px;
		    -moz-border-radius: 6px 0 6px 6px;
		    border-radius: 6px 0 6px 6px;
		}
		.bar {
		    margin-bottom: 14px;
		    margin-top: 13.5px;
		}
	 	.navbar-text{
	 		padding-bottom: 0px;
	 	}
	 	.dropdown-toggle::after {
		    display: none;
		}
	</style>
	<script type="text/javascript">

//   			$(function(){
//  				$('#t1').keyup(function(){
// 		    		if($(this).val() != ""){
// 		        		$("button[id='eulaText1AcceptBtn']").removeAttr("disabled");
// 		    		}  else {
// 		                $("button[id='eulaText1AcceptBtn']").attr('disabled', true); //disable input
// 		            }
// 				});
//  			});

//   			$(function(){
//  				$('#t0').keyup(function(){
// 		    		if($(this).val() != ""){
// 		        		$("button[id='eulaText0AcceptBtn']").removeAttr("disabled");
// 		    		}  else {
// 		                $("button[id='eulaText0AcceptBtn']").attr('disabled', true); //disable input
// 		            }
// 				});
//  			});
  			
//   			$(function(){
//  				$('#t2').keyup(function(){
// 		    		if($(this).val() != ""){
// 		        		$("button[id='eulaText2AcceptBtn']").removeAttr("disabled");
// 		    		}  else {
// 		                $("button[id='eulaText2AcceptBtn']").attr('disabled', true); //disable input
// 		            }
// 				});
//  			});

	
		//Enable popovers
		$(function(){
		    $("[data-toggle=popover]").popover({
		        html : true,
		        content: function() {
		          var content = $(this).attr("data-popover-content");
		          return $(content).children(".popover-body").html();
		        },
		        title: function() {
		          var title = $(this).attr("data-popover-content");
		          return $(title).children(".popover-heading").html();
		        }
		    });
		});
		
		//Used to rotate loader icon in modals
		function rotateIcon(){
			let n = 0;
			console.log(status);
			$('#spinner').removeClass('d-none');
			let interval = setInterval(function(){
		    	n += 1;
		    	if(n >= 60000){
		            $('#spinner').addClass('d-none');
		        	clearInterval(interval);
		        }else{
		        	$('#spinner').css("transform","rotate(" + n + "deg)");
		        }
			},5);
			
			$('#initTinterInProgressModal').one('hide.bs.modal',function(){
				$('#spinner').addClass('d-none');
	        	if(interval){clearInterval(interval);}
			});
		}
		
		function wssBrowserCheck(){
			if(wssCount === 0){
				if (ws_tinter || ws_spectro && $("#startNewJob_newSession").val() === "true") {
					if(!ws_tinter.validBrowser || !ws_spectro.validBrowser){
						wssCount++;
						$("#unsupportedBrowserModal").modal('show');
					}
				}
			}
		}

	</script>
  </head>
		    
	    <nav class="navbar navbar-dark bg-dark navbar-expand-md">			 
			   <button type="button" class="navbar-toggler" data-toggle="collapse" data-target="#navbarContent" aria-controls="navbarContent" aria-expanded="false">
		      		<span class="navbar-toggler-icon"></span>
		   		</button>
			   <div class="collapse navbar-collapse" id="navbarContent"> 
<!-- 			   		<ul id="menuPanelL" class="nav navbar-nav navbar-left"> -->
<!-- 	         			<li class="nav-item dropdown"> -->
<!-- 	         				<button class="btn dropdown-toggle bg-dark pt-2" data-toggle="dropdown" type="button" aria-haspopup="true" -->
<!-- 	        				aria-expanded="false"><i class="fa fa-bars" style="color: white;font-size: 1.4rem;"></i></button> -->
	        				
<!-- 	        			</li> -->
<!-- 	        				Popover links -->
	        			
	        			
<!-- 	        			Content for Popovers -->
						
	
<!-- 			      	</ul> -->
			   		<s:set var="thisGuid" value="reqGuid" />
			     	<ul class="navbar-nav ml-auto">
			     		<li class="nav-item"><span class='navbar-text'>Logged in as ${sessionScope[thisGuid].firstName} ${sessionScope[thisGuid].lastName}</span></li>
			     		<li class="nav-item p-2 pl-3 pr-3"><span id="bar"><strong style="color: dimgrey;">|</strong></span></li>
			     		<li class="nav-item"><span class='navbar-text'>${sessionScope[thisGuid].customerName}</span></li>
			     		<s:url var="loUrl" action="logoutAction"><s:param name="reqGuid" value="%{thisGuid}"/></s:url>
			     		<li class="nav-item pl-3"><a class="nav-link" href="<s:property value="loUrl" />">Logout <span class='fa fa-sign-out' style="font-size: 18px;"></span></a></li> 
			     	</ul>
			   </div><!--/.nav-collapse -->
		</nav>

	  	</div>
	</div>
	
	<body>
		<div class="container-fluid">
		<div class="row mt-5">
				<div class="col-lg-2 col-md-2 col-xs-2 col-xs-0">
					
				</div>
				<div class="col-lg-8 col-md-8 col-sm-8 col-xs-12">
<!-- 						<div class="text-center"><img id="shercolorimg" src="graphics/shercolor-lg.jpg" class="img-fluid" style="width: 33rem;"/></div> -->
				</div>
				<div class="col-lg-2 col-md-2 col-xs-2 col-xs-0">

				</div>
			</div>
			<div class="row">
				<div class="col-lg-4 col-md-3 col-xs-2 col-xs-0">
					
				</div>
				<div class="col-lg-4 col-md-6 col-sm-8 col-xs-12">

				</div>
				<div class="col-lg-4 col-md-3 col-xs-2 col-xs-0">

				</div>
			</div>
			<br>

			<s:iterator value="allEulas" status="outerStat">
				<s:if test="#outerStat.first == true">
					<div class="collapse show" id="collapseExample${outerStat.index}">
				</s:if>
				<s:else>
					<s:if test="#outerStat.last == true">
						<div class="collapse" id="collapseExampleLast">
					</s:if>
					<s:else>
 						<div class="collapse" id="collapseExample${outerStat.index}">
 					</s:else>
				</s:else>
					<div class="row">
						<div class="col-lg-2 col-md-2 col-xs-2 col-xs-0">
						</div>
						<div class="col-lg-8 col-md-8 col-sm-8 col-xs-12">	
						  	<div class="card card-body">
						  		<s:property escapeHtml="false"/>
						    </div>
						</div>
						<div class="col-lg-2 col-md-2 col-xs-2 col-xs-0">
						</div>
					</div>
					<div class="row">
						<div class="col-lg-4 col-md-3 col-xs-2 col-xs-0">
  
						</div>
						<div class="col-lg-4 col-md-6 col-sm-8 col-xs-12">
							<div class="custom-control custom-checkbox">
								<s:if test="#outerStat.last == false">
    								<s:checkbox name="t%{#outerStat.index}" id="t%{#outerStat.index}" onchange="document.getElementById('eulaText%{#outerStat.index}AcceptBtn').disabled = !this.checked; document.getElementById('eulaText%{#outerStat.index}AcceptBtn').style.visibility='visible';" label="I have read the above" />
    							</s:if>
    							<s:else>
    								<s:checkbox name="t%{#outerStat.index}" id="t%{#outerStat.index}" onchange="document.getElementById('acceptEulaBtn').disabled = !this.checked; document.getElementById('acceptEulaBtn').style.visibility='visible';" label="I have read the above" />
    							</s:else>
<%--     							<s:label>I have read the above</s:label> --%>
							</div>
						
<!-- 							<div class="form-group" style="margin-top: 20px;"> -->
<%-- 								<s:textfield id="t%{#outerStat.index}" name="eulaText%{#outerStat.index}Inits" /> --%>
<!-- 							</div> -->
						</div>
						
						<div class="col-lg-4 col-md-3 col-xs-2 col-xs-0">
							<s:if test="#outerStat.last == false">
								<s:if test="#outerStat.index==allEulas.size()-2">
	           						<button class="btn btn-primary" type="button" id="eulaText${outerStat.index}AcceptBtn" data-toggle="collapse" data-target="#collapseExampleLast" aria-expanded="false" aria-controls="collapseExampleLast"  disabled>
										Continue</button> 
								</s:if>
								<s:else>
									<button class="btn btn-primary" type="button" id="eulaText${outerStat.index}AcceptBtn" data-toggle="collapse" data-target="#collapseExample${outerStat.index + 1}" aria-expanded="false" aria-controls="collapseExample${outerStat.index + 1}"  disabled>
										Continue</button> 
								</s:else>
							</s:if>
						</div>
					</div>
				</div>
			</s:iterator>
			

			<!-- Display text box for signature and button to open next group if present. -->
			<div class="row" style="margin-bottom: 30px;">
				<div class="col-xl-4 col-lg-3 col-sm-1 col-xs-0">
				</div>
				<div class="col-xl-4 col-lg-6 col-sm-10 col-xs-12">
					<s:form action="AcceptEulaAction" method="post" align="center" theme="bootstrap" focusElement="acceptEulaBtn">
						<div class="form-group" style="margin-top: 20px;">
 							<s:hidden name="reqGuid" value="%{reqGuid}"/> 
 							<s:hidden name="eulaSeqNbr" value="%{eulaSeqNbr}"/> 
							<s:submit cssClass="btn btn-primary btn-lg pull-left" style="visibility:hidden" id="acceptEulaBtn" autofocus="autofocus" value="Accept Agreement" action="acceptEulaAction" disabled="true"/>
							<s:submit cssClass="btn btn-secondary" id="eulaTextNotAcceptBtn" value="I Do Not Agree" action="declineEulaAction"/>
							<s:submit cssClass="btn btn-secondary" id="printEulaBtn" value="Print" action="printEulaAction"/>
						</div>  
<!-- 		    			Unsupported Browser Modal Window -->
					    <div class="modal fade" aria-labelledby="unsupportedBrowserModal" aria-hidden="true"  id="unsupportedBrowserModal" role="dialog">
					    	<div class="modal-dialog" role="document">
								<div class="modal-content">
									<div class="modal-header">
										<h5 class="modal-title" id="unsupportedBrowserTitle">Unsupported Browser</h5>
										<button type="button" class="close" data-dismiss="modal" aria-label="Close" ><span aria-hidden="true">&times;</span></button>
									</div>
									<div class="modal-body">
										<div class="alert alert-danger" role="alert" id="wsserror">You are currently using an unsupported browser, if you are using a <strong>Tinter</strong> or <strong>Color-Eye</strong>, please use <strong>Google Chrome</strong> (Version 43 and above) in order to assure proper Tinter/Color-Eye communication.</div>
									</div>
									<div class="modal-footer">
										<button type="button" class="btn btn-success" id="unsupportedBrowserOK" data-dismiss="modal" aria-label="Close" >OK</button>
									</div>
								</div>
							</div>
						</div>		    
					</s:form>
				</div>
				<div class="col-xl-4 col-lg-3 col-sm-1 col-xs-0">
					
				</div>
			</div>
		</div>
		<br>
		<br>
		<br>

		<script>
		$(document).ready(function(){
			  $("#collapseExampleLast").on("show.bs.collapse", function(){
				  document.getElementById('acceptEulaBtn').style.visibility='visible';
// 				  $("button[id='acceptEulaBtn']").removeAttr("disabled");
			  });
			});
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
		-->
		</script>
		
		<!-- Including footer -->
		<s:include value="Footer.jsp"></s:include>
		
	</body>
</html>	