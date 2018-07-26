import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.AlphaComposite;
import java.awt.ComponentOrientation;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Graphics2D;
import javax.swing.JTextField;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class QR_Code_Generator {

	private JFrame frame;
	private JTextField amount;
	private JTextField ref1;
	private JTextField ref2;
	private JTextField terminalID;
	private JTextField billerID;
	private JTextField merchantName;
	private JLabel lblNewLabel;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					QR_Code_Generator window = new QR_Code_Generator();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public QR_Code_Generator() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private String dir = System.getProperty("user.dir");
	
	private void generateQR(String message,int i) {
		Map hints = new HashMap<>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		QRCodeWriter writer = new QRCodeWriter();
		BitMatrix bitMatrix = null;
		new ByteArrayOutputStream();
		
		try {
			    // Create a qr code with the url as content and a size of 250x250 px
			bitMatrix = writer.encode(message, BarcodeFormat.QR_CODE, 250, 250, hints);
			MatrixToImageConfig config = new MatrixToImageConfig(MatrixToImageConfig.BLACK, MatrixToImageConfig.WHITE);
			    // Load QR image
			BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, config);
			    // Load logo image
			File file = new File(dir+"\\logo.png");
			BufferedImage logoImage = ImageIO.read(file);
			    // Calculate the delta height and width between QR code and logo
			int deltaHeight = qrImage.getHeight() - logoImage.getHeight();
			int deltaWidth = qrImage.getWidth() - logoImage.getWidth();
			    // Initialize combined image
			BufferedImage combined = new BufferedImage(qrImage.getHeight(), qrImage.getWidth(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = (Graphics2D) combined.getGraphics();
			    // Write QR code to new image at position 0/0
			g.drawImage(qrImage, 0, 0, null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
			    // Write logo into combine image at position (deltaWidth / 2) and
			    // (deltaHeight / 2). Background: Left/Right and Top/Bottom must be
			    // the same space for the logo to be centered
			g.drawImage(logoImage, (int) Math.round(deltaWidth / 2), (int) Math.round(deltaHeight / 2), null);
			    // Write combined image as PNG to OutputStream
			ImageIO.write(combined, "png", new File(dir+"\\"+i+".png"));
//			System.out.println("done");
		} catch (Exception e) {
			System.out.println(e);
		}  
	}
	
	public String text ="";
	int Maxbtn=10;
	
	int btnid=0;
	Dataset dataset = new Dataset();
	String[] data1 = new String[500];
	String[] AID = new String[500];
    String[] billID = new String[500];
    String[] refer1 = new String[500];
    String[] refer2 = new String[500];
    String[] transcode = new String[500];
    String[] amounts = new String[500];
    String[] countrys = new String[500];
    String[] Merchants = new String[500];
    String[] terminals = new String[500];
    JLabel img = new JLabel();
    private String reString(String dat,int start,int limit) {
		if(dat.length()>limit){
			dat = dat.substring(start, limit).toUpperCase();
		}
		return dat;
	}
    
    private void WriteCSV() {
    	// The name of the file to open.
        String fileName = "temp.csv";

        try {
            // Assume default encoding.
            FileWriter fileWriter =
                new FileWriter(fileName);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter =
                new BufferedWriter(fileWriter);

            // Note that write() does not automatically
            // append a newline character.
            bufferedWriter.write("AID,BillerID,Reference1,Reference2,TransactionCurrency,amount,Countrycode,MerchantName,TerminalID");
//            bufferedWriter.newLine();
            for(int i = 1;i<Maxbtn;i++){
            	if(i == btnid){
            		amounts[i] = amount.getText(); 
            		billID[i] = billerID.getText();
            		refer1[i] = ref1.getText();
            		refer2[i] = ref2.getText();
            		Merchants[i] = merchantName.getText();
            		data1[i] = dataset.dataset(AID[i],billID[i],refer1[i],refer2[i],transcode[i], 
                    		amounts[i],countrys[i],Merchants[i],terminals[i]);
                    System.out.println("data"+i+" :"+data1[i]);
                    generateQR(data1[i],i);
            	}
            	bufferedWriter.newLine();
            	bufferedWriter.write(AID[i]+","+billID[i]+","+refer1[i]+","+refer2[i]+","+transcode[i]+","+amounts[i]+","+countrys[i]+","+Merchants[i]+","+terminals[i]);
            }

            // Always close files.
            bufferedWriter.close();
        }
        catch(IOException ex) {
            System.out.println(
                "Error writing to file '"
                + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
        
	}
    
	private void ReadCSV() {
		String Filedir = dir+"/temp.csv";
		BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        int i=0;
        try {
            br = new BufferedReader(new FileReader(Filedir));
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] data = line.split(cvsSplitBy);
                AID[i] = reString(data[0], 0, 16);
                billID[i] = reString(data[1], 0, 15);
                refer1[i] = reString(data[2], 0, 20);
                refer2[i] = reString(data[3], 0, 20);
                transcode[i] = data[4];
                amounts[i] = reString(data[5], 0, 13);
                countrys[i] = data[6];
                Merchants[i] = reString(data[7], 0, 25);
                terminals[i] = reString(data[8], 0, 26);
                data1[i] = dataset.dataset(AID[i],billID[i],refer1[i],refer2[i],transcode[i], 
                		amounts[i],countrys[i],Merchants[i],terminals[i]);
                System.out.println("data"+i+" :"+data1[i]);
                generateQR(data1[i],i);
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } 
	}
	
	private void show(){
		text = data1[btnid];
		amount.setText(amounts[btnid]);
		ref1.setText(refer1[btnid]);
		ref2.setText(refer2[btnid]);
		terminalID.setText(terminals[btnid]);
		merchantName.setText(Merchants[btnid]);
		billerID.setText(billID[btnid]);
		ImageIcon imgico = new ImageIcon(dir+"\\"+btnid+".png");
		imgico.getImage().flush();
		lblNewLabel.setIcon(imgico);
	}
	private void initialize() {
		ReadCSV();
		frame = new JFrame();
		frame.getContentPane().setFont(new Font("Monospaced", Font.PLAIN, 20));
		frame.setBounds(100, 100, 1002, 768);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		lblNewLabel = new JLabel("");
		
		JButton btn1 = new JButton("1");
		btn1.setFont(new Font("Monospaced", Font.PLAIN, 20));
		btn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnid = 1;
				show();
			}
		});
		btn1.setBounds(35, 346, 250, 80);
		frame.getContentPane().add(btn1);
		
		JButton btn2 = new JButton("2");
		btn2.setFont(new Font("Monospaced", Font.PLAIN, 20));
		btn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnid = 2;
				show();
			}
		});
		btn2.setBounds(370, 346, 250, 80);
		frame.getContentPane().add(btn2);
		
		JButton btn3 = new JButton("3");
		btn3.setFont(new Font("Monospaced", Font.PLAIN, 20));
		btn3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnid = 3;
				show();
			}
		});
		btn3.setBounds(684, 346, 250, 80);
		frame.getContentPane().add(btn3);
		
		JButton btn4 = new JButton("4");
		btn4.setFont(new Font("Monospaced", Font.PLAIN, 20));
		btn4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnid = 4;
				show();
			}
		});
		btn4.setBounds(35, 439, 250, 80);
		frame.getContentPane().add(btn4);
		
		JButton btn5 = new JButton("5");
		btn5.setFont(new Font("Monospaced", Font.PLAIN, 20));
		btn5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnid = 5;
				show();
			}
		});
		btn5.setBounds(370, 439, 250, 80);
		frame.getContentPane().add(btn5);
		
		JButton btn6 = new JButton("6");
		btn6.setFont(new Font("Monospaced", Font.PLAIN, 20));
		btn6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnid = 6;
				show();
			}
		});
		btn6.setBounds(684, 439, 250, 80);
		frame.getContentPane().add(btn6);
		
		JButton btn7 = new JButton("7");
		btn7.setFont(new Font("Monospaced", Font.PLAIN, 20));
		btn7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnid = 7;
				show();
			}
		});
		btn7.setBounds(35, 532, 250, 80);
		frame.getContentPane().add(btn7);
		
		JButton btn8 = new JButton("8");
		btn8.setFont(new Font("Monospaced", Font.PLAIN, 20));
		btn8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnid = 8;
				show();
			}
		});
		btn8.setBounds(370, 532, 250, 80);
		frame.getContentPane().add(btn8);
		
		JButton btn9 = new JButton("9");
		btn9.setFont(new Font("Monospaced", Font.PLAIN, 20));
		btn9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnid = 9;
				show();
			}
		});
		btn9.setBounds(684, 532, 250, 80);
		frame.getContentPane().add(btn9);
		
		amount = new JTextField();
		amount.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if((!Character.isDigit(c)||c==KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE )&& c != '.'){
					e.consume();
				}
			}
		});
		amount.setBounds(140, 33, 142, 45);
		amount.setFont(new Font("Monospaced", Font.PLAIN, 16));
		amount.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		frame.getContentPane().add(amount);
		amount.setColumns(10);
		
		ref1 = new JTextField();
		ref1.setColumns(10);
		ref1.setFont(new Font("Monospaced", Font.PLAIN, 16));
		ref1.setBounds(140, 94, 480, 45);
		frame.getContentPane().add(ref1);
		
		ref2 = new JTextField();
		ref2.setColumns(10);
		ref2.setFont(new Font("Monospaced", Font.PLAIN, 16));
		ref2.setBounds(137, 152, 483, 45);
		frame.getContentPane().add(ref2);
		
		terminalID = new JTextField();
		terminalID.setColumns(10);
		terminalID.setFont(new Font("Monospaced", Font.PLAIN, 16));
		terminalID.setBounds(223, 268, 397, 45);
		frame.getContentPane().add(terminalID);
		
		billerID = new JTextField();
		billerID.setColumns(10);
		billerID.setFont(new Font("Monospaced", Font.PLAIN, 16));
		billerID.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		billerID.setBounds(407, 34, 213, 45);
		frame.getContentPane().add(billerID);
		
		merchantName = new JTextField();
		merchantName.setColumns(10);
		merchantName.setFont(new Font("Monospaced", Font.PLAIN, 16));
		merchantName.setBounds(223, 210, 397, 45);
		frame.getContentPane().add(merchantName);
		
		JButton btnSave = new JButton("Save Data");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {		
				WriteCSV();
				ReadCSV();
				show();
			}
		});
		btnSave.setFont(new Font("Monospaced", Font.PLAIN, 20));
		btnSave.setBounds(35, 625, 900, 73);
		frame.getContentPane().add(btnSave);
		
		JLabel lblAmount = new JLabel("Amount :");
		lblAmount.setFont(new Font("Monospaced", Font.PLAIN, 16));
		lblAmount.setBounds(35, 33, 90, 45);
		frame.getContentPane().add(lblAmount);
		
		JLabel lblRef = new JLabel("Ref 1 :");
		lblRef.setFont(new Font("Monospaced", Font.PLAIN, 16));
		lblRef.setBounds(35, 94, 90, 45);
		frame.getContentPane().add(lblRef);
		
		JLabel lblRef_1 = new JLabel("Ref2 :");
		lblRef_1.setFont(new Font("Monospaced", Font.PLAIN, 16));
		lblRef_1.setBounds(35, 152, 90, 45);
		frame.getContentPane().add(lblRef_1);
		
		JLabel lblTerminalId = new JLabel("Terminal ID :");
		lblTerminalId.setFont(new Font("Monospaced", Font.PLAIN, 16));
		lblTerminalId.setBounds(35, 267, 180, 45);
		frame.getContentPane().add(lblTerminalId);
		
		JLabel lblBillerId = new JLabel("Biller ID :");
		lblBillerId.setFont(new Font("Monospaced", Font.PLAIN, 16));
		lblBillerId.setBounds(294, 32, 110, 45);
		frame.getContentPane().add(lblBillerId);
		
		JLabel lblMerchantName = new JLabel("Merchant Name :");
		lblMerchantName.setFont(new Font("Monospaced", Font.PLAIN, 16));
		lblMerchantName.setBounds(35, 210, 180, 45);
		frame.getContentPane().add(lblMerchantName);
		
		lblNewLabel.setBounds(684, 62, 250, 250);
		frame.getContentPane().add(lblNewLabel);
		
	}
}
