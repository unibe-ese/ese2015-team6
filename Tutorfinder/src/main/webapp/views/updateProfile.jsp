<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Update profile</title>
</head>
<body>



	<form:form method="post" modelAttribute="updateProfileForm" action="update" id="updateProfileForm" autocomplete="off">
		<fieldset>
			
			<!-- 
				Hidden input for pass email 
			-->
				<c:set var="emailErrors"><form:errors path="email"/></c:set>
		       	<div class="control-group<c:if test="${not empty emailErrors}"> error</c:if>">
		
		            <div class="controls">
		                <form:hidden path="email" id="field-email" tabindex="0" maxlength="45" placeholder="Email" value="${User.email}"/>
		                <form:errors path="email" cssClass="help-inline" element="span"/>
		            </div>
		        </div>
        	
        	<!--
	        	Biography field
	        	Placeholder loads the saved value on the server connected to the user's profile.
        	-->
				<c:set var="biographyErrors"><form:errors path="biography"/></c:set>
		        <div class="control-group">
		            <label class="control-label" for="field-biography">Biography</label>
		
			         <div class="controls">
			         	 <!-- TODO change in textarea input for better reading -->
			             <form:input path="biography" id="field-biography" tabindex="1" maxlength="255" 
			             placeholder="${Profile.biography}" value="${Profile.biography}"/>
			             <form:errors path="biography" cssClass="help-inline" element="span"/>
			        </div>
		        </div>
	        
	        <!-- 
	        	Region field 
	        	Placeholder loads the saved value on the server connected to the user's profile
	        -->
		        <c:set var="regionErrors"><form:errors path="region"/></c:set>
		        <div class="control-group">
		            <label class="control-label" for="field-region">Region</label>
		
			         <div class="controls">
			             <form:input path="region" id="field-region" tabindex="1" maxlength="255" 
			             placeholder="${Profile.region}" value="${Profile.region}"/>
			             <form:errors path="region" cssClass="help-inline" element="span"/>
			        </div>
		        </div>
	        
	        <!-- 
	        	Wage field 
	        	Placeholder loads the saved value on the server connected to the user's profile
	        -->
		        <c:set var="wageErrors"><form:errors path="wage"/></c:set>
		        <div class="control-group">
		            <label class="control-label" for="field-wage">Wage in CHF (Format: 00.00)</label>
		
			         <div class="controls">
			             <form:input path="wage" id="field-wage" tabindex="1" maxlength="6" 
			             placeholder="${Profile.wage}" value="${Profile.wage}"/>
			             <form:errors path="wage" cssClass="help-inline" element="span"/>
			        </div>
		        </div>
	
			
			<div class="form-actions">
				
				<!-- 
					Button to save the changes, loads again the uploadProfile page with the new information.
				-->
		            <button type="submit" class="btn btn-primary">Save</button>
		            
		        <!-- 
					Button to redirect to showProfile and close the edit.
					Does not save the last changes!
				-->
					<button type="button" onClick="window.location.href='showProfile'" >Cancel</button>
			</div>

	
		</fieldset>
	</form:form>
	
	
</body>
</html>