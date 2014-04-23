package hku.coc.steven;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class RemoveMapper extends
		Mapper<Text, BytesWritable, Text, CompositeWritable> {

	public void map(Text key, BytesWritable value, Context context)
			throws IOException, InterruptedException {
		String md5Str;
		try {
			
			md5Str = SupportFunc.MD5String(value.copyBytes());
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			context.setStatus("Internal error - can't find the algorithm for calculating the MD5");
			return;
		}
		
		Text md5Text = new Text(md5Str);
		String imageID = key.toString();
		CompositeWritable output = new CompositeWritable(imageID,
				value.getBytes());

		context.write(md5Text, output);
	}
	
	
}