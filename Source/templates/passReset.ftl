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
		<form role="form" action="resetPassword" method="post">
			<div class="form-group">
				<label for="username">Username</label>
				<input type="text" class="form-control" placeholder="Username" name="username" id="username">
			</div>
			<div class="form-group">
				<button type="submit" class="btn btn-primary">Submit</button>
			</div>
		</form>
	</div>
</div>
</@default.mainLayout>
