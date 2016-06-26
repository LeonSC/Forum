<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<%@ include file="static/header.jsp" %>
<body>
<%@ include file="static/titleline.jsp" %>
<div class="container">
	<c:forEach items="${forumCache.forumTitle['root'].subForumTitle}" varStatus="i" var="sft" >
	<!-- block line loop -->
	<div class="row">
		<div class="col-xs-12" style="min-height:4.5em"><h3>${sft.name}</h3></div>
	</div>
	<div class="row">
	<c:forEach items="${sft.subForumTitle}" varStatus="ii" var="sftsub" >
	<!-- sub block line loop -->	
	<div class="col-xs-6">
		<div class="panel panel-default">
			<div class="panel-body">
				<h3><a href="${config.rootPath}/block/${sftsub.BM_ID}" class="alink alinkblack">${sftsub.name}</a></h3>
				<hr/>
				<c:forEach var="item" items="${sftsub.manager}" varStatus="status">
					<small>${item.value.nickname}</small> <c:if test="${not status.last}">/</c:if>
				</c:forEach>
				<c:if test="${empty sftsub.manager}"><small>-</small></c:if>
			</div>
		</div>
	</div>
	<!-- sub block line loop done -->
	</c:forEach>
	</div>
	<!-- block line loop end -->
	</c:forEach>
</div>
<%@ include file="static/footer.jsp" %>
</body>
</html>