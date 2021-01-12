<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp"%>

<div class="container" style="padding-top: 50px">
	<form action="/blogTest/user?cmd=join" method="post">

		<div class="form-group" style="padding-top: 10px">
			<input type="text" class="form-control" placeholder="Enter Username"
				name="username">
		</div>

		<div class="form-group" style="padding-top: 10px">
			<input type="password" class="form-control"
				placeholder="Enter Password" name="password">
		</div>

		<div class="form-group" style="padding-top: 10px">
			<input type="email" class="form-control" placeholder="Enter Email"
				name="email">
		</div>
		<br/>
		<button type="submit" class="btn btn-primary">회원가입</button>
	</form>
</div>


</body>
</html>