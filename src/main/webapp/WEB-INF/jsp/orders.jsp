<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<h1>Current orders</h1>
	<%-- ${orders}
${orders} --%>
	<table>
		<thead>
			<tr>
				<td>orderID</td>
				<td>endUserID</td>
				<td>shopID</td>
				<td>itemCount</td>
				<td>shop Name</td>
			</tr>
		</thead>
		<tbody id="demo">
		</tbody>
	</table>
	<!-- <p id="demo"></p> -->
</body>
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script type="text/javascript">
	var x;
	var myObj = ${orders};
	 for (i in myObj) {
		x += "<tr><td>" + myObj[i].orderID + "</td>";
		x += "<td>" + myObj[i].endUserID + "</td>";
		x += "<td>" + myObj[i].shopID + "</td>";
		x += "<td>" + myObj[i].itemCount + "</td>";
		x += "<td>" + myObj[i].shop.shopName + "</td></tr>";

	} 
	document.getElementById("demo").innerHTML = x;

	$(document).ready(
			function() {
				var jsonArray = ${orders};
				$(jsonArray).each(
						function(index, item) {

							// each iteration
							var result = item.orderID;
							var isAdmin = item.endUserID;
							var CustomerCode = item.shopID;
							var CompanyCode = item.itemCount;
							//x += "<tr><td>" + result + "<td>" + isAdmin + "<td>" + CustomerCode + "<td>" + CompanyCode + "<td>" + item.shopName + "<td></tr>";
							/* alert(result + '|' + isAdmin + '|' + CustomerCode
									+ '|' + CompanyCode); */
						});

			});
</script>
</html>