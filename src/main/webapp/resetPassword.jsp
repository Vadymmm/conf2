<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="resources"/>

<!DOCTYPE html>
<html lang="${sessionScope.locale}">

<head>
    <title>Conference Smart App. <fmt:message key="reset.password"/></title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/my.css">
    <script src="js/bootstrap.min.js"></script>
</head>

<body>

<jsp:include page="fragments/mainMenu.jsp"/>

<div class="col-lg-5 mx-auto p-4 py-md-5">
    <tags:header value="reset.password"/>

    <form method="POST" action="controller">
        <input type="hidden" name="action" value="password-reset">

        <div>
            <tags:notEmptyMessage value="${requestScope.message}"/><br>
            <label class="form-label fs-5" for="email"><fmt:message key="email"/>: </label>
            <input class="form-control" type="email" name="email" id="email" required
                   value="${not empty requestScope.email ? requestScope.email : ""}"
                   pattern="[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$">
            <tags:notEmptyError value="${requestScope.error}"/><br>
        </div>

        <button type="submit" class="btn btn-dark mt-4 mb-4"><fmt:message key="reset.password"/></button>
    </form>
</div>

<jsp:include page="fragments/footer.jsp"/>

</body>
</html>