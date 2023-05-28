<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="resources"/>

<!DOCTYPE html>
<html lang="${sessionScope.locale}">

<head>
    <title>Conference Smart App. <fmt:message key="contacts"/></title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <script src="js/bootstrap.min.js"></script>
</head>
<body>

<jsp:include page="fragments/mainMenu.jsp"/>

<jsp:include page="fragments/menuChoice.jsp"/>

<div class="col-lg-7 mx-auto p-4 py-md-5">
    <tags:header value="contacts"/>

    <main>
        <p class="fs-5 col-md-8">York House, 18 York Road, Maidenhead, Berkshire, SL6 1SF, UK</p>

        <p class="fs-5 col-md-8">Registered in England & Wales Company number 02118204</p>

        <p class="fs-5 col-md-8">+44 (0) 1628 773300 enquiries@conferencecontacts.co.uk</p>
    </main>
</div>

<jsp:include page="fragments/footer.jsp"/>

</body>
</html>