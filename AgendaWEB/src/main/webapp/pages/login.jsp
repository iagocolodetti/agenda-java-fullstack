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
        <title>Login</title>
    </head>
    <body>
        <div class="container-fluid">
            <div class="d-flex justify-content-center text-center">
                <div class="form-group col-xs-12 col-sm-10 col-md-8 col-lg-5">
                    <h4 class="mb-3 text-muted">Login</h4>
                    <form class="card mb-4 justify-content-center" action="login" method="POST">
                        <div class="card-body">
                            <label class="form-group has-float-label">
                                <input class="form-control" type="text" id="username" name="username" value="${username}" placeholder="Nome" required />
                                <span>Nome</span>
                            </label>
                            <label class="form-group has-float-label">
                                <input class="form-control" type="password" id="password" name="password" value="${password}" placeholder="Senha" required />
                                <span>Senha</span>
                            </label>
                            <jsp:include page="../components/div-alert.jsp">
                                <jsp:param name="message" value="${message}" />
                                <jsp:param name="alert" value="${alert}" />
                            </jsp:include>
                            <div class="d-flex justify-content-between">
                                <a class="nav-link" href="${cp}/new">Criar conta</a>
                                <input type="submit" class="btn btn-primary btn-fix" value="Login" />
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>
