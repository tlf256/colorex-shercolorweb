<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>

<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
	<!-- first check if we have a locale saved in a cookie, then forward to login -->
	<s:action name="checkLocaleCookieAction" executeResult="true" namespace="/" />

</html>