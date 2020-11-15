

<c:if test="${userClickDashboard == true }">
	<%@include file="home.jsp"%>
</c:if>

<c:if test="${userClickItems == true }">
	<%@include file="category.jsp"%>
</c:if>

<c:if test="${userClickEditCategory == true }">
	<%@include file="editCategory.jsp"%>
</c:if>

<c:if test="${userClickAddCategory == true }">
	<%@include file="addCategory.jsp"%>
</c:if>

<c:if test="${userClickEditItem == true }">
	<%@include file="editItem.jsp"%>
</c:if>

<c:if test="${userClickAddItem == true }">
	<%@include file="addItem.jsp"%>
</c:if>

<c:if test="${userClickShops == true }">
	<%@include file="shop.jsp"%>
</c:if>

<c:if test="${userClickEditShops == true }">
	<%@include file="editShop.jsp"%>
</c:if>

<c:if test="${userPageUnderConstruction == true }">
	<%@include file="underConstruction.jsp"%>
</c:if>

<c:if test="${userClickOrders == true }">
	<%@include file="order.jsp"%>
</c:if>

<c:if test="${userClickUsers == true }">
	<%@include file="users.jsp"%>
</c:if>

<c:if test="${userClickEditUser == true }">
	<%@include file="editUser.jsp"%>
</c:if>

