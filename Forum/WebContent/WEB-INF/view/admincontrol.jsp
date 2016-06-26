<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${not empty mem.admin && mem.admin.forumLv>100}">
<li class="dropdown">
	<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">操作 <span class="caret"></span></a>
	<ul class="dropdown-menu">
		<c:if test="${fc.position==2}">
		<li><a href="${config.rootPath}/manage/settopictonormal?topicid=${fc.BM_ID}">取消置顶全局</a></li>
		</c:if>
		<c:if test="${fc.position==0}">
		<li><a href="${config.rootPath}/manage/settopictoblocktop?topicid=${fc.BM_ID}">置顶</a></li>
		<li><a href="${config.rootPath}/manage/settopictoforumtop?topicid=${fc.BM_ID}">置顶全局</a></li>
		</c:if>
	</ul>
</li>
<li><a href="${config.rootPath}/forum/${forumtitle.BM_ID}/manage/recycle">回收站</a></li>
</c:if>
<c:if test="${not empty ft.manager[mem.BM_ID]}">
<li class="dropdown">
	<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">操作 <span class="caret"></span></a>
	<ul class="dropdown-menu">
		<c:if test="${fc.position==1}">
		<li><a href="${config.rootPath}/manage/settopictonormal?topicid=${fc.BM_ID}">取消置顶</a></li>
		</c:if>
		<c:if test="${fc.position==0}">
		<li><a href="${config.rootPath}/manage/settopictoblocktop?topicid=${fc.BM_ID}">置顶</a></li>
		</c:if>
		<li role="separator" class="divider"></li>
		<li><a href="${config.rootPath}/manage/settopictorecycle?topicid=${fc.BM_ID}">回收</a></li>
		<li><a href="${config.rootPath}/manage/del?topicid=${fc.BM_ID}">删除</a></li>
	</ul>
</li>
<li><a href="${config.rootPath}/manage/recycle">回收站</a></li>
</c:if>