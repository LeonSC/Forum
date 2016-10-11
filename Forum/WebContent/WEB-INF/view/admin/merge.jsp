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
			<form action="${config.rootPath}/admin/merge-submit" method="POST" id="merge-submit-form">
			<div class="row">
				<div class="col-xs-6">
					<div class="btn-group">
						<button type="button" class="btn btn-primary">${from.name}</button>
						<button type="button" class="btn btn-danger">到</button>
						<c:if test="${not empty to}">
						<button type="button" class="btn btn-danger dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
							<span id="merge_merge_selectable_to"></span> <span class="caret"></span>
						</button>
						<ul class="dropdown-menu">
							<c:forEach var="ftto" items="${to}" varStatus="index">
							<c:if test="${ftto.BM_ID != from.BM_ID}">
							<li><a href="#" id="${ftto.BM_ID}" class="merge_selectable">${ftto.name}</a></li>
							</c:if>
							</c:forEach>
						</ul>
						</c:if>
						<input type="hidden" id="merge_merge_selectable_to_id" name="toBMID"/>
						<input type="hidden" name="from" value="${from.BM_ID}"/>
						<input type="hidden" name="root" value="${root}"/>
					</div>
					<div class="btn-group">
						<a type="button" class="btn btn-default a-alt" href="all-merge-submit">全部移动</a>
						<a type="button" class="btn btn-default a-alt" href="merge-submit">移动</a>
					</div>
					<div class="btn-group">
						<a type="button" class="btn btn-default a-alt" href="all-recycle-submit">全部回收</a>
						<a type="button" class="btn btn-default a-alt" href="recycle-submit">回收</a>
					</div>
					<c:if test="${empty page.list}">
					<div class="btn-group">
						<a type="button" class="btn btn-danger a-alt" href="block-del-submit">删除</a>
					</div>
					</c:if>
					<div class="btn-group">
						<a type="button" class="btn btn-default" href="${config.rootPath}/admin/forumedit?key=${root}">返回</a>
					</div>
					<hr/>
					<ul class="list-group">
						<c:forEach var="fc" items="${page.list}" varStatus="index">
						<li class="list-group-item">
							<div class="checkbox">
							<label><input type="checkbox" name="titlechecked" value="${fc.BM_ID}">${fc.title}</label><span class="pull-right"><a href="${config.rootPath}/${forumtitle.BM_ID}/view?key=${fc.BM_ID}" class="alink" target="_blank">查看</a></span>
							</div>
						</li>
						</c:forEach>
					</ul>
				</div>
			</div>
			</form>
		</div>
	</div>
</div>
<%@ include file="footer.jsp" %>
<script type="text/javascript">
$(document).ready(function(){
	$(".merge_selectable").click(function(e){
		e.preventDefault();
		var th=$(this);
		$("#merge_merge_selectable_to").html(th.html());
		$("#merge_merge_selectable_to_id").val(th.attr("id"));
	});
	
	$(".a-alt").click(function(e){
		e.preventDefault();
		
		if(!window.confirm('弹出这么丑的一个框来提示你确认操作?'))
		{
            //alert("确定");
            return false;
        }
		
		$("#merge-submit-form").attr("action","${config.rootPath}/admin/"+$(this).attr("href"));
		$("#merge-submit-form").submit();
	});
});
</script>
</body>
</html>