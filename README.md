DuplicationRemover_Hadoop
=========================

Detect (and remove) duplications in large cluster. Using Hadoop and HBase.

1. Generate sequence file to boost map reduce process

Jar: pacjfiles.jar
Usage: 
hadoop jar packfiles.jar <input dir> <output dir>
Example: 
hadoop jar packfiles.jar /inputFiles /seqdata

Where /inputFiles contains the files need to be processed
Output: After run, there will be a directory "/seqdata” locates HDFS /

2. Remove duplicate files

Requirement: build base table in hbase shell first
hbase shell
create 'dup','cf'
scan 'dup'
	
Jar: removeDup.jar
Usage: 
hadoop jar removedup.jar <input sequence file>
Example: 
hadoop jar removedup.jar /seqdata
where "/seqdata” is sequence file directory generated in the previous step.
Output: in hbase shell do, scan 'dup'

3. Search Duplication files

Jar: search.jar
Usage: 
hadoop jar search.jar <query file>
Example: 
hadoop jar search.jar /xxxxxxx.xxx
where “/xxxxxx.xxxx” is query file directory on HDFS.
Output: See the console





