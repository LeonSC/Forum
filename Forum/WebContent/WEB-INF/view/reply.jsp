<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<%@ include file="static/header.jsp" %>
<link rel="stylesheet" href="//cdn.bootcss.com/medium-editor/5.10.0/css/medium-editor.min.css" type="text/css" media="screen"/>
<link rel="stylesheet" href="//cdn.bootcss.com/medium-editor/5.10.0/css/themes/bootstrap.min.css" type="text/css" media="screen"/>
<body>
<%@ include file="static/titleline.jsp" %>
<div class="container">
	<nav class="navbar navbar-default">
		<div class="container">
			<div class="navbar-header">
				<a class="navbar-brand" href="${config.rootPath}/block/${ft.BM_ID}">${ft.name}</a>
			</div>
		</div>
	</nav>
	<div class="row">
		<div class="col-xs-10">
			<div class="panel panel-default">
				<div class="panel-heading"><h3 class="panel-title">${fc.title}</h3></div>
				<div class="panel-body" style="min-height:150px">
					${fc.content}
				</div>
				<div class="panel-footer">这里是签名</div>
			</div>
		</div>
		<div class="col-xs-2">
			<div class="panel panel-default">
				<div class="panel-heading">${fc.startuser.nickname}</div>
				<div class="panel-body center-block">
					<img src="http://7xnnmr.com1.z0.glb.clouddn.com/cedar.png" class="img-rounded img-responsive"/>
				</div>
				<div class="panel-footer">LV: ${fc.startuser.lv}</div>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-12">
			<form action="${config.rootPath}/replysubmit" method="POST">
				<div class="form-group">
					<c:if test="${empty tmpCon}"><input type="text" class="form-control" placeholder="标题" name="title"/></c:if>
					<c:if test="${not empty tmpCon}"><input type="text" class="form-control" placeholder="标题" name="title" value="${tmpCon.title}"/></c:if>
				</div>
				<div class="form-group">
					<!-- <textarea class="form-control" placeholder="正文" rows="15" name="content" style="min-width:100%;max-width:100%"></textarea> -->
					<div class="panel panel-default">
						<input type="hidden" name="content" id="forum_reply_editor_content">
						<div class="panel-body" style="min-height:650px" id="forum_reply_write_editor_body"><div class="editor" id="forum_reply_write_editor"><c:if test="${not empty tmpCon}">${tmpCon.content}</c:if></div></div>
					</div>
				</div>
				<button type="button" class="btn btn-default"  id="upload_toqiniu_pickfiles">上传</button>
				<button type="submit" class="btn btn-default">提交</button>
			</form>
		</div>
	</div>
</div>
<%@ include file="static/footer.jsp" %>
<script src="//cdn.bootcss.com/medium-editor/5.10.0/js/medium-editor.min.js"></script>
<script type="text/javascript" src="${config.systemFile}/plupload.full.min.js"></script>
<script type="text/javascript" src="${config.systemFile}/plupload_zh_CN.js"></script>
<script type="text/javascript" src="${config.systemFile}/qiniu.js"></script>
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
	
	//引入Plupload 、qiniu.js后
	var uploader = Qiniu.uploader({
	    runtimes: 'html5,html4',    //上传模式,依次退化
	    browse_button: 'upload_toqiniu_pickfiles',       //上传选择的点选按钮，**必需**
	    uptoken_url: '${config.rootPath}/img/token',//Ajax请求upToken的Url，**强烈建议设置**（服务端提供）
	    // uptoken : '', //若未指定uptoken_url,则必须指定 uptoken ,uptoken由其他程序生成
	    unique_names: true, // 默认 false，key为文件名。若开启该选项，SDK为自动生成上传成功后的key（文件名）。
	    //save_key: true,   // 默认 false。若在服务端生成uptoken的上传策略中指定了 `sava_key`，则开启，SDK会忽略对key的处理
	    domain: 'http://img.thinkingmax.com',   //bucket 域名，下载资源时用到，**必需**
	    get_new_uptoken: false,  //设置上传文件的时候是否每次都重新获取新的token
	    //container: 'container',           //上传区域DOM ID，默认是browser_button的父元素，
	    max_file_size: '3mb',           //最大文件体积限制
	    max_retries: 3,                   //上传失败最大重试次数
	    dragdrop: true,                   //开启可拖曳上传
	    drop_element: 'forum_reply_write_editor_body',        //拖曳上传区域元素的ID，拖曳文件或文件夹后可触发上传
	    chunk_size: '4mb',                //分块上传时，每片的体积
	    auto_start: true,                 //选择文件后自动上传，若关闭需要自己绑定事件触发上传
	    init: {
	        'FilesAdded': function(up, files) {
	            plupload.each(files, function(file) {
	                // 文件添加进队列后,处理相关的事情
	            	
	            });
	        },
	        'BeforeUpload': function(up, file) {
				// 每个文件上传前,处理相关的事情
				$('#upload_toqiniu_pickfiles').attr('disabled','disabled');
				$('#upload_toqiniu_pickfiles').html('上传中 <i class="fa fa-spinner fa-spin"></i>');
	        },
	        'UploadProgress': function(up, file) {
	               // 每个文件上传时,处理相关的事情
	        },
	        'FileUploaded': function(up, file, info) {
				var domain = up.settings.domain;
				$.each(up.files,function(i,n){
					var img=$('<img src="'+domain+'/'+n.target_name+'" style="width:100%"/>');
					$('#forum_reply_write_editor').append(img);
				});
	        },
	        'Error': function(up, err, errTip) {
	               //上传出错时,处理相关的事情
	               alert(up);alert(err);alert(errTip);
	        },
	        'UploadComplete': function() {
	            //队列文件处理完毕后,处理相关的事情
	            $('#upload_toqiniu_pickfiles').removeAttr('disabled');
	        	$('#upload_toqiniu_pickfiles').html('上传');
	        },
	        'Key': function(up, file) {
	            // 若想在前端对每个文件的key进行个性化处理，可以配置该函数
	            // 该配置必须要在 unique_names: false , save_key: false 时才生效
	
	            var key = "";
	            // do something with key here
	            return key
	        }
	    }
	});
	
	$('#forum_reply_write_editor_body').click(function(){
		$(this).find('div').focus();
	});
	
	$('form').submit( function () {$('#forum_reply_editor_content').val($('#forum_reply_write_editor').html())});
	
	//自动保存模块
	setTimeout("autoSave()",reRunTime);
});


function autoSave()
{
	$('#forum_reply_editor_content').val($('#forum_reply_write_editor').html());
	
	if($('#forum_reply_editor_content').val()==''||$('#forum_reply_editor_content').val()==recodContent)
	{
		setTimeout("autoSave()",reRunTime);
		return 0;
	}
	
	recodContent=$('#forum_reply_editor_content').val();
	$.post("${config.rootPath}/forum/${forumtitle.BM_ID}/replyautosave", $('form').serialize(),
	function(data)
	{
		setTimeout("autoSave()",reRunTime);
	});
	
	return 0;
}
</script>
</body>
</html>