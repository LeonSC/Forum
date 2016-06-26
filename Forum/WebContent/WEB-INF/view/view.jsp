<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:useBean id="timeValues" class="java.util.Date"/>
<!DOCTYPE html>
<html>
<%@ include file="static/header.jsp" %>
<body>
<%@ include file="static/titleline.jsp" %>
<div class="container">
	<nav class="navbar navbar-default">
		<div class="container">
			<div class="navbar-header">
				<a class="navbar-brand" href="${config.rootPath}/block/${ft.BM_ID}">${ft.name}</a>
			</div>
			<div class="collapse navbar-collapse">
				<ul class="nav navbar-nav">
					<li><a href="${config.rootPath}/reply/${fc.BM_ID}">新回复</a></li>
					<%@ include file="admincontrol.jsp" %>
				</ul>
				<c:if test="${page.totalPages!=0}">
				<ul class="nav navbar-nav pull-right">
					<li><a href="${config.rootPath}/view?key=${fc.BM_ID}&page=${page.nowPage-1}">上一页</a></li>
					<li class="dropdown">
						<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">${page.nowPage} / ${page.totalPages} <span class="caret"></span></a>
						<ul class="dropdown-menu">
							<c:forEach var="thepage" begin="1" end="${page.totalPages}">
							<li><a href="${config.rootPath}/view?key=${fc.BM_ID}&page=${thepage}">${thepage} / ${page.totalPages}</a></li>
							</c:forEach>
						</ul>
					</li>
					<li><a href="${config.rootPath}/view?key=${fc.BM_ID}&page=${page.nowPage+1}">下一页</a></li>
				</ul>
				</c:if>
			</div>
		</div>
	</nav>
	<div class="row">
		<div class="col-xs-10">
			<div class="panel panel-default">
				<div class="panel-heading"><h3 class="panel-title">${fc.title}</h3></div>
				<div class="panel-body">
					${fc.content}
					<c:if test="${not empty fcf.contentList}">
					<c:forEach var="ov" items="${fcf.contentList}" varStatus="index">
					<hr/>${ov.content}<br/><strong><c:set target="${timeValues}" value="${ov.changeTime}" property="time"/><fmt:formatDate value="${timeValues}" type="both"/></strong>
					</c:forEach>
					</c:if>
				</div>
				<div class="panel-footer">
					<div class="row">
						<div class="col-xs-6">
						<c:if test="${not empty mem}"><c:if test="${mem.BM_ID==fc.startuser.BM_ID}">
						<a href="${config.rootPath}/editnewtopic?key=${fc.BM_ID}&cid=${fc.BM_ID}" class="alink">编辑</a> / 
						<a href="${config.rootPath}/delselfcontent?block=${fc.outerkey}&cid=${fc.BM_ID}" class="alink">删除</a>
						</c:if></c:if>
						</div>
						<div class="col-xs-6" style="text-align:right">
						发布时间: <c:set target="${timeValues}" value="${fc.BM_TIME}" property="time"/><fmt:formatDate value="${timeValues}" type="both"/>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="col-xs-2">
			<div class="panel panel-default">
				<div class="panel-heading">${fc.startuser.nickname}</div>
				<div class="panel-body center-block">
					<c:if test="${empty fc.startuser.headerIcon}"><img src="http://7xnnmr.com1.z0.glb.clouddn.com/cedar.png" class="img-rounded img-responsive" id="upload_toqiniu_pickfiles"/></c:if>
					<c:if test="${not empty fc.startuser.headerIcon}"><img src="http://headericon.thinkingmax.com/${fc.startuser.headerIcon}?imageView2/1/w/200/h/200" class="img-rounded img-responsive"/></c:if>
				</div>
				<div class="panel-footer">LV: ${fc.startuser.lv}</div>
			</div>
		</div>
	</div>
	<c:forEach var="fcf" items="${page.list}" varStatus="index">
	<div class="row">
		<div class="col-xs-10">
			<div class="panel panel-default">
				<div class="panel-heading"><h3 class="panel-title">${fcf.title}</h3>&nbsp;</div>
				<div class="panel-body">
					${fcf.content}
					<c:if test="${not empty fcf.contentList}">
					<c:forEach var="ov" items="${fcf.contentList}" varStatus="index">
					<hr/>${ov.content}<br/><strong><c:set target="${timeValues}" value="${ov.changeTime}" property="time"/><fmt:formatDate value="${timeValues}" type="both"/></strong>
					</c:forEach>
					</c:if>
				</div>
				<div class="panel-footer">
					<div class="row">
						<div class="col-xs-6">
						<c:if test="${not empty mem}"><c:if test="${mem.BM_ID==fcf.startuser.BM_ID}">
						<a href="${config.rootPath}/editnewtopic?key=${fc.BM_ID}&cid=${fcf.BM_ID}&page=${page.nowPage}" class="alink">编辑</a> / 
						<a href="${config.rootPath}/delselfcontent?key=${fc.BM_ID}&cid=${fcf.BM_ID}" class="alink">删除</a>
						</c:if></c:if>
						</div>
						<div class="col-xs-6" style="text-align:right">
						发布时间: <c:set target="${timeValues}" value="${fcf.BM_TIME}" property="time"/><fmt:formatDate value="${timeValues}" type="both"/>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="col-xs-2">
			<div class="panel panel-default">
				<div class="panel-heading">${fcf.startuser.nickname}</div>
				<div class="panel-body">
					<c:if test="${empty fcf.startuser.headerIcon}"><img src="http://7xnnmr.com1.z0.glb.clouddn.com/cedar.png" class="img-rounded img-responsive"/></c:if>
					<c:if test="${not empty fcf.startuser.headerIcon}"><img src="http://headericon.thinkingmax.com/${fcf.startuser.headerIcon}?imageView2/1/w/200/h/200" class="img-rounded img-responsive"/></c:if>
				</div>
				<div class="panel-footer">LV: ${fcf.startuser.lv}</div>
			</div>
		</div>
	</div>
	</c:forEach>
	<%@ include file="quickreply.jsp" %>
</div>
<%@ include file="static/footer.jsp" %>
</body>
</html>