<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ include file="../static/header.jsp" %>
<body>
<%@ include file="message.jsp" %>
<div class="container-fluid">
	<div class="row">
		<%@ include file="navi.jsp" %>
		<div class="col-xs-10">
			<div class="row">
				<div class="col-xs-12">
				<h3>同步设置</h3>
				</div>
			</div>
			<hr/>
			<div class="row">
				<div class="col-xs-12">
					<form class="form-inline" action="${config.rootPath}/admin/synchro-submit" method="post">
						<div class="form-group">
							<label>地址</label>
							<input type="text" class="form-control" name="ip">
						</div>
						<div class="form-group">
							<div class="input-group"><div class="input-group-addon">@</div><input class="form-control" type="text" name="alias"></div>
						</div>
						<button type="submit" class="btn btn-default">录入</button>
					</form>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-6">
					<ul class="list-group">
						<c:forEach items="${page.list}" varStatus="i" var="item" >
						<li class="list-group-item"><span class="badge btn-warning">未连接</span><span class="syn_ip_address">${item.ipAddress}</span> - ${item.alias}</li>
						</c:forEach>
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>
<%@ include file="footer.jsp" %>
<script type="text/javascript">
$(document).ready(function(){
	$("span.syn_ip_address").each(function(i){
		
		var thi=$(this);
		
		$.get("http://"+thi.html()+"/synchro/check", function(data){
			if(data=="ok")
			{
				var tmp=thi.prev();
				
				tmp.addClass("btn-success");
				tmp.removeClass("btn-warning");
				tmp.html("正常");
			}
		});
	});
});
</script>
</body>
</html>