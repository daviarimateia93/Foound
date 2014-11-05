//Created by Davi de Sousa Arimateia, daviarimateia93@gmail.com

var jshtml = {
    
    data: 'data',
    
    render: function(jshtml__source, jshtml__data)
    {
	if(jshtml__source === null)
	{
	    return null;
	}
	
	eval('var ' + this.data + ' = jshtml__data');
	
	var jshtml__js = '', jshtml__cache = '', jshtml__HTML = '';
	
	for(var jshtml__i = 0, jshtml__len = jshtml__source.length; jshtml__i < jshtml__len; jshtml__i++)
	{
	    var jshtml__tryCacheAddition = false;
	    
	    if(jshtml__source[jshtml__i] === '#')
	    {
		if(jshtml__source[jshtml__i + 1] === '#')
		{
		    var jshtml__isPrint = (jshtml__source[jshtml__i + 2] === '@');
		    
		    jshtml__i += jshtml__isPrint ? 3 : 2;
		    
		    var jshtml__JSHTML = '';
		    
		    while(jshtml__source[jshtml__i] !== '#' || jshtml__source[jshtml__i + 1] !== '#')
		    {
			jshtml__JSHTML += jshtml__source[jshtml__i++];
		    }
		    
		    jshtml__i += 1;
		    
		    jshtml__js += (jshtml__cache.trim().length > 0 ? 'jshtml__HTML += \'' + jshtml__cache.replace(/\'/g, '\\\'') + '\';' : '') + (jshtml__isPrint ? 'jshtml__HTML += ' + jshtml__JSHTML + ';' : jshtml__JSHTML);
		    
		    jshtml__cache = '';
		}
		else
		{
		    jshtml__tryCacheAddition = true;
		}
	    }
	    else
	    {
		jshtml__tryCacheAddition = true;
	    }
	    
	    if(!/[\n\r]/.test(jshtml__source[jshtml__i]) && jshtml__tryCacheAddition)
	    {
		jshtml__cache += jshtml__source[jshtml__i];
	    }
	}
	
	jshtml__js += (jshtml__cache.trim().length > 0 ? 'jshtml__HTML += \'' + jshtml__cache.replace(/\'/g, '\\\'') + '\';' : '');
	
	try
	{
	    eval(jshtml__js);
	}
	catch(jshtml__exception)
	{
	    console.log(jshtml__js);
	    throw jshtml__exception;
	}
	
	return jshtml__HTML.length > 0 ? jshtml__HTML : null;
    },
    
    remoteRender: function(jshtml__url, jshtml__data)
    {
	var jshtml__source = null;
	
	$.ajax({
	    url: jshtml__url,
	    async: false,
	    success: function(jshtml__ajaxData)
	    {
		jshtml__source = jshtml__ajaxData;
	    }
	});
	
	return this.render(jshtml__source, jshtml__data);
    }
};