package hku.coc.steven;


import java.io.File;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

public class SequenceDriver{
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException{
		
		if (args.length == 2) {
			File f = new File(args[0]);
			System.out.println("Processing files in " + args[0]);

		}
		else{
			System.out.println("Usage: hadoop jar packfile.jar <input directory> <output location>");
		}

		DirectoryToText dtt = new DirectoryToText(args[0]);
		dtt.GenerateDirText();
		
		Job job = Job.getInstance(new Configuration());
		job.setJobName("Pack Files to Sequence File");

		job.setJarByClass(SequenceDriver.class);
		job.setMapperClass(SequenceMapper.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(BytesWritable.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(SequenceFileOutputFormat.class);
		

		FileInputFormat.addInputPath(job, new Path(dtt.getTextLocation()));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.waitForCompletion(true);

		System.out.println("Your files have been well packed into sequence file.");
		
	}
}