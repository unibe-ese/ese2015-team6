<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page session="true"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login</title>
<link rel="stylesheet" href="/css/style.css" />
</head>
<body onload='document.loginForm.email.focus();'>

	<h1>Login Form</h1>

	<div id="login-box">

		<h2>Login with E-Mail address and Password</h2>

		<c:if test="${not empty error}">
			<div class="error">${error}</div>
		</c:if>
		<c:if test="${not empty msg}">
			<div class="msg">${msg}</div>
		</c:if>
		<c:if test="${not empty logout}">
			<div class="msg">You have been logged out</div>
		</c:if>

		<form name='loginForm'
		  action="${loginUrl}" method="post">

		<table>
			<tr>
				<td>User:</td>
				<td><input type='text' id="email" name='email'></td>
			</tr>
			<tr>
				<td>Password:</td>
				<td><input type='password' id = "password" name='password' /></td>
			</tr>
			<tr>
				<td colspan='2'><input name="submit" type="submit"
				  value="submit" /></td>
			</tr>
		  </table>

		  <input type="hidden" 
		  	name="${_csrf.parameterName}"
			value="${_csrf.token}" />

		</form>
	</div>

</body>
</html>