package mask_push3;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class CoupPangThread extends Thread{
	private final String WEB_DRIVER_ID = "webdriver.chrome.driver";
	private final String LOGIN_URL = "https://login.coupang.com/login/login.pang";
	private final String URL = "https://www.coupang.com/np/search?q=kf+%EB%A7%88%EC%8A%A4%ED%81%AC+%EB%8C%80%ED%98%95&brand=&offerCondition=&filter=&availableDeliveryFilter=&filterType=rocket%2C&isPriceRange=false&priceRange=&minPrice=&maxPrice=&page=1&trcid=&traid=&filterSetByUser=true&channel=user&backgroundColor=&component=&rating=0&sorter=scoreDesc&listSize=36";
//	private final String URL = "https://www.coupang.com/np/search?q=kf+%EB%A7%88%EC%8A%A4%ED%81%AC+%EB%8C%80%ED%98%95&brand=&offerCondition=&filter=&availableDeliveryFilter=&filterType=&isPriceRange=false&priceRange=&minPrice=&maxPrice=&page=1&trcid=&traid=&filterSetByUser=true&channel=auto&backgroundColor=&component=&rating=0&sorter=scoreDesc&listSize=36&rocketAll=false";

	private String id;
	private String pw;
	private String drivePath;
	//private FcmPush fcmPush;
	private String token;
	private Sound notiSound;
	
	private Boolean stopFlag= true;
	
	public CoupPangThread(String id, String pw, String drivePath, String token, Sound notiSound) {
		this.id = id;
		this.pw = pw;
		this.drivePath = drivePath;
		//this.fcmPush = fcmPush;
		this.token = token;
		this.notiSound = notiSound;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ("yyyy.MM.dd HH:mm:ss");
		String keyWord;
		Document doc;
		
		WebElement webElement;
		System.setProperty(WEB_DRIVER_ID, drivePath);
		WebDriver driver = new ChromeDriver();
		
	//	final long timeInterval = 200; 
		int tapIndex = 0;
		try {
			// 쿠팡 로그인 화면 띄우기
			driver.get(LOGIN_URL);
			// 아이디 입력
			webElement = driver.findElement(By.id("login-email-input"));
			webElement.clear();
			webElement.sendKeys(id);
			// 비밀번호 입력
			webElement = driver.findElement(By.id("login-password-input"));
			webElement.clear();
			webElement.sendKeys(pw);
			// 로그인
			webElement = driver.findElement(By.className("login__button"));
			webElement.click();
			
			// 검색어 입력
			/*
			 * driver.get(URL); webElement =
			 * driver.findElement(By.id("headerSearchKeyword")); webElement.clear();
			 * webElement.sendKeys("kf 마스크 대형1");
			 * 
			 * // 검색 webElement = driver.findElement(By.id("headerSearchBtn"));
			 * webElement.click();
			 */
			
			while(stopFlag) {
				System.out.println ("쿠팡 : " + mSimpleDateFormat.format(new Date()));
				
				driver.get(URL);
				String pageSource = driver.getPageSource();
				
				// 상품 목록 파싱
				doc = Jsoup.parse(pageSource);
				Elements titles = doc.select("li.search-product");
				
				for(Element el : titles) {
					if(el.toString().indexOf("일시품절") > -1) {
						//System.out.println("쿠팡 품절");
					}else {
						keyWord = el.select("div.name").text();
						
						// 알림음 재생
						notiSound.play();
						// 푸시 전송
					//	fcmPush.pushFCMNotification("쿠팡 마스크 입고 : " + keyWord);
						FcmPush fcmPush = new FcmPush(token, keyWord);
						fcmPush.start();
						
						// 재고있는 상품 id값 얻어온 후 클릭
						webElement = driver.findElement(By.id(el.id()));
						webElement.click();
						tapIndex++;
						
						// 상품 클릭 후 새탭으로 열림. 드라이브 위치를 새로 열린 창으로 이동
				        driver.switchTo().window((String)driver.getWindowHandles().toArray()[tapIndex]);
				       
				        // 새로열린 창에서 장바구니에 담기 클릭
				        if(!"".equals(driver.findElement(By.className("prod-cart-btn")).getText())) {
				        	webElement = driver.findElement(By.className("prod-cart-btn"));
							webElement.click();
				        }				
						//driver.close();
						driver.switchTo().window((String)driver.getWindowHandles().toArray()[0]);
					}
				}
			//	Thread.sleep(timeInterval);
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
	}
	
	public void stopThread() {
		this.stopFlag = false;
	}
}
