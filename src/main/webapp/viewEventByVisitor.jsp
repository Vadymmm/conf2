<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="resources"/>

<!DOCTYPE html>
<html lang="${sessionScope.locale}">

<head>
    <title>Conference Smart App. <fmt:message key="event"/></title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/my.css">
    <script src="js/bootstrap.min.js"></script>
</head>

<body>

<jsp:include page="fragments/mainMenu.jsp"/>

<jsp:include page="fragments/menuChoice.jsp"/>

<div class="col-lg-7 mx-auto p-4 py-md-5">
    <header class="d-flex align-items-center pb-3 mb-3 border-bottom">
        <span class="fs-4">${requestScope.event.title}</span>
    </header>

    <c:choose>
        <c:when test="${not empty requestScope.error}">
            <span class="text-danger"><fmt:message key="${requestScope.error}"/></span>
        </c:when>

        <c:otherwise>
            <p class="fs-5"><fmt:message key="date"/>: ${requestScope.event.date}&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
                <fmt:message key="location"/>: ${requestScope.event.location}</p>
            <p class="fs-5"><fmt:message key="description"/>: ${requestScope.event.description} </p>

            <c:if test="${requestScope.isComing}">
                <jsp:include page="/fragments/registerOrCancelRegistration.jsp"/>
            </c:if>

            <div class="bd-example-snippet bd-code-snippet">
                <div class="bd-example">
                    <table class="table table-striped" aria-label="report-table">
                        <thead>
                        <tr>
                            <th scope="col"><fmt:message key="topic.name"/></th>
                            <th scope="col"><fmt:message key="speaker.name"/></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="report" items="${requestScope.reports}">
                            <tr>
                                <td><c:out value="${report.topic}"/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${empty report.speakerName}">
                                            <span class="text-danger"><fmt:message key='not.available'/></span>
                                        </c:when>
                                        <c:otherwise>
                                            ${report.speakerName}
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="fragments/footer.jsp"/>

</body>
</html>