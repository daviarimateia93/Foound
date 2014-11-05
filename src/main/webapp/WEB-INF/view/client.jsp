<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:set var="sex">${client.sex}</c:set>

<c:if test="${sessionScope.USER != null}">
	<div class="container">
		<h2 class="title-section"><span class="glyphicon glyphicon-fire"></span> <span class="i18n" i18n-key="CLIENT_TITLE_SECTION"></span></h2>
		<div class="content">
			<form method="post" action="${__contextPath__}/client/${client.id}" class="form-horizontal" role="form" id="form-client">
				<fieldset>
					<legend class="i18n" i18n-key="CLIENT_FORM_LEGEND_INFORMATIONS"></legend>
</c:if>
					<div class="form-group">
					   	<label for="ipt-name" class="i18n col-sm-2 control-label" i18n-key="CLIENT_FORM_NAME"></label>
					   	<div class="col-sm-10">
					     	<input type="text" class="i18n form-control" id="ipt-name" i18n-key="CLIENT_FORM_NAME" name="name" i18n-render="placeholder" value="${client.name}" required>
					   	</div>
					 </div>
					 <div class="form-group">
					   	<label for="ipt-birthday" class="i18n col-sm-2 control-label" i18n-key="CLIENT_FORM_BIRTHDAY"></label>
					   	<div class="col-sm-10">
					     	<input type="text" date-type="date" class="i18n form-control" id="ipt-birthday" i18n-key="CLIENT_FORM_BIRTHDAY" name="birthday" i18n-render="placeholder" value="${client.birthday.time}" required>
					   	</div>
					 </div>
					 <div class="form-group">
					   	<label for="ipt-email" class="i18n col-sm-2 control-label" i18n-key="CLIENT_FORM_EMAIL"></label>
					   	<div class="col-sm-10">
					     	<input type="email" class="i18n form-control" id="ipt-email" name="email" i18n-key="CLIENT_FORM_EMAIL" i18n-render="placeholder" value="${client.email}" required>
					   	</div>
					 </div>
					 <div class="form-group">
					 	<label class="i18n col-sm-2 control-label" i18n-key="CLIENT_FORM_GENDER"></label>
					  	<div class="col-sm-10">
				  			<div class="radio">
				      			<label>
				        			<input type="radio" name="sex" value="1" ${(sex == '1' || sessionScope.user == null)? 'checked' : ''} /> <span class="i18n" i18n-key="CLIENT_FORM_GENDER_MAN"></span>
				      			</label>
				      		</div>
				      		<div class="radio">
				      			<label>
				        			<input type="radio" name="sex" value="2" ${(sex == '2')? 'checked' : ''} /> <span class="i18n" i18n-key="CLIENT_FORM_GENDER_WOMAN"></span>
				      			</label>
				     		</div>
					  	</div>
					</div>
<c:if test="${sessionScope.USER != null}">
					<div class="form-group">
					 	<div class="col-sm-offset-2 col-sm-10">
					   		<button type="submit" class="i18n btn btn-primary" id="btn-alterar" i18n-key="CLIENT_FORM_EDIT"></button>
					 	</div>
					</div>
				</fieldset>
			</form>
		</div>
	</div>
</c:if>

