<#import "default.ftl" as default>
<#import "categoryListMacro.ftl" as catList>

<@default.mainLayout "DawgTrades: Admin Panel - Create Category">
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
		<!-- create item stuff goes here -->
		
		<div class="form-group">
			<label for="name"> Category Name </label>
			<input type="text" class="form-control" name="name">
		</div>
		<div class="form-group">
			<label for="category"> Parent Category </label>
			<#if categoriesMap??>
			<select id="category" name="category" class="form-control">
				<option value="#" disabled>Select Category</option>
				<option value="0">None (Root-level Category)</option>
				<@catList.categoryList categoryMap=categoriesMap categoryID="0" />
			</select>
			<#else>
			<p> No categories found</p>
			</#if>

		</div>
		<button type="submit" class="btn btn-default"> Create Category</button>
	</form>
</div>
</@default.mainLayout>
