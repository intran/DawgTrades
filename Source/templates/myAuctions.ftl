<#import "default.ftl" as default>
<@default.mainLayout "My Auctions">
<h1> My Auctions </h1>

        <#if error??>
        <div class="row">
                <div class="col-md-12 alert alert-danger" role="alert">
                        <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                        <span class="sr-only">Error:</span>
                        ${error}
                </div>
        </div>
        </#if>


<div class="row">
	<div class="col-md-3">
		<div class="panel panel-default">
			<div class="panel-heading">
				Actions
			</div>
			<div class="list-group">
				<a class="list-group-item" href="createAuction">
                    <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
                    Create Auction
                </a>
			</div>
		</div>
	</div>
	<div class="col-md-9">
		<#if auctions??>
			<div class="list-group">
			<#list items as item>
				<#assign itemId = "${item.getId()}">
				<#if auctions[itemId]??>
					<a href="${baseContext}/auction?id=${auctions[itemId].getId()}" class="list-group-item <#if auctions[itemId].getIsClosed()>list-group-item-danger<#else>list-group-item-success</#if>">${item.getName()} (<#if auctions[itemId].getIsClosed()>Closed<#else>Open</#if>)</a>
				</#if>
			</#list>
			</div>
		<#else>
			<p class="lead lead-no-bottom-margin text-center">You have no auctions.</p>
		</#if>
	</div>
</div>
</@default.mainLayout>
