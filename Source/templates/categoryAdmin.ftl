<#import "default.ftl" as default>
<#import "categoryAdminMacro.ftl" as catAdmin>
<@default.mainLayout "DawgTrades: Admin Panel - Categories">
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
	<#if categoriesMap??>
		<div class="row">
			<div class="col-md-12 clearfix">
				<a class="btn btn-primary pull-right margin-bottom" href="${baseContext}/admin/categories/create"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Create Category</a>
			</div>
		</div>
		<ul class="list-group">
			<@catAdmin.categoryRow categoryMap=categoriesMap categoryID="0" />
		</ul>
	</#if>
	</div>
</div>
</@default.mainLayout>
