package bookmarketproject2;

public class MusicCD extends Item {
	private String num;
	private String Artists;
	private String Tracks;
	private String Producer;
	private String MusicGenre;
	private String ReleaseDate;

	public MusicCD(String Id, String name, int unitPrice) {
		super(Id, name, unitPrice);
	}

	public MusicCD(String num, String Id, String name, int unitPrice, String Artists, String Tracks, String Producer,
			String MusicGenre, String ReleaseDate) {
		super(Id, name, unitPrice);
		this.Artists =Artists;
		this.Tracks = Tracks;
		this.Producer = Producer;
		this.MusicGenre = MusicGenre;
		this.num=num;
		this.ReleaseDate=ReleaseDate;
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

	public String getArtists() {
		return Artists;
	}

	public void setArtists(String Artists) {
		this.Artists = Artists;
	}

	public String getTracks() {
		return Tracks;
	}

	public void setTracks (String Tracks) {
		this.Tracks = Tracks;
	}

	public String getProducer() {
		return Producer;
	}

	public void setProducer(String Producer) {
		this.Producer = Producer;
	}

	public String getMusicGenre() {
		return MusicGenre;
	}

	public void setMusicGenre(String MusicGenre) {
		this.MusicGenre = MusicGenre;
	}
	public String getReleaseDate() {
		return ReleaseDate;
	}

	public void setReleaseDate(String ReleaseDate) {
		this.ReleaseDate=ReleaseDate;   
	}

	
	
}


