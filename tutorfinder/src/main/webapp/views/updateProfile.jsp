<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ page session="false" %>

<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Update profile</title>

<link rel="stylesheet" href="/css/style.css" />

</head>
<body>

	<!-- TODO edit format with if-statements, student does not need all format, but an additional button to change to an Tutor -->


	<form:form method="post" modelAttribute="updateProfileForm"
		action="update" id="updateProfileForm" autocomplete="off">
		<fieldset>

			<!-- 
				Hidden input for pass email 
			-->
			<c:set var="emailErrors">
				<form:errors path="email" />
			</c:set>
			<div
				class="control-group<c:if test="${not empty emailErrors}"> error</c:if>">

				<div class="controls">
					<form:hidden path="email" id="field-email" tabindex="0"
						maxlength="45" placeholder="Email" value="${User.email}" />
					<form:errors path="email" cssClass="help-inline" element="span" />
				</div>
			</div>
			
			<input type="hidden" 
		  		name="${_csrf.parameterName}"
				value="${_csrf.token}" />
			
			
			<!-- 
				ProfilePicture form
				Shows the actual picture and allows the user to upload an new picture
			 -->
			<div id="profilePicture">
				<img src=<c:out value="${Profile.imgPath}"/> alt="Profile picture" width="192" height="192" 
				onerror="this.onerror=null; this.src='../img/test-avatar.png;'"><br>
				
				<form method="POST" action="uploadImage" enctype="multipart/form-data">
					Upload new profile picture: <br>
					<input type="file" name="file" accept="image/*"><br> 
					<input type="submit" value="Upload">
				</form>
			</div><br>
			
			<!--
	        	Biography field
	        	Placeholder loads the saved value on the server connected to the user's profile.
        	-->
			<c:set var="biographyErrors">
				<form:errors path="biography" />
			</c:set>
			<div class="control-group">
				<label class="control-label" for="field-biography">Biography</label>

				<div class="controls">
					<form:textarea rows="4" cols="50" path="biography"
						id="field-biography" tabindex="1" maxlength="255" />
					<form:errors path="biography" cssClass="help-inline" element="span" />
				</div>
			</div>

			<!-- The follow form-tags are only shown if it is the profile from an tutor -->
			<sec:authorize access="hasAnyRole('ROLE_TUTOR')">
				<!-- 
		        	Region field 
		        	Placeholder loads the saved value on the server connected to the user's profile
		        -->
				<c:set var="regionErrors">
					<form:errors path="region" />
				</c:set>
				<div class="control-group">
					<label class="control-label" for="field-region">Region</label>

					<div class="controls">
						<form:input path="region" id="field-region" tabindex="1"
							maxlength="255" placeholder="${Profile.region}"
							value="${Profile.region}" />
						<form:errors path="region" cssClass="help-inline" element="span" />
					</div>
				</div>

				<!-- 
		        	Wage field 
		        	Placeholder loads the saved value on the server connected to the user's profile
		        -->
				<c:set var="wageErrors">
					<form:errors path="wage" />
				</c:set>
				<div class="control-group">
					<label class="control-label" for="field-wage">Wage in CHF
						(Format: 00.00)</label>

					<div class="controls">
						<form:input path="wage" id="field-wage" tabindex="1" maxlength="6"
							placeholder="${Profile.wage}" value="${Profile.wage}" />
						<form:errors path="wage" cssClass="help-inline" element="span" />
					</div>
				</div>
			</sec:authorize>


			<div class="form-actions">

				<!-- 
					Button to save the changes, loads again the uploadProfile page with the new information.
				-->
				<button type="submit" class="btn btn-primary">Save</button>

				<!-- 
					Button to redirect to showProfile and close the edit.
					Does not save the last changes!
				-->
				<button type="button" onClick="window.location.href='showProfile'">Cancel</button>
			</div>


		</fieldset>
	</form:form>


</body>
</html>