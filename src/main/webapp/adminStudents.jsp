<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>All Students — College Student Portal</title>
</head>

<body>

<h1>All Students</h1>

<a href="${pageContext.request.contextPath}/dashboard.jsp">
    Back to Dashboard
</a>

<br><br>

<!-- Student Search -->
<form method="get"
      action="${pageContext.request.contextPath}/admin/students">

    <input
        type="text"
        name="q"
        placeholder="Search name, roll number, or department"
        value="${keyword}"
    />

    <button type="submit">Search</button>

    <c:if test="${not empty keyword}">
        <a href="${pageContext.request.contextPath}/admin/students">
            Clear
        </a>
    </c:if>

</form>

<br>

<!-- Determine the next sorting direction -->
<c:set var="nextDir"
       value="${sortDir == 'asc' ? 'desc' : 'asc'}" />

<table border="1" cellpadding="6" cellspacing="0">

    <tr>

        <!-- Student ID -->
        <th>ID</th>

        <!-- Name -->
        <th>
            <a href="${pageContext.request.contextPath}/admin/students?q=${keyword}&sortBy=first_name&sortDir=${sortBy == 'first_name' ? nextDir : 'asc'}">
                Name
            </a>
        </th>

        <!-- Roll Number -->
        <th>
            <a href="${pageContext.request.contextPath}/admin/students?q=${keyword}&sortBy=roll_number&sortDir=${sortBy == 'roll_number' ? nextDir : 'asc'}">
                Roll Number
            </a>
        </th>

        <!-- Department -->
        <th>
            <a href="${pageContext.request.contextPath}/admin/students?q=${keyword}&sortBy=department&sortDir=${sortBy == 'department' ? nextDir : 'asc'}">
                Department
            </a>
        </th>

        <!-- Course -->
        <th>
            Course
        </th>

        <!-- Semester -->
        <th>
            <a href="${pageContext.request.contextPath}/admin/students?q=${keyword}&sortBy=semester&sortDir=${sortBy == 'semester' ? nextDir : 'asc'}">
                Semester
            </a>
        </th>

        <!-- Phone -->
        <th>
            Phone
        </th>

        <!-- Actions -->
        <th>
            Actions
        </th>

    </tr>

    <!-- Student Rows -->
    <c:forEach var="s" items="${students}">

        <tr>

            <!-- ID -->
            <td>
                ${s.studentId}
            </td>

            <!-- Name -->
            <td>
                ${s.firstName} ${s.lastName}
            </td>

            <!-- Roll Number -->
            <td>
                <c:choose>
                    <c:when test="${empty s.rollNumber}">
                        Not set
                    </c:when>
                    <c:otherwise>
                        ${s.rollNumber}
                    </c:otherwise>
                </c:choose>
            </td>

            <!-- Department -->
            <td>
                <c:choose>
                    <c:when test="${empty s.department}">
                        Not set
                    </c:when>
                    <c:otherwise>
                        ${s.department}
                    </c:otherwise>
                </c:choose>
            </td>

            <!-- Course -->
            <td>
                <c:choose>
                    <c:when test="${empty s.course}">
                        Not set
                    </c:when>
                    <c:otherwise>
                        ${s.course}
                    </c:otherwise>
                </c:choose>
            </td>

            <!-- Semester -->
            <td>
                <c:choose>
                    <c:when test="${empty s.semester}">
                        Not set
                    </c:when>
                    <c:otherwise>
                        ${s.semester}
                    </c:otherwise>
                </c:choose>
            </td>

            <!-- Phone -->
            <td>
                <c:choose>
                    <c:when test="${empty s.phone}">
                        Not set
                    </c:when>
                    <c:otherwise>
                        ${s.phone}
                    </c:otherwise>
                </c:choose>
            </td>

            <!-- Actions -->
            <td>

                <!-- Edit -->
                <a href="${pageContext.request.contextPath}/admin/editStudent?id=${s.studentId}">
                    Edit
                </a>

                &nbsp;

                <!-- Delete -->
                <form
                    method="post"
                    action="${pageContext.request.contextPath}/admin/deleteStudent"
                    style="display:inline;"
                    onsubmit="return confirm('Delete this student record? This cannot be undone.');">

                    <input
                        type="hidden"
                        name="id"
                        value="${s.studentId}"
                    />

                    <button type="submit">
                        Delete
                    </button>

                </form>

            </td>

        </tr>

    </c:forEach>

</table>

<!-- Pagination -->
<p>

    <c:if test="${currentPage > 1}">
        <a href="${pageContext.request.contextPath}/admin/students?q=${keyword}&sortBy=${sortBy}&sortDir=${sortDir}&page=${currentPage - 1}">
            Previous
        </a>
    </c:if>

    Page ${currentPage} of ${totalPages}

    <c:if test="${currentPage < totalPages}">
        <a href="${pageContext.request.contextPath}/admin/students?q=${keyword}&sortBy=${sortBy}&sortDir=${sortDir}&page=${currentPage + 1}">
            Next
        </a>
    </c:if>

</p>

</body>
</html>