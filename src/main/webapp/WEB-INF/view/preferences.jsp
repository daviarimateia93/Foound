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
    <li>
    	<a href="${__contextPath__}/i18n?language=es" class="language">
    		<img src="${__contextPath__}/img/flag-es.png" width="20" alt="es" title="es">
    		<span>es</span>
    	</a>
    </li>
</ul>