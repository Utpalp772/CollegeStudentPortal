<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Profile — College Student Portal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/auth.css">
</head>
<body>

<h1>My Profile</h1>

<table>
    <tr>
        <th>Full Name</th>
        <td>${student.firstName} ${student.lastName}</td>
    </tr>
    <tr>
        <th>Date of Birth</th>
        <td>
            <c:choose>
                <c:when test="${empty student.dob}">Not set</c:when>
                <c:otherwise>${student.dob}</c:otherwise>
            </c:choose>
        </td>
    </tr>
    <tr>
        <th>Phone</th>
        <td>
            <c:choose>
                <c:when test="${empty student.phone}">Not set</c:when>
                <c:otherwise>${student.phone}</c:otherwise>
            </c:choose>
        </td>
    </tr>
    <tr>
        <th>Roll Number</th>
        <td>
            <c:choose>
                <c:when test="${empty student.rollNumber}">Not set</c:when>
                <c:otherwise>${student.rollNumber}</c:otherwise>
            </c:choose>
        </td>
    </tr>
    <tr>
        <th>Department</th>
        <td>
            <c:choose>
                <c:when test="${empty student.department}">Not set</c:when>
                <c:otherwise>${student.department}</c:otherwise>
            </c:choose>
        </td>
    </tr>
    <tr>
        <th>Course</th>
        <td>
            <c:choose>
                <c:when test="${empty student.course}">Not set</c:when>
                <c:otherwise>${student.course}</c:otherwise>
            </c:choose>
        </td>
    </tr>
    <tr>
        <th>Semester</th>
        <td>
            <c:choose>
                <c:when test="${empty student.semester}">Not set</c:when>
                <c:otherwise>${student.semester}</c:otherwise>
            </c:choose>
        </td>
    </tr>
    <tr>
        <th>Address</th>
        <td>
            <c:choose>
                <c:when test="${empty student.address}">Not set</c:when>
                <c:otherwise>${student.address}</c:otherwise>
            </c:choose>
        </td>
    </tr>
</table>

<a href="${pageContext.request.contextPath}/editProfile">Edit Profile</a>

</body>
</html>