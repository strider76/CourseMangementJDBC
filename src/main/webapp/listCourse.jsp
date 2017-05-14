<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Listado de Cursos</title>
</head>
<body>
	<c:catch var="err">
		<jsp:useBean id="courseBean" class="packt.book.jee.eclipse.ch4.bean.Course"/>
		<c:set var="courseBean" value="${courseBean.getCourses()}"></c:set>
	</c:catch>
	<c:choose>
		<c:when test="${err != null}">
			<c:set var="errMsg" value="${err.message}"/>
		</c:when>
		<c:otherwise>
		</c:otherwise>
	</c:choose>
	<h2>Cursos:</h2>
	<c:if test="${errMsg != null}">
		<span style="color: red;">
			<c:out value="${errMsg}"></c:out>
		</span>
	</c:if>
	<table>
		<tr>
			
		</tr>
	</table>
</body>
</html>