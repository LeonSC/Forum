<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:useBean id="timeValues" class="java.util.Date"/>
<!DOCTYPE html>
<html>
<%@ include file="static/header.jsp" %>
<body>
<div class="container">
	<%@ include file="titleline.jsp" %>
	<nav class="navbar navbar-default">
		<div class="container">
			<div class="navbar-header">
				<a class="navbar-brand" href="#">${ft.name}</a>
			</div>
		</div>
	</nav>
	<div class="row">
		<div class="col-xs-10">
			<div class="panel panel-default">
				<div class="panel-heading"><h3 class="panel-title">${fc.title}</h3></div>
				<div class="panel-body">
					${fc.content}
				</div>
				<div class="panel-footer">
					<div class="row">
						<div class="col-xs-6">
						LV: ${fc.startuser.lv}
						</div>
						<div class="col-xs-6" style="text-align:right">
						发布时间: <c:set target="${timeValues}" value="${fc.BM_TIME}" property="time"/><fmt:formatDate value="${timeValues}" type="both"/>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="col-xs-2">
			<div class="panel panel-default">
				<div class="panel-heading">${fc.startuser.nickname}</div>
				<div class="panel-body center-block">
					<c:if test="${empty fc.startuser.headerIcon}"><img src="http://7xnnmr.com1.z0.glb.clouddn.com/cedar.png" class="img-rounded img-responsive" id="upload_toqiniu_pickfiles"/></c:if>
					<c:if test="${not empty fc.startuser.headerIcon}"><img src="http://headericon.thinkingmax.com/${fc.startuser.headerIcon}?imageView2/1/w/200/h/200" class="img-rounded img-responsive"/></c:if>
				</div>
				<div class="panel-footer">LV: ${fc.startuser.lv}</div>
			</div>
		</div>
	</div>
	<c:forEach var="fcf" items="${page.list}" varStatus="index">
	<div class="row">
		<div class="col-xs-10">
			<div class="panel panel-default">
				<div class="panel-heading"><h3 class="panel-title">${fcf.title}</h3><span class="badge">1</span></div>
				<div class="panel-body">
					${fcf.content}
				</div>
				<div class="panel-footer">
					<div class="row">
						<div class="col-xs-6">
						LV: ${fcf.startuser.lv}
						</div>
						<div class="col-xs-6" style="text-align:right">
						发布时间: <c:set target="${timeValues}" value="${fcf.BM_TIME}" property="time"/><fmt:formatDate value="${timeValues}" type="both"/>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="col-xs-2">
			<div class="panel panel-default">
				<div class="panel-heading">${fcf.startuser.nickname}</div>
				<div class="panel-body">
					<c:if test="${empty fcf.startuser.headerIcon}"><img src="http://7xnnmr.com1.z0.glb.clouddn.com/cedar.png" class="img-rounded img-responsive" id="upload_toqiniu_pickfiles"/></c:if>
					<c:if test="${not empty fcf.startuser.headerIcon}"><img src="http://headericon.thinkingmax.com/${fcf.startuser.headerIcon}?imageView2/1/w/200/h/200" class="img-rounded img-responsive"/></c:if>
				</div>
				<div class="panel-footer">LV: ${fcf.startuser.lv}</div>
			</div>
		</div>
	</div>
	</c:forEach>
</div>
<%@ include file="static/footer.jsp" %>
</body>
</html>