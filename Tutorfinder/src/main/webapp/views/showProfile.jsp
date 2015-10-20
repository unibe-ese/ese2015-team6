<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE>
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Profile</title>
</head>

<body>

	<!-- 
		Profile picture 
	-->
		<img src=<c:out value="${Profile.imgPath}"/> alt="Profile picture" width="192" height="192" 
		onerror="this.onerror=null; this.src='../img/default-avatar.png;'">
	
	<!-- 
		Profile name 
	-->
		<h1><c:out value="${User.firstName}"/> <c:out value=" ${User.lastName}"/></h1>
	
    <!-- 
    	Biography 
    -->
		<h3>Short biography</h3>
		<p style="width:700px"> 
			<c:out value="${Profile.biography}"/>
		</p>	
	
	<!-- 
		Region 
	-->
		<p>
			<b>Region:</b> <c:out value="${Profile.region}"/>
		</p>
		
	<!-- 
		Wage 
	-->
		<p>
			<b>Wage:</b> <c:out value="${Profile.wage}"/> CHF per hour
		</p>
	
</body>

</html>