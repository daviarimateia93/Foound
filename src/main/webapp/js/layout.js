var Layout = {
    
    init: function()
    {
	this.__i18n();
	
	this.__moment();
	
	this.applyHandlers();
	
	this.registerLogout();
    },
    
    __i18n: function()
    {
	i18n.init({
	    
	    language: LANGUAGE,
	    baseDir: ROOT + '/i18n/'
	});
	
	i18n.applyOnDocument();
    },
    
    __moment: function()
    {
	moment.locale(i18n.getLanguage());
    },
    
    alert: {
	
	show: function(type, content)
	{
	    $('.alert.alert-' + type + ' .content').html(content).parents('.alert').show();
	},
	
	hide: function(type)
	{
	    $('.alert.alert-' + type + ' .content').html('').parent('.alert').hide();
	}
    },
    
    isPublicPage: function()
    {
	try
	{
	    return PUBLIC_PAGE === true;
	}
	catch(exception)
	{
	    return false;
	}
    },
    
    registerLogout: function()
    {
	if(!this.isPublicPage())
	{
	    var self = this;
	    
	    $.ajax({
		
		url: ROOT + '/authentication/registerLogout',
		success: function(data)
		{
		    window.location = ROOT + '/authentication';
		},
		error: function(jqXHR, textStatus, errorThrown)
		{
		    if(jqXHR.status === 500)
		    {
			self.registerLogout();
		    }
		    else
		    {
			window.location = ROOT + '/authentication';
		    }
		}
	    });
	}
    },
    
    applyHandlers: function()
    {
	var self = this;
	
	// handle ul-preferences-chooser
	$(document).off('click', '#ul-preferences-chooser a.language');
	$(document).on('click', '#ul-preferences-chooser a.language', function(e)
	{
	    $.removeCookie('LANGUAGE');
	    
	    window.location = $(this).attr('href');
	    
	    e.preventDefault();
	    
	    return false;
	});
	
	// handle bootstrap dialog selector
	$(document).off('click', '.bootstrap-dialog-selector li');
	$(document).on('click', '.bootstrap-dialog-selector li', function()
	{
	    $(this).addClass('active');
	    $(this).siblings().removeClass('active');
	});
	
	// handle alerts
	$(document).off('click', '.alert > .close');
	$(document).on('click', '.alert > .close', function()
	{
	    $(this).parent('.alert').hide();
	});
	
	// handle *[phone]
	$('*[phone]').each(function()
	{
	    var phone;
	    
	    try
	    {
		var hasValueProperty = (this['value'] !== undefined);
		
		if(hasValueProperty)
		{
		    phone = $(this).val();
		}
		else
		{
		    phone = $(this).text();
		}
		
		var country = parseInt(phone.substring(0, 3));
		var area = parseInt(phone.substring(3, 6))
		var number = phone.substring(6, phone.length);
		
		var prettyPhone = '+' + country + ' ' + area + ' ' + number;
		
		if($(this).prop('tagName').toUpperCase() === 'INPUT' && $(this).attr('disabled') === undefined)
		{
		    $(this).attr('type', 'hidden');

		    var countryHTML = i18n.applyOnDocument('<input type="text" class="i18n form-control phone country" id="ipt-phone-country" i18n-key="ESTABLISHMENT_FORM_PHONE_COUNTRY" i18n-render="placeholder" required />');
		    var areaHTML = i18n.applyOnDocument('<input type="text" class="i18n form-control phone area" id="ipt-phone-area" i18n-key="ESTABLISHMENT_FORM_PHONE_AREA" i18n-render="placeholder" required />');
		    var numberHTML = i18n.applyOnDocument('<input type="text" class="i18n form-control phone number" id="ipt-phone-number" i18n-key="ESTABLISHMENT_FORM_PHONE_NUMBER" i18n-render="placeholder" required />');
		    
		    var $country = $(countryHTML);
		    var $area = $(areaHTML);
		    var $number = $(numberHTML);
		    
		    $(this).parent().append([$country, $area, $number]);
		}
		else
		{
		    if(hasValueProperty)
		    {
			$(this).val(prettyPhone);
		    }
		    else
		    {
			$(this).html(prettyPhone);
		    }
		}
	    }
	    catch(exception)
	    {
		
	    }
	});
	
	// handle <input date-type="date|datetime|time" />
	$('*[date-type]').each(function()
	{
	    var undefined;
	    var date;
	    
	    try
	    {
		var hasValueProperty = (this['value'] !== undefined);
		
		if(hasValueProperty)
		{
		    date = new Date(parseInt($(this).val(), 10));
		}
		else
		{
		    date = new Date(parseInt($(this).text(), 10));
		}
		
		if(!isNaN(date))
		{
		    var type = $(this).attr('date-type');
		    
		    $(this).data('date', date);
		    
		    if(hasValueProperty)
		    {
			$(this).val(self.date.formatDateASLocal(date, type));
		    }
		    else
		    {
			$(this).html(self.date.formatDateASLocal(date, type));
		    }
		}
	    }
	    catch(exception)
	    {
		
	    }
	});
	
	// handle <form></form>
	$(document).off('submit', 'form');
	$(document).on('submit', 'form', function(e)
	{
	    e.stopPropagation();
	    e.preventDefault();
	    
	    var $form = $(this);
	    
	    $form.find('*[type="submit"]').attr('disabled', '');
	    
	    $('input[date-type]').each(function()
	    {
		var type = $(this).attr('date-type');
		var date = self.date.convertLocalToDate($(this).val(), type);
		
		$(this).data('date', date);
		
		$(this).val(self.date.formatDateAsISO($(this).data('date'), type));
	    });
	    
	    var action = {
		
		stop: false,
	    };
	    
	    $(this).trigger('ajaxsubmit', action);
	    
	    if(action.stop === false)
	    {
		var isUpload = ($(this).attr('enctype') == 'multipart/form-data');
		var url = $(this).attr('action');
		var data = isUpload ? new FormData(this) : $(this).serialize();
		var type = $(this).attr('method');
		
		var undefined;
		
		var onComplete = function(data, textStatus, jqXHR, queryString)
		{
		    var contentType = jqXHR.getResponseHeader('Content-Type');
		    
		    if(typeof contentType === 'string')
		    {
			contentType = contentType.split(';')[0];
			
			if(contentType.toUpperCase() == 'TEXT/HTML')
			{
			    window.location = jqXHR.getResponseHeader('X-Url');
			}
			else
			{
			    var url = window.location.toString();
			    
			    if(typeof queryString === 'string')
			    {
				var parts = queryString.split('&');
				
				for(var i = 0, len = parts.length; i < len; i++)
				{
				    var variable = parts[i].split('=')[0];
				    RegExp
				    regExp = new RegExp('[\&]?' + variable + '\=[^\&]*', 'ig');
				    
				    url = url.replace(regExp, '');
				}
			    }
			    
			    // remove form-* variables
			    url = url.replace(/[\&]?form\-([^=])+([^\;])*/gi, '');
			    
			    var indexUrlSeparator;
			    
			    if((indexUrlSeparator = url.indexOf('?')) > -1)
			    {
				if(indexUrlSeparator !== url.length - 1)
				{
				    url += '&';
				}
			    }
			    else
			    {
				url += '?';
			    }
			    
			    url += queryString;
			    
			    window.location = url;
			}
		    }
		    
		    $form.find('*[type="submit"]').removeAttr('disabled');
		};
		
		var onSuccess = function(data, textStatus, jqXHR)
		{
		    onComplete(data, textStatus, jqXHR, 'form-success=true');
		};
		
		var onError = function(jqXHR, textStatus, errorThrown)
		{
		    onComplete(data, textStatus, jqXHR, 'form-success=false&form-error=' + jqXHR.responseText);
		};
		
		if(url !== undefined && type !== undefined)
		{
		    var options = {
			
			url: url,
			data: data,
			type: type.toUpperCase(),
			success: onSuccess,
			error: onError
		    };
		    
		    if(isUpload)
		    {
			options['mimeType'] = 'multipart/form-data';
			options['contentType'] = false;
			options['cache'] = false;
			options['processData'] = false;
		    }
		    
		    $.ajax(options);
		}
		else
		{
		    onError(null, 'form method or action was not defined', null);
		}
	    }
	    else
	    {
		$form.find('*[type="submit"]').removeAttr('disabled');
	    }
	    
	    return false;
	});
    },
    
    date: {
	
	convertLocalToDate: function(local, type)
	{
	    switch(type)
	    {
		case 'date':

		    return moment(local, 'L').toDate();
		    
		case 'datetime':

		    return moment(local, 'L LT').toDate();
		    
		case 'time':

		    return moment(local, 'LT').toDate();
	    }
	},
	
	formatDateASLocal: function(date, type)
	{
	    switch(type)
	    {
		case 'date':

		    return moment(date).format('L');
		    
		case 'datetime':

		    return moment(date).format('L LT');
		    
		case 'time':

		    return moment(date).format('LT');
	    }
	},
	
	formatDateAsISO: function(date, type)
	{
	    if(date !== null)
	    {
		// adjust to local timezone date
		var timezoneOffset = -1 * date.getTimezoneOffset();
		
		date = new Date(date.getTime() + timezoneOffset * 60 * 1000);
		
		switch(type)
		{
		    case 'date':

			return date.toISOString().split('T')[0];
			
		    case 'datetime':

			return date.toISOString();
			
		    case 'time':

			return date.toISOString().split('T')[1];
		}
	    }
	}
    }
};

$(function()
{
    Layout.init();
});