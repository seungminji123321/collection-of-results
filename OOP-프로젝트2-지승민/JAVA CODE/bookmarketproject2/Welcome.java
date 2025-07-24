package bookmarketproject2;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import bookmarketproject2.Book;
import bookmarketproject2.Cart;
import bookmarketproject2.Admin;
import bookmarketproject2.User;
import bookmarketproject2.CartException;
import java.util.ArrayList;

public class Welcome {
	static final int NUM_BOOK = 3;
	static final int NUM_ITEM = 7;
	// static CartItem[] mCartItem = new CartItem[NUM_BOOK];
	// static int mCartCount = 0;
	static Cart mCart = new Cart();
	static User mUser;

	public static void main(String[] args) {
		ArrayList<Book> mBookList; // 책
		ArrayList<MovieDvd> mMovieList; //영화
		ArrayList<MusicCD> mMusicCD;//음악
		ArrayList<Toy> mToy; //장난감 
		int mTotalBook = 0;

		Scanner input = new Scanner(System.in);
		System.out.print("당신의 이름을 입력하세요 : ");
		String userName = input.next();

		System.out.print("연락처를 입력하세요 : ");
		int userMobile = input.nextInt();

		mUser = new User(userName, userMobile);

		String greeting = "Welcome to Shopping Mall";
		String tagline = "Welcome to Book Market!";

		boolean quit = false;

		while (!quit) {
			System.out.println("***********************************************");
			System.out.println("\t" + greeting);
			System.out.println("\t" + tagline);
			/*
			 * System.out.println("***********************************************");
			 * System.out.println(" 1. 고객 정보 확인하기 \t4. 바구니에 항목 추가하기");
			 * System.out.println(" 2. 장바구니 상품 목록 보기 \t5. 장바구니에 항목수량 줄이기");
			 * System.out.println(" 3. 장바구니 비우기 \t6. 장바구니의 항목 삭제하기");
			 * System.out.println(" 7. 영수증 표시하기 \t8. 종료");
			 * System.out.println("***********************************************");
			 */

			menuIntroduction();

			try {
				System.out.print("메뉴 번호를 선택해주세요 ");
				int n = input.nextInt();
				// System.out.println(n + "번을 선택했습니다");

				if (n < 1 || n > 10) {
					System.out.println("1부터 10까지의 숫자를 입력하세요.");
				} else {
					switch (n) {

					case 1:
						// System.out.println("현재 고객 정보 : ");
						// System.out.println("이름 " + userName + " 연락처 " + userMobile);
						menuGuestInfo(userName, userMobile);
						break;
					case 2:
						// System.out.println("장바구니 상품 목록 보기 :");
						menuCartItemList();
						break;
					case 3:
						// System.out.println("장바구니 비우기");
						menuCartClear();
						break;
					case 4: 
						Scanner input1 = new Scanner(System.in);
						System.out.println("세부 항목을 고르시오");
						System.out.println("1.책 2.영화 3.음악 4.장난감");
						int n1= input1.nextInt();
						switch(n1) { 
						case 1: //책
							mBookList = new ArrayList<Book>();
							menuCartAddItem1(mBookList);
							break;
						case 2: //영화
							mMovieList= new ArrayList<MovieDvd>();
							menuCartAddItem2(mMovieList);
							break;
						case 3: //음악
							mMusicCD= new ArrayList<MusicCD>();
							menuCartAddItem3(mMusicCD);
						    break;
						case 4: //장난감 
							mToy=new ArrayList<Toy>();
							menuCartAddItem4(mToy);
							break;
						default:
							System.out.println("1부터 4까지 숫자만 입력해주세요.");
							break;
						}
						break;
		      
					case 5:
						// System.out.println("5. 장바구니의 항목 수량 줄이기");
						menuCartminusItemCount();
						break;
					case 6:
						// System.out.println("6. 장바구니의 항목 삭제하기");
						menuCartRemoveItem();
						break;
					case 7:
						// System.out.println("7. 영수증 표시하기");
						menuCartBill();
						break;
					case 8:
						// System.out.println("8. 출판사 별 도서 찾기 검색");
						mBookList = new ArrayList<Book>();
						searchCompany(mBookList);
						break;
					case 9:
						menuAdminLogin();
						break;
					case 10:
						menuExit();
						quit = true;
						break;
						
					}
				}

			} catch (CartException e) {
				System.out.println(e.getMessage());
				//quit = true;  //24 시간 가동되게 만들어버리자
			} catch (Exception e) {
				System.out.println("올바르지 않은 메뉴 선택으로 종료합니다.");
				//quit = true;
			}
		}

	}
//=================================================================================================================
	public static void menuIntroduction() {
		System.out.println("***********************************************");
		System.out.println(" 1. 고객 정보 확인하기 \t4. 바구니에 항목 추가하기");
		System.out.println(" 2. 장바구니 상품 목록 보기 \t5. 장바구니의 항목 수량 줄이기");
		System.out.println(" 3. 장바구니 비우기 \t6. 장바구니의 항목 삭제하기");
		System.out.println(" 7. 영수증 표시하기 \t8.출판사 별 도서 찾기");
		System.out.println(" 9. 관리자 로그인 \t10.종료");
		System.out.println("***********************************************");
	}
//=============================================================================================================== 1번
	public static void menuGuestInfo(String name, int mobile) {
		System.out.println("현재 고객 정보 : ");
		System.out.println("이름 " + mUser.getName() + "   연락처 " + mUser.getPhone());
	}
	//=============================================================================================================== 2번
	public static void menuCartItemList() {
		if (mCart.mCartCount >= 0) {
			mCart.printCart();
		}
	}
	//=============================================================================================================== 3번
	public static void menuCartClear() throws CartException {
		// System.out.println("장바구니 비우기");
		if (mCart.mCartCount == 0)
			throw new CartException("장바구니에 항목이 없습니다");
		// System.out.println("장바구니에 항목이 없습니다");
		else {
			System.out.println("장바구니에 모든 항목을 삭제하겠습니까?  Y  | N ");
			Scanner input = new Scanner(System.in);
			String str = input.nextLine();

			if (str.toUpperCase().equals("Y")) {
				System.out.println("장바구니에 모든 항목을 삭제했습니다");
				// mCart.mCartItem = new CartItemBook[NUM_BOOK];
				mCart.deleteBook();
			}
		}
	}
	//=============================================================================================================== 4-1번
	public static void menuCartAddItem1(ArrayList<Book> booklist) {
		// System.out.println("장바구니에 항목 추가하기 : ");

		BookList(booklist);

		mCart.printBookList(booklist); //cart 클래스임

		boolean quit = false;

		while (!quit) {

			System.out.print("장바구니에 추가할 도서의 ID를 입력하세요 :");

			Scanner input = new Scanner(System.in);
			String str = input.nextLine();

			boolean flag = false;
			int numId = -1;

			for (int i = 0; i < booklist.size(); i++) {
				if (str.equals(booklist.get(i).getBookId())) {
					numId = i;
					flag = true;
					break;
				}
			}
			if (flag) {
				System.out.println("장바구니에 추가하겠습니까?  Y  | N ");
				str = input.nextLine();
				if (str.toUpperCase().equals("Y")) {
					System.out.println(booklist.get(numId).getBookId() + " 도서가 장바구니에 추가되었습니다.");
					// 카트에 넣기
					if (!isCartInBook(booklist.get(numId).getBookId())) {
						mCart.insertBook(booklist.get(numId));
					}
				}
				quit = true;
			} else
				System.out.println("해당 제품은 취급하지 않습니다. 다시 입력해 주세요.");

		}
	}
	public static void menuCartAddItem2(ArrayList<MovieDvd> MovieDvd) {
		MoiveDvdList(MovieDvd);
		mCart.printMovieDvdList(MovieDvd);
		boolean quit = false;

		while (!quit) {

			System.out.print("장바구니에 추가할 영화의 ID를 입력하세요 :");

			Scanner input = new Scanner(System.in);
			String str = input.nextLine();

			boolean flag = false;
			int numId = -1;

			for (int i = 0; i < MovieDvd.size(); i++) {
				if (str.equals(MovieDvd.get(i).getBookId())) {
					numId = i;
					flag = true;
					break;
				}
			}
			if (flag) {
				System.out.println("장바구니에 추가하겠습니까?  Y  | N ");
				str = input.nextLine();
				if (str.toUpperCase().equals("Y")) {
					System.out.println(MovieDvd.get(numId).getBookId() + " 도서가 장바구니에 추가되었습니다.");
					// 카트에 넣기
					if (!isCartInBook(MovieDvd.get(numId).getBookId())) {
						mCart.insertMovieDvd(MovieDvd.get(numId)); // 수정해야함
					}
				}
				quit = true;
			} else
				System.out.println("해당 제품은 취급하지 않습니다. 다시 입력해 주세요.");

		}
		
	}
	public static void menuCartAddItem3(ArrayList<MusicCD> MusicCD) {
		MusicCDList(MusicCD);
		mCart.printMusicCDList(MusicCD);
		boolean quit = false;

		while (!quit) {

			System.out.print("장바구니에 추가할 음악의 ID를 입력하세요 :");

			Scanner input = new Scanner(System.in);
			String str = input.nextLine();

			boolean flag = false;
			int numId = -1;

			for (int i = 0; i < MusicCD.size(); i++) {
				if (str.equals(MusicCD.get(i).getBookId())) {
					numId = i;
					flag = true;
					break;
				}
			}
			if (flag) {
				System.out.println("장바구니에 추가하겠습니까?  Y  | N ");
				str = input.nextLine();
				if (str.toUpperCase().equals("Y")) {
					System.out.println(MusicCD.get(numId).getBookId() + " 도서가 장바구니에 추가되었습니다.");
					// 카트에 넣기
					if (!isCartInBook(MusicCD.get(numId).getBookId())) {
						mCart.insertMusicCD(MusicCD.get(numId)); // 수정해야함
					}
				}
				quit = true;
			} else
				System.out.println("해당 제품은 취급하지 않습니다. 다시 입력해 주세요.");

		}
		
	}
	public static void menuCartAddItem4(ArrayList<Toy> Toy) {
		ToyList(Toy);
		mCart.printToyList(Toy);
		boolean quit = false;

		while (!quit) {

			System.out.print("장바구니에 추가할 장난감의 ID를 입력하세요 :");

			Scanner input = new Scanner(System.in);
			String str = input.nextLine();

			boolean flag = false;
			int numId = -1;

			for (int i = 0; i < Toy.size(); i++) {
				if (str.equals(Toy.get(i).getBookId())) {
					numId = i;
					flag = true;
					break;
				}
			}
			if (flag) {
				System.out.println("장바구니에 추가하겠습니까?  Y  | N ");
				str = input.nextLine();
				if (str.toUpperCase().equals("Y")) {
					System.out.println(Toy.get(numId).getBookId() + " 도서가 장바구니에 추가되었습니다.");
					// 카트에 넣기
					if (!isCartInBook(Toy.get(numId).getBookId())) {
						mCart.insertToy(Toy.get(numId)); // 수정해야함
					}
				}
				quit = true;
			} else
				System.out.println("해당 제품은 취급하지 않습니다. 다시 입력해 주세요.");

		}
		
	}
	
