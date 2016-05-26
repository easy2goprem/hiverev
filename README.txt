R-REF
=====
R Server Restart 
----------------
Details:
r37cn00
http://r37cn00.bnymellon.net:8787

r37dn00
http://r37dn00.bnymellon.net:8788

Steps:

Login to r37dn00
sesu - root
df /users
                if 100%, need to delete some files.
ps -ef | grep rstudio-server
rstudio-server stop
rstudio-server start
==================================================================================
Reading Data from R
-------------------
Step 1 : kinit 
kinit username@PAC.BNYMELLON.NET
Step 2: 
$ R
Step 3:  Install RHive, RJava and RServe  
>install.packages('Rserve')
>install.packages('rJava')
>install.packages('RHive')
Step 4: Using Rhive library 
>library(RHive)
>Sys.setenv(HADOOP_HOME = "/usr/lib/hadoop/")
>Sys.setenv(HIVE_HOME="/etc/hive/") 
>Sys.setenv(RHIVE_FS_HOME="/tmp/testr") 
>Sys.setenv(RHIVE_FS_HOME="/tmp/testr")
>rhive.init(hiveHome = "/etc/hive/",hiveLib = "/etc/hive/lib" , hadoopHome = "/usr/lib/hadoop/", hadoopLib = "/usr/lib/hadoop/lib/", hadoopConf = "/usr/lib/hadoop/etc/hadoop")

Step 5: Reading HDFS data using RHive
rhive.hdfs.cat("/apps/hive/warehouse/emp_info_table/emp_info")
132,steve,IT_dev
135,ray,IT_dev
153,roy,IT_dev
163,john,IT_Supp
=============================================================================================
R to HDFS via rhdfs 
-------------------
Download package from github
>rhdfs_1.0.8.tar.gz
Requred packages
>rJava
Install step
 >export HADOOP_CMD=/usr/lib/hadoop/bin/hadoop
 > R CMD INSTALL ~/rhdfs_1.0.8.tar.gz
 Config step
 >library(rhdfs)
 >hdfs.init()
 >hdfs.ls('/')
 
 conn <- hdfs.file("/tmp/testr.txt","r",buffersize=104857600);
 fread <- hdfs.read(conn);
 fil <- unserialize(fread);
 
f <- rawToChar(fread)
 data = read.table(textConnection(f), sep = "\t");
 
 ----------------------------------------------
 ####Reading a text file 
 con <- hdfs.file("/tmp/rest.txt","r",buffersize=104857600);
 fread1 <- hdfs.read.text.file("/tmp/rest.txt")
 print(fread1)
> print(fread1)
[1] "hadoop" "rest"   "api"    ""
 -------------------------------------------------------
 ####Reading a numeric file 
 conn <- hdfs.file("/tmp/testr.txt","r",buffersize=104857600);
 fread <- hdfs.read(conn);
 f <- rawToChar(fread)
 > print(f)
[1] "0\t21.5\t43\t0\n0.5\t28.2\t56.4\t14.1\n1\t32.5\t65\t32.5\n1.5\t35.3\t70.6\t52.95\n2\t37.7\t75.4\t75.4\n2.5\t39.2\t78.4\t98\n3\t40.1\t80.2\t120.3\n4\t41.2\t82.4\t164.8\n5\t42.2\t84.4\t211\n7\t43.6\t87.2\t305.2\n10\t45.6\t91.2\t456\n"

 -----------------------------------------------------------
 Same data with read.table 
 data = read.table(textConnection(f), sep = "\t");
 > print(data)
     V1   V2   V3     V4
1   0.0 21.5 43.0   0.00
2   0.5 28.2 56.4  14.10
3   1.0 32.5 65.0  32.50
4   1.5 35.3 70.6  52.95
5   2.0 37.7 75.4  75.40
6   2.5 39.2 78.4  98.00
7   3.0 40.1 80.2 120.30
8   4.0 41.2 82.4 164.80
9   5.0 42.2 84.4 211.00
10  7.0 43.6 87.2 305.20
11 10.0 45.6 91.2 456.00

d1 <-data.frame(data)
f1<-d1$V1
print(f1)
 [1]  0.0  0.5  1.0  1.5  2.0  2.5  3.0  4.0  5.0  7.0 10.0

 logic <- f1>4
 > print(logic)
 [1] FALSE FALSE FALSE FALSE FALSE FALSE FALSE FALSE  TRUE  TRUE  TRUE

 f2<-d1$V2
 f3<-d1$V3
 f4<-d1$V4
 mat <- cbind(f1,f2)     ---------Column binding
 mat <- rbind(f1,f2,f3,f4)   ----------row binding
 
 out<-outer(f1,f2)  --------outer product.
 
 matr<-f1%*%f2;
  
  curve(f1, from = 0, to = 10, n = 101,
           type = "l", xname = "x", xlab = xname, ylab = NULL,
           log = NULL, xlim = NULL)
		   
-----------------------
summary(data)
  V1               V2              V3              V4
 Min.   : 0.000   Min.   :21.50   Min.   :43.00   Min.   :  0.00
 1st Qu.: 1.250   1st Qu.:33.90   1st Qu.:67.80   1st Qu.: 42.73
 Median : 2.500   Median :39.20   Median :78.40   Median : 98.00
 Mean   : 3.318   Mean   :37.01   Mean   :74.02   Mean   :139.11
 3rd Qu.: 4.500   3rd Qu.:41.70   3rd Qu.:83.40   3rd Qu.:187.90
 Max.   :10.000   Max.   :45.60   Max.   :91.20   Max.   :456.00
-----------------------
 > t.test(f1,f2)

        Welch Two Sample t-test

data:  f1 and f2
t = -14.3115, df = 13.431, p-value = 1.623e-09
alternative hypothesis: true difference in means is not equal to 0
95 percent confidence interval:
 -38.76013 -28.62169
sample estimates:
mean of x mean of y
 3.318182 37.009091

 -----------------------
 search()
 [1] ".GlobalEnv"        "package:rhdfs"     "package:rJava"
 [4] "package:stats"     "package:graphics"  "package:grDevices"
 [7] "package:utils"     "package:datasets"  "package:methods"
[10] "Autoloads"         "package:base"
------------------------
 points(f1, col = 1:4, pch = 8, cex = 2)
 ------------------------------------------------------------
 
