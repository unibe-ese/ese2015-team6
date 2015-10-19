<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Update profile</title>
</head>
<body>

	<form:form method="post" modelAttribute="updateProfileForm" action="/update" id="updateProfileForm" autocomplete="off">
		<fieldset>
			
				
			<c:set var="biographyErrors"><form:errors path="biography"/></c:set>
	        <div class="control-group">
	            <label class="control-label" for="field-biography">Biography</label>
	
		         <div class="controls">
		             <form:input path="biography" id="field-biography" tabindex="1" maxlength="300" 
		             placeholder="${Profile.biography}" value="${Profile.biography}"/>
		             <form:errors path="biography" cssClass="help-inline" element="span"/>
		        </div>
	        </div>
	
			<div class="form-actions">
	            <button type="submit" class="btn btn-primary">Save</button>
	        </div>
	
		</fieldset>
	</form:form>

</body>
</html>