<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp"%>

<div class="container" style="padding-top: 50px">
	<h2>User List</h2>
	<table class="table table-hover">
		<thead>
			<tr>
				<th>id</th>
				<th>username</th>
				<th>password</th>
				<th>email</th>
				<th>role</th>
				<th>delete</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="user" items="${users}">
				<tr>
					<td>${user.id}</td>
					<td>${user.username}</td>
					<c:choose>
						<c:when test="${sessionScope.principal.role eq 'admin'}">
							<td>${user.password}</td>
						</c:when>
						<c:when test="${sessionScope.principal.id == user.id }">
							<td>${sessionScope.principal.password}</td>
						</c:when>
						<c:otherwise>
							<td>***********</td>
						</c:otherwise>
					</c:choose>
					<td>${user.email}</td>
					<td>${user.role}</td>
					<c:choose>
						<c:when test="${sessionScope.principal.role eq 'admin'}">
							<td>
								<button onClick="deleteById(${user.id}, 'admin')" type="button"
									class="btn btn-danger">정보삭제</button>
							</td>
						</c:when>
						<c:when test="${sessionScope.principal.id == user.id }">
							<td>
								<button onClick="deleteById(${user.id}, 'user')" type="button"
									class="btn btn-danger">정보삭제</button>
							</td>
						</c:when>
					</c:choose>

				</tr>
			</c:forEach>

		</tbody>
	</table>
</div>
<script>

	function deleteById(id, role){
		
		var data ={
			id: id,
			role: role
		};
		
		$.ajax({
			type:"post",
			url:"/blogTest/user?cmd=delete",
			contentType:"application/json; charset=utf-8",
			data:JSON.stringify(data),
			dataType:"json"
		}).done(function(result){
			if(result == 1){
				alert("정보 삭제에 성공 했습니다.");
				location.href = "index.jsp"
			} else {
				alert("정보 삭제에 실패 했습니다.");
			}

		});

	}
</script>

</body>
</html>