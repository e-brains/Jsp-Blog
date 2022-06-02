<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../layout/ly_header.jsp"%>


<div class="container">

	<!-- 검색영역 -->
	<div class="m-2">		
		<form class="form-inline d-flex justify-content-end"	action="/blog/board">			
			<input type="hidden" name="cmd" value="search" /> <!-- cmd = 검색 -->
			<input type="hidden" name="page" value="0" /> <!-- 페이지 = 0 페이지 -->
			<input type="text" name="keyword" class="form-control mr-sm-2" placeholder="검색할 단어를 입력해 주세요">
			<button class="btn btn-primary m-1">검색</button>
		</form>
	</div>

	<!-- 조회량에 따른 막대바를 동적으로 그리기 -->
	<div class="progress col-md-12 m-2">		
		<div class="progress-bar"	style="width: ${requestScope.currentPosition}%"></div>
	</div>

	<!-- JSTL foreach문을 써서 보여준다. el표현식과 함께  -->
	<!-- sessionScope만 표현하고 requestScope는 표현하지 않고 boards (변수)만 표현해도 
		sessionScope와 구분이 되기 때문에 생략해도 되지만 그냥 다 표현하자. -->
	<c:forEach var="board" items="${requestScope.boards }">

		<div class="card col-md-12 m-2">
			<div class="card-body">
				<h4 class="card-title">${board.title}</h4>
				<!--  세션을 체크하여 로그인한 경우만 조회가 되도록 해야 한다. -->			
				<c:choose>
					<c:when test="${sessionScope.principal.username != null}">
						<a href="/blog/board?cmd=detail&id=${board.id}"	class="btn btn-primary">상세보기</a>		
					</c:when>
					<c:otherwise>						
						<a href="#" class="btn btn-primary">상세보기</a> 		
					</c:otherwise>
				</c:choose>
				
			</div>
		</div>

	</c:forEach>

	<!-- <<<<<<<<  페이징 구현  >>>>>>>> -->
	<!--  EL표현식 중 param을 이용하여 queryString을 읽을 수 있다. -->
	<!--   param.cmd -> list  param.page -> 현재 페이지 값 -->
	<br />
	<ul class="pagination justify-content-center">
	
		<!-- 검색 키워드로 검색했을때 페이징 처리 -->
	<c:choose>
			<c:when test="${empty param.keyword}"> <!-- 검색키워드가 없으면 일반 목록 페이징 -->
				<c:set var ="pagePrev" value="/blog/board?cmd=list&page=${param.page-1 }"/>
				<c:set var="pageNext" value="/blog/board?cmd=list&page=${param.page+1}"/>
			</c:when>
			<c:otherwise> <!-- 검색키워드가 있으면 글검색 페이징 -->
				<c:set var="pagePrev" value="/blog/board?cmd=search&page=${param.page-1}&keyword=${param.keyword}"/>
				<c:set var="pageNext" value="/blog/board?cmd=search&page=${param.page+1}&keyword=${param.keyword}"/>
			</c:otherwise>
		</c:choose>	
	
		<!-- 첫번째 페이지는 무조건 disabled -->
		<c:choose>		
			<c:when test="${param.page == 0}">				
				<li class="page-item disabled">
				<a class="page-link" href="#">Previous</a></li>
			</c:when>
			<c:otherwise>				
				<li class="page-item">
				<a class="page-link"	href="${pageScope.pagePrev}">Previous</a></li>
			</c:otherwise>
		</c:choose>
		<!-- 마지막 페이지가 현재 페이지면 disabled-->
		<!-- pageScope를 사용하면 현재 페이지에 var로 선언된 변수를 사용할 수 있다. -->
		<c:choose>			
			<c:when test="${requestScope.lastPage == param.page}"> 
				<li class="page-item disabled">
				<a class="page-link" href="#">Next</a></li>
			</c:when>
			<c:otherwise>
				<li class="page-item">				
				<a class="page-link"	href="${pageScope.pageNext}">Next</a></li>
			</c:otherwise>
		</c:choose>

	</ul>
</div>

</body>
</html>


