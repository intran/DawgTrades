<#import "default.ftl" as default>
<@default.mainLayout "DawgTrades - Searching: ${searchCategory.getName()}">
<h1>Searching ${searchCategory.getName()}</h1>
<div class="row">
	<div class="col-md-12">
		<form role="form" action="search" method="GET">
			<input type="hidden" name="searchCategory" value="${searchCategory.getId()}" />
			<div class="form-group">
				<label for="name">Name Contains</label>
				<div class="input-group input-group">
					<input type="text" class="form-control" name="name">
				</div>
			</div>
			<div class="form-group">
				<label for="description">Description Contains</label>
				<div class="input-group input-group">
					<input type="text" class="form-control" name="description">
				</div>
			</div>
			<#list attributeTypes as attributeType>
				<#assign attrFormName = "attr_${attributeType.getId()}">

				<#if attributeType.getIsString()>
					<div class="form-group">
						<label for="${attrFormName}">${attributeType.getName()} Contains</label>
						<div class="input-group input-group">
							<input type="text" class="form-control" name="${attrFormName}">
						</div>
					</div>
				<#else>
					<div class="form-group">
						<label for="${attrFormName}">${attributeType.getName()}</label>
						<div class="input-group input-group">
							<select class="form-control" name="${attrFormName}_comparison">
								<option value="lt">Less Than</option>
								<option value="lte">Less Than Or Equal To</option>
								<option value="eq">Equal To</option>
								<option value="eq">Not Equal To</option>
								<option value="gte">Greater Than Or Equal To</option>
								<option value="gt">Greater Than</option>
							</select>
							<input type="text" class="form-control" name="${attrFormName}">
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
</@default.mainLayout>
