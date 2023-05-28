<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:choose>
    <c:when test="${sessionScope.role eq 'ADMIN'}">
        <jsp:include page="adminMenu.jsp"/>
    </c:when>
    <c:when test="${sessionScope.role eq 'MODERATOR'}">
        <jsp:include page="moderatorMenu.jsp"/>
    </c:when>
    <c:when test="${sessionScope.role eq 'SPEAKER'}">
        <jsp:include page="speakerMenu.jsp"/>
    </c:when>
    <c:when test="${sessionScope.role eq 'VISITOR'}">
        <jsp:include page="visitorMenu.jsp"/>
    </c:when>
</c:choose>