<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Error — College Student Portal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/portal.css">
</head>
<body class="portal">

<nav class="portal-nav">
    <div class="brand"><span class="crest">CSP</span>College Student Portal</div>
</nav>

<main class="portal-main">
    <div class="card state-block">
        <div class="glyph">!</div>
        <h1 style="margin-bottom:8px;">Something went wrong</h1>
        <p>
            <c:choose>
                <c:when test="${not empty error}">${error}</c:when>
                <c:otherwise>An unexpected error occurred. Please try again.</c:otherwise>
            </c:choose>
        </p>
        <a href="${pageContext.request.contextPath}/dashboard.jsp" class="btn btn-primary">Back to Dashboard</a>
    </div>
</main>

</body>
</html>