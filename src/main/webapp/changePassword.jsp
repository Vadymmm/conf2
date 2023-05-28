<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="resources"/>

<!DOCTYPE html>
<html lang="${sessionScope.locale}">

<head>
    <title>Conference Smart App. <fmt:message key="change.password"/></title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/my.css">
    <script src="js/bootstrap.min.js"></script>
    <script src="js/showPass.js"></script>
</head>

<body>

<jsp:include page="fragments/mainMenu.jsp"/>

<jsp:include page="fragments/menuChoice.jsp"/>

<div class="col-lg-5 mx-auto p-4 py-md-5">
    <tags:header value="change.password"/>

    <form method="POST" action="controller">
        <input type="hidden" name="action" value="change-password">

        <div class="form-group">
            <tags:notEmptyMessage value="${requestScope.message}"/><br>
            <label class="form-label fs-5" for="old-password"><fmt:message key="old.password"/>*: </label>
            <input class="form-control" type="password" name="old-password" id="old-password" required>
            <tags:notEmptyError value="${requestScope.error}"/><br>
        </div>

        <div class="form-group">
            <label class="form-label fs-5" for="password"><fmt:message key="new.password"/>*: </label>
            <input class="form-control" type="password" name="password" id="password"
                   pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,20}$"
                   title="<fmt:message key="password.requirements"/>" required><br>
        </div>

        <div class="form-group">
            <label class="form-label fs-5" for="confirm-password"><fmt:message key="confirm.password"/>*: </label>
            <input class="form-control" type="password" name="confirm-password" id="confirm-password"
                   pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,20}$"
                   title="<fmt:message key="password.requirements"/>" required><br>
        </div>

        <div class="form-check">
            <input class="form-check-input" type="checkbox" id="flexCheckDefault"
                   onclick="showPass('old-password'); showPass('password'); showPass('confirm-password')">
            <label class="form-check-label" for="flexCheckDefault"><fmt:message key="show.password"/></label>
        </div><br>

        <button type="submit" class="btn btn-dark mt-4 mb-4"><fmt:message key="change.password"/></button>
    </form>
</div>

<jsp:include page="fragments/footer.jsp"/>

</body>
</html>