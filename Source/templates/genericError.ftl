<#import "default.ftl" as default>
<@default.mainLayout "DawgTrades: Error">
<h1>An error occurred.</h1>
<div class="row">
	<div class="col-md-12 alert alert-danger">
		<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
		${error}
	</div>
</div>
<#if returnTo??>
<div class="row">
	<div class="col-md-12 clearfix">
		<a class="btn btn-primary pull-right margin-bottom" href="${returnTo}">Return</a>
	</div>
</div>
</#if>
</@default.mainLayout>
