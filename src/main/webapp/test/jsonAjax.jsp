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
	<button onclick="jsonAjax()">jsonAjax() 호출</button>

	<script>
	
	var username = "깁갑동";
	var password = "1111";
	
	//  자바스크립트 오브렉트 만들기
	var data = {
			username : "깁갑동",
			password : "1111"
	 }
	
	// JSON.stringify() -> 자바스크립트 오브젝트를 json으로 변경
	// JSON.parse() -> json을 자바스크립트 오브젝트로 변경
	// json 오브젝트로 만들기
	var jsonData = JSON.stringify(data);	
	
	function jsonAjax(){
		// post로 json 데이터를 전송하고 응답을 json으로 받을 예정
		$.ajax({
			type: "POST",  
			url: " http://localhost:8090/blog/ajax1",
			data: jsonData,
			contentType: "application/json",
			dataType: "json"     	
		}) 
		.done(function(data){  // ajax 통신 완료 후 정상이면 done호출
			alert(data);
			alert(data.username); // dataType을 json으로 받으면 접근자로 접근이 가능하다.
			alert(data.password);
		}) 
		.fail(function(error){  // ajax 통신 완료 후 비정상이면 fail호출
			
		});   
	}
	
	</script>

</body>
</html>