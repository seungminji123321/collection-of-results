package bookmarketproject2;

public class MovieDvd extends Item {
	private String num;
	private String mainauthor;
	private String distributor;
	private String releaseDate;
	private String regioncode;
	private String sound;
	private String language;
	private String totaltime;
	private String grade;

	public MovieDvd(String Id, String name, int unitPrice) {
		super(Id, name, unitPrice);
	}

	public MovieDvd(String num, String Id, String name, int unitPrice, String mainauthor, String distributor, String releaseDate,
			String regioncode, String sound,String language,String totaltime,String grade) {
		super(Id, name, unitPrice);
		this.mainauthor = mainauthor;
		this.distributor = distributor;
		this.regioncode = regioncode;
		this.releaseDate = releaseDate;
		this.num=num;
		this.sound=sound;
		this.language=language;
		this.totaltime=totaltime;
		this.grade=grade;
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

	public String getmainauthor() {
		return mainauthor;
	}

	public void setmainauthor(String mainauthor) {
		this.mainauthor = mainauthor;
	}

	public String getdistributor() {
		return distributor;
	}

	public void setdistributor (String distributor) {
		this.distributor = distributor;
	}

	public String getregioncode() {
		return regioncode;
	}

	public void setregioncode(String regioncode) {
		this.regioncode = regioncode;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	public String getsound() {
		return sound;
	}

	public void setsound(String sound) {
		this.sound=sound;   
	}
	public String getlanguage() {
		return language;
	}

	public void setlanguage(String language) {
		this.language=language;  
	}
	public String gettotaltime() {
		return totaltime;
	}

	public void settotoaltiem(String totaltime) {
		this.totaltime=totaltime;
	}
	public String getgrade() {
		return grade;
	}

	public void setgrade(String grade) {
		this.grade=grade;
	}
	
	
}

