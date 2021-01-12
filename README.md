# BlogTest

## 목차
 
1.[환경](#환경)

2.[MySQL](#MySQL)

3.[.xml](#xml-file)

4.[.jsp](#jsp-file)

5.[.java](#java-file)

## 환경

- windows
- jdk 1.8
- tomcat 9.0
- sts tool
- mysql 8.0
- lombok
- gson(json파싱)
- jstl 1.2
- 인코딩 utf-8


## MySQL

### MySQL 데이터 베이스 생성 및 사용자 생성

```sql
CREATE USER 'testuser' @'%' IDENTIFIED BY 'bitc5600';
GRANT ALL PRIVILEGES ON *.* TO 'testuser' @'%';
CREATE DATABASE test;
```

### MySQL 테이블 생성

```sql
CREATE TABLE user(
	id int primary key auto_increment,
    username varchar(20) not null unique,
    password varchar(20) not null,
    email varchar(50) not null unique,
    role varchar(5)
);
```

------------

## xml File

### web.xml

```xml
<description>MySQL Test App</description>
	<resource-ref>
		<description>DB Connection</description>
		<res-ref-name>jdbc/TestDB</res-ref-name>
		<res-type>javax.sql.DataSource</res-type>
		<res-auth>Container</res-auth>
	</resource-ref>
	
	<filter>
		<filter-name>charConfig</filter-name>
		<filter-class>com.cos.blogtest.config.CharConfig</filter-class>
	</filter>

	<filter-mapping>
		<filter-name>charConfig</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>
```

### context.xml

```xml
<Resource name="jdbc/TestDB" auth="Container"
		type="javax.sql.DataSource" maxTotal="100" maxIdle="30"
		maxWaitMillis="10000" username="testuser" password="bitc5600"
		driverClassName="com.mysql.cj.jdbc.Driver"
		url="jdbc:mysql://localhost:3306/test?serverTimezone=Asia/Seoul" />
```

------------

## jsp File

### index.jsp

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	RequestDispatcher dis = request.getRequestDispatcher("/board/list.jsp");
	dis.forward(request, response);
%>
```

### header.jsp

```jsp
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
```

### list.jsp

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp"%>


</body>
</html>
```

### join.jsp

```jsp
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
```

### success.jsp

```jsp
<div class="container" style="margin: 50px">
	<div class="card">
		<div class="card-body">
			<h4 class="card-title d-flex justify-content-center">회원가입 성공~</h4>
			<p class="card-text d-flex justify-content-center">저희 블로그에 회원가입을
				해주셔서 감사합니다.</p>
			<a href="/blogTest/user?cmd=loginForm"
				class="btn btn-primary d-flex justify-content-center">로그인 하러가기</a>
		</div>
	</div>
</div>
```

### login.jsp

```jsp
<div class="container" style="padding-top: 50px">
	<form action="/blogTest/user?cmd=login" method="post">

		<div class="form-group" style="padding-top: 10px">
			<input type="text" class="form-control" placeholder="Enter Username"
				name="username">
		</div>

		<div class="form-group" style="padding-top: 10px">
			<input type="password" class="form-control"
				placeholder="Enter Password" name="password">
		</div>

		<br />
		<button type="submit" class="btn btn-primary">로그인</button>
	</form>
</div>
```

### userList.jsp

```jsp
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
```

------------

## java File

### DB.java

```java
public class DB {
	public static Connection getConnection() {
		try {
			Context initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource)envContext.lookup("jdbc/TestDB");
			Connection conn = ds.getConnection();
			System.out.println("DB 연결 성공");
			return conn;
		} catch (Exception e) {
			System.out.println("DB 연결 실패");
			e.printStackTrace();
		}
		return null;
	}
	
	public static void close(Connection conn, PreparedStatement pstmt) {
		try {
			conn.close();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
		try {
			conn.close();
			pstmt.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
```

### CharConfig.java

```java
public class CharConfig implements Filter{
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)resp;
		
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		
		chain.doFilter(request, response);
	}
}
```

### User.java

```java
public class User {
	private int id;
	private String username;
	private String password;
	private String email;
	private String role; // 회원가입일 경우 user 로 고정
}
```

### DeleteReqDto.java

```java
public class DeleteReqDto {
	private int id;
	private String role;
}
```

### UserLoginDto.java

```java
public class UserLoginDto {
	private String username;
	private String password;
}
```

### UserJoinDto.java

```java
public class UserJoinDto {
	private String username;
	private String password;
	private String email;
}
```

### UserDao.java

#### save
```java
public int save(UserJoinDto dto) {
		StringBuffer sb = new StringBuffer();
		sb.append("INSERT INTO user(username, password, email, role) ");
		sb.append("VALUE (?, ? ,? , \"user\")");
		String sql = sb.toString();
		
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getUsername());
			pstmt.setString(2, dto.getPassword());
			pstmt.setString(3, dto.getEmail());
			
			int result = pstmt.executeUpdate();
			
			return result;
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.close(conn, pstmt);
		}
		return -1;
	}
```

#### findByUnAndPsw
```	
	public User findByUnAndPsw(UserLoginDto dto) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT id, username, password, email, role ");
		sb.append("FROM user WHERE username = ? AND password = ?");
		
		String sql = sb.toString();
		
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		User user;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getUsername());
			pstmt.setString(2, dto.getPassword());
			
			rs = pstmt.executeQuery();
			
			
			if(rs.next()) {
				user = User.builder()
						.id(rs.getInt("id"))
						.username(rs.getString("username"))
						.password(rs.getString("password"))
						.email(rs.getString("email"))
						.role(rs.getString("role"))
						.build();
				
				return user;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.close(conn, pstmt, rs);
		}
		return null;
	}
```

#### findAll
```	
	public List<User> findAll(){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT id, username, password, email, role ");
		sb.append("FROM user");
		
		String sql = sb.toString();
		
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<User> users = new ArrayList<>();
		User user;
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				user = User.builder()
						.id(rs.getInt("id"))
						.username(rs.getString("username"))
						.password(rs.getString("password"))
						.email(rs.getString("email"))
						.role(rs.getString("role"))
						.build();
				
				users.add(user);
			}
			
			return users;
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.close(conn, pstmt, rs);
		}
		return null;
	}
```

#### deleteById
```
	public int deleteById(DeleteReqDto dto) {
		
		String sql = "DELETE FROM user WHERE id = ?";
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getId());
			
			int result = pstmt.executeUpdate();
			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.close(conn, pstmt);
		}
		return -1;
	}
```

### UserController.java

####
```java
@WebServlet("/user")
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public UserController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doProcess(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doProcess(request, response);
	}

	protected void doProcess(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String cmd = request.getParameter("cmd");
		UserDao userDao = new UserDao();
````

#### /user?cmd=joinForm
```java
		if (cmd.equals("joinForm")) {
			RequestDispatcher dis = request.getRequestDispatcher("/user/join.jsp");
			dis.forward(request, response);
		}
````

#### /user?cmd=loginForm
```java
		else if (cmd.equals("loginForm")) {
			RequestDispatcher dis = request.getRequestDispatcher("/user/login.jsp");
			dis.forward(request, response);
		}
````

#### /user?cmd=userListForm
```java
		else if (cmd.equals("userListForm")) {

			List<User> users = userDao.findAll();
			request.setAttribute("users", users);

			RequestDispatcher dis = request.getRequestDispatcher("/user/userList.jsp");
			dis.forward(request, response);

		}
````

#### /user?cmd=join
```java
		else if (cmd.equals("join")) {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String email = request.getParameter("email");

			UserJoinDto userJoinDto = UserJoinDto.builder().username(username).password(password).email(email).build();
			int result = userDao.save(userJoinDto);

			if (result == 1) {
				response.sendRedirect("/blogTest/board/success.jsp");
			} else {
				Script.back(response, "회원가입이 정상적으로 처리되지 못했습니다. 다시 시도해 주세요.");
			}
		}
````

#### /user?cmd=login
```java
		else if (cmd.equals("login")) {
			String username = request.getParameter("username");
			String password = request.getParameter("password");

			UserLoginDto userLoginDto = new UserLoginDto();
			userLoginDto.setUsername(username);
			userLoginDto.setPassword(password);

			User userEntity = userDao.findByUnAndPsw(userLoginDto);

			if (userEntity != null) {
				HttpSession session = request.getSession();
				session.setAttribute("principal", userEntity);
				response.sendRedirect("index.jsp");
			} else {
				Script.back(response, "아이디 또는 비밀번호가 잘못되었습니다. 확인 후 다시 시도해주십시오");
			}
		}
````

#### /user?cmd=delete
```java
		else if (cmd.equals("delete")) {
			BufferedReader br = request.getReader();

			String data = br.readLine();
			System.out.println(data);

			Gson gson = new Gson();

			DeleteReqDto dto = gson.fromJson(data, DeleteReqDto.class);

			int result = userDao.deleteById(dto);

			
			if (dto.getRole().equals("admin")) {
				
			} else if (dto.getRole().equals("user")) {
				HttpSession session = request.getSession();
				session.invalidate();
			}
			
			PrintWriter out = response.getWriter();
			out.print(result);
			out.flush();

		}
````

#### /user?cmd=logout
```java
		else if (cmd.equals("logout")) {
			HttpSession session = request.getSession();
			session.invalidate();
			response.sendRedirect("index.jsp");
		}

	}
}

```

### Script.java

```java
public class Script {
	public static void back(HttpServletResponse response, String msg) {
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print("<script>");
			out.print("alert('"+msg+"');");
			out.print("history.back();");
			out.print("</script>");
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
```
