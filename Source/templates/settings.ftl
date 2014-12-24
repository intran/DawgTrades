<#import "default.ftl" as default>
<@default.mainLayout "Settings">
<h1>Account Settings</h1>
<#if error??>
        <div class="col-md-12 alert alert-danger" role="alert">
                <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                <span class="sr-only"> Error: </span>
                ${error}
        </div>
</#if>
<#include "sideNav.ftl">
	<#if loggedInUser??>
	<#if profile??>
	<label> Username: </label><p> ${loggedInUser.getName()}</p>
	<form role="form" id="settingsForm" action="settings" method="post">
		<div class="form-group">
			<label> First Name:</label>
			<input type="text" class="form-control" name="fname" placeholder="${loggedInUser.getFirstName()}">
		</div>		
		   <div class="form-group">
                        <label> Last Name:</label>
                        <input type="text" class="form-control" name="lname" placeholder="${loggedInUser.getLastName()}">
                </div>
		<div class ="form-group">
			<label> Phone Number:</label>
			<input type="text" class="form-control" name="lname" placeholder="${loggedInUser.getPhone()}">
		</div>	
		<div class="form-group>
			<label> <input type="checkbox">Recieve Text Messages</input> </label>
		</div>

		<button type="submit" class="btn btn-default">Update Profile </button>
		</form>
		<form role="form" id="deleteAccountForm" action="deleteAccount" method="get">
		<button type="submit" class="btn btn-danger"> Delete Account </button>
		</form>

	<#elseif password??>
	<form role="form" id="settingsForm" action="settings" method="post">
                <div classs="form-group">
                        <label for="password">Enter a new password</label>
                        <input type="password" class="form-control" placeholder="Password" name="password">
                </div>
                <div classs="form-group">
                        <label for="passwordReEnter">Re-enter password</label>
                        <input type="password" class="form-control" placeholder="Re-enter Password" name="passwordRe">
                </div>
		<button type="submit" class="btn btn-default">Update Password </button>
	</form>
	
	</#if>
	</#if>
	</div>
</div>
</@default.mainLayout>