	//=============================================================================================================== 5번
	public static void menuCartminusItemCount() throws CartException {
		System.out.println("5. 장바구니의 항목 수량 줄이기");
		if (mCart.mCartCount == 0) {
			throw new CartException("장바구니에 항목이 없습니다");
			
		}
		
		// System.out.println("장바구니에 항목이 없습니다");
		else {
			menuCartItemList();
			boolean quit = false;
			while (!quit) {
				System.out.print("장바구니에서 수량을 줄일 제품의 ID를 입력해 주세요 :");
				Scanner input = new Scanner(System.in);
				String str = input.nextLine();
				boolean flag = false;
				int numId = -1;

				for (int i = 0; i < mCart.mCartCount; i++) {
					if (str.equals(mCart.mCartItem.get(i).getBookID())) {
						numId = i;
						flag = true;
						break;
					}
				}

				if (flag) {
					System.out.println("장바구니의 수량을 줄이겠습니까?  Y  | N ");
					str = input.nextLine();
					if (str.toUpperCase().equals("Y")) {
						if(mCart.mCartItem.get(numId).getQuantity()==1) {
							mCart.removeCart(numId); //수량이 1개면 그냥 삭제
							System.out.println( " 장바구니에서 수량이 0이므로 삭제되었습니다.");
						}
						else {
						mCart.minusCart(numId);
						System.out.println(mCart.mCartItem.get(numId).getQuantity() + " 장바구니에서 수량이 줄어들었습니다.");
						}
						
					}
					quit = true;
				} else
					System.out.println("다시 입력해 주세요");
			}
		}
		
	}
	//=============================================================================================================== 6번
	public static void menuCartRemoveItem() throws CartException {
		// System.out.println("6. 장바구니의 항목 삭제하기");
		if (mCart.mCartCount == 0)
			throw new CartException("장바구니에 항목이 없습니다");
		// System.out.println("장바구니에 항목이 없습니다");
		else {
			menuCartItemList();
			boolean quit = false;
			while (!quit) {
				System.out.print("장바구니에서 삭제할 제품의 ID를 입력하세요 :");
				Scanner input = new Scanner(System.in);
				String str = input.nextLine();
				boolean flag = false;
				int numId = -1;

				for (int i = 0; i < mCart.mCartCount; i++) {
					if (str.equals(mCart.mCartItem.get(i).getBookID())) {
						numId = i;
						flag = true;
						break;
					}
				}

				if (flag) {
					System.out.println("장바구니의 항목을 삭제하겠습니까?  Y  | N ");
					str = input.nextLine();
					if (str.toUpperCase().equals("Y")) {

						System.out.println(mCart.mCartItem.get(numId).getBookID() + " 장바구니에서  해당 제품이 삭제되었습니다.");
						mCart.removeCart(numId);
					}
					quit = true;
				} else
					System.out.println("다시 입력해 주세요");
			}
		}
	}
	//=============================================================================================================== 7번
	public static void menuCartBill() throws CartException {
		// System.out.println("7. 영수증 표시하기");
		if (mCart.mCartCount == 0)
			throw new CartException("장바구니에 항목이 없습니다");
		// System.out.println("장비구니에 항목이 없습니다");
		else {
			System.out.println("배송받을 분은 고객정보와 같습니까? Y  | N ");
			Scanner input = new Scanner(System.in);
			String str = input.nextLine();

			if (str.toUpperCase().equals("Y")) {
				System.out.print("배송지를 입력해주세요 ");
				String address = input.nextLine();
				printBill(mUser.getName(), String.valueOf(mUser.getPhone()), address);
			}

			else {
				System.out.print("배송받을 고객명을 입력하세요 ");
				String name = input.nextLine();
				System.out.print("배송받을 고객의 연락처를 입력하세요 ");
				String phone = input.nextLine();
				System.out.print("배송받을 고객의 배송지를 입력해주세요 ");
				String address = input.nextLine();
				printBill(name, phone, address);
			}
		}
	}
	//=============================================================================================================== 8번
	public static void menuExit() {
		System.out.println("10. 종료");
	}

