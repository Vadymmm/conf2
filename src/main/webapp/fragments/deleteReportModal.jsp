<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="resources"/>

<div class="modal fade" id="exampleModalDefault" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content rounded-4 shadow">
            <div class="modal-header border-bottom-0">
                <h1 class="modal-title fs-5 text-md-center"><fmt:message key="delete.report"/></h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body py-0">
                <p><fmt:message key="delete.report.confirmation"/></p>
            </div>
            <div class="modal-footer flex-column border-top-0">
                <form method="POST" action="controller">
                    <input type="hidden" name="action" value="delete-report">
                    <input type="hidden" name="report-id" value="${requestScope.report.id}">
                    <input type="hidden" name="topic" value="${requestScope.report.topic}">
                    <input type="hidden" name="event-id" value="${requestScope.report.eventId}">
                    <input type="hidden" name="title" value="${requestScope.report.title}">
                    <input type="hidden" name="date" value="${requestScope.report.date}">
                    <input type="hidden" name="location" value="${requestScope.report.location}">
                    <input type="hidden" name="email" value="${requestScope.report.speakerEmail}">
                    <input type="hidden" name="name" value="${requestScope.report.speakerName}">
                    <button type="submit" class="btn btn-dark mt-4 mb-4"><fmt:message key="yes"/></button>
                </form>
            </div>
        </div>
    </div>
</div>
