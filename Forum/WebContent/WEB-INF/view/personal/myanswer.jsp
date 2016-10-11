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
<table class="table">
	<thead>
		<tr>
			<th colspan="2">我的回复</th>
		</tr>
	</thead>
	<c:forEach var="fc" items="${answerpage.list}" varStatus="index">
	<tr>
		<td style="width:20%"><c:set target="${timeValues}" value="${fc.BM_TIME}" property="time"/><fmt:formatDate value="${timeValues}" type="both"/></td>
		<td><a href="${config.rootPath}/answer?key=${fc.BM_ID}" target="_blank" style="display:block;white-space:nowrap; overflow:hidden; text-overflow:ellipsis;">${fc.content}</a></td>
	</tr>
	</c:forEach>
</table>
</div>
</div>
</div>
<%@ include file="../static/footer.jsp" %>
</body>
</html>