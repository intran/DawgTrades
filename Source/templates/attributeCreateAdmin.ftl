<#import "default.ftl" as default>

<@default.mainLayout "DawgTrades: Admin Panel - Create Attribute">
<h1>Category Admin</h1>
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
	<form role="form" action="create" method="POST">
	<input type="hidden" name="categoryID" id="categoryID" value="${categoryID}" />
		<!-- create item stuff goes here -->
		
		<div class="form-group">
			<label for="name"> Attribute Name </label>
			<input type="text" class="form-control" name="name">
		</div>
		<div class="form-group">
			<label for="isString"> Is it a String (if not, it's a number) </label>
			<input type="checkbox" class="form-control" name="isString">

		</div>
		<button type="submit" class="btn btn-default"> Create Attribute</button>
	</form>
</div>
</@default.mainLayout>
