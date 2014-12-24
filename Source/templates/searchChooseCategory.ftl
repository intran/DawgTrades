<#import "default.ftl" as default>
<#import "categoryListMacro.ftl" as catList>
<@default.mainLayout "DawgTrades - Search">
<h1>Search</h1>
<div class="row">
	<div class="col-md-12">
		<form role="form" action="search" method="GET">
			<div class="form-group">
				<label for="category">Category to Search</label>
				<select id="category" name="category" class="form-control">
					<option value="#" disabled>Select Category</option>
					<option value="0">None (Search All Categories)</option>
					<@catList.categoryList categoryMap=categoriesMap />
				</select>
			</div>
			<div class="form-group">
				<button type="submit" class="btn btn-primary">Next</button>
			</div>
		</form>
	</div>
</div>
</@default.mainLayout>
