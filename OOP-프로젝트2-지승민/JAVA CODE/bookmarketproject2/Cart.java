package bookmarketproject2;
import java.util.ArrayList;
import bookmarketproject2.Book;

public class Cart implements CartInterface {

	public ArrayList<CartItem> mCartItem = new ArrayList<CartItem>();

	public static int mCartCount = 0;

	public Cart() {

	}
//=======================================================================================================================4개 print
	public void printBookList(ArrayList<Book> booklist) {

		for (int i = 0; i < booklist.size(); i++) {
			Book bookitem = booklist.get(i);
			System.out.print(bookitem.getBookId() + " | ");
			System.out.print(bookitem.getName() + " | ");
			System.out.print(bookitem.getUnitPrice() + " | ");
			System.out.print(bookitem.getAuthor() + " | ");
			System.out.print(bookitem.getDescription() + " | ");
			System.out.print(bookitem.getCategory() + " | ");
			System.out.print(bookitem.getReleaseDate()+" | ");
			System.out.print(bookitem.getCompany());
			System.out.println("");
		}
	}
	public void printMovieDvdList(ArrayList<MovieDvd> MovieDvdlist) {
		for (int i = 0; i < MovieDvdlist.size(); i++) {
			MovieDvd MovieDvdlistitem = MovieDvdlist.get(i);
			System.out.print(MovieDvdlistitem.getBookId() + " | ");
			System.out.print(MovieDvdlistitem.getName() + " | ");
			System.out.print(MovieDvdlistitem.getUnitPrice() + " | ");
			System.out.print(MovieDvdlistitem.getmainauthor() + " | ");
			System.out.print(MovieDvdlistitem.getdistributor() + " | ");
			System.out.print(MovieDvdlistitem.getregioncode() + " | ");
			System.out.print(MovieDvdlistitem.getReleaseDate()+" | ");
			System.out.print(MovieDvdlistitem.getsound()+" | ");
			System.out.print(MovieDvdlistitem.getlanguage()+" | ");
			System.out.print(MovieDvdlistitem.gettotaltime()+" | ");
			System.out.print(MovieDvdlistitem.getgrade());			
			System.out.println("");
		}
	}
	public void printMusicCDList(ArrayList<MusicCD> MusicCDlist) {
		for (int i = 0; i < MusicCDlist.size(); i++) {
			MusicCD MusicCDlistitem = MusicCDlist.get(i);
			System.out.print(MusicCDlistitem.getBookId() + " | ");
			System.out.print(MusicCDlistitem.getName() + " | ");
			System.out.print(MusicCDlistitem.getUnitPrice() + " | ");
			System.out.print(MusicCDlistitem.getArtists() + " | ");
			System.out.print(MusicCDlistitem.getTracks() + " | ");
			System.out.print(MusicCDlistitem.getProducer() + " | ");
			System.out.print(MusicCDlistitem.getMusicGenre()+" | ");
			System.out.print(MusicCDlistitem.getReleaseDate());		
			System.out.println("");
		}
	}
	public void printToyList(ArrayList<Toy> Toylist) {
		for (int i = 0; i < Toylist.size(); i++) {
			Toy Toylistitem = Toylist.get(i);
			System.out.print(Toylistitem.getBookId() + " | ");
			System.out.print(Toylistitem.getName() + " | ");
			System.out.print(Toylistitem.getUnitPrice() + " | ");
			System.out.print(Toylistitem.getMaterial() + " | ");
			System.out.print(Toylistitem.getManufacturedBy() + " | ");
			System.out.print(Toylistitem.getCountry() + " | ");
			System.out.print(Toylistitem.getManufacturedDate()+" | ");
			System.out.print(Toylistitem.getRecommendedAge()+" | ");
			System.out.print(Toylistitem.getCaution());		
			System.out.println("");
		}
	}
//============================================================================================================//삽입
	public void insertBook(Book book) {
		// mCartItem[mCartCount++] = new CartItem(book);
		CartItem bookitem = new CartItem(book);
		mCartItem.add(bookitem);
		mCartCount = mCartItem.size();
	}
	public void insertMovieDvd(MovieDvd MovieDvd) {
		// mCartItem[mCartCount++] = new CartItem(book);
		CartItem MovieDvditem = new CartItem(MovieDvd);
		mCartItem.add(MovieDvditem);
		mCartCount = mCartItem.size();
	}
	public void insertMusicCD(MusicCD MusicCD) {
		// mCartItem[mCartCount++] = new CartItem(book);
		CartItem MusicCDitem = new CartItem(MusicCD);
		mCartItem.add(MusicCDitem);
		mCartCount = mCartItem.size();
	}
	public void insertToy(Toy Toy) {
		// mCartItem[mCartCount++] = new CartItem(book);
		CartItem Toyitem = new CartItem(Toy);
		mCartItem.add(Toyitem);
		mCartCount = mCartItem.size();
	}
//===================================================================================================================	

	public void deleteBook() {
		// mCartItem = new CartItem[NUM_BOOK];
		mCartItem.clear();
		mCartCount = 0;
	}

	public void printCart() {
		System.out.println("장바구니 상품 목록 :");
		System.out.println("---------------------------------------------");
		System.out.println("    제품ID \t|     수량 \t|      합계");



		for (int i = 0; i < mCartItem.size(); i++) { // 13
			System.out.print("    " + mCartItem.get(i).getBookID() + " \t| ");
			System.out.print("    " + mCartItem.get(i).getQuantity() + " \t| ");
			System.out.print("    " + mCartItem.get(i).getTotalPrice());
			System.out.println("  ");
		}

		System.out.println("---------------------------------------------");
	}
//=================================================================================================// 여기 수정
	public boolean isCartInBook(String bookId) {
		boolean flag = false;

		for (int i = 0; i < mCartItem.size(); i++) {
			if (bookId.equals(mCartItem.get(i).getBookID())) {
				mCartItem.get(i).setQuantity(mCartItem.get(i).getQuantity() + 1);
				flag = true;
			}
		}
		return flag;
	}

	public void removeCart(int numId) {

		mCartItem.remove(numId);
		mCartCount = mCartItem.size();
	}
	public void minusCart(int numId) {
		mCartItem.get(numId).setminusQuantity();
	}
}
