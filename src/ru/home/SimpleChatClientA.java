package ru.home;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class SimpleChatClientA {
	JFrame frame;
	JPanel panel;
	JTextField massage;
	JButton send;
	Socket sock;
	PrintWriter writer;
	JTextArea history;
	BufferedReader reader;

	public void go() {

		frame = new JFrame("SimpleChat");
		panel = new JPanel();
		history = new JTextArea(25, 30);
		history.setLineWrap(true);
		history.setWrapStyleWord(true);
		history.setEditable(false);
		JScrollPane qScroller = new JScrollPane(history);
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		massage = new JTextField(20);
		send = new JButton("Send");
		send.addActionListener(new SendButtonListener());
		panel.add(qScroller);
		panel.add(massage);
		panel.add(send);
		frame.getContentPane().add(BorderLayout.CENTER, panel);
		setUpNetwork();
		Thread readerTread = new Thread(new IncomingReader());
		readerTread.start();
		frame.setSize(400, 500);
		frame.setVisible(true);

	}

	private void setUpNetwork() {
		try {
			sock = new Socket("127.0.0.1", 5000);
			InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
			reader = new BufferedReader(streamReader);
			writer = new PrintWriter(sock.getOutputStream());
			System.out.println("networking established");

		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	public static void main(String[] args) {
		SimpleChatClientA client = new SimpleChatClientA();
		client.go();

	}

	public class SendButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				writer.println(massage.getText());
				writer.flush();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			massage.setText("");
			massage.requestFocus();

		}

	}

	public class IncomingReader implements Runnable {

		@Override
		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
					System.out.println("read " + message);
					history.append(message + "\n");
				}
			} catch (Exception ex) {
				ex.getStackTrace();
			}

		}

	}

}
