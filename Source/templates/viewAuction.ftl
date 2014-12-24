<#import "default.ftl" as default>
<@default.mainLayout "DawgTrades - View Auction: ${item.getName()}">
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
<h1>${item.getName()}</h1>
<div class="row">
	<div class="col-md-3">
		<#if loggedInUser?? && (loggedInUser.getId() == owner.getId() || loggedInUser.getIsAdmin())>
			<div class="panel panel-default">
				<div class="panel-heading">
					Manage Auction
				</div>
				<div class="list-group">
					<a class="list-group-item list-group-item-danger" href="auction/delete?id=${auction.getId()}">
						Delete Auction
					</a>
				</div>
			</div>
		</#if>
		<div class="panel panel-default">
			<div class="panel-heading">
				Auction Information
			</div>
			<ul class="list-group">
				<li class="list-group-item">
					<h4 class="list-group-item-heading">Ends on:</h4>
					<p class="list-group-item-text">${auction.getExpiration()?datetime}</p>
				</li>
				<li class="list-group-item">
					<h4 class="list-group-item-heading">Minimum Bid:</h4>
					<p class="list-group-item-text">$${auction.getMinPrice()}</p>
				</li>
				<li class="list-group-item">
					<h4 class="list-group-item-heading">Owner:</h4>
					<p class="list-group-item-text"><a href="/user?id=${owner.getId()}">${owner.getFirstName()} ${owner.getLastName()}</a></p>
				</li>
				<#if winner??>
					<li class="list-group-item">
						<h4 class="list-group-item-heading">Winner:</h4>
						<p class="list-group-item-text"><a href="/user?id=${winner.getId()}">${winner.getFirstName()} ${winner.getLastName()}</a></p>
					</li>
				</#if>
			</ul>
		</div>
		<div class="panel panel-default">
			<div class="panel-heading">
				Latest Bids
			</div>
		  	<#if bids?size gt 0>
		  		<ul class="list-group">
		  			<#list bids?chunk(5)?first as bid>
		  				<li class="list-group-item">$${bid.getAmount()} on ${bid.getDate()?datetime}</li>
		  			</#list>
		  		</ul>
	  		<#else>
				<ul class="list-group">
					<li class="list-group-item">No bids have been placed.</li>
				</ul>
		  	</#if>
	  	</div>
	  	<#if loggedInUser?? && loggedInUser.getId() != owner.getId() && !auction.getIsClosed()>
			<div class="panel panel-default">
				<div class="panel-heading">
					Place a bid
				</div>
				<form class="panel-body" role="form" action="bid" method="POST">
					<input type="hidden" name="auctionID" value="${auction.getId()}" />
					<div class="form-group">
						<label class="control-label" for="amount"> Amount </label>
						<div class="input-group input-group-lg">
							<span class="input-group-addon">$</span>
							<input type="text" class="form-control" name="amount">
						</div>
					</div>
					<div class="clearfix">
						<button type="submit" class="btn btn-success pull-right"> Place Bid</button>
					</div>
				</form>
			</div>
	  	</#if>
	</div>
	<div class="col-md-9">
		<h3>Category</h3>
		<p><a href="/category?id=${category.getId()}">${category.getName()}</a></p>
		<h3>Description</h3>
		<p>${item.getDescription()}</p>
		<h3>Attributes</h3>
		<table class="table">
			<thead>
				<tr>
					<th>Attribute</th>
					<th>Value</th>
				</tr>
			</thead>
			<tbody>
				<#list attributeTypes as attType>
				<#assign attID = "${attType.getId()}">
					<tr>
						<td>${attType.getName()}</td>
						<td><#if attributeForType[attID]??>${attributeForType[attID].getValue()}<#else><em>None</em></#if></td>
					</tr>
				</#list>
			</tbody>
		</table>
	</div>
</div>
</@default.mainLayout>