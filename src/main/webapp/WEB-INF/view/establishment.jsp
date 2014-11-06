<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link href="${__contextPath__}/css/establishment.css" rel="stylesheet">

<c:set var="defaultAvatarImage" value="${__contextPath__}/img/avatar-establishment-default.png" />

<script>var PUBLIC_PAGE = ${(sessionScope.user == null)? 'true' : 'false'};</script>

<script>var VIEW_MODE = ${(establishment != null && !sameEstablishment)? 'true' : 'false'};</script>

<c:if test="${sessionScope.USER != null || establishment != null}">
	<div class="container">
		<h2 class="title-section">
			<span class="glyphicon glyphicon-shopping-cart"></span> 
			<c:choose>
				<c:when test="${!sameEstablishment}">
					${establishment.name}
				</c:when>
				<c:otherwise>
					<span class="i18n" i18n-key="ESTABLISHMENT_TITLE_SECTION"></span>
				</c:otherwise>
			</c:choose>
		</h2>
		<div class="content page-establishment">
			<form method="post" action="${__contextPath__}/establishment/${establishment.id}?change-avatar=false" class="form-horizontal" role="form" id="form-establishment-infos">
				<fieldset>
					<legend class="i18n" i18n-key="ESTABLISHMENT_FORM_LEGEND_INFORMATIONS"></legend>
</c:if>
					<input type="hidden" name="latitude" value="-1" />
					<input type="hidden" name="longitude" value="-1" />
					<div class="form-group">
				    	<label for="ipt-name" class="i18n col-sm-2 control-label" i18n-key="ESTABLISHMENT_FORM_NAME"></label>
				    	<div class="col-sm-10">
				      		<input type="text" class="i18n form-control" id="ipt-name" i18n-key="ESTABLISHMENT_FORM_NAME" name="name" i18n-render="placeholder" value="${establishment.name}" required ${(!sameEstablishment && establishment != null)? 'disabled' : ''} />
				    	</div>
				  	</div>
				  	<div class="form-group">
				    	<label for="ipt-phone" class="i18n col-sm-2 control-label" i18n-key="ESTABLISHMENT_FORM_PHONE"></label>
				    	<div class="col-sm-10">
				      		<input type="text" class="i18n form-control" id="ipt-phone" i18n-key="ESTABLISHMENT_FORM_PHONE" name="phone" i18n-render="placeholder" value="${establishment.phone}" phone required ${(!sameEstablishment && establishment != null)? 'disabled' : ''} />
				    	</div>
				  	</div>
				  	<div class="form-group">
				    	<label for="txta-address" class="i18n col-sm-2 control-label" i18n-key="ESTABLISHMENT_FORM_ADDRESS"></label>
				    	<div class="col-sm-10">
				      		<textarea class="i18n form-control" id="txta-address" name="address" i18n-key="ESTABLISHMENT_FORM_ADDRESS" i18n-render="placeholder" required ${(!sameEstablishment && establishment != null)? 'disabled' : ''}>${establishment.address}</textarea>
				    	</div>
				  	</div>
				  	<div class="form-group">
				    	<label for="txta-about" class="i18n col-sm-2 control-label" i18n-key="ESTABLISHMENT_FORM_ABOUT"></label>
				    	<div class="col-sm-10">
				      		<textarea class="i18n form-control" id="txta-about" name="about" i18n-key="ESTABLISHMENT_FORM_ABOUT" i18n-render="placeholder" required ${(!sameEstablishment && establishment != null)? 'disabled' : ''}>${establishment.about}</textarea>
				    	</div>
				  	</div>
				  	<div class="form-group">
				  		<label for="slct-specialities" class="i18n col-sm-2 control-label" i18n-key="ESTABLISHMENT_FORM_SPECIALITY">Speciality</label>
				  		<div class="col-sm-10">
				  			<select name="speciality.name" class="form-control" id="slct-specialities" ${(!sameEstablishment && establishment != null)? 'disabled' : ''}>
				  				<c:forEach items="${specialities}" var="speciality">
				  					<option value="${speciality.name}" class="i18n" i18n-key="SPECIALITY_${speciality.name}" ${(establishment.speciality.name == speciality.name)? 'selected' : ''}></option>
				  				</c:forEach>
				  			</select>
				  		</div>
				  	</div>
					<c:if test="${sessionScope.USER != null && sameEstablishment}">
					  	<div class="form-group">
					    	<div class="col-sm-offset-2 col-sm-10">
					      		<button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-pencil"></span> <span class="i18n" i18n-key="ESTABLISHMENT_FORM_EDIT"></span></button>
					    	</div>
					  	</div>
					</c:if>
					<div class="form-group">
						<div class="col-sm-12">
				      		<h4 class="i18n text-center hide" i18n-key="ESTABLISHMENT_FORM_ADDRESS_LOADING" id="establishment-address-matching"></h4>
				    	</div>
					</div>
<c:if test="${sessionScope.USER != null || establishment != null}">
				</fieldset>			  	
			</form>
			<form method="post" action="${__contextPath__}/establishment/changeAvatar/${establishment.id}" class="form-horizontal" role="form" id="form-establishment-avatar" enctype="multipart/form-data">
				<fieldset class="col-sm-6 pull-left" id="fs-media">
					<legend class="i18n" i18n-key="ESTABLISHMENT_FORM_LEGEND_MEDIA"></legend>
					<div class="form-group">
						<label for="ipt-adress" class="i18n col-sm-2 control-label" i18n-key="ESTABLISHMENT_FORM_AVATAR"></label>
						<div class="col-sm-10">
							<span class="media-container">
								<div class="wrapper">
									<c:if test="${sameEstablishment}">
										<div class="mask">
											<span class="i18n label" i18n-key="ESTABLISHMENT_FORM_EDIT"></span>
										</div>
									</c:if>
									<img class="i18n img-thumbnail" src="${(establishment.avatar != null)? establishment.avatar : defaultAvatarImage}" i18n-key="ESTABLISHMENT_FORM_AVATAR" i18n-render="alt|title" />
									<c:if test="${sameEstablishment}">
										<input class="i18n" type="file" name="avatar" i18n-key="ESTABLISHMENT_FORM_AVATAR" i18n-render="alt|title" />
									</c:if>
								</div>
							</span>
						</div>					
					</div>
				</fieldset>
			</form>
			<div class="col-sm-6 pull-right" id="fs-promotions">
				<h3 class="title-legend"><span class="i18n" i18n-key="ESTABLISHMENT_TITLE_LEGEND_PROMOTIONS"></span> <em><b>#top5</b></em></h3>
				<c:import url="/WEB-INF/view/promotion-list.jsp"></c:import>
				<ul class="list-inline text-right">
					<c:if test="${sameEstablishment}">
						<li><a href="${__contextPath__}/promotion" class="i18n pull-right" i18n-key="ESTABLISHMENT_NEW_PROMOTION"></a></li>
					</c:if>
					<c:if test="${fn:length(topPromotions) > 0}">
						<li><a href="${__contextPath__}/promotion/list/establishment/${establishment.id}" class="i18n pull-right" i18n-key="ESTABLISHMENT_SEE_ALL_PROMOTIONS"></a></li>
					</c:if>
				</ul>
			</div>
		</div>
	</div>
</c:if>


<script src="${__contextPath__}/js/establishment.js"></script>