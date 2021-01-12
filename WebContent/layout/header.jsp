<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<head>
<title>Blog Test</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>

<body>

	<nav class="navbar navbar-expand-sm bg-dark navbar-dark">
		<a class="navbar-brand" href="/blogTest/index.jsp">TESTBLOG</a>
		<ul class="navbar-nav">
			<c:choose>
				<c:when test="${sessionScope.principal eq null }">
					<li class="nav-item"><a class="nav-link"
						href="/blogTest/user?cmd=joinForm">회원가입</a>
					</li>
					<li class="nav-item"><a class="nav-link"
						href="/blogTest/user?cmd=loginForm">로그인</a>
					</li>
				</c:when>
				<c:otherwise>
					<li class="nav-item"><a class="nav-link"
						href="/blogTest/user?cmd=userListForm">회원정보</a>
					</li>
					<li class="nav-item"><a class="nav-link"
						href="/blogTest/user?cmd=logout">로그아웃</a>
					</li>
				</c:otherwise>
			</c:choose>


		</ul>
	</nav>