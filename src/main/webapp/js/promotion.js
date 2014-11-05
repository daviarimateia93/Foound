var Promotion = {
    
    init: function()
    {
	$('input[name="avatar"]').on('change', function()
	{
	    $(this).parents('form').submit();
	});
	
	if(VIEW_MODE === true)
	{
	    $('textarea[name="description"]').elastic();
	}
    }
};

$(function()
{
    Promotion.init();
});