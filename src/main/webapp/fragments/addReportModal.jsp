<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="resources"/>

<div class="modal fade" id="exampleModalDefault2" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content rounded-4 shadow">
            <div class="modal-header border-bottom-0">
                <h1 class="modal-title fs-5 text-md-center"><fmt:message key="add.report"/></h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-footer flex-column border-top-0">
                <form method="POST" action="controller">
                    <input type="hidden" name="action" value="create-report">
                    <input type="hidden" name="event-id" value=${requestScope.event.id}>
                    <label class="form-label fs-5" for="topic"><fmt:message key="topic.name"/>*: </label>
                    <input class="col-4" name="topic" id="topic" required
                           pattern="^[0-9A-Za-zА-ЩЬЮЯҐІЇЄа-щьюяґіїє'.,;:+\-~`!@#$^&*()={}| ]{2,70}">
                    <button type="submit" class="btn btn-dark mt-4 mb-4"><fmt:message key="add.report"/></button>
                </form>
            </div>
        </div>
    </div>
</div>
