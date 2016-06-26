<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<%@ include file="../static/header.jsp" %>
<body>
<%@ include file="../static/titleline.jsp" %>
<div class="container">
	<nav class="navbar navbar-default">
		<div class="container">
			<div class="navbar-header">
				<a class="navbar-brand" href="#">回收站</a>
			</div>
			<div class="collapse navbar-collapse">
				<ul class="nav navbar-nav">
					<li><a href="#">删除</a></li>
				</ul>
			</div>
		</div>
	</nav>
	<div class="row">
		<div class="col-xs-12">
			<%@ include file="../placetop.jsp" %>
			<ul class="list-group">
				<c:forEach var="fc" items="${page.list}" varStatus="index">
				<li class="list-group-item">${fc.title}
					<span class="pull-right">[<a href="${config.rootPath}/manage/delview?key=${fc.BM_ID}" class="alink" target="_blank">查看</a>]</span>
					<span class="pull-right">[<a href="${config.rootPath}/manage/setbacktopicfromrecycle?key=${fc.BM_ID}" class="alink">取消回收</a>]</span>
					<span class="pull-right">[<a href="${config.rootPath}/manage/del?topicid=${fc.BM_ID}" class="alink">删除</a>]</span>
				</li>
				</c:forEach>
			</ul>
		</div>
	</div>
</div>
<%@ include file="../static/footer.jsp" %>
</body>
</html>