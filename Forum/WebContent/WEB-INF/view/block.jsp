<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<%@ include file="static/header.jsp" %>
<body>
<%@ include file="static/titleline.jsp" %>
<c:if test="${not empty forumCache.forumTitle[ft.BM_ID].background.src}">
<img src="http://${config.domainBucket}/${forumCache.forumTitle[ft.BM_ID].background.src}" style="position:absolute;width:100%;top:0px;filter:alpha(opacity=15);-moz-opacity:0.15;opacity: 0.15;"/>
</c:if>
<div class="container">
	<nav class="navbar navbar-default">
		<div class="container">
			<div class="navbar-header">
				<c:if test="${not empty ft.icon}">
				<a class="navbar-brand" href="${config.rootPath}/block/${ft.BM_ID}"><img src="http://${config.domainBucket}/${ft.icon}" class="img-rounded img-responsive" style="width:1.5em"></a>
				</c:if>
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