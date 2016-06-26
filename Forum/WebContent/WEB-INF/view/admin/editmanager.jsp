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
				<div class="col-xs-6">
					<table class="table table-bordered">
						<tr>
							<td>ID</td><td>姓名</td><td>操作</td>
						</tr>
						<c:forEach var="item" items="${forumtitle.manager}">
						<tr>
							<td>${item.value.username}</td><td>${item.value.nickname}</td><td><a href="${config.rootPath}/admin/delmanagerinblock?key=${nowkey}&ukey=${item.value.BM_ID}" class="btn btn-link">移除</a></td>
						</tr>
						</c:forEach>
					</table>
				</div>
				<div class="col-xs-6">
					<div class="row">
						<div class="col-xs-12">
							<form class="form-horizontal" action="${config.rootPath}/admin/editmanager" method="post">
								<div class="form-group">
									<label class="col-xs-2 control-label">搜索</label>
									<div class="col-xs-6">
										<input type="text" class="form-control" name="sname"/>
										<input type="hidden" name="key" value="${nowkey}"/>
									</div>
									<div class="col-xs-2">
										<button type="submit" class="btn btn-default">查询</button>
									</div>
								</div>
							</form>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-12">
							<table class="table table-bordered">
								<tr>
									<td>ID</td><td>姓名</td><td>操作</td>
								</tr>
								<c:if test="${not empty susers}">
								<c:if test="${not empty susers.list}">
								<c:forEach var="user" items="${susers.list}">
								<tr>
									<td>${user.username}</td><td>${user.nickname}</td><td><a href="${config.rootPath}/admin/addmanagertoblock?key=${nowkey}&ukey=${user.BM_ID}" class="btn btn-link">添加</a></td>
								</tr>
								</c:forEach>
								</c:if>
								</c:if>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</body>
</html>