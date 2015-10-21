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

    <div id ="logo">
        <h1>ESE Tutorfinder</h1>
    </div>
    
    
	

	<div id="login-box">

		<h2></h2>

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
				<td>EMAIL:</td>
				<td><input type='text' id="email" name='email'></td>
			</tr>
			<tr>
				<td>PASSWORD:</td>
				<td><input type='password' id = "password" name='password' /></td>
			</tr>
			<tr>
				<td colspan='1'><input name="submit" type="submit"
				  value="LOGIN" /></td>
                <td colspan='1'><button type="button" onClick="window.location.href='/register'">REGISTER</button>
                </td>
		  </table>

		  <input type="hidden" 
		  	name="${_csrf.parameterName}"
			value="${_csrf.token}" />

		</form>
	
    </div>


</body>
</html>