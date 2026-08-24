<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Edit Profile — College Student Portal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/portal.css">
</head>
<body class="portal">

<c:set var="pageName" value="profile" />
<jsp:include page="/navbar.jsp" />

<main class="portal-main">
    <div class="page-header">
        <span class="eyebrow">Student Record</span>
        <h1>Edit Profile</h1>
    </div>

    <div class="card">
        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/editProfile" enctype="multipart/form-data">

            <div class="field">
                <label>Full Name</label>
                <div class="static-value">${student.firstName} ${student.lastName}</div>
            </div>

            <div class="field">
                <label for="dob">Date of Birth</label>
                <input type="date" id="dob" name="dob" value="${student.dob}" />
            </div>

            <div class="field">
                <label for="phone">Phone</label>
                <input type="tel" id="phone" name="phone" value="${student.phone}" />
            </div>

            <div class="field">
                <label>Roll Number</label>
                <div class="static-value">
                    <c:choose>
                        <c:when test="${empty student.rollNumber}">Not assigned yet</c:when>
                        <c:otherwise>${student.rollNumber}</c:otherwise>
                    </c:choose>
                </div>
                <div class="hint">Assigned by admin — not student-editable.</div>
            </div>

            <div class="field">
                <label for="department">Department</label>
                <select id="department" name="department" required>
                    <option value="">-- Select department --</option>
                    <c:forEach var="dept" items="${departments}">
                        <option value="${dept}" ${dept == student.department ? 'selected' : ''}>${dept}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="field">
                <label for="course">Course</label>
                <input type="text" id="course" name="course" value="${student.course}" required />
            </div>

            <div class="field">
                <label for="semester">Semester</label>
                <input type="number" id="semester" name="semester" min="1" max="12" value="${student.semester}" />
            </div>

            <div class="field">
                <label for="address">Address</label>
                <textarea id="address" name="address">${student.address}</textarea>
            </div>

            <div class="field">
                <label for="photo">Profile Photo</label>
                <input type="file" id="photo" name="photo" accept="image/jpeg,image/png,image/webp" />
                <div class="hint">JPEG, PNG, or WebP. Max 2MB.</div>
            </div>

            <c:if test="${not empty student.profilePhoto}">
                <div class="field">
                    <label style="display:flex; align-items:center; gap:8px; font-weight:500;">
                        <input type="checkbox" name="removePhoto" value="true" style="width:auto;" />
                        Remove current photo
                    </label>
                </div>
            </c:if>

            <div class="form-actions">
                <button type="submit" class="btn btn-brass">Save Profile</button>
                <a href="${pageContext.request.contextPath}/profile" class="btn btn-outline">Cancel</a>
            </div>
        </form>
    </div>
</main>

</body>
</html>