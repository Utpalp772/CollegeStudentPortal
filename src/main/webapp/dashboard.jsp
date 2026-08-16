<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard</title>
</head>

<body>

    <h2>
        Welcome, <c:out value="${sessionScope.username}" />!
    </h2>

    <p>
        Role: <c:out value="${sessionScope.role}" />
    </p>

    <a href="logout">Log out</a>

    <a href="${pageContext.request.contextPath}/profile">
        My Profile
    </a>

    <c:if test="${sessionScope.role == 'ADMIN'}">
        | <a href="${pageContext.request.contextPath}/admin/students">
            All Students
        </a>
    </c:if>

</body>
</html>