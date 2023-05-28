<%@ attribute name="value" required="true" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="resources"/>

<header class="d-flex align-items-center pb-3 mb-3 border-bottom">
    <span class="fs-4"><fmt:message key="${value}"/></span>
</header>