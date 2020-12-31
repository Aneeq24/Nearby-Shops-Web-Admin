<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<main>
	<header
		class="page-header page-header-dark bg-gradient-primary-to-secondary pb-10">
		<div class="container">
			<div class="page-header-content pt-4">
				<div class="row align-items-center justify-content-between">
					<div class="col-auto mt-4">
                                                            <h1 class="page-header-title">
                                                                <div class="page-header-icon"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-activity"><polyline points="22 12 18 12 15 21 9 3 6 12 2 12"></polyline></svg></div>
                                                                Dashboard
                                                            </h1>
                                                            <div class="page-header-subtitle">Sales summary</div>
                                                        </div>
				</div>
			</div>
		</div>
	</header>
	<!-- Main page content-->
	<div class="container mt-n10">

	    <div class="row">
                                    <div class="col-xxl-3 col-lg-6">
                                        <div class="card bg-primary text-white mb-4">
                                            <div class="card-body">
                                                <div class="d-flex justify-content-between align-items-center">
                                                    <div class="mr-3">
                                                        <div class="text-white-75 small">Total Sales</div>
                                                        <div class="text-lg font-weight-bold">&#8377;${totalSales}</div>
                                                    </div>
                                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-dollar-sign feather-xl text-white-50"><line x1="12" y1="1" x2="12" y2="23"></line><path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"></path></svg>
                                                    </div>
                                            </div>
                                            <div class="card-footer d-flex align-items-center justify-content-between"></div>

                                        </div>
                                    </div>
                                    <div class="col-xxl-3 col-lg-6">
                                        <div class="card bg-warning text-white mb-4">
                                            <div class="card-body">
                                                <div class="d-flex justify-content-between align-items-center">
                                                    <div class="mr-3">
                                                        <div class="text-white-75 small">Order Count</div>
                                                        <div class="text-lg font-weight-bold">${orderCount}</div>
                                                    </div>
                                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-activity feather-xl text-white-50"><line x1="8" y1="6" x2="21" y2="6"></line><line x1="8" y1="12" x2="21" y2="12"></line><line x1="8" y1="18" x2="21" y2="18"></line><line x1="3" y1="6" x2="3" y2="6"></line><line x1="3" y1="12" x2="3" y2="12"></line><line x1="3" y1="18" x2="3" y2="18"></line></svg>
                                                </div>
                                            </div>
                                            <div class="card-footer d-flex align-items-center justify-content-between"></div>

                                        </div>
                                    </div>
                                    <div class="col-xxl-3 col-lg-6">
                                        <div class="card bg-success text-white mb-4">
                                            <div class="card-body">
                                                <div class="d-flex justify-content-between align-items-center">
                                                    <div class="mr-3">
                                                        <div class="text-white-75 small">Vendor Count</div>
                                                        <div class="text-lg font-weight-bold">${vendorCount}</div>
                                                    </div>
                                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-activity feather-xl text-white-50"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path><circle cx="12" cy="7" r="4"></circle></svg>
                                                </div>
                                            </div>
                                            <div class="card-footer d-flex align-items-center justify-content-between"></div>

                                        </div>
                                    </div>
                                    <div class="col-xxl-3 col-lg-6">
                                        <div class="card bg-danger text-white mb-4">
                                            <div class="card-body">
                                                <div class="d-flex justify-content-between align-items-center">
                                                    <div class="mr-3">
                                                        <div class="text-white-75 small">User Count</div>
                                                        <div class="text-lg font-weight-bold">${userCount}</div>
                                                    </div>
                                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-activity feather-xl text-white-50"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path><circle cx="9" cy="7" r="4"></circle><path d="M23 21v-2a4 4 0 0 0-3-3.87"></path><path d="M16 3.13a4 4 0 0 1 0 7.75"></path></svg>
                                                 </div>
                                            </div>
                                            <div class="card-footer d-flex align-items-center justify-content-between"></div>

                                        </div>
                                    </div>
                                </div>
		<div class="card mb-4">
			<div class="card-header">Total Sales Report</div>
			<div class="card-body">
				<div class="datatable">

                    <form class="datatable-date-filter-form" action="/sales" method="GET">
                        Start Date &nbsp;&nbsp;<input type="date" name="startDate" id="startDate">&nbsp;&nbsp;
                        End Date &nbsp;&nbsp;<input type="date" name="endDate" id="endDate">&nbsp;&nbsp;
                        <input type="submit" value="Filter">
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