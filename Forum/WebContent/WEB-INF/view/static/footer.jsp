<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="container" style="margin-top:30px">
	<!-- FOOTER -->
	<footer>
	  <p class="pull-right"><a href="javascript:scroll(0,0);">Back to top</a></p>
	  <p>&copy; POWERED BY SoulMax Team <!-- 灵溢科技股份有限公司. --> &middot; Last Update Time : ${config.updateTime} &middot; <a href="${config.rootPath}/building">Privacy</a> &middot; <a href="${config.rootPath}/building">Terms</a></p>
	</footer>
</div>
<script>
$(document).ready(function() {
	
	$('a[href="#"]').click(function(e){
		e.preventDefault();
	});
});
</script>