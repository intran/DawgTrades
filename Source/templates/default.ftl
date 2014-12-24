<#macro mainLayout title="DawgTrades">
	<!doctype html>
	<html class="no-js" lang="">
		<head>
			<meta charset="utf-8">
			<meta http-equiv="X-UA-Compatible" content="IE=edge">
			<title>${title}</title>
			<meta name="description" content="">
			<meta name="viewport" content="width=device-width, initial-scale=1">
			<!-- Bootstrap -->
			<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">
			<!-- Font-Awesome -->
			<link rel="stylesheet" href="//netdna.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css">
			<link rel="stylesheet" href="${baseContext}/resources/css/custom.css">
			
			<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
			<!--[if lt IE 9]>
				<script src="//cdnjs.cloudflare.com/ajax/libs/html5shiv/3.7.2/html5shiv.min.js"></script>
				<script src="//cdnjs.cloudflare.com/ajax/libs/respond.js/1.4.2/respond.min.js"></script>
			<![endif]-->
			<script src="//cdnjs.cloudflare.com/ajax/libs/modernizr/2.8.3/modernizr.min.js"></script>
		</head>
		<body>
			<nav class="navbar navbar-default navbar-static-top navbar-inverse" role="navigation">
			  <div class="container-fluid">
			    <!-- Brand and toggle get grouped for better mobile display -->
			    <div class="navbar-header">
			      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
			        <span class="sr-only">Toggle navigation</span>
			        <span class="icon-bar"></span>
			        <span class="icon-bar"></span>
			        <span class="icon-bar"></span>
			      </button>
			      <a class="navbar-brand" href="${baseContext}/">DawgTrades</a>
			    </div>

			    <!-- Collect the nav links, forms, and other content for toggling -->
			    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
			      <ul class="nav navbar-nav">
			        <li><a href="${baseContext}/category">Browse</a></li>
			        <li><a href="${baseContext}/search">Search</a></li>
			      </ul>
			      <ul class="nav navbar-nav navbar-right">
			        <#if loggedInUser??>
			        <li class="dropdown">
			          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">${loggedInUser.getFirstName()} <span class="caret"></span></a>
			          <ul class="dropdown-menu" role="menu">
			            <li><a href="${baseContext}/settings">Account Settings</a></li>
			            <li><a href="${baseContext}/myAuctions">My Auctions</a></li>
			            <li class="divider"></li>
			            <#if loggedInUser.getIsAdmin()>
			            <li><a href="${baseContext}/admin">Admin Panel</a></li>
			            <li class="divider"></li>
			            </#if>
			            <li><a href="${baseContext}/logout">Log Out</a></li>
			          </ul>
			        </li>
			        <#else>
			        <li><a href="${baseContext}/login">Login</a></li>
			        </#if>
			      </ul>
			    </div><!-- /.navbar-collapse -->
			  </div><!-- /.container-fluid -->
			</nav>
			<div class="container">
				<#nested/>
			</div>
			
			<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
			<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
			<script>window.jQuery || document.write('<script src="//ajax.aspnetcdn.com/ajax/jQuery/jquery-1.11.1.min.js"><\/script>')</script>
			<!-- Include all compiled plugins (below), or include individual files as needed -->
			<script src="//netdna.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>
			
		        <!-- Google Analytics: change UA-XXXXX-X to be your site's ID. -->
		        <script>
		            (function(b,o,i,l,e,r){b.GoogleAnalyticsObject=l;b[l]||(b[l]=function(){(b[l].q=b[l].q||[]).push(arguments)});b[l].l=+new Date;e=o.createElement(i);r=o.getElementsByTagName(i)[0];e.src='//www.google-analytics.com/analytics.js';r.parentNode.insertBefore(e,r)}(window,document,'script','ga'));
		            ga('create','UA-XXXXX-X');ga('send','pageview');
		        </script>
		</body>
	</html>
</#macro>
