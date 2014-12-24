<#import "default.ftl" as default>
<@default.mainLayout "DawgTrades - Login">
<h1>Login to DawgTrades</h1>
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
	<div class="col-md-12 alert alert-info">
		<span class="glyphicon glyphicon-question-sign" aria-hidden="true"></span>
		Need to register? <a href="register">Click here!</a>
	</div>
</div>
<div class="row">
	<div class="col-md-12">
		<form role="form" action="login" method="post">
			<div class="form-group">
				<label for="username">Username</label>
				<input type="text" class="form-control" placeholder="Username" name="username" id="username">
			</div>
			<div class="form-group">
	                <label for="password">Password</label>
	                <input type="password" class="form-control" placeholder="Password" id="password" name="password">
			</div>
			<div class="form-group">
				<button type="submit" class="btn btn-primary">Submit</button>
			</div>
		</form>
	</div>
</div>
</@default.mainLayout>
