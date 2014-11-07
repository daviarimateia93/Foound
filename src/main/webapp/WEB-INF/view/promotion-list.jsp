<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link href="${__contextPath__}/css/promotion-list.css" rel="stylesheet">

<c:set var="currentEstablishment" value="${(selectedEstablishment != null)? selectedEstablishment : establishment}" />
<c:set var="promotions" value="${(selectedEstablishment != null)? selectedEstablishment.promotions : (client != null && establishment == null)? client.promotions : establishment.promotions}"/>
<c:set var="defaultAvatarImage" value="${__contextPath__}/img/avatar-promotion-default.png" />

<script>var PUBLIC_PAGE = ${(sessionScope.user == null)? 'true' : 'false'};</script>

<c:if test="${topPromotions == null}">
	<div class="container">
</c:if>
<c:choose>
	<c:when test="${fn:length(promotions) > 0}">
		<c:if test="${topPromotions == null}">
			<h2 class="title-section">
				<span class="glyphicon glyphicon-fire"></span> 
				<span class="i18n" i18n-key="PROMOTION_LIST_TITLE_SECTION"></span>
				<c:if test="${!sameEstablishment && currentEstablishment != null}">
					 > <small><a href="${__contextPath__}/establishment/${currentEstablishment.id}">${currentEstablishment.name}</a></small>
				</c:if>
			</h2>
		</c:if>
		<c:if test="${!activePromotions && sameEstablishment && currentEstablishment != null && topPromotions == null || (activePromotions && sameEstablishment)}">
			<a href="${__contextPath__}/promotion" class="btn btn-primary pull-right" id="btn-new-promotion"><span class="glyphicon glyphicon-plus"></span> <span class="i18n" i18n-key="PROMOTION_LIST_NEW_PROMOTION"></span></a>
		</c:if>
		<ul class="${(topPromotions != null)? 'list-promotion-establishment' : 'list-promotions'}">
			<c:forEach items="${(topPromotions != null)? topPromotions : promotions}" var="promotion">
		   		<li class="${(selectedEstablishment != null)? '' : (establishment != null)? '' : (client != null)? (promotion.complete)? 'complete' : '' : ''} ${(!promotion.active)? 'inactive' : ''}">
			   		<a href="${__contextPath__}/promotion/${promotion.id}${(activePromotions)? '?active-promotions=true' : ''}">
				   		<c:choose>
							<c:when test="${topPromotions == null}">
								<img src="${(promotion.avatar != null)? promotion.avatar : defaultAvatarImage}" height="150" width="150" alt="Avatar" title="Avatar">
								<div class="pull-left">
									<c:choose>
										<c:when test="${selectedEstablishment != null || establishment != null}">
											<h3>${promotion.title} ${(!promotion.active)? '<small>[ <span class="i18n" i18n-key="PROMOTION_LIST_INACTIVE"></span> ]</small> ' : ''}</h3>
											<p>${promotion.description}</p>
					                        <dl>
						                        <dt><span class="i18n" i18n-key="PROMOTION_LIST_SCORE_THRESHOLD"></span>:</dt> <dd>${promotion.limitScore}</dd>
						                        <dt><span class="i18n" i18n-key="PROMOTION_LIST_CREATION"></span>:</dt> <dd date-type="date">${promotion.creation.time}</dd>
						                        <c:if test="${sameEstablishment}">
						                        	<dt><span class="i18n" i18n-key="PROMOTION_LIST_PARTICIPANTS"></span>:</dt> <dd>${clientsOnPromotion[promotion.id]}</dd>
						                        </c:if>
					                        </dl>
										</c:when>
										<c:otherwise>
											<h3>
				                        		${(!promotion.active)? '<small>[ <span class="i18n" i18n-key="PROMOTION_LIST_INACTIVE"></span> ]</small> ' : ''}
				                        		${promotion.title} [ 
				                        		${(!promotion.complete)? '<small>' : ''}
				                        		${promotion.currentScore}
				                        		${(!promotion.complete)? '</small>' : ''}
					                        	 / ${promotion.limitScore} ]
				                        	</h3>
				                        	<p>
				                        		<b>${promotion.establishment.name}</b>
												<p>${promotion.description}</p>
											</p>
											<dl>
												<dt><span class="i18n" i18n-key="PROMOTION_LIST_ADDRESS"></span>:</dt> <dd>${promotion.establishment.address}</dd>
											</dl>
										</c:otherwise>
									</c:choose>
								</div>
								<div class="clearfix"></div>
							</c:when>
							<c:otherwise>${promotion.title}${(!promotion.active)? ' <small>[ <span class="i18n" i18n-key="PROMOTION_LIST_INACTIVE"></span> ]</small>' : ''}</c:otherwise>
						</c:choose>
					</a>
		   		</li>
			</c:forEach>
		</ul>
	</c:when>
	<c:otherwise>
		<c:if test="${topPromotions == null}">
			<div class="content">
		</c:if>
		<h4 class="i18n text-center" i18n-key="PROMOTION_LIST_NONE_PROMOTION"></h4>
		<c:if test="${!activePromotions && sameEstablishment && currentEstablishment != null && topPromotions == null || (activePromotions && sameEstablishment)}">
			<br />
			<a href="${__contextPath__}/promotion" class="btn btn-primary pull-right btn-new-promotion"><span class="glyphicon glyphicon-plus"></span> <span class="i18n" i18n-key="PROMOTION_LIST_NEW_PROMOTION"></span></a>
		</c:if>
		<c:if test="${topPromotions == null}">
			</div>
		</c:if>
	</c:otherwise>
</c:choose>
<c:if test="${topPromotions == null}">
	<c:if test="${!activePromotions && !sameEstablishment}">
		<a href="javascript: history.go(-1);" class="btn btn-default"><span class="glyphicon glyphicon-chevron-left"></span> <span class="i18n" i18n-key="PROMOTION_LIST_GO_BACK"></span></a>
	</c:if>
	</div>
</c:if>
