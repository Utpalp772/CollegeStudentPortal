<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>All Students — College Student Portal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/portal.css">
</head>
<body class="portal">

<c:set var="pageName" value="admin" />
<jsp:include page="/navbar.jsp" />

<main class="portal-main">
    <div class="page-header">
        <span class="eyebrow">Administration</span>
        <h1>All Students</h1>
    </div>

    <div class="toolbar">
        <form class="search-form" method="get" action="${pageContext.request.contextPath}/admin/students">
            <input type="text" name="q" placeholder="Search name, roll number, or department" value="${keyword}" />
            <button type="submit" class="btn btn-outline btn-sm">Search</button>
            <c:if test="${not empty keyword}">
                <a href="${pageContext.request.contextPath}/admin/students" class="btn btn-sm" style="color:var(--slate);">Clear</a>
            </c:if>
        </form>
    </div>

    <c:set var="nextDir" value="${sortDir == 'asc' ? 'desc' : 'asc'}" />

    <c:choose>
    <c:when test="${empty students}">
        <div class="table-wrap">
            <div class="state-block">
                <div class="glyph">?</div>
                <p>No students match this search.</p>
                <a href="${pageContext.request.contextPath}/admin/students" class="btn btn-outline btn-sm">Clear search</a>
            </div>
        </div>
    </c:when>
    <c:otherwise>
    <div class="table-wrap">
        <table class="data-table">
            <thead>
            <tr>
                <th>Photo</th>
                <th>ID</th>
                <th><a href="${pageContext.request.contextPath}/admin/students?q=${keyword}&sortBy=first_name&sortDir=${sortBy == 'first_name' ? nextDir : 'asc'}">Name</a></th>
                <th><a href="${pageContext.request.contextPath}/admin/students?q=${keyword}&sortBy=roll_number&sortDir=${sortBy == 'roll_number' ? nextDir : 'asc'}">Roll Number</a></th>
                <th><a href="${pageContext.request.contextPath}/admin/students?q=${keyword}&sortBy=department&sortDir=${sortBy == 'department' ? nextDir : 'asc'}">Department</a></th>
                <th>Course</th>
                <th><a href="${pageContext.request.contextPath}/admin/students?q=${keyword}&sortBy=semester&sortDir=${sortBy == 'semester' ? nextDir : 'asc'}">Semester</a></th>
                <th>Phone</th>
                <th>Role</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="s" items="${students}">
                <tr>
                    <td>
                        <c:choose>
                        <c:when test="${not empty s.profilePhoto}">
                        <img class="thumb" src="${pageContext.request.contextPath}/photo?studentId=${s.studentId}" alt="" />
                        </c:when>
                        <c:otherwise>
                        <img class="thumb" src="${pageContext.request.contextPath}/resources/images/default-avatar.png" alt="" />
                        </c:otherwise>
                        </c:choose>
                    </td>
                    <td class="mono">${s.studentId}</td>
                    <td>${s.firstName} ${s.lastName}</td>
                    <td class="mono">
                        <c:choose>
                            <c:when test="${empty s.rollNumber}"><span class="muted">Not set</span></c:when>
                            <c:otherwise>${s.rollNumber}</c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${empty s.department}"><span class="muted">Not set</span></c:when>
                            <c:otherwise>${s.department}</c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${empty s.course}"><span class="muted">Not set</span></c:when>
                            <c:otherwise>${s.course}</c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${empty s.semester}"><span class="muted">Not set</span></c:when>
                            <c:otherwise>${s.semester}</c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${empty s.phone}"><span class="muted">Not set</span></c:when>
                            <c:otherwise>${s.phone}</c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <span class="badge ${s.role == 'ADMIN' ? 'badge-admin' : 'badge-student'}">${s.role}</span>
                        <form method="post" action="${pageContext.request.contextPath}/admin/changeRole" style="display:inline; margin-left:6px;">
                            <input type="hidden" name="userId" value="${s.userId}" />
                            <c:choose>
                            <c:when test="${s.role == 'ADMIN'}">
                            <input type="hidden" name="newRole" value="STUDENT" />
                            <button type="submit" class="btn btn-outline btn-sm" onclick="return confirm('Demote this user to STUDENT?');">Demote</button>
                            </c:when>
                            <c:otherwise>
                            <input type="hidden" name="newRole" value="ADMIN" />
                            <button type="submit" class="btn btn-outline btn-sm" onclick="return confirm('Promote this user to ADMIN?');">Promote</button>
                            </c:otherwise>
                            </c:choose>
                        </form>
                    </td>
                    <td>
                        <div class="actions-cell">
                            <a href="${pageContext.request.contextPath}/admin/editStudent?id=${s.studentId}" class="btn btn-outline btn-sm">Edit</a>
                            <form method="post" action="${pageContext.request.contextPath}/admin/deleteStudent"
                                  onsubmit="return confirm('Delete this student record? This cannot be undone.');">
                                <input type="hidden" name="id" value="${s.studentId}" />
                                <button type="submit" class="btn btn-danger btn-sm">Delete</button>
                            </form>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    </c:otherwise>
    </c:choose>

    <div class="pagination">
        <c:if test="${currentPage > 1}">
            <a href="${pageContext.request.contextPath}/admin/students?q=${keyword}&sortBy=${sortBy}&sortDir=${sortDir}&page=${currentPage - 1}">&larr; Previous</a>
        </c:if>
        Page ${currentPage} of ${totalPages}
        <c:if test="${currentPage < totalPages}">
            <a href="${pageContext.request.contextPath}/admin/students?q=${keyword}&sortBy=${sortBy}&sortDir=${sortDir}&page=${currentPage + 1}">Next &rarr;</a>
        </c:if>
    </div>
</main>

</body>
</html>