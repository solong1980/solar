<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>Solar</title>
<!-- Tell the browser to be responsive to screen width -->
<meta
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"
	name="viewport">
<link rel="stylesheet" href="../statics/css/bootstrap.min.css">
<link rel="stylesheet" href="../statics/css/font-awesome.min.css">
<link rel="stylesheet" href="../statics/css/AdminLTE.min.css">
<!-- AdminLTE Skins. Choose a skin from the css/skins
       folder instead of downloading all of them to reduce the load. -->
<link rel="stylesheet" href="../statics/css/all-skins.min.css">
<link rel="stylesheet" href="../statics/css/main.css">
<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
  <![endif]-->
</head>
<body class="hold-transition login-page">
	<div class="login-box">
		<div class="login-logo">
			<c:if test="${success == 1}">
				<div class="alert alert-danger alert-dismissible">
					<h4 style="margin-bottom: 0px;">
						<i class="fa fa-exclamation-triangle"></i> ${message}
					</h4>
				</div>
			</c:if>
			<c:if test="${success == 0}">
				<p>
					<strong>${message}</strong>
				</p>
			</c:if>
		</div>
	</div>
</body>
</html>