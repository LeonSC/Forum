<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<%@ include file="../static/header.jsp" %>
<body>
<%@ include file="../static/titleline.jsp" %>
<div class="container">
<div class="row">
<div class="col-xs-2">
<%@ include file="nav.jsp" %>
</div>
<div class="col-xs-10">
<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
  <div class="panel panel-default">
    <div class="panel-heading" role="tab" id="headingOne">
      <h4 class="panel-title">
        <a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">个人信息</a>
      </h4>
    </div>
    <div id="collapseOne" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingOne">
	<div class="panel-body">
		<form action="${config.rootPath}/personal/edit-submit" method="POST">
		<ul class="list-group">
			<li class="list-group-item list-group-item-success">登录账户: ${mem.username}</li>
			<li class="list-group-item list-group-item-info">
				<c:if test="${empty mem.headerIcon}"><img src="http://7xnnmr.com1.z0.glb.clouddn.com/cedar.png" class="img-rounded img-responsive" id="upload_toqiniu_pickfiles"/></c:if>
				<c:if test="${not empty mem.headerIcon}"><img src="http://headericon.thinkingmax.com/${mem.headerIcon}?imageView2/1/w/200/h/200" class="img-rounded img-responsive" id="upload_toqiniu_pickfiles"/></c:if>
				<input type="hidden" name="headericon" id="headericon"/>
			</li>
			<li class="list-group-item list-group-item-info">性别:
				<div class="btn-group" data-toggle="buttons">
					<c:choose>
					<c:when test="${mem.gender==1}">
					<label class="btn btn-default active">
						<input type="radio" name="gender" value="1" autocomplete="off" checked/> 男士
					</label>
					</c:when>
					<c:otherwise>
					<label class="btn btn-default">
						<input type="radio" name="gender" value="1" autocomplete="off"/> 男士
					</label>
					</c:otherwise>
					</c:choose>
					<c:choose>
					<c:when test="${mem.gender==2}">
					<label class="btn btn-default active">
						<input type="radio" name="gender" value="2" autocomplete="off" checked/> 女士
					</label>
					</c:when>
					<c:otherwise>
					<label class="btn btn-default">
						<input type="radio" name="gender" value="2" autocomplete="off"/> 女士
					</label>
					</c:otherwise>
					</c:choose>
					<c:choose>
					<c:when test="${mem.gender==0}">
					<label class="btn btn-default active">
						<input type="radio" name="gender" value="0" autocomplete="off" checked/> 保密
					</label>
					</c:when>
					<c:otherwise>
					<label class="btn btn-default">
						<input type="radio" name="gender" value="0" autocomplete="off"/> 保密
					</label>
					</c:otherwise>
					</c:choose>
				</div>
			</li>
			<li class="list-group-item list-group-item-info"> 
			<div class="input-group col-xs-6">
				<span class="input-group-addon">昵称:</span>
				<input type="text" class="form-control" placeholder="Username" aria-describedby="basic-addon1" name="nickname" value="${mem.nickname}">
			</div>
			</li>
			<li class="list-group-item list-group-item-info"> 
			<button type="submit" class="btn btn-default">确认编辑</button>
			</li>
		</ul>
		</form>
	</div>
    </div>
  </div>
  <div class="panel panel-default">
    <div class="panel-heading" role="tab" id="headingTwo">
      <h4 class="panel-title">
        <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">我的通知</a>
      </h4>
    </div>
    <div id="collapseTwo" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingTwo">
      <div class="panel-body">
      	暂无
      </div>
    </div>
  </div>
</div>
</div>
</div>
</div>
<script type="text/javascript" src="${config.rootPath}/system/plupload.full.min.js"></script>
<script type="text/javascript" src="${config.rootPath}/system/plupload_zh_CN.js"></script>
<script type="text/javascript" src="${config.rootPath}/system/qiniu.js"></script>
<script>
$(document).ready(function(){
markOnHeaderIcon.init();
//引入Plupload 、qiniu.js后
var uploader = Qiniu.uploader({
    runtimes: 'html5,html4',    //上传模式,依次退化
    browse_button: 'upload_toqiniu_pickfiles',       //上传选择的点选按钮，**必需**
    uptoken_url: '${config.rootPath}/img/headerIconToken',//Ajax请求upToken的Url，**强烈建议设置**（服务端提供）
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
			markOnHeaderIcon.beforeMark();
        },
        'UploadProgress': function(up, file) {
        	// 每个文件上传时,处理相关的事情
        	markOnHeaderIcon.runMark(file.percent);
        	////console.log(JSON.stringify(file));
        },
        'FileUploaded': function(up, file, info) {
			var domain = up.settings.domain;
			$.each(up.files,function(i,n){
				//console.log(JSON.stringify(file));
				//console.log(JSON.stringify(up));
				//console.log(JSON.stringify(info));
				$("#headericon").val(n.target_name);
				$("#upload_toqiniu_pickfiles").attr("src",domain+'/'+n.target_name+"?imageView2/1/w/200/h/200");
			});
        },
        'Error': function(up, err, errTip) {
               //上传出错时,处理相关的事情
               alert(up);alert(err);alert(errTip);
        },
        'UploadComplete': function() {
            //队列文件处理完毕后,处理相关的事情
        	markOnHeaderIcon.afterMark();
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

//初始化marker
//var getMarkOnHeaderIcon=new markOnHeaderIcon();
});
// domain 为七牛空间（bucket)对应的域名，选择某个空间后，可通过"空间设置->基本设置->域名设置"查看获取
// uploader 为一个plupload对象，继承了所有plupload的方法，参考http://plupload.com/docs

//遮罩逻辑
var markOnHeaderIcon=
{
	beMarked:$("#upload_toqiniu_pickfiles"),
	
	pMarked:$("<div></div>").css({"position":"relative"}),
	
	marker:$("<div></div>").css({"position":"absolute","top":"0px","left":"0px","z-index":"99"}),
	
	loader:$("<div></div>").css({"position":"absolute","top":"0px","left":"0px","z-index":"199","filter":"alpha(opacity=50)","-moz-opacity":"0.5","opacity":"0.5","background-color":"black"}),
	
	init:function()
	{
		this.pMarked.width(this.beMarked.width()).height(this.beMarked.height());
		this.marker.width(0).height(0);
		this.loader.width(0).height(0);
		this.beMarked.wrap(this.pMarked);
		this.beMarked.after(this.loader);
		this.beMarked.after(this.marker);
	},
	
	beforeMark:function()
	{
		this.marker.width(this.beMarked.width()).height(this.beMarked.height());
		this.loader.width(this.beMarked.width()).height(this.beMarked.height());
	},
	afterMark:function()
	{
		this.marker.width(0).height(0);
		this.loader.width(0).height(0);
	},
	runMark : function(percent)
	{
		var h=this.beMarked.height();
		
		var percentHeight=percent*0.01*h;
		
		this.loader.height(percentHeight);
	}
}
</script>
<%@ include file="../static/footer.jsp" %>
</body>
</html>