package bookmarketproject2;

public class Book extends Item {
	public String num;
	private String author;
	private String description;
	private String category;
	private String releaseDate;
	private String company;

	public Book(String Id, String name, int unitPrice) {
		super(Id, name, unitPrice);
	}

	public Book(String num, String Id, String name, int unitPrice, String author, String description, String category,
			String releaseDate, String company) {
		super(Id, name, unitPrice);
		this.author = author;
		this.description = description;
		this.category = category;
		this.releaseDate = releaseDate;
		this.num=num;
		this.company=company;
	}

	public String getBookId() {
		return Id;
	}

	public void setBookId(String bookId) {
		this.Id = bookId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(int unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company=company;
	}
}
