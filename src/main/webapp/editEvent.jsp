<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="custom" uri="/WEB-INF/custom.tld" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="resources"/>

<!DOCTYPE html>
<html lang="${sessionScope.locale}">

<head>
    <title>Conference Smart App. <fmt:message key="edit.event"/></title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/my.css">
    <script src="js/bootstrap.min.js"></script>
</head>

<body>

<jsp:include page="fragments/mainMenu.jsp"/>

<jsp:include page="fragments/menuChoice.jsp"/>

<div class="col-lg-5 mx-auto p-4 py-md-5">
    <tags:header value="edit.event"/>

    <c:set var="error" value="${requestScope.error}"/>

    <c:choose>
        <c:when test="${fn:contains(error, 'error.event.edit') or fn:contains(error, 'error.event.absent')}">
            <span class="text-danger"><fmt:message key="${error}"/></span>
        </c:when>

        <c:otherwise>
            <c:set var="idValue" value="${requestScope.eventNew.id eq null ?
                                requestScope.event.id : requestScope.eventNew.id}"/>
            <c:set var="emailValue" value="${requestScope.eventNew.title eq null ?
                                requestScope.event.title : requestScope.eventNew.title}"/>
            <c:set var="dateValue" value="${requestScope.eventNew.date eq null ?
                                requestScope.event.date : requestScope.eventNew.date}"/>
            <c:set var="locationValue" value="${requestScope.eventNew.location eq null ?
                                requestScope.event.location : requestScope.eventNew.location}"/>
            <c:set var="descriptionValue" value="${requestScope.eventNew.description eq null ?
                                requestScope.event.description : requestScope.eventNew.description}"/>

            <tags:notEmptyMessage value="${requestScope.message}"/><br>

            <form method="POST" action="controller">
                <input type="hidden" name="action" value="edit-event">
                <input type="hidden" name="event-id" value="${requestScope.event.id}">

                <div>
                    <label class="form-label fs-5" for="title"><fmt:message key="title"/>*: </label>
                    <input class="form-control" name="title" id="title"
                           pattern="^[0-9A-Za-zА-ЩЬЮЯҐІЇЄа-щьюяґіїє'.,;:+\-~`!?@#$^&*()={}| ]{2,70}"
                           title="<fmt:message key="title.requirements"/>"
                           required value="${emailValue}">
                    <tags:contains error="${error}" value="title"/><br>
                </div>

                <div>
                    <label class="form-label fs-5" for="date"><fmt:message key="date"/>*: </label>
                    <input class="form-control" type="date" name="date" id="date"
                           required value="${dateValue}" min="<custom:now/>">
                    <tags:contains error="${error}" value="date"/><br>
                </div>

                <div class="form-group">
                    <label class="form-label fs-5" for="location"><fmt:message key="location"/>*: </label>
                    <input class="form-control" name="location" id="location"
                           pattern="^[0-9A-Za-zА-ЩЬЮЯҐІЇЄа-щьюяґіїє'.,;:+\-~`!?@#$^&*()={}| ]{2,70}"
                           title="<fmt:message key="location.requirements"/>"
                           required value="${locationValue}">
                    <tags:contains error="${error}" value="location"/><br>
                </div>

                <div class="form-group">
                    <label class="form-label fs-5" for="description"><fmt:message key="description"/>*: </label>
                    <input class="form-control" name="description" id="description"
                           pattern="^[0-9A-Za-zА-ЩЬЮЯҐІЇЄа-щьюяґіїє'.,;:+\-~`!?@#$^&*()={}| ]{1,200}"
                           title="<fmt:message key="description.requirements"/>"
                           required value="${descriptionValue}">
                    <tags:contains error="${error}" value="description"/><br>
                </div>

                <button type="submit" class="btn btn-dark mt-4 mb-4"><fmt:message key="submit"/></button>
            </form>

            <a class="link-dark" href=controller?action=search-event&event-id=${idValue}>
                <fmt:message key="back.to.conference"/>
            </a>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="fragments/footer.jsp"/>

</body>
</html>