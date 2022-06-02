
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<%@ include file="../layout/ly_header.jsp"%>


<div class="container">
	<!-- onsubmit="return valid()" 속성을 걸어 주면 버튼이 눌려 질때 반드시 여기 함수를 실행한다. -->
	<form action="/blog/user?cmd=join" method="post" onsubmit="return valid()" >

		<!-- id 중복 체크 -->
		<div class="d-flex justify-content-end">
			<button type="button" class="btn btn-info" onClick="idCheck()">중복체크</button>
		</div>
		
		<!-- required를 사용하여 값을 반드시 입력하도록 강제한다. -->
		<div class="form-group">
			<input type="text" name="username" id="username" class="form-control"
				placeholder="Enter username" required="required">
		</div>

		<div class="form-group">
			<input type="password" name="password" class="form-control"
				placeholder="Enter password" required="required">
		</div>

		<div class="form-group">
			<input type="email" name="email" class="form-control"
				placeholder="Enter email" required="required">
		</div>
		
		<!-- 주소 검색 버튼 -->
		<div class="d-flex justify-content-end">		
			<!-- 타입을 버튼으로 설정해야 submit이 안됨 -->
			<button type="button" onclick="goPopup();" class="btn btn-info">주소검색</button>
		</div>

		<div class="form-group">
		<!-- 주소 검색으로 입력해야 하므로 readonly 로 설정 -->
			<input type="text" name="address" id="address" class="form-control"
				placeholder="Enter address" required="required" readonly="readonly"> 
		</div>

		<br>
		<button type="submit" class="btn btn-primary">회원가입완료</button>

	</form>

</div>

<script>

	// id 중복 체크가 되어 있는 지 마지막 submit 시 체킹하기 위한 flag
	var isChecking = false; 

	function valid() {
		if(isChecking == false){
			alert("id 중복 체크를 해주세요");
		}
		return isChecking;
	}
	
	/* id 중복체크 */
	function idCheck() {
		// 서버에서 id 중복 체크 결과를 json을 받는다.
		// ajax 통신을 한다.
		// 자바스크립트에서는 중괄호{}를 이용하여 객체를 만든다. 중괄호{}로 쌓여 있으면 객체로 인식한다.
		// document.querySelector(#username)을 jquery 문법으로 바꾸면 아래와 같다.
		var username = $("#username").val();   
		
		// id체크는 select해보는 것이기 때문에 get을 사용하지만 체크 결과를 가져와야 하기 때문에 post로 함
		// data가 "username = " + username 키 : 밸류 형태이면 contentType이 x-www-form-urlencoded
		// 받는 dataType이 json이면 자바스크립트 오브젝트로 파싱해 준다.
		$.ajax({
			type: "POST",
			url: "/blog/user?cmd=idCheck",
			data: username,
			contentType: "text/plain; charset=utf-8",
			dataType: "text"  // 응답 받을 데이터의 타입을 적으면 자바스크립트 오브젝트로 파싱해줌.
		}).done(function(data){
			if(data === 'ok'){ // 유저네임 있다는 것
				isChecking = false;
				alert('유저네임이 중복되었습니다.')
			}else{
				isChecking = true;
				$("#username").attr("readonly", "readonly");
				alert("해당 유저네임을 사용할 수 있습니다.")
			}
		});	
	}


	// opener관련 오류가 발생하는 경우 아래 주석을 해지하고, 사용자의 도메인정보를 입력합니다.
   // ("팝업API 호출 소스"도 동일하게 적용시켜야 합니다.)
	//document.domain = "abc.go.kr";

	function goPopup() {
		// 주소검색을 수행할 팝업 페이지를 호출합니다.
		// 호출된 페이지(jusopopup.jsp)에서 실제 주소검색URL(https://www.juso.go.kr/addrlink/addrLinkUrl.do)를 호출하게 됩니다.
		// 그래서 jusopopup.jsp는 잠깐 떳다가 사라짐
		var pop = window.open("/blog/user/jusoPopup.jsp", "pop",
				"width=570,height=420, scrollbars=yes, resizable=yes");
	}

	// jusoPopup.jsp에서 opener를 이용해 이 함수를 호출하여 roadFullAddr넘겨 주면 여기서 받음
	function jusoCallBack(roadFullAddr) {
		var addressEl = document.querySelector("#address");
		addressEl.value = roadFullAddr;
		//document.form.roadFullAddr.value = roadFullAddr;
	}
</script>

</body>

</html>