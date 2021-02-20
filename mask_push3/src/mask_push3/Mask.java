package mask_push3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Mask {
	private static CoupPangThread thread;

	public static void main(String[] args) {
		// Creating instance of JFrame
		JFrame frame = new JFrame("쿠팡 마스크 재입고 알림");
		// Setting the width and height of frame
		frame.setSize(390, 260);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		// adding panel to frame
		frame.add(panel);

		placeComponents(panel);

		// Setting the frame visibility to true
		frame.setVisible(true);
	}

	private static void placeComponents(JPanel panel) {

		panel.setLayout(null);

		// Creating ID Form
		JLabel userLabel = new JLabel("* ID");
		userLabel.setBounds(10, 20, 80, 25);
		panel.add(userLabel);

		JTextField idText = new JTextField(20);
		idText.setBounds(140, 20, 165, 25);
		panel.add(idText);

		// Creating Password Form
		JLabel passwordLabel = new JLabel("* Password");
		passwordLabel.setBounds(10, 50, 80, 25);
		panel.add(passwordLabel);

		JPasswordField passwordText = new JPasswordField(20);
		passwordText.setBounds(140, 50, 165, 25);
		panel.add(passwordText);

		// Creating drive Form
		JLabel driveLabel = new JLabel("* Drive path");
		driveLabel.setBounds(10, 80, 80, 25);
		panel.add(driveLabel);

		JTextField driveText = new JTextField(20);
		driveText.setBounds(140, 80, 165, 25);
		panel.add(driveText);

		JButton driveButton = new JButton("불러오기");
		driveButton.setBounds(310, 80, 30, 25);
		panel.add(driveButton);

		// Creating Token Form
		JLabel tokenLabel = new JLabel("  Device token");
		tokenLabel.setBounds(10, 110, 80, 25);
		panel.add(tokenLabel);

		JTextField tokenText = new JTextField(20);
		tokenText.setBounds(140, 110, 165, 25);
		panel.add(tokenText);

		JButton pushButton = new JButton("Send");
		pushButton.setBounds(310, 110, 30, 25);
		panel.add(pushButton);

		// Creating Sound Form
		JLabel soundLabel = new JLabel("  Sound path");
		soundLabel.setBounds(10, 140, 80, 25);
		panel.add(soundLabel);

		JTextField soundText = new JTextField(20);
		soundText.setBounds(140, 140, 165, 25);
		panel.add(soundText);

		JButton soundButton = new JButton("불러오기");
		soundButton.setBounds(310, 140, 30, 25);
		panel.add(soundButton);

		// Creating login button
		JButton startButton = new JButton("실행");
		startButton.setBounds(10, 170, 80, 25);
		panel.add(startButton);

		JButton stopButton = new JButton("중지");
		stopButton.setBounds(140, 170, 80, 25);
		panel.add(stopButton);
		stopButton.setEnabled(false);

		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if ("".equals(idText.getText().trim())) {
					JOptionPane.showMessageDialog(null, "아이디를 입력해주세요.", "알림", 1, null);
				} else if ("".equals(String.valueOf(passwordText.getPassword()))) {
					JOptionPane.showMessageDialog(null, "비밀번호를 입력해주세요.", "알림", 1, null);
				} else if ("".equals(driveText.getText().trim())) {
					JOptionPane.showMessageDialog(null, "드라이브 경로를 입력해주세요.", "알림", 1, null);
				} else {
					startButton.setEnabled(false);
					startButton.setText("실행중");
					idText.setEditable(false);
					passwordText.setEditable(false);
					tokenText.setEditable(false);
					driveButton.setEnabled(false);
					driveText.setEditable(false);
					pushButton.setEnabled(false);
					soundText.setEditable(false);
					soundButton.setEnabled(false);
					stopButton.setEnabled(true);

					thread = new CoupPangThread(idText.getText()
							                  , String.valueOf(passwordText.getPassword())
							                  , driveText.getText()
							                  , tokenText.getText().trim()
							                  , new NotificationSound(new File(soundText.getText())));
					thread.start();
				}
			}
		});

		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				startButton.setEnabled(true);
				startButton.setText("실행");
				idText.setEditable(true);
				passwordText.setEditable(true);
				tokenText.setEditable(true);
				driveButton.setEnabled(true);
				driveText.setEditable(true);
				pushButton.setEnabled(true);
				soundText.setEditable(true);
				soundButton.setEnabled(true);
				stopButton.setEnabled(false);

				thread.stopThread();
			}
		});

		driveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					JFileChooser chooser = new JFileChooser();
					FileNameExtensionFilter fiter = new FileNameExtensionFilter("EXE", "exe");
					chooser.setFileFilter(fiter);
					chooser.showOpenDialog(null);
					driveText.setText(chooser.getSelectedFile().getPath());
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		});

		pushButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if ("".equals(tokenText.getText().trim())) {
					JOptionPane.showMessageDialog(null, "디바이스 토큰값을 입력해주세요.", "Push 테스트", 1, null);
				} else {
					FcmPush fcmPush = new FcmPush(tokenText.getText().trim(), "Push 테스트");
					try {
						fcmPush.start();
						//fcmPush.pushFCMNotification("Push 테스트");
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		soundButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					JFileChooser chooser = new JFileChooser();
					FileNameExtensionFilter fiter = new FileNameExtensionFilter("WAV", "wav");
					chooser.setFileFilter(fiter);
					chooser.showOpenDialog(null);
					soundText.setText(chooser.getSelectedFile().getPath());
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		});
	}

}