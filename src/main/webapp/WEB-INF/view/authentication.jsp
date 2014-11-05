<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link href="${__contextPath__}/css/authentication.css" rel="stylesheet">

<script>var PUBLIC_PAGE = true;</script>

<div class="container auth">
	<form method="post" action="${__contextPath__}/authentication/login" class="form-authentication" role="form">
		<h1 class="form-authentication-heading">
			<span>
				<b>Foo</b>un<b>d</b>
			</span>
			<div class="btn-group">
				<c:import url="/WEB-INF/view/preferences.jsp"></c:import>
			</div>
			<div class="clearfix"></div>
		</h1>
		<input type="text" name="nickname" class="i18n form-control" i18n-key="AUTHENTICATION_USER" i18n-render="placeholder" required autofocus />
        <input type="password" name="password" class="i18n form-control" i18n-key="AUTHENTICATION_PASSWORD" i18n-render="placeholder" required />
        <button class="i18n btn btn-lg btn-primary btn-block" type="submit" i18n-key="AUTHENTICATION_ENTER"></button>
		<a class="i18n" href="${__contextPath__}/join" i18n-key="AUTHENTICATION_SIGN_UP"></a>
	</form>
</div>