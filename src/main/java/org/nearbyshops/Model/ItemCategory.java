package org.nearbyshops.Model;

public class ItemCategory {

	// contract class describing the Globals schema for the ItemCategory

	// Table Name
	public static final String TABLE_NAME = "ITEM_CATEGORY";


	// Column Names
	public static final String ITEM_CATEGORY_ID = "ID";
	public static final String ITEM_CATEGORY_NAME = "ITEM_CATEGORY_NAME";
	public static final String ITEM_CATEGORY_DESCRIPTION = "ITEM_CATEGORY_DESC";
//	public static final String ITEM_CATEGORY_DESCRIPTION_SHORT = "ITEM_CATEGORY_DESCRIPTION_SHORT";
	public static final String PARENT_CATEGORY_ID = "PARENT_CATEGORY_ID";
	public static final String IMAGE_PATH = "IMAGE_PATH";
	public static final String CATEGORY_ORDER = "CATEGORY_ORDER";



	// Create Table Statement
	public static final String createTablePostgres = "CREATE TABLE IF NOT EXISTS "
			+ ItemCategory.TABLE_NAME + "("
			+ " " + ItemCategory.ITEM_CATEGORY_ID + " SERIAL PRIMARY KEY,"
			+ " " + ItemCategory.ITEM_CATEGORY_NAME + " text,"
			+ " " + ItemCategory.ITEM_CATEGORY_DESCRIPTION + " text,"
			+ " " + ItemCategory.PARENT_CATEGORY_ID + " INT,"
			+ " " + ItemCategory.IMAGE_PATH + " text,"
			+ " " + ItemCategory.CATEGORY_ORDER + " INT,"
			+ " FOREIGN KEY(" + ItemCategory.PARENT_CATEGORY_ID +") REFERENCES " + ItemCategory.TABLE_NAME + "(" + ItemCategory.ITEM_CATEGORY_ID + ")"
			+ ")";





	// Instance Variables

	private int itemCategoryID;
	private String categoryName;
	private String categoryDescription;
	private Integer parentCategoryID;
	private boolean isLeafNode;
	private String imagePath;
	private Integer categoryOrder;
	// recently added
	private boolean isAbstractNode;
	private String descriptionShort;

	ItemCategory parentCategory = null;







	//Getters and Setters

	public Integer getCategoryOrder() {
		return categoryOrder;
	}

	public void setCategoryOrder(Integer categoryOrder) {
		this.categoryOrder = categoryOrder;
	}

	public Boolean getisAbstractNode() {
		return isAbstractNode;
	}

	public void setisAbstractNode(Boolean abstractNode) {
		isAbstractNode = abstractNode;
	}

	public String getDescriptionShort() {
		return descriptionShort;
	}

	public void setDescriptionShort(String descriptionShort) {
		this.descriptionShort = descriptionShort;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryDescription() {
		return categoryDescription;
	}

	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}

	public int getItemCategoryID() {
		return itemCategoryID;
	}

	public void setItemCategoryID(int itemCategoryID) {
		this.itemCategoryID = itemCategoryID;
	}


	public Integer getParentCategoryID() {
		return parentCategoryID;
	}

	public void setParentCategoryID(Integer parentCategoryID) {
		this.parentCategoryID = parentCategoryID;
	}

	public boolean getIsLeafNode() {
		return isLeafNode;
	}

	public void setIsLeafNode(boolean isLeafNode) {
		this.isLeafNode = isLeafNode;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
}
