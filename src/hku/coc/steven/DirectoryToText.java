package hku.coc.steven;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.Progressable;


public class DirectoryToText{
	
	private String targetDir;
	private String outputLoc;
	
	public DirectoryToText(String dir){
		this.targetDir = dir;
		this.outputLoc = new String("/dirText");
	}
	
	public void GenerateDirText() throws IOException{
		System.out.println("enter the function");
		Configuration confHadoop = new Configuration();
		FileSystem fs = FileSystem.get(confHadoop);
		Path file = new Path(outputLoc);
				
		if (fs.exists(file)) {
			fs.delete(file, true);
		}
		
		OutputStream os = fs.create(file, new Progressable() {
			public void progress() {
				System.out.println("...........Working..........");
			}
		});
		
		BufferedWriter br = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
		
		FileStatus[] status = fs.listStatus(new Path(targetDir));
		for (int i = 0; i < status.length; i++) {

			Path filePath = status[i].getPath();
			System.out.println("Processing " + filePath.getName() + "  "
					+ String.valueOf(i) + "/" + String.valueOf(status.length));

			br.write(fs.getUri() + targetDir + "/" + filePath.getName());
			br.newLine();
		}

		br.close();
		fs.close();
		System.out.println("Done.");
	}
	
	public String getTextLocation(){
		return this.outputLoc;
	}
}