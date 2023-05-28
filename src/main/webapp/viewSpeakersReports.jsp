<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="resources"/>

<!DOCTYPE html>
<html lang="${sessionScope.locale}">

<head>
    <title>Conference Smart App. <fmt:message key="view.speakers.reports"/></title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/bootstrap-icons.css">
    <link rel="stylesheet" href="css/my.css">
    <script src="js/bootstrap.min.js"></script>
</head>

<body>

<jsp:include page="fragments/mainMenu.jsp"/>

<jsp:include page="fragments/menuChoice.jsp"/>

<div class="col-lg-7 mx-auto p-4 py-md-5">
    <tags:header value="view.speakers.reports"/>

    <c:if test="${empty requestScope.reports}">
        <p class="fs-6"><fmt:message key="zero.reports"/></p>
    </c:if>

    <div class="bd-example-snippet bd-code-snippet">
        <div class="bd-example">
            <table class="table table-striped" aria-label="user-table">
                <thead>
                <tr>
                    <th scope="col"><fmt:message key="topic.name"/></th>
                    <th scope="col"><fmt:message key="event"/></th>
                    <th scope="col"><fmt:message key="date"/></th>
                    <th scope="col"><fmt:message key="location"/></th>
                    <th scope="col"><fmt:message key="action"/></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="report" items="${requestScope.reports}">
                    <tr>
                        <td><c:out value="${report.topic}"/></td>
                        <td><c:out value="${report.title}"/></td>
                        <td><c:out value="${report.date}"/></td>
                        <td><c:out value="${report.location}"/></td>
                        <td>
                            <form method="POST" action="controller">
                                <input type="hidden" name="action" value="set-or-remove-speaker-by-speaker">
                                <input type="hidden" name="todo" value="remove">
                                <input type="hidden" name="report-id" value="${report.id}">
                                <input type="hidden" name="topic" value="${report.topic}">
                                <input type="hidden" name="title" value="${report.title}">
                                <button type="submit" class="btn btn-dark mt-0 mb-0">
                                    <fmt:message key="decline.report"/>
                                </button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="fragments/footer.jsp"/>

</body>
</html>