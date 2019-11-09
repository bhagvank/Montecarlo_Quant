<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<link rel="stylesheet" href="CSS/navbar.css">
<script src="JS/dropdown.js"></script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>MonteCarlo FinTech</title>
</head>
<body>
<%
	if(session.getAttribute("username")==null)
	{
		response.sendRedirect("login.jsp");
	}
%>

<div class="navbar">
  <a href="/MonteCarlo/home.jsp">Home</a>
  <div class="dropdown">
  <button class="dropbtn" onclick="myFunction()">Options Pricing
    <i class="fa fa-caret-down"></i>
  </button>
  <div class="dropdown-content" id="myDropdown">
    <a href="/MonteCarlo/asian_option.jsp">Asian Option</a>
    <a href="/MonteCarlo/european_option.jsp">European Option</a>
    <a href="/MonteCarlo/black_sholes.jsp">Black Sholes</a>
  </div>
  </div>
  <a href="#statistics">Statistics</a> 
  <a href="#about_us">About Us</a> 
  <a href="/MonteCarlo/Logout" style="float:right">Logout</a>
</div>

<h3>HOME PAGE</h3>
<p>BASIC INFORMATION AND FACTS</p>
</body>
</html>

