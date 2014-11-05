<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:set var="status">${sessionScope.USER.status.name}</c:set>

<link href="${__contextPath__}/css/user.css" rel="stylesheet" />

<c:if test="${sessionScope.USER != null}">
	<div class="container">
		<h2 class="title-section">
			<span class="glyphicon glyphicon-user"></span> <span class="i18n" i18n-key="USER_TITLE_SECTION">Conta</span>
		</h2>
		<div class="content">
			<form method="post" action="${__contextPath__}/user/${sessionScope.USER.id}" class="form-horizontal" role="form" id="form-user">
				<fieldset>
					<legend class="i18n" i18n-key="USER_FORM_LEGEND_CONFIGURATIONS"></legend>
</c:if>
					<div class="form-group">
						<label for="ipt-nickname" class="i18n col-sm-2 control-label" i18n-key="USER_FORM_USER"></label>
						<div class="col-sm-10">
							<input type="text" class="i18n form-control" id="ipt-nickname" name="${(sessionScope.USER != null)? 'nickname' : 'user.nickname'}" i18n-key="USER_FORM_USER" i18n-render="placeholder" value="${sessionScope.USER.nickname}" required>
						</div>
					</div>
					<div class="form-group">
						<label for="ipt-password" class="i18n col-sm-2 control-label" i18n-key="USER_FORM_PASSWORD"></label>
						<c:choose>
							<c:when test="${sessionScope.USER == null}">
								<div class="col-sm-10">
									<input type="password" class="i18n form-control" id="ipt-password" name="user.password" i18n-key="USER_FORM_PASSWORD" i18n-render="placeholder" required>
								</div>
							</c:when>
							<c:otherwise>
								<button type="button" class="i18n change-password btn btn-link" i18n-key="USER_FORM_CHANGE_PASSWORD"></button>
							</c:otherwise>
						</c:choose>
					</div>
<c:if test="${sessionScope.USER != null}">
						<div class="form-group">
							<label class="i18n col-sm-2 control-label" i18n-key="USER_FORM_STATUS"></label>
							<div class="col-sm-10">
								<div class="radio">
									<label>
										<input type="radio" name="status.name" value="ACTIVE" ${(status == 'ACTIVE' || sessionScope.USER == null)? 'checked' : ''} /> <span class="i18n" i18n-key="USER_FORM_STATUS_ACTIVE"></span>
									</label>
								</div>
								<div class="radio">
									<label>
										<input type="radio" name="status.name" value="INACTIVE" ${(status == 'INACTIVE')? 'checked' : ''} /> <span class="i18n" i18n-key="USER_FORM_STATUS_INACTIVE"></span>
									</label>
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-offset-2 col-sm-10">
								<button type="submit" class="btn btn-primary" id="btn-alterar">
									<span class="glyphicon glyphicon-pencil"></span> <span class="i18n" i18n-key="USER_FORM_EDIT"></span>
								</button>
							</div>
						</div>
				</fieldset>
			</form>
		</div>
	</div>
</c:if>

<script src="${__contextPath__}/js/user.js"></script>