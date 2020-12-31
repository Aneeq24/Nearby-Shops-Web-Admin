<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<main>
	<header
		class="page-header page-header-compact page-header-light border-bottom bg-white mb-4">
		<div class="container-fluid">
			<div class="page-header-content">
				<div class="row align-items-center justify-content-between pt-3">
					<div class="col-auto mb-3">
						<h1 class="page-header-title">
							<div class="page-header-icon">
								<i data-feather="user"></i>
							</div>
							User Profile
							<%-- ${item } --%>
						</h1>
					</div>
				</div>
			</div>
		</div>
	</header>
	<!-- Main page content-->
	<div class="container mt-4">

		<div class="row">
			<div class="col-xl-4">
				<!-- Profile picture card-->
				<div class="card">
					<div class="card-header">User Picture</div>
					<div class="card-body text-center">
						<!-- Profile picture image-->
						
						<img class="img-account-profile  mb-2"
								src="${assets}/img/user.png" alt="" />
						<!-- Profile picture help block-->
						<div class="small font-italic text-muted mb-4">JPG or PNG no
							larger than 2 MB</div>
						<!-- Profile picture upload button-->
						
					</div>
				</div>
			</div>
			<div class="col-xl-8">
				<!-- Account details card-->
				<div class="card mb-4">
					<div class="card-header">User Details</div>
					<div class="card-body">
						<form:form method="POST" action="${contextRoot}/updateUser"
							modelAttribute="user">
							<!-- Form Group (username)-->
							<div class="form-group col-md-">
								<label class="small mb-1" for="username">User Name</label>
								<form:input path="username" class="form-control"
										id="username" />
								<form:hidden path="userID" class="form-control" id="userID" />
							</div>
							<div class="form-group col-md-">
								<label class="small mb-1" for="role">User Role</label>
								<form:select path="role" class="form-control" id="role">
									<form:option value="1">Admin</form:option>
									<form:option value="2">Staff</form:option>
									<form:option value="3">Delivery Guy</form:option>
									<form:option value="4">Shop Admin</form:option>
									<form:option value="5">Shop Staff</form:option>
									<form:option value="6">Delivery Guy Self</form:option>
									<form:option value="7">End User</form:option>
								</form:select>
							</div>
							<!-- Form Row-->
							<div class="form-row">
								<!-- Form Group (first name)-->
								<div class="form-group col-md-6">
									<label class="small mb-1" for="name">Name of User</label>
									<form:input path="name" class="form-control"
										id="inputFirstName" />
								</div>
								<!-- Form Group (last name)-->
								<div class="form-group col-md-6">
									<label class="small mb-1" for="secretCode">secret Code</label>
									<form:input path="secretCode" class="form-control"
										id="secretCode" />
								</div>
							</div>
							<div class="form-row">
								<!-- Form Group (first name)-->
								<div class="form-group col-md-6">
									<label class="small mb-1" for="email">User Email</label>
									<form:input path="email" class="form-control" id="email" />
								</div>
								<div class="form-group col-md-6">
									<label class="small mb-1" for="Gender">Gender</label><br>									
									<form:radiobutton path="gender" value="true" />
									Male
									<form:radiobutton path="gender" value="false" />
									Female
								</div>
							</div>
							<!-- Form Row        -->
							<div class="form-row">
								<!-- Form Group (organization name)-->
								<div class="form-group col-md-8">
									<label class="small mb-1" for="phone">Phone</label>
									<form:input path="phone" class="form-control" id="phone" />
								</div>
								<div class="form-group col-md-4">
									<label class="small mb-1" for="accountPrivate">Make
										Account Private</label>
									<form:checkbox path="isAccountPrivate" class="form-control" />
								</div>
								<!-- Form Group (location)-->
							</div>
							
							<div class="form-group col-md-">
								<label class="small mb-1" for="about">About User</label>
								<form:input path="about" class="form-control" id="userID" />
							</div>
							<button class="btn btn-primary" type="submit">Save
								changes</button>
						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>
</main>