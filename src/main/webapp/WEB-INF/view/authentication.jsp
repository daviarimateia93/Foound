<link href="${__contextPath__}/css/authentication.css" rel="stylesheet">

<script>var PUBLIC_PAGE = true;</script>

<div class="container auth">
	<form method="post" action="${__contextPath__}/authentication/login" class="form-authentication" role="form">
		<h1 class="form-authentication-heading">
			<span>
				<b>Foo</b>un<b>d</b>
			</span>
			<div class="btn-group">
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
			<div class="clearfix"></div>
		</h1>
		<input type="text" name="nickname" class="i18n form-control" i18n-key="AUTHENTICATION_USER" i18n-render="placeholder" required autofocus />
        <input type="password" name="password" class="i18n form-control" i18n-key="AUTHENTICATION_PASSWORD" i18n-render="placeholder" required />
        <button class="i18n btn btn-lg btn-primary btn-block" type="submit" i18n-key="AUTHENTICATION_ENTER"></button>
		<a class="i18n" href="${__contextPath__}/join" i18n-key="AUTHENTICATION_SIGN_UP"></a>
	</form>
</div>