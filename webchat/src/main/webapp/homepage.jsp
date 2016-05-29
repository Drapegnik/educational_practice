<%@  taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>

<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">


    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="normalize.css">
    <link rel="stylesheet" href="responsive.css">
    <link rel="stylesheet" href="style.css">
    <script src="scripts.js"></script>
    <title>Chat</title>
</head>
<body onload="run();" class="all">

<header class="header">
    <div class="hed-inn">
        <div class="headline">
            <h1>Chat</h1>
        </div>
        <div class="status">
            <span>server: <span hidden id="err"><i style="color: #d9534f;"
                                                   class="fa fa-exclamation-triangle"></i></span>
                <span id="ok"><i style="color: #5cb85c;" class="fa fa-check-circle"></i></span></span>
        </div>
    </div>
</header>
<section class="content">
    <div class="form-group">
        <label class="control-label marg" for="name">Hello, <span id="locname"></span> <c:out
                value="${requestScope.username}"></c:out>!</label>
        <input id="name" type="text" value="${requestScope.username}" hidden><br>
        <button class="logout btn marg" onclick="logOut()">Log out</button>
    </div>
    <div class="chat-block">
        <ul id="chat" class="chat">

        </ul>

        <div id="sendarea">
            <input onkeypress="enter(event)" class="form-control marg" type="text" id="mes"
                   placeholder="say something"/>
            <button onclick="send()" class="btn marg">Send</button>
        </div>
    </div>
</section>
<footer class="footer">
    <h4 class="footer-text">Ivan Pazhitnykh, FAMCS, BSU, 2016</h4>
</footer>

</body>
</html>