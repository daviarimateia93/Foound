<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="user" value="${(establishment != null)? establishment.user : client.user}"/>
<!DOCTYPE html>
<html lang="pt-br">
	<head>
    	<meta charset="utf-8" />
    	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
    	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=0" />
    	<title>${__title__}</title>
    	
    	<!-- Bootstrap -->
    	<link href="${__contextPath__}/lib/bootstrap-3.1.1-dist/css/bootstrap.min.css" rel="stylesheet" />
    	
    	<link href="${__contextPath__}/lib/bootstrap3-dialog-master/css/bootstrap-dialog.min.css" rel="stylesheet" />
    
	    <!-- Layout -->
	    <link href="${__contextPath__}/css/layout.css" rel="stylesheet" />

    	<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    	<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    	<!--[if lt IE 9]>
      		<script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      		<script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    	<![endif]-->
    
    	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>

    	<!-- Include all compiled plugins (below), or include individual files as needed -->
    	<script src="${__contextPath__}/lib/bootstrap-3.1.1-dist/js/bootstrap.min.js"></script>
    	
    	<script src="${__contextPath__}/lib/bootstrap3-dialog-master/js/bootstrap-dialog.min.js"></script>
    	
    	<script src="${__contextPath__}/lib/jquery.cookie/jquery.cookie.js"></script>
    	
    	<script src="${__contextPath__}/lib/moment-2.8.3/moment-with-locales.js"></script>
    	
    	<script src="${__contextPath__}/lib/utils/utils.js"></script>
    	
    	<script src="${__contextPath__}/lib/i18n/i18n.js"></script>
    	
    	<script src="${__contextPath__}/lib/jshtml/jshtml.js"></script>
    	
    	<script src="${__contextPath__}/lib/typeahead/typeahead.jquery.js"></script>
    	
    	<script src="${__contextPath__}/lib/handlebars-2.0.0/handlebars-v2.0.0.js"></script>
    	
    	<script src="${__contextPath__}/lib/jquery.elastic-1.6.11/jquery.elastic.source.js"></script>
    	
    	<script src="${__contextPath__}/js/layout.js"></script>
    
    	<script>
    	
    		var ROOT = '${__contextPath__}';
    		var JSESSIONID = '${cookie.JSESSIONID.value}';
    		var USER_ROLES = [
    			<c:forEach items="${user.roles}" var="role" varStatus="loop">
					'${role.name}'<c:if test="${!loop.last}">,</c:if>
				</c:forEach>
			];
    		
    		var LANGUAGE = '${cookie.LANGUAGE.value}';
    		
    		function userHasRole(role)
    		{
    			return ($.inArray(role, USER_ROLES) > -1);
    		}
    		
    	</script>
  	</head>
  	<body>
  		<div class="alert alert-success" role="alert">
  			<button type="button" class="close">
  				<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
  			</button>
  			<p class="content">Mensagem de sucesso!</p>
  		</div>
  		<div class="alert alert-danger" role="alert">
  			<button type="button" class="close">
  				<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
  			</button>
  			<p class="content">Mensagem de erro!</p>
  		</div>
 		
 		<c:if test="${__partialViewSimpleName__ != 'authentication'}">
			<div id="main-navbar" class="navbar navbar-default navbar-fixed-top" role="navigation">
	      		<div class="container">
	        		<div class="navbar-header">
	          			<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
	            			<span class="sr-only">Toggle navigation</span>
	            			<span class="icon-bar"></span>
	            			<span class="icon-bar"></span>
	            			<span class="icon-bar"></span>
	          			</button>
	          			<div class="btn-group">
	          				<a class="navbar-brand" href="${__contextPath__}"><b>Foo</b>un<b>d</b></a>
	          				<button type="button" id="btn-preferences-toggler" class="btn dropdown-toggle" data-toggle="dropdown">
								<span class="caret"></span>
								<span class="sr-only">Toggle Dropdown</span>
							</button>
							<ul id="ul-preferences-chooser" class="dropdown-menu" role="menu">
								<li role="presentation" class="i18n dropdown-header" i18n-key="PREFERENCES_CHOOSER_LANGUAGE"></li>
							    <li>
							    	<a href="${__contextPath__}/i18n?language=pt-BR" class="language">
							    		<img src="${__contextPath__}/img/flag-pt-br.png" width="20" alt="pt-BR" title="pt-BR">
							    		<span>pt-BR</span>
							    	</a>
							    </li>
							    <li>
							    	<a href="${__contextPath__}/i18n?language=en" class="language">
							    		<img src="${__contextPath__}/img/flag-en.png" width="20" alt="en" title="en">
							    		<span>en</span>
							    	</a>
							    </li>
							</ul>
	          			</div>
	        		</div>
	        		<c:if test="${sessionScope.USER != null}">
		        		<div class="navbar-collapse collapse">
		          			<ul class="nav navbar-nav navbar-right">
		            			<li class="${(__partialViewSimpleName__ == 'dashboard') ? 'active' : ''}"><a href="${__contextPath__}/dashboard" class="i18n" i18n-key="NAVBAR_DASHBOARD"></a></li>
		            			<li class="${(__partialViewSimpleName__ == 'promotion-list' || __partialViewSimpleName__ == 'promotion') ? 'active' : ''}"><a href="${__contextPath__}/promotion/list" class="i18n" i18n-key="NAVBAR_PROMOTIONS"></a></li>
	               				<li class="${((__partialViewSimpleName__ == 'establishment' && establishment != null && client == null) || (__partialViewSimpleName__ == 'client' && client != null && establishment == null)) ? 'active' : ''}">
		               				<c:choose>
										<c:when test="${client != null}">
											<a href="${__contextPath__}/client" class="i18n" i18n-key="NAVBAR_CLIENT"></a>
										</c:when>
										<c:otherwise>
											<a href="${__contextPath__}/establishment" class="i18n" i18n-key="NAVBAR_ESTABLISHMENT"></a>
										</c:otherwise>
									</c:choose>
								</li>
	               				<li class="${(__partialViewSimpleName__ == 'user') ? 'active' : ''}"><a href="${__contextPath__}/user" class="i18n" i18n-key="NAVBAR_USER"></a></li>
								<c:if test="${client == null && establishment != null}">
	               					<li class="${(__partialViewSimpleName__ == 'add-score-to-promotion') ? 'active' : ''}"><a href="${__contextPath__}/establishment/addScoreToPromotion" class="i18n" i18n-key="NAVBAR_ADD_SCORE_TO_PROMOTION"></a></li>
	               				</c:if>
		                		<li><a href="${__contextPath__}/authentication/logout"><span class="glyphicon glyphicon-log-out"></span> <span class="i18n" i18n-key="NAVBAR_EXIT"></span></a></li>
		          			</ul>
		        		</div>
	        		</c:if>
	      		</div>
	    	</div>
    	</c:if>
	
    	<c:import url="${__partialViewFullName__}"></c:import>
		
		<c:choose>
			<c:when test="${param['form-success'] == true}">
		    	<script>$(function(){ Layout.alert.show('success', i18n.get('ALERT_FORM_SUCCESS')); });</script>
		  	</c:when>
		  	<c:when test="${param['form-success'] == false}">
		    	<script>$(function(){ Layout.alert.show('danger', "${param['form-error']}"); });</script>
		  	</c:when>
		</c:choose>
	</body>
</html>