	public static void BookList(String[][] book) {

		book[0][0] = "ISBN1234";
		book[0][1] = "쉽게 배우는 JSP 웹 프로그래밍";
		book[0][2] = "27000";
		book[0][3] = "송미영";
		book[0][4] = "단계별로 쇼핑몰을 구현하며 배우는 JSP 웹 프로그래밍 ";
		book[0][5] = "IT전문서";
		book[0][6] = "2018/10/08";

		book[1][0] = "ISBN1235";
		book[1][1] = "안드로이드 프로그래밍";
		book[1][2] = "33000";
		book[1][3] = "우재남";
		book[1][4] = "실습 단계별 명쾌한 멘토링!";
		book[1][5] = "IT전문서";
		book[1][6] = "2022/01/22";

		book[2][0] = "ISBN1236";
		book[2][1] = "스크래치";
		book[2][2] = "22000";
		book[2][3] = "고광일";
		book[2][4] = "컴퓨팅 사고력을 키우는 블록 코딩";
		book[2][5] = "컴퓨터입문";
		book[2][6] = "2019/06/10";
	}
	//=============================================================================================================== 책 장바구니에 넣기
	public static boolean isCartInBook(String bookId) {
		return mCart.isCartInBook(bookId);
	}
	//=============================================================================================================== 관리자
	public static void menuAdminLogin() {
		System.out.println("관리자 정보를 입력하세요");

		Scanner input = new Scanner(System.in);
		System.out.print("아이디 : ");
		String adminId = input.next();

		System.out.print("비밀번호 : ");
		String adminPW = input.next();

		Admin admin = new Admin(mUser.getName(), mUser.getPhone());
		if (adminId.equals(admin.getId()) && adminPW.equals(admin.getPassword())) {

			String[] writeBook = new String[10];
			String[] writeMovie= new String[12];
			String[] writeMusic= new String[9];
			String[] writeToy=   new String[10];
			System.out.println("정보를 추가하겠습니까?  Y  | N ");
			String str = input.next();

			if (str.toUpperCase().equals("Y")) {
				Scanner input11 = new Scanner(System.in);
				System.out.println("추가 할 세부 항목을 고르시오");
				System.out.println("1.책 2.영화 3.음악 4.장난감");
				int n1= input11.nextInt();	
		        Date date = new Date();
		        SimpleDateFormat formatter = new SimpleDateFormat("yyMMddhhmmss");
		        String strDate = formatter.format(date);
				switch(n1){
				case 1:
				        int newBookNumber = readBookLastNumber() + 1;
				        writeBook[0] = String.valueOf(newBookNumber);
				        input.nextLine();// 버퍼 삭제
				        System.out.print("책ID : ");
				        String st1 = input.nextLine();
				        writeMusic[1] = "ISBN" + st1;

				        System.out.print("도서명 : ");
				        writeBook[2] = input.nextLine();
				        System.out.print("가격 : ");
				        writeBook[3] = input.nextLine();
				        System.out.print("저자 : ");
				        writeBook[4] = input.nextLine();
				        System.out.print("설명 : ");
				        writeBook[5] = input.nextLine();
				        System.out.print("분야 : ");
				        writeBook[6] = input.nextLine();
				        System.out.print("출판일 : ");
				        writeBook[7] = input.nextLine();
				        System.out.print("출판사:");    
				        writeBook[8] = input.nextLine();

				        try (FileWriter fw = new FileWriter("Book.csv", true)) {
				            StringBuilder hang = new StringBuilder();
				            for (int i = 0; i < writeBook.length; i++) {
				                hang.append(writeBook[i]);
				                if (i < writeBook.length - 1) {
				                    hang.append(",");// 쉼표로 행으로 구별할 수 있게 세팅
				                }
				            }
				            hang.append("\n");
				            fw.write(hang.toString());
				            System.out.println("새 도서 정보가 저장되었습니다.");
				            break;
				        } catch (Exception e) {
				            System.out.println(e);
				        }
				case 2:
			        int newMovieNumber = readMovieLastNumber() + 1;
			        writeMovie[0] = String.valueOf(newMovieNumber);
			        input.nextLine();// 버퍼 삭제
			        System.out.print("영화ID : ");
			        String st2 = input.nextLine();
			        writeMusic[1] = "DVD" + st2;

			        System.out.print("영화명 : ");
			        writeMovie[2] = input.nextLine();
			        System.out.print("가격 : ");
			        writeMovie[3] = input.nextLine();
			        System.out.print("주 배우 : ");
			        writeMovie[4] = input.nextLine();
			        System.out.print("영화 제작사 : ");
			        writeMovie[5] = input.nextLine();
			        System.out.print("분야 : ");
			        writeMovie[6] = input.nextLine();
			        System.out.print("출시일 : ");
			        writeMovie[7] = input.nextLine();
			        System.out.print("지역코드:");    
			        writeMovie[8] = input.nextLine();
			        System.out.print("사운드:");    
			        writeMovie[9] = input.nextLine();
			        System.out.print("언어:");    
			        writeMovie[10] = input.nextLine();
			        System.out.print("전체 상영 시간:");    
			        writeMovie[11] = input.nextLine();
			        System.out.print("연령제한:");    
			        writeMovie[12] = input.nextLine();

			        try (FileWriter fw = new FileWriter("Movie.csv", true)) {
			            StringBuilder hang = new StringBuilder();
			            for (int i = 0; i < writeMovie.length; i++) {
			                hang.append(writeMovie[i]);
			                if (i < writeMovie.length - 1) {
			                    hang.append(",");// 쉼표로 행으로 구별할 수 있게 세팅
			                }
			            }
			            hang.append("\n");
			            fw.write(hang.toString());
			            System.out.println("새 영화 정보가 저장되었습니다.");
			            break;
			        } catch (Exception e) {
			            System.out.println(e);
			        }
				case 3:
			        int newMusicNumber = readMusicLastNumber() + 1;
			        writeMusic[0] = String.valueOf(newMusicNumber);
			        input.nextLine();// 버퍼 삭제
			        System.out.print("음악ID : ");
			        String st3 = input.nextLine();
			        writeMusic[1] = "Music" + st3;

			        System.out.print("타이틀 : ");
			        writeMusic[2] = input.nextLine();
			        System.out.print("가격 : ");
			        writeMusic[3] = input.nextLine();
			        System.out.print("배우 : ");
			        writeMusic[4] = input.nextLine();
			        System.out.print("트랙 : ");
			        writeMusic[5] = input.nextLine();
			        System.out.print("생산자 : ");
			        writeMusic[6] = input.nextLine();
			        System.out.print("장르 : ");
			        writeMusic[7] = input.nextLine();
			        System.out.print("출시일:");    
			        writeMusic[8] = input.nextLine();

			        try (FileWriter fw = new FileWriter("MusicCD.csv", true)) {
			            StringBuilder hang = new StringBuilder();
			            for (int i = 0; i < writeMusic.length; i++) {
			                hang.append(writeMusic[i]);
			                if (i < writeMusic.length - 1) {
			                    hang.append(",");// 쉼표로 행으로 구별할 수 있게 세팅
			                }
			            }
			            hang.append("\n");
			            fw.write(hang.toString());
			            System.out.println("새 음악 정보가 저장되었습니다.");
			            break;
			        } catch (Exception e) {
			            System.out.println(e);
			        }
				case 4:
					 int newToyNumber = readToyLastNumber() + 1;
				        writeToy[0] = String.valueOf(newToyNumber);
				        input.nextLine();// 버퍼 삭제
				        System.out.print("장난감ID : ");
				        writeToy[1] =input.nextLine();
				        System.out.print("이름 : ");
				        writeToy[2] = input.nextLine();
				        System.out.print("가격 : ");
				        writeToy[3] = input.nextLine();
				        System.out.print("재료 : ");
				        writeToy[4] = input.nextLine();
				        System.out.print("제조사 : ");
				        writeToy[5] = input.nextLine();
				        System.out.print("나라 : ");
				        writeToy[6] = input.nextLine();
				        System.out.print("제작날짜 : ");
				        writeToy[7] = input.nextLine();
				        System.out.print("추천 나이제한:");    
				        writeToy[8] = input.nextLine();
				        System.out.print("유의점:");    
				        writeToy[9] = input.nextLine();

				        try (FileWriter fw = new FileWriter("Toy.csv", true)) {
				            StringBuilder hang = new StringBuilder();
				            for (int i = 0; i < writeToy.length; i++) {
				                hang.append(writeToy[i]);
				                if (i < writeToy.length - 1) {
				                    hang.append(",");// 쉼표로 행으로 구별할 수 있게 세팅
				                }
				            }
				            hang.append("\n");
				            fw.write(hang.toString());
				            System.out.println("새 장난감 정보가 저장되었습니다.");
				            break;
				        } catch (Exception e) {
				            System.out.println(e);
				        }
				default:
					System.out.println("1부터 4까지 숫자만 입력해주세요.");
					break;
				}
				
						
			}
		 else {
				System.out.println("이름 " + admin.getName() + " 연락처 " + admin.getPhone());
				System.out.println("아이디 " + admin.getId() + " 비밀번호 " + admin.getPassword());
		 }
		} else {
			System.out.println("관리자 정보가 일치하지 않습니다."); 
			}
	}

