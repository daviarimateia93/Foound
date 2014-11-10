<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link href="${__contextPath__}/css/dashboard.css" rel="stylesheet">

<script id="scpt-map-info-window-template" type="text/template">
	<div>
		## var avatar = data.establishment.avatar !== null ? data.establishment.avatar : '${__contextPath__}/img/avatar-establishment-default.png'; ##
		<a href="${__contextPath__}/establishment/##@ data.establishment.id ##">
			<img src="##@ avatar ##" class="avatar pull-left" width="100" />
		</a>
	</div>
	<div class="info">
		<span>##@ data.promotionsLabel ##</span>
		<div class="name">
			<a href="${__contextPath__}/establishment/##@ data.establishment.id ##">##@ data.establishment.name ##</a>
		</div>
	    <div class="about">##@ data.establishment.about ##</div>
		<hr />
		<div class="address">##@ data.establishment.address ##</div>
	</div>
	<div class="clearfix"></div>
</script>

<script id="scpt-map-filters" type="text/template">
	<div class="pull-left toggler">
		<span class="glyphicon glyphicon-resize-full"></span>
		<div class="i18n title" i18n-key="DASHBOARD_FILTERS_TITLE"></div>
	</div>
	<form role="form" class="base pull-left">
		<div class="i18n title" i18n-key="DASHBOARD_FILTERS_TITLE"></div>
		<hr />
		<div class="radio">
	    	<label>
	      		<input type="radio" name="filters-base-promotions" value="BASE_ALL_PROMOTIONS" checked /> <span class="i18n" i18n-key="DASHBOARD_FILTERS_BASE_ALL_PROMOTIONS"></span>
	    	</label>
	  	</div>
	  	<div class="radio">
	    	<label>
	      		<input type="radio" name="filters-base-promotions" value="BASE_ALL_ACTIVE_PROMOTIONS" /> <span class="i18n" i18n-key="DASHBOARD_FILTERS_BASE_ALL_ACTIVE_PROMOTIONS"></span>
	    	</label>
	  	</div>
		## if(userHasRole('CLIENT')) ##
		## { ##
			
			<div class="radio">
				<label>
					<input type="radio" name="filters-base-promotions" value="BASE_ALL_JOINED_PROMOTIONS" /> <span class="i18n" i18n-key="DASHBOARD_FILTERS_BASE_ALL_JOINED_PROMOTIONS"></span>
				</label>
			</div>

		## } ##
	</form>
	<form role="form" class="specialities pull-left">
		<hr />
		<div class="checkbox">
	    	<label>
	      		<input type="checkbox" name="filters-specialities" value="SPECIALITY_ALL" checked /> <span class="i18n" i18n-key="DASHBOARD_FILTERS_SPECIALITY_ALL"></span>
	    	</label>
	  	</div>
		<c:forEach items="${specialities}" var="speciality">
			<div class="checkbox">
	    		<label>
		      		<input type="checkbox" name="filters-specialities" value="SPECIALITY_${speciality.name}" /> <span class="i18n" i18n-key="SPECIALITY_${speciality.name}"></span>
	    		</label>
	  		</div>
		</c:forEach>
	</form>
	<div class="clearfix"></div>
</script>

<div id="div-query-holder">
	<div class="input-group">
		<input type="text" id="inpt-map-searchbox" class="i18n form-control" i18n-key="DASHBOARD_QUERY_HOLDER_FIND_ADDRESS" i18n-render="placeholder" />
		<span class="input-group-addon">
			<span class="glyphicon glyphicon-shopping-cart"></span>
		</span>
	</div>
	<div class="input-group hide">
		<input type="text" id="inpt-establishment-searchbox" class="i18n form-control" i18n-key="DASHBOARD_QUERY_HOLDER_FIND_ESTABLISHMENT" i18n-render="placeholder" />
		<span class="input-group-addon">
			<span class="glyphicon glyphicon-globe"></span>
		</span>
	</div>
</div>
<div id="div-filters"></div>
<div id="div-map-canvas"></div>
<a href="javascript: void(0);" id="a-my-location">
	<img src="${__contextPath__}/img/icon-my-location.png" width="14" height="14" />
</a>

<script src="http://maps.google.com/maps/api/js?sensor=false&libraries=places"></script>
<script src="${__contextPath__}/lib/infobox-1.9/infobox_packed.js"></script>
<script src="${__contextPath__}/lib/marker-clusterer-2.1.2/marker-clusterer_packed.js"></script>
<script src="${__contextPath__}/lib/oms/oms.min.js"></script>
<script src="${__contextPath__}/js/dashboard.js"></script>