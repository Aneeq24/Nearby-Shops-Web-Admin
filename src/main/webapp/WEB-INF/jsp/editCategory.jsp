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
							Category Profile
							<%-- ${category } --%>
						</h1>
					</div>
				</div>
			</div>
		</div>
	</header>
	<!-- Main page content-->
	<div class="container mt-4">

		<form:form method="POST" action="${contextRoot}/updateCategory"
			modelAttribute="category" enctype="multipart/form-data">
			<div class="row">
				<div class="col-xl-4">
					<!-- Profile picture card-->
					<div class="card">
						<div class="card-header">Category Picture</div>
						<div class="card-body text-center">
							<!-- Profile picture image-->
							
								<c:if test="${category.imagePath != null }">
									<img id="blah" class="img-account-profile mb-2"
										src="${imgURL}/ItemCategory/Image/${category.imagePath }"
										alt="" />
								</c:if>
								<c:if test="${category.imagePath == null }">
									<img id="blah" class="img-account-profile mb-2"
										src="${assets}/img/list.png" alt="" />
								</c:if>
								<!-- Profile picture help block-->
								<div class="small font-italic text-muted mb-4">JPG or PNG
									no larger than 2 MB</div>
								<!-- Profile picture upload button-->
								 <input class="inputfile" type="file" name="file" id="imgInp" /> 

						</div>
					</div>
				</div>
				<div class="col-xl-8">
					<!-- Account details card-->
					<div class="card mb-4">
						<div class="card-header">Category Details</div>
						<div class="card-body">
							<!-- Form Group (username)-->
							<div class="form-group">
								<label class="small mb-1" for="categoryName">Category
									Name</label>
								<form:hidden path="itemCategoryID" class="form-control"
									id="inputUsername" />
								<form:input path="categoryName" class="form-control"
									id="categoryName" />
							</div>
							<div class="form-group">
								<label class="small mb-1" for="categoryDescription">Category
									Description</label>
								<form:input path="categoryDescription" class="form-control"
									id="categoryDescription" />
							</div>
							<!-- Form Row-->
							<div class="form-row">
								<!-- Form Group (first name)-->
								<div class="form-group col-md-12">
									<label class="small mb-1" for="categoryOrder">Category
										Order</label>
									<form:input path="categoryOrder" class="form-control"
										id="categoryOrder" />
									<form:hidden path="parentCategoryID" class="form-control"
										id="parentCategoryID" />
									<form:hidden path="isLeafNode" class="form-control"
										id="isLeafNode" />
									<form:hidden path="imagePath" class="form-control"
										id="imagePath" />
									<form:hidden path="isAbstractNode" class="form-control"
										id="isAbstractNode" />
								</div>
							</div>

							<button class="btn btn-primary" type="submit">Save
								changes</button>
						</div>
					</div>
				</div>

			</div>

		</form:form>
	</div>
</main>