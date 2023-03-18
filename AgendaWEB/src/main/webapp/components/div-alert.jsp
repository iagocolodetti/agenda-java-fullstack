<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix ="c"%>
<c:if test="${!param.message.isEmpty()}">
    <div class="d-flex justify-content-center mt-4 mb-2">
        <span class="alert ${param.alert} alert-fix text-center" role="alert">
            ${param.message}
        </span>
    </div>
</c:if>