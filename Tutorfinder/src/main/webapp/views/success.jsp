<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login successful</title>
</head>
<body>
	<sec:authorize access="hasAnyRole('ROLE_STUDENT','ROLE_TUTOR')">
		<!-- For login user -->
		<c:if test="${pageContext.request.userPrincipal.name != null}">
			<h2>
				Welcome, ${pageContext.request.userPrincipal.name} | 
				<a href="<c:url value="/logout"/>">Logout</a>
			</h2>
		</c:if>


	</sec:authorize>
</body>
</html>