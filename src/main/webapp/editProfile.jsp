<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Profile — College Student Portal</title>
</head>
<body>

<h1>Edit Profile</h1>

<c:if test="${not empty error}">
    <p style="color:red;">${error}</p>
</c:if>

<form method="post" action="${pageContext.request.contextPath}/editProfile">

    <label>Full Name</label>
    <p>${student.firstName} ${student.lastName}</p>

    <label for="dob">Date of Birth</label>
    <input type="date" id="dob" name="dob" value="${student.dob}" />

    <label for="phone">Phone</label>
    <input type="tel" id="phone" name="phone" value="${student.phone}" />

    <label>Roll Number</label>
    <p>
        <c:choose>
            <c:when test="${empty student.rollNumber}">Not assigned yet</c:when>
            <c:otherwise>${student.rollNumber}</c:otherwise>
        </c:choose>
        (assigned by admin)
    </p>

    <label for="department">Department</label>
    <select id="department" name="department" required>
        <option value="">-- Select department --</option>
        <c:forEach var="dept" items="${departments}">
            <option value="${dept}" ${dept == student.department ? 'selected' : ''}>${dept}</option>
        </c:forEach>
    </select>

    <label for="course">Course</label>
    <input type="text" id="course" name="course" value="${student.course}" required />

    <label for="semester">Semester</label>
    <input type="number" id="semester" name="semester" min="1" max="12" value="${student.semester}" />

    <label for="address">Address</label>
    <textarea id="address" name="address">${student.address}</textarea>

    <button type="submit">Save Profile</button>
</form>

<a href="${pageContext.request.contextPath}/profile">Cancel</a>

</body>
</html>
