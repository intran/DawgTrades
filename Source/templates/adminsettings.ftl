<#import "default.ftl" as default>
<@default.mainLayout "Admin Panel">
<h1>Admin Panel</h1>
<#if error??>
<div class="row">
    <div class="col-md-12 alert alert-danger" role="alert">
            <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
            <span class="sr-only"> Error: </span>
            ${error}
    </div>
</div>
</#if>
<#include "adminNav.ftl">
	<#if loggedInUser??>
	<p class="lead">Welcome, ${loggedInUser.getFirstName()}</p>
	</#if>
	</div>
</div>
</@default.mainLayout>
