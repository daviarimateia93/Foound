var User = {
    
    __lastAjax: null,
    
    init: function()
    {
	var self = this;
	
	$('.change-password').click(function()
	{
	    if(self.__lastAjax !== null)
	    {
		self.__lastAjax.abort();
	    }
	    
	    self.__lastAjax = $.ajax({
		
		url: ROOT + '/user/changePassword',
		success: function(data)
		{
		    BootstrapDialog.show({
			
			title: i18n.get('USER_CHANGE_PASSWORD_TITLE_SECTION'),
			message: $(i18n.applyOnDocument(data)),
			buttons: [{
			    
			    cssClass: 'btn-primary',
			    label: i18n.get('USER_CHANGE_PASSWORD_EDIT'),
			    action: function(dialog)
			    {
				$('#form-user-change-password').submit();
				
				dialog.close();
			    }
			}, {
			    
			    cssClass: 'btn-danger',
			    label: i18n.get('USER_CHANGE_PASSWORD_CANCEL'),
			    action: function(dialog)
			    {
				dialog.close();
			    }
			}]
		    });
		}
	    });
	});
    }
};

$(function()
{
    User.init();
});