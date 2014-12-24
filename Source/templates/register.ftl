<#import "default.ftl" as default>
<@default.mainLayout "Register">
<h1>Register for DawgTrades</h1>
<#if error??>
        <div class="col-md-12 alert alert-danger" role="alert">
                <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                <span class="sr-only"> Error: </span>
                ${error}
        </div>
</#if>
<div class="row">
	<form role="form" action="register" method="post">
		<div classs="form-group">
			<label for="name">Username</label>
			<input type="text" class="form-control" placeholder="Username" name="username">
		</div>
                <div classs="form-group">
                        <label for="name">First Name</label>
                        <input type="text" class="form-control" placeholder="First Name" name="fname">
                </div>
                <div classs="form-group">
                        <label for="name">Last Name</label>
                        <input type="text" class="form-control" placeholder="Last Name" name="lname">
                </div>
		<div classs="form-group">
                        <label for="email">Email</label>
                        <input type="email" class="form-control" placeholder="Email" name="email">
                </div>
                <div classs="form-group">
                        <label for="phone">Phone Number</label>
                        <input type="tel" class="form-control" placeholder="Phone Number" name="phone">
                </div>
                <div classs="form-group">
                        <label for="texts">Phone Accepts Texts?</label>
                        <input type="checkbox" class="form-control" name="texts">
                </div>
		<div classs="form-group">
                        <label for="password">Enter a password</label>
                        <input type="password" class="form-control" placeholder="Password" name="password">
                </div>
                <div classs="form-group">
                        <label for="passwordReEnter">Re-enter password</label>
                        <input type="password" class="form-control" placeholder="Re-enter Password" name="passwordRe">
                </div>
                <div class="form-group">
		<button type="submit" class="btn btn-default">Submit</button>
                </div>

	</form>
</div>
</@default.mainLayout>
