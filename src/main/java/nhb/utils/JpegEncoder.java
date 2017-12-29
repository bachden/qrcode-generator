package nhb.utils;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class JpegEncoder {
	private int quality = 50;
	private static final int[] QL = new int[] { 16, 11, 10, 16, 24, 40, 51, 61, 12, 12, 14, 19, 26, 58, 60, 55, 14, 13,
			16, 24, 40, 57, 69, 56, 14, 17, 22, 29, 51, 87, 80, 62, 18, 22, 37, 56, 68, 109, 103, 77, 24, 35, 55, 64,
			81, 104, 113, 92, 49, 64, 78, 87, 103, 121, 120, 101, 72, 92, 95, 98, 112, 100, 103, 99 };
	private static final int[] QC = new int[] { 17, 18, 24, 47, 99, 99, 99, 99, 18, 21, 26, 66, 99, 99, 99, 99, 24, 26,
			56, 99, 99, 99, 99, 99, 47, 66, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
			99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99 };
	private static final int[] ZIGZAG = new int[] { 0, 1, 5, 6, 14, 15, 27, 28, 2, 4, 7, 13, 16, 26, 29, 42, 3, 8, 12,
			17, 25, 30, 41, 43, 9, 11, 18, 24, 31, 40, 44, 53, 10, 19, 23, 32, 39, 45, 52, 54, 20, 22, 33, 38, 46, 51,
			55, 60, 21, 34, 37, 47, 50, 56, 59, 61, 35, 36, 48, 49, 57, 58, 62, 63 };
	private static final int[] CODESDCL = new int[] { 0, 0, 1, 5, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0 };
	private static final int[] SYMBOLSDCL = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
	private static final int[] CODESACL = new int[] { 0, 0, 2, 1, 3, 3, 2, 4, 3, 5, 5, 4, 4, 0, 0, 1, 125 };
	private static final int[] SYMBOLSACL = new int[] { 1, 2, 3, 0, 4, 17, 5, 18, 33, 49, 65, 6, 19, 81, 97, 7, 34, 113,
			20, 50, 129, 145, 161, 8, 35, 66, 177, 193, 21, 82, 209, 240, 36, 51, 98, 114, 130, 9, 10, 22, 23, 24, 25,
			26, 37, 38, 39, 40, 41, 42, 52, 53, 54, 55, 56, 57, 58, 67, 68, 69, 70, 71, 72, 73, 74, 83, 84, 85, 86, 87,
			88, 89, 90, 99, 100, 101, 102, 103, 104, 105, 106, 115, 116, 117, 118, 119, 120, 121, 122, 131, 132, 133,
			134, 135, 136, 137, 138, 146, 147, 148, 149, 150, 151, 152, 153, 154, 162, 163, 164, 165, 166, 167, 168,
			169, 170, 178, 179, 180, 181, 182, 183, 184, 185, 186, 194, 195, 196, 197, 198, 199, 200, 201, 202, 210,
			211, 212, 213, 214, 215, 216, 217, 218, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 241, 242, 243,
			244, 245, 246, 247, 248, 249, 250 };
	private static final int[] CODESDCC = new int[] { 0, 0, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0 };
	private static final int[] SYMBOLSDCC = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
	private static final int[] CODESACC = new int[] { 0, 0, 2, 1, 2, 4, 4, 3, 4, 7, 5, 4, 4, 0, 1, 2, 119 };
	private static final int[] SYMBOLSACC = new int[] { 0, 1, 2, 3, 17, 4, 5, 33, 49, 6, 18, 65, 81, 7, 97, 113, 19, 34,
			50, 129, 8, 20, 66, 145, 161, 177, 193, 9, 35, 51, 82, 240, 21, 98, 114, 209, 10, 22, 36, 52, 225, 37, 241,
			23, 24, 25, 26, 38, 39, 40, 41, 42, 53, 54, 55, 56, 57, 58, 67, 68, 69, 70, 71, 72, 73, 74, 83, 84, 85, 86,
			87, 88, 89, 90, 99, 100, 101, 102, 103, 104, 105, 106, 115, 116, 117, 118, 119, 120, 121, 122, 130, 131,
			132, 133, 134, 135, 136, 137, 138, 146, 147, 148, 149, 150, 151, 152, 153, 154, 162, 163, 164, 165, 166,
			167, 168, 169, 170, 178, 179, 180, 181, 182, 183, 184, 185, 186, 194, 195, 196, 197, 198, 199, 200, 201,
			202, 210, 211, 212, 213, 214, 215, 216, 217, 218, 226, 227, 228, 229, 230, 231, 232, 233, 234, 242, 243,
			244, 245, 246, 247, 248, 249, 250 };
	private final int[] TL = new int[64];
	private final int[] TC = new int[64];
	private final float[] FDL = new float[64];
	private final float[] FDC = new float[64];
	private final int[] DU = new int[64];
	private final int[] DCHTL = new int[4096];
	private final int[] DCHTC = new int[4096];
	private final int[] ACHTL = new int[4096];
	private final int[] ACHTC = new int[4096];
	private final int[] codes = new int[65536];
	private final int[] items = new int[65536];
	ByteArrayOutputStream bos;
	private int buffer;
	private int pointer = 7;
	private byte[] pixelBytes;
	private int[] pixelInts;

	public void write(BufferedImage image, OutputStream out) throws IOException {
		this.updatePixelHolder(image);
		this.encode(image.getWidth(), image.getHeight(), image.getType());
		out.write(this.bos.toByteArray());
	}

	public void writeByteGray(byte[] image, int width, int height, OutputStream out) throws IOException {
		this.pixelBytes = image;
		this.encode(width, height, BufferedImage.TYPE_BYTE_GRAY);
		out.write(this.bos.toByteArray());
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

	public int getQuality() {
		return this.quality;
	}

	private void generateQTables(float qq) {
		int multipliyer;
		int k;
		for (multipliyer = 0; multipliyer < 64; ++multipliyer) {
			k = (int) (((float) QL[multipliyer] * qq + 50.0F) / 100.0F);
			this.TL[ZIGZAG[multipliyer]] = k < 1 ? 1 : (k > 255 ? 255 : k);
		}

		for (multipliyer = 0; multipliyer < 64; ++multipliyer) {
			k = (int) (((float) QC[multipliyer] * qq + 50.0F) / 100.0F);
			this.TC[ZIGZAG[multipliyer]] = k < 1 ? 1 : (k > 255 ? 255 : k);
		}

		float[] arg5 = new float[] { 1.0F, 1.3870399F, 1.306563F, 1.1758755F, 1.0F, 0.78569496F, 0.5411961F,
				0.27589938F };
		k = 0;

		for (int row = 0; row < 8; ++row) {
			for (int col = 0; col < 8; ++col) {
				this.FDL[k] = (float) (1.0D / ((double) ((float) this.TL[ZIGZAG[k]] * arg5[row] * arg5[col]) * 8.0D));
				this.FDC[k] = (float) (1.0D / ((double) ((float) this.TC[ZIGZAG[k]] * arg5[row] * arg5[col]) * 8.0D));
				++k;
			}
		}

	}

	private static void generateHuffmanMapping(int[] huffman, int[] codes, int[] lookup) {
		int value = 0;
		int index = 0;
		int max = 1;

		for (int i = 1; i <= 16; ++i) {
			for (int j = 1; j <= codes[i]; ++j) {
				int t = lookup[index];
				huffman[t] = value << 16 | i;
				++index;
				++value;
				max = Math.max(max, value);
			}

			value *= 2;
		}

	}

	private void generateHuffmanItems() {
		int low = 1;
		int high = 2;
		int max = 1;

		for (int i = 1; i <= 15; ++i) {
			int nrneg;
			for (nrneg = low; nrneg < high; ++nrneg) {
				this.items[32767 + nrneg] = i;
				this.codes[32767 + nrneg] = nrneg << 16 | i;
				max = Math.max(max, nrneg);
			}

			for (nrneg = -(high - 1); nrneg <= -low; ++nrneg) {
				this.items[32767 + nrneg] = i;
				this.codes[32767 + nrneg] = high - 1 + nrneg << 16 | i;
				max = Math.max(max, high - 1 + nrneg);
			}

			low <<= 1;
			high <<= 1;
		}

	}

	private void putHuffBits(int bs) {
		int value = bs >> 16;
		int pp = (bs & '￿') - 1;

		while (pp >= 0) {
			if ((value & 1 << pp) != 0) {
				this.buffer |= 1 << this.pointer;
			}

			--pp;
			--this.pointer;
			if (this.pointer < 0) {
				if (this.buffer == 255) {
					this.putByte(255);
					this.putByte(0);
				} else {
					this.putByte(this.buffer);
				}

				this.pointer = 7;
				this.buffer = 0;
			}
		}

	}

	private void putByte(int v) {
		this.bos.write(v);
	}

	private void putChar(int v) {
		this.putByte(v >> 8 & 255);
		this.putByte(v & 255);
	}

	private static int[] fDCTQuant(float[] inp, float[] fdtbl, int[] temp) {
		float d0;
		float d1;
		float d2;
		float d3;
		float d4;
		float d5;
		float d6;
		float d7;
		float t0;
		float t1;
		float t2;
		float t3;
		float t4;
		float t5;
		float t6;
		float t7;
		float t10;
		float t11;
		float t12;
		float t13;
		int p;
		for (p = 0; p < 64; p += 8) {
			d0 = inp[p];
			d1 = inp[p + 1];
			d2 = inp[p + 2];
			d3 = inp[p + 3];
			d4 = inp[p + 4];
			d5 = inp[p + 5];
			d6 = inp[p + 6];
			d7 = inp[p + 7];
			t0 = d0 + d7;
			t7 = d0 - d7;
			t1 = d1 + d6;
			t6 = d1 - d6;
			t2 = d2 + d5;
			t5 = d2 - d5;
			t3 = d3 + d4;
			t4 = d3 - d4;
			t10 = t0 + t3;
			t13 = t0 - t3;
			t11 = t1 + t2;
			t12 = t1 - t2;
			inp[p] = t10 + t11;
			inp[p + 4] = t10 - t11;
			d1 = (t12 + t13) * 0.70710677F;
			inp[p + 2] = t13 + d1;
			inp[p + 6] = t13 - d1;
			t10 = t4 + t5;
			t12 = t6 + t7;
			d5 = (t10 - t12) * 0.38268343F;
			d2 = 0.5411961F * t10 + d5;
			d4 = 1.306563F * t12 + d5;
			d3 = (t5 + t6) * 0.70710677F;
			d6 = t7 + d3;
			d7 = t7 - d3;
			inp[p + 5] = d7 + d2;
			inp[p + 3] = d7 - d2;
			inp[p + 1] = d6 + d4;
			inp[p + 7] = d6 - d4;
		}

		for (p = 0; p < 8; ++p) {
			d0 = inp[p];
			d1 = inp[p + 8];
			d2 = inp[p + 16];
			d3 = inp[p + 24];
			d4 = inp[p + 32];
			d5 = inp[p + 40];
			d6 = inp[p + 48];
			d7 = inp[p + 56];
			t0 = d0 + d7;
			t7 = d0 - d7;
			t1 = d1 + d6;
			t6 = d1 - d6;
			t2 = d2 + d5;
			t5 = d2 - d5;
			t3 = d3 + d4;
			t4 = d3 - d4;
			t10 = t0 + t3;
			t13 = t0 - t3;
			t11 = t1 + t2;
			t12 = t1 - t2;
			inp[p] = t10 + t11;
			inp[p + 32] = t10 - t11;
			d1 = (t12 + t13) * 0.70710677F;
			t10 = t4 + t5;
			t12 = t6 + t7;
			d5 = (t10 - t12) * 0.38268343F;
			d2 = 0.5411961F * t10 + d5;
			d4 = 1.306563F * t12 + d5;
			d3 = (t5 + t6) * 0.70710677F;
			d6 = t7 + d3;
			d7 = t7 - d3;
			inp[p + 16] = t13 + d1;
			inp[p + 48] = t13 - d1;
			inp[p + 40] = d7 + d2;
			inp[p + 24] = d7 - d2;
			inp[p + 8] = d6 + d4;
			inp[p + 56] = d6 - d4;
		}

		for (p = 0; p < 64; ++p) {
			float fDCTQuant = inp[p] * fdtbl[p];
			temp[p] = (int) ((double) fDCTQuant > 0.0D ? (double) fDCTQuant + 0.5D : (double) fDCTQuant - 0.5D);
		}

		return temp;
	}

	private void writeAPP0() {
		this.putChar('￠');
		this.putChar(16);
		this.putByte(74);
		this.putByte(70);
		this.putByte(73);
		this.putByte(70);
		this.putByte(0);
		this.putByte(1);
		this.putByte(1);
		this.putByte(0);
		this.putChar(1);
		this.putChar(1);
		this.putByte(0);
		this.putByte(0);
	}

	private void writeSOF0(int width, int height) {
		this.putChar('￀');
		this.putChar(17);
		this.putByte(8);
		this.putChar(height);
		this.putChar(width);
		this.putByte(3);
		this.putByte(1);
		this.putByte(17);
		this.putByte(0);
		this.putByte(2);
		this.putByte(17);
		this.putByte(1);
		this.putByte(3);
		this.putByte(17);
		this.putByte(1);
	}

	private void writeSOS() {
		this.putChar('ￚ');
		this.putChar(12);
		this.putByte(3);
		this.putByte(1);
		this.putByte(0);
		this.putByte(2);
		this.putByte(17);
		this.putByte(3);
		this.putByte(17);
		this.putByte(0);
		this.putByte(63);
		this.putByte(0);
	}

	private void writeDQT() {
		this.putChar('ￛ');
		this.putChar(132);
		this.putByte(0);

		int j;
		for (j = 0; j < 64; ++j) {
			this.putByte(this.TL[j]);
		}

		this.putByte(1);

		for (j = 0; j < 64; ++j) {
			this.putByte(this.TC[j]);
		}

	}

	private void writeDHT() {
		this.putChar('ￄ');
		this.putChar(418);
		this.putByte(0);

		int i;
		for (i = 0; i < 16; ++i) {
			this.putByte(CODESDCL[i + 1]);
		}

		for (i = 0; i <= 11; ++i) {
			this.putByte(SYMBOLSDCL[i]);
		}

		this.putByte(16);

		for (i = 0; i < 16; ++i) {
			this.putByte(CODESACL[i + 1]);
		}

		for (i = 0; i <= 161; ++i) {
			this.putByte(SYMBOLSACL[i]);
		}

		this.putByte(1);

		for (i = 0; i < 16; ++i) {
			this.putByte(CODESDCC[i + 1]);
		}

		for (i = 0; i <= 11; ++i) {
			this.putByte(SYMBOLSDCC[i]);
		}

		this.putByte(17);

		int p;
		for (p = 0; p < 16; ++p) {
			this.putByte(CODESACC[p + 1]);
		}

		for (p = 0; p <= 161; ++p) {
			this.putByte(SYMBOLSACC[p]);
		}

	}

	private int compress(float[] CDU, float[] fdtbl, int DC, int[] huffDC, int[] huffAC, int[] temp) {
		int EOB = huffAC[0];
		int M16zeroes = huffAC[240];
		int[] DU_DCT = fDCTQuant(CDU, fdtbl, temp);

		int Diff;
		for (Diff = 0; Diff < 64; ++Diff) {
			this.DU[ZIGZAG[Diff]] = DU_DCT[Diff];
		}

		Diff = this.DU[0] - DC;
		DC = this.DU[0];
		int pos;
		if (Diff == 0) {
			this.putHuffBits(huffDC[0]);
		} else {
			pos = 32767 + Diff;
			this.putHuffBits(huffDC[this.items[pos]]);
			this.putHuffBits(this.codes[pos]);
		}

		int end0pos;
		for (end0pos = 63; end0pos > 0 && this.DU[end0pos] == 0; --end0pos) {
			;
		}

		if (end0pos == 0) {
			this.putHuffBits(EOB);
			return DC;
		} else {
			for (int i = 1; i <= end0pos; ++i) {
				int startpos;
				for (startpos = i; this.DU[i] == 0 && i <= end0pos; ++i) {
					;
				}

				int nrzeroes = i - startpos;
				if (nrzeroes >= 16) {
					int lng = nrzeroes >> 4;

					for (int nrmarker = 1; nrmarker <= lng; ++nrmarker) {
						this.putHuffBits(M16zeroes);
					}

					nrzeroes &= 15;
				}

				pos = 32767 + this.DU[i];
				this.putHuffBits(huffAC[(nrzeroes << 4) + this.items[pos]]);
				this.putHuffBits(this.codes[pos]);
			}

			if (end0pos != 63) {
				this.putHuffBits(EOB);
			}

			return DC;
		}
	}

	private void encode(int width, int height, int type) throws IOException {
		this.bos = new ByteArrayOutputStream();
		this.quality = this.quality <= 0 ? 1 : (this.quality > 100 ? 100 : this.quality);
		int qq = this.quality < 50 ? 5000 / this.quality : 200 - (this.quality << 1);
		this.generateQTables((float) qq);
		generateHuffmanMapping(this.DCHTL, CODESDCL, SYMBOLSDCL);
		generateHuffmanMapping(this.DCHTC, CODESDCC, SYMBOLSDCC);
		generateHuffmanMapping(this.ACHTL, CODESACL, SYMBOLSACL);
		generateHuffmanMapping(this.ACHTC, CODESACC, SYMBOLSACC);
		this.generateHuffmanItems();
		this.buffer = 0;
		this.pointer = 7;
		this.putChar('￘');
		this.writeAPP0();
		this.writeDQT();
		this.writeSOF0(width, height);
		this.writeDHT();
		this.writeSOS();
		int DCY = 0;
		int DCU = 0;
		int DCV = 0;
		this.buffer = 0;
		this.pointer = 7;
		float[] unitY = new float[64];
		float[] unitU = new float[64];
		float[] unitV = new float[64];
		int[] temp = new int[64];
		int y = 0;

		for (int maxLen = width * height; y < height; y += 8) {
			for (int x = 0; x < width; x += 8) {
				int start = width * y + x;

				for (int pos = 0; pos < 64; ++pos) {
					int row = pos >> 3;
					int col = pos & 7;
					int p = start + row * width + col;
					if (y + row >= height) {
						p -= width * (y + 1 + row - height);
					}

					if (x + col >= width) {
						p -= x + col - width + 4;
					}

					if (p <= maxLen && p >= 0) {
						int[] v = this.getPixel(p, type);
						int r = v[0];
						int g = v[1];
						int b = v[2];
						unitY[pos] = (float) ((128 + 76 * r + 150 * g + 29 * b >> 8) - 128);
						unitU[pos] = (float) (128 + 127 * b - 84 * g - 43 * r >> 8);
						unitV[pos] = (float) (128 + 127 * r - 106 * g - 21 * b >> 8);
					}
				}

				DCY = this.compress(unitY, this.FDL, DCY, this.DCHTL, this.ACHTL, temp);
				DCU = this.compress(unitU, this.FDC, DCU, this.DCHTC, this.ACHTC, temp);
				DCV = this.compress(unitV, this.FDC, DCV, this.DCHTC, this.ACHTC, temp);
			}
		}

		if (this.pointer >= 0) {
			int n = this.pointer + 1;
			int m = (1 << this.pointer + 1) - 1;
			this.putHuffBits(m << 16 | n);
		}

		this.putChar('￙');
		this.bos.close();
	}

	private void updatePixelHolder(BufferedImage source) {
		switch (source.getType()) {
		case 1:
		case 2:
		case 3:
		case 4:
			this.pixelInts = ((DataBufferInt) source.getRaster().getDataBuffer()).getData();
			break;
		case 5:
		case 6:
		case 7:
		case 10:
			this.pixelBytes = ((DataBufferByte) source.getRaster().getDataBuffer()).getData();
			break;
		case 8:
		case 9:
		default:
			BufferedImage image2 = new BufferedImage(source.getWidth(), source.getHeight(), 1);
			Graphics g2 = image2.getGraphics();
			g2.drawImage(source, 0, 0, (ImageObserver) null);
			g2.dispose();
			this.pixelInts = ((DataBufferInt) image2.getRaster().getDataBuffer()).getData();
		}

	}

	private int[] getPixel(int p, int type) {
		int v;
		int r;
		int g;
		int b;
		switch (type) {
		case 1:
		case 2:
		case 3:
			v = this.pixelInts[p];
			return new int[] { v >> 16 & 255, v >> 8 & 255, v & 255 };
		case 4:
			v = this.pixelInts[p];
			return new int[] { v & 255, v >> 8 & 255, v >> 16 & 255 };
		case 5:
			v = p * 3;
			b = this.pixelBytes[v] & 255;
			g = this.pixelBytes[v + 1] & 255;
			r = this.pixelBytes[v + 2] & 255;
			return new int[] { r, g, b };
		case 6:
		case 7:
			v = p * 4;
			b = this.pixelBytes[v + 1] & 255;
			g = this.pixelBytes[v + 2] & 255;
			r = this.pixelBytes[v + 3] & 255;
			return new int[] { r, g, b };
		case 8:
		case 9:
		default:
			v = this.pixelInts[p];
			return new int[] { v >> 16 & 255, v >> 8 & 255, v & 255 };
		case 10:
			v = this.pixelBytes[p] & 255;
			return new int[] { v, v, v };
		}
	}
}