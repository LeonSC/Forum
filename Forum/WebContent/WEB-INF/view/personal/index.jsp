<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:useBean id="timeValues" class="java.util.Date"/>
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
        <a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">我的帖子</a>
      </h4>
    </div>
    <div id="collapseOne" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingOne">
	<div class="panel-body">
		<table class="table">
			<c:forEach var="fc" items="${topicpage.list}" varStatus="index">
			<tr>
				<td style="width:20%"><c:set target="${timeValues}" value="${fc.BM_TIME}" property="time"/><fmt:formatDate value="${timeValues}" type="both"/></td>
				<td><a href="${config.rootPath}/view?key=${fc.BM_ID}" target="_blank">${fc.title}</a></td>
			</tr>
			</c:forEach>
		</table>
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
<%@ include file="../static/footer.jsp" %>
</body>
</html>