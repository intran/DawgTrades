<#import "default.ftl" as default>
<@default.mainLayout "DawgTrades: Admin Panel - Categories">
<h1>Delete Attribute</h1>
<#if error??>
<div class="row">
	<div class="col-md-12 alert alert-danger" role="alert">
		<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
		<span class="sr-only">Error:</span>
		${error}
	</div>
</div>
</#if>

<#if message??>
<div class="row">
	<div class="col-md-12 alert alert-success" role="alert">
		<span class="glyphicon glyphicon-question-sign" aria-hidden="true"></span>
		<span class="sr-only">Message:</span>
		${message}
	</div>
</div>
</#if>
<#include "adminNav.ftl">
		<p>Are you sure you want to delete this attribute?</p>
		<form role="form" action="delete" method="post" class="clearfix">
			<input type="hidden" name="id" id="id" value="${toDelete}" />
			<div class="form-group pull-right">
				<a class="btn btn-default" href="${baseContext}/admin/categories">Cancel</a> <button type="submit" class="btn btn-danger">Confirm</button>
			</div>
		</form>
	</div>
</div>
</@default.mainLayout>
