<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../layout/ly_header.jsp"%>

<div class="container">

	<!-- 로그인 한 사람과 글 쓴 사람이 동일인 이면  수정 , 삭제 버튼을 달아 준다. -->
	<c:if test="${sessionScope.principal.id == requestScope.dto.userId}">
		<a href="/blog/board?cmd=updateForm&id=${requestScope.dto.id}"
			class="btn btn-warning">수정</a>			
		<button onClick="deleteById(${requestScope.dto.id})"
			class="btn btn-danger">삭제</button>
	</c:if>

	<br /> <br />
	<h6 class="m-2">
		작성자 : <i>${requestScope.dto.username}</i> &nbsp; &nbsp; 조회수 : <i>${requestScope.dto.readCount}</i>
	</h6>
	<br />
	<h3 class="m-2">
		<b>${requestScope.dto.title}</b>
	</h3>
	<hr />
	<div class="form-group">
		<div class="m-2">${requestScope.dto.content}</div>
	</div>

	<hr />

	<!-- 댓글 박스 -->
	<div class="row bootstrap snippets">
		<div class="col-md-12">
			<div class="comment-wrapper">
				<div class="panel panel-info">
					<div class="panel-heading m-2">
						<b>Comment</b>
					</div>
					<div class="panel-body">
						<input type="hidden" name="userId"	value="${sessionScope.principal.id}" /> 
						<input type="hidden" name="boardId" value="${requestScope.dto.id}" />
						
						<!-- 댓글 content -->
						<textarea id="content" id="reply__write__form"
							class="form-control" rows="2"></textarea>
						
						<br>

						<button
							onClick="replySave(${sessionScope.principal.id}, ${requestScope.dto.id})"
							class="btn btn-primary pull-right">댓글쓰기</button>

						<div class="clearfix"></div>
						<hr />

						<!-- 댓글 쓰기가 성공하면 댓글 리스트에 화면 refresh없이 바로바로 댓글 적은 결과가 리스트로  -->
						<!-- display되도록 스크립트에서 동적으로 html을 바꿔준다. -->
						<!--  boardDetail.js의 function addReply(data){} 에서	수행  -->
						<ul id="reply__list" class="media-list">		
							<c:forEach var="reply" items="${requestScope.replys}">
								<!-- 댓글 아이템  addReply () 함수의 작업 대상 영역 -->
								<li id="reply-${reply.id}" class="media">
									<div class="media-body">
										<strong class="text-primary">${reply.userId}</strong>
										<p>${reply.content}</p>
									</div>
									<div class="m-2">
										<!-- 로그인 유저와 댓글 단 유저가 같은때만 삭제버튼이 보이도록 한다. -->
										<c:if test="${sessionScope.principal.id == reply.userId }">
											<i onclick="deleteReply(${reply.id})" class="material-icons">delete</i>
										</c:if>
									</div>
								</li>
							</c:forEach>

						</ul>
						<!-- 댓글 리스트 끝-->
					</div>
				</div>
			</div>

		</div>
	</div>
	<!-- 댓글 박스 끝 -->
</div>

<script src="/blog/js/boardDetail.js"></script>
</body>
</html>


