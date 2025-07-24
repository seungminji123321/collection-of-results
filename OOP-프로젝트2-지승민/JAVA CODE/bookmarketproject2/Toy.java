package bookmarketproject2;

public class Toy extends Item {
	private String num;
	private String Material;
	private String ManufacturedBy;
	private String Country;
	private String ManufacturedDate;
	private String RecommendedAge;
	private String Caution;

	public Toy(String Id, String name, int unitPrice) {
		super(Id, name, unitPrice);
	}

	public Toy(String num, String Id, String name, int unitPrice,String Material,
			String ManufacturedBy, String Country,String ManufacturedDate,String RecommendedAge,String Caution) {
		super(Id, name, unitPrice);
		this.Material = Material;
		this.ManufacturedBy = ManufacturedBy;
		this.Country = Country;
		this.ManufacturedDate = ManufacturedDate;
		this.num=num;
		this.RecommendedAge=RecommendedAge;
		this.Caution=Caution;

	}
	public String getnum() {
		return num;
	}

	public void setnum(String num) {
		this.num = num;
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

	public String getMaterial() {
		return Material;
	}

	public void setMaterial(String Material) {
		this.Material = Material;
	}

	public String getManufacturedBy() {
		return ManufacturedBy;
	}

	public void setManufacturedBy (String ManufacturedBy) {
		this.ManufacturedBy = ManufacturedBy;
	}

	public String getCountry() {
		return Country;
	}

	public void setCountry(String Country) {
		this.Country = Country;
	}

	public String getManufacturedDate() {
		return ManufacturedDate;
	}

	public void setManufacturedDate(String ManufacturedDate) {
		this.ManufacturedDate = ManufacturedDate;
	}
	public String getRecommendedAge() {
		return RecommendedAge;
	}

	public void setRecommendedAge(String RecommendedAge) {
		this.RecommendedAge=RecommendedAge;   
	}
	public String getCaution() {
		return Caution;
	}

	public void setCaution(String Caution) {
		this.Caution=Caution;  
	}

	
	
}

