<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ include file="../layout/ly_header.jsp" %>

<div class="container">
	<!-- login은 select이므로 get으로 해야 하지만 편의상 post로 한다. -->
	<form action="/blog/user?cmd=login" method="post"  >
		
		<!-- required를 사용하여 값을 반드시 입력하도록 강제한다. -->
		<div class="form-group">
			<input type="text" name="username" id="username" class="form-control"
				placeholder="Enter username" required="required">
		</div>

		<div class="form-group">
			<input type="password" name="password" id="password" class="form-control"
				placeholder="Enter password" required="required">
		</div>

		<br>
		<button type="submit" class="btn btn-primary">로그인 완료</button>

	</form>

</div>

<script>
	
</script>


</body>
</html>