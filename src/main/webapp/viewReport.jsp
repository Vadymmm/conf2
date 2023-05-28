<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="resources"/>

<!DOCTYPE html>
<html lang="${sessionScope.locale}">

<head>
    <title>Conference Smart App. <fmt:message key="view.report"/></title>
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
        <span class="fs-4">${requestScope.report.topic}</span>
    </header>

    <span class="fs-5">
        <fmt:message key="speaker.name"/>: ${requestScope.report.speakerName} &nbsp&nbsp
    </span>
    <c:if test="${requestScope.isComing}">
        <c:choose>
            <c:when test="${not empty requestScope.report.speakerName}">
                <form method="post" action="controller">
                    <input type="hidden" name="action" value="set-or-remove-speaker">
                    <input type="hidden" name="todo" value="remove">
                    <input type="hidden" name="report-id" value="${requestScope.report.id}">
                    <input type="hidden" name="email" value="${requestScope.report.speakerEmail}">
                    <input type="hidden" name="name" value="${requestScope.report.speakerName}">
                    <button type="submit" class="btn btn-dark mt-3 mb-4"><fmt:message key="remove.speaker"/></button>
                </form>
            </c:when>

            <c:otherwise>
                <form method="post" action="controller">
                    <input type="hidden" name="action" value="set-or-remove-speaker">
                    <input type="hidden" name="todo" value="set">
                    <input type="hidden" name="report-id" value="${requestScope.report.id}">
                    <label>
                        <select name="user-id" class="form-select mt-2">
                            <c:forEach var="speaker" items="${requestScope.speakers}">
                                <option value="${speaker.id}">${speaker.name} ${speaker.surname}</option>
                            </c:forEach>
                        </select>
                    </label>
                    <button type="submit" class="btn btn-dark mt-3 mb-4"><fmt:message key="set.speaker"/></button>
                </form>
            </c:otherwise>
        </c:choose>
    </c:if>

    <tags:notEmptyMessage value="${requestScope.message}"/>
    <tags:notEmptyError value="${requestScope.error}"/><br>

    <c:if test="${requestScope.isComing}">

        <form method="post" action="controller">
            <input type="hidden" name="action" value="change-topic">
            <input type="hidden" name="report-id" value="${requestScope.report.id}">
            <label class="fs-5" for="topic"><fmt:message key="change.topic"/>: </label>
            <input class="col-7"  name="topic" id="topic" required value="${requestScope.report.topic}"
                   pattern="^[0-9A-Za-zА-ЩЬЮЯҐІЇЄа-щьюяґіїє'.,;:+\-~`!?@#$^&*()={}| ]{2,70}"
                   title="<fmt:message key="topic.requirements"/>">
            <button type="submit" class="btn btn-dark mt-3 mb-4"><fmt:message key="submit"/></button>
        </form>


        <button class="btn btn-dark mt-3 mb-4" data-bs-toggle="modal" data-bs-target="#exampleModalDefault">
            <fmt:message key="delete"/>
        </button>

    </c:if>

    <br><br>
    <a class="link-dark" href="controller?action=search-event&event-id=${requestScope.report.eventId}">
        <fmt:message key="back.to.conference"/>
    </a>
</div>

<jsp:include page="fragments/footer.jsp"/>

<jsp:include page="fragments/deleteReportModal.jsp"/>

</body>
</html>