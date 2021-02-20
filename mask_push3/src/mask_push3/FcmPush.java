package mask_push3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FcmPush extends Thread{

	// 구글 인증 서버키
	private final String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
	private final String AUTH_KEY_FCM = "AAAAj4-g6JU:APA91bF0qpLLbe-zFAQp-nCo9NOr-5IfF_5lStNJKZLy_jUhSEWL6z4PxHzkII3tmHFx30wNRpIyW9LuacPCLx-YnOoR9mXOtvqOCTDo1WfDQ2Unm6nFP1IEqrMV3KF-c1A9BjWld9b-";
	//private List<String> APP_TOKEN = new ArrayList<String>();
	private String token;
	private HttpURLConnection conn;
	private String message;
	
	public FcmPush(String token, String message) {
		this.token = token;
		this.message = message;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//super.run();
		try {
			this.pushFCMNotification();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 구글서버로 푸시 request pushFCMNotification
	 * 
	 * @throws Exception
	 */
	public void pushFCMNotification() throws Exception {
		if(!"".equals(token) && token != null) {
			URL url = new URL(API_URL_FCM);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "key=" + AUTH_KEY_FCM);
			conn.setDoInput(true);
			
	        String input = "{\"notification\" : {\"title\" : \" 마스크 재입고 알림 \", \"body\" :\"" + this.message + "\"}, \"to\":\"" + token +"\"}";
			
			OutputStream os = conn.getOutputStream();
	        
	        // 서버에서 날려서 한글 깨지는 사람은 아래처럼  UTF-8로 인코딩해서 날려주자
	        os.write(input.getBytes("UTF-8"));
	        os.flush();
	        os.close();

	        int responseCode = conn.getResponseCode();
	        System.out.println("\nSending 'POST' request to URL : " + url);
	        System.out.println("Post parameters : " + input);
	        System.out.println("Response Code : " + responseCode);
	        
	        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        String inputLine;
	        StringBuffer response = new StringBuffer();

	        while ((inputLine = in.readLine()) != null) {
	            response.append(inputLine);
	        }
	        in.close();
	        // print result
	        System.out.println(response.toString());
		}
	}
}
