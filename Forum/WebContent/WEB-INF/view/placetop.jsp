<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<ul class="list-group">
<c:forEach var="hl" items="${headline}" varStatus="index">
	<li class="list-group-item"><a href="${config.rootPath}/view?key=${hl.BM_ID}" class="alink">${hl.title}</a></li>
</c:forEach>
</ul>
<hr/>