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
    <title>Conference Smart App. <fmt:message key="edit.profile"/></title>
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
    <tags:header value="edit.profile"/>

    <form method="POST" action="controller">
        <input type="hidden" name="action" value="edit-profile">
        <c:set var="emailValue" value="${requestScope.user.email eq null ?
                                sessionScope.loggedUser.email : requestScope.user.email}"/>
        <c:set var="nameValue" value="${requestScope.user.name eq null ?
                                sessionScope.loggedUser.name : requestScope.user.name}"/>
        <c:set var="surnameValue" value="${requestScope.user.surname eq null ?
                                sessionScope.loggedUser.surname : requestScope.user.surname}"/>

        <div class="form-group">
            <tags:notEmptyMessage value="${requestScope.message}"/><br>
            <label class="form-label fs-5" for="email"><fmt:message key="email"/>: </label>
            <input class="form-control" type="email" name="email" id="email"
                   pattern="[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$" required value="${emailValue}">
            <tags:contains error="${requestScope.error}" value="email"/><br>
        </div>

        <div class="form-group">
            <label class="form-label fs-5" for="name"><fmt:message key="name"/>*: </label>
            <input class="form-control" name="name" id="name" pattern="^[A-Za-zА-ЩЬЮЯҐІЇЄа-щьюяґіїє'\- ]{1,30}"
                   title="<fmt:message key="name.requirements"/>" required value="${nameValue}">
            <tags:contains error="${requestScope.error}" value=".name"/><br>

        </div>

        <div class="form-group">
            <label class="form-label fs-5" for="surname"><fmt:message key="surname"/>*: </label>
            <input class="form-control" name="surname" id="surname" pattern="^[A-Za-zА-ЩЬЮЯҐІЇЄа-щьюяґіїє'\- ]{1,30}"
                   title="<fmt:message key="surname.requirements"/>" required value="${surnameValue}">
            <tags:contains error="${requestScope.error}" value="surname"/><br>
        </div>

        <button type="submit" class="btn btn-dark mt-4 mb-4"><fmt:message key="submit"/></button>
    </form>

    <p class="fs-6 col-md-8">
        <a href="changePassword.jsp" class="link-dark"><fmt:message key="change.password"/></a>
    </p>
</div>

<jsp:include page="fragments/footer.jsp"/>

</body>
</html>