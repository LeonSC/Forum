<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- nav -->
<nav class="navbar navbar-default navbar-fixed-top">
	<div class="container">
		<div class="navbar-header">
			<c:if test="${not empty forumCache.forumTitle['root'].icon}"><a class="navbar-brand" href="${config.rootPath}"><img src="http://${config.domainBucket}/${forumCache.forumTitle['root'].icon}" class="img-rounded img-responsive" style="width:1.5em"></a></c:if>
			<a class="navbar-brand" href="${config.rootPath}">${forumCache.forumTitle["root"].name}</a>
		</div>
		<div class="navbar-collapse collapse">
			<ul class="nav navbar-nav">
				<c:if test="${empty mem}"><li><a href="${config.rootPath}/memlogin?goback="><strong>登录</strong></a></li></c:if>
            	<c:if test="${not empty mem}">
            	<li>
            	<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">${mem.username} <span class="caret"></span></a>
            		<ul class="dropdown-menu">
						<li><a href="personal/index">用户中心</a></li>
						<li role="separator" class="divider"></li>
						<li><a href="memlogoutsubmit?goback=">登出</a></li>
					</ul>
            	</li>
            	</c:if>
			</ul>
		</div>
	</div>
</nav>
<!-- nav end -->
<!-- title line -->
<div class="row" style="margin-top:4.8em">
	<!-- <div class="col-xs-12"><h2><a href="${config.rootPath}/forum/${forumtitle.BM_ID}" class="alink alinkblack">${forumCache.forumTitle["root"].name}</a></h2></div> -->
</div>
<!-- title line end -->