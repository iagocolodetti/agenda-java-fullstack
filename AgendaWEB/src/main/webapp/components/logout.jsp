<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix ="c"%>
<c:set var="cp" value="${pageContext.request.contextPath}" />
<nav class="navbar navbar-expand py-0">
    <ul class="navbar-nav ml-auto">
        <li class="nav-item">
            <a class="nav-link" href="${cp}/logout">Logout</a>
        </li>
    </ul>
</nav>
