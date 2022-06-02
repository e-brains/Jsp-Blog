<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<!-- <script src="https://code.jquery.com/jquery-3.6.0.js"
	integrity="sha256-H+K7U5CnXl1h5ywQfKtSj8PCmoN9aaq30gDh27Xc0jk="
	crossorigin="anonymous"></script> -->
</head>
<body>
	<button onclick="getAjax()">getAjax() 호출</button>

	<script>
	function getAjax(){
		// get으로 key=value 데이터를 전송하고 응답을 text/plain으로 받을 예정
		$.ajax({
			type: "GET",  // default값이 get이라서 생략 가능함
			url: " http://localhost:8090/blog/ajax1?username=김갑동&password=1111",
			// data: get은 전송할 http body가 없어서 data 필드가 필요 없음
			// contentType: 전송할 data가 없으니 이를 설명할 컨텐트 타입 필드도 필요 없음
			dataType: "text"   // 응답 데이터를  파싱하는 것이 목적, 주고받는 데이터가 텍스트이면 생략가능 
		}) 
		.done(function(data){  // ajax 통신 완료 후 정상이면 done호출
			alert(data);
		}) 
		.fail(function(error){  // ajax 통신 완료 후 비정상이면 fail호출
			
		});   
	}
	
	</script>

</body>
</html>