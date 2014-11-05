var Join = {
    
    __auxHTML: null,
    
    init: function()
    {
	var self = this;
	
	this.__saveAndRemoveHiddenForm();
	
	$('input[name="want-to-be"]').change(function()
	{
	    var value = $(this).val();
	    
	    switch(value)
	    {
		case 'client':

		    $('.join.container .content').append(self.__auxHTML);
		    
		    $('#form-join-establishment').addClass('hide');
		    
		    self.__saveAndRemoveHiddenForm();
		    
		    break;
		
		case 'establishment':

		    $('.join.container .content').append(self.__auxHTML);
		    
		    $('#form-join-client').addClass('hide');
		    
		    self.__saveAndRemoveHiddenForm();
		    
		    break;
	    }
	});
    },
    
    __saveAndRemoveHiddenForm: function()
    {
	this.__auxHTML = $('<div></div>').append($('form.hide').removeClass('hide')).html();
	
	$('form.hide').remove();
    }
};

$(function()
{
    Join.init();
});