<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="custom" uri="/WEB-INF/custom.tld" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="resources"/>
<c:set var="now"><custom:now/></c:set>
<c:set var="isComing" value="${now le requestScope.event.date}" scope="request"/>

<!DOCTYPE html>
<html lang="${sessionScope.locale}">

<head>
    <title>Conference Smart App. <fmt:message key="view.event"/></title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/my.css">
    <script src="js/bootstrap.min.js"></script>
</head>

<body>

<jsp:include page="fragments/mainMenu.jsp"/>

<jsp:include page="fragments/menuChoice.jsp"/>

<div class="col-lg-6 mx-auto p-4 py-md-5">
    <header class="d-flex align-items-center pb-0 mb-3 border-bottom">
        <span class="fs-4">${requestScope.event.title}</span>
    </header>

    <div>
        <p class="fs-5"><fmt:message key="date"/>: ${requestScope.event.date}&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
            <fmt:message key="location"/>: ${requestScope.event.location}</p>
        <p class="fs-5"><fmt:message key="description"/>: ${requestScope.event.description}</p>
        <p class="fs-5">
            <fmt:message key="reports"/>: ${requestScope.event.reports} &nbsp&nbsp
            <fmt:message key="registrations"/>: ${requestScope.event.registrations} &nbsp&nbsp
            <c:if test="${not isComing}">
                <fmt:message key="visitors"/>: ${requestScope.event.visitors}
            </c:if>
        </p>
    </div>

    <c:choose>
        <c:when test="${isComing}">
            <a href="controller?action=edit-event&event-id=${requestScope.event.id}"
               class="btn btn-dark mt-4 mb-4"><fmt:message key="edit.event"/></a>

            <button class="btn btn-dark mt-4 mb-4" data-bs-toggle="modal" data-bs-target="#exampleModalDefault">
                <fmt:message key="delete"/>
            </button>

            <button class="btn btn-dark mt-4 mb-4" data-bs-toggle="modal" data-bs-target="#exampleModalDefault2">
                <fmt:message key="add.report"/>
            </button>
        </c:when>

        <c:otherwise>
            <form method="POST" action="controller">
                <input type="hidden" name="action" value="set-visitors">
                <input type="hidden" name="event-id" value=${requestScope.event.id}>
                <label class="form-label fs-5" for="visitors"><fmt:message key="set.visitors"/>: </label>
                <input class="col-1" type="number" min="0" value="${requestScope.event.visitors}"
                       name="visitors" id="visitors">&nbsp&nbsp&nbsp&nbsp&nbsp
                <button type="submit" class="btn btn-dark mt-3 mb-4"><fmt:message key="submit"/></button>
            </form>
        </c:otherwise>
    </c:choose><br>
    <tags:notEmptyMessage value="${requestScope.message}"/><br>

    <div class="bd-example-snippet bd-code-snippet">
        <div class="bd-example">
            <table class="table table-striped" aria-label="report-table">
                <thead>
                <tr>
                    <th scope="col"><fmt:message key="topic.name"/></th>
                    <th scope="col"><fmt:message key="speaker.name"/></th>
                    <c:if test="${isComing}">
                        <th scope="col"><fmt:message key="action"/></th>
                    </c:if>
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

                        <td>
                            <c:if test="${isComing}">
                                <a class="link-dark" href="controller?action=view-report&report-id=${report.id}">
                                    <fmt:message key="edit"/>
                                </a>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="fragments/footer.jsp"/>

<jsp:include page="fragments/deleteEventModal.jsp"/>

<jsp:include page="fragments/addReportModal.jsp"/>

</body>
</html>