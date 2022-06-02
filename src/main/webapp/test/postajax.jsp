<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<!-- <script src="https://code.jquery.com/jquery-3.6.0.js"
	integrity="sha256-H+K7U5CnXl1h5ywQfKtSj8PCmoN9aaq30gDh27Xc0jk="
	crossorigin="anonymous">	
</script> -->
</head>
<body>
	<button onclick="postAjax()">postAjax() 호출</button>

	<script>
		function postAjax() {
			// post로 key=value 데이터를 전송하고 응답을 json으로 받을 예정
			$.ajax({
				type : "POST",
				url : " http://localhost:8090/blog/ajax1",
				data : "username=김갑동&password=1111",
				contentType : "application/x-www-form-urlencoded",
			//dataType: "text"     // json을 그대로 문자열로 찍는다.
			//dataType: "json"     // json을 파싱하여 자바스크립트 오브젝트로 변환한다.
			// 서버에서 response.setContentType("application/json");을 통해 컨텐트 타입을 정해주면
			// 클라이언트에서 dataType을 생략해도 json으로 자동 파싱 된다.
			}).done(function(data) { // ajax 통신 완료 후 정상이면 done호출
				alert(data);
				alert(data.username); // dataType을 json으로 받으면 접근자로 접근이 가능하다.
				alert(data.password);
			}).fail(function(error) { // ajax 통신 완료 후 비정상이면 fail호출

			});
		}
	</script>

</body>
</html>