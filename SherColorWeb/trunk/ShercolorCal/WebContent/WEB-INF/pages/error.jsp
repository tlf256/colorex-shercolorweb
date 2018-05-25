<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Error Occurred</title>
</head>
<body>
<br>
<br>
<h2>Error Occurred!</h2>
</body>
	<br>
	<s:textfield class='errormessage' id='message' name="message" > </s:textfield>
	<s:if test="hasActionErrors()">
		<div class="errormessage">
			<s:actionerror/>
		</div>
	</s:if>
	
 	<s:if test="hasActionMessages()">
		<div class="warnmessage">
			<s:actionmessage/>
		</div>
	</s:if> 
	<br>
</html>