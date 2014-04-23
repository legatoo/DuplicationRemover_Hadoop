package hku.coc.steven;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;

public class RemoveDup{
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException{
		Configuration conf = HBaseConfiguration.create();
        //conf.set("hbase.zookeeper.quorum.", "localhost"); 
		
		
		Job job = Job.getInstance(conf);
		
		job.setJarByClass(RemoveDup.class);
		job.setMapperClass(RemoveMapper.class);
		job.setReducerClass(RemoveReducer.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(CompositeWritable.class);
		
		//input is not the small files
		job.setInputFormatClass(SequenceFileInputFormat.class);
				
		FileInputFormat.addInputPath(job, new Path(args[0]));
		
		// define output table
		//we assume a table 'dup','cf' is in your hbase
		//create 'dup','cf'      --> create table in Hbase
		
		TableMapReduceUtil.initTableReducerJob("dup", RemoveReducer.class, job);

		job.waitForCompletion(true);
	}
}