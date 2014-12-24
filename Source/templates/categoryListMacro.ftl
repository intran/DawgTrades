<#macro categoryList categoryMap categoryID="0" indent=0 myID=-1 selected="">
<#list categoryMap[categoryID] as category>
	<#if category.getId() != myID>
		<option value="${category.getId()}"<#if selected == "${category.getId()}"> selected</#if>><#if indent gt 0><#list 1..indent as i>--</#list> </#if>${category.getName()}</option>
		<#assign stringID="${category.getId()}">
		<#if categoryMap[stringID]??>
				<@categoryList categoryMap=categoryMap categoryID=stringID indent=indent+1 selected=selected />
		</#if>
	</#if>
</#list>
</#macro>