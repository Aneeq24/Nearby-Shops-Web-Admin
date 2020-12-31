
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
							Orders
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
			<div class="card-header">Order List</div>
			<div class="card-body">
				<div class="datatable">


					<table class="table table-bordered table-hover display" id=""
						width="100%" cellspacing="0">
						<thead>
							<tr>
								<th>Order ID</th>
								<th>Shop Name</th>
								<th>Item Count</th>
								<th>Net Payable</th>
								<th>Date Time Placed</th>
							</tr>
						</thead>
						<tfoot>
							<tr>
								<th>Order ID</th>
								<th>Shop Name</th>
								<th>Item Count</th>
								<th>Net Payable</th>
								<th>Date Time Placed</th>
							</tr>
						</tfoot>
						<tbody>
							<c:forEach items="${orderList}" var="city">
								<tr>
									<td>${city.orderID}</td>
									<td>${city.shop.shopName}</td>
									<td>${city.itemCount}</td>
									<td>${city.netPayable}</td>
									<td>${city.dateTimePlaced}</td>
									<%-- <td><a href="${contextRoot}/editOrder/${city.shopID}"
										class="btn btn-datatable btn-icon btn-transparent-dark mr-2">

											<svg xmlns="http://www.w3.org/2000/svg" width="24"
												height="24" viewBox="0 0 24 24" fill="none"
												stroke="currentColor" stroke-width="2"
												stroke-linecap="round" stroke-linejoin="round"
												class="feather feather-edit-2">
															<polygon points="16 3 21 8 8 21 3 21 3 16 16 3"></polygon></svg>
									</a>  <a href=""
										class="btn btn-datatable btn-icon btn-transparent-dark"> <svg
												xmlns="http://www.w3.org/2000/svg" width="24" height="24"
												viewBox="0 0 24 24" fill="none" stroke="currentColor"
												stroke-width="2" stroke-linecap="round"
												stroke-linejoin="round" class="feather feather-trash-2">
															<polyline points="3 6 5 6 21 6"></polyline>
															<path
													d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
															<line x1="10" y1="11" x2="10" y2="17"></line>
															<line x1="14" y1="11" x2="14" y2="17"></line></svg>
									</a> </td> --%>
								</tr>
							</c:forEach>
						</tbody>
					</table>



				</div>
			</div>
		</div>
	</div>
</main>