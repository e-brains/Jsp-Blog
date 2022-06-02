// 서버에서 댓글이 저장되었으니 서버에서 내려 받은 저장에 성공한
// 데이터를 다시 받아서 화면상에서도 추가해 준다.
// 그래서 화면을 다시 읽을 필요가 없다. 
function addReply(data){
	
	var replyItem = `<li id="reply-${data.id}" class="media">`;
	replyItem += `<div class="media-body">`;
	replyItem += `<strong class="text-primary">${data.userId}</strong>`;
	replyItem += `<p>${data.content}.</p></div>`;
	replyItem += `<div class="m-2">`;
	
	replyItem += `<i onclick="deleteReply(${data.id})" class="material-icons">delete</i></div></li>`;
	
	$("#reply__list").prepend(replyItem);  // prepend : 앞에 붙이다. 기존 리스트 상단에 붙이기를 해준다.
}

/*댓글 삭제 요청*/
function deleteReply(id){
	// 세션의 유저의 id와 reply의 userId를 비교해서 같을때만!!
	$.ajax({
		type : "post",
		url : "/blog/reply?cmd=delete&id="+id,
		dataType : "json"
	}).done(function(result) { //  { "statusCode" : 1 }
		if (result.statusCode == 1) {
			console.log(result);
			$("#reply-"+id).remove();  // 서버에서 삭제에 성공했으니  화면상에서도 삭제한다.
		} else {
			alert("댓글삭제 실패");
		}
	});
}

/*댓글 달기 저장*/
function replySave(userId, boardId) {

	var data = {
		userId : userId,
		boardId : boardId,
		content : $("#content").val()
	}

	$.ajax({
		type : "post",
		url : "/blog/reply?cmd=save",
		data : JSON.stringify(data),
		contentType : "application/json; charset=utf-8",
		dataType : "json"
	}).done(function(result) {
		if (result.statusCode == 1) {  // 성공했으면
			console.log(result);
			// 서버에서 댓글 달기 성공 했으니 화면을 다시 불러 오는 것이 아니라 
			// 서버에서 데이터먼 가지고 와서 ui 에서 자체적으로 추가한다. 
			addReply(result.data); // 댓글 리스트에 추가하는 함수
			$("#content").val("");
			//location.reload();
		} else {
			alert("댓글쓰기 실패");
		}
	});
}

/*게시글 삭제 요청*/
function deleteById(boardId){
// delete와 get은 body가 필요없다.
// body를 사용하려면 json으로 만들기 위해 데이터 오브젝트를 만들고
// JSON.stringify(data) -> json으로 변환해서 data: 에 넣으면 됨
	$.ajax({
		type: "post",  // get으로 하면 스크립트 공격 당할 수 있으므로  post로 한다.
		url: "/blog/board?cmd=delete&id="+boardId,
		dataType: "json"
	}).done(function(result){
		console.log(result);
		if(result.statusCode == 1){
			location.href="index.jsp";
		}else{
			alert("삭제에 실패하였습니다.");
		}
	});
}