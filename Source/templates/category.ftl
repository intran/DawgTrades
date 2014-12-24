<#import "default.ftl" as default>
<#if specificCategory??>
	<#assign title = "DawgTrades - Browse: ${specificCategory.getName()}">
<#else>
	<#assign title = "DawgTrades - Browse">
</#if>
<@default.mainLayout title>
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
<#if specificCategory??>
	<h1>Browsing ${specificCategory.getName()}</h1>
<#else>
	<h1>Browsing all categories</h1>
</#if>
<div class="row">
	<div class="col-md-3">
		<#if specificCategory??>
		<div class="panel panel-default">
		  <div class="panel-heading">
		  	Category Navigation
		  </div>
		  <div class="list-group">
		  	<a href="category?id=${specificCategory.getParentId()}" class="list-group-item"><span class="glyphicon glyphicon-arrow-up" aria-hidden="true"></span> Go to parent category</a>
		  </div>
		</div>
		</#if>
		<div class="panel panel-default">
		  <div class="panel-heading">
			<#if specificCategory??>
				Subcategories
			<#else>
				Categories
			</#if>
			</div>
			<#if subCategories??>
				<div class="list-group">
					<#list subCategories as subCategory>
						<a href="category?id=${subCategory.getId()}" class="list-group-item">
							<#assign idx = subCategories?seq_index_of(subCategory)>
							<#if subCategoryCounts[idx] gte 0><span class="badge">${subCategoryCounts[idx]}</span></#if>
							${subCategory.getName()}
						</a>
					</#list>
				</div>
			<#else>
				<ul class="list-group">
    				<li class="list-group-item">No <#if specificCategory??>sub</#if>categories.</li>
				</ul>
			</#if>
		</div>
	</div>
	<div class="col-md-9">
		<div class="panel panel-default">
		  <div class="panel-heading">
		  	Auctions
		  </div>
		  <div class="panel-body">
			<#if categoryAuctions??>
				<#list categoryAuctions?chunk(3) as auctionRow>
					<div class="row">
						<#list auctionRow as auction>
						<#assign auctionID = "${auction.getId()}">
							<div class="col-md-4">
								<h5><a href="${baseContext}/auction?id=${auction.getId()}">${categoryItems[auctionID].getName()}</a></h5>
								<p>Ends on ${auction.getExpiration()?datetime}</p>
								<p>Minimum bid: $${auction.getMinPrice()}</p>
								<p>Current bid: <#if auctionBids[auctionID]??>$${auctionBids[auctionID].getAmount()}<#else>None</#if></p>
							</div>
						</#list>
					</div>
				</#list>
			<#else>
				<p class="lead lead-no-bottom-margin text-center">No open auctions found.</p>
			</#if>
		  </div>
	</div>
</div>
</@default.mainLayout>