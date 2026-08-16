<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Student — College Student Portal</title>
</head>
<body>

<h1>Edit Student: ${student.firstName} ${student.lastName}</h1>

<form method="post" action="${pageContext.request.contextPath}/admin/editStudent">
    <input type="hidden" name="studentId" value="${student.studentId}" />

    <label for="rollNumber">Roll Number</label>
    <input type="text" id="rollNumber" name="rollNumber" value="${student.rollNumber}" />

    <label for="department">Department</label>
    <select id="department" name="department" required>
        <option value="">-- Select department --</option>
        <c:forEach var="dept" items="${departments}">
            <option value="${dept}" ${dept == student.department ? 'selected' : ''}>${dept}</option>
        </c:forEach>
    </select>

    <label for="course">Course</label>
    <input type="text" id="course" name="course" value="${student.course}" />

    <label for="semester">Semester</label>
    <input type="number" id="semester" name="semester" min="1" max="12" value="${student.semester}" />

    <label for="phone">Phone</label>
    <input type="tel" id="phone" name="phone" value="${student.phone}" />

    <label for="address">Address</label>
    <textarea id="address" name="address">${student.address}</textarea>

    <label for="dob">Date of Birth</label>
    <input type="date" id="dob" name="dob" value="${student.dob}" />

    <button type="submit">Save Changes</button>
</form>

<a href="${pageContext.request.contextPath}/admin/students">Cancel</a>

</body>
</html>