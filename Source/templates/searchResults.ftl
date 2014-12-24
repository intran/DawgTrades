<#import "default.ftl" as default>
<@default.mainLayout "DawgTrades - Search Results">
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
<h1>Search Results in ${searchCategory.getName()}</h1>
<div class="row">
	<div class="col-md-3">
		<div class="panel panel-default">
		  <div class="panel-heading">
				New Search
			</div>
			<form role="form" action="search" method="GET" class="panel-body">
				<input type="hidden" name="searchCategory" value="${searchCategory.getId()}" />
				<div class="form-group">
					<label for="name">Name Contains</label>
					<div class="input-group input-group">
						<input type="text" class="form-control" name="name"<#if currentName??> value="${currentName}"</#if> />
					</div>
				</div>
				<div class="form-group">
					<label for="description">Description Contains</label>
					<div class="input-group input-group">
						<input type="text" class="form-control" name="description"<#if currentDescription??> value="${currentDescription}"</#if> />
					</div>
				</div>
				<#list attributeTypes as attributeType>
					<#assign attrFormName = "attr_${attributeType.getId()}">
					<#assign attrFormNameComp = "attr_${attributeType.getId()}_comparison">

					<#if attributeType.getIsString()>
						<div class="form-group">
							<label for="${attrFormName}">${attributeType.getName()} Contains</label>
							<div class="input-group input-group">
								<input type="text" class="form-control" name="${attrFormName}"<#if currentValues[attrFormName]??> value="${currentValues[attrFormName]}"</#if> />
							</div>
						</div>
					<#else>
						<div class="form-group">
							<label for="${attrFormName}">${attributeType.getName()}</label>
							<div class="input-group input-group">
								<select class="form-control" name="${attrFormName}_comparison">
									<option value="lt"<#if currentValues[attrFormNameComp]?? && currentValues[attrFormNameComp] == "lt"> selected</#if>>Less Than</option>
									<option value="lte"<#if currentValues[attrFormNameComp]?? && currentValues[attrFormNameComp] == "lte"> selected</#if>>Less Than Or Equal To</option>
									<option value="eq"<#if currentValues[attrFormNameComp]?? && currentValues[attrFormNameComp] == "eq"> selected</#if>>Equal To</option>
									<option value="neq"<#if currentValues[attrFormNameComp]?? && currentValues[attrFormNameComp] == "neq"> selected</#if>>Not Equal To</option>
									<option value="gte"<#if currentValues[attrFormNameComp]?? && currentValues[attrFormNameComp] == "gte"> selected</#if>>Greater Than Or Equal To</option>
									<option value="gt"<#if currentValues[attrFormNameComp]?? && currentValues[attrFormNameComp] == "gt"> selected</#if>>Greater Than</option>
								</select>
								<input type="text" class="form-control" name="${attrFormName}"<#if currentValues[attrFormName]??> value="${currentValues[attrFormName]}"</#if> />
							</div>
						</div>
					</#if>
				</#list>
				<div class="form-group">
					<button type="submit" class="btn btn-primary">Search</button>
				</div>
			</form>
		</div>
	</div>
	<div class="col-md-9">
		<div class="panel panel-default">
		  <div class="panel-heading">
		  	Auctions
		  </div>
		  <div class="panel-body">
			<#if searchResults?size gt 0>
				<#list searchResults?chunk(3) as auctionRow>
					<div class="row">
						<#list auctionRow as auction>
						<#assign auctionID = "${auction.getId()}">
							<div class="col-md-4">
								<h5><a href="${baseContext}/auction?id=${auction.getId()}">${itemsForAuctions[auctionID].getName()}</a></h5>
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