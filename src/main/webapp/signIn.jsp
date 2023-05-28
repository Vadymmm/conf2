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
    <title>Conference Smart App. <fmt:message key="sign.in"/></title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/my.css">
    <script src="js/bootstrap.min.js"></script>
    <script src="js/showPass.js"></script>
</head>

<body>

<jsp:include page="fragments/mainMenu.jsp"/>

<div class="col-lg-5 mx-auto p-4 py-md-5">
    <tags:header value="sign.in"/>

    <form method="POST" action="controller">
        <input type="hidden" name="action" value="sign-in">

        <div class="mb-1">
            <tags:notEmptyMessage value="${requestScope.message}"/><br>
            <label class="form-label fs-5" for="email"><fmt:message key="email"/>: </label>
            <input class="form-control" type="email" name="email" id="email"
                   pattern="[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$" required value="${requestScope.email}">
            <tags:contains error="${requestScope.error}" value="email"/><br>
        </div>

        <div class="mb-1">
            <label class="form-label  fs-5" for="password"><fmt:message key="password"/>: </label>
            <input class="form-control" type="password" name="password" id="password" required>
            <tags:contains error="${requestScope.error}" value="pass"/><br>
            <div class="form-check">
                <input class="form-check-input" type="checkbox" onclick="showPass('password')" id="flexCheckDefault">
                <label class="form-check-label" for="flexCheckDefault"><fmt:message key="show.password"/></label>
            </div>
        </div>

        <button type="submit" class="btn btn-dark mt-4 mb-4"><fmt:message key="sign.in"/></button>
    </form>

    <p class="fs-6 col-md-8">
        <fmt:message key="forgot.password"/>
        <a href="resetPassword.jsp" class="link-dark"><fmt:message key="reset.password"/></a>
    </p>

    <p class="fs-6 col-md-8">
        <fmt:message key="no.account"/>
        <a href="signUp.jsp" class="link-dark"><fmt:message key="sign.up"/></a>
    </p>
</div>

<jsp:include page="fragments/footer.jsp"/>

</body>
</html>