<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<%@ include file="static/header.jsp" %>
<body>
<%@ include file="static/titleline.jsp" %>
<div class="container" style="margin-bottom:50px">
	<div class="row">
		<div class="col-xs-6">
			<c:if test="${empty mem}">
			<form class="form-horizontal" method="post" action="${config.rootPath}/memloginsubmit">
				<div class="form-group">
					<label class="col-xs-2 control-label">Email</label>
					<div class="col-xs-10">
						<input type="email" class="form-control" placeholder="Email" name="email"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-xs-2 control-label">Password</label>
					<div class="col-xs-10">
						<input type="password" class="form-control" placeholder="Password" id="memlogin_pw"/>
						<input type="hidden"  name="pw" id="inputPasswordSubmit">
						<c:if test="${not empty error_wrongpw}"><p class="help-block" style="color:red">用户密码错误.</p></c:if>
					</div>
				</div>
				<!-- 
				<div class="form-group">
					<div class="col-xs-offset-2 col-xs-10">
						<div class="checkbox">
							<label><input type="checkbox"/> Remember me</label>
						</div>
					</div>
	  			</div>
	  			 -->
				<div class="form-group">
					<div class="col-xs-offset-2 col-xs-10">
						<button type="submit" class="btn btn-default">登录</button>
					</div>
				</div>
			</form>
			</c:if>
			<c:if test="${not empty mem}">
			<form class="form-horizontal" method="post" action="${config.rootPath}/memlogoutsubmit">
				<div class="form-group">
					<label class="col-xs-2 control-label">Email</label>
					<div class="col-xs-10">
						<p class="form-control-static">${mem.username}</p>
					</div>
				</div>
				<!-- 
				<div class="form-group">
					<div class="col-xs-offset-2 col-xs-10">
						<div class="checkbox">
							<label><input type="checkbox"/> Remember me</label>
						</div>
					</div>
	  			</div>
	  			 -->
				<div class="form-group">
					<div class="col-xs-offset-2 col-xs-10">
						<button type="submit" class="btn btn-default">登出</button>
					</div>
				</div>
			</form>
			</c:if>
		</div>
		<div class="col-xs-6">
			<a href="${config.rootPath}/memregister" class="btn btn-default">注册</a>
		</div>
	</div>
</div>
<script>
$(document).ready(function(){
	$("form").submit(function(){
		$("#inputPasswordSubmit").val(hex_sha1($("#memlogin_pw").val()).toUpperCase());
	});
});
</script>
<%@ include file="static/footer.jsp" %>
<script src="${config.rootPath}/system/sha1.min.js"></script>
</body>
</html>