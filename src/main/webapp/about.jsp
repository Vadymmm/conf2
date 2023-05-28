<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="resources"/>

<!DOCTYPE html>
<html lang="${sessionScope.locale}">

<head>
    <title>Conference Smart App. <fmt:message key="about"/></title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <script src="js/bootstrap.min.js"></script>
</head>

<body>

<jsp:include page="fragments/mainMenu.jsp"/>

<jsp:include page="fragments/menuChoice.jsp"/>

<div class="col-lg-7 mx-auto p-4 py-md-5">
    <tags:header value="about"/>

    <main>
        <p class="fs-5 col-md-12">Finding the right Conference venue in the right location at the right price to meet the budget
            can be very time-consuming and stressful for the Meeting booker, so why not use the FREE service
            provided by an experienced Conference Agency.</p><br>

        <p class="fs-5 col-md-12">An experienced conference agent will understand what it takes to make the meeting successful.
            They will take the brief and source a minimum of three options in a location of the client's choice.
            These days there are a number of other options available instead of hotels and conference centres,
            the choices may include unusual venues the client may not be familiar with.</p>
    </main>
</div>

<jsp:include page="fragments/footer.jsp"/>

</body>
</html>