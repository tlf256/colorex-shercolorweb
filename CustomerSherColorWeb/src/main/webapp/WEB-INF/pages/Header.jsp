<%@ taglib uri="/struts-tags" prefix="s" %>

<!-- Fixed navbar -->
<nav class="navbar navbar-dark bg-dark navbar-expand-md pt-0 pb-0">
	   <a href='<s:url action="loginAction"><s:param name="reqGuid" value="%{reqGuid}"/></s:url>'><img src="graphics/shercolor-sm.jpg" alt="Sher-Color" style="height: 3.44rem;"/></a>
	 
	   <button type="button" class="navbar-toggler" data-toggle="collapse" data-target="#navbarContent" aria-controls="navbarContent" aria-expanded="false">
      		<span class="navbar-toggler-icon"></span>
   		</button>
	   <div class="collapse navbar-collapse" id="navbarContent">
	   		<s:set var="thisGuid" value="reqGuid" />
	     	<ul class="navbar-nav ml-auto">
	     		<li class="nav-item"><span class='navbar-text'>Logged in as ${sessionScope[thisGuid].firstName} ${sessionScope[thisGuid].lastName}</span></li>
	     		<li class="nav-item p-2 pl-3 pr-3"><span id="bar"><strong style="color: dimgrey;">|</strong></span></li>
	     		<li class="nav-item"><span class='navbar-text'>${sessionScope[thisGuid].customerName}</span></li>
	     		<s:url var="loUrl" action="logoutAction"><s:param name="reqGuid" value="%{thisGuid}"/></s:url>
	     		<li class="nav-item pl-3"><a class="nav-link" href="<s:property value="loUrl" />">Logout <span class='fa fa-sign-out'></span></a></li> 
	     	</ul> 
	   </div><!--/.nav-collapse -->
</nav>