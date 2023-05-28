<%@ page contentType="text/html;charset=UTF-8"%>
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
    <title>Conference Smart App. <fmt:message key="create.event"/></title>
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
    <tags:header value="create.event"/>

    <form method="POST" action="controller">
        <input type="hidden" name="action" value="create-event">

        <div>
            <label class="form-label fs-5" for="title"><fmt:message key="title"/>*: </label>
            <input class="form-control" name="title" id="title"
                   pattern="^[0-9A-Za-zА-ЩЬЮЯҐІЇЄа-щьюяґіїє'.,;:+\-~`!?@#$^&*()={}| ]{2,70}"
                   required value="${requestScope.event.title}"
                   title="<fmt:message key="title.requirements"/>">
            <tags:contains error="${requestScope.error}" value="title"/><br>
        </div>

        <div>
            <label class="form-label fs-5" for="date"><fmt:message key="date"/>*: </label>
            <input class="form-control" type="date" name="date" id="date"
                   required value="${requestScope.event.date}" min="<custom:now/>"/>
            <tags:contains error="${requestScope.error}" value="date"/><br>
        </div>

        <div>
            <label class="form-label fs-5" for="location"><fmt:message key="location"/>*: </label>
            <input class="form-control" name="location" id="location"
                   pattern="^[0-9A-Za-zА-ЩЬЮЯҐІЇЄа-щьюяґіїє'.,;:+\-~`!@#$^&*()={}| ]{2,70}"
                   required value="${requestScope.event.location}"
                   title="<fmt:message key="location.requirements"/>">
            <tags:contains error="${requestScope.error}" value="location"/><br>
        </div>

        <div>
            <label class="form-label fs-5" for="description"><fmt:message key="description"/>*: </label>
            <input class="form-control" name="description" id="description"
                   pattern="^[0-9A-Za-zА-ЩЬЮЯҐІЇЄа-щьюяґіїє'.,;:+\-~`!@#$^&*()={}| ]{1,200}"
                   required value="${requestScope.event.description}"
                   title="<fmt:message key="description.requirements"/>">
            <tags:contains error="${requestScope.error}" value="description"/><br>
        </div>

        <button type="submit" class="btn btn-dark mt-4 mb-4"><fmt:message key="submit"/></button>
    </form>
</div>

<jsp:include page="fragments/footer.jsp"/>

</body>
</html>