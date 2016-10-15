<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<%@ include file="static/header.jsp" %>
<link rel="stylesheet" href="//cdn.bootcss.com/medium-editor/5.22.1/css/medium-editor.min.css" type="text/css" media="screen"/>
<link rel="stylesheet" href="//cdn.bootcss.com/medium-editor/5.22.1/css/themes/bootstrap.min.css" type="text/css" media="screen"/>
<body>
<%@ include file="static/titleline.jsp" %>
<div class="container">
	<nav class="navbar navbar-default">
		<div class="container">
			<div class="navbar-header">
				<a class="navbar-brand" href="${config.rootPath}/forum/${forumtitle.BM_ID}/block/${ft.BM_ID}">${ft.name}</a>
			</div>
		</div>
	</nav>
	<div class="row">
		<div class="col-xs-12">
			<form action="${config.rootPath}/newtopicsubmit" method="POST">
				<div class="form-group">
					<c:if test="${empty tmpCon}"><input type="text" class="form-control" placeholder="标题" name="title"/></c:if>
					<c:if test="${not empty tmpCon}"><input type="text" class="form-control" placeholder="标题" name="title" value="${tmpCon.title}"/></c:if>
				</div>
				<div class="form-group">
					<!-- <textarea class="form-control" placeholder="正文" rows="15" name="content" style="min-width:100%;max-width:100%"></textarea> -->
					<div class="panel panel-default">
						<input type="hidden" name="content" id="forum_newtopic_editor_content">
						<div class="panel-body" style="min-height:450px" id="forum_newtopic_write_editor_body">
						<div class="editor" id="forum_newtopic_write_editor">
							<c:if test="${not empty tmpCon}">${tmpCon.content}</c:if>
						</div>
						</div>
					</div>
				</div>
				<button type="submit" class="btn btn-default">提交</button>
			</form>
		</div>
	</div>
</div>
<%@ include file="static/footer.jsp" %>
<script src="//cdn.bootcss.com/medium-editor/5.22.1/js/medium-editor.min.js"></script>
<script>
var reRunTime=180000;
var recodContent="";
$(document).ready(function() {
	var editor = new MediumEditor('.editor', {
		toolbar: {
			buttons: ['removeFormat' , 'bold', 'italic', 'underline', 'anchor', 'quote' , 'justifyLeft' , 'justifyCenter']
		},
		placeholder: {text: '......'}
	});
	$('#forum_newtopic_write_editor_body').click(function(){
		$(this).find('div').focus();
	});
	$('form').submit( function () {$('#forum_newtopic_editor_content').val($('#forum_newtopic_write_editor').html())});
	
	//自动保存模块
	setTimeout("autoSave()",reRunTime);
});

function autoSave()
{
	$('#forum_newtopic_editor_content').val($('#forum_newtopic_write_editor').html());
	
	if($('#forum_newtopic_editor_content').val()==''||recodContent==$('#forum_newtopic_editor_content').val())
	{
		setTimeout("autoSave()",reRunTime);
		return 0;
	}
	
	recodContent=$('#forum_newtopic_write_editor').html();
	$.post("${config.rootPath}/replyautosave", $('form').serialize(),
	function(data)
	{
		setTimeout("autoSave()",reRunTime);
	});
	
	return 0;
}
</script>
</body>
</html>