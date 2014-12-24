<#import "default.ftl" as default>
<#import "categoryAdminMacro.ftl" as catAdmin>
<@default.mainLayout "DawgTrades: Admin Panel - Categories">
<h1>Unapproved Users</h1>
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
		<ul class="list-group">
			<#if unapprovedUsers?size gt 0>
				<#list unapprovedUsers as user>
					<li class="list-group-item clearfix"><span class="btn">${user.getFirstName()} ${user.getLastName()} (Username: ${user.getName()})</span><a href="approve?id=${user.getId()}" class="btn btn-success pull-right">Approve</a></li>
				</#list>
			<#else>
				<p class="lead lead-no-bottom-margin text-center">No users awaiting approval!</p>
			</#if>
		</ul>
	</div>
</div>
</@default.mainLayout>
