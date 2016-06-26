<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<%@ include file="static/header.jsp" %>
<body>
<div class="container">
	<%@ include file="titleline.jsp" %>
	<nav class="navbar navbar-default" style="margin-top:1em">
		<div class="container">
			<div class="navbar-header">
				<a class="navbar-brand" href="${config.rootPath}/block/${ft.BM_ID}">${ft.name}</a>
			</div>
			<div class="collapse navbar-collapse">
				<ul class="nav navbar-nav">
					<li><a href="${config.rootPath}/newtopic/${ft.BM_ID}">新帖</a></li>
					<c:if test="${not empty mem.admin}"><li><a href="${config.rootPath}/manage/recycle">回收站</a></li></c:if>
				</ul>
			</div>
		</div>
	</nav>
	<div class="row">
		<div class="col-xs-12">
			<%@ include file="placetop.jsp" %>
			<ul class="list-group">
				<c:forEach var="fc" items="${page.list}" varStatus="index">
				<li class="list-group-item"><a href="${config.rootPath}/view?key=${fc.BM_ID}" class="alink">${fc.title}</a><span class="badge">${fc.replyCount}</span></li>
				</c:forEach>
			</ul>
		</div>
	</div>
</div>
<%@ include file="static/footer.jsp" %>
</body>
</html>