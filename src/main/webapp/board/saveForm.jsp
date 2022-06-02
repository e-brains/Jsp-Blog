<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../layout/ly_header.jsp"%>


<!-- 해당 페이지로 직접 URL(파일.확장자)을 쳐서 접근을 하게 되면 또 파일 마다 
내부에서 세션 체크를 갹각 해서 막아야함. -->
<!-- 해결방법 : 공통으로 필터에 .jsp로 접근하는 모든 접근을 막아버리면 됨. -->

<div class="container">
	<form action="/blog/board?cmd=save" method="POST">
		<input type="hidden" name="userId"  
			value="${sessionScope.principal.id}" />
		<div class="form-group">
			<label for="title">Title:</label> <input type="text"
				class="form-control" placeholder="title" id="title" name="title">
		</div>

		<div class="form-group">
			<label for="content">Content:</label>
			<textarea id="summernote" class="form-control" rows="5" id="content"
				name="content"></textarea>
		</div>

		<button type="submit" class="btn btn-primary">글쓰기 등록</button>
	</form>
</div>

<script>
	// ready는 document가 다 그려져서 준비가 되면 이라는 뜻
	// 스크립트를 아래에 적을거면 필요없다.
	$(document).ready(function() {

		// summernote id를 찾아서 summernote() 함수를 실행하라는 의미
		$('#summernote').summernote({
			placeholder : '글을 쓰세요.',
			tabsize : 2,
			height : 300
		});
	});
</script>
</body>
</html>