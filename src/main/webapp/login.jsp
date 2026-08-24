<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
%>

<c:if test="${not empty sessionScope.userId}">
    <c:redirect url="/dashboard.jsp" />
</c:if>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Sign In — College Student Portal</title>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>

    <link
        href="https://fonts.googleapis.com/css2?family=Fraunces:ital,opsz,wght@0,9..144,400;0,9..144,600;1,9..144,500&family=IBM+Plex+Mono:wght@400;500&family=Inter:wght@400;500;600&display=swap"
        rel="stylesheet">

    <link
        rel="stylesheet"
        href="${pageContext.request.contextPath}/resources/css/auth.css">
</head>

<body>

    <div class="auth-page">

        <div class="auth-card">

            <!-- Left Branding Section -->
            <div class="auth-brand">

                <div class="auth-seal">
                    CSP
                </div>

                <p class="eyebrow eyebrow--light">
                    Student Portal Access
                </p>

                <h1>
                    College Student Portal
                </h1>

                <p class="auth-brand-tagline">
                    One account for your profile, records, and documents.
                </p>

                <div class="auth-ledger"></div>

            </div>

            <div class="divider-seam" aria-hidden="true"></div>

            <!-- Login Form Section -->
            <div class="auth-form-panel">

                <p class="eyebrow">
                    Sign In
                </p>

                <h2>
                    Welcome back
                </h2>

                <p class="auth-subheading">
                    Enter your credentials to continue to your dashboard.
                </p>

                <!-- Registration Success Message -->
                <c:if test="${param.registered == 'true'}">
                    <div class="banner banner--success">
                        Your account is ready — sign in to continue.
                    </div>
                </c:if>

                <!-- Login Error Message -->
                <c:if test="${not empty error}">
                    <div class="banner banner--error">
                        <c:out value="${error}" />
                    </div>
                </c:if>

                <!-- Login Form -->
                <form action="${pageContext.request.contextPath}/login" method="post">

                    <div class="field">

                        <label for="username">
                            Username
                        </label>

                        <input
                            type="text"
                            id="username"
                            name="username"
                            required
                            autocomplete="username">

                    </div>

                    <div class="field">

                        <label for="password">
                            Password
                        </label>

                        <input
                            type="password"
                            id="password"
                            name="password"
                            required
                            autocomplete="current-password">

                    </div>

                    <label class="checkbox-row">

                        <input
                            type="checkbox"
                            name="rememberMe"
                            value="true">

                        <span>
                            Keep me signed in on this device
                        </span>

                    </label>

                    <button
                        type="submit"
                        class="btn-primary">
                        Sign in
                    </button>

                </form>

                <!-- Registration Link -->
                <p class="auth-footer-link">
                    New to the portal?
                    <a href="${pageContext.request.contextPath}/register.jsp">
                        Create an account
                    </a>
                </p>

            </div>

        </div>

    </div>

</body>

</html>