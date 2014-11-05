<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link href="${__contextPath__}/css/add-score-to-promotion.css" rel="stylesheet">

<div class="container">
	<form method="post" action="${__contextPath__}/establishment/addScoreToPromotion" class="form-add-score-to-promotion" role="form">
		<c:choose>
			<c:when test="${fn:length(establishment.promotions) > 0}">
				<h2 class="title-section"><span class="glyphicon glyphicon-thumbs-up"></span> <span class="i18n" i18n-key="ADD_SCORE_TO_PROMOTION_TITLE_SECTION"></span></h2>
				<div class="content">
					<div class="form-group">
						<label for="ipt-client-user-nickname" class="i18n" i18n-key="ADD_SCORE_TO_PROMOTION_FORM_CLIENT_USER"></label>
				    	<input type="text" name="clientUserNickname" class="i18n form-control" id="ipt-client-user-nickname" i18n-key="ADD_SCORE_TO_PROMOTION_FORM_CLIENT_USER" i18n-render="placeholder" required autofocus />
				  	</div>
				  	<div class="form-group">
				    	<label for="slct-promotion" class="i18n" i18n-key="ADD_SCORE_TO_PROMOTION_FORM_PROMOTION"></label>
				    	<select name="promotionId" class="form-control" id="slct-promotion">
				    		<c:forEach items="${establishment.promotions}" var="promotion">
				    			<option value="${promotion.id}">${promotion.title}</option>
				    		</c:forEach>
				    	</select>
				  	</div>
				  	<div class="form-group">
					    <label for="ipt-score" class="i18n" i18n-key="ADD_SCORE_TO_PROMOTION_SCORE"></label>
					    <input type="number" name="score" class="i18n form-control" i18n-key="ADD_SCORE_TO_PROMOTION_SCORE" i18n-render="placeholder" id="ipt-score" required>
				  	</div>
				  	<button class="i18n btn btn-lg btn-primary btn-block" type="submit" i18n-key="ADD_SCORE_TO_PROMOTION_FORM_ADD"></button>
			  	</div>
			</c:when>
			<c:otherwise>
			<div class="content">
				<h4 class="i18n text-center" i18n-key="ADD_SCORE_TO_PROMOTION_NO_ACTIVE_PROMOTION"></h4>
			</div>
			</c:otherwise>
		</c:choose>
	</form>
</div>