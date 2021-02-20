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
			// ���� �α��� ȭ�� ����
			driver.get(LOGIN_URL);
			// ���̵� �Է�
			webElement = driver.findElement(By.id("login-email-input"));
			webElement.clear();
			webElement.sendKeys(id);
			// ��й�ȣ �Է�
			webElement = driver.findElement(By.id("login-password-input"));
			webElement.clear();
			webElement.sendKeys(pw);
			// �α���
			webElement = driver.findElement(By.className("login__button"));
			webElement.click();
			
			// �˻��� �Է�
			/*
			 * driver.get(URL); webElement =
			 * driver.findElement(By.id("headerSearchKeyword")); webElement.clear();
			 * webElement.sendKeys("kf ����ũ ����1");
			 * 
			 * // �˻� webElement = driver.findElement(By.id("headerSearchBtn"));
			 * webElement.click();
			 */
			
			while(stopFlag) {
				System.out.println ("���� : " + mSimpleDateFormat.format(new Date()));
				
				driver.get(URL);
				String pageSource = driver.getPageSource();
				
				// ��ǰ ��� �Ľ�
				doc = Jsoup.parse(pageSource);
				Elements titles = doc.select("li.search-product");
				
				for(Element el : titles) {
					if(el.toString().indexOf("�Ͻ�ǰ��") > -1) {
						//System.out.println("���� ǰ��");
					}else {
						keyWord = el.select("div.name").text();
						
						// �˸��� ���
						notiSound.play();
						// Ǫ�� ����
					//	fcmPush.pushFCMNotification("���� ����ũ �԰� : " + keyWord);
						FcmPush fcmPush = new FcmPush(token, keyWord);
						fcmPush.start();
						
						// ����ִ� ��ǰ id�� ���� �� Ŭ��
						webElement = driver.findElement(By.id(el.id()));
						webElement.click();
						tapIndex++;
						
						// ��ǰ Ŭ�� �� �������� ����. ����̺� ��ġ�� ���� ���� â���� �̵�
				        driver.switchTo().window((String)driver.getWindowHandles().toArray()[tapIndex]);
				       
				        // ���ο��� â���� ��ٱ��Ͽ� ��� Ŭ��
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
