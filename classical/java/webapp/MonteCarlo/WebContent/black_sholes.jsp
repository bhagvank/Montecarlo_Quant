<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<link rel="stylesheet" type="text/css" href="CSS/form.css">
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

<form action="BlackSholes" method="post">
  <div class="container">
  	<h2>Black Sholes</h2><br>
    <label for="s">Underlying price</label>
    <input type="text" placeholder="Enter Underlying Price" name="s" required>

    <label for="x">Vol</label>
    <input type="text" placeholder="Enter Vol" name="x" required>
    
    <label for="r">Rate</label>
    <input type="text" placeholder="Enter Rate" name="r" required>

    <label for="sigma">K</label>
    <input type="text" placeholder="Enter K" name="sigma" required>
    
    <label for="t">Simulations</label>
    <input type="text" placeholder="Enter Simulations" name="t" required>
   
    <button class="loginbtn" type="submit">Generate result</button>
  </div>
</form>

</body>
</html>