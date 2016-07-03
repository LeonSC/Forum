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
				<div class="col-xs-12"><h3>LOGO</h3></div>
				<div class="col-xs-12"><img src="http://sys.thinkingmax.com/cedar.png" class="img-rounded img-responsive" style="width:2.5em"/></div>
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
<script type="text/javascript">
$(document).ready(function(){
	
});
</script>
</body>
</html>