1. The entities that R creates and manipulates are known as objects. 
>objects() or ls()
2. Can remove those objects by 
>rm(obj_name)
3. these obj ll be stored in .RData


 setwd(


  out<-read.table(pipe("hadoop dfs -cat '/tmp/testdata/test.csv'"), sep=",", header=TRUE) 
  ============================================================================================================================
  Reference 

http://www.slideshare.net/SeonghakHong/rhive-tutorial-installation
http://ftp.iitm.ac.in/

http://ftp.iitm.ac.in/cran/src/contrib/Rserve_1.7-3.tar.gz
=======================================================================
rJava not package is not installed 

The downloaded source packages are in
        â/tmp/RtmpXpksu1/downloaded_packagesâ    ------------  Success 

		
		------------------------------------
install.packages('Rserve')
install.packages('rJava')
install.packages('RHive')
library(Rserve)
library(rJava)
step 1
	------------------------------------
	krbtgt/HADOOP-DQA-CNJ01.BNYMELLON.NET@HADOOP-DQA-CNJ01.BNYMELLON.NET
	
	kinit -kt /etc/security/keytabs/hbase.headless.keytab dshmbnm@HADOOP-DQA-CNJ01.BNYMELLON.NET
	
	kinit -kt /etc/security/keytabs/hive.headless.keytab dshmhtm@HADOOP-DQA-TPC01.BNYMELLON.NET
	
	kinit -kt /etc/security/keytabs/hbase.headless.keytab dshmbtm@HADOOP-DQA-TPC01.BNYMELLON.NET
	
	kinit xbblwv5@PAC.BNYMELLON.NET

	library(RJDBC)

-----------------------------------------------------
create table orc_tmp_table (
 emp_id INT,emp_name STRING,commit_id STRING,app STRING)
 ROW FORMAT DELIMITED
 FIELDS TERMINATED BY ','
 STORED AS ORC;


load data inpath '/tmp/emp_info' into table emp_info_table;

select * from emp_info_table;


FROM temp_tbl1 t
INSERT INTO TABLE orc_tmp_table 
SELECT t.emp_id, t.emp_name, t.commit_id, t.application;
	----------------------------------------------------
R		
library(RHive)
Sys.setenv(HADOOP_HOME = "/usr/lib/hadoop/")
Sys.setenv(HIVE_HOME="/etc/hive/") 
Sys.setenv(RHIVE_FS_HOME="/tmp/testr") 
Sys.setenv(RHIVE_FS_HOME="/tmp/testr")
rhive.init(hiveHome = "/etc/hive/",hiveLib = "/etc/hive/lib" , hadoopHome = "/usr/lib/hadoop/", hadoopLib = "/usr/lib/hadoop/lib/", hadoopConf = "/usr/lib/hadoop/etc/hadoop")

rhive.hdfs.cat("/apps/hive/warehouse/emp_info_table/emp_info")


 Sys.setenv("HADOOP_CMD"="/usr/lib/hadoop/bin/hadoop")

install.packages('plyrmr')
 
rhive.init()
 
rhive.connect(host="r39an00.bnymellon.net:9083", port=9083, hiveServer2=TRUE)

rhive.hdfs.ls("/tmp/")
 
rhive.hdfs.cat("/tmp/testr.txt")

rhive.hdfs.cat("/apps/hive/warehouse/test3/test")

a <- rhive.hdfs.cat("/apps/hive/warehouse/test3/test")

/apps/hive/warehouse/test3/test
r38yn10.bnymellon.net:50070

read.table("http://r38yn10.bnymellon.net:50070/apps/hive/warehouse/test3/test")

setwd("http://r38yn10.bnymellon.net:50070")


hsLineReader(con,chunkSize=-1,FUN=print)


rhive.hdfs.histogram("/tmp/testr.txt")

mydata = read.table("test")

readLines("test")

mydata = read.csv("test.csv")

read.*("/tmp/testr.txt")

A <- as.matrix(read.table("./tmp/testr.txt"))

rhdfs.init()

//Sys.setenv("RHIVE_FS_HOME"="/apps/hive/warehouse/testr.db/lib")
//rhive.list.tables()
test <- rhive.write.table(test)
========================================================================================
R to HDFS via rhdfs 
-------------------
Download package from github
>rhdfs_1.0.8.tar.gz
Requred packages
>rJava
Install step
 >export HADOOP_CMD=/usr/lib/hadoop/bin/hadoop
 > R CMD INSTALL ~/rhdfs_1.0.8.tar.gz
 Config step
 >library(rhdfs)
 >hdfs.init()
 >hdfs.ls('/')
 
 
Sys.setenv("HADOOP_PREFIX"="/usr/lib/hadoop/hadoop-nfs-2.4.0.2.1.2.0-402.jar")
Sys.setenv("HADOOP_CMD"="/usr/lib/hadoop/bin/hadoop")
Sys.setenv("HADOOP_STREAMING"="/usr/lib/hadoop-mapreduce/hadoop-streaming-2.4.0.2.1.2.0-402.jar")

export HADOOP_CMD=/usr/lib/hadoop/bin/hadoop

hdfs.init()


---------------------------------------------------------------
10.59.90.135
hdfs://myclustertesttpc

rhive.connect("r37an00.bnymellon.net:10000")

rhive.connect("10.59.90.135", defaultFS="hdfs://myclustertesttpc:9083")
 
 :hdfs://myclustertesttpc/apps/hive/warehouse/testr.db/
 
 ------------------------------------------------------------
 
 rhive.env()
        hadoop home: /usr/lib/hadoop/
        hadoop conf: /usr/lib/hadoop/etc/hadoop
        fs: hdfs://myclustertestcnj
        fs home: /tmp/testr
        hive home: /etc/hive/
        user name: xbblwv5
        user home: /users/home/xbblwv5
        temp dir: /tmp/xbblwv5R>

		Sys.setenv("RHIVE_FS"="hdfs://r37an00.bnymellon.net") 
		
		//r39an00.bnymellon.net:9083
		
rhive.connect(host="r39an00.bnymellon.net", port=10000, hiveServer2=TRUE)

rhive.connect("r39an00.bnymellon.net:9083")		
---------------------------------------------
Sys.setenv(HIVE_HOME=”/usr/local/hive”)

rhive.connect("r37an00.bnymellon.net:9083")
 
 hdfs dfs -chmod +rwx /tmp/testr

===========================================================================

/etc/rstudio/rserver.conf   ---- edited to 8089

 netstat -anp | grep 8089

[root@r39cn00 ~]# netstat -anp | grep 8089
tcp        0      0 0.0.0.0:8089                0.0.0.0:*                   LISTEN      58003/rserver

=======================================================================================================

*****************************************************************************************************************************************************************
PIG-REF
=======
Pig JAR to use Hcatalog without using -useHCatalog
---------------------------------------------
REGISTER /usr/hdp/2.2.6.0-2800/hive-hcatalog/share/hcatalog/hive-hcatalog-core-0.14.0.2.2.6.0-2800.jar
REGISTER /usr/hdp/2.2.6.0-2800/hive/lib/hive-exec-0.14.0.2.2.6.0-2800.jar
REGISTER /usr/hdp/2.2.0.0-2041/hive/lib/hive-metastore.jar

REGISTER /usr/hdp/2.2.6.0-2800/hive-hcatalog/share/hcatalog/hive-hcatalog-*.jar
REGISTER /usr/hdp/2.2.6.0-2800/hive/lib/hive-metastore-*.jar
REGISTER /usr/hdp/2.2.6.0-2800/hive/lib/libthrift-*.jar
REGISTER /usr/hdp/2.2.6.0-2800/hive/lib/hive-exec-*.jar
REGISTER /usr/hdp/2.2.6.0-2800/hive/lib/libfb303-*.jar
REGISTER /usr/hdp/2.2.6.0-2800/hive/lib/jdo-api-*.jar
REGISTER /usr/hdp/2.2.6.0-2800/ranger-hive-plugin/install/lib/slf4j-api-1.7.5.jar

REGISTER /users/home/bcamuvm/ucitsv/scripts/temp/ucitsvUDF-0.0.1-SNAPSHOT.jar
DEFINE getRandomVal dsh.bnymellon.ucitsvUDF.GetRandom();

--Clean the folder for second run
SET mapred.fairscheduler.pool 'inhouse';
--set pig.schematuple true;
set mapreduce.map.java.opts '-Xmx7168m -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+CMSClassUnloadingEnabled -XX:+CMSScavengeBeforeRemark -XX:+DisableExplicitGC -Djava.awt.headless=true -Xloggc:/tmp/@taskid@.gc -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp/pig.hprof';
set mapreduce.reduce.java.opts -Xmx4096m;
set mapreduce.reduce.memory.mb 8192;
set mapreduce.map.memory.mb 8192;
REGISTER /usr/hdp/2.2.6.0-2800/pig/lib/piggybank.jar;
%default name 'filterddata'
--fs -rm -r -skipTrash $hdfs_path_processed_feed_store


===================================================================================
Config Params 
-------------
set mapred.child.java.opts -Xmx1024m;
set io.sort.mb 256;
set io.sort.factor 	100;
set mapred.compress.map.output true;
set pig.exec.mapPartAgg true;
set pig.cachedbag.memusage 0.2;

%declare CURR_DATE `date +%Y%m%d`;
================================================================================
Using Json Storage:
{"id1":"foo", "id2":"bar", "type":"type1"}
{"id1":"foo", "id2":"bar", "type":"type2"}
data1 = LOAD '/tmp/test.json' USING JsonLoader('id1:chararray,id2:chararray,type:chararray');
store data1 into 'json_data' using JsonStorage();
Using Avro Storage:
REGISTER /usr/lib/pig/lib/avro-mapred-1.7.4.jar
	To load data with any other format and convert it into Avro
data2 = LOAD '/tmp/test.json' USING JsonLoader('id1:chararray,id2:chararray,type:chararray');
STORE data2 INTO 'measurements' USING AvroStorage('measurement');
	To Load Avro data by itself
data2 = LOAD 'measurements/part-m-00000.avro' USING AvroStorage('id1:chararray,id2:chararray,type:chararray');
STORE data2 INTO 'avro_data' USING AvroStorage();
Using XML Loader:
	Data used
<revision>
      <id>1</id>
      <username>muehlburger</username>
</revision>
<revision>
      <id>2</id>
      <username>muehlburger</username>
</revision>
<revision>
      <id>3</id>
      <username>user1</username>
</revision>
-------------
REGISTER /usr/lib/pig/piggybank.jar
DEFINE RegexExtractAll org.apache.pig.piggybank.evaluation.string.RegexExtractAll();
revisionXML = LOAD 'tmp/text1.xml' USING org.apache.pig.piggybank.storage.XMLLoader('revision') AS (x:chararray);
rev = FOREACH revisionXML GENERATE FLATTEN (RegexExtractAll(x,'<revision>\\n\\s*<id>([^>]*)</id>\\n\\s*<username>([^>]*)</username>\\n\\s*</revision>')) AS(id_revision:chararray,username:chararray);
HCatalog Configuration:
	export HADOOP_CLASSPATH=$HCAT_HOME/storage-handlers/hbase/lib/hive-hcatalog-hbase-storage-handler-0.13.0.2.1.2.0-402.jar:$HCAT_HOME/hive-hcatalog-core-0.13.0.2.1.2.0-402.jar:$HCAT_HOME/share/hcatalog/hive-hcatalog-pig-adapter-0.13.0.2.1.2.0-402.jar:$HCAT_HOME/share/hcatalog/hive-hcatalog-server-extensions-0.13.0.2.1.2.0-402.jar

	echo $HADOOP_CLASSPATH
	export HCAT_HOME=/usr/lib/hive-hcatalog/share/hcatalog

	export PIG_CLASSPATH=$HCAT_HOME/storage-handlers/hbase/lib/hive-hcatalog-hbase-storage-handler-0.13.0.2.1.2.0-402.jar:$HCAT_HOME/hive-hcatalog-core-0.13.0.2.1.2.0-402.jar:$HCAT_HOME/share/hcatalog/hive-hcatalog-pig-adapter-0.13.0.2.1.2.0-402.jar:$HCAT_HOME/share/hcatalog/hive-hcatalog-server-extensions-0.13.0.2.1.2.0-402.jar

Registers that has to be used while connecting to PIG:
REGISTER /usr/lib/hive-hcatalog/share/hcatalog/*.jar
REGISTER /usr/lib/hive-hcatalog/share/hcatalog/storage-handlers/hbase/lib/*.jar
REGISTER /usr/lib/hadoop-mapreduce/hadoop-mapreduce-client-core-2.4.0.2.1.2.0-402.jar
REGISTER /usr/lib/hadoop-mapreduce/hadoop-mapreduce-client-common-2.4.0.2.1.2.0-402.jar
-------------------------------------------------------------------------------------------------------------------------
Calling PIG from Java
=====================
Export variables for 

export JAVA_HOME=/usr/jdk64/jdk1.7.0_45 
export HIVE_HOME=/usr/hdp/2.2.6.0-2800/hive
export PIG_HOME=/usr/hdp/2.2.6.0-2800/pig
export HCAT_HOME=/usr/hdp/2.2.6.0-2800/hive-hcatalog
export HADOOP_HOME=/usr/hdp/2.2.6.0-2800/hadoop
export  HADOOP_CLASSPATH=$HCAT_HOME/share/hcatalog/*:$HCAT_HOME/share/webhcat/svr/lib/*:$HIVE_HOME/lib/*:$HIVE_HOME/conf:$HADOOP_HOME/conf:$HADOOP_HOME/lib/*:$PIG_HOME/lib/*:$PIG_HOME/lib/h2/*:$PIG_HOME/*:$HADOOP_HOME/client/*:$PIG_HOME/conf:$HADOOP_CLASSPATH

export CLASSPATH=$JAVA_HOME/lib/tools.jar:$JAVA_HOME/db/lib/*:$JAVA_HOME/jre/lib/rt.jar:$HIVE_HOME/lib/*:$HADOOP_HOME/*:$HADOOP_HOME/lib/*:$HADOOP_HOME/client/*:/usr/hdp/2.2.6.0-2800/hadoop-hdfs/*:/usr/hdp/2.2.6.0-2800/hadoop-hdfs/lib/*:$PIG_HOME/lib/*:$PIG_HOME/lib/h2/*:$PIG_HOME/*:$HCAT_HOME/share/hcatalog/*:$HCAT_HOME/share/webhcat/svr/lib/*:

export PATH=$PATH:/usr/lib64/qt-3.3/bin:/usr/local/bin:/bin:/usr/bin:/usr/local/sbin:/usr/sbin:/sbin:/usr/jdk64/jdk1.7.0_45/bin:/usr/lib/hadoop/bin:/usr/hdp/2.2.6.0-2800/hive/bin:/usr/hdp/2.2.6.0-2800/pig/bin

Java Compile 
------------

javac  pig_test.java
 
java -cp $CLASSPATH:.:$HADOOP_HOME/conf pig_test

java -Dhdp.version=2.2.6.0-2800 -cp $CLASSPATH:.:$HADOOP_HOME/conf pig_test

===============================================================
Working for Getting result from Pig Call 
----------------------------------------
import java.io.IOException;
import org.apache.pig.*;
import org.junit.Assert;
import org.apache.pig.tools.pigstats.*;
import org.apache.pig.PigRunner.ReturnCode;
import java.util.*;

public class pig_test {
   public static void main(String[] args)throws IOException {
//PigServer pigServer = new PigServer(ExecType.MAPREDUCE);
//pigServer.registerScript("/users/home/dshmhtm/ucits/test.pig");

      ExecType execType = ExecType.MAPREDUCE;
        String[] args1 = { "-f","/users/home/dshmhtm/ucits/test.pig" };
      PigStats stats = PigRunner.run(args1,null);
        System.out.println(stats.isSuccessful());
        System.out.println(stats.getRecordWritten());

   }
}
=================================================================
TO JSON For Sukumar 

1429626689144::10.78.144.228::9443::4628305~-1234~POST~utf-8~10.78.145.187~/services/DPProcessPayload_Resp~text/xml~10.78.145.187~http://10.78.145.188:8280~rfusp0c.bnymellon.net:9763~eedf0c34-56ec-473d-ac19-26dce3745137~IN~DPProcessPayload_Resp~LogActivityRecordEntry~urn:uuid:7f36b1da-fccf-4011-9ced-5548617fdc17~1429626689128~Process~01201500009459~01201500003636~01735~P~M~ACTV~PRA~FUPD~HFAD~P~01201500009459~D~~~~2015-04-21T10:31:29.0394366-04:00~XECC35P~AIS~~~~~295908.0~0.0~0.0~295908.0~0.0~0.0~0.0~0.0~0.0~0.0~BUY~POSTED~WSOMTCXPRA11~~VALUE~~Manual Trade~~Susa Fund Management LLP - UAT~Susa European Equities Fund L.P.~Susa European Equities Fund L.P.~Susa European Equities Fund L.P. Class A Interests Unrestricted~The Kahn Holdings Ltd Partnership~00:01:02~~~~~~~~~~~~~~~~~~~~U~2.0~PRA~NA~~~~~0.0~~172766~1429180807~2015-04-21 10:31:29.527195~2015-04-21~2015~4~21~10~31     NULL     NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL     NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL     NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL     NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL    NULL     NULL    NULL    NULL    NULL    NULL    NULL
======================
select row_key_id from T_DP_Analytics_For_ES_index limit 1;
hive> select row_key_id from T_DP_Analytics_For_ES_index limit 1;
Query ID = dshmhtm_20150507063131_c9f6c7ff-16b4-4f32-8531-e87a643706cc
Total jobs = 1
Launching Job 1 out of 1


Status: Running (application id: application_1427971414223_1667)

Map 1: -/-
Map 1: 0/1
Map 1: 0/1
Map 1: 0/1
Map 1: 1/1
Status: Finished successfully
OK
1429626689144::10.78.144.228::9443::4628305~-1234~POST~utf-8~10.78.145.187~/services/DPProcessPayload_Resp~text/xml~10.78.145.187~http://10.78.145.188:8280~rfusp0c.bnymellon.net:9763~eedf0c34-56ec-473d-ac19-26dce3745137~IN~DPProcessPayload_Resp~LogActivityRecordEntry~urn:uuid:7f36b1da-fccf-4011-9ced-5548617fdc17~1429626689128~Process~01201500009459~01201500003636~01735~P~M~ACTV~PRA~FUPD~HFAD~P~01201500009459~D~~~~2015-04-21T10:31:29.0394366-04:00~XECC35P~AIS~~~~~295908.0~0.0~0.0~295908.0~0.0~0.0~0.0~0.0~0.0~0.0~BUY~POSTED~WSOMTCXPRA11~~VALUE~~Manual Trade~~Susa Fund Management LLP - UAT~Susa European Equities Fund L.P.~Susa European Equities Fund L.P.~Susa European Equities Fund L.P. Class A Interests Unrestricted~The Kahn Holdings Ltd Partnership~00:01:02~~~~~~~~~~~~~~~~~~~~U~2.0~PRA~NA~~~~~0.0~~172766~1429180807~2015-04-21 10:31:29.527195~2015-04-21~2015~4~21~10~31
Time taken: 7.591 seconds, Fetched: 1 row(s)

===================
Detailed Table Information      Table(tableName:t_dp_analytics_for_es_index, dbName:default, owner:xeccwmg, createTime:1430836113, lastAccessTime:0, retention:0, sd:StorageDescriptor(cols:[FieldSchema(name:row_key_id, type:string, comment:null), FieldSchema(name:meta_tenant_id, type:bigint, comment:null), FieldSchema(name:meta_http_method, type:string, comment:null), FieldSchema(name:meta_character_set_encoding, type:string, comment:null), FieldSchema(name:meta_remote_address, type:string, comment:null), FieldSchema(name:meta_transport_in_url, type:string, comment:null), FieldSchema(name:meta_message_type, type:string, comment:null), FieldSchema(name:meta_remote_host, type:string, comment:null), FieldSchema(name:meta_service_prefix, type:string, comment:null), FieldSchema(name:meta_host, type:string, comment:null), FieldSchema(name:correlation_bam_activity_id, type:string, comment:null), FieldSchema(name:message_direction, type:string, comment:null), FieldSchema(name:service_name, type:string, comment:null), FieldSchema(name:operation_name, type:string, comment:null), FieldSchema(name:message_id, type:string, comment:null), FieldSchema(name:timestamp, type:bigint, comment:null), FieldSchema(name:payload_type, type:string, comment:null), FieldSchema(name:source_request_id, type:string, comment:null), FieldSchema(name:account_number, type:string, comment:null), FieldSchema(name:client_id, type:string, comment:null), FieldSchema(name:action_type_code, type:string, comment:null), FieldSchema(name:activity_name, type:string, comment:null), FieldSchema(name:activity_status_code, type:string, comment:null), FieldSchema(name:application_mnemonic, type:string, comment:null), FieldSchema(name:business_process_code, type:string, comment:null), FieldSchema(name:business_process_category, type:string, comment:null), FieldSchema(name:business_event_code, type:string, comment:null), FieldSchema(name:enterprise_contextidguid, type:string, comment:null), FieldSchema(name:enterprise_status_code, type:string, comment:null), FieldSchema(name:entp_activity_comment_text, type:string, comment:null), FieldSchema(name:internal_office_number, type:string, comment:null), FieldSchema(name:client_location, type:string, comment:null), FieldSchema(name:last_activity_time_stamp, type:string, comment:null), FieldSchema(name:last_activity_user_id, type:string, comment:null), FieldSchema(name:line_of_business, type:string, comment:null), FieldSchema(name:group_code, type:string, comment:null), FieldSchema(name:nigo_flag, type:string, comment:null), FieldSchema(name:nigo_reasons, type:string, comment:null), FieldSchema(name:service_level_groups, type:string, comment:null), FieldSchema(name:amount_1, type:float, comment:null), FieldSchema(name:amount_2, type:float, comment:null), FieldSchema(name:amount_3, type:float, comment:null), FieldSchema(name:amount_4, type:float, comment:null), FieldSchema(name:amount_5, type:float, comment:null), FieldSchema(name:amount_6, type:float, comment:null), FieldSchema(name:amount_7, type:float, comment:null), FieldSchema(name:amount_8, type:float, comment:null), FieldSchema(name:amount_9, type:float, comment:null), FieldSchema(name:amount_10, type:float, comment:null), FieldSchema(name:miscellaneous_1, type:string, comment:null), FieldSchema(name:miscellaneous_2, type:string, comment:null), FieldSchema(name:miscellaneous_3, type:string, comment:null), FieldSchema(name:miscellaneous_4, type:string, comment:null), FieldSchema(name:miscellaneous_5, type:string, comment:null), FieldSchema(name:guid, type:string, comment:null), FieldSchema(name:miscellaneous_6, type:string, comment:null), FieldSchema(name:miscellaneous_7, type:string, comment:null), FieldSchema(name:miscellaneous_8, type:string, comment:null), FieldSchema(name:miscellaneous_9, type:string, comment:null), FieldSchema(name:miscellaneous_10, type:string, comment:null), FieldSchema(name:miscellaneous_11, type:string, comment:null), FieldSchema(name:miscellaneous_12, type:string, comment:null), FieldSchema(name:miscellaneous_13, type:string, comment:null), FieldSchema(name:miscellaneous_14, type:string, comment:null), FieldSchema(name:miscellaneous_15, type:string, comment:null), FieldSchema(name:miscellaneous_blob_1, type:string, comment:null), FieldSchema(name:miscellaneous_blob_2, type:string, comment:null), FieldSchema(name:optional_context_1, type:string, comment:null), FieldSchema(name:optional_context_2, type:string, comment:null), FieldSchema(name:optional_context_3, type:string, comment:null), FieldSchema(name:optional_context_4, type:string, comment:null), FieldSchema(name:optional_context_5, type:string, comment:null), FieldSchema(name:timestamp_1, type:string, comment:null), FieldSchema(name:timestamp_2, type:string, comment:null), FieldSchema(name:timestamp_3, type:string, comment:null), FieldSchema(name:timestamp_4, type:string, comment:null), FieldSchema(name:timestamp_5, type:string, comment:null), FieldSchema(name:timestamp_6, type:string, comment:null), FieldSchema(name:timestamp_7, type:string, comment:null), FieldSchema(name:timestamp_8, type:string, comment:null), FieldSchema(name:timestamp_9, type:string, comment:null), FieldSchema(name:timestamp_10, type:string, comment:null), FieldSchema(name:request_type, type:string, comment:null), FieldSchema(name:version, type:float, comment:null), FieldSchema(name:originating_application_id, type:string, comment:null), FieldSchema(name:post_application_id, type:string, comment:null), FieldSchema(name:organization_hier_level_1, type:string, comment:null), FieldSchema(name:double_forward_flag, type:string, comment:null), FieldSchema(name:organization_hier_level_2, type:string, comment:null), FieldSchema(name:organization_hier_level_3, type:string, comment:null), FieldSchema(name:unit_cost_per_manhour, type:float, comment:null), FieldSchema(name:entp_activity_reason_text, type:string, comment:null), FieldSchema(name:counter, type:string, comment:null), FieldSchema(name:server_init_timestamp, type:string, comment:null), FieldSchema(name:db_timestamp, type:string, comment:null), FieldSchema(name:db_epoch_timestamp, type:string, comment:null)], location:hdfs://myclustertesttpc/tmp/esdata_05_05_2015, inputFormat:org.apache.hadoop.mapred.TextInputFormat, outputFormat:org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat, compressed:false, numBuckets:-1, serdeInfo:SerDeInfo(name:null, serializationLib:org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe, parameters:{serialization.format=1}), bucketCols:[], sortCols:[], parameters:{}, skewedInfo:SkewedInfo(skewedColNames:[], skewedColValues:[], skewedColValueLocationMaps:{}), storedAsSubDirectories:false), partitionKeys:[], parameters:{numFiles=0, EXTERNAL=TRUE, COLUMN_STATS_ACCURATE=false, transient_lastDdlTime=1430836113, numRows=-1, totalSize=0, rawDataSize=-1}, viewOriginalText:null, viewExpandedText:null, tableType:EXTERNAL_TABLE)
Time taken: 0.524 seconds, Fetched: 98 row(s)

=================================================================
Sample PIG opt 
REGISTER hadoop_udf.jar;

--set mapred.child.java.opts -Xmx128m;
--set io.sort.mb 64;

set mapred.child.java.opts -Xmx1024m;
set io.sort.mb 256;
set io.sort.factor 	100;
set mapred.compress.map.output true;
set pig.exec.mapPartAgg true;
set pig.cachedbag.memusage 0.2;
--set pig.schematuple true;
--set mapreduce.output.fileoutputformat.compress true;
=================================================================
TO get TENANT Space 
REGISTER /usr/hdp/2.2.6.0-2800/pig/piggybank.jar
DEFINE UnixToISO org.apache.pig.piggybank.evaluation.datetime.convert.UnixToISO();

Raw_Data =  LOAD '/tmp/xbblwv5/lsfile.csv' USING PigStorage(',') As (Permission_:chararray,Replication_:chararray,Owner_:chararray,Group_own:chararray,Size_:double,Date1:chararray,time1:chararray,file_name:chararray); 

Form_data = FOREACH Raw_Data GENERATE Size_, SUBSTRING(file_name,1,9) as tenant:chararray;

Segg = GROUP Form_data by tenant;

res_ = FOREACH Segg GENERATE group as tenant, SUM(Form_data.Size_) as Tot_Size;

tenan = FILTER res_ BY (STARTSWITH(tenant,'aduh') == true);

toGB = FOREACH tenan GENERATE tenant, Tot_Size/(double)1073741824 as Size_GB;

*****************************************************************************************************************************************************************
HCATALOG-REF
============


stag_sequence

B = LOAD 'test_hive_txt_rc' USING org.apache.hive.hcatalog.pig.HCatLoader();


STORE A INTO 'hcatpig' USING org.apache.hive.hcatalog.pig.HCatStorer();

===============================================================================

avro 

hcat -e "CREATE TABLE avro_tbl ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.avro.AvroSerDe' STORED AS OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.avro.AvroContainerOutputFormat';"


hcat -e "CREATE TABLE my_avro_table(notused INT) ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.avro.AvroSerDe' STORED as INPUTFORMAT 'org.apache.hadoop.hive.ql.io.avro.AvroContainerInputFormat' OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.avro.AvroContainerOutputFormat';"

pig -useHCatalog


A = LOAD '/measurements/part-m-00000.avro' USING org.apache.hive.hcatalog.pig.HCatLoader();

STORE A INTO 'my_avro_table' USING org.apache.hive.hcatalog.pig.HCatStorer();

*****************************************************************************************************************************************************************
OOZIE-REF
===================================================================================================================
Code Corrected BY HWX Ppl
-------------------------
nameNode=hdfs://myclustertesttpc
jobTracker=r37bn00.bnymellon.net:8050
queueName=default
examplesRoot=examples
oozie.wf.application.path=${nameNode}/tmp/xbblwv5/


---------

<workflow-app xmlns="uri:oozie:workflow:0.2" name="ssh-wf">
    <start to="ssh"/>

    <action name="ssh">
        <ssh xmlns="uri:oozie:ssh-action:0.1">
            <host>dshmhtm@r37cn00.bnymellon.net</host>
            <command>mkdir</command>
            <args>sample</args>
        </ssh>
        <ok to="end"/>
        <error to="fail"/>
    </action>
    <kill name="fail">
        <message>SSH action failed, error message[${wf:errorMessage(wf:lastErrorNode())}]</message>
    </kill>
    <end name="end"/>
</workflow-app>

----------------------------------------------------------
Have to setup a password less ssh connection for connecting with the servers.
-Oozie will be connecting from its server to the node we trying to create directory via ssh action. for that it requires ssh connection. 

have tried with ambari server to its agent<as it has ssh connection to its clients> -- attempt failed with error 

have tried to do ssh action within same ie: same server r37an00(oozie server)  -- attempt failed with error 
===================================================================================================================

With Kerberos
-------------
<workflow-app xmlns="uri:oozie:workflow:0.2.5" name="example-wf">
        <credentials>
                <credential name='hive_credentials' type='hcat'>
                        <property>
                            <name>hcat.metastore.uri</name>
                            <value>thrift://r36qp00.bnymellon.net:9083</value>
                        </property>
                        <property>
                          <name>hive.metastore.kerberos.principal</name>
						  <value>hive/_HOST@HADOOP-PROD-TPC01.BNYMELLON.NET</value>
                        </property>
                </credential>
        </credentials>
<start to="hive-example"/>
<action name="hive-example" cred="hive_credentials">
        <hive xmlns="uri:oozie:hive-action:0.2">
                <job-tracker>${jobTracker}</job-tracker>
                <name-node>${nameNode}</name-node>
                <job-xml>${hiveSiteXML}</job-xml>
                <script>${dbScripts}/hive-example.hql</script>
        </hive>
        <ok to="end"/>
        <error to="fail"/>
</action>
<kill name="fail">
        <message>Workflow failed, error message[${wf:errorMessage(wf:lastErrorNode())}]</message>
</kill>
<end name="end"/>
</workflow-app>
======================================================================================

oozie job -oozie http://r37an00.bnymellon.net:11000/oozie -config job.properties -run

oozie job -oozie http://r37an00.bnymellon.net:11000/oozie -config job.properties -submit


==============================================================================================

input.txt
12595,Steve,XBBLWXW,DWP
12593,John,XBBLDER,EDSA
12594,Broad,XBBLYRE,FYG
12599,Alex,XCCFRTE,EDSA
12354,Kellen,XVVBGRE,DP
12987,Kiwi,XDDFRTY,DWP

oozie pig test - Pig 

load.pig
--------
A = load '$INPUT' using PigStorage(',');
B = foreach A generate $0 as id;
store B into '$OUTPUT' USING PigStorage();

Job.properties
--------------
nameNode=hdfs://myclustertesttpc
jobTracker=r37bn00.bnymellon.net:8050
queueName=default
examplesRoot=/tmp/xbblwv5/pig-oozie
oozie.use.system.libpath=true
oozie.wf.application.path=${nameNode}/tmp/xbblwv5/pig-oozie
oozie.authentication.type=kerberos
oozie.authentication.token.validity=36000
oozie.authentication.signature.secret=
oozie.authentication.cookie.domain=
oozie.authentication.simple.anonymous.allowed=true
oozie.authentication.kerberos.principal=xbblwv5@HADOOP-DQA-TPC01.BNYMELLON.NET
oozie.authentication.kerberos.keytab=/users/home/xbblwv5/oozie_hive/xbblwv5.keytab

workflow.xml
------------
<workflow-app xmlns="uri:oozie:workflow:0.2" name="pig-wf">
    <start to="pig-node"/>
    <action name="pig-node">
        <pig>
            <job-tracker>${jobTracker}</job-tracker>
            <name-node>${nameNode}</name-node>
            <prepare>
                <delete path="${nameNode}/${examplesRoot}/pig/"/>
            </prepare>
            <configuration>
                <property>
                    <name>mapred.job.queue.name</name>
                    <value>${queueName}</value>
                </property>
                <property>
                    <name>mapred.compress.map.output</name>
                    <value>true</value>
                </property>
            </configuration>
            <script>load.pig</script>
            <param>INPUT=${examplesRoot}/input.txt</param>
            <param>OUTPUT=${examplesRoot}/pig/</param>
        </pig>
        <ok to="end"/>
        <error to="fail"/>
    </action>
    <kill name="fail">
        <message>Pig failed, error message[${wf:errorMessage(wf:lastErrorNode())}]</message>
    </kill>
    <end name="end"/>
</workflow-app>

============================================================================================

*****************************************************************************************************************************************************************
HIVE-REF
===================================================================================================================

Beeline Command 
----------------
!connect  jdbc:hive2://r38qp00.bnymellon.net:10000/default;principal=hive/_HOST@HADOOP-PROD-CNJ01.BNYMELLON.NET
==================================================================================================================
Creating External Table for HBASE 
---------------------------------

CREATE EXTERNAL TABLE testhbase1(key string,w MAP<STRING,STRING>)
STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
WITH SERDEPROPERTIES ( 'hbase.columns.mapping' = ':key,d:')
TBLPROPERTIES ( 'hbase.table.name' = 'Ucits_rk_fnd_pos_sec_14feb16',"hbase.struct.autogenerate"="true" );

SELECT key,
w['fundData::FEED_SOURCE_TYPE']	as	fundData_FEED_SOURCE_TYPE,
w['fundData::SOURCE_CLIENT_NAME']	as	fundData_SOURCE_CLIENT_NAME,
w['fundData::FEED_SRC_TIMESTAMP']	as	fundData_FEED_SRC_TIMESTAMP,
w['fundData::FEED_SRC_DATE']	as	fundData_FEED_SRC_DATE,
w['fundData::ACCT_CD']	as	fundData_ACCT_CD,
w['fundData::ACCT_NAME']	as	 fundData_ACCT_NAME,
w['fundData::ACCT_TYP_CD']	as	fundData_ACCT_TYP_CD,
w['fundData::F_CRRNCY_CD']	as	fundData_F_CRRNCY_CD,
w['fundData::MKT_VAL']	as	fundData_MKT_VAL..............
====================================================================================================================
Inserting info sec table with timestamp(with millisecond) as requested RON.
---------------------------------------------------------------------------
From default.info_sec_tmp s
select s.MISCELLANEOUS_1,
cast(REGEXP_REPLACE(substr(REGEXP_REPLACE(s.LAST_ACTIVITY_TIME_STAMP,'[^a-zA-Z0-9]+',''),1,17),'(\\d{2})(\\d{2})(\\d{4})(\\d{2})(\\d{2})(\\d{2})(\\d{3})','$3-$1-$2 $4:$5:$6.$7') as timestamp),
s.MISCELLANEOUS_2,
s.MISCELLANEOUS_3,
s.MISCELLANEOUS_4,
s.TIMESTAMP_2,
s.MISCELLANEOUS_5,
s.MISCELLANEOUS_6,
s.MISCELLANEOUS_7,
cast(substr(REGEXP_REPLACE(s.LAST_ACTIVITY_TIME_STAMP,'[\\[\\]]+',''),7,4) as int) as  info_year,
cast(substr(REGEXP_REPLACE(s.LAST_ACTIVITY_TIME_STAMP,'[\\[\\]]+',''),1,2) as int) as  info_month,
cast(substr(REGEXP_REPLACE(s.LAST_ACTIVITY_TIME_STAMP,'[\\[\\]]+',''),4,2) as int) as  info_day,
cast(substr(REGEXP_REPLACE(s.LAST_ACTIVITY_TIME_STAMP,'[\\[\\]]+',''),12,2) as int) as info_hour
where
cast(substr(REGEXP_REPLACE(from_unixtime(unix_timestamp(substr(REGEXP_REPLACE(s.LAST_ACTIVITY_TIME_STAMP,'[\\[\\]]+',''),2,19),'MM-dd-yyyy.HH:mm:ss'),'yyyy-MM-dd HH:mm:ss'),'[^0-9A-Za-z]',''),1,10)as INT) between 2000010100 and 2050010100
limit 1;
===================================================================================================================
Partitioning Table 
------------------
CREATE TABLE infosec_partial_tbl_test(
Message_priority_and_facility_indicator STRING, 
Intermediate_Forwarder_Date_Timestamp STRING, 
FTP_Hostname STRING,
FTP_SourceType STRING,
FTP_Source STRING,
Universal_Forwarder_Date_Time_Stamp STRING,
FTP_Session STRING,
FTP_Message STRING,
FTP_Process STRING) 
PARTITIONED BY (yr INT, mnt INT, day_ INT, time_ INT)
--------

INSERT INTO TABLE infosec_partial_tbl_temp_orc PARTITION(yr,mnt,day_,time_)

From infosec_temp s 
INSERT INTO TABLE infosec_partial_tbl_test PARTITION(yr,mnt,day_,time_)
select s.MISCELLANEOUS_1,
from_unixtime(unix_timestamp(substr(REGEXP_REPLACE(s.LAST_ACTIVITY_TIME_STAMP,'[\\[\\]]+',''),2,19),'dd-MM-yyyy.HH:mm:ss'),'yyyy-MM-dd HH:mm:ss') last_activity,
s.MISCELLANEOUS_2,
s.MISCELLANEOUS_3,
s.MISCELLANEOUS_4,
s.TIMESTAMP_2,
s.MISCELLANEOUS_5,
s.MISCELLANEOUS_6,
s.MISCELLANEOUS_7,
cast(substr(REGEXP_REPLACE(s.LAST_ACTIVITY_TIME_STAMP,'[\\[\\]]+',''),8,4) as int) as  yr,
cast(substr(REGEXP_REPLACE(s.LAST_ACTIVITY_TIME_STAMP,'[\\[\\]]+',''),2,2) as int) as  mnt,
cast(substr(REGEXP_REPLACE(s.LAST_ACTIVITY_TIME_STAMP,'[\\[\\]]+',''),5,2) as int) as  day_,
cast(substr(REGEXP_REPLACE(s.LAST_ACTIVITY_TIME_STAMP,'[\\[\\]]+',''),13,2) as int) as time_
where 
cast(substr(REGEXP_REPLACE(from_unixtime(unix_timestamp(substr(REGEXP_REPLACE(s.LAST_ACTIVITY_TIME_STAMP,'[\\[\\]]+',''),2,19),'dd-MM-yyyy.HH:mm:ss'),'yyyy-MM-dd HH:mm:ss'),'[^0-9A-Za-z]',''),1,10)as INT) between 2000010100 and 2050010100
 limit 1000;
===================================================================================================================
Immutable Insert 
Alter table 
-----------
ALTER TABLE emp_info1 SET TBLPROPERTIES ("immutable"="true");
hive> SHOW TBLPROPERTIES emp_info1;
OK
last_modified_by        dshmhtm
last_modified_time      1430814530
immutable       true
transient_lastDdlTime   1430814530
Time taken: 0.641 seconds, Fetched: 4 row(s)
===================================================================================================================
Hive Script to enable TEZ
-------------------------
set mapreduce.map.memory.mb= 4096;
set hive.tez.container.size=8192;
set hive.execution.engine=tez;
set mapreduce.map.java.opts=-Xmx1638m;
set hive.auto.convert.join.noconditionaltask.size=25000000;
set hive.tez.java.opts=-server -Xmx6554m -Djava.net.preferIPv4Stack=true -XX:NewRatio=8 -XX:+UseNUMA -XX:+UseParallelGC -XX:+PrintGCDetails -verbose:gc -XX:+PrintGCTimeStamps;
set mapreduce.map.output.compress=true;
set mapreduce.map.output.compress.codec=org.apache.hadoop.io.compress.SnappyCodec;
set hive.exec.compress.intermediate=true;
set hive.exec.compress.output=true;
set tez.runtime.intermediate-output.should-compress = true;
set tez.runtime.intermediate-input.is-compressed = true;
set tez.runtime.intermediate-output.compress.codec = "org.apache.hadoop.io.compress.SnappyCodec";
set tez.runtime.intermediate-input.compress.codec = "org.apache.hadoop.io.compress.SnappyCodec";
===================================================================================================================
Subquery Execution in hive
------------------
select t.timest,count(*) from (select cast(REGEXP_REPLACE(substr(REGEXP_REPLACE(LAST_ACTIVITY_TIME_STAMP,'[^a-zA-Z0-9]+',''),1,17),'(\\d{2})(\\d{2})(\\d{4})(\\d{2})(\\d{2})(\\d{2})(\\d{3})','$3-$1-$2 $4:$5:$6.$7') as timestamp) as timest from default.info_sec_tmp) t
 group by t.timest;
===================================================================================================================

To implement UDF 
----------------
export CLASSPATH=/usr/hdp/2.3.4.7-4/hadoop/conf:/usr/hdp/2.3.4.7-4/hadoop/lib/*:/usr/hdp/2.3.4.7-4/hadoop/.//*:/usr/hdp/2.3.4.7-4/hadoop-hdfs/./:/usr/hdp/2.3.4.7-4/hadoop-hdfs/lib/*:/usr/hdp/2.3.4.7-4/hadoop-hdfs/.//*:/usr/hdp/2.3.4.7-4/hadoop-yarn/lib/*:/usr/hdp/2.3.4.7-4/hadoop-yarn/.//*:/usr/hdp/2.3.4.7-4/hadoop-mapreduce/lib/*:/usr/hdp/2.3.4.7-4/hadoop-mapreduce/.//*::/usr/share/java/mysql-connector-java-5.1.17.jar:/usr/share/java/mysql-connector-java.jar:/usr/share/java/ojdbc6.jar:/usr/hdp/current/hadoop-mapreduce-client/*:/usr/hdp/2.3.4.7-4/tez/*:/usr/hdp/2.3.4.7-4/tez/lib/*:/usr/hdp/2.3.4.7-4/tez/conf

export CLASSPATH=$CLASSPATH:/usr/hdp/2.3.4.7-4/hive/lib/*:/usr/hdp/2.3.4.7-4/hive/conf:

add jar /users/home/xbblwv5/ToUpper.jar

create temporary function to_upper as 'ToUpper';

select to_upper(emp_name) from emp_info_xbblwv5;
OK
JOHN
BROAD
ALEX
KELLEN
KIWI
Time taken: 0.236 seconds, Fetched: 5 row(s)
------------------------------------------------------------------------------

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

public class ToUpper extends UDF {

    public Text evaluate(Text s) {
		Text to_value = new Text("");
		if (s != null) {
		    try {
				to_value.set(s.toString().toUpperCase());
		    } catch (Exception e) { // Should never happen

				to_value = new Text(s);
		    }
		}
		return to_value;
    }
}
==============================================================================
CASE STATEMENT
INSERT INTO TABLE infosecdb.InfoSec_hostname PARTITION(date_partition)
select A.hostname,A.last_time,count(A.hostname),A.last_time from  (
select CASE 
WHEN upper(substr(ftp_hostname,1,4))='RSBC' THEN 'RSBC'
WHEN upper(substr(ftp_hostname,1,4)) ='RSBA' THEN 'RSBA'
WHEN upper(substr(ftp_hostname,1,4)) ='RSBD' THEN 'RSBD'
WHEN upper(substr(ftp_hostname,1,4)) ='SNM4' THEN 'SNM4'
WHEN upper(substr(ftp_hostname,1,4)) ='SNM5' THEN 'SNM5'
WHEN upper(substr(ftp_hostname,1,4)) ='FTX' THEN 'FTX'
WHEN upper(substr(ftp_hostname,1,4)) ='FFTP' THEN 'FFTP'
WHEN upper(substr(ftp_hostname,1,4)) ='EDSA' THEN 'EDSA'
WHEN upper(substr(ftp_hostname,1,4)) ='R50A' THEN 'R50A'
WHEN upper(substr(ftp_hostname,1,4)) ='R50D' THEN 'R50D'
WHEN upper(substr(ftp_hostname,1,4)) ='R50E' THEN 'R50E'
WHEN upper(substr(ftp_hostname,1,4)) ='RS21' THEN 'RS21'
WHEN upper(substr(ftp_hostname,1,4)) ='RS22' THEN 'RS22'
WHEN upper(substr(ftp_hostname,1,4)) ='SPLU' THEN 'SPLU'
ELSE "OTHERS" END AS hostname,
to_date(last_activity_time_stamp) as last_time 
FROM infosecdb.infosec_interim_tbl
)A
GROUP BY 
A.hostname,
A.last_time
having 
A.last_time<=date_sub(to_date(from_unixtime(unix_timestamp())),1);

REGEXP_REPLACE
--------------
cast(substr(REGEXP_REPLACE(s.LAST_ACTIVITY_TIME_STAMP,'[\\[\\]]+',''),7,4) as int) as  info_year;
Database copy 
-------------
show tables;
------------------
Show Create table 
------------------
instruction
----------
Dump the table structure. - Run the shell script.
Move the structure from Dev client to UAT client 
Get the parent data with its structure.
Move it from dev client to UAT client 
Edit the table_structure with new database and path according to UAT 
create the directory inside tenant with database name as it looks like in Dev.
create the database with by specifying the new location. 
create the table with newly created table_structure.
Move the data to repective tenant. 
......................................
temp.sh
#!/bin/bash
     ls /grid1/amlhtp/op_tbl_list/ | while read file;
     do
	 sed -i 's/emp_info_xbblwv5/emp_info_xbblwv1/g' /users/home/xbblwv5/op_tbl_list/$file
	 sed -i 's/myclustertesttpc/myclustertestcnj/g' /users/home/xbblwv5/op_tbl_list/$file
     done

createtbl.sh 

#!/bin/bash
     ls /users/home/xbblwv5/op_tbl_list/ | while read file;
      do
	hive -f /users/home/xbblwv5/op_tbl_list/$file
     done
......................................
Data Copy to prod server 
------------------------
- Using Falcon. 
Have to change the falcon home dir /hadoop/falcon/

hadoop distcp hdfs://r38yn20.bnymellon.net:8020//tmp/xbblwv5/job.properties hdfs://r38ap10.bnymellon.net:8020/tmp/xbblwv5

hadoop distcp hdfs://r38yn20.bnymellon.net:8020//aduh0470/data/aduh0471/data/* hdfs://r38ap10.bnymellon.net:8020//adph0320/data/adph0321/data/

hadoop distcp hdfs://r38yn10.bnymellon.net:8020//aduh0470/hive/aduh0471/warehouse/amlhtu/* hdfs://r38ap10.bnymellon.net:8020//adph0320/hive/adph0321/warehouse/amlhtp/

......................................
#!/bin/bash
set -x
db=bwh_db 
hdfs_op=/tmp/xbblwv5/
hive -f /users/home/dshmhtm/sample/test/gettable_list.hql > /users/home/dshmhtm/sample/test/table_name.txt
if [ $? != 0 ];then
echo "not able to fetch data"
fi
if [ ! -f /users/home/dshmhtm/sample/test/table_name.txt ];then
echo "file not exixts"
fi
#hdfs dfs -mkdir /tmp/xbblwv5/$db/
cat /users/home/dshmhtm/sample/test/table_name.txt | while read line
do
#hdfs dfs -mkdir /tmp/xbblwv5/$db/$line
 -e "show create table $db.$line" > /users/home/dshmhtm/sample/test/op_tbl_list/$line
#hive -f temp.txt
#cat temp.txt >> mv_tbl_list.txt
done
.......
to copy 

hdfs dfs -get /tmp/xbblwv5/default/

rsync -av /users/home/dshmhtm/sample/test/op_tbl_list xbblwv5@r39dn00:/users/home/xbblwv5/

hdfs dfs -put infosec_hostname/ /tmp/xbblwv5/

import from '/tmp/xbblwv5/infosec_hostname/'

*****************************************************************************************************************************************************************
KAFKA-REF
---------
cd /usr/hdp/2.2.0.0-2041/kafka/bin

./usr/hdp/2.2.0.0-2041/kafka/bin/kafka-server-start.sh /usr/lib/kafka/config/server.properties

./usr/hdp/2.2.0.0-2041/kafka/bin/kafka-server-start.sh /etc/kafka/conf/server.properties


>Creating a topic.
/usr/hdp/2.2.0.0-2041/kafka/bin/kafka-topics.sh --create --zookeeper rue5n0c.bnymellon.net:2181 --replication-factor 1 --partitions 1 --topic premtest1
Created topic "premtest".

>Delete a Topic
/usr/hdp/2.2.0.0-2041/kafka/bin/kafka-topics.sh --zookeeper rue5n0c.bnymellon.net:2181 --delete --topic premtest1

>List a topic
/usr/hdp/2.2.0.0-2041/kafka/bin/kafka-topics.sh --list --zookeeper rue5n0c.bnymellon.net:2181

/usr/hdp/2.2.0.0-2041/kafka/bin/kafka-console-producer.sh --broker-list rue5n0c.bnymellon.net:6667 --topic premtest1

kafka-console-producer.sh --broker-list r37en00.bnymellon.net:9092 --topic premtest

/usr/hdp/2.2.0.0-2041/kafka/bin/kafka-console-consumer.sh --zookeeper rue5n0c.bnymellon.net:2181 --topic premtest1 --from-beginning
*****************************************************************************************************************************************************************
YARN-REF
========
Container Allocation 
--------------------
yarn.scheduler.maximum-allocation-mb
from abdelrahman shettia to Everyone:
SET mapred.output.compression.codec org.apache.hadoop.io.compress.SnappyCodec;
SET mapred.output.compression.type BLOCK;
from abdelrahman shettia to Everyone:
1. /etc/hadoop
from abdelrahman shettia to Everyone:
2. yarn logs -applicationId 
from abdelrahman shettia to Everyone:
3. Resource manager log 
from abdelrahman shettia to Everyone:
yarn logs -applicationId application_1427971414223_1746
from abdelrahman shettia to Everyone:
yarn logs -applicationId application_1427971414223_1746
from abdelrahman shettia to Everyone:
yarn logs -applicationId application_1427971414223_1746 > /tmp/app.log 2>&1 

yarn rmadmin -refreshQueues

hadoop queue -list

*****************************************************************************************************************************************************************
STORM-REF
=========
able to submit topology after making teh below changes in cluster 
===================================================================

* have created a .storm directory and storm.yaml file inside our home directory.

* have changed the owner permission of the below file to group wecdshht (b4 it was hadoop)

-r-sr-s--- 1 root wecdshht 56193 May 18 17:25 worker-launcher

* have changed storm.yaml locate in (/usr/hdp/2.2.6.0-2800/etc/storm/conf.dist/storm.yaml) -----done via ambari

removed unwanted  double quotes from  --- > worker.childopts: javaagent:"usr/hdp/current/storm-client/contrib/storm-jmxetric/lib/jmxetric-1.0.4.jar 
to   --- > worker.childopts: javaagent:usr/hdp/current/storm-client/contrib/storm-jmxetric/lib/jmxetric-1.0.4.jar 



+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
Note : 
Need to check the below:
Heap space are mentioned in /usr/hdp/current/storm-client/contrib/storm-jmxetric/conf/jmxetric-conf.xml

==========

chown root:wecdshht /usr/hdp/2.2.6.0-2800/storm/bin/worker-launcher
chmod 6550 /usr/hdp/2.2.6.0-2800/storm/bin/worker-launcher


+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

in r39jn00
have changed chown 
-r-sr-s--- 1 root hadoop 56193 May 18 17:25 worker-launcher
to 
-r-xr-x--- 1 xbblwv5 hadoop 56193 May 18 17:25 worker-launcher

from 
drwxr-xr-x 2 dshmstm wecdshht 4096 Sep 30 05:44 conf.dist
to 
root root 4096 Sep 30 05:44 conf.dist
--

--------
[root@r39kn00 ~]# chown root:dshmhtm /usr/hdp/2.2.6.0-2800/storm/bin/worker-launcher
[root@r39kn00 ~]# ls -ltr /usr/hdp/2.2.6.0-2800/storm/bin/worker-launcher
-r-xr-x--- 1 root dshmhtm 56193 May 18 17:25 /usr/hdp/2.2.6.0-2800/storm/bin/worker-launcher

-r-xr-x--- 1 root dshmhtm 56193 May 18 17:25 worker-launcher

chown root:root worker-launcher
chmod 555 worker-launcher

-r-xr-xr-x 1 root root 56193 May 18 17:25 worker-launcher
-------
dshmstm wecdshht 4096 Sep 30 05:45 conf.dist
>chown root:wecdshht /etc/storm/conf.dist
root wecdshht 4096 Sep 30 05:45 conf.dist

---------
chmod 557 /usr/hdp/2.2.6.0-2800/storm/bin/worker-launcher

------------------
:Error opening zip file or JAR manifest missing : "/usr/hdp/current/storm-client/contrib/storm-jmxetric/lib/jmxetric-1.0.4.jar
 Worker Process 01fff89d-202f-4d61-afee-d98b2671235b:Error occurred during initialization of VM
>> ls -ltr /usr/hdp/current/storm-client/contrib/storm-jmxetric/lib/jmxetric-1.0.4.jar
-rw-r--r-- 1 root root 14504 May 18 17:18 /usr/hdp/current/storm-client/contrib/storm-jmxetric/lib/jmxetric-1.0.4.jar
>>chmod 554 /usr/hdp/current/storm-client/contrib/storm-jmxetric/lib/jmxetric-1.0.4.jar
 -r-xr-xr-- 1 root wecdshht 14504 May 18 17:18 /usr/hdp/current/storm-client/contrib/storm-jmxetric/lib/jmxetric-1.0.4.jar

/usr/hdp/current/storm-client/contrib/storm-jmxetric/lib/jmxetric-1.0.4.jar

have tried the above solutions but no hope so trying the below sol suggested by mail arch



=============

 "/usr/hdp/2.2.6.0-2800/storm/bin/worker-launcher" "xbblwv5" "code-dir" "/diskdump/hadoop/storm/supervisor/stormdist/HDFS_TOPOLOGY-6-1444830628"


"/usr/hdp/2.2.6.0-2800/storm/bin/worker-launcher" "xbblwv5" "worker" "/diskdump/hadoop/storm/workers/3618cdb2-4c3d-49a3-8348-a471e79850d3" "/diskdump/hadoop/storm/workers/3618cdb2-4c3d-49a3-8348-a471e79850d3/storm-worker-script.sh"


 map.put("hdfs.keytab.file","/etc/security/keytabs/storm.service.keytab");
    map.put("hdfs.kerberos.principal","storm@HADOOP-DQA-CNJ01.BNYMELLON.NET");

	
	============================
	Details asked by HWX team 
	$ id
uid=49639(xbblwv5) gid=9553(yecbdc) groups=9553(yecbdc),8811(ahd0pend),9729(adshadmt)

$ grep xbblwv5 /etc/group
ahd0pend::8811:xbblqy9,xbbl1z5,xeccwmg,xbbllcw,xeccv34,xbblwv5,xbbkgcq,xbbjs26,xbbl0tp,xbblwp9,xbblyfh,xbblnw7,xbbl1wk,xbbkf4u
yecbdc::9553:xbblv2e,xeccwmg,xbblh8u,xbbl172,xbbl0tp,xbbkblq,xeccv34,xbbj3xl,xbblwv5,xeccv25,xbblv7z,xbblwyr,xbblt14,xbblp85,xeccdp7,xeccbv9,xbbl1wk,xbbkf4u,xbbl2ya
adshadmt::9729:xecc08d,xbbl2ya,xbbltb5,xbblqy9,xbblwv5,xecc15c,xbblh8u,dshmftm,xbblyfh,xbblwp9,xeccv34,xbbkgcq,xbbjs26,xbbllcw,xeccwmg,xbbln9d,xdkbg5u,xbbkkgr,xjfb3gf,xbbl0tp,xbbl1wk,xbbkf4u,xbbl1z5,xbblv4c,xbblyp1,xbblz5m,xbblz99,xbblzp7,xbbl172

$ grep wecdshht /etc/group
hadoop::498:hdfs,mapred,hive,pig,hcat,hbase,zookeeper,oozie,ambari-qa,ambari,hue,flume,tez,storm,accumulo
whadoop::10187:yarn

$ ls -lrt /usr/hdp/2.2.6.0-2800/storm/bin/worker-launcher
-r-sr-sr-x 1 root hadoop 56193 May 18 17:25 /usr/hdp/2.2.6.0-2800/storm/bin/worker-launcher

$ ls -lrt /etc/storm/conf/worker-launcher.cfg
-rw-r--r-- 1 root wecdshht 52 Aug 23 08:03 /etc/storm/conf/worker-launcher.cfg

$ ls -lrt /usr/hdp/2.2.6.0-2800/storm/bin/
total 124
-rw-r--r-- 1 root root    1831 May 18 17:18 storm-slider
-rw-r--r-- 1 root root    8779 May 18 17:18 storm.cmd
-rwxr-xr-x 1 root root   12752 May 18 17:18 storm-slider.py
-rw-r--r-- 1 root root   16666 May 18 17:18 storm.py
-rwxr-xr-x 1 root root    1701 May 18 17:18 storm.distro
-rw-r--r-- 1 root root    1589 May 18 17:18 storm-config.sh
-rw-r--r-- 1 root root    3332 May 18 17:18 storm-config.cmd
-r-sr-sr-x 1 root hadoop 56193 May 18 17:25 worker-launcher
-rwxr-xr-x 1 root root     515 May 18 17:26 storm

$ ls -lrt /etc/storm/conf/
total 60
-rw-r--r-- 1 root    wecdshht 4549 Jul 20 06:06 storm.yaml.rpmsave
-rw-r--r-- 1 dshmstm wecdshht  165 Aug 21 17:08 storm-env.sh
-rw-r--r-- 1 dshmstm wecdshht  792 Aug 23 08:03 storm_jaas.conf
-rw-r--r-- 1 dshmstm wecdshht  142 Aug 23 08:03 client_jaas.conf
-rw-r--r-- 1 root    wecdshht   52 Aug 23 08:03 worker-launcher.cfg
-rw-r--r-- 1 root    wecdshht   69 Aug 25 12:27 ranger-security.xml
-rwxr--r-- 1 root    wecdshht 2269 Aug 25 12:27 xasecure-policymgr-ssl.xml
-rwxr--r-- 1 root    wecdshht 3170 Aug 25 12:27 xasecure-storm-security.xml
-rw-r--r-- 1 dshmstm wecdshht 1226 Sep  9 06:55 config.yaml
-rw-r--r-- 1 dshmstm wecdshht   83 Sep  9 06:55 storm-metrics2.properties
lrwxrwxrwx 1 root    root       30 Sep 11 03:47 hdfs-site.xml -> /etc/hadoop/conf/hdfs-site.xml
-rw-r--r-- 1 dshmstm wecdshht 7652 Sep 16 23:49 storm.yaml
-rwxr--r-- 1 root    wecdshht 5078 Sep 30 05:44 xasecure-audit.xml

$ cat /etc/storm/conf/worker-launcher.cfg
storm.worker-launcher.group=wecdshht
min.user.id=500$
==============================================================================================================================
To Submit a Topology In Cluster 
-------------------------------
StormSubmitter.submitTopologyWithProgressBar(args[0], conf, builder.createTopology());

 LocalCluster cluster = new LocalCluster();
   cluster.submitTopology("HDFS_TOPOLOGY", config, builder.createTopology());
===========================================================================================================================
Code Ref 
---------
Steps that has been recomended By Hadoop Admin team.
-------------------------------------------------------------------------------------------
Prerequest
----------
export JAVA_HOME=/usr/jdk64/jdk1.7.0_45
export PATH=$PATH:/usr/lib64/qt-3.3/bin:/usr/local/bin:/bin:/usr/bin:/usr/local/sbin:/usr/sbin:/sbin:/usr/jdk64/jdk1.7.0_45/bin:/usr/lib/hadoop/bin:/usr/lib/hive/bin:/usr/lib/pig/bin:/usr/local/apache-maven-3.2.5/apache-maven-3.2.5/bin:/usr/hdp/2.2.6.0-2800/storm/bin

export PATH=$PATH:/usr/jdk64/jdk1.7.0_45/bin

Creating a maven project
------------------------
mvn archetype:generate -DgroupId=com.hortonworks.pso -DartifactId=CLASS_NAME(Storm_Num) -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false

It will create a directory with the class name given(Storm_Num)

Copy the java code into the below path of ./Storm_Num

/src/main/java/com/hortonworks/pso

Copy the core-site.xml and hdfs-site.xml to the below path 

/src/main/resources

there will a pom.xml file created in present directory. replace the pom.xml with the code given below. 

do mvn clean install to build the code. 

Which will create a directory named /target in present directory

do cd to target and run the jar created by mvn 

storm jar Storm_Num-1.0-SNAPSHOT.jar Storm_Num -c nimbus.host=r39bn00.bnymellon.net

--------------------------------------------------------------------------------------------------
//Java Code.
---------
import backtype.storm.StormSubmitter;
import org.apache.storm.hdfs.bolt.format.DefaultFileNameFormat;
import org.apache.storm.hdfs.bolt.format.DelimitedRecordFormat;
import org.apache.storm.hdfs.bolt.format.FileNameFormat;
import org.apache.storm.hdfs.bolt.format.RecordFormat;
import org.apache.storm.hdfs.bolt.rotation.FileRotationPolicy;
import org.apache.storm.hdfs.bolt.rotation.FileSizeRotationPolicy;
import org.apache.storm.hdfs.bolt.rotation.FileSizeRotationPolicy.Units;
import org.apache.storm.hdfs.bolt.HdfsBolt;
import org.apache.storm.hdfs.bolt.AbstractHdfsBolt;
import org.apache.storm.hdfs.bolt.sync.CountSyncPolicy;
import org.apache.storm.hdfs.bolt.sync.SyncPolicy;
import org.apache.storm.hdfs.common.rotation.MoveFileAction;
import org.yaml.snakeyaml.Yaml;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import java.util.Map;
import backtype.storm.task.OutputCollector;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import backtype.storm.utils.Utils;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.ArrayList;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Storm_Num{
private static int currentNumber = 1;
private static class NumberSpout extends BaseRichSpout
{
    private SpoutOutputCollector collector;

    @Override
    public void open( Map conf, TopologyContext context, SpoutOutputCollector collector )
    {
        this.collector = collector;
    }

    @Override
    public void nextTuple()
    {
        collector.emit( new Values( new Integer( currentNumber++ ) ) );
    }

    @Override
    public void ack(Object id)
    {
    }

    @Override
    public void fail(Object id)
    {
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer)
    {
        declarer.declare( new Fields( "number" ) );
    }
}

private static class PrimeNumberBolt extends BaseRichBolt
{
    private OutputCollector collector;

    public void prepare( Map conf, TopologyContext context, OutputCollector collector )
    {
        this.collector = collector;
    }

    public void execute( Tuple tuple )
    {
        int number = tuple.getInteger( 0 );
        if( isPrime( number) )
        {
                System.out.println(number);
                                collector.emit(new Values(number ));

        }
        collector.ack( tuple );
    }

    public void declareOutputFields( OutputFieldsDeclarer declarer )
    {
        declarer.declare( new Fields( "number" ) );
    }

    private boolean isPrime( int n )
    {
        if( n == 1 || n == 2 || n == 3 )
        {
            return true;
        }
        if( n % 2 == 0 )
        {
            return false;
        }

        for( int i=3; i*i<=n; i+=2 )
        {
            if( n % i == 0)
            {
                return false;
            }
        }
        return true;
    }
}
private static class HdfsBolT1 extends BaseRichBolt
{
    private OutputCollector collector;
    private List<Integer> prime_num;

    @SuppressWarnings("rawtypes")
    public void prepare(
        Map stormConf,
        TopologyContext context,
        OutputCollector collector)
    {
        this.collector = collector;
        }

    public void execute(Tuple input)
    {
        System.out.println("in HDFS BOLT");
        int number = input.getInteger( 0 );
        this.prime_num.add(number);
                if(this.prime_num.size()>=500)
                {
                writeToHDFS();
                this.prime_num = new ArrayList<Integer>(500);
                }
    }

    private void writeToHDFS()
    {
        FileSystem hdfs = null;
        Path file = null;
        OutputStream os = null;
        BufferedWriter wd = null;
        try
        {
            Configuration conf = new Configuration();
            conf.addResource(new Path("/etc/hadoop/conf/core-site.xml"));
            conf.addResource(new Path("/etc/hadoop/conf/hdfs-site.xml"));
            hdfs = FileSystem.get(conf);
            file = new Path("/tmp/storm_output.txt");
            if (hdfs.exists(file))
                os = hdfs.append(file);
            else
                os = hdfs.create(file);
            wd = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            for (Integer prime_no : prime_num)
            {
                wd.write(prime_no);
            }
        }
        catch (IOException ex)
        {
            System.out.println(ex);
        }
        finally
        {
            try
            {
                if (os != null) os.close();
                if (wd != null) wd.close();
                if (hdfs != null) hdfs.close();
            }
            catch (IOException ex)
            {

                System.out.println(ex);
            }
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) { }
}
public static void main(String[] args)
{
    RecordFormat format = new DelimitedRecordFormat().withFieldDelimiter("|");
    /*Synchronize data buffer with the filesystem every 1000 tuples*/
    SyncPolicy syncPolicy = new CountSyncPolicy(1000);
    /* Rotate data files when they reach five MB*/
    FileRotationPolicy rotationPolicy = new FileSizeRotationPolicy(5.0f, Units.MB);
    /* Use default, Storm-generated file names*/
    FileNameFormat fileNameFormat = new DefaultFileNameFormat().withPath("/tmp/infosec/");

    HdfsBolt bolt = new HdfsBolt()
                .withFsUrl("hdfs://myclustertestcnj")
                .withFileNameFormat(fileNameFormat)
                .withRecordFormat(format)
                .withRotationPolicy(rotationPolicy)
                .withSyncPolicy(syncPolicy)
                .withConfigKey("hdfs.config")
                .addRotationAction(new MoveFileAction().toDestination("/tmp/storm_output/"));

   Map<String, Object> map = new HashMap<String,Object>();
   map.put("hdfs.keytab.file","/users/home/xbblwv5/xbblwv5.keytab");
   map.put("hdfs.kerberos.principal","xbblwv5@HADOOP-DQA-CNJ01.BNYMELLON.NET");

   Config config = new Config();
   config.put("hdfs.config", map);

   TopologyBuilder builder = new TopologyBuilder();
   builder.setSpout( "spout", new NumberSpout() );
   builder.setBolt( "prime", new PrimeNumberBolt()).shuffleGrouping("spout");
   builder.setBolt( "hdfs_bolt", bolt).shuffleGrouping("prime");

   LocalCluster cluster = new LocalCluster();
   cluster.submitTopology("HDFS_TOPOLOGY", config, builder.createTopology());
   Utils.sleep(10000);
   cluster.killTopology("HDFS_TOPOLOGY");
   cluster.shutdown();

    }
}

