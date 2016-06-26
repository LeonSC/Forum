<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%@ include file="static/header.jsp" %>
<body>
<%@ include file="static/titleline.jsp" %>
<div class="container" style="margin-bottom:50px">
	<div class="row">
		<div class="col-xs-6">
			<form class="form-horizontal" method="post" action="${config.rootPath}/memregistersubmit">
				<div class="form-group">
					<h3><label class="col-xs-2 control-label">注册</label></h3>
				</div>
				<div class="form-group">
					<label class="col-xs-2 control-label">Email</label>
					<div class="col-xs-10">
						<input type="email" class="form-control" placeholder="Email" name="email"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-xs-2 control-label">Password</label>
					<div class="col-xs-10">
						<input type="password" class="form-control" placeholder="Password" name="pw"/>
					</div>
				</div>
				<div class="form-group">
					<div class="col-xs-offset-2 col-xs-10">
						<button type="submit" class="btn btn-default">注册</button>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>
<script>
$(document).ready(function(){

});
</script>
<%@ include file="static/footer.jsp" %>
</body>
</html>