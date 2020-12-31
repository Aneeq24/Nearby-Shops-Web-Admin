
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
						<div class="page-header-subtitle">Showing latest 100 orders</div>
					</div>
				</div>
			</div>
		</div>
	</header>
	<!-- Main page content-->
	<div class="container mt-n10">
		<div class="card mb-4">
			<div class="card-header">Sales List</div>
			<div class="card-body">
				<div class="datatable">

					<table class="table table-bordered table-hover display" id=""
						width="100%" cellspacing="0">

						<thead>
							<tr>
								<th>Shop ID</th>
								<th>Shop Name</th>
								<th>Average</th>
								<th>Total Sales</th>
								<th>Order Count</th>
							</tr>
						</thead>
						<tfoot>
							<tr>
								<th>Shop ID</th>
                                <th>Shop Name</th>
                                <th>Average</th>
                                <th>Total Sales</th>
                                <th>Order Count</th>
							</tr>
						</tfoot>
						<tbody>
							<c:forEach items="${salesList}" var="sale">
								<tr>
									<td>${sale.shopID}</td>
									<td>${sale.shopName}</td>
									<td>${sale.avgOrderPrice}</td>
									<td>${sale.totalOrderSales}</td>
									<td>${sale.orderCount}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>



				</div>
			</div>
		</div>
	</div>
</main>