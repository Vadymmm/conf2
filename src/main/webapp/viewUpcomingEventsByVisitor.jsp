<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="resources"/>

<!DOCTYPE html>
<html lang="${sessionScope.locale}">

<head>
    <title>Conference Smart App. <fmt:message key="view.up.events.by.visitor"/></title>
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
    <tags:header value="view.up.events.by.visitor"/>

    <form method="GET" action="controller" class="flex">
        <input type="hidden" name="action" value="view-upcoming-events">
        <input type="hidden" name="offset" value="0">
        <label for="records"><fmt:message key="number.records"/></label>
        <input class="col-2" type="number" min="1" value="${not empty requestScope.records ? requestScope.records : "5"}"
               name="records" id="records">&nbsp&nbsp&nbsp&nbsp&nbsp
        <button type="submit" class="btn btn-dark mt-3 mb-4"><fmt:message key="submit"/></button>
    </form>

    <div class="bd-example-snippet bd-code-snippet">
        <div class="bd-example">
            <table class="table table-striped" aria-label="user-table">
                <thead>
                <tr>
                    <c:set var="base" value="controller?action=view-upcoming-events&"/>
                    <c:set var="byTitle" value="sort=title&"/>
                    <c:set var="byDate" value="sort=date&"/>
                    <c:set var="byLocation" value="sort=location&"/>
                    <c:set var="titleOrder"
                           value="order=${param.sort ne 'title' || param.order eq 'DESC' ? 'ASC' : 'DESC'}"/>
                    <c:set var="dateOrder"
                           value="order=${param.sort ne 'date' || param.order eq 'DESC' ? 'ASC' : 'DESC'}"/>
                    <c:set var="locationOrder"
                           value="order=${param.sort ne 'location' || param.order eq 'DESC' ? 'ASC' : 'DESC'}"/>
                    <c:set var="limits" value="&offset=0&records=${param.records}"/>

                    <th scope="col">
                        <fmt:message key="title"/>
                        <a href="${base.concat(byTitle).concat(titleOrder).concat(limits)}">
                            <i class="bi bi-arrow-down-up link-dark"></i>
                        </a>
                    </th>
                    <th scope="col">
                        <fmt:message key="date"/>
                        <a href="${base.concat(byDate).concat(dateOrder).concat(limits)}">
                            <i class="bi bi-arrow-down-up link-dark"></i>
                        </a>
                    </th>
                    <th scope="col">
                        <fmt:message key="location"/>
                        <a href="${base.concat(byLocation).concat(locationOrder).concat(limits)}">
                            <i class="bi bi-arrow-down-up link-dark"></i>
                        </a>
                    </th>
                    <th scope="col"><fmt:message key="action"/></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="event" items="${requestScope.events}">
                    <tr>
                        <td><c:out value="${event.title}"/></td>
                        <td><c:out value="${event.date}"/></td>
                        <td><c:out value="${event.location}"/></td>
                        <td>
                            <a class="link-dark" href=controller?action=view-event-by-visitor&event-id=${event.id}>
                                <fmt:message key="view"/>
                            </a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <c:set var="href" value="controller?action=view-upcoming-events&sort=${param.sort}&order=${param.order}&" scope="request"/>

    <jsp:include page="/fragments/pagination.jsp"/>
</div>

<jsp:include page="fragments/footer.jsp"/>

</body>
</html>