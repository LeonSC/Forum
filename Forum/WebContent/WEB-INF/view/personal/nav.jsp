<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="row">
<div class="col-xs-12">
<div class="list-group" id="nav_nav">
	<a class="list-group-item" href="${config.rootPath}/personal/index">我的首页</a>
	<a class="list-group-item" href="${config.rootPath}/personal/myinfo">我的信息</a>
	<a class="list-group-item" href="${config.rootPath}/personal/mytopic">我的主题</a>
	<a class="list-group-item" href="${config.rootPath}/personal/mycontent">我的回复</a>
	<!-- 
	<a class="list-group-item" href="${config.rootPath}/personal/myfavorite">我的收藏</a>
	<a class="list-group-item" href="${config.rootPath}/personal/mymessage">我的通知</a>
	 -->
</div>
</div>
</div>
<script type="text/javascript">
$(document).ready(function(){
	var url=document.location+'';
	var tmp=$('#nav_nav').find('.list-group-item');

	tmp.each(function(){
		if(url==$(this).attr('href'))
		{
			$(this).addClass('active');

			return;
		}
	});
});
</script>