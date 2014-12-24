<#import "default.ftl" as default>
<#import "categoryListMacro.ftl" as catList>

<@default.mainLayout "DawgTrades: Admin Panel - Create Category">
<h1>Edit Category</h1>
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
	<form role="form" action="edit" method="POST">
	<input type="hidden" name="id" id="id" value="${myID}" />
		
		<div class="form-group">
			<label for="name"> Attribute Name </label>
			<input type="text" class="form-control" name="name" value="${name}">
		</div>
		<div class="form-group">
			<label for="parent"> Category </label>
			<p>${parent}</p>
		</div>
		<div class="form-group">
			<label for="type"> Type </label>
			<p><#if isString>String<#else>Number</#if></p>
		</div>
		<div class="clearfix">
			<button type="submit" class="btn btn-primary pull-right">Save</button>
		</div>
	</form>
</div>
</@default.mainLayout>
