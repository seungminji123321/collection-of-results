package bookmarketproject2;

import bookmarketproject2.Book;

public class CartItem {

	// private String[] itemBook = new String[7];
	private Book itemBook;
	private MovieDvd itemMovie;
	private MusicCD itemMusic;
	private Toy itemToy;
	private String bookID;
	private int quantity;
	private int totalPrice;

	public CartItem() {}

	public CartItem(Book booklist) {
		this.itemBook = booklist;
		this.bookID = booklist.getBookId();
		this.quantity = 1;
		updateTotalPrice();
	}
	public CartItem(MovieDvd MovieDvdlist) {
		this.itemMovie = MovieDvdlist;
		this.bookID = MovieDvdlist.getBookId();
		this.quantity = 1;
		updateTotalPrice();
	}
	public CartItem(MusicCD MusicCDlist) {
		this.itemMusic = MusicCDlist;
		this.bookID = MusicCDlist.getBookId();
		this.quantity = 1;
		updateTotalPrice();
	}
	public CartItem(Toy Toylist) {
		this.itemToy = Toylist;
		this.bookID = Toylist.getBookId();
		this.quantity = 1;
		updateTotalPrice();
	}
	
//==================================================================================
	public Book getItemBook() {
		return itemBook;
	}

	public void setItemBook(Book itemBook) {
		this.itemBook = itemBook;
	}
	public MovieDvd getItemMovieDvd() {
		return itemMovie;
	}

	public void setItemMovieDvd(MovieDvd itemMovie) {
		this.itemMovie = itemMovie;
	}
	public MusicCD getItemMusic() {
		return itemMusic;
	}

	public void setItemMusic(MusicCD itemMusic) {
		this.itemMusic = itemMusic;
	}
	public Toy getItemToy() {
		return itemToy;
	}

	public void setItemToy(Toy itemToy) {
		this.itemToy = itemToy;
	}
//=======================================================================
	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getBookID() {
		return bookID;
	}

	public void setBookID(String bookID) {
		this.bookID = bookID;
		this.updateTotalPrice();
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
		this.updateTotalPrice();
	}
	public void setminusQuantity() {
		if(this.quantity>0) {
			this.quantity--;
			this.updateTotalPrice();
			
		}
		
		
	}

	public int getTotalPrice() {
		return totalPrice;
	}
//==================================================================================================
	public void updateTotalPrice() {
		// totalPrice = Integer.parseInt(this.itemBook[2]) * this.quantity;
		totalPrice = 0;
        if (itemBook != null) {
            totalPrice += itemBook.getUnitPrice() * quantity;
        }
        if (itemMovie != null) {
            totalPrice += itemMovie.getUnitPrice() * quantity;
        }
        if (itemMusic != null) {
            totalPrice += itemMusic.getUnitPrice() * quantity;
        }
        if (itemToy != null) {
            totalPrice += itemToy.getUnitPrice() * quantity;
        }
    }
	}


