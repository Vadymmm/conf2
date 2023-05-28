<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="resources"/>

<!DOCTYPE html>
<html lang="${sessionScope.locale}">

<head>
    <title>Conference Smart App. <fmt:message key="view.user"/></title>
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
    <tags:header value="view.user"/>

    <c:set var="user" value="${requestScope.user}"/>

    <main>
        <p class="fs-5"><fmt:message key="email"/>: ${requestScope.user.email}</p>
        <p class="fs-5"><fmt:message key="name"/>: ${requestScope.user.name}</p>
        <p class="fs-5"><fmt:message key="surname"/>: ${requestScope.user.surname}</p>
        <p class="fs-5"><fmt:message key="role"/>: <fmt:message key="${requestScope.user.role}"/></p>
    </main>

    <form method="POST" action="controller">
        <input type="hidden" name="action" value="set-role">
        <input type="hidden" name="email" value=${requestScope.user.email}>
        <label>
            <select name="role" class="form-select mt-2">
                <option value="VISITOR" ${requestScope.user.role eq 'VISITOR' ? 'selected' : ''}>
                    <fmt:message key="VISITOR"/>
                </option>
                <option value="SPEAKER" ${requestScope.user.role eq 'SPEAKER' ? 'selected' : ''}>
                    <fmt:message key="SPEAKER"/>
                </option>
                <option value="MODERATOR" ${requestScope.user.role eq 'MODERATOR' ? 'selected' : ''}>
                    <fmt:message key="MODERATOR"/>
                </option>
                <option value="ADMIN" ${requestScope.user.role eq 'ADMIN' ? 'selected' : ''}>
                    <fmt:message key="ADMIN"/>
                </option>
            </select>
        </label>
        <button type="submit" class="btn btn-dark mt-3 mb-4"><fmt:message key="set.role"/></button>
    </form>

    <button class="btn btn-dark mt-4 mb-4" data-bs-toggle="modal" data-bs-target="#exampleModalDefault">
        <fmt:message key="delete"/>
    </button>
</div>

<jsp:include page="fragments/footer.jsp"/>

<jsp:include page="fragments/deleteUserModal.jsp"/>

</body>
</html>