<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix ="c"%>
<c:set var="cp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"/>
        <link rel="stylesheet" type="text/css" href="${cp}/css/bootstrap-float-label.min.css"/>
        <link rel="stylesheet" type="text/css" href="${cp}/css/global.css"/>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"/>
        <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.8.0/angular.min.js"></script>
        <script src="${cp}/js/form-contact.js"></script>
        <title>Contacts</title>
    </head>
    <body>
        <div class="container-fluid">
            <jsp:include page="../components/logout.jsp" />
            <div class="row">
                <div class="col-sm-4 order-md-2">
                    <div class="sticky-top">
                        <jsp:include page="../components/form-contact.jsp" />
                    </div>
                </div>
                <div class="col-sm-8 order-md-1">
                    <h4 class="mb-3 text-muted">Contatos</h4>
                    <jsp:include page="../components/div-alert.jsp">
                        <jsp:param name="message" value="${message}" />
                        <jsp:param name="alert" value="${alert}" />
                    </jsp:include>
                    <div class="row">
                        <c:if test="${contacts != null}">
                            <c:forEach var="c" items="${contacts}">
                                <div class="col-xs-12 col-sm-6 col-md-6 col-lg-6 mb-4">
                                    <form class="card" action="contacts" method="POST">
                                        <div class="card-body">
                                            <div class="d-flex justify-content-between mb-2">
                                                <h5 class="card-title">${c.name}</h5>
                                                <div>
                                                    <button type="submit" class="btn btn-outline-secondary btn-sm mr-3" id="update" name="update" value='${c.toJson()}'>
                                                        <span class="fa fa-edit" />
                                                    </button>
                                                    <button type="submit" class="btn btn-outline-danger btn-sm" id="delete" name="delete" value='${c.getId()}'>
                                                        <span class="fa fa-trash" />
                                                    </button>
                                                </div>
                                            </div>
                                            <h6 class="card-subtitle mb-4 text-muted">${c.alias}</h6>
                                            <h6 class="card-subtitle mb-1 text-muted">Telefone(s)</h6>
                                            <ul class="list-group text-center">
                                                <c:forEach var="p" items="${c.phone}">
                                                    <li class="list-group-item">
                                                        ${p.phone}
                                                    </li>
                                                </c:forEach>
                                            </ul>
                                            <h6 class="card-subtitle mt-3 mb-1 text-muted">E-mail(s)</h6>
                                            <ul class="list-group text-center">
                                                <c:forEach var="e" items="${c.email}">
                                                    <li class="list-group-item">
                                                        ${e.email}
                                                    </li>
                                                </c:forEach>
                                            </ul>
                                        </div>
                                    </form>
                                </div>
                            </c:forEach>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
