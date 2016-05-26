Java to compress Small files.
-----------------------------

import java.io.IOException;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

class smFileInputFormat extends
		FileInputFormat<NullWritable, BytesWritable> {
	@Override
	protected boolean isSplitable(JobContext context, Path file) {
		return true; 
	}

	@Override
	public RecordReader<NullWritable, BytesWritable> createRecordReader(
			InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
		smFileRecordReader reader = new smFileRecordReader();
		reader.initialize(split, context);
		return reader;
	}
}
//--------------------------
 
class smFileRecordReader extends RecordReader<NullWritable, BytesWritable> {
private FileSplit fileSplit;
private Configuration conf;
private BytesWritable value = new BytesWritable();
private boolean processed = false;
 
@Override
public void initialize(InputSplit split, TaskAttemptContext context)
throws IOException, InterruptedException {
this.fileSplit = (FileSplit) split;
this.conf = context.getConfiguration();
}
 
@Override
public boolean nextKeyValue() throws IOException, InterruptedException {
if (!processed) {
byte[] contents = new byte[(int) fileSplit.getLength()];
Path file = fileSplit.getPath();
FileSystem fs = file.getFileSystem(conf);
FSDataInputStream in = null;
try {
in = fs.open(file);
IOUtils.readFully(in, contents, 0, contents.length);
value.set(contents, 0, contents.length);
} finally {
IOUtils.closeStream(in);
}
processed = true;
return true;
}
return false;
}
 
@Override
public NullWritable getCurrentKey() throws IOException, InterruptedException {
return NullWritable.get();
}
 
@Override
public BytesWritable getCurrentValue() throws IOException, InterruptedException {
return value;
}
 
@Override
public float getProgress() throws IOException {
return processed ? 1.0f : 0.0f;
}
 
@Override
public void close() throws IOException {
// do nothing
}
}
//------------------------
	public class SmallFilesToSequenceFile extends Configured implements Tool {
	public static class SequenceFileMapper extends
			Mapper<NullWritable, BytesWritable, Text, BytesWritable> {
		private Text filename;

		@Override
		protected void setup(Context context) throws IOException,
				InterruptedException {
			InputSplit split = context.getInputSplit();
			Path path = ((FileSplit) split).getPath();
			filename = new Text(path.toString());
		}

		@Override
		protected void map(NullWritable key, BytesWritable value,
				Context context) throws IOException, InterruptedException {
			context.write(filename, value);
		}
	}

	@Override
	public int run(String[] args) throws Exception {
        Path inputFile = new Path("/tmp/xbblwv5/");
        Path outputFile = new Path("/seqout");
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);
		job.setJarByClass(SmallFilesToSequenceFile.class);
		job.setJobName("smallfilestoseqfile");
		job.setInputFormatClass(smFileInputFormat.class);
		job.setOutputFormatClass(SequenceFileOutputFormat.class);
		
		// compression tech used
		SequenceFileOutputFormat.setOutputCompressionType(job,org.apache.hadoop.io.SequenceFile.CompressionType.BLOCK);
		SequenceFileOutputFormat.setCompressOutput(job,true);
		conf.set("mapreduce.output.fileoutputformat.compress.codec","org.apache.hadoop.io.compress.SnappyCodec");

		job.setNumReduceTasks(1);
		FileInputFormat.setInputPaths(job, inputFile);
	    FileOutputFormat.setOutputPath(job, outputFile);
	    
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(BytesWritable.class);
		job.setMapperClass(SequenceFileMapper.class);
		return job.waitForCompletion(true) ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new SmallFilesToSequenceFile(), args);
		System.exit(exitCode);
	}
}
--------------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------------

hadoop --config /etc/hadoop/conf jar SmallFilesToSequenceFile.jar SmallFilesToSequenceFile  -libjars $LIB_JARS



-------------------------------------------------------------------------------------------------

==========================================================================================================
B4 Compression 
.........Status: HEALTHY
 Total size:  147965145 B         ///------ 147.8 mb
 Total dirs:  9
 Total files: 35
 Total symlinks:   0
 Total blocks (validated): 36 (avg. block size 4110142 B)
 Minimally replicated blocks: 36 (100.0 %)
 Over-replicated blocks: 0 (0.0 %)
 Under-replicated blocks: 14 (38.88889 %)
 Mis-replicated blocks:  0 (0.0 %)
 Default replication factor: 3
 Average block replication: 4.9444447
 Corrupt blocks:   0
 Missing replicas:  28 (13.592233 %)
 Number of data-nodes:  8
 Number of racks:   1
FSCK ended at Tue Apr 07 10:49:43 EDT 2015 in 6 milliseconds


