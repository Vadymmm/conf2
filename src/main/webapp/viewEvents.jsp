<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="resources"/>

<!DOCTYPE html>
<html lang="${sessionScope.locale}">

<head>
    <title>Conference Smart App. <fmt:message key="events"/></title>
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

<div class="col-lg-8 mx-auto p-4 py-md-5">
    <tags:header value="events"/>

    <c:if test="${empty requestScope.events}">
        <p class="fs-6"><fmt:message key="zero.events"/></p>
    </c:if>

    <div class="row">
        <form class="col-11" method="GET" action="controller">
            <input type="hidden" name="action" value="view-events">
            <input type="hidden" name="offset" value="0">

            <label>
                <select name="date" class="form-select mt-2" onchange='submit();'>
                    <option><fmt:message key="select.date"/></option>
                    <option value="upcoming" ${param.date eq "upcoming" ? "selected" : ""}>
                        <fmt:message key="upcoming"/>
                    </option>
                    <option value="passed" ${param.date eq "passed" ? "selected" : ""}>
                        <fmt:message key="passed"/>
                    </option>
                </select>
            </label>&nbsp&nbsp&nbsp&nbsp&nbsp

            <label for="records"><fmt:message key="number.records"/></label>
            <input class="col-2" type="number" min="1" name="records" id="records"
                   value="${not empty requestScope.records ? requestScope.records : "5"}">&nbsp&nbsp&nbsp
            <button type="submit" class="btn btn-dark mt-2 mb-3"><fmt:message key="submit"/></button>
        </form>

        <form class="col-1 mt-3" method="GET" action="controller">
            <input type="hidden" name="action" value="events-pdf">
            <input type="hidden" name="date" value="${param.date}">
            <input type="hidden" name="sort" value="${param.sort}">
            <input type="hidden" name="order" value="${param.order}">
            <button type="submit" class="icon-button"><i class="bi bi-download"></i></button>
        </form>
    </div>

    <div class="bd-example-snippet bd-code-snippet">
        <div class="bd-example">
            <table class="table table-striped" aria-label="user-table">
                <thead>
                <tr>
                    <c:set var="base" value="controller?action=view-events&date=${param.date}&"/>
                    <c:set var="byTitle" value="sort=title&"/>
                    <c:set var="byDate" value="sort=date&"/>
                    <c:set var="byLocation" value="sort=location&"/>
                    <c:set var="byReport" value="sort=reports&"/>
                    <c:set var="byRegistrations" value="sort=registrations&"/>
                    <c:set var="byVisitors" value="sort=visitors&"/>
                    <c:set var="titleOrder"
                           value="order=${param.sort ne 'title' || param.order eq 'DESC' ? 'ASC' : 'DESC'}"/>
                    <c:set var="dateOrder"
                           value="order=${param.sort ne 'date' || param.order eq 'DESC' ? 'ASC' : 'DESC'}"/>
                    <c:set var="locationOrder"
                           value="order=${param.sort ne 'location' || param.order eq 'DESC' ? 'ASC' : 'DESC'}"/>
                    <c:set var="reportsOrder"
                           value="order=${param.sort ne 'reports' || param.order eq 'DESC' ? 'ASC' : 'DESC'}"/>
                    <c:set var="visitorsOrder"
                           value="order=${param.sort ne 'visitors' || param.order eq 'DESC' ? 'ASC' : 'DESC'}"/>
                    <c:set var="registrationsOrder"
                           value="order=${param.sort ne 'registrations' || param.order eq 'DESC' ? 'ASC' : 'DESC'}"/>
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
                    <th scope="col">
                        <fmt:message key="reports"/>
                        <a href="${base.concat(byReport).concat(reportsOrder).concat(limits)}">
                            <i class="bi bi-arrow-down-up link-dark"></i>
                        </a>
                    </th>
                    <th scope="col">
                        <fmt:message key="registrations"/>
                        <a href="${base.concat(byRegistrations).concat(registrationsOrder).concat(limits)}">
                            <i class="bi bi-arrow-down-up link-dark"></i>
                        </a>
                    </th>
                    <th scope="col">
                        <fmt:message key="visitors"/>
                        <a href="${base.concat(byVisitors).concat(visitorsOrder).concat(limits)}">
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
                        <td><c:out value="${event.reports}"/></td>
                        <td><c:out value="${event.registrations}"/></td>
                        <td><c:out value="${event.visitors}"/></td>
                        <td>
                            <a class="link-dark" href=controller?action=search-event&event-id=${event.id}>
                                <fmt:message key="view"/>
                            </a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <c:set var="href" scope="request"
           value="controller?action=view-events&date=${param.date}&sort=${param.sort}&order=${param.order}&"/>

    <jsp:include page="/fragments/pagination.jsp"/>
</div>

<jsp:include page="fragments/footer.jsp"/>

</body>
</html>