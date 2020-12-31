<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<main>
	<header
		class="page-header page-header-dark bg-gradient-primary-to-secondary pb-10">
		<div class="container">
			<div class="page-header-content pt-4">
				<div class="row align-items-center justify-content-between">
					<div class="col-auto mt-4">
						<h1 class="page-header-title">
							<div class="page-header-icon">
								<i data-feather="filter"></i>
							</div>
							Sales
						</h1>
						<div class="page-header-subtitle">Showing Shop wise monthly sales</div>
					</div>
				</div>
			</div>
		</div>
	</header>
	<!-- Main page content-->
	<div class="container mt-n10">
		<div class="card mb-4">
			<div class="card-header">Order List</div>
			<div class="card-body">
				<div class="datatable">

                    <form action="/sales" method="GET">
                        <input type="date" name="startDate" id="startDate">
                        <input type="date" name="endDate" id="endDate">
                        <input type="submit">
                    </form>
					<table class="table table-bordered table-hover display" id=""
						width="100%" cellspacing="0">
						<thead>
							<tr>
								<th>Shop ID</th>
								<th>Shop Name</th>
								<th>Average Order value</th>
								<th>Order Count</th>
								<th>Order Sales</th>
							</tr>
						</thead>
						<tfoot>
							<tr>
								<th>Shop ID</th>
								<th>Shop Name</th>
								<th>Average Order value</th>
								<th>Order Count</th>
								<th>Order Sales</th>
							</tr>
						</tfoot>
						<tbody>
							<c:forEach items="${salesList}" var="city">
								<tr>
									<td>${city.shopID}</td>
									<td>${city.shopName}</td>
									<td><fmt:formatNumber type="number" maxFractionDigits="2" value="${city.avgOrderPrice}"/></td>
									<td>${city.orderCount}</td>
									<td>${city.totalOrderSales}</td>

								</tr>
							</c:forEach>
						</tbody>
					</table>



				</div>
			</div>
		</div>
	</div>
</main>