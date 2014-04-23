package hku.coc.steven;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.SequenceFile.Reader;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.CompressionType;
import org.apache.hadoop.io.SequenceFile.Writer;
import org.apache.hadoop.io.compress.DefaultCodec;

public class ReadFromSeqTest {

	private static void writeToSeq(String inPath, String outPath)
			throws IOException, NoSuchAlgorithmException {

		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(conf);

		Path input = new Path(fs.getUri().toString() + "/" + inPath);
		Path output = new Path(outPath);

		FSDataInputStream in = null;
//		FSDataOutputStream out = null;

		in = fs.open(input);
//		out = fs.create(output);
		byte[] buffer = IOUtils.toByteArray(in);

//		Writer writer = SequenceFile.createWriter(conf, Writer.stream(out),
//				Writer.keyClass(Text.class),
//				Writer.valueClass(BytesWritable.class),
//				Writer.compression(CompressionType.NONE, new DefaultCodec()));
		
		Writer writer = SequenceFile.createWriter(conf,
				Writer.file(output), Writer.keyClass(Text.class),
				Writer.valueClass(BytesWritable.class));

		writer.append(new Text(inPath), new BytesWritable(buffer));
		writer.close();

		BufferedImage imag;
		imag = ImageIO.read(new ByteArrayInputStream(buffer));
		
		System.out.println("The byte array has Length: "
				+ String.valueOf(buffer.length));
		System.out.println("The height: " + String.valueOf(imag.getHeight())
				+ " width: " + String.valueOf(imag.getWidth()));
		System.out.println("MD5: " + SupportFunc.MD5String(buffer));

	}

	private static void readFromSeq(String inPath) throws IOException, NoSuchAlgorithmException {
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(conf);
		System.out.println("Readding1");

		
		Path input = new Path(inPath);

		Reader reader = new SequenceFile.Reader(conf, Reader.file(input));

		Text key = new Text();
		BytesWritable val = new BytesWritable();

		String fileName = "";
		String md5 = "";
		int length = 0;
		String height = "";
		String width = "";

		while (reader.next(key, val)) {

			fileName = key.toString();
//			byte[] rawdata = val.getBytes();
//			length = val.getLength();
//			byte[] data = Arrays.copyOfRange(rawdata,  0, length);
			
			byte[] data = val.copyBytes();
			

			BufferedImage imag;
			imag = ImageIO.read(new ByteArrayInputStream(data));

			height = String.valueOf(imag.getHeight());
			width = String.valueOf(imag.getWidth());
			md5 = SupportFunc.MD5String(data);
		}
		reader.close();
		
		System.out.println("The byte array has Length: " + length);
		System.out.println("The height: " + height + " width: " + width);
		System.out.println("MD5: " + md5);

	}


	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

		writeToSeq(args[0], args[1]);
		
		System.out.println("<-------------------------------------->");
		
		readFromSeq(args[1]);

	}
}