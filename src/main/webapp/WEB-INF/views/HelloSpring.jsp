<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>HelloSpring</title>
<link rel="stylesheet"  href="/springlegacy/static/bootstrap5.1.3/css/bootstrap.css" />
<script src="/springlegacy/resources/jquery/jquery-3.6.0.js"></script>
</head>
<body>
<div class="container">
	<h2>Hello Spring Framework</h2>
	<h3>첫번째 컨트롤러 만들기</h3>
	<h4>컨트롤러에서 뷰로 전달한 값 : ${firstMassage }</h4>
	
	<script>
		$(function() {
			alert('jQuery 실행잘됨?');
		});
	</script>
</div>
</body>
</html>