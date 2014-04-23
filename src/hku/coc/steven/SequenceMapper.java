package hku.coc.steven;

import java.io.IOException;
import java.net.URI;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class SequenceMapper extends
		Mapper<Object, Text, Text, BytesWritable> {

	public void map(Object key, Text value, Context context)
			throws IOException, InterruptedException {

		String uri = value.toString();
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(uri), conf);
		FSDataInputStream in = null;

		try {

			in = fs.open(new Path(uri));
			byte buffer[] = IOUtils.toByteArray(in);
			context.write(value, new BytesWritable(buffer));

		} finally {
			IOUtils.closeQuietly(in);
		}
	}
}
