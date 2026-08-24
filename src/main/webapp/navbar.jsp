<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<nav class="portal-nav">
    <div class="brand">
        <span class="crest">CSP</span>
        College Student Portal
    </div>
    <div class="links">
        <a href="${pageContext.request.contextPath}/dashboard.jsp"
           class="${pageName == 'dashboard' ? 'active' : ''}">Dashboard</a>
        <a href="${pageContext.request.contextPath}/profile"
           class="${pageName == 'profile' ? 'active' : ''}">My Profile</a>
        <c:if test="${sessionScope.role == 'ADMIN'}">
            <a href="${pageContext.request.contextPath}/admin/students"
               class="${pageName == 'admin' ? 'active' : ''}">All Students</a>
        </c:if>
        <a href="${pageContext.request.contextPath}/logout">Log out</a>
    </div>
</nav>