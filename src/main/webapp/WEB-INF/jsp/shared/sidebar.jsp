<div id="layoutSidenav_nav">
	<nav class="sidenav shadow-right sidenav-light">
		<div class="sidenav-menu">
			<div class="nav accordion" id="accordionSidenav">
				<div class="sidenav-menu-heading">Dashboard</div>
				<c:choose>
					<c:when test="${userClickDashboard == true }">
						<a class="nav-link active" href="/dashboard">
							<div class="nav-link-icon">
								<i data-feather="bar-chart"></i>
							</div> Dashboard
						</a>
					</c:when>
					<c:otherwise>
						<a class="nav-link" href="/dashboard">
							<div class="nav-link-icon">
								<i data-feather="bar-chart"></i>
							</div> Dashboard
						</a>
					</c:otherwise>
				</c:choose>
				<div class="sidenav-menu-heading">Shops & Items</div>
				<a class="nav-link collapsed" href="javascript:void(0);"
					data-toggle="collapse" data-target="#collapseDashboards"
					aria-expanded="false" aria-controls="collapseDashboards">
					<div class="nav-link-icon">
						<i data-feather="activity"></i>
					</div> Shops & Items
					<div class="sidenav-collapse-arrow">
						<i class="fas fa-angle-down"></i>
					</div>
				</a>
				<div class="collapse" id="collapseDashboards"
					data-parent="#accordionSidenav">
					<nav class="sidenav-menu-nested nav accordion"
						id="accordionSidenavPages">
						<a class="nav-link" href="/shops"> Shops <!-- <span class="badge badge-primary-soft text-primary ml-auto">Updated</span> -->
						</a> <a class="nav-link" href="/getCategory/1"> Items <!-- <span class="badge badge-primary-soft text-primary ml-auto">Updated</span> -->
						</a>
						<!-- <a class="nav-link" href="dashboard-3.html">
                                        Affiliate
                                        <span class="badge badge-primary-soft text-primary ml-auto">Updated</span>
                                    </a> -->
					</nav>
				</div>
				<div class="sidenav-menu-heading">Orders</div>
				<a class="nav-link collapsed" href="javascript:void(0);"
					data-toggle="collapse" data-target="#collapsePages"
					aria-expanded="false" aria-controls="collapsePages">
					<div class="nav-link-icon">
						<i data-feather="grid"></i>
					</div> Order History <!-- <div class="sidenav-collapse-arrow"><i class="fas fa-angle-down"></i></div> -->
				</a> <a class="nav-link collapsed" href="javascript:void(0);"
					data-toggle="collapse" data-target="#collapseFlows"
					aria-expanded="false" aria-controls="collapseFlows">
					<div class="nav-link-icon">
						<i data-feather="repeat"></i>
					</div> Market Configuration <!-- <div class="sidenav-collapse-arrow"><i class="fas fa-angle-down"></i></div> -->
				</a>
				<div class="sidenav-menu-heading">Accounts</div>
				<a class="nav-link collapsed" href="javascript:void(0);"
					data-toggle="collapse" data-target="#collapseLayouts"
					aria-expanded="false" aria-controls="collapseLayouts">
					<div class="nav-link-icon">
						<i data-feather="layout"></i>
					</div> Accounts
					<div class="sidenav-collapse-arrow">
						<i class="fas fa-angle-down"></i>
					</div>
				</a>
				<div class="collapse" id="collapseLayouts"
					data-parent="#accordionSidenav">
					<nav class="sidenav-menu-nested nav accordion"
						id="accordionSidenavLayout">
						<a class="nav-link collapsed" href="javascript:void(0);"
							data-toggle="collapse"
							data-target="#collapseLayoutSidenavVariations"
							aria-expanded="false"
							aria-controls="collapseLayoutSidenavVariations"> User
							Accounts
							<div class="sidenav-collapse-arrow">
								<i class="fas fa-angle-down"></i>
							</div>
						</a> <a class="nav-link collapsed" href="javascript:void(0);"
							data-toggle="collapse" data-target="#collapseLayoutContainers"
							aria-expanded="false" aria-controls="collapseLayoutContainers">
							Staff Accounts
							<div class="sidenav-collapse-arrow">
								<i class="fas fa-angle-down"></i>
							</div>
						</a> <a class="nav-link collapsed" href="javascript:void(0);"
							data-toggle="collapse" data-target="#collapseLayoutsPageHeaders"
							aria-expanded="false" aria-controls="collapseLayoutsPageHeaders">
							Page Headers
							<div class="sidenav-collapse-arrow">
								<i class="fas fa-angle-down"></i>
							</div>
						</a>
					</nav>
				</div>
				<div class="sidenav-menu-heading">Delivery</div>
				<a class="nav-link" href="charts.html">
					<div class="nav-link-icon">
						<i data-feather="bar-chart"></i>
					</div> Delivery Inventory
				</a> <a class="nav-link" href="tables.html">
					<div class="nav-link-icon">
						<i data-feather="filter"></i>
					</div> Delivery Staff
				</a>
			</div>
		</div>
		<div class="sidenav-footer">
			<div class="sidenav-footer-content">
				<div class="sidenav-footer-subtitle">Logged in as:</div>
				<div class="sidenav-footer-title"><%= session.getAttribute("username") %></div>
			</div>
		</div>
	</nav>
</div>