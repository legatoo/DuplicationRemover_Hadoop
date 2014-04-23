/*
 * @author Steven Yifan Yang
 * @UID 2013950747
 */
package hku.coc.steven;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;


public class CompositeWritable implements Writable {
    
    private Text filePath;
    
    private BytesWritable fileData;
 

    public CompositeWritable() {
    	this.filePath = new Text();
    	this.fileData = new BytesWritable();
    }

    public CompositeWritable(String val1, byte[] val2) {
        this.filePath = new Text(val1);
        this.fileData = new BytesWritable(val2);
    }


	public void readFields(DataInput datainput) throws IOException {
		filePath.readFields(datainput);
		fileData.readFields(datainput);
	}

	public void write(DataOutput dataoutput) throws IOException {
		filePath.write(dataoutput);
		fileData.write(dataoutput);
	}
	
	
	public Text getFilePath(){
		return filePath;
	}
	
	
	public BytesWritable getFileData(){
		return fileData;
	}
	
	public int hashCode() {
		
		return filePath.hashCode();
	}

}