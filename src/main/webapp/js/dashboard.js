var Dashboard = {
    
    init: function()
    {
	this.queryHolder.__init();
	
	this.filters.__init();
	
	if(userHasRole('CLIENT'))
	{
	    this.client.__init();
	}
	else
	{
	    this.map.__init();
	}
    },
    
    utils: {
	
	countActivePromotions: function(establishment)
	{
	    var counter = 0;
	    
	    if(establishment.promotions !== null)
	    {
		for(var i = 0, len = establishment.promotions.length; i < len; i++)
		{
		    if(establishment.promotions[i].active === true)
		    {
			counter++;
		    }
		}
	    }
	    
	    return counter;
	},
	
	clientHasJoinedPromotionFromEstablishment: function(establishment)
	{
	    if(userHasRole('CLIENT'))
	    {
		var client = Dashboard.client.__data;
		
		for(var i = 0, iLen = client.promotions.length; i < iLen; i++)
		{
		    for(var j = 0, jLen = establishment.promotions.length; j < jLen; j++)
		    {
			if(client.promotions[i].id === establishment.promotions[j].id)
			{
			    return true;
			}
		    }
		}
		
		return false;
	    }
	    else
	    {
		return false;
	    }
	}
    },
    
    queryHolder: {
	
	__lastMarker: null,
	__lastMarkerIcon: null,
	
	__init: function()
	{
	    var self = this;
	    
	    var bloodhound = new Bloodhound({
		
		queryTokenizer: Bloodhound.tokenizers.whitespace,
		remote: {
		    
		    url: ROOT + '/establishment/search/%QUERY',
		    filter: function(establishments)
		    {
			return establishments;
		    }
		},
		datumTokenizer: function(datum)
		{
		    return Bloodhound.tokenizers.whitespace(datum.value);
		}
	    });
	    
	    bloodhound.initialize();
	    
	    $('#inpt-establishment-searchbox').typeahead({
		
		highlight: true,
		minLength: 1
	    }, {
		
		displayKey: 'name',
		source: bloodhound.ttAdapter(),
		templates: {
		    
		    suggestion: Handlebars.compile('<div class="pac-item"><span class="pac-item-query"><span class="pac-icon pac-icon-marker"></span> {{name}}</span></div>')
		}
	    });
	    
	    $('#div-query-holder .tt-dropdown-menu').addClass('map-autocomplete');
	    
	    $('#inpt-establishment-searchbox').on('typeahead:selected', function(obj, datum, name)
	    {
		var establishment = datum;
		var map = Dashboard.map.__map;
		
		if(self.__lastMarkerIcon !== null)
		{
		    self.__lastMarker.setIcon(self.__lastMarkerIcon);
		}
		
		var marker = Dashboard.map.__markers[establishment.id];
		
		if(marker !== undefined)
		{
		    self.__lastMarker = marker;
		    self.__lastMarkerIcon = marker.getIcon();
		    
		    marker.setIcon(Dashboard.map.__getMarkerIcon(Dashboard.map.__markerIconTypes.PINK, establishment));
		    
		    if(marker.getMap() === null)
		    {
			marker.setMap(Dashboard.map.__map);
		    }
		}
		
		Dashboard.map.clearSearchMarkers();
		
		if(Dashboard.map.__currentFilterType !== null)
		{
		    Dashboard.map.filter(Dashboard.map.__currentFilterType);
		}
		
		Dashboard.map.openInfoWindow(marker);
		
		map.setCenter(new google.maps.LatLng(establishment.latitude, establishment.longitude));
	    });
	    
	    $('#div-query-holder .input-group .input-group-addon').click(function()
	    {
		$(this).parents('.input-group').addClass('hide').siblings().removeClass('hide');
	    });
	}
    },
    
    filters: {
	
	__init: function()
	{
	    $('#div-filters').html(jshtml.render(i18n.applyOnDocument($('#scpt-map-filters').html())));
	    
	    $('#div-filters .toggler').click(function()
	    {
		var $this = $(this);
		
		var $filters = $this.parents('#div-filters');
		
		if($filters.hasClass('opened'))
		{
		    $this.find('.glyphicon').removeClass('glyphicon-resize-small').addClass('glyphicon-resize-full');
		    
		    $filters.removeClass('opened');
		}
		else
		{
		    $this.find('.glyphicon').removeClass('glyphicon-resize-full').addClass('glyphicon-resize-small');
		    
		    $filters.addClass('opened');
		}
	    });
	    
	    $('input[name="filters-base-promotions"]').change(function()
	    {
		Dashboard.map.filter(Dashboard.map.filterTypes[$(this).val()]);
	    });
	}
    },
    
    client: {
	
	__data: null,
	
	__init: function()
	{
	    var self = this;
	    
	    var loadAjaxCallback = function(data)
	    {
		self.__data = data;
		
		Dashboard.map.__init();
		
		var registerUpdates = function()
		{
		    $.ajax({
			
			dataType: 'JSON',
			url: ROOT + '/client/registerUpdates',
			success: function(data)
			{
			    self.__data = data;
			    
			    for(var i = 0, len = data.promotions.length; i < len; i++)
			    {
				var marker = null;
				
				for( var index in Dashboard.map.__markers)
				{
				    if(Dashboard.map.__markers[index].__establishment.id === data.promotions[i].establishment.id)
				    {
					marker = Dashboard.map.__markers[index];
				    }
				}
				
				if(marker !== null)
				{
				    marker.setIcon(Dashboard.map.__setupMarkerIcon(data.promotions[i].establishment));
				    
				    if(Dashboard.queryHolder.__lastMarker !== null)
				    {
					if(Dashboard.queryHolder.__lastMarker.__establishment.id === data.promotions[i].establishment.id)
					{
					    Dashboard.queryHolder.__lastMarkerIcon = marker.getIcon();
					    
					    Dashboard.queryHolder.__lastMarker.setIcon(Dashboard.map.__getMarkerIcon(Dashboard.map.__markerIconTypes.PINK, data.promotions[i].establishment));
					}
				    }
				}
			    }
			    
			    if(Dashboard.map.__currentFilterType !== null)
			    {
				Dashboard.map.filter(Dashboard.map.__currentFilterType);
			    }
			    
			    registerUpdates.call();
			},
			error: function(jqXHR, textStatus, errorThrown)
			{
			    if(jqXHR.status === 500)
			    {
				registerUpdates.call();
			    }
			    else
			    {
				window.location = ROOT + '/authentication';
			    }
			}
		    });
		};
		
		registerUpdates();
	    };
	    
	    this.loadAjax(loadAjaxCallback);
	},
	
	loadAjax: function(callback)
	{
	    $.ajax({
		
		dataType: 'JSON',
		url: ROOT + '/client/me',
		success: function(data)
		{
		    if(typeof callback === 'function')
		    {
			callback(data);
		    }
		},
		error: function()
		{
		    window.location = ROOT + '/authentication';
		}
	    });
	}
    },
    
    map: {
	
	didLoad: false,
	
	__markerClustererEnabled: false,
	
	__currentEstablishment: null,
	
	__currentMarker: null,
	
	__currentInfoWindow: null,
	
	__markers: {},
	
	__placeMarkers: [],
	
	__map: null,
	
	__latLngBounds: null,
	
	__markerClusterer: null,
	
	__searchBox: null,
	
	__oms: null,
	
	__infoWindowTemplate: null,
	
	__init: function()
	{
	    var self = this;
	    
	    this.__map = new google.maps.Map($('#div-map-canvas')[0], {
		
		// brazil-sp coords by default ;-)
		center: new google.maps.LatLng(-23.550520, -46.633309),
		zoom: 11
	    });
	    
	    if(navigator.geolocation)
	    {
		navigator.geolocation.getCurrentPosition(function(position)
		{
		    self.__map.setCenter(new google.maps.LatLng(position.coords.latitude, position.coords.longitude));
		});
	    }
	    
	    var loadAjaxCallback = function(data)
	    {
		var registerUpdates = function()
		{
		    $.ajax({
			
			dataType: 'JSON',
			url: ROOT + '/establishment/registerUpdates',
			success: function(data)
			{
			    self.renderOne(data);
			    
			    registerUpdates.call();
			},
			error: function(jqXHR, textStatus, errorThrown)
			{
			    if(jqXHR.status === 500)
			    {
				registerUpdates.call();
			    }
			    else
			    {
				window.location = ROOT + '/authentication';
			    }
			}
		    });
		};
		
		registerUpdates();
	    };
	    
	    this.__infoWindowTemplate = $('#scpt-map-info-window-template').html();
	    this.__latLngBounds = new google.maps.LatLngBounds();
	    this.__searchBox = this.__setupSearchBox();
	    this.__oms = this.__setupOMS();
	    
	    if(this.__markerClustererEnabled)
	    {
		this.__markerClusterer = new MarkerClusterer(this.__map, [], {
		    
		    maxZoom: 18
		});
	    }
	    
	    this.loadAjax(loadAjaxCallback);
	},
	
	clearSearchMarkers: function()
	{
	    for(var i = 0, marker; marker = this.__placeMarkers[i]; i++)
	    {
		marker.setMap(null);
	    }
	    
	    this.__placeMarkers = [];
	},
	
	addSearchMarker: function(name, latitude, longitude)
	{
	    var self = this;
	    
	    var location = new google.maps.LatLng(latitude, longitude);
	    
	    var marker = new google.maps.Marker({
		
		map: self.__map,
		icon: ROOT + '/img/marker-red-flag.png',
		title: name,
		position: location
	    });
	    
	    self.__placeMarkers.push(marker);
	},
	
	__markerIconTypes: {
	    
	    BLUE: 1,
	    GREEN: 2,
	    PINK: 4
	},
	
	__getMarkerIcon: function(type, establishment)
	{
	    var self = this;
	    
	    switch(type)
	    {
		case self.__markerIconTypes.BLUE:
		{
		    return Dashboard.utils.clientHasJoinedPromotionFromEstablishment(establishment) ? ROOT + '/img/marker-blue-star.png' : ROOT + '/img/marker-blue.png';
		}
		case self.__markerIconTypes.GREEN:
		{
		    return Dashboard.utils.clientHasJoinedPromotionFromEstablishment(establishment) ? ROOT + '/img/marker-green-star.png' : ROOT + '/img/marker-green.png';
		}
		case self.__markerIconTypes.PINK:
		{
		    return Dashboard.utils.clientHasJoinedPromotionFromEstablishment(establishment) ? ROOT + '/img/marker-pink-star.png' : ROOT + '/img/marker-pink.png';
		}
		default:
		{
		    return ROOT + '/img/marker-blue.png';
		}
	    }
	},
	
	__setupOMS: function()
	{
	    var self = this;
	    
	    var oms = new OverlappingMarkerSpiderfier(this.__map, {
		
		keepSpiderfied: true
	    });
	    
	    oms.addListener('unspiderfy', function(marker, event)
	    {
		if(self.__currentInfoWindow !== null)
		{
		    self.__currentInfoWindow.close();
		}
	    });
	    
	    return oms;
	},
	
	__setupSearchBox: function()
	{
	    var self = this;
	    
	    var searchBox = new google.maps.places.SearchBox($('#inpt-map-searchbox')[0]);
	    
	    google.maps.event.addListener(searchBox, 'places_changed', function()
	    {
		var places = searchBox.getPlaces();
		
		if(places.length == 0)
		{
		    return;
		}
		
		if(Dashboard.queryHolder.__lastMarkerIcon !== null)
		{
		    Dashboard.queryHolder.__lastMarker.setIcon(Dashboard.queryHolder.__lastMarkerIcon);
		}
		
		Dashboard.queryHolder.__lastMarkerIcon = null;
		Dashboard.queryHolder.__lastMarker = null;
		
		self.clearSearchMarkers();
		
		if(Dashboard.map.__currentFilterType !== null)
		{
		    Dashboard.map.filter(Dashboard.map.__currentFilterType);
		}
		
		var latLngBounds = new google.maps.LatLngBounds();
		
		for(var i = 0, place; place = places[i]; i++)
		{
		    self.addSearchMarker(place.name, place.geometry.location.lat(), place.geometry.location.lng());
		    
		    latLngBounds.extend(place.geometry.location);
		}
		
		var zoom = self.__map.getZoom();
		
		self.__map.setCenter(latLngBounds.getCenter());
		self.__map.fitBounds(latLngBounds);
		
		if(self.__map.getZoom() > zoom)
		{
		    self.__map.setZoom(zoom);
		}
	    });
	    
	    return searchBox;
	},
	
	__resetCurrents: function()
	{
	    this.__currentEstablishment = null;
	    this.__currentMarker = null;
	    this.__currentInfoWindow = null;
	},
	
	__setupMarker: function(establishment)
	{
	    var self = this;
	    
	    var icon = self.__setupMarkerIcon(establishment);
	    
	    var marker = new google.maps.Marker({
		
		map: self.__map,
		position: self.__setupMarkerPosition(establishment),
		title: establishment.name,
		icon: icon
	    });
	    
	    return marker;
	},
	
	__setupMarkerIcon: function(establishment)
	{
	    var self = this;
	    
	    var icon;
	    
	    if(Dashboard.utils.countActivePromotions(establishment) > 0)
	    {
		icon = self.__getMarkerIcon(self.__markerIconTypes.GREEN, establishment);
	    }
	    else
	    {
		icon = self.__getMarkerIcon(self.__markerIconTypes.BLUE, establishment);
	    }
	    
	    return icon;
	},
	
	__setupMarkerPosition: function(establishment)
	{
	    return new google.maps.LatLng(establishment.latitude, establishment.longitude);
	},
	
	__setupInfoWindow: function(establishment)
	{
	    var self = this;
	    
	    var setup = self.__setupInfoWindowContent(establishment);
	    
	    return new InfoBox({
		
		content: setup.html,
		pixelOffset: new google.maps.Size(-(setup.width / 2), 0),
		disableAutoPan: false,
		closeBoxMargin: '15px 5px 5px 5px',
		closeBoxURL: ROOT + '/img/icon-close.png',
		infoBoxClearance: new google.maps.Size(1, 1),
		isHidden: false,
		pane: 'floatPane',
		enableEventPropagation: false
	    });
	},
	
	__setupInfoWindowContent: function(establishment)
	{
	    var parent = Dashboard;
	    
	    var activePromotionsCounter = parent.utils.countActivePromotions(establishment);
	    
	    var setupPromotionsTitle = function(establishment)
	    {
		return (activePromotionsCounter === 0 ? '' : activePromotionsCounter > 1 ? '(' + activePromotionsCounter + ' ' + i18n.get('DASHBOARD_INFO_WINDOW_PROMOTIONS') + ')' : '(' + activePromotionsCounter + ' ' + i18n.get('DASHBOARD_INFO_WINDOW_PROMOTION') + ')');
	    };
	    
	    var setupPromotionsLabel = function(establishment)
	    {
		if(establishment.promotions === null)
		{
		    return setupPromotionsTitle(establishment);
		}
		else
		{
		    if(activePromotionsCounter > 0)
		    {
			return '<a href="' + ROOT + '/promotion/list/active/establishment/' + establishment.id + '">' + setupPromotionsTitle(establishment) + '</a>';
		    }
		    else
		    {
			return setupPromotionsTitle(establishment);
		    }
		}
	    };
	    
	    var HTML = jshtml.render(i18n.applyOnDocument(this.__infoWindowTemplate), {
		
		establishment: establishment,
		promotionsLabel: setupPromotionsLabel(establishment)
	    });
	    
	    var $infoWindowContent = $('<div id="marker-info-window">' + HTML + '</div>').css({
		
		float: 'left',
		height: 'auto'
	    });
	    
	    $('body').append($infoWindowContent);
	    
	    var width = $infoWindowContent.outerWidth() + 30;
	    
	    $infoWindowContent.remove();
	    
	    return {
		
		html: '<div id="marker-info-window" style="width: ' + width + 'px; height: auto;">' + HTML + '</div>',
		width: width
	    }
	},
	
	loadAjax: function(callback)
	{
	    var self = this;
	    
	    $.ajax({
		
		dataType: 'JSON',
		url: ROOT + '/establishment/all',
		success: function(data)
		{
		    self.render(data);
		    
		    self.didLoad = true;
		    
		    if(typeof callback === 'function')
		    {
			callback(data);
		    }
		},
		error: function()
		{
		    window.location = ROOT + '/authentication';
		}
	    });
	},
	
	filterTypes: {
	    
	    KEEP_FILTERED: 1, // should be applied for all base_*
	    BASE_ALL_PROMOTIONS: 2,
	    BASE_ALL_ACTIVE_PROMOTIONS: 4,
	    BASE_ALL_JOINED_PROMOTIONS: 8
	},
	
	__currentFilterType: null,
	
	filter: function(type)
	{
	    var self = this;
	    
	    // aux functions
	    var contains = function(type, filterType)
	    {
		return ((type & Dashboard.map.filterTypes[filterType]) == Dashboard.map.filterTypes[filterType]);
	    };
	    
	    var getFiltereds = function()
	    {
		var markers = {};
		
		for( var index in Dashboard.map.__markers)
		{
		    if(Dashboard.map.__markers[index].getMap() !== null)
		    {
			markers[index] = Dashboard.map.__markers[index];
		    }
		}
		
		return markers;
	    };
	    
	    var loop = function(callback)
	    {
		var markers = contains(type, 'KEEP_FILTERED') ? getFiltereds() : Dashboard.map.__markers;
		
		for( var index in markers)
		{
		    if(!establishmentIsFromQueryHolderLastMarker(markers[index].__establishment))
		    {
			callback(markers[index], markers[index].__establishment);
		    }
		}
	    };
	    
	    var establishmentIsFromQueryHolderLastMarker = function(establishment)
	    {
		if(Dashboard.queryHolder.__lastMarker !== null)
		{
		    return Dashboard.queryHolder.__lastMarker.__establishment.id === establishment.id;
		}
		else
		{
		    return false;
		}
	    };
	    
	    var hide = function(marker)
	    {
		marker.setMap(null);
		
		if(Dashboard.map.__currentInfoWindow !== null && Dashboard.map.__currentEstablishment !== null)
		{
		    if(marker.__establishment.id === Dashboard.map.__currentEstablishment.id)
		    {
			Dashboard.map.__currentInfoWindow.close();
		    }
		}
	    };
	    
	    var show = function(marker)
	    {
		marker.setMap(Dashboard.map.__map);
	    };
	    
	    // filter
	    if(contains(type, 'BASE_ALL_JOINED_PROMOTIONS'))
	    {
		loop(function(marker, establishment)
		{
		    if(!Dashboard.utils.clientHasJoinedPromotionFromEstablishment(establishment))
		    {
			hide(marker);
		    }
		    else
		    {
			show(marker);
		    }
		});
	    }
	    
	    if(contains(type, 'BASE_ALL_ACTIVE_PROMOTIONS'))
	    {
		loop(function(marker, establishment)
		{
		    if(Dashboard.utils.countActivePromotions(establishment) === 0)
		    {
			hide(marker);
		    }
		    else
		    {
			show(marker);
		    }
		});
	    }
	    
	    if(contains(type, 'BASE_ALL_PROMOTIONS'))
	    {
		loop(function(marker, establishment)
		{
		    show(marker);
		});
	    }
	    
	    // unspiderfy
	    this.__oms.unspiderfy();
	    
	    this.__currentFilterType = type;
	},
	
	openInfoWindow: function(marker)
	{
	    var self = Dashboard.map;
	    var establishment = marker.__establishment;
	    
	    if(self.__currentInfoWindow != null)
	    {
		self.__currentInfoWindow.close();
		
		self.__resetCurrents();
	    }
	    
	    self.__currentInfoWindow = self.__setupInfoWindow(establishment);
	    self.__currentInfoWindow.open(self.__map, marker);
	    self.__currentMarker = marker;
	    self.__currentEstablishment = establishment;
	    
	    google.maps.event.addListener(self.__currentInfoWindow, 'closeclick', function(establishment, marker)
	    {
		self.__resetCurrents();
	    });
	},
	
	renderOne: function(data)
	{
	    var self = this, establishment = data, active = establishment.user.status.name === 'ACTIVE';
	    
	    var marker;
	    
	    if(self.__markers[establishment.id] !== undefined)
	    {
		marker = self.__markers[establishment.id];
		
		if(self.__markerClustererEnabled)
		{
		    self.__markerClusterer.removeMarker(marker);
		}
		
		self.__oms.removeMarker(marker);
		
		if(active === false)
		{
		    marker.setMap(null);
		    
		    if(self.__currentEstablishment !== null)
		    {
			if(self.__currentEstablishment.id === establishment.id)
			{
			    self.__currentInfoWindow.close();
			}
		    }
		    
		    delete self.__markers[establishment.id];
		}
		else
		{
		    marker.setMap(self.__map);
		    marker.setPosition(self.__setupMarkerPosition(establishment));
		    marker.setTitle(establishment.name);
		    
		    if(self.__markerClustererEnabled)
		    {
			self.__markerClusterer.addMarker(marker);
		    }
		    
		    self.__oms.addMarker(marker);
		}
	    }
	    else if(active === true)
	    {
		marker = self.__setupMarker(establishment);
		
		self.__markers[establishment.id] = marker;
		
		// lets ensure that we will keep the reference here
		if(self.__markerClustererEnabled)
		{
		    self.__markerClusterer.addMarker(self.__markers[establishment.id]);
		}
		
		self.__oms.addMarker(self.__markers[establishment.id]);
	    }
	    
	    if(active === true)
	    {
		marker['__establishment'] = establishment;
		
		if(self.didLoad === false)
		{
		    marker['animation'] = google.maps.Animation.DROP;
		    
		    self.__latLngBounds.extend(marker.position);
		    
		    this.__map.fitBounds(this.__latLngBounds);
		}
		
		google.maps.event.addListener(marker, 'click', function(establishment, marker)
		{
		    return function()
		    {
			self.openInfoWindow(marker);
		    };
		}(establishment, marker));
		
		marker.setIcon(self.__setupMarkerIcon(establishment));
		
		if(Dashboard.queryHolder.__lastMarker !== null)
		{
		    if(Dashboard.queryHolder.__lastMarker.__establishment.id === establishment.id)
		    {
			Dashboard.queryHolder.__lastMarkerIcon = marker.getIcon();
			
			Dashboard.queryHolder.__lastMarker.setIcon(self.__getMarkerIcon(self.__markerIconTypes.PINK, establishment));
		    }
		}
		
		var doFilter = false;
		
		if(self.__currentEstablishment !== null)
		{
		    if(establishment.id === self.__currentEstablishment.id)
		    {
			self.__currentEstablishment = establishment;
			self.__currentMarker = marker;
			
			var infoWindowNewContent = self.__setupInfoWindowContent(establishment).html;
			
			if(infoWindowNewContent !== self.__currentInfoWindow.getContent())
			{
			    self.__currentInfoWindow.setContent(infoWindowNewContent);
			    
			    $('#marker-info-window').replaceWith(infoWindowNewContent);
			}
		    }
		    else
		    {
			doFilter = true;
		    }
		}
		else
		{
		    doFilter = true;
		}
		
		if(doFilter === true)
		{
		    if(self.__currentFilterType !== null)
		    {
			self.filter(self.__currentFilterType);
		    }
		    else
		    {
			self.filter(self.filterTypes.BASE_ALL_PROMOTIONS);
		    }
		}
	    }
	},
	
	render: function(data)
	{
	    for(var i = 0, len = data.length; i < len; i++)
	    {
		this.renderOne(data[i]);
	    }
	}
    }
};

$(function()
{
    Dashboard.init();
});