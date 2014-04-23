package hku.coc.steven;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;

/*
 * This Reducer will write the record to HBase
 * In the HBase, the key is MD5 value that uniquely denotes a files
 * the value is the label with cell value equals to the file name(without extention name).
 */
public class RemoveReducer extends
		TableReducer<Text, CompositeWritable, ImmutableBytesWritable> {

	public void reduce(Text key, Iterable<CompositeWritable> values,
			Context context) throws IOException, InterruptedException {
		try {

			Put insHBase = new Put(Bytes.toBytes(key.toString()));
			// create hbase put with MD5 as date
			Text location = new Text();
			String fileName = new String();
			StringBuilder label = new StringBuilder();

			for (CompositeWritable file : values) {

				String tmp = file.getFilePath().toString();
				String[] parts = tmp.split("/");
				int length = parts.length;
				fileName = parts[length - 1];
				label.append(fileName.split("\\.")[0]);
				label.append("#");
				
			}
			// write record to HBase
			insHBase.add(Bytes.toBytes("cf"), Bytes.toBytes("label"),
					Bytes.toBytes(label.toString()));

			context.write(new ImmutableBytesWritable(key.getBytes()), insHBase);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}