===========================================================================

pom.xml
-------

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>com.hortonworks.pso</groupId>
  <artifactId>Storm_Num</artifactId>
  <packaging>jar</packaging>
  <version>1.0-SNAPSHOT</version>
  <name>HWX :: PSO Data Generator</name>
  <url>http://www.hortonworks.com</url>

  <properties>
    <hdp.version>2.6.0</hdp.version>
  </properties>

  <repositories>
    <repository>
     <releases>
      <enabled>true</enabled>
      <updatePolicy>always</updatePolicy>
      <checksumPolicy>warn</checksumPolicy>
    </releases>
    <snapshots>
      <enabled>false</enabled>
      <updatePolicy>never</updatePolicy>
      <checksumPolicy>fail</checksumPolicy>
    </snapshots>
    <id>HDPReleases</id>
    <name>HDP Releases</name>
    <url>http://repo.hortonworks.com/content/repositories/releases/</url>
    <layout>default</layout>
  </repository>
  <repository>
    <id>clojars.org</id>
    <url>http://clojars.org/repo</url>
  </repository>
</repositories>

<build>
  <plugins>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-shade-plugin</artifactId>
      <version>1.4</version>
      <configuration>
        <createDependencyReducedPom>true</createDependencyReducedPom>
      </configuration>
      <executions>
        <execution>
          <phase>package</phase>
          <goals>
            <goal>shade</goal>
          </goals>
          <configuration>
            <transformers>
              <transformer
                implementation="org.apache.maven.plugins.shade.resource.ServicesResourceTransformer"/>
                <transformer
                  implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                  <mainClass></mainClass>
                </transformer>
              </transformers>
              <filters>
                <filter>
                  <artifact>*:*</artifact>
                  <excludes>
                    <exclude>META-INF/*.SF</exclude>
                    <exclude>META-INF/*.DSA</exclude>
                    <exclude>META-INF/*.RSA</exclude>
                  </excludes>
                </filter>
              </filters>
            </configuration>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>



  <dependencies>
    <dependency>
      <groupId>org.apache.storm</groupId>
      <artifactId>storm-hdfs</artifactId>
      <version>0.9.3</version>
      <type>jar</type>
      <exclusions>
        <exclusion>
          <groupId>log4j</groupId>
          <artifactId>log4j</artifactId>
        </exclusion>
        <exclusion>
          <groupId>org.slf4j</groupId>
          <artifactId>slf4j-log4j12</artifactId>
        </exclusion>
      </exclusions>
    </dependency>
    <dependency>
      <groupId>org.apache.storm</groupId>
      <artifactId>storm-hdfs</artifactId>
      <version>0.9.3</version>
      <exclusions>
        <exclusion>
          <groupId>log4j</groupId>
          <artifactId>log4j</artifactId>
        </exclusion>
        <exclusion>
          <groupId>org.slf4j</groupId>
          <artifactId>slf4j-log4j12</artifactId>
        </exclusion>
      </exclusions>
    </dependency>
    <dependency>
      <groupId>org.apache.storm</groupId>
      <artifactId>storm-hbase</artifactId>
      <version>0.9.3</version>
      <type>jar</type>
    </dependency>
    <dependency>
      <groupId>org.apache.storm</groupId>
      <artifactId>storm-hbase</artifactId>
      <version>0.9.3</version>
      <exclusions>
        <exclusion>
          <groupId>log4j</groupId>
          <artifactId>log4j</artifactId>
        </exclusion>
        <exclusion>
          <groupId>org.slf4j</groupId>
          <artifactId>slf4j-log4j12</artifactId>
        </exclusion>
      </exclusions>
    </dependency>
    <dependency>
      <groupId>org.apache.hadoop</groupId>
      <artifactId>hadoop-hdfs</artifactId>
      <version>2.6.0</version>
      <type>jar</type>
      <exclusions>
        <exclusion>
          <groupId>log4j</groupId>
          <artifactId>log4j</artifactId>
        </exclusion>
        <exclusion>
          <groupId>org.slf4j</groupId>
          <artifactId>slf4j-log4j12</artifactId>
        </exclusion>
      </exclusions>
    </dependency>
    <dependency>
      <groupId>org.apache.hadoop</groupId>
      <artifactId>hadoop-common</artifactId>
      <version>2.6.0</version>
      <type>jar</type>
      <exclusions>
        <exclusion>
          <groupId>log4j</groupId>
          <artifactId>log4j</artifactId>
        </exclusion>
        <exclusion>
          <groupId>org.slf4j</groupId>
          <artifactId>slf4j-log4j12</artifactId>
        </exclusion>
      </exclusions>
    </dependency>
    <dependency>
      <groupId>org.apache.storm</groupId>
      <artifactId>storm-core</artifactId>
      <version>0.9.3</version>
      <type>jar</type>
      <scope>provided</scope>
    </dependency>
    <dependency>
      <groupId>org.anarres.lzo</groupId>
      <artifactId>lzo-hadoop</artifactId>
      <version>1.0.0</version>
      <exclusions>
        <exclusion>
          <groupId>log4j</groupId>
          <artifactId>log4j</artifactId>
        </exclusion>
        <exclusion>
          <groupId>org.slf4j</groupId>
          <artifactId>slf4j-log4j12</artifactId>
        </exclusion>
      </exclusions>
    </dependency>
  </dependencies>

</project>
=================================================================================================================================


   
*****************************************************************************************************************************************************************
SPARK-REF
=========

spark written in  scala 
hdfs friendly 
unified toolset(like apple)
pipeline oriented


yarn(or standalone scheduler)
rdd (transformation and action)
sc.textFile(hdfs://path)
transformation -- filter,map, flatmap,distinct and sample
multi-RDD transformation - union, intersection,suntract,cartesian 
pair RDD transformation -- my.RDD.group-by
Pair RDD transformation -- reduceByKey, groupbyKey,keys, value 
Laziness concept --> like pig
RDD - Actions --> count - myRDD.count(), collect,reduce, take,countByvalue, 
PairRDD -- countByKey
Persistence -- recompute a RDD , reason y spark is faster*, important ***
lineage concept-- data flow in spark - main shuffled rdd - for efficency 
accumulator - like counter, for debug
spark worker nodes - wont communicate with each other
*statscounter -
*dataframes - API - datastructure in spark like sql 


sparkConf
conf = new SparkConf()
conf.set("sparkexecutor,memory", "1g")
sc = SparkContext(conf)

MLLib - like R programming 
Native ML framework-->classification, Regresssion, clustering, correlation 

Spark SQL
Graphx- hive level programming 
native graph proceessing framwork
similar to pregal,giraphm graphlab(twitter analysis, pagerank)

Spark streaming - real time analysis like strom 
shopping cart suggestion (flipcart and amazon)
checkpoint
ssc- streaming spark context

join and union in spark 
joins use k/v data
union not specifying k/v
default port is 4040






spark user 
dshm00m

---------------------------- hadoop docu --- bny ---


Please use the edge/client nodes r37cn00/r37dn00 r39cn00/r39dn00 r38bp00/r38cp00 r36bp00/r36pp00
 
 
For webhdfs you can use myclustertesttpc , myclusterprodcnj , myclustertestcnj , myclusterprodtpc from the client nodes




E.g: hdfs dfs -ls webhdfs://myclustertesttpc/addh0000.
 
 
FOR REST you could try the namenode host directly
E.g: curl -i --negotiate -u : http://r36yn20.bnymellon.net:50070/webhdfs/v1/?op=GETFILESTATUS


=============================spark started ===========
spark sql
!
import org.apache.spark.streaming._!
import org.apache.spark.streaming.StreamingContext._!
!
// create a StreamingContext with a SparkConf configuration!
val ssc = new StreamingContext(sparkConf, Seconds(10))!
!
// create a DStream that will connect to serverIP:serverPort!
val lines = ssc.socketTextStream(serverIP, serverPort)!
!
// split each line into words!
val words = lines.flatMap(_.split(" "))!
!
// count each word in each batch!
val pairs = words.map(word => (word, 1))!
val wordCounts = pairs.reduceByKey(_ + _)!
!
// print a few of the counts to the console!
wordCounts.print()!
!
ssc.start() // start the computation!
ssc.awaitTermination() // wait for the computation to terminate

--------------------------
spark streaming 

// http://spark.apache.org/docs/latest/streaming-programming-guide.html!
!
import org.apache.spark.streaming._!
import org.apache.spark.streaming.StreamingContext._!
!
// create a StreamingContext with a SparkConf configuration!
val ssc = new StreamingContext(sparkConf, Seconds(10))!
!
// create a DStream that will connect to serverIP:serverPort!
val lines = ssc.socketTextStream(serverIP, serverPort)!
!
// split each line into words!
val words = lines.flatMap(_.split(" "))!
!
// count each word in each batch!
val pairs = words.map(word => (word, 1))!
val wordCounts = pairs.reduceByKey(_ + _)!
!
// print a few of the counts to the console!
wordCounts.print()!
!
ssc.start() // start the computation!
ssc.awaitTermination() // wait for the computation to terminate

----------------spark Mllib

MLI: An API for Distributed Machine Learning
Evan Sparks, Ameet Talwalkar, et al.
International Conference on Data Mining (2013)
http://arxiv.org/abs/1310.5426
Unifying the Pieces: MLlib
// http://spark.apache.org/docs/latest/mllib-guide.html!
!
val train_data = // RDD of Vector!
val model = KMeans.train(train_data, k=10)!
!
// evaluate the model!
val test_data = // RDD of Vector!
test_data.map(t => model.predict(t)).collect().foreach(println)!
-------------------------------------------------------Spark graph ---


// http://spark.apache.org/docs/latest/graphx-programming-guide.html!
!
import org.apache.spark.graphx._!
import org.apache.spark.rdd.RDD!
!
case class Peep(name: String, age: Int)!
!
val vertexArray = Array(!
(1L, Peep("Kim", 23)), (2L, Peep("Pat", 31)),!
(3L, Peep("Chris", 52)), (4L, Peep("Kelly", 39)),!
(5L, Peep("Leslie", 45))!
)!
val edgeArray = Array(!
Edge(2L, 1L, 7), Edge(2L, 4L, 2),!
Edge(3L, 2L, 4), Edge(3L, 5L, 3),!
Edge(4L, 1L, 1), Edge(5L, 3L, 9)!
)!
!
val vertexRDD: RDD[(Long, Peep)] = sc.parallelize(vertexArray)!
val edgeRDD: RDD[Edge[Int]] = sc.parallelize(edgeArray)!
val g: Graph[Peep, Int] = Graph(vertexRDD, edgeRDD)!
!
val results = g.triplets.filter(t => t.attr > 7)!
!
for (triplet <- results.collect) {!
println(s"${triplet.srcAttr.name} loves ${triplet.dstAttr.name}")!
}
Unifying the Pieces: GraphX
-------------------------------------------------------------------------------
spark login 

1.login to hdfs
2.cd /usr/hdp/current/spark-client/
3.Run the Spark shell:
 ./bin/spark-shell --master yarn-client --driver-memory 512m -- executor-memory 512m
 
 ./bin/spark-shell --master yarn-client --driver-memory 512m -- executor-memory 512m
 
 
 ------------------------------
 
 Resilient Distributed Datasets
RDDs provide an interface for coarse-grained
transformations (map, group-by, join, …)
Efficient fault recovery using lineage
» Log one operation to apply to many elements
» Recompute lost partitions of RDD on failure
» No cost if nothing fails
Rich enough to capture many models:
» Data flow models: MapReduce, Dryad, SQL, …
» Specialized models for iterative apps: Pregel, Hama, 


https://databricks-training.s3.amazonaws.com/index.html  -- spark training  
http://spark.apache.org/docs/latest/sql-programming-guide.html -- 
====================spark web UI

http://r39cn00.bnymellon.net:4043  --- it will start once  you spark sessions 


val sqlContext = new org.apache.spark.sql.hive.HiveContext(sc)
val xat = sqlContext.sql("select activity_name,action_type_code , count(*) from dp_xat.t_dp_payload_v2_1_0_0 group by activity_name,action_type_code limit 10").collect().foreach(println)


val xat = sqlContext.sql("select service_name,account_number , count(*) from dp_xat.t_dp_payload_v2_1_0_0 group by service_name,account_number limit 10").collect().foreach(println)


val yat = sqlContext.sql("select from_unixtime(CAST(timestamp/1000 as BIGINT), 'yyyy-MM-dd'),application_mnemonic ,count(*) from dp_xat.t_dp_payload_v2_1_0_0 group by timestamp,application_mnemonic limit 10").collect().foreach(println)


val yarnclus = sqlContext.sql("select from_unixtime(CAST(timestamp/1000 as BIGINT), 'yyyy-MM-dd'),application_mnemonic ,count(*) from dp_xat.t_dp_payload_v2_1_0_0 group by timestamp,application_mnemonic limit 10").collect().foreach(println)


SELECT count(*) service_name from t_dp_payload_v2_1_0_0 FULL OUTER JOIN t_dp_payload_v2_1_0_0_stage ON t_dp_payload_v2_1_0_0.service_name=t_dp_payload_v2_1_0_0_stage.account_number;
FROM table1
FULL OUTER JOIN table2
ON table1.column_name=table2.column_name;


PTA --r27dn0v -- staging server 

/db/staging/sourcefiles/global_ops/input
 4pm we get data daily est 
 
 /db/staging/sourcefiles/global_ops/input/pta -- scripts 
 
 vertica -- r71an10
 staging server = r27dn0v
gdpm02m on Vertica box
gdpm02m on Vertica box


=======================

scala> val aaa = sqlContext.sql("select from_unixtime(CAST(timestamp/1000 as BIGINT), 'yyyy-MM-dd'),application_mnemonic ,count(*) from dp_xat.t_dp_payload_v2_1_0_0 group by timestamp,application_mnemonic limit 10").collect().foreach(println)


spark-shell --driver-memory 1G --executor-memory 1G --executor-cores 3 --num-executors 3 

 - SPARK_EXECUTOR_MEMORY, Memory per Worker (e.g. 1000M, 2G) (Default: 1G)
 - SPARK_DRIVER_MEMORY, Memory for Master (e.g. 1000M, 2G) (Default: 512 Mb)
 
 http://stackoverflow.com/questions/27181737/how-to-deal-with-executor-memory-and-driver-memory-in-spark

 /usr/hdp/current/spark-client/bin/incubator-zeppelin
 
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!spark notes !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!



 rebuilt if a partition is lost."

  * A cool thing about RDD's is how they respond to failure (i.e. a cluster of the node dies).  The solution is a technique they refer to as lineage.  Another definition from the Spark paper:
  * "The elements of an RDD need not exist in physical storage; instead, a handle to an RDD contains enough information to compute it starting from data in reliable storage. This means that they can always be reconstructed if nodes fail."
  * "RDDs achieve fault tolerance through a notion of lineage: if a partition of an RDD is lost, it has enough information about how it was derived from other RDDs to be able to rebuild just that partition"

  * RDD's are implemented as an iterable stream which makes it easy to plugin to existing language collections API's

* The Spark Scala API allows you to implement complex dataflows that use the standard Scala collections API functions like map, reduce, groupBy, filter, etc.

* After each step in the dataflow the intermediate data is stored across a distributed memory store

* You can launch Spark in a modified Scala REPL to experiment with your dataflow in the same way you experiment with your Scala application.
* Spark is the first system to allow an efficient, general purpose programming language to be used interactively to process large data sets on the cluster.

* (REPL image) There's an actual scala prompt at the bottom of this window, but I wanted to show the cool ASCII art!
* Also.. latest version is 1.3.0!
---
class: large-points
.left-column[
## What is spark?
## Isn't this MapReduce?
]
.right-column[
###How is Spark any better than MapReduce?

  * No need to flush data between steps which makes **iterative jobs faster**

  * Cache large datasets in distributed memory to branch output operations and perform **interactive analysis**

  * An **intuitive API** that improves code readability and reduces typical MapReduce ceremony

```scala
val file = spark.textFile("hdfs://...")
val counts = file.flatMap(line => line.split(" "))
                 .map(word => (word, 1))
                 .reduceByKey(_ + _)
counts.saveAsTextFile("hdfs://...") 
```
]
???
* Spark is different from standard MapReduce in a few ways.

* Iterative jobs. 
  * Again, from the Berkley paper:
  * "Many common machine learning algorithms apply a function repeatedly to the same dataset to optimize a parameter"
  * To implement these algorithms with Hadoop you're forced to dump to disk and then re-read your data between reduce steps.  Spark keeps this data in memory, which obviously has an enormous performance increase if your dataflow contains many steps.

* Interactive analysis.
  * The Berkley paper states
  * "Hadoop is often used to perform ad-hoc exploratory queries on big datasets, through SQL interfaces such as Pig and Hive. Ideally,
a user would be able to load a dataset of interest into memory across a number of machines and query it repeatedly. However, with Hadoop, each query incurs significant latency (tens of seconds) because it runs as a separate MapReduce job and reads data from disk"
  * Spark allows you to temporarily persist, or 'cache', a transformation or reduction to memory to use it later in your application.
  * This is useful because often times you want to use a single intermediate representation of your data and branch out to generate several different results

* The API for Scala allows you to write a lot less code than is typical on the Hadoop map and reduce framework.
  * MapReduce Data flows are usually more involved than a single map and reduce step
  * So Instead of trying to rework your algorithm to work with the MapReduce framework, Spark plugs into Scala's Collection API to make the implementation easier to write, read, and reason about.
---
# Driver Application
* The SparkContext contains information used to run your driver app

  * Configured by env vars, spark-defaults.xml, CLI `spark-shell` & `spark-submit`

  * Singleton

  * Testing.  Change `master` by configuration.

* Data sources can be in memory collections, local filesystem, HDFS, HTTP
<div style="text-align:center">
<img src="hdfs-architecture.gif" alt="HDFS Architecture" style="width: 400px;"/>
</div>
???
* The SparkContext is the heart of the Spark implementation in your driver application.
  * It contains all the information required for your driver application to run.
  * You can configure the SparkContext with environment variables, the spark-defaults.xml, and through CLI with Spark helper scripts `spark-shell` & `spark-submit`
  * The SparkContext is a singleton.  So you can only have one SparkContext per JVM process.
  * Testing is fairly straightforward with Spark because it's easy to change your `master` Resource Manager via configuration.
    * For example in production you could be using a Hadoop YARN cluster, but for tests you can use a single core in the same process
    * Tests must be run in serial because of the one SparkContext per JVM rule

* Data sources the SparkContext supplies
  * The SparkContext Spark operates on a number of familiar data sources
  * Local filesystem, HDFS, HTTP
  * It's common to utilize HDFS for Spark application I/O
  * There's also a spark streaming project which allows you to stream data from a number of different sources.

---
# SampleApp.scala
```scala
/* SimpleApp.scala */
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

