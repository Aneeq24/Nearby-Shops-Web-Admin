<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:set var="contextRoot" value="${pageContext.request.contextPath}" />

<spring:url var="css" value="/resources/css" />
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>

<link rel="stylesheet" type="text/css" href="${css}/sweetalert.min.css">
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.js"></script>
</head>
<body>
	<script type="text/javascript">
		setTimeout(function () { 
		<c:if test="${typeSucess == true }">
			swal({
				  title: "Success ${response}",
				  text: "${message}.",
				  type: "success",
				  confirmButtonText: "OK"
				},
		</c:if>
				<c:if test="${typeSucess == false }">
				swal({
					  title: "Error ${response}",
					  text: "${message}.",
					  type: "error",
					  confirmButtonText: "OK"
					},
			</c:if>
					function(isConfirm){
						  if (isConfirm) {
							window.location.href = "${contextRoot}"+"${url}";
						  }
						}); });
						</script>'
	</script>
</body>
</html>