<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<link href="${__contextPath__}/css/promotion.css" rel="stylesheet" />

<c:set var="formActionInsert" value="${__contextPath__}/promotion" />
<c:set var="formActionUpdate" value="${__contextPath__}/promotion/${promotion.id}" />
<c:set var="defaultAvatarImage" value="${__contextPath__}/img/avatar-promotion-default.png" />

<script>var PUBLIC_PAGE = ${(sessionScope.user == null)? 'true' : 'false'};</script>

<script>var VIEW_MODE = ${(promotion != null && !belongsToEstablishment)? 'true' : 'false'};</script>

<div class="container">
	<h2 class="title-section"><span class="glyphicon glyphicon-fire"></span> <span class="i18n" i18n-key="PROMOTION_TITLE_SECTION"></span></h2>
	<div class="content page-promotion">
		<c:if test="${promotion != null}">
			<fieldset id="fs-media">
				<legend class="i18n" i18n-key="PROMOTION_FORM_LEGEND_MEDIA"></legend>
				<form method="post" action="${__contextPath__}/promotion/changeAvatar/${promotion.id}" class="form-horizontal" role="form" id="form-promotion-avatar" enctype="multipart/form-data">
					<div class="form-group">
						<label for="ipt-adress" class="i18n col-sm-2 control-label" i18n-key="PROMOTION_FORM_AVATAR"></label>
						<div class="col-sm-10">
							<span class="media-container">
								<div class="wrapper">
									<c:if test="${establishment != null && belongsToEstablishment}">
										<div class="mask">
											<span class="i18n label" i18n-key="PROMOTION_FORM_EDIT"></span>
										</div>
									</c:if>
									<img class="i18n img-thumbnail" src="${(promotion.avatar != null)? promotion.avatar : defaultAvatarImage}" i18n-key="PROMOTION_FORM_AVATAR" i18n-render="alt|title">
									<c:if test="${establishment != null && belongsToEstablishment}">
										<input class="i18n" type="file" name="avatar" i18n-key="PROMOTION_FORM_AVATAR" i18n-render="alt|title" />
									</c:if>
								</div>
							</span>
						</div>
					</div>
				</form>
			</fieldset>
		</c:if>
		<fieldset>
			<legend class="i18n" i18n-key="PROMOTION_FORM_LEGEND_INFORMATIONS"></legend>
			<form method="post" class="form-horizontal" role="form" action="${(promotion == null)? formActionInsert : formActionUpdate}">
				<div class="form-group">
			    	<label for="ipt-title" class="i18n col-sm-2 control-label" i18n-key="PROMOTION_FORM_TITLE"></label>
			    	<div class="col-sm-10">
			      		<input type="text" class="i18n form-control" id="ipt-title" name="title" i18n-key="PROMOTION_FORM_TITLE" i18n-render="placeholder" value="${promotion.title}" required ${(promotion != null)? 'disabled' : ''} />
			    	</div>
			  	</div>
			  	<div class="form-group">
			    	<label for="ipt-limit-score" class="i18n col-sm-2 control-label" i18n-key="PROMOTION_FORM_SCORE_THRESHOLD"></label>
			    	<div class="col-sm-10">
			      		<input type="text" class="i18n form-control" id="ipt-limit-score" name="limitScore" i18n-key="PROMOTION_FORM_SCORE_THRESHOLD" i18n-render="placeholder" value="${promotion.limitScore}" required ${(promotion != null)? 'disabled' : ''} />
			    	</div>
			  	</div>
			  	<div class="form-group">
			    	<label for="txta-description" class="i18n col-sm-2 control-label" i18n-key="PROMOTION_FORM_DESCRIPTION"></label>
			    	<div class="col-sm-10">
			      		<textarea class="i18n form-control" id="txta-description" i18n-key="PROMOTION_FORM_DESCRIPTION" name="description" i18n-render="placeholder" required ${(promotion != null)? 'disabled' : ''}>${promotion.description}</textarea>
			    	</div>
			  	</div>
				<c:if test="${promotion != null}">
				  	<div class="form-group">
			  			<label for="ipt-limit-score" class="i18n col-sm-2 control-label" i18n-key="PROMOTION_FORM_CREATION"></label>
				    	<div class="col-sm-10">
				      		<input type="text" date-type="datetime" class="i18n form-control" id="ipt-limit-score" name="creation" i18n-key="PROMOTION_FORM_CREATION" i18n-render="placeholder" value="${promotion.creation.time}" ${(promotion != null)? 'disabled' : ''} required />
				    	</div>
			  		</div>
				</c:if>
			  	<div class="form-group">
			  		<label class="i18n col-sm-2 control-label" i18n-key="PROMOTION_FORM_STATUS"></label>
		    		<div class="col-sm-10">
		    			<div class="radio">
			       			<label>
			         			<input type="radio" name="active" value="true" ${(promotion.active)? 'checked' : (promotion == null)? 'checked' : ''}${(promotion != null && !belongsToEstablishment)? ' disabled' : ''} /> <span class="i18n" i18n-key="PROMOTION_FORM_STATUS_ACTIVE"></span>
			       			</label>
			       		</div>
			       		<div class="radio">
			       			<label>
			         			<input type="radio" name="active" value="false" ${(!promotion.active && promotion != null)? 'checked' : ''}${(promotion != null && !belongsToEstablishment)? ' disabled' : ''} /> <span class="i18n" i18n-key="PROMOTION_FORM_STATUS_INACTIVE"></span>
			       			</label>
		       			</div>
		    		</div>
		  		</div>
		  		<div class="form-group">
			    	<div class="col-sm-offset-2 col-sm-10">
			    		<c:choose>
							<c:when test="${activePromotions || !belongsToEstablishment}">
								<a href="javascript: history.go(-1);" class="btn btn-default"><span class="glyphicon glyphicon-chevron-left"></span> <span class="i18n" i18n-key="PROMOTION_FORM_GO_BACK"></span></a>
							</c:when>
							<c:otherwise>
								<c:if test="${establishment != null}">
						    		<c:if test="${promotion == null}">
										<button type="submit" class="btn btn-primary pull-right"><span class="glyphicon glyphicon-plus"></span> <span class="i18n" i18n-key="PROMOTION_FORM_ADD"></span></button>
									</c:if>
									<c:if test="${promotion != null && belongsToEstablishment}">
										<button type="submit" class="btn btn-primary pull-right"><span class="glyphicon glyphicon-pencil"></span> <span class="i18n" i18n-key="PROMOTION_FORM_EDIT"></span></button>
									</c:if>
					    		</c:if>
					    		<a href="${__contextPath__}/promotion/list" class="btn btn-default"><span class="glyphicon glyphicon-list-alt"></span> <span class="i18n" i18n-key="PROMOTION_FORM_LIST"></span></a>
							</c:otherwise>
			    		</c:choose>
			    	</div>
			  	</div>
			</form>
		</fieldset>
	</div>
</div>

<script src="${__contextPath__}/js/promotion.js"></script>