<%--
<div id="layoutSidenav_nav">
	<nav class="sidenav shadow-right sidenav-light">
		<div class="sidenav-menu">
			<div class="nav accordion" id="accordionSidenav">
				<div class="sidenav-menu-heading">Dashboard</div>
				<a class="nav-link" href="${contextRoot }/shops">
					<div class="nav-link-icon">
						<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24"
							viewBox="0 0 24 24" fill="none" stroke="currentColor"
							stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
							class="feather feather-activity">
									<polyline points="22 12 18 12 15 21 9 3 6 12 2 12"></polyline></svg>
					</div> Dashboard
				</a>
				<div class="sidenav-menu-heading">Shops & Items</div>
				<a class="nav-link" href="${contextRoot}/shops">
					<div class="nav-link-icon">
						<i class="fas fa-store-alt"></i>
					</div> Shops
				</a> <a class="nav-link" href="${contextRoot}/getCategory/1">
					<div class="nav-link-icon">

						<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24"
							viewBox="0 0 24 24" fill="none" stroke="currentColor"
							stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
							class="feather feather-activity">
							<line x1="8" y1="6" x2="21" y2="6"></line>
							<line x1="8" y1="12" x2="21" y2="12"></line>
							<line x1="8" y1="18" x2="21" y2="18"></line>
							<line x1="3" y1="6" x2="3" y2="6"></line>
							<line x1="3" y1="12" x2="3" y2="12"></line>
							<line x1="3" y1="18" x2="3" y2="18"></line></svg>
					</div> Items
				</a>
				<div class="sidenav-menu-heading">Orders</div>

				<a class="nav-link" href="${contextRoot}/orders">
					<div class="nav-link-icon">
						<i class="fas fa-shopping-basket"></i>
					</div> Order History
				</a> <a class="nav-link" href="${contextRoot}/underConstruction">
					<div class="nav-link-icon">
						<i class="fas fa-cogs"></i>
					</div> Market Configuration
				</a>
				<div class="sidenav-menu-heading">Accounts</div>
				<a class="nav-link" href="${contextRoot}/users">
					<div class="nav-link-icon">
						<i class="far fa-user"></i>
					</div> User Accounts
				</a>
				
			</div>
		</div>
		<div class="sidenav-footer">
			<div class="sidenav-footer-content">
				<div class="sidenav-footer-subtitle">Logged in as:</div>
				<div class="sidenav-footer-title"><%=session.getAttribute("username")%></div>
			</div>
		</div>
	</nav>
</div>
--%>


<div id="layoutSidenav_nav">
	<nav class="sidenav shadow-right sidenav-light">
		<div class="sidenav-menu">
			<div class="nav accordion" id="accordionSidenav">
				<div class="sidenav-menu-heading">Dashboard</div>
				<a class="nav-link" href="${contextRoot }/shops">
					<div class="nav-link-icon">
						<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24"
							viewBox="0 0 24 24" fill="none" stroke="currentColor"
							stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
							class="feather feather-activity">
									<polyline points="22 12 18 12 15 21 9 3 6 12 2 12"></polyline></svg>
					</div> Dashboard
				</a>
				<a class="nav-link" href="${contextRoot }/sales">
					<div class="nav-link-icon">
						<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24"
                        							viewBox="0 0 24 24" fill="none" stroke="currentColor"
                        							stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                        							class="feather feather-activity">
                        							<line x1="8" y1="6" x2="21" y2="6"></line>
                        							<line x1="8" y1="12" x2="21" y2="12"></line>
                        							<line x1="8" y1="18" x2="21" y2="18"></line>
                        							<line x1="3" y1="6" x2="3" y2="6"></line>
                        							<line x1="3" y1="12" x2="3" y2="12"></line>
                        							<line x1="3" y1="18" x2="3" y2="18"></line></svg>
					</div> Sales
				</a>
				<div class="sidenav-menu-heading">Shops & Items</div>
				<a class="nav-link" href="${contextRoot}/shops">
					<div class="nav-link-icon">
						<i class="fas fa-store-alt"></i>
					</div> Shops
				</a> <a class="nav-link" href="${contextRoot}/getCategory/1">
					<div class="nav-link-icon">

						<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24"
							viewBox="0 0 24 24" fill="none" stroke="currentColor"
							stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
							class="feather feather-activity">
							<line x1="8" y1="6" x2="21" y2="6"></line>
							<line x1="8" y1="12" x2="21" y2="12"></line>
							<line x1="8" y1="18" x2="21" y2="18"></line>
							<line x1="3" y1="6" x2="3" y2="6"></line>
							<line x1="3" y1="12" x2="3" y2="12"></line>
							<line x1="3" y1="18" x2="3" y2="18"></line></svg>
					</div> Items
				</a>
				<div class="sidenav-menu-heading">Orders</div>

				<a class="nav-link" href="${contextRoot}/orders">
					<div class="nav-link-icon">
						<i class="fas fa-shopping-basket"></i>
					</div> Order History
				</a> <a class="nav-link" href="${contextRoot}/underConstruction">
					<div class="nav-link-icon">
						<i class="fas fa-cogs"></i>
					</div> Settings
				</a>
				<div class="sidenav-menu-heading">Accounts</div>
				<a class="nav-link" href="${contextRoot}/users">
					<div class="nav-link-icon">
						<i class="far fa-user"></i>
					</div> User Accounts
				</a>

			</div>
		</div>
		<div class="sidenav-footer">
			<div class="sidenav-footer-content">
				<div class="sidenav-footer-subtitle">Logged in as:</div>
				<div class="sidenav-footer-title"><%=session.getAttribute("username")%></div>
			</div>
		</div>
	</nav>
</div>