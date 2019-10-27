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

<form action="AsianOption" method="post">
  <div class="container">
  	<h2>Asian Option</h2><br>
    <label for="T">T</label>
    <input type="text" placeholder="Enter T value" name="T" required>

    <label for="r">r</label>
    <input type="text" placeholder="Enter r value" name="r" required>
    
    <label for="vol">vol</label>
    <input type="text" placeholder="Enter vol value" name="vol" required>

    <label for="dt">dt</label>
    <input type="text" placeholder="Enter dt value" name="dt" required>
    
    <label for="S0">S0</label>
    <input type="text" placeholder="Enter S0 value" name="S0" required>

    <label for="K">K</label>
    <input type="text" placeholder="Enter K value" name="K" required>
    
    <label for="sims">sims</label>
    <input type="text" placeholder="Enter sims value" name="sims" required>
        
    <button class="loginbtn" type="submit">Generate result</button>
  </div>
</form>

</body>
</html>