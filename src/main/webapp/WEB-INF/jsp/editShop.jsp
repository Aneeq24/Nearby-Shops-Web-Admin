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
							Shop Profile
							<%-- ${Shop } --%>
						</h1>
					</div>
				</div>
			</div>
		</div>
	</header>
	<!-- Main page content-->
	<div class="container mt-4">

		<form:form method="POST" action="${contextRoot}/updateShop"
			modelAttribute="Shop" enctype="multipart/form-data">
			<div class="row">
				<div class="col-xl-4">
					<!-- Profile picture card-->
					<div class="card">
						<div class="card-header">Shop Profile Picture</div>
						<div class="card-body text-center">
							<!-- Profile picture image-->

							<c:if test="${Shop.logoImagePath != null }">
								<img id="blah" class="img-account-profile mb-2"
									src="${imgURL}/${Shop.logoImagePath }" alt="" />
							</c:if>
							<c:if test="${Shop.logoImagePath == null }">
								<img id="blah" class="img-account-profile mb-2"
									src="${assets}/img/list.png" alt="" />
							</c:if>
							<!-- Profile picture help block-->
							<div class="small font-italic text-muted mb-4">JPG or PNG
								no larger than 2 MB</div>
							<!-- Profile picture upload button-->
							<input class="" type="file" name="file" id="imgInp" />
							<!-- <button class="btn btn-primary" type="button">Upload new
								image</button> -->
						</div>
					</div>
				</div>
				<div class="col-xl-8">
					<!-- Account details card-->
					<div class="card mb-4">
						<div class="card-header">Account Details</div>
						<div class="card-body">
							<!-- Form Group (username)-->
							<div class="form-group">
								<label class="small mb-1" for="inputUsername">Shop Id</label>
								<form:input path="shopID" class="form-control"
									id="inputUsername" />
							</div>
							<!-- Form Row-->
							<div class="form-row">
								<!-- Form Group (first name)-->
								<div class="form-group col-md-6">
									<label class="small mb-1" for="inputFirstName">Shop
										name</label>
									<form:input path="shopName" class="form-control"
										id="inputFirstName" />
								</div>
								<!-- Form Group (last name)-->
								<div class="form-group col-md-3">
									<label class="small mb-1" for="shopEnabled">Is Shop
										Enabled?</label>
									<form:checkbox path="shopEnabled" class="form-control" />
								</div>
								<div class="form-group col-md-3">
									<label class="small mb-1" for="inputLastName">Is Shop
										Open?</label>
									<form:checkbox path="isOpen" class="form-control" />
								</div>
							</div>
							<!-- Form Row        -->
							<div class="form-row">
								<!-- Form Group (organization name)-->
								<div class="form-group col-md-6">
									<label class="small mb-1" for="address">Enter Shop
										Address</label>
									<form:input path="shopAddress" class="form-control"
										id="address" placeholder="Enter your location" />
								</div>
								<!-- Form Group (location)-->
								<div class="form-group col-md-6">
									<label class="small mb-1" for="city">Enter Shop
										city/Town</label>
									<form:input path="city" class="form-control" id="city"
										placeholder="Enter your Shop City/Town" />
								</div>
							</div>
							<!-- Form Group (email address)-->

							<!-- Form Row        -->
							<div class="form-row">
								<!-- Form Group (organization name)-->
								<div class="form-group col-md-6">
									<label class="small mb-1" for="picode">Pincode</label>
									<form:input path="pincode" class="form-control" id="picode"
										placeholder="Enter your location" />
								</div>
								<!-- Form Group (location)-->
								<div class="form-group col-md-6">
									<label class="small mb-1" for="landmark">Enter Landmark</label>
									<form:input path="landmark" class="form-control" id="landmark"
										placeholder="Enter your Shop City/Town" />
								</div>
							</div>
							<!-- Form Row-->
							<div class="form-row">
								<!-- Form Group (phone number)-->
								<div class="form-group col-md-6">
									<label class="small mb-1" for="customerSupport">Customer
										Helpline Number</label>
									<form:input path="customerHelplineNumber" class="form-control"
										id="customerSupport" placeholder="Enter your Shop City/Town" />
								</div>
								<div class="form-group col-md-6">
									<label class="small mb-1" for="DeliverySupport">Delivery
										Helpline Number</label>
									<form:input path="deliveryHelplineNumber" class="form-control"
										id="deliveryHelplineNumber" placeholder="Enter your location" />
								</div>
							</div>

							<!-- Form Row-->
							<div class="form-row">
								<!-- Form Group (phone number)-->
								<div class="form-group col-md-6">
									<label class="small mb-1" for="shortDescription">Short
										Description</label>
									<form:input path="shortDescription" class="form-control"
										id="shortDescription" placeholder="Enter your Shop City/Town" />
								</div>
								<div class="form-group col-md-6">
									<label class="small mb-1" for="longDescription">Long
										Description</label>
									<form:input path="longDescription" class="form-control"
										id="longDescription" placeholder="Enter your location" />
								</div>
							</div>

							<!-- Form Row-->
							<div class="form-row">
								<!-- Form Group (phone number)-->
								<div class="form-group col-md-6">
									<label class="small mb-1" for="latCenter">Enter
										latitude</label>
									<form:input path="latCenter" class="form-control"
										id="latCenter" placeholder="Enter your Shop City/Town" />
								</div>
								<div class="form-group col-md-6">
									<label class="small mb-1" for="lonCenter">Enter
										Longitude</label>
									<form:input path="lonCenter" class="form-control"
										id="lonCenter" placeholder="Enter your location" />
								</div>
							</div>

							<div class="form-group">
								<label class="small mb-1" for="deliveryRange">Enter
									Range of Delivery(in kms)</label>
								<form:input path="deliveryRange" class="form-control"
									id="deliveryRange" />
							</div>

							<!-- Form Row-->
							<div class="form-row">
								<!-- Form Group (phone number)-->
								<div class="form-group col-md-6">
									<label class="small mb-1" for="deliveryCharges">Enter
										Delivery Charge per Order</label>
									<form:input path="deliveryCharges" class="form-control"
										id="deliveryCharges" placeholder="Enter your Shop City/Town" />
								</div>
								<div class="form-group col-md-6">
									<label class="small mb-1" for="billAmountForFreeDelivery">Enter
										Bill Amouunt for Free Delivery</label>
									<form:input path="billAmountForFreeDelivery"
										class="form-control" id="billAmountForFreeDelivery"
										placeholder="Enter your location" />
								</div>
							</div>

							<!-- Form Row-->
							<div class="form-row">
								<!-- Form Group (first name)-->
								<div class="form-group col-md-6">
									<label class="small mb-1" for="pickFromShopAvailable">Pick
										From Shop Available</label>
									<form:checkbox path="pickFromShopAvailable"
										class="form-control" />
								</div>
								<!-- Form Group (last name)-->
								<div class="form-group col-md-6">
									<label class="small mb-1" for="homeDeliveryAvailable">Home
										Delivery Available</label>
									<form:checkbox path="homeDeliveryAvailable"
										class="form-control" />
										<form:hidden path="logoImagePath" class="form-control"
									id="inputUsername" />
								</div>
							</div>
							<!-- Save changes button-->
							<button class="btn btn-primary" type="submit">Save
								changes</button>

						</div>
					</div>
				</div>
			</div>
		</form:form>
	</div>
</main>