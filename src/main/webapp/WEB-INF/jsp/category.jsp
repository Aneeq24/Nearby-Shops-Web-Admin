
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
										Tables
									</h1>
									<div class="page-header-subtitle">An extended version of
										the DataTables library, customized for SB Admin Pro</div>
								</div>
							</div>
						</div>
					</div>
				</header>
				<!-- Main page content-->
				<div class="container mt-n10">
					<div class="card mb-4">
						<div class="card-header">Sub Categories Tables
							<a href="/addCategory/toCategory/${categoryID}" class="btn btn-primary float-right">+Add Category</a></div>
						<div class="card-body">
							<div class="datatable">


								<table class="table table-bordered table-hover display" id=""
									width="100%" cellspacing="0">
									<thead>
										<tr>
											<th>Category ID</th>
											<th>Category Name</th>
											<th>Actions</th>
										</tr>
									</thead>
									<tfoot>
										<tr>
											<th>Category ID</th>
											<th>Category Name</th>
											<th>Actions</th>
										</tr>
									</tfoot>
									<tbody>
										<c:forEach items="${SubCategoriesList}" var="city">
											<tr>
												<td>${city.itemCategoryID}</td>
												<td>${city.categoryName}</td>
												<td><a href="/getCategory/${city.itemCategoryID}"
													class="btn btn-datatable btn-icon btn-transparent-dark mr-2">

														<svg xmlns="http://www.w3.org/2000/svg" width="24"
															height="24" viewBox="0 0 24 24" fill="none"
															stroke="currentColor" stroke-width="2"
															stroke-linecap="round" stroke-linejoin="round"
															class="feather feather-eye">
																<path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
																<circle cx="12" cy="12" r="3"></circle></svg>
												</a> 
												<a href="/editCategory/${city.itemCategoryID}"
													class="btn btn-datatable btn-icon btn-transparent-dark mr-2">

														<svg xmlns="http://www.w3.org/2000/svg" width="24"
															height="24" viewBox="0 0 24 24" fill="none"
															stroke="currentColor" stroke-width="2"
															stroke-linecap="round" stroke-linejoin="round"
															class="feather feather-edit-2">
															<polygon points="16 3 21 8 8 21 3 21 3 16 16 3"></polygon></svg>
												</a> <a href="/delete/inCategory/${city.parentCategoryID }/category/${city.itemCategoryID}"
													class="btn btn-datatable btn-icon btn-transparent-dark">
														<svg xmlns="http://www.w3.org/2000/svg" width="24"
															height="24" viewBox="0 0 24 24" fill="none"
															stroke="currentColor" stroke-width="2"
															stroke-linecap="round" stroke-linejoin="round"
															class="feather feather-trash-2">
															<polyline points="3 6 5 6 21 6"></polyline>
															<path
																d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
															<line x1="10" y1="11" x2="10" y2="17"></line>
															<line x1="14" y1="11" x2="14" y2="17"></line></svg>
												</a></td>
											</tr>
										</c:forEach>
									</tbody>
								</table>



							</div>
						</div>
					</div>
					<div class="card mb-4">
						<div class="card-header">Items Tables
							<a href="/addItem/toCategory/${categoryID}" class="btn btn-primary float-right">+Add Item</a>
						</div>
						<div class="card-body">
							<div class="datatable">


								<table class="table table-bordered table-hover display" id=""
									width="100%" cellspacing="0">
									<thead>
										<tr>
											<th>Item ID</th>
											<th>Item Name</th>
											<th>Actions</th>
										</tr>
									</thead>
									<tfoot>
										<tr>
											<th>Item ID</th>
											<th>Item Name</th>
											<th>Actions</th>
										</tr>
									</tfoot>
									<tbody>
										<c:forEach items="${itemList}" var="city">
											<tr>
												<td>${city.itemID}</td>
												<td>${city.itemName}</td>
												<td><a href="/editItem/${city.itemCategoryID}"
													class="btn btn-datatable btn-icon btn-transparent-dark mr-2">

														<svg xmlns="http://www.w3.org/2000/svg" width="24"
															height="24" viewBox="0 0 24 24" fill="none"
															stroke="currentColor" stroke-width="2"
															stroke-linecap="round" stroke-linejoin="round"
															class="feather feather-eye">
																<path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
																<circle cx="12" cy="12" r="3"></circle></svg>
												</a> <a href="/editItem/${city.itemID}"
													class="btn btn-datatable btn-icon btn-transparent-dark mr-2">

														<svg xmlns="http://www.w3.org/2000/svg" width="24"
															height="24" viewBox="0 0 24 24" fill="none"
															stroke="currentColor" stroke-width="2"
															stroke-linecap="round" stroke-linejoin="round"
															class="feather feather-edit-2">
															<polygon points="16 3 21 8 8 21 3 21 3 16 16 3"></polygon></svg>
												</a> <a href="/delete/inCategory/${city.itemCategoryID}/item/${city.itemID}"
													class="btn btn-datatable btn-icon btn-transparent-dark">
														<svg xmlns="http://www.w3.org/2000/svg" width="24"
															height="24" viewBox="0 0 24 24" fill="none"
															stroke="currentColor" stroke-width="2"
															stroke-linecap="round" stroke-linejoin="round"
															class="feather feather-trash-2">
															<polyline points="3 6 5 6 21 6"></polyline>
															<path
																d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
															<line x1="10" y1="11" x2="10" y2="17"></line>
															<line x1="14" y1="11" x2="14" y2="17"></line></svg>
												</a></td>
											</tr>
										</c:forEach>
									</tbody>
								</table>



							</div>
						</div>
					</div>
				</div>
			</main>