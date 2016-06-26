<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="row">
	<div class="col-xs-12">
		<form action="${config.rootPath}/quickreplysubmit" method="POST">
			<div class="form-group">
				<input type="hidden" value="${fc.BM_ID}" name="key"/>
				<textarea class="form-control" placeholder="正文" rows="5" name="content" style="min-width:100%;max-width:100%"></textarea>
			</div>
			<button type="submit" class="btn btn-default">提交</button>
		</form>
	</div>
</div>