<#import "default.ftl" as default>
<@default.mainLayout "Delete Account">
<h1>Delete My Account</h1>
<#if error??>
        <div class="col-md-12 alert alert-danger" role="alert">
                <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                <span class="sr-only"> Error: </span>
                ${error}
        </div>
</#if>
<div class="row">

	<form role="form" action="deleteAccount" method="post">

	
		<div class="form-group">
				<label for="confirm"> Are you sure you want to delete your account?</label>
                        <div class="radio">
				<label>	
					<input type="radio" name="confirm" value="yes"> yes
				</label>
			</div>
                        <div class="radio">
                                <label>
                                        <input type="radio" name="confirm" value="no"> no
                                </label>
                        </div>

		</div>
		<div class="form-group">
			<button type="submit" class="btn btn-default"> Confirm </button>
		</div>

	</form>
</div>
</@default.mainLayout>

