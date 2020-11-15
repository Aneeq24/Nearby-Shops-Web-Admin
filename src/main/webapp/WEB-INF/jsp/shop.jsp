
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
							Shoplist
						</h1>
						<div class="page-header-subtitle">Showing all the Shops in your market</div>
					</div>
				</div>
			</div>
		</div>
	</header>
	<!-- Main page content-->
	<div class="container mt-n10">
		<div class="card mb-4">
			<div class="card-header">Shops List</div>
			<div class="card-body">
				<div class="datatable">


					<table class="table table-bordered table-hover display" id=""
						width="100%" cellspacing="0">
						<thead>
							<tr>
								<th>Shop ID</th>
								<th>Shop Name</th>
								<th>Shop Status</th>
								<th>Actions</th>
							</tr>
						</thead>
						<tfoot>
							<tr>
								<th>Shop ID</th>
								<th>Shop Name</th>
								<th>Shop Status</th>
								<th>Actions</th>
							</tr>
						</tfoot>
						<tbody>
							<c:forEach items="${shopList}" var="city">
								<tr>
									<td>${city.shopID}</td>
									<td>${city.shopName}</td>
									<td>
										<c:choose>
											<c:when test="${city.shopEnabled == false }">
												<span class="badge badge-danger">Disabled</span>
											</c:when>
											<c:when test="${city.shopEnabled == true }">
												<span class="badge badge-success">Enabled</span>
											</c:when>
											<c:when test="${city.shopWaitlisted == true }">
												<span class="badge badge-warning">Waitlisted</span>
											</c:when>
											<c:otherwise>
												<span class="badge badge-success">Enabled</span>
											</c:otherwise>
										</c:choose>
									</td>
									<td><a href="${contextRoot}/editShop/${city.shopID}"
										class="btn btn-datatable btn-icon btn-transparent-dark mr-2">

											<svg xmlns="http://www.w3.org/2000/svg" width="24"
												height="24" viewBox="0 0 24 24" fill="none"
												stroke="currentColor" stroke-width="2"
												stroke-linecap="round" stroke-linejoin="round"
												class="feather feather-edit-2">
															<polygon points="16 3 21 8 8 21 3 21 3 16 16 3"></polygon></svg>
									</a> <!-- <a href=""
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
									</a> --></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>



				</div>
			</div>
		</div>
	</div>
</main>