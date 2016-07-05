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
		<div class="col-xs-10">
			<div class="row">
				<div class="col-xs-12"><h3>ICON</h3></div>
				<div class="col-xs-12"><h3>
					<c:if test="${empty forumtitle.icon}"><img src="${config.rootPath}/system/cedar.png" class="img-rounded img-responsive" style="width:2.5em" id="background_ICON_imgSrc_img"/></c:if>
					<c:if test="${not empty forumtitle.icon}"><img src="http://${config.domainBucket}/${forumtitle.icon}" class="img-rounded img-responsive" style="width:2.5em" id="background_ICON_imgSrc_img"/></c:if>
				</h3></div>
				<div class="col-xs-12">
				<form class="form-inline" action="${config.rootPath}/admin/background-icon-submit" method="post">
					<div class="form-group">
						<input type="hidden" class="form-control" name="BM_ID" value="${forumtitle.BM_ID}"/>
						<input type="hidden" class="form-control" name="imgSrc" value="${forumtitle.icon}" id="background_ICON_imgSrc"/>
					</div>
					<button type="button" class="btn btn-default" id="background_ICON_imgSrc_button">选择</button>
					<button type="button" class="btn btn-default" onclick="document.getElementById('background_ICON_imgSrc').value=''">取消</button>
					<button type="submit" class="btn btn-default">提交</button>
				</form>
				</div>
			</div>
			<hr/>
			<div class="row">
				<div class="col-xs-12"><h3>背景图</h3></div>
				<div class="col-xs-12"><img src="http://sys.thinkingmax.com/cedar.png" class="img-rounded img-responsive" style="width:2.5em"/></div>
			</div>
		</div>
	</div>
</div>
<%@ include file="footer.jsp" %>
<script type="text/javascript" src="${config.rootPath}/system/plupload.full.min.js"></script>
<script type="text/javascript" src="${config.rootPath}/system/plupload_zh_CN.js"></script>
<script type="text/javascript" src="${config.rootPath}/system/qiniu.js"></script>
<script type="text/javascript">
$(document).ready(function(){

	var uploader = Qiniu.uploader({
	    runtimes: 'html5,html4',    //上传模式,依次退化
	    browse_button: 'background_ICON_imgSrc_button',       //上传选择的点选按钮，**必需**
	    uptoken_url: '${config.rootPath}/img/adminToken',//Ajax请求upToken的Url，**强烈建议设置**（服务端提供）
	    unique_names: true, // 默认 false，key为文件名。若开启该选项，SDK为自动生成上传成功后的key（文件名）。
	    domain: '${config.domainBucket}',   //bucket 域名，下载资源时用到，**必需**
	    get_new_uptoken: false,  //设置上传文件的时候是否每次都重新获取新的token
	    max_file_size: '3mb',           //最大文件体积限制
	    max_retries: 3,                   //上传失败最大重试次数
	    dragdrop: false,                   //开启可拖曳上传
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
				if(up.files.length>1||up.files.length==0)
		        {
		        	return false;
		        }
				$("#background_ICON_imgSrc_button").attr("disabled",true);
	        },
	        'UploadProgress': function(up, file) {
	        	// 每个文件上传时,处理相关的事情
	        	////console.log(JSON.stringify(file));
	        },
	        'FileUploaded': function(up, file, info) {
				var domain = up.settings.domain;
				$.each(up.files,function(i,n){
					//console.log(JSON.stringify(file));
					//console.log(JSON.stringify(up));
					//console.log(JSON.stringify(info));
					$("#background_ICON_imgSrc").val(n.target_name);
					$("#background_ICON_imgSrc_img").attr("src","http://"+domain+'/'+n.target_name);
				});
	        },
	        'Error': function(up, err, errTip) {
	               //上传出错时,处理相关的事情
	               alert(up);alert(err);alert(errTip);
	        },
	        'UploadComplete': function() {
	            //队列文件处理完毕后,处理相关的事情
	        	$("#background_ICON_imgSrc_button").attr("disabled",false);
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

});
</script>
</body>
</html>