<%--
  Created by IntelliJ IDEA.
  User: Drapegnik
  Date: 02.05.16
  Time: 12:39
  To change this template use File | Settings | File Templates.
--%>
<%@  taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="stylesheet" href="normalize.css">
    <link rel="stylesheet" href="responsive.css">
    <link rel="stylesheet" href="style.css">
    <title>Login | Chat</title>
</head>

<script>
    function getCookie(name) {
        var matches = document.cookie.match(new RegExp(
                "(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
        ));
        return matches ? decodeURIComponent(matches[1]) : undefined;
    }

    if (getCookie('uid') != null)
        window.location = "http://localhost:8080/homepage.jsp";
</script>

<body class="all">
<header class="header">
    <div class="hed-inn">
        <div class="headline">
            <h1>Chat</h1>
        </div>
    </div>
</header>
<section class="content">
    <form id="login_form" class="form-group" action="/login" method="post">
        <label class="control-label marg" for="name">Login:</label>
        <input class="form-control marg" id="name" name="name" type="text" value="User"
               placeholder="you username">

        <label class="control-label marg" for="pass">Password:</label>
        <input class="form-control marg" id="pass" name="pass" type="password"
               placeholder="you password">
        <p class="control-label marg err">
            <c:choose>
                <c:when test="${requestScope.errorMessage!=null}">
                    <c:out value="${requestScope.errorMessage}"></c:out>
                </c:when>
                <c:otherwise></c:otherwise>
            </c:choose>
        </p>
        <button type=”submit” class="btn marg">Sign in</button>
    </form>
    <p>(for example User: 1234)</p>
</section>
<footer class="footer">
    <h4 class="footer-text">Ivan Pazhitnykh, FAMCS, BSU, 2016</h4>
</footer>

</body>
</html>
