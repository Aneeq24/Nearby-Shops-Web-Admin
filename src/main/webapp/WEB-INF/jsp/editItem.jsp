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
							Item Profile <%-- ${item } --%>
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
					<div class="card-header">Item  Picture</div>
					<div class="card-body text-center">
						<!-- Profile picture image-->
						<img class="img-account-profile rounded-circle mb-2"
							src="https://source.unsplash.com/QAB-WJcbgJk/300x300" alt="" />
						<!-- Profile picture help block-->
						<div class="small font-italic text-muted mb-4">JPG or PNG no
							larger than 2 MB</div>
						<!-- Profile picture upload button-->
						<button class="btn btn-primary" type="button">Upload new
							image</button>
					</div>
				</div>
			</div>
			<div class="col-xl-8">
				<!-- Account details card-->
				<div class="card mb-4">
					<div class="card-header">Item Details</div>
					<div class="card-body">
						<form:form method="POST" action="/updateItem" modelAttribute="item">
							<!-- Form Group (username)-->
							<div class="form-group">
								<label class="small mb-1" for="inputUsername">Item ID</label>
								<form:input path="itemID" class="form-control"
									id="inputUsername" disabled="disabled"/>
									<form:hidden path="itemCategoryID" class="form-control"
									id="inputUsername"/>
							</div>
							<!-- Form Row-->
							<div class="form-row">
								<!-- Form Group (first name)-->
								<div class="form-group col-md-6">
									<label class="small mb-1" for="inputFirstName">Item Name</label>
									<form:input path="itemName" class="form-control"
										id="inputFirstName" />
								</div>
								<!-- Form Group (last name)-->
								<div class="form-group col-md-6">
									<label class="small mb-1" for="inputLastName">Item Description</label>
									<form:input path="itemDescription" class="form-control"
										id="inputFirstName" />									
									<form:hidden path="itemImageURL" class="form-control"
									id="itemImageURL"/>
								</div>
							</div>
							<!-- Form Row        -->
							<div class="form-row">
								<!-- Form Group (organization name)-->
								<div class="form-group col-md-6">
									<label class="small mb-1" for="itemDescriptionLong">Item Description Long</label>
									<form:input path="itemDescriptionLong" class="form-control"
										id="itemDescriptionLong" />
								</div>
								<div class="form-group col-md-6">
									<label class="small mb-1" for="quantityUnit">Item Unit</label>
									<form:input path="quantityUnit" class="form-control"
										id="quantityUnit" />
								</div>
								<!-- Form Group (location)-->
							</div>
							<!-- Form Group (email address)-->
							
							<!-- Form Row        -->
							<div class="form-row">
								<!-- Form Group (organization name)-->
								<div class="form-group col-md-6">
									<label class="small mb-1" for="listPrice">List Price</label>
									<form:input path="listPrice" class="form-control"
										id="listPrice"  />
								</div>
								<!-- Form Group (location)-->
								<div class="form-group col-md-6">
									<label class="small mb-1" for="imageCopyrights">Discounted Price</label>
									<form:input path="discountedPrice" class="form-control"
										id="imageCopyrights" />
								</div>
							</div>
							<div class="form-group">
								<label class="small mb-1" for="imageCopyrights">Image CopyRight</label>
								<form:input path="imageCopyrights" class="form-control"
									id="imageCopyrights" disabled="disabled"/>
									
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