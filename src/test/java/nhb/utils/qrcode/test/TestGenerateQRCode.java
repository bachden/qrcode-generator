package nhb.utils.qrcode.test;

import java.io.IOException;
import java.util.Base64;

import com.google.zxing.WriterException;

import nhb.utils.QRCodeGenerator;

public class TestGenerateQRCode {

	public static void main(String[] args) throws WriterException, IOException {
		String data = "https://www.google.com";

		byte[] qr = QRCodeGenerator.createQRImage(data, 240);
		String qrBase64 = Base64.getEncoder().encodeToString(qr);

		System.out.println("data:image/jpeg;base64, " + qrBase64);
	}
}
