<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Dashboard</title></head>
<body>
  <h2>Welcome, <c:out value="${sessionScope.username}" />!</h2>
  <p>Role: <c:out value="${sessionScope.role}" /></p>
  <a href="logout">Log out</a>
</body>
</html>