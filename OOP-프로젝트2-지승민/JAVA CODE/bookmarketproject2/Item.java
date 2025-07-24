package bookmarketproject2;
public abstract class Item {
	String Id;
	String name;
	int unitPrice;

	public Item(String bookId, String name, int unitPrice) {
		this.Id = bookId;
		this.name = name;
		this.unitPrice = unitPrice;
	}

	abstract String getBookId();

	abstract String getName();

	abstract int getUnitPrice();

	abstract void setBookId(String bookId);

	abstract void setName(String name);

	abstract void setUnitPrice(int unitPrice);
}
