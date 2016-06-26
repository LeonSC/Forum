<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<%@ include file="../static/header.jsp" %>
<body>
<%@ include file="message.jsp" %>
<div class="container-fluid">
	<div class="row">
		<%@ include file="navi.jsp" %>
		<div class="col-xs-10" id="container">
			<div class="row">
				<div class="col-xs-12"><h3>${forumtitle.name}</h3></div>
			</div>
			<div class="row">
				<div class="col-xs-12">
					<div class="col-xs-12">
						<form class="form-inline" action="${config.rootPath}/admin/forumedit-submit" method="post">
							<div class="form-group">
								<input type="text" class="form-control" name="name"/>
								<input type="hidden" class="form-control" name="BM_ID" value="${forumtitle.BM_ID}"/>
							</div>
							<button type="submit" class="btn btn-default">修改</button>
							<c:if test="${empty forumCache.forumTitle[forumtitle.BM_ID]}"><a href="${config.rootPath}/admin/forumonoffline?key=${forumtitle.BM_ID}&page=${ftpage.nowPage}" class="btn btn-default">上线</a></c:if>
							<c:if test="${not empty forumCache.forumTitle[forumtitle.BM_ID]}"><a href="${config.rootPath}/admin/forumonoffline?key=${forumtitle.BM_ID}&page=${ftpage.nowPage}" class="btn btn-success">在线</a></c:if>
							<a href="${config.rootPath}" class="btn btn-default" target="_blank">浏览</a>
						</form>
					</div>
				</div>
			</div>
			<c:forEach items="${forumtitle.subForumTitle}" varStatus="i" var="sft" >
			<hr/>
			<div class="row">
				<div class="col-xs-1"><form class="form-horizontal"><div class="form-group"><label class="col-sm-12 control-label">|-</label></div></form></div>
				<div class="col-xs-11">
					<form class="form-inline" action="${config.rootPath}/admin/forumedit-submit" method="post">
						<div class="form-group">
							<input type="number" class="form-control" name="order" min="1" max="100" value="${sft.order}"/>
						</div>
						<div class="form-group">
							<input type="text" class="form-control" name="name" value="${sft.name}"/>
							<input type="hidden" class="form-control" name="BM_ID" value="${sft.BM_ID}"/>
							<input type="hidden" class="form-control" name="topkey" value="${forumtitle.BM_ID}"/>
						</div>
						<button type="submit" class="btn btn-default">修改</button>
						<a href="#" class="btn btn-default">管理员</a>
						<a href="${config.rootPath}/admin/block-del-submit?from=${sft.BM_ID}&page=1&root=${forumtitle.BM_ID}" class="btn btn-link">删除</a>
					</form>
				</div>
			</div>
			<c:forEach items="${sft.subForumTitle}" varStatus="i" var="sftsub" >
			<div class="row">
				<div class="col-xs-1 col-xs-offset-1"><form class="form-horizontal"><div class="form-group"><label class="col-sm-12 control-label">|-</label></div></form></div>
				<div class="col-xs-10">
					<form class="form-inline" action="${config.rootPath}/admin/forumedit-submit" method="post">
						<div class="form-group">
							<input type="number" class="form-control" name="order" min="1" max="100" value="${sftsub.order}"/>
						</div>
						<div class="form-group">
							<input type="text" class="form-control" name="name" value="${sftsub.name}"/>
							<input type="hidden" class="form-control" name="BM_ID" value="${sftsub.BM_ID}"/>
							<input type="hidden" class="form-control" name="topkey" value="${forumtitle.BM_ID}"/>
						</div>
						<button type="submit" class="btn btn-default">保存</button>
						<a href="${config.rootPath}/admin/merge?from=${sftsub.BM_ID}&page=1&root=${forumtitle.BM_ID}" class="btn btn-link">删除/回收</a>
						<a href="${config.rootPath}/admin/editmanager?key=${sftsub.BM_ID}&page=1" class="btn btn-link">管理员</a>
						<div class="form-group">
							<p class="form-control-static">
							<c:forEach var="item" items="${sftsub.manager}" varStatus="status">
								${item.value.nickname} <c:if test="${not status.last}">/</c:if>
							</c:forEach>
							</p>
						</div>
					</form>
				</div>
			</div>
			</c:forEach>
			<div class="row">
				<div class="col-xs-1 col-xs-offset-1"><form class="form-horizontal"><div class="form-group"><label class="col-sm-12 control-label">|-</label></div></form></div>
				<div class="col-xs-10">
					<form class="form-inline" action="${config.rootPath}/admin/forumadd-submit" method="post">
						<div class="form-group">
							<input type="number" class="form-control" name="order" min="1" max="100" value="10"/>
						</div>
						<div class="form-group">
							<input type="text" class="form-control" name="name"/>
							<input type="hidden" class="form-control" name="outerkey" value="${sft.BM_ID}"/>
							<input type="hidden" class="form-control" name="topkey" value="${forumtitle.BM_ID}"/>
						</div>
						<button type="submit" class="btn btn-default">新增</button>
					</form>
				</div>
			</div>
			</c:forEach>
			<hr/>
			<div class="row">
				<div class="col-xs-1"><form class="form-horizontal"><div class="form-group"><label class="col-sm-12 control-label">|-</label></div></form></div>
				<div class="col-xs-11">
					<form class="form-inline" action="${config.rootPath}/admin/forumadd-submit" method="post">
						<div class="form-group">
							<input type="number" class="form-control" name="order" min="1" max="100" value="10"/>
						</div>
						<div class="form-group">
							<input type="text" class="form-control" name="name"/>
							<input type="hidden" class="form-control" name="outerkey" value="${forumtitle.BM_ID}"/>
							<input type="hidden" class="form-control" name="topkey" value="${forumtitle.BM_ID}"/>
						</div>
						<button type="submit" class="btn btn-default">新增</button>
						<button type="submit" class="btn btn-default">回收站</button>
					</form>
				</div>
			</div>
		</div>
	</div>
</div>
<%@ include file="footer.jsp" %>
<script type="text/javascript">
$(document).ready(function(){
	
});
</script>
</body>
</html>