<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<form method="post" action="${__contextPath__}/user/changePassword" role="form" id="form-user-change-password">
	<div class="form-group">
	  	<label for="ipt-old-password" class="i18n control-label" i18n-key="USER_CHANGE_PASSWORD_OLD_PASSWORD"></label>
		<input type="password" class="i18n form-control" id="ipt-old-password" name="oldPassword" i18n-key="USER_CHANGE_PASSWORD_OLD_PASSWORD" i18n-render="placeholder" required>
	</div>
	<div class="form-group">
	  	<label for="ipt-new-password" class="i18n control-label" i18n-key="USER_CHANGE_PASSWORD_NEW_PASSWORD"></label>
		<input type="password" class="i18n form-control" id="ipt-new-password" name="newPassword" i18n-key="USER_CHANGE_PASSWORD_NEW_PASSWORD" i18n-render="placeholder" required>
	</div>
	<div class="form-group hide"> <!-- hidden for usabilituy purporses [submit on enter pressed] -->
		<div class="col-sm-offset-2 col-sm-10">
	    	<button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-pencil"></span> <span class="i18n" i18n-key="USER_CHANGE_PASSWORD_EDIT"></span></button>
	    </div>
	</div>
</form>