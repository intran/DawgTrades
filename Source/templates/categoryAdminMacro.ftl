<#macro categoryRow categoryMap categoryID="0" indent=0>
<#list categoryMap[categoryID] as category>
	<#assign stringID="${category.getId()}">
	<li class="list-group-item category-list-item">
		<div class="list-group-item-heading clearfix">
			<a href="${baseContext}/admin/categories/edit?id=${category.getId()}" class="btn btn-link">${category.getName()}</a>
			<a class="btn btn-danger pull-right" href="${baseContext}/admin/categories/delete?id=${category.getId()}"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span> Delete</a>
		</div>
	<#if categoryMap[stringID]??>
		<ul class="list-group list-group-item-text">
			<@categoryRow categoryMap=categoryMap categoryID=stringID />
		</ul>
	</#if>
	</li>
</#list>
</#macro>