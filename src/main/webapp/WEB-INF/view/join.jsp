<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<script>var PUBLIC_PAGE = true;</script>

<div class="join container">
	<h2 class="title-section"><span class="glyphicon glyphicon-user"></span> <span class="i18n" i18n-key="JOIN_TITLE_SECTION"></span></h2>
	<div class="content">
		<form class="form-horizontal" role="form">
			<div class="form-group">
			  	<label class="i18n col-sm-2 control-label" i18n-key="JOIN_FORM_I_WANT_TO_BE"></label>
		    	<div class="col-sm-10">
		    		<div class="radio">
			    		<label>
			   				<input type="radio" name="want-to-be" value="client" checked /> <span class="i18n" i18n-key="JOIN_FORM_CLIENT"></span>
			       		</label>
			       	</div>
			       	<div class="radio">
			       		<label>
			       			<input type="radio" name="want-to-be" value="establishment" /> <span class="i18n" i18n-key="JOIN_FORM_ESTABLISHMENT"></span>
			       		</label>
		       		</div>
		    	</div>
		  	</div>
		</form>
		<form method="post" action="${__contextPath__}/client" class=" form-horizontal" role="form" id="form-join-client">
			<c:import url="/WEB-INF/view/user.jsp"></c:import>
			<c:import url="/WEB-INF/view/client.jsp"></c:import>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button type="submit" class="btn btn-primary pull-right"><span class="glyphicon glyphicon-pencil"></span> <span class="i18n" i18n-key="JOIN_FORM_SIGN_UP"></span></button>
					<a href="${__contextPath__}/authentication" class="btn btn-default pull-left"><span class="glyphicon glyphicon-chevron-left"></span> <span class="i18n" i18n-key="JOIN_FORM_GO_BACK"></span></a>
			    </div>
			</div>
		</form>
		<form method="post" action="${__contextPath__}/establishment" class="hide form-horizontal" role="form" id="form-join-establishment">
			<c:import url="/WEB-INF/view/user.jsp"></c:import>
			<c:import url="/WEB-INF/view/establishment.jsp"></c:import>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button type="submit" class="btn btn-primary pull-right"><span class="glyphicon glyphicon-pencil"></span> <span class="i18n" i18n-key="JOIN_FORM_SIGN_UP"></span></button>
			    	<a href="${__contextPath__}/authentication" class="btn btn-default pull-left"><span class="glyphicon glyphicon-chevron-left"></span> <span class="i18n" i18n-key="JOIN_FORM_GO_BACK"></span></a>
			    </div>
			</div>
		</form>
	</div>
</div>

<script src="${__contextPath__}/js/join.js"></script>