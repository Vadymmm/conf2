<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="resources"/>

<c:choose>
    <c:when test="${requestScope.isRegistered}">
        <button class="btn btn-dark mt-4 mb-4" data-bs-toggle="modal" data-bs-target="#exampleModalDefault2">
            <fmt:message key="cancel.registration"/>
        </button>
    </c:when>
    <c:otherwise>
        <button class="btn btn-dark mt-4 mb-4" data-bs-toggle="modal" data-bs-target="#exampleModalDefault1">
            <fmt:message key="register"/>
        </button>
    </c:otherwise>
</c:choose>

<div class="modal fade" id="exampleModalDefault1" tabindex="-1" aria-labelledby="exampleModalLabel1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content rounded-4 shadow">
            <div class="modal-header border-bottom-0">
                <h1 class="modal-title fs-5 text-md-center"><fmt:message key="register.for.event"/></h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body py-0">
                <p><fmt:message key="register.for.event.confirmation"/></p>
            </div>
            <div class="modal-footer flex-column border-top-0">
                <form method="POST" action="controller">
                    <input type="hidden" name="action" value="register-or-cancel">
                    <input type="hidden" name="todo" value="register">
                    <input type="hidden" name="event-id" value="${requestScope.event.id}">
                    <button type="submit" class="btn btn-dark mt-4 mb-4"><fmt:message key="register"/></button>
                </form>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="exampleModalDefault2" tabindex="-1" aria-labelledby="exampleModalLabel2" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content rounded-4 shadow">
            <div class="modal-header border-bottom-0">
                <h1 class="modal-title fs-5 text-md-center"><fmt:message key="cancel.registration"/></h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body py-0">
                <p><fmt:message key="cancel.registration.confirmation"/></p>
            </div>
            <div class="modal-footer flex-column border-top-0">
                <form method="POST" action="controller">
                    <input type="hidden" name="action" value="register-or-cancel">
                    <input type="hidden" name="todo" value="cancel">
                    <input type="hidden" name="event-id" value="${requestScope.event.id}">
                    <button type="submit" class="btn btn-dark mt-4 mb-4">
                        <fmt:message key="cancel.registration"/>
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>