	//====================================================================================================
	public static int readBookLastNumber() {
	    int lastNumber = 0;
	    try (FileReader fr = new FileReader("Book.csv");
	         BufferedReader br = new BufferedReader(fr)) {
	        String lastLine;
	        while ((lastLine = br.readLine()) != null) {
	            String[] parts = lastLine.split(",");
	            if (parts.length > 0) {
	                String id = parts[0];
	                if (id.matches("\\d+")) { // 번호만 있는지 확인
	                    int number = Integer.parseInt(id);
	                    if (number > lastNumber) {
	                        lastNumber = number;
	                    }
	                }
	            }
	        }
	    } catch (Exception e) {
	    	System.out.println(e);
	    }
	    return lastNumber;
	}
	public static int readMovieLastNumber() {
	    int lastNumber = 0;
	    try (FileReader fr = new FileReader("MovieDvd.csv");
	         BufferedReader br = new BufferedReader(fr)) {
	        String lastLine;
	        while ((lastLine = br.readLine()) != null) {
	            String[] parts = lastLine.split(",");
	            if (parts.length > 0) {
	                String id = parts[0];
	                if (id.matches("\\d+")) { // 번호만 있는지 확인
	                    int number = Integer.parseInt(id);
	                    if (number > lastNumber) {
	                        lastNumber = number;
	                    }
	                }
	            }
	        }
	    } catch (Exception e) {
	    	System.out.println(e);
	    }
	    return lastNumber;
	}
	public static int readMusicLastNumber() {
	    int lastNumber = 0;
	    try (FileReader fr = new FileReader("MusicCD.csv");
	         BufferedReader br = new BufferedReader(fr)) {
	        String lastLine;
	        while ((lastLine = br.readLine()) != null) {
	            String[] parts = lastLine.split(",");
	            if (parts.length > 0) {
	                String id = parts[0];
	                if (id.matches("\\d+")) { // 번호만 있는지 확인
	                    int number = Integer.parseInt(id);
	                    if (number > lastNumber) {
	                        lastNumber = number;
	                    }
	                }
	            }
	        }
	    } catch (Exception e) {
	    	System.out.println(e);
	    }
	    return lastNumber;
	}
	public static int readToyLastNumber() {
	    int lastNumber = 0;
	    try (FileReader fr = new FileReader("Toy.csv");
	         BufferedReader br = new BufferedReader(fr)) {
	        String lastLine;
	        while ((lastLine = br.readLine()) != null) {
	            String[] parts = lastLine.split(",");
	            if (parts.length > 0) {
	                String id = parts[0];
	                if (id.matches("\\d+")) { // 번호만 있는지 확인
	                    int number = Integer.parseInt(id);
	                    if (number > lastNumber) {
	                        lastNumber = number;
	                    }
	                }
	            }
	        }
	    } catch (Exception e) {
	    	System.out.println(e);
	    }
	    return lastNumber;
	}

