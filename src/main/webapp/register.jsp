<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Create Account — College Student Portal</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Fraunces:ital,opsz,wght@0,9..144,400;0,9..144,600;1,9..144,500&family=IBM+Plex+Mono:wght@400;500&family=Inter:wght@400;500;600&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/auth.css">
</head>
<body>
  <div class="auth-page">
    <div class="auth-card">

      <div class="auth-brand">
        <div class="auth-seal">CSP</div>
        <p class="eyebrow eyebrow--light">New Registration</p>
        <h1>College Student Portal</h1>
        <p class="auth-brand-tagline">Set up access to your profile, records, and documents.</p>
        <div class="auth-ledger"></div>
      </div>

      <div class="divider-seam" aria-hidden="true"></div>

      <div class="auth-form-panel">
        <p class="eyebrow">Create Account</p>
        <h2>Join the portal</h2>
        <p class="auth-subheading">Register with your college email to get started.</p>

        <c:if test="${not empty error}">
          <div class="banner banner--error"><c:out value="${error}" /></div>
        </c:if>

        <form action="register" method="post">
          <div class="field">
            <label for="username">Username</label>
            <input type="text" id="username" name="username" required autocomplete="username">
          </div>

          <div class="field">
            <label for="email">College email</label>
            <input type="email" id="email" name="email" required autocomplete="email">
          </div>

          <div class="field">
            <label for="password">Password</label>
            <input type="password" id="password" name="password" required minlength="8" autocomplete="new-password">
            <p class="hint">At least 8 characters.</p>
          </div>

          <div class="field">
            <label for="confirmPassword">Confirm password</label>
            <input type="password" id="confirmPassword" name="confirmPassword" required autocomplete="new-password">
          </div>

          <button type="submit" class="btn-primary">Create account</button>
        </form>

        <p class="auth-footer-link">Already registered? <a href="login.jsp">Sign in</a></p>
      </div>

    </div>
  </div>
</body>
</html>