<#import "default.ftl" as default>
<#import "categoryListMacro.ftl" as catList>

<@default.mainLayout "DawgTrades: Admin Panel - Create Category">
<h1>Edit Category</h1>
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
	<form role="form" action="edit" method="POST">
	<input type="hidden" name="id" id="id" value="${myID}" />
		<!-- create item stuff goes here -->
		
		<div class="form-group">
			<label for="name"> Category Name </label>
			<input type="text" class="form-control" name="name" value="${toEdit.getName()}">
		</div>
		<div class="form-group">
			<label for="parent"> Parent Category </label>
			<p>${parent}</p>
		</div>
		<div class="form-group clearfix">
			<div class="panel panel-default">
				<div class="panel-heading">
					Attributes
				</div>
				<#if attributes??>
					<ul class="list-group">
						<#list attributes as attribute>
							<li class="list-group-item">
								<a href="${baseContext}/admin/attributes/edit?id=${attribute.getId()}" class="btn btn-link">${attribute.getName()} (<#if attribute.getIsString()>String<#else>Number</#if>)</a>
								<a class="btn btn-danger pull-right" href="${baseContext}/admin/attributes/delete?id=${attribute.getId()}"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span> Delete</a>
							</li>
						</#list>
					</ul>
				<#else>
					<ul class="list-group">
	    				<li class="list-group-item">No attributes.</li>
					</ul>
				</#if>
			</div>
			<div class="pull-right"><a class="btn btn-primary" href="${baseContext}/admin/attributes/create?categoryID=${myID}">Add Attribute</a></div>
		</div>
		<div class="clearfix">
			<button type="submit" class="btn btn-primary pull-right">Save</button>
		</div>
	</form>
</div>
</@default.mainLayout>
