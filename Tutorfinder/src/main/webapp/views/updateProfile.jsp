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
<!-- 
<textarea id="field-biography" tabindex="1" rows="4" cols="50"></textarea>

<button type="button" onclick="/editBiography">Try it</button>
-->

<form:form method="get" action="/updateProfile" id="biographyForm" autocomplete="off">
	<fieldset>


		<div class="form-actions">
            <button type="submit" class="btn btn-primary">Save</button>
        </div>

	</fieldset>
</form:form>

</body>
</html>