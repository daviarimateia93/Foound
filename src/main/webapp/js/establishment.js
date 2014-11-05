var Establishment = {
    
    __latitude: null,
    __longitude: null,
    
    init: function()
    {
	var self = this;
	
	if(VIEW_MODE === true)
	{
	    $('textarea[name="address"]').elastic();
	}
	
	$('textarea[name="address"]').parents('form').on('ajaxsubmit', function(e, action)
	{
	    if(self.__latitude == null || self.__longitude == null)
	    {
		action.stop = true;
		
		self.matchAddress($('textarea[name="address"]').val());
	    }
	});
	
	$('input[name="avatar"]').on('change', function()
	{
	    $(this).parents('form').submit();
	});
	
	$('#btn-establishment-preferences').click(function()
	{
	    $('.page-establishment-preferences').removeClass('hide');
	});
	
	$('.page-establishment-preferences .btn-danger').click(function()
	{
	    $('.page-establishment-preferences').addClass('hide');
	});
    },
    
    matchAddress: function(address)
    {
	var self = this;
	var $matching = $('#establishment-address-matching');
	var $textarea = $('textarea[name="address"]');
	var $form = $textarea.parents('form');
	
	$form.attr('disabled', '');
	
	$matching.removeClass('hide');
	
	$.ajax({
	    
	    url: encodeURI(ROOT + '/google?address=' + address + '&language=pt-BR'),
	    dataType: 'JSON',
	    success: function(data)
	    {
		if(data.status === 'OK')
		{
		    if(data.results.length > 0)
		    {
			var html = '';
			html += '<ul class="bootstrap-dialog-selector">';
			
			for(var i = 0, len = data.results.length; i < len; i++)
			{
			    html += '<li>' + data.results[i].formatted_address + '</li>';
			}
			
			html += '</ul>';
			
			var selected = false;
			
			BootstrapDialog.show({
			    
			    title: i18n.get('ESTABLISHMENT_ADDRESS_MODAL_VALIDATION_SUCCESS'),
			    message: $(html),
			    buttons: [{
				cssClass: 'btn-primary',
				label: i18n.get('ESTABLISHMENT_ADDRESS_MODAL_VALIDATION_SELECT'),
				action: function(dialog)
				{
				    if($('.bootstrap-dialog-selector .active').length > 0)
				    {
					selected = true;
					
					$textarea.val($('.bootstrap-dialog-selector .active').text());
					
					$form.removeAttr('disabled');
					
					self.__latitude = data.results[0].geometry.location.lat;
					self.__longitude = data.results[0].geometry.location.lng;
					
					$('input[name="latitude"]').val(self.__latitude);
					$('input[name="longitude"]').val(self.__longitude);
					
					$form.submit();
				    }
				}
			    }, {
				cssClass: 'btn-danger',
				label: i18n.get('ESTABLISHMENT_ADDRESS_MODAL_VALIDATION_CANCEL'),
				action: function(dialog)
				{
				    cancelled = true;
				    
				    dialog.close();
				}
			    }],
			    onhide: function(dialog)
			    {
				if(selected === false)
				{
				    self.__latitude = null;
				    self.__longitude = null;
				    
				    $matching.addClass('hide');
				}
			    }
			});
		    }
		}
		else
		{
		    self.__latitude = null;
		    self.__longitude = null;
		    
		    BootstrapDialog.show({
			
			title: i18n.get('ESTABLISHMENT_ADDRESS_MODAL_VALIDATION_ERROR'),
			message: i18n.get('ESTABLISHMENT_ADDRESS_MODAL_VALIDATION_ERROR_CONTENT'),
			buttons: [{
			    cssClass: 'btn-primary',
			    label: i18n.get('ESTABLISHMENT_ADDRESS_MODAL_VALIDATION_OK'),
			    action: function(dialog)
			    {
				dialog.close();
			    }
			}],
			onhide: function(dialog)
			{
			    $form.removeAttr('disabled');
			    
			    $matching.addClass('hide');
			}
		    });
		}
	    }
	});
	
	// https://developers.google.com/maps/documentation/geocoding/?hl=pt-BR
	// http://maps.googleapis.com/maps/api/geocode/json?address=rua+francisco+marinho+de+gusmao+163
	// http://maps.googleapis.com/maps/api/geocode/json?address=asdfafsdafsd
	// http://maps.googleapis.com/maps/api/geocode/json?address=rua+francisco+marinho+de+gusmao
    }
};

$(function()
{
    Establishment.init();
});