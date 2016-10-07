<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="col-xs-2">
	<ul class="list-group">
		<li class="list-group-item">用户名:${admin.nickname} | <a href="${config.rootPath}/adminlogout">登出</a></li>
		<li class="list-group-item">用户组</li>
	</ul>
	<div class="panel panel-default">
		<div class="panel-heading"><h3>功能</h3></div>
		<div class="panel-body">
			<ul class="nav nav-pills nav-stacked" id="navi_nav">
				<li><a href="${config.rootPath}/admin/index">首页</a></li>
				<li><a href="${config.rootPath}/admin/forumedit">论坛管理</a></li>
				<li><a href="${config.rootPath}/admin/synchro">论坛设置</a></li>
			</ul>
		</div>
	</div>
</div>
<script type="text/javascript">
$(document).ready(function(){
	var url=document.location+'';
	var tmp=$('#navi_nav').find('li');

	tmp.each(function(){
		if(url==$(this).find('a').attr('href'))
		{
			$(this).addClass('active');

			return;
		}
	});
});
</script>
