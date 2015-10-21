<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Tutorfinder register</title>
</head>
<body>
<form:form method="post" modelAttribute="signupForm" action="create" id="signupForm" autocomplete="off">
    <fieldset>
        <legend>Enter Your Information</legend>

        <c:set var="emailErrors"><form:errors path="email"/></c:set>
        <div class="control-group<c:if test="${not empty emailErrors}"> error</c:if>">
            <label class="control-label" for="field-email">Email</label>
            <div class="controls">
                <form:input path="email" id="field-email" tabindex="1" maxlength="45" placeholder="Email"/>
                <form:errors path="email" cssClass="help-inline" element="span"/>
                <c:if test="${not empty email_error}"> ${email_error}</c:if>
            </div>
        </div>
        <c:set var="firstNameErrors"><form:errors path="firstName"/></c:set>
        <div class="control-group<c:if test="${not empty firstNameErrors}"> error</c:if>">
            <label class="control-label" for="field-firstName">First Name</label>
            <div class="controls">
                <form:input path="firstName" id="field-firstName" tabindex="2" maxlength="35" placeholder="First Name"/>
                <form:errors path="firstName" cssClass="help-inline" element="span"/>
            </div>
        </div>
        <c:set var="lastNameErrors"><form:errors path="lastName"/></c:set>
        <div class="control-group<c:if test="${not empty lastNameErrors}"> error</c:if>">
            <label class="control-label" for="field-lastName">Last Name</label>
            <div class="controls">
                <form:input path="lastName" id="field-lastName" tabindex="3" maxlength="35" placeholder="Last Name"/>
                <form:errors path="lastName" cssClass="help-inline" element="span"/>
            </div> 
        </div>
        
        <c:set var="passwordErrors"><form:errors path="password"/></c:set>
        <div class="control-group<c:if test="${not empty passwordErrors}"> error</c:if>">
            <label class="control-label" for="field-password">Password</label>
            <div class="controls">
                <form:input path="password" id="field-password" tabindex="3" maxlength="35" placeholder="Password" type="password"/>
                <form:errors path="password" cssClass="help-inline" element="span"/>
            </div> 
        </div>
        
        <div class="control-group<c:if test="${not empty tutorErrors}"> error</c:if>">
            <label class="control-label" for="field-tutor">Sign up as:</label>
            <div class="controls">
            	
                <form:select path="tutor">
                	<form:option value="true">Tutor</form:option>
                  	<form:option value="false">Student</form:option>
                </form:select>
                <form:errors path="tutor" cssClass="help-inline" element="span"/>
            </div>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary">Sign up</button>
        </div>
    </fieldset>
</form:form>


</body>
</html>