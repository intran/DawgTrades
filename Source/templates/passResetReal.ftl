<#import "default.ftl" as default>
<@default.mainLayout "DawgTrades - Password Reset">
<h1>Password Reset</h1>
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
		<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
		<span class="sr-only">Message:</span>
		${message}
	</div>
</div>
</#if>
<div class="row">
	<div class="col-md-12">
		<form role="form" action="doReset" method="post">
			<input type="hidden" name="id" value="${id}" />
			<input type="hidden" name="key" value="${key}" />
			<div class="form-group">
				<label for="password">New Password</label>
				<input type="password" class="form-control" placeholder="Password" name="password" id="password">
			</div>
			<div class="form-group">
				<label for="passwordRepeat">Re-enter New Password</label>
				<input type="password" class="form-control" placeholder="Password" name="passwordRepeat" id="passwordRepeat">
			</div>
			<div class="form-group">
				<button type="submit" class="btn btn-primary">Submit</button>
			</div>
		</form>
	</div>
</div>
</@default.mainLayout>