The filesystem under path '/tmp/xbblwv5' is HEALTHY


After Compression:

[root@r37bn00 seq]# hdfs fsck /seqout
  No encryption was performed by peer.
Connecting to namenode via http://r36yn10.bnymellon.net:50070
FSCK started by dshmbtm (auth:KERBEROS_SSL) from /10.59.90.136 for path /seqout at Tue Apr 07 10:48:08 EDT 2015
..Status: HEALTHY
 Total size:  4832691 B              //----   4.8 MB
 Total dirs:  1
 Total files: 2
 Total symlinks:   0
 Total blocks (validated): 1 (avg. block size 4832691 B)
 Minimally replicated blocks: 1 (100.0 %)
 Over-replicated blocks: 0 (0.0 %)
 Under-replicated blocks: 0 (0.0 %)
 Mis-replicated blocks:  0 (0.0 %)
 Default replication factor: 3
 Average block replication: 3.0
 Corrupt blocks:   0
 Missing replicas:  0 (0.0 %)
 Number of data-nodes:  8
 Number of racks:   1
FSCK ended at Tue Apr 07 10:48:08 EDT 2015 in 1 milliseconds


The filesystem under path '/seqout' is HEALTHY

=============================================================================
without Compression

[root@r37bn00 seq]# hdfs fsck /seqout
  No encryption was performed by peer.
Connecting to namenode via http://r36yn10.bnymellon.net:50070
FSCK started by dshmbtm (auth:KERBEROS_SSL) from /10.59.90.136 for path /seqout at Tue Apr 07 10:54:45 EDT 2015
..Status: HEALTHY
 Total size:  127620670 B     ///----- 127 mb approx
 Total dirs:  1
 Total files: 2
 Total symlinks:   0
 Total blocks (validated): 1 (avg. block size 127620670 B)
 Minimally replicated blocks: 1 (100.0 %)
 Over-replicated blocks: 0 (0.0 %)
 Under-replicated blocks: 0 (0.0 %)
 Mis-replicated blocks:  0 (0.0 %)
 Default replication factor: 3
 Average block replication: 3.0
 Corrupt blocks:   0
 Missing replicas:  0 (0.0 %)
 Number of data-nodes:  8
 Number of racks:   1
FSCK ended at Tue Apr 07 10:54:45 EDT 2015 in 1 milliseconds


The filesystem under path '/seqout' is HEALTHY


====================================================


--------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------
Extract keys from merged sequence file:----
--------------------------------------

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.Reader;
import org.apache.hadoop.io.Text;
 
public class SequenceFileKeyExtractor {
public static void main(String[] args) throws Exception {
Configuration conf = new Configuration();
Path path = new Path(args[0]);
SequenceFile.Reader reader = null;
try {
reader = new SequenceFile.Reader(conf, Reader.file(path));
Text key = new Text();
while (reader.next(key)) { System.out.println(key);
}
} finally {
IOUtils.closeStream(reader);
}
}
}

--------------------------------------------------------------------------------------------------
hadoop --config /etc/hadoop/conf jar SequenceFileKeyExtractor.jar SequenceFileKeyExtractor  -libjars $LIB_JARS
==========================================================================================================

[root@r37bn00 seq]# hadoop --config /etc/hadoop/conf jar SequenceFileKeyExtractor.jar SequenceFileKeyExtractor  -libjars $LIB_JARS
  No encryption was performed by peer.
15/04/07 11:07:05 INFO zlib.ZlibFactory: Successfully loaded & initialized native-zlib library
15/04/07 11:07:05 INFO compress.CodecPool: Got brand-new decompressor [.deflate]
15/04/07 11:07:05 INFO compress.CodecPool: Got brand-new decompressor [.deflate]
15/04/07 11:07:05 INFO compress.CodecPool: Got brand-new decompressor [.deflate]
15/04/07 11:07:05 INFO compress.CodecPool: Got brand-new decompressor [.deflate]
hdfs://myclustertesttpc/tmp/xbblwv5/access_log-20150315
hdfs://myclustertesttpc/tmp/xbblwv5/access_log-20150322
hdfs://myclustertesttpc/tmp/xbblwv5/access_log-20150329
hdfs://myclustertesttpc/tmp/xbblwv5/access_log-20150405
hdfs://myclustertesttpc/tmp/xbblwv5/pig_142
hdfs://myclustertesttpc/tmp/xbblwv5/pig_143
hdfs://myclustertesttpc/tmp/xbblwv5/pig_144




files stored locations:
hdfs location -/tmp/xbblwv5/

/var/log/storm


======================================================


Notes :: 
1. Fails when issplitable is false(reason - reading 2.5GB file at one go throws error) 
2. Setting issplitable is true makes it run.