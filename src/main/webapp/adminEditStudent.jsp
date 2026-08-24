<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Edit Student — College Student Portal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/portal.css">
</head>
<body class="portal">

<c:set var="pageName" value="admin" />
<jsp:include page="/navbar.jsp" />

<main class="portal-main">
    <div class="page-header">
        <span class="eyebrow">Administration</span>
        <h1>Edit Student: ${student.firstName} ${student.lastName}</h1>
    </div>

    <div class="card">
        <form method="post" action="${pageContext.request.contextPath}/admin/editStudent">
            <input type="hidden" name="studentId" value="${student.studentId}" />

            <div class="field">
                <label for="rollNumber">Roll Number</label>
                <input type="text" id="rollNumber" name="rollNumber" value="${student.rollNumber}" />
                <div class="hint">Admin-assigned identifier.</div>
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
                <input type="text" id="course" name="course" value="${student.course}" />
            </div>

            <div class="field">
                <label for="semester">Semester</label>
                <input type="number" id="semester" name="semester" min="1" max="12" value="${student.semester}" />
            </div>

            <div class="field">
                <label for="phone">Phone</label>
                <input type="tel" id="phone" name="phone" value="${student.phone}" />
            </div>

            <div class="field">
                <label for="address">Address</label>
                <textarea id="address" name="address">${student.address}</textarea>
            </div>

            <div class="field">
                <label for="dob">Date of Birth</label>
                <input type="date" id="dob" name="dob" value="${student.dob}" />
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-brass">Save Changes</button>
                <a href="${pageContext.request.contextPath}/admin/students" class="btn btn-outline">Cancel</a>
            </div>
        </form>
    </div>
</main>

</body>
</html>