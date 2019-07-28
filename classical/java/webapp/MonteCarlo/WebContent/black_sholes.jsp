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
    <label for="s">s</label>
    <input type="text" placeholder="Enter s value" name="s" required>

    <label for="x">x</label>
    <input type="text" placeholder="Enter x value" name="x" required>
    
    <label for="r">r</label>
    <input type="text" placeholder="Enter r value" name="r" required>

    <label for="sigma">sigma</label>
    <input type="text" placeholder="Enter sigma value" name="sigma" required>
    
    <label for="t">t</label>
    <input type="text" placeholder="Enter t value" name="t" required>
   
    <button class="loginbtn" type="submit">Generate result</button>
  </div>
</form>

<form action="BlackSholes" method="post">
		s: <input type="text" name="s"><br>
		x: <input type="text" name="x"><br>
		r: <input type="text" name="r"><br>
		sigma: <input type="text" name="sigma"><br>
		t: <input type="text" name="t"><br>
		<input type="submit">
</form>

</body>
</html>