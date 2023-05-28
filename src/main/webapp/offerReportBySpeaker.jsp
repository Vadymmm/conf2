<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="resources"/>

<!DOCTYPE html>
<html lang="${sessionScope.locale}">

<head>
    <title>Conference Smart App. <fmt:message key="offer.report"/></title>
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
    <tags:header value="offer.report"/>

    <c:set var="error" value="${requestScope.error}"/>

    <c:choose>
        <c:when test="${fn:contains(error, 'offer.forbidden')}">
            <span class="text-danger"><fmt:message key="${requestScope.error}"/></span>
        </c:when>

        <c:otherwise>
            <p class="fs-6"><fmt:message key="event"/>: ${requestScope.event.title} </p>

            <form method="POST" action="controller">
                <input type="hidden" name="action" value="offer-report">
                <input type="hidden" name="event-id" value="${requestScope.event.id}">
                <input type="hidden" name="title" value="${requestScope.event.title}">

                <div>
                    <tags:notEmptyMessage value="${requestScope.message}"/><br>
                    <label class="form-label fs-5" for="topic"><fmt:message key="topic.name"/>: </label>
                    <input class="form-control" name="topic" id="topic"
                           pattern="^[0-9A-Za-zА-ЩЬЮЯҐІЇЄа-щьюяґіїє'.,;:+\-~`!?@#$^&*()={}| ]{2,70}"
                           title="<fmt:message key="topic.requirements"/>"
                           required value="">
                    <tags:contains error="${error}" value="topic"/><br>
                </div>

                <button type="submit" class="btn btn-dark mt-4 mb-4"><fmt:message key="submit"/></button>
            </form>

            <a class="link-dark" href=controller?action=view-event-by-speaker&event-id=${requestScope.event.id}>
                <fmt:message key="back.to.conference"/>
            </a>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="fragments/footer.jsp"/>

</body>
</html>