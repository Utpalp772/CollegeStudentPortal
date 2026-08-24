<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Dashboard — College Student Portal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/portal.css">
</head>
<body class="portal">

<c:set var="pageName" value="dashboard" />
<jsp:include page="/navbar.jsp" />

<main class="portal-main">
    <div class="page-header">
        <span class="eyebrow">Welcome back</span>
        <h1><c:out value="${sessionScope.username}" /></h1>
    </div>

    <div class="card">
        <p style="margin:0 0 4px 0; color:var(--slate); font-size:0.86rem;">Signed in as</p>
        <span class="badge ${sessionScope.role == 'ADMIN' ? 'badge-admin' : 'badge-student'}">
            <c:out value="${sessionScope.role}" />
        </span>
    </div>

    <div class="stat-grid">
        <a href="${pageContext.request.contextPath}/profile" class="stat-card" style="text-decoration:none;">
            <div class="label">Your Profile</div>
            <div class="value" style="font-size:1.1rem;">View &amp; edit &rarr;</div>
        </a>
        <c:if test="${sessionScope.role == 'ADMIN'}">
            <a href="${pageContext.request.contextPath}/admin/students" class="stat-card" style="text-decoration:none;">
                <div class="label">Administration</div>
                <div class="value" style="font-size:1.1rem;">All students &rarr;</div>
            </a>
        </c:if>
    </div>
</main>

</body>
</html>