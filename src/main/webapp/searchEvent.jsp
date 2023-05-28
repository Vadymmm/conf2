<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="resources"/>

<!DOCTYPE html>
<html lang="${sessionScope.locale}">

<head>
    <title>Conference Smart App. <fmt:message key="search.event"/></title>
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
    <tags:header value="search.event"/>

    <form method="GET" action="controller">
        <input type="hidden" name="action" value="search-event">

        <div>
            <tags:notEmptyMessage value="${requestScope.message}"/><br>
            <label class="form-label fs-5" for="title"><fmt:message key="search.event"/></label>
            <input class="form-control" name="title" id="title" value="${requestScope.title}"
                   pattern="^[0-9A-Za-zА-ЩЬЮЯҐІЇЄа-щьюяґіїє'.,;:+\-~`!@#$^&*()={}| ]{2,70}"
                   title="<fmt:message key="title.requirements"/>"
                   required>
            <tags:notEmptyError value="${requestScope.error}"/><br>
        </div>

        <button type="submit" class="btn btn-dark mt-3 mb-4"><fmt:message key="search"/></button>

    </form>
</div>

<jsp:include page="fragments/footer.jsp"/>

</body>
</html>
