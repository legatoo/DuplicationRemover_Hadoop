package hku.coc.steven;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.client.Result;

public class Search {

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

		String fileName = args[0];

		Configuration config = HBaseConfiguration.create();
		HTable table = new HTable(config, "dup");

		if (args.length != 1) {
			System.out.println("Please send me a file");
			System.exit(0);
		}

		Configuration confHadoop = new Configuration();
		FileSystem fs = FileSystem.get(confHadoop);
		FSDataInputStream in = null;
		Path file = new Path(fs.getUri().toString() + "/" + fileName);

		in = fs.open(file);
		byte[] data = IOUtils.toByteArray(in);

		Get g = new Get(Bytes.toBytes(SupportFunc.MD5String(data)));

		Result r = table.get(g);
		byte[] value = r.getValue(Bytes.toBytes("cf"), Bytes.toBytes("label"));
		String valueStr = Bytes.toString(value);

		if (valueStr != null) {
			String[] labels = valueStr.split("#");
			for (String s : labels) {
				System.out.println("People labels this file as ---->  " + s);
			}
		}
		
		table.close();
	}
}