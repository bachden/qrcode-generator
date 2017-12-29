package nhb.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRCodeGenerator {

	public static byte[] createQRImage(String qrCodeText, int size) throws WriterException, IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		createQRImage(qrCodeText, size, outputStream);
		return outputStream.toByteArray();
	}

	public static void createQRImage(String qrCodeText, int size, OutputStream out)
			throws WriterException, IOException {

		// Create the ByteMatrix for the QR-Code that encodes the given String
		Hashtable<EncodeHintType, Object> hintMap = new Hashtable<>();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hintMap.put(EncodeHintType.MARGIN, 2);

		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, size, size, hintMap);

		// Make the BufferedImage that are to hold the QRCode
		int matrixWidth = byteMatrix.getWidth();

		byte[] bytes = new byte[matrixWidth * matrixWidth];
		for (int x = 0; x < matrixWidth; x++) {
			for (int y = 0; y < matrixWidth; y++) {
				bytes[y * matrixWidth + x] = (byte) (byteMatrix.get(x, y) ? 0 : 255);
			}
		}

		JpegEncoder encoder = new JpegEncoder();
		encoder.setQuality(100);

		encoder.writeByteGray(bytes, size, size, out);
	}
}