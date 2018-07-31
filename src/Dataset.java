import java.util.zip.CRC32;

public class Dataset {
	public String dataset(String AID,String billerID,String ref1,String ref2,String transactioncode,String amount,String country,String Merchant,String terminal) {
		text = "";
		addText("000201");
		addText("010212");
		addText("3082");
		addText(("00".concat(ToString(AID.length()))).concat(AID));
		if(billerID.length()!=0){
			addText(("01".concat(ToString(billerID.length()))).concat(billerID));
		}
		addText(("02".concat(ToString(ref1.length()))).concat(ref1));
		if(ref2.length()!=0){
			addText(("03".concat(ToString(ref2.length()))).concat(ref2));
		}
		addText(("53".concat(ToString(transactioncode.length()))).concat(transactioncode));
		amount = AmountCheck(amount);
		addText(("54".concat(ToString(amount.length()))).concat(amount));
		addText("5802TH");
		addText(("59".concat(ToString(Merchant.length()))).concat(Merchant));
		String terminalidData = ("07".concat(ToString(terminal.length()))).concat(terminal);
		addText(("62".concat(ToString(terminalidData.length()))).concat(terminalidData));
		addText("6304");
		addText(Integer.toHexString(crc16(text.getBytes())).toUpperCase());
		
		return text;
	}
	private String ToString(int textLenght) {
		if(textLenght<10) return "0".concat(String.valueOf(textLenght));
		return String.valueOf(textLenght);
	}
	private String AmountCheck(String textLenght) {
		if(textLenght.matches("[0-9]*[.][0-9]{2}")){
			return textLenght;
		}
		return textLenght+".00";
	}
	public String text ="";
	private void addText(String message) {
		text+=message;
	}
	public static int crc16(final byte[] buffer) {
	    int crc = 0xFFFF;

	    for (int j = 0; j < buffer.length ; j++) {
	        crc = ((crc  >>> 8) | (crc  << 8) )& 0xffff;
	        crc ^= (buffer[j] & 0xff);//byte to int, trunc sign
	        crc ^= ((crc & 0xff) >> 4);
	        crc ^= (crc << 12) & 0xffff;
	        crc ^= ((crc & 0xFF) << 5) & 0xffff;
	    }
	    crc &= 0xffff;
//	    System.out.println("CRC : "+ Integer.toHexString(crc) );
	    return crc;

	}
}