    //========================================================================================================
	//=============================================================================================================== 북 리스트
	public static void BookList(ArrayList<Book> booklist) {
		setFileToBookList(booklist);
	}
	public static void MoiveDvdList(ArrayList<MovieDvd> MovieDvdlist) {
		setFileToMovieDvdList(MovieDvdlist);
	}
	public static void MusicCDList(ArrayList<MusicCD> MusicCD) {
		setFileToMusicCDList(MusicCD);
	}
	public static void ToyList(ArrayList<Toy> Toy) {
		setFileToToyList(Toy);
	}
	//=============================================================================================================== 영수증 출력
	public static void printBill(String name, String phone, String address) {

		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		String strDate = formatter.format(date);
		System.out.println();
		System.out.println("---------------배송 받을 고객 정보----------------");
		System.out.println("고객명 : " + name + "   \t\t연락처 : " + phone);
		System.out.println(" 배송지 : " + address + "t\t발송일 : " + strDate);
		mCart.printCart();

		int sum = 0;
		for (int i = 0; i < mCart.mCartCount; i++)
			sum += mCart.mCartItem.get(i).getTotalPrice();

		System.out.println("\t\t\t주문 총금액 : " + sum + "원\n");
		System.out.println("----------------------------------------------");
		System.out.println();
	}
//=======================================================================================================================
	public static int totalFileToBookList() { //이건 필요없는거 

		try {

			FileReader fr = new FileReader("book.txt");
			BufferedReader reader = new BufferedReader(fr);

			String str;

			int num = 0;

			while ((str = reader.readLine()) != null) {
				if (str.contains("ISBN"))
					++num;
			}

			reader.close();
			fr.close();
			return num;
		} catch (Exception e) {
			System.out.println(e);
		}
		return 0;
	}
	//=============================================================================================================== 북 csv파일 불러오기 
	public static void setFileToBookList(ArrayList<Book> booklist) {
		try {
			FileReader fr = new FileReader("Book.csv");
			BufferedReader reader = new BufferedReader(fr);

			String line;
			reader.readLine();// 헤더 건너뛰기
			
			
			while ((line = reader.readLine()) != null) {
				String[] readBook = line.split(","); // 쉼표 기준 나누기 
				Book bookitem = new Book(readBook[0], readBook[1],readBook[2], Integer.parseInt(readBook[3]),
						readBook[4], readBook[5], readBook[6], readBook[7],readBook[8]);
				booklist.add(bookitem);
			}

			reader.close();
			fr.close();

		} catch (Exception e) {
			System.out.println(e);
		}
	}
	//=========================================================================================영화 csv파일 불러오기 
	public static void setFileToMovieDvdList(ArrayList<MovieDvd> MovieDvdlist) {
		try {
			FileReader fr = new FileReader("MovieDvd.csv");
			BufferedReader reader = new BufferedReader(fr);

			String line;
			reader.readLine();// 헤더 건너뛰기
			
			
			while ((line = reader.readLine()) != null) {
				String[] readBook = line.split(","); // 쉼표 기준 나누기 
				MovieDvd MovieDvd1 = new MovieDvd(readBook[0], readBook[1],readBook[2], Integer.parseInt(readBook[3]),
						readBook[4], readBook[5], readBook[6], readBook[7], readBook[8], readBook[9], readBook[10], readBook[11]);
				MovieDvdlist.add(MovieDvd1);
			}

			reader.close();
			fr.close();

		} catch (Exception e) {
			System.out.println(e);
		}
	}
	public static void setFileToMusicCDList(ArrayList<MusicCD> MusicCDlist) {
		try {
			FileReader fr = new FileReader("MusicCD.csv");
			BufferedReader reader = new BufferedReader(fr);

			String line;
			reader.readLine();// 헤더 건너뛰기
			
			
			while ((line = reader.readLine()) != null) {
				String[] readBook = line.split(","); // 쉼표 기준 나누기 
				MusicCD MusicCD1 = new MusicCD(readBook[0], readBook[1],readBook[2], Integer.parseInt(readBook[3]),
						readBook[4], readBook[5], readBook[6], readBook[7], readBook[8]);
				MusicCDlist.add(MusicCD1);
			}

			reader.close();
			fr.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
	public static void setFileToToyList(ArrayList<Toy> Toylist) {
		try {
			FileReader fr = new FileReader("Toy.csv");
			BufferedReader reader = new BufferedReader(fr);

			String line;
			reader.readLine();// 헤더 건너뛰기
			
			
			while ((line = reader.readLine()) != null) {
				String[] readBook = line.split(","); // 쉼표 기준 나누기 
				Toy Toy1 = new Toy(readBook[0], readBook[1],readBook[2], Integer.parseInt(readBook[3]),
						readBook[4], readBook[5], readBook[6], readBook[7], readBook[8], readBook[9]);
				Toylist.add(Toy1);
			}

			reader.close();
			fr.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
	public static void searchCompany(ArrayList<Book> booklist) {
		try {
			FileReader fr = new FileReader("Book.csv");
			BufferedReader reader = new BufferedReader(fr);
			Scanner input = new Scanner(System.in);
			System.out.println("찾고싶은 도서의 출판사를 입력하시오");
			String str=input.nextLine();

			String line;
			reader.readLine();// 헤더 건너뛰기
			
            boolean quit = false;
            while ((line = reader.readLine()) != null) {
                String[] readBook = line.split(","); // 쉼표 기준 나누기
                String company = readBook[8];

                if (company.equals(str)) {
                    System.out.println(readBook[0]+"|"+readBook[1]+"|"+readBook[2]+"|"+readBook[3]+"|"+readBook[4]+"|"+readBook[5]+"|"
                +readBook[6]+"|"+readBook[7]+"|"+readBook[8]);                    
                    quit = true;
                }
            }

            if (!quit) {
                System.out.println("해당하는 출판사가 없습니다");
            }
            

			reader.close();
			fr.close();

		} catch (Exception e) {
			System.out.println(e);
		}
	

		
		
}
}

