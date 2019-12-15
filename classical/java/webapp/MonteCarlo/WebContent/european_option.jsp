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

<form action="EuropeanOption" method="post">
  <div class="container">
  	<h2>European Option</h2><br>
    <label for="stock_price">stock_price</label>
    <input type="text" placeholder="Enter stock_price value" name="stock_price" required>

    <label for="strike_price">strike_price</label>
    <input type="text" placeholder="Enter strike_price value" name="strike_price" required>
    
    <label for="interest_rate">interest_rate</label>
    <input type="text" placeholder="Enter interest_rate value" name="interest_rate" required>

    <label for="numberOfSteps">numberOfSteps</label>
    <input type="text" placeholder="Enter numberOfSteps value" name="numberOfSteps" required>
    
    <label for="timeTillExpiration">timeTillExpiration</label>
    <input type="text" placeholder="Enter timeTillExpiration value" name="timeTillExpiration" required>
    
    <label for="timeTillExpiration">numberOfComputations</label>
    <input type="text" placeholder="Enter numberOfComputations value" name="numberOfComputations" required>

    <label for="volatility">volatility</label>
    <input type="text" placeholder="Enter volatility value" name="volatility" required>
    
    <label for="totalPayoff">totalPayoff</label>
    <input type="text" placeholder="Enter totalPayoff value" name="totalPayoff" required>
    
    <label for="averagePayoff">averagePayoff</label>
    <input type="text" placeholder="Enter averagePayoff value" name="averagePayoff" required>
    
    <label for="compundRate">compundRate</label>
    <input type="text" placeholder="Enter compundRate value" name="compundRate" required>
        
    <button class="loginbtn" type="submit">Generate result</button>
  </div>
</form>

</body>
</html>