object SimpleApp {
  def main(args: Array[String]) {
    val logFile = "YOUR_SPARK_HOME/README.md" // Should be some file on your system
    val conf = new SparkConf().setAppName("Simple Application")
    val sc = new SparkContext(conf)
    val logData = sc.textFile(logFile, 2).cache()
    val numAs = logData.filter(line => line.contains("a")).count()
    val numBs = logData.filter(line => line.contains("b")).count()
    println("Lines with a: %s, Lines with b: %s".format(numAs, numBs))
  }
}
```
https://spark.apache.org/docs/latest/quick-start.html
???
* Sample Application straight from the Spark documentation quick start guide 
* What's in this example?
  * Instantiate a SparkContext
  * Load in the Spark README markdown file as sample data from the filesystem
  * `Suggest` that Spark `cache` the dataset in cluster memory so multiple operations can be performed on it.
  * Count the number of lines that contain a's and lines that contain b's
---
# Stack Exchange dataset 
<img src="stackexchange.png" alt="StackExchange" style="width:200px;"/>

*"All community-contributed content on Stack Exchange is licensed under the Creative Commons BY-SA 3.0 license. As part of our commitment to that, we release a quarterly dump of all user-contributed data (after carefully sanitizing it to protect user private data, of course)."*
http://blog.stackexchange.com/category/cc-wiki-dump/

StackOverflow.com data:

```bash
$ du stackoverflow.com*/*.xml --total --block-size=G
1G      stackoverflow.com-Badges/Badges.xml
8G      stackoverflow.com-Comments/Comments.xml
46G     stackoverflow.com-PostHistory/PostHistory.xml
1G      stackoverflow.com-PostLinks/PostLinks.xml
29G     stackoverflow.com-Posts/Posts.xml
1G      stackoverflow.com-Tags/Tags.xml
1G      stackoverflow.com-Users/Users.xml
7G      stackoverflow.com-Votes/Votes.xml
90G     total
``` 
???
* The examples I'm going to show you tonight are based StackOverflow data

* Every quarter, the Stack Exchange network releases all of its data for all of its sites under a Creative Commons license
* The dataset contains all Stack Exchange site posts, comments, badges, links, history, votes, users, and tags.
* It's scrubbed of all personally identifying information
* The dataset is available as a BitTorrent from the Internet Archive, it's about 21 GB compressed
* It's serialized as XML with one record per line

* For my demo I will be working exclusively with StackOverflow.com data which comes out to 90GB of uncompressed XML data
  * Of that I will only be working with the Posts.xml data which comes out to a paltry 29GB
  * (but it's just XML so take that with a grain of salt)
* Not a huge dataset, but for my limited computational resources it does the trick!
---
class: large-points
# ScalaTagCount demo in Standalone mode

## hulk

* Intel® Core™ i7-2600K CPU @ 3.40GHz × 8

* 16GB RAM

* ATA OCZ-VERTEX3 SSD

<img src="hulk.jpg" alt="Hulk" style="width:200px"/ >
???
* This is the first example I'll be running for you.
* It's a Spark driver application running on a single system I have called hulk.

* Although Spark has lots of cluster support, you can just as easily run it in on a single system utilizing all cores and memory you have available.
* You can run Spark Applications with a single library download and no other infrastructure to setup
* This makes it easy to develop and troubleshoot Spark Applications in isolation which makes it easier to learn.

* Count all distinct tags that are associated with StackOverflow questions that are tagged with `Scala`
* *Show StackAnalysis.scala and describe each step of data flow

* *Run StackAnalysis.scala* and show some of the output.. come back to results at end of presentation
---
class: large-points
# Spark on the cluster

## Spark supports lots of cluster connectivity options

* Spark Standalone <img src="spark-logo.png"  style="height: 30px;"/>
* Hadoop YARN <img src="hadoop-logo.png"  style="height: 30px;"/>
* Mesos <img src="mesos-logo.png" style="height: 30px;" />

<div style="text-align:center">
<img src="spark-cluster-overview.png" alt="Spark Cluster Overview" />
http://spark.apache.org/docs/latest/running-on-mesos.html
</div> 
???
* Spark was designed to run in a clustered environment

* You can compile or download specific binaries of Spark that run on each of the supported environments.

* With the Spark Standalone you can setup a Spark cluster without any dependency on any other Resource Mangers
* This is probably the best option for quick & dirty solutions, but if you want to build a cluster that other applications can run upon (like MapReduce), then it's a good idea to consider some of the other options

* Some other cluster options include YARN and Mesos.
* YARN is what encompasses the new Resource and Node Manager in the Hadoop 2.x ecosystem.  The purpose of YARN is to decouple these responsibilities from the daemon's of the traditional Hadoop ecosystem

* Apache Mesos is similar to YARN, but goes a step further in unifying a cluster into a single pool of resources that applications can use.
* Definition from Apache Mesos website:
* "Apache Mesos abstracts CPU, memory, storage, and other compute resources away from machines (physical or virtual), enabling fault-tolerant and elastic distributed systems to easily be built and run effectively."
---
## Apache Mesos cluster demo

### StackOverflow.com posts in `.cache()` using `spark-shell`

### learning-spark project

* Google Cloud Compute (free tier)
* 1 master VM and 3 slave VM's, 4 total, `n1-standard-2` instance type
* 2 vCPU each, 8 vCPU total
* 7.5 GB RAM each, 30GB RAM total
* HDFS, 500 GB each, 2 TB total

<br />
```bash
$ gcloud compute instances list --project learning-spark
NAME                 ZONE          MACHINE_TYPE  INTERNAL_IP    EXTERNAL_IP     STATUS
development-5159-d3d us-central1-a n1-standard-2 10.217.7.180   146.148.xxx.xxx RUNNING
development-5159-b61 us-central1-a n1-standard-2 10.144.195.205 104.197.xxx.xxx RUNNING
development-5159-d9  us-central1-a n1-standard-2 10.173.40.36   104.197.xxx.xxx RUNNING
development-5159-5d7 us-central1-a n1-standard-2 10.8.67.247    104.154.xxx.xxx RUNNING
```
???
* This is my second demo

* I chose to use Google Cloud Compute service because they have an amazing deal of $300 or 60 days (whichever runs out first) to use nearly nearly any part of their platform
  * The main restrictions are that you can only have 8 vCPU total
  * The `n1-standard-2` image is the largest VM you can setup.
  * If you don't have a `hulk` laying around or want to play with a cluster setup then this is an excellent option.

* I setup an Apache Mesos cluster with the help of Mesosphere:
  * From their website:
  * "The Mesosphere is a new kind of operating system that organizes all of your machines, VMs, and cloud instances into a single pool of intelligently and dynamically shared resources. It runs on top of and enhances any modern version of Linux. ""
  * Mesosphere has excellent guides to setup Mesos clusters on various Infrastructure as as Service providers like Google Cloud Compute, Amazon Web Services, and Digital Ocean.
  * They even have scripts that allow you to automate the setup of these environments if you provide them with your provider access key
  * All I had to do to accomodate my setup was attach larger disks and update HDFS config to use them

* Show Google Cloud Computer console
* Show spark-shell console with cached dataset

* Show total count of scala questions

```
:paste

val q = StackAnalysis.questions(sc, "file:///home/seglo/source/learning-spark/data/stackexchange/stackoverflow.com-Posts/Posts1m.xml")

// scala questions
val scalaQs = qCache.filter { case (id, creationDate, tags) => tags.contains("scala") }.cache

scalaQs.count()
```

* Total scala questions by month
```
:paste
val sdfMonth = new java.text.SimpleDateFormat("yyyy-MM")

scalaQs.map { case (id, creationDate, tags) => (sdfMonth.format(creationDate), 1)} 
  .reduceByKey((a, b) => a + b) 
  .sortByKey(true)
  .collect 
  .foreach(println)
```

* ** show hulk output **
---
# References

### Spark
* [Spark Documentation](https://spark.apache.org/docs/latest/)
* [Spark: Cluster Computing with Working Sets](https://amplab.cs.berkeley.edu/wp-content/uploads/2011/06/Spark-Cluster-Computing-with-Working-Sets.pdf) - University of Berkely paper introducing Spark - Much of the introduction of this talk is paraphrased from this paper.
* **[`spark-workshop` by Dean Wampler](https://github.com/deanwampler/spark-workshop) - Spark impl. examples in a Typesafe activator project**
* [Cloudera: Introduction to Apache Spark developer training](http://www.slideshare.net/cloudera/spark-devwebinarslides-final?related=1)

### Clusters

* [Google Cloud Compute](https://cloud.google.com/compute/) 
* [Mesosphere in the Cloud](http://mesosphere.com/docs/getting-started/) - Tutorials to setup mesosphere on IaaS (Amazon, DigitalOcean, Google)
  * [Google Cloud Compute setup](http://mesosphere.com/docs/getting-started/cloud/google/)
  * [Running Spark on Mesos](http://spark.apache.org/docs/1.3.0/running-on-mesos.html) - Mesosphere docs are out-of-date, use this once mesosphere sets up cluster for you.
* [Spark atop Mesos on Google Cloud Platform](http://ceteri.blogspot.ca/2014/09/spark-atop-mesos-on-google-cloud.html)
* [Running Spark on Mesos](https://spark.apache.org/docs/1.3.0/running-on-mesos.html)
* [YARN Architecture](http://hadoop.apache.org/docs/current/hadoop-yarn/hadoop-yarn-site/YARN.html)
* [HDFS Architecture](https://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-hdfs/HdfsDesign.html)
* [`docker-spark` by SequenceIQ](https://github.com/sequenceiq/docker-spark) - If you want to play around with a YARN cluster config

### Data

* [Stack Exchange Creative Commons data](http://blog.stackexchange.com/category/cc-wiki-dump/)
---
# That's it!

For this presentation and sample code.

https://github.com/seglo/learning-spark/

## I'm a Software Engineer working at <a href="http://www.ethoca.com/" alt="ethoca"><img src="ethoca.jpg" style="height:40px" /></a> (we're hiring!)

* <a href=mailto:seglo@randonom.com>seglo@randonom.com</a>
* http://randonom.com/blog/
* https://github.com/seglo/
* https://twitter.com/randonom/
    </textarea>
    <script src="https://gnab.github.io/remark/downloads/remark-latest.min.js">
    </script>
    <script>
      var slideshow = remark.create();
    </script>
  </body>
</html>
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 

processing logs 

 val ambariLogs = sc.textFile("file:///var/log/ambari-agent/ambari-agent.log")
 
 ===================
 
 Issue:- Spark on comand line is not working. Below is the syntax we use and attached error msgs and jar files (HWX 00056360)
Error msg:- we ran into some issues with the Spark on Yarn when running in the cluster mode. The error message indicates datanucleus jars are not resolved. I tried to explicitly added the jar files like:
cd /usr/hdp/current/spark-client 
./bin/spark-submit --class com.dev.spark.DigitalPulseSparkBigTableXJob --master yarn-cluster /users/home/xbblwp9/lamdax_2.10-1.0.jar --jars lib/datanucleus-api-jdo-3.2.6.jar,lib/datanucleus-core-3.2.10.jar,lib/datanucleus-rdbms-3.2.9.jar
Solution:- Spark + Kerberos + yarn-cluster mode + remote hive meta store for SparkSQL won't work. We are working on adding this feature to Spark and will have it in Dal-M10.
The workaround right now is to try this with Spark + Kerberos + yarn-client mode for SparkSQL



./bin/spark-shell --master spark://r39bn00:7077 yarn-cluster --driver-memory 2g -- executor-memory 2g
/usr/hdp/2.2.6.0-2800/spark



export HADOOP_HOME=${HADOOP_HOME:-/usr/hdp/2.2.6.0-2800/hadoop}
export HADOOP_MAPRED_HOME=${HADOOP_MAPRED_HOME:-/usr/hdp/2.2.6.0-2800/hadoop-mapreduce}
export HADOOP_YARN_HOME=${HADOOP_YARN_HOME:-/usr/hdp/2.2.6.0-2800/hadoop-yarn}
export HADOOP_LIBEXEC_DIR=${HADOOP_HOME}/libexec
export HDP_VERSION=${HDP_VERSION:-2.2.6.0-2800}
export HADOOP_OPTS="${HADOOP_OPTS} -Dhdp.version=${HDP_VERSION}"
export YARN_OPTS="${YARN_OPTS} -Dhdp.version=${HDP_VERSION}"

exec /usr/hdp/2.2.6.0-2800//hadoop-yarn/bin/yarn.distro "$@"
-bash-4.1$ ls

/bin/spark-submit --class my.main.Class --master yarn-cluster --jars my-other-jar.jar,my-other-other-jar.jarmy-main-jar.jarapp_arg1 app_arg



val yc = sqlContext.sql("select service_name,account_number , count(*) from dp_xat.t_dp_payload_v2_1_0_0 group by service_name,account_number limit 10").collect().foreach(println)
15/09/24 09:24:57 INFO ParseDriver: Parsing command: select service_name,account_number , count(*) from dp_xat.t_dp_payload_v2_1_0_0 group by service_name,account_number limit 10
15/09/24 09:24:58 INFO ParseDriver: Parse Completed
15/09/24 09:24:58 INFO metastore: Trying to connect to metastore with URI thrift://r39an00.bnymellon.net:9083
15/09/24 09:24:58 INFO metastore: Connected to metastore.
15/09/24 09:24:59 WARN DomainSocketFactory: The short-circuit local reads feature cannot be used because libhadoop cannot be loaded.
15/09/24 09:24:59 INFO SessionState: No Tez session required at this point. hive.execution.engine=mr.
15/09/24 09:25:00 INFO deprecation: mapred.map.tasks is deprecated. Instead, use mapreduce.job.maps
15/09/24 09:25:00 INFO MemoryStore: ensureFreeSpace(528214) called with curMem=0, maxMem=1111794647
15/09/24 09:25:00 INFO MemoryStore: Block broadcast_0 stored as values in memory (estimated size 515.8 KB, free 1059.8 MB)
15/09/24 09:25:00 INFO MemoryStore: ensureFreeSpace(87811) called with curMem=528214, maxMem=1111794647
15/09/24 09:25:00 INFO MemoryStore: Block broadcast_0_piece0 stored as bytes in memory (estimated size 85.8 KB, free 1059.7 MB)
15/09/24 09:25:00 INFO BlockManagerInfo: Added broadcast_0_piece0 in memory on r39cn00.bnymellon.net:46901 (size: 85.8 KB, free: 1060.2 MB)
15/09/24 09:25:00 INFO BlockManagerMaster: Updated info of block broadcast_0_piece0
15/09/24 09:25:00 INFO DefaultExecutionContext: Created broadcast 0 from broadcast at TableReader.scala:68
15/09/24 09:25:00 INFO DefaultExecutionContext: Starting job: runJob at basicOperators.scala:141
15/09/24 09:25:00 INFO PerfLogger: <PERFLOG method=OrcGetSplits from=org.apache.hadoop.hive.ql.io.orc.ReaderImpl>
15/09/24 09:25:00 INFO deprecation: mapred.input.dir is deprecated. Instead, use mapreduce.input.fileinputformat.inputdir
15/09/24 09:25:01 INFO OrcInputFormat: FooterCacheHitRatio: 0/10
15/09/24 09:25:01 INFO PerfLogger: </PERFLOG method=OrcGetSplits start=1443101100977 end=1443101101279 duration=302 from=org.apache.hadoop.hive.ql.io.orc.ReaderImpl>
15/09/24 09:25:01 INFO PerfLogger: <PERFLOG method=OrcGetSplits from=org.apache.hadoop.hive.ql.io.orc.ReaderImpl>
15/09/24 09:25:01 INFO OrcInputFormat: FooterCacheHitRatio: 0/1
15/09/24 09:25:01 INFO PerfLogger: </PERFLOG method=OrcGetSplits start=1443101101282 end=1443101101481 duration=199 from=org.apache.hadoop.hive.ql.io.orc.ReaderImpl>
15/09/24 09:25:01 INFO PerfLogger: <PERFLOG method=OrcGetSplits from=org.apache.hadoop.hive.ql.io.orc.ReaderImpl>
15/09/24 09:25:01 INFO OrcInputFormat: FooterCacheHitRatio: 0/10

15/09/24 09:32:17 WARN TaskSchedulerImpl: Initial job has not accepted any resources; check your cluster UI to ensure that workers are registered and have sufficient memory
15/09/24 09:32:32 WARN TaskSchedulerImpl: Initial job has not accepted any resources; check your cluster UI to ensure that workers are registered and have sufficient memory
15/09/24 09:32:47 WARN TaskSchedulerImpl: Initial job has not accepted any resources; check your cluster UI to ensure that workers are registered and have sufficient memory

hdp-select status hadoop-client | sed 's/hadoop-client - \(.*\)/\1/' --- hdp version 
2.2.6.0-2800


========

 ./bin/spark-submit --class org.apache.spark.examples.SparkPi --master yarn-cluster --num-executors 3 --driver-memory 512m --executor-memory 512m --executor-cores 1 lib/spark-examples*.jar 10 -- workig 
 =========================
 class path for spark 
 /etc/hadoop/conf	System Classpath
/usr/hdp/2.2.6.0-2800/spark/conf	System Classpath
/usr/hdp/2.2.6.0-2800/spark/lib/datanucleus-api-jdo-3.2.6.jar	System Classpath
/usr/hdp/2.2.6.0-2800/spark/lib/datanucleus-core-3.2.10.jar	System Classpath
/usr/hdp/2.2.6.0-2800/spark/lib/datanucleus-rdbms-3.2.9.jar	System Classpath
/usr/hdp/2.2.6.0-2800/spark/lib/spark-assembly-1.2.1.2.2.6.0-2800-hadoop2.6.0.2.2.6.0-2800.jar

----------------------qrejb41l

Kafka used Yarn or not ?
spark realtime graph in ambari 
spark cluster adv and disadvan
spark to kafka
spark memory details and management
RDD flow 
spark  streaming vs storm
spark env setting 

how spark using memory with RDD


r0009un0c	qrejb41l
r000a1n0c	qrejb41l
r000awn0c	qrejb41l
r000avn0c	qrejb41l

qrejb41l
yecbdc 
|||||||||||||||||||||||||||||||||||||||||||||||||||||||
keytab for spark 

/etc/security/keytabs/spark.headless.keytab
dshm00m@HADOOP-DQA-CNJ01.BNYMELLON.NET
kinit dshm00m@PAC.BNYMELLON.NET
`|||||||||||||||||||||||||||||||||||||||||||||||||||||||

hadoop checknative -a
2015-10-07 06:13:03,747 INFO  [main] bzip2.Bzip2Factory: Successfully loaded & initialized native-bzip2 library system-native
2015-10-07 06:13:03,754 INFO  [main] zlib.ZlibFactory: Successfully loaded & initialized native-zlib library
Native library checking:
hadoop:  true /usr/hdp/2.2.6.0-2800/hadoop/lib/native/libhadoop.so.1.0.0
zlib:    true /lib64/libz.so.1
snappy:  true /usr/hdp/2.2.6.0-2800/hadoop/lib/native/libsnappy.so.1
lz4:     true revision:99
bzip2:   true /lib64/libbz2.so.1
openssl: false Cannot load libcrypto.so (libcrypto.so: cannot open shared object file: No such file or directory)!
===================

(2) Try adding the HADOOP_OPTS environment variable:

export HADOOP_OPTS="$HADOOP_OPTS -Djava.library.path=/usr/local/hadoop/lib/native"
It doesn't work, and reports the same error.

(3) Try adding the HADOOP_OPTS and HADOOP_COMMON_LIB_NATIVE_DIR environment variable:

export HADOOP_COMMON_LIB_NATIVE_DIR=$HADOOP_HOME/lib/native
export HADOOP_OPTS="-Djava.library.path=$HADOOP_HOME/lib"
It still doesn't work, and reports the same error.

Could anyone give some clues about the issue?


====

Spark will try to load 128Mb chunk into memory and process it in RAM. Keep in mind that that the size in memory can be several times larger than the original size from the raw file due to Java overhead (Java headers, etc). From my experience it can be 2-4 time larger. If there is not enough memory (RAM) Spark will spill the data to local disk. You may want to tweak these two parameters to minimise the spill: spark.shuffle.memoryFraction and spark.storage.memoryFraction.



--------------------installing spark on git 

git clone git://github.com/apache/spark.git


build/mvn -DskipTests clean package

build/mvn -DskipTests -Psparkr package

===============================================================================================================================================
Spark Testing 
-------------
Reference for Speed Test: 
http://www.slideshare.net/hortonworks/hive-on-spark-is-blazing-fast-or-is-it-final
https://cwiki.apache.org/confluence/display/Hive/Hive+on+Spark
https://www.xplenty.com/blog/2015/01/apache-spark-vs-tez-comparison/



----------------------------------------------------------------------------
hdfs dfs -du -s -h /addh0000/data/addh0001/PTA_PAY_TRX_PARTY_data/output
10.0 G  /addh0000/data/addh0001/PTA_PAY_TRX_PARTY_data/output

create external table ld_data_temp (bank_id STRING, mnemonic STRING,bank_name STRING, location STRING)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ','
STORED AS TEXTFILE
LOCATION '/addh0000/data/addh0001/PTA_PAY_TRX_PARTY_data/output';


drop table ld_data;

create table ld_data (bank_id STRING, mnemonic STRING,bank_name STRING, location STRING)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ','
STORED AS TEXTFILE;

From ld_data_temp 
insert into table ld_data
select *;

select location,count(*) as sum from ld_data group by location sort by sum DESC limit 100

val sourval = sqlContext.sql("select location,count(*) as sum from ld_data group by location sort by sum DESC limit 10")
sourval.map(t => "Name: " + t(0)).collect().foreach(println)

sourval.map(t => "Name: " + t(0) +t(1) ).collect().foreach(println)


Spark -> took 125.509773 s
Hive -> Time taken: 55.298 seconds 
Hive via spark Engine -> Time taken: 62.78 seconds


--------------------------------
emp_info
select application,count(*) as sum from emp_info group by application sort by sum DESC limit 2;

val sourval = sqlContext.sql("select application,count(*) as sum from emp_info group by application sort by sum DESC limit 2")
sourval.map(t => "Name: " + t(0) +t(1) ).collect().foreach(println)

Took 
Spark -> 5.727039 s

Hive -> Time taken: 22.876 seconds

Hive on spark engin -> Time taken: 11.257 seconds
--------------------------------------------------------------------------------------------------------------------------------

set up spark 
-------------
r39cn00.bnymellon.net

1.login to hdfs

2.cd /usr/hdp/current/spark-client/

3.Run the Spark shell:

./bin/spark-shell --master yarn-client --driver-memory 512m -- executor-memory 512m

val sqlContext = new org.apache.spark.sql.hive.HiveContext(sc)
=============================================================================================
Spark Sample 
============
import org.apache.spark.api.java.*;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function;

public class SimpleApp {
  public static void main(String[] args) {
    String logFile = "YOUR_SPARK_HOME/README.md"; // Should be some file on your system
    SparkConf conf = new SparkConf().setAppName("Simple Application");
    JavaSparkContext sc = new JavaSparkContext(conf);
    JavaRDD<String> logData = sc.textFile(logFile).cache();

    long numAs = logData.filter(new Function<String, Boolean>() {
      public Boolean call(String s) { return s.contains("a"); }
    }).count();

    long numBs = logData.filter(new Function<String, Boolean>() {
      public Boolean call(String s) { return s.contains("b"); }
    }).count();

    System.out.println("Lines with a: " + numAs + ", lines with b: " + numBs);
  }
}

*****************************************************************************************************************************************************************
UNIX-REF
========
Backup to NFS mount script.
---------------------------
#!/bin/sh
####################################
#
# Backup to NFS mount script.
# Note: have to run root access. As some files are read restricted 
####################################

# What to backup.
backup_files=("/etc/hadoop/conf.empty" "/etc/ambari-agent/conf" "/etc/hbase/conf.dist" "/etc" "/etc/hive/conf.dist" "/etc/flume/conf.dist")

# Where to backup to.
dest=("/usr/tmp/backup/backup1/hadoop_conf" "/usr/tmp/backup/backup1/ambari-conf" "/usr/tmp/backup/backup1/hbase-conf" "/usr/tmp/backup/backup1/etc_conf" "/usr/tmp/backup/backup1/hive-conf" "/usr/tmp/backup/backup1/flume-conf")

# Print start status message.

for ((i=0;i<${#backup_files[@]};++i)); do
    printf "backing up  %s to %s\n" "${backup_files[i]}" "${dest[i]}"
        mkdir ${dest[i]}
        cp ${backup_files[i]}/* ${dest[i]}/	
done
# Print end status message.
echo
echo "Backup finished"
==============================================================================================
ps -ef | grep -in ambar
==============================================================================================
To Move last N number of files in Linux 
----------------------------------------
#!/bin/bash
SRC=/grid1/ftpdata/addh0010/InfoSecDataExceptions/temp
DEST=/grid1/ftpdata/addh0010/InfoSecDataExceptions/tempdest
N=100;
ls -ltr $SRC | tail -$N | while read FN
do
ts=`echo $FN | awk -F " " '{print $6,$7,$8,$9}'`
mvfile=`echo $FN | awk -F " " '{print $9}'`
echo $ts
mvsrc=$SRC/$mvfile
mv $mvsrc $DEST
done
==============================================================================================
to know whether the crontab access is provided or not : 

 >/usr/seos/bin/sesudo xa.eac-denies
==============================================================================================
 to know whether the port is listening or not 

netstat -tupln | grep -i 1000 

==============================================================================================
To find the last modified time of a directory and find the difference between present time and last mod time 
-------------------------------------------------------------------------------------------------------------
#!/bin/bash
#res=`date | cut -d ' ' -f4|cut -d ':' -f1`
last_mod=`stat -c %y /grid1/ftpdata/addh0010/InfoSecBoltpoint/ | cut -d '.' -f1`
echo "last modified time $last_mod"
present_time=`date +"%s"`
echo "$present_time"
lst=`date --date="$last_mod" +"%s"`
echo "unix last mod time $lst"
op=`expr $present_time - $lst`
echo "time diff is $op"
hr=`expr $op / 60`
echo "min diff is $hr"
==============================================================================================
Find Space quota in HDFS Tenant 
#!/bin/bash
cat input.txt | while read line
do
echo $line
hdfs dfs -count -q /$line/ > temp_space.log
res=`tail -1 temp_space.log` >> space_quota.txt
echo $line $res
done
==============================================================================================
to print list of files modified with 35 mins and store it in array and printing it with new line 
#!/bin/bash
i=0
for f in $(find /grid1/ftpdata/addh0010/InfoSecDuplicate/ -amin -350 | sed -n '1!p');
do
file_name[$i]=`ls -ltr $f `
i=`expr $i + 1`
done
printf '%s\n' "${file_name[@]}"

*****************************************************************************************************************************************************************

HDFS-REF
========

hdfs fsck /addh0110/data/addh0111/fullz.txt -files -blocks -locations -racks

hadoop fs -chown -R gdpmesm:addh0011 /addh0010/hive/addh0011/warehouse/InfoSec.db/infosec_audit_tbl

hadoop fs -chmod -R 770 /addh0010/hive/addh0011/warehouse/InfoSec.db/infosec_hostname

hadoop dfsadmin -report

hdfs fsck /addh0990/data/addh0991/nimbus.log -files -blocks

hdfs dfsadmin -setSpaceQuota 50g /addh0540

hdfs dfs -getfacl [-R] <path>

hadoop fs -count -q /addh0990

To get a valid Kerberos ticket use kinit:
----------------------------------------
1.	kinit <Hadoop_User_ID>@REALM_NAME
Here, Hadoop_User_ID should be in small letters and REALM_NAME should be in capital letters.
When prompted for a password, enter your Windows/LAN password.
2.	Use klist to check status of your Kerberos ticket
Note: This ticket will be valid for 10 hours since the time of creation. After that, you need to renew the ticket. You can also use UserID keytab to get a Kerberos ticket.
3.	Once you get a valid ticket you can access the Hadoop services.

e.g. user xbblj3x belonging to PAC domain can get a kerberos ticket using:
kinit xbblj3x@PAC.BNYMELLON.NET 

Please refer to this document for more details:
https://mysourcesocial.bnymellon.net/docs/DOC-19166
==============================================================================================================
Keytab kadmin.local

addprinc -randkey xbblwv5@HADOOP-DQA-TPC01.BNYMELLON.NET 

xst -norandkey -k xbblwv5.keytab xbblwv5@HADOOP-DQA-TPC01.BNYMELLON.NET 

end 
/////////////////////////////////////////////
addprinc -randkey xeccv34/_HOST@HADOOP-PROD-TPC01.BNYMELLON.NET
xst -norandkey -k xeccv34.tpcprod.keytab xeccv34/_HOST@HADOOP-PROD-TPC01.BNYMELLON.NET
addprinc -randkey xeccv34@HADOOP-PROD-TPC01.BNYMELLON.NET
xst -norandkey -k xeccv34.tpcprod.keytab xeccv34@HADOOP-PROD-TPC01.BNYMELLON.NET


creating keytab for gdpmesm on 9 jul 2015

addprinc -randkey gdpmesm@HADOOP-DQA-TPC01.BNYMELLON.NET 
xst -norandkey -k gdpmesm.keytab gdpmesm@HADOOP-DQA-TPC01.BNYMELLON.NET 

kinit -kt /users/home/gdpmesm/gdpmesm.keytab gdpmesm@HADOOP-DQA-TPC01.BNYMELLON.NET


creating keytab for dshm05m on 30 jul 2015

addprinc -randkey dshm05m@HADOOP-DQA-TPC01.BNYMELLON.NET 
xst -norandkey -k dshm05m.keytab dshm05m@HADOOP-DQA-TPC01.BNYMELLON.NET 



kinit -kt /users/home/dshm05m/dshm05m.keytab dshm05m@HADOOP-DQA-TPC01.BNYMELLON.NET
=================================================================================================
REST Api r39dn00

curl -i --negotiate -u : "http://r38yn10.bnymellon.net:50070/webhdfs/v1/tmp/?op=LISTSTATUS"

curl -i --negotiate -u : "http://r38ap10.bnymellon.net:50470/webhdfs/v1/tmp/data.tsv?&op=OPEN"

/tmp/data.tsv  ----- curl -i --negotiate -u : "http://r38yn10.bnymellon.net:50070/webhdfs/v1/tmp/data.tsv?&op=OPEN"
=================================================================================================
Ambari server restart 

sesu - root

at now

service ambari-agent stop

ctrl + D

service ambari-agent status
=================================================================================================
Data Movement - Speed up 
------------------------

Set the dfs.client-write-packet-size size.  https://hadoop.apache.org/docs/r2.4.1/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml

Config parameters suggested by HDP:http://docs.hortonworks.com/HDPDocuments/HDP2/HDP-2.3.4/bk_upgrading_hdp_manually/content/migrate-configs-13.html

Reference
---------
http://stackoverflow.com/questions/30400249/hadoop-pipeline-write-and-parallel-read
https://issues.apache.org/jira/browse/HDFS-7308
https://hadoop.apache.org/docs/stable/hadoop-project-dist/hadoop-hdfs/HdfsDesign.html
http://www.aosabook.org/en/hdfs.html -  abt yahoo practice 

http://hadoop.apache.org/docs/r2.6.2/hadoop-project-dist/hadoop-common/core-default.xml
=================================================================================================
*****************************************************************************************************************************************************************
FLUME-REF

# Flume agent config
testAgent.sources = eventlog
testAgent.channels = file_channel
testAgent.sinks = sink_to_hdfs

# Define / Configure source
testAgent.sources.eventlog.type = exec
testAgent.sources.eventlog.command = tail -F /tmp/dshmhtm/hive.log
testAgent.sources.eventlog.restart = true
testAgent.sources.eventlog.batchSize = 1000
#testAgent.sources.eventlog.type = seq

# HDFS sinks
testAgent.sinks.sink_to_hdfs.type = hdfs
testAgent.sinks.sink_to_hdfs.hdfs.fileType = DataStream
testAgent.sinks.sink_to_hdfs.hdfs.path = /tmp/dshmhtm/events
testAgent.sinks.sink_to_hdfs.hdfs.filePrefix = eventlog
testAgent.sinks.sink_to_hdfs.hdfs.fileSuffix = .log
testAgent.sinks.sink_to_hdfs.hdfs.batchSize = 1000

# Use a channel which buffers events in memory
testAgent.channels.file_channel.type = file
testAgent.channels.file_channel.checkpointDir = /tmp/dshmhtm/checkpoint
testAgent.channels.file_channel.dataDirs = /tmp/dshmhtm/data

# Bind the source and sink to the channel
testAgent.sources.eventlog.channels = file_channel
testAgent.sinks.sink_to_hdfs.channel = file_channel

/usr/hdp/2.3.4.7-4/flume/bin/flume-ng.distro agent -n testAgent --conf /usr/hdp/2.3.4.7-4/etc/flume/conf.empty -f /users/home/dshmhtm/flume/testAgent.conf

*****************************************************************************************************************************************************************
HBASE-REF
=========


*****************************************************************************************************************************************************************
INTEGERATION-REF 
=================
PIG to HBASE 
raw = LOAD 'hbase://d1' USING org.apache.pig.backend.hadoop.hbase.HBaseStorage('cf:dno cf:id cf:name', '-loadKey=true') AS (row:chararray, dno:chararray, id:chararray, name:chararray);

dept = LOAD 'hbase://d2' USING org.apache.pig.backend.hadoop.hbase.HBaseStorage('cf:dno cf:name', '-loadKey=true') AS (row:chararray, dno:chararray, name:chararray);

join1 = JOIN raw BY $1 LEFT OUTER, dept BY $1;

res = foreach join1 generate $0,$1,$2,$3,$6;

dump res;

STORE res INTO 'hbase://d3' USING org.apache.pig.backend.hadoop.hbase.HBaseStorage('cf:dno cf:id cf:name cf:dname');
================================================================================================================================
HBASE to HIVE 
-------------
CREATE external TABLE d1_g1_hbase (key STRING, dno STRING, id STRING, name STRING)
STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
WITH SERDEPROPERTIES ('hbase.columns.mapping' = ':key,cf:dno,cf:id,cf:name')
TBLPROPERTIES ('hbase.table.name' = 'd1');

*****************************************************************************************************************************************************************
NIFI-REF
========
Nifi
Apache Nifi supports powerful and scalable directed graphs of data routing, transformation, and system mediation logic.
Key capabilities:
o	Collect any and all IoAT data from dynamically changing, disparate and physically distributed sensors, machines, geo location devices, clickstreams, files, and social feeds via a highly secure lightweight agent.
o	Reliably conduct secure point-to-point and bidirectional data flows in real time
o	Curate data via tracing, parsing, filtering, joining, transforming, forking or cloning of dataflows to generate holistic context and support appropriate responses.
Advantages:-
	No Programmer required - Visual Drag & Drop interface to create agile DataFlows.
	Can process any data.
	Encrypted data transfer.
	Guaranteed Delivery.
	Prioritized Queuing.
	Data Buffering w/ Back Pressure and Pressure Release.
	Max throughput.
	Will provide Audit functionality (Will have the entire log for Dataflow).
	Can extend Nifi. 
	Dynamic flow changes - Can set a new business rule via RESTAPI. Not Possible in Storm.
Performance 
NiFi for a large class of dataflows then should be able to efficiently reach 100 or more MB/s of throughput.
Nifi in USE
One early adopter of NiFi, a security outfit called Prescient Edge, is using the technology to manage thousands of streaming feeds sources from around the world for the purpose of alerting clientele to potential trouble spots. The company uses NiFi to control the flow of data into an analytic application running in SAP HANA  that automatically sends alerts to clients to avoid geopolitical hotspots.
Nifi Vs Kafka
Nifi will not replace Kafka. It can process data from kafka or vice-versa.
Nifi Vs Storm/Spark Streaming 
Nifi can replace storm in simple event processing not in complex event processing.
Nifi Vs ETL Tool 
Nifi will not replace ETL as Nifi is stream processing and batch data ingest from new or traditional data sources.
HDF vs. HDP
Hortonworks DataFlow (HDF) is a completely separate product from Hortonworks Data Platform (HDP). They can certainly work together but they are separate support subscriptions.

Ref: 

https://nifi.apache.org/docs.html
Q&A - http://hortonworks.com/webinar/introducing-hortonworks-dataflow/
Admin guide (Installing)- https://nifi.apache.org/docs/nifi-docs/html/administration-guide.html
Bright Talk - https://www.brighttalk.com/webcast/9573/179025?utm_campaign=add-to-calendar&utm_medium=calendar&utm_source=brighttalk-transact 
*****************************************************************************************************************************************************************