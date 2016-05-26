import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.fs.Path;

public class HBaseSample {

        private static Configuration conf = null;
        static {
                conf = HBaseConfiguration.create();
                conf.addResource(new Path("/etc/hbase/conf.dist/hbase-site.xml"));
        }

        public static void creatTable(String tableName, String[] familys)
                        throws Exception {
                HBaseAdmin admin = new HBaseAdmin(conf);
                if (admin.tableExists(tableName)) {
                        System.out.println("table already exists!");
                } else {
                        HTableDescriptor tableDesc = new HTableDescriptor(tableName);
                        for (int i = 0; i < familys.length; i++) {
                                tableDesc.addFamily(new HColumnDescriptor(familys[i]));
                        }
                        admin.createTable(tableDesc);
                        System.out.println("create table " + tableName + " ok.");
                }
        }
        public static void deleteTable(String tableName) throws Exception {
                try {
                        HBaseAdmin admin = new HBaseAdmin(conf);
                        admin.disableTable(tableName);
                        admin.deleteTable(tableName);
                        System.out.println("delete table " + tableName + " ok.");
                } catch (MasterNotRunningException e) {
                        e.printStackTrace();
                } catch (ZooKeeperConnectionException e) {
                        e.printStackTrace();
                }
        }
        public static void main(String[] agrs) {
                try {
                        String tablename = "d1";
                        String[] familys = { "grade", "course" };
                        if (!UserGroupInformation.isSecurityEnabled())
                                throw new IOException(
                                                "Security is not enabled in core-site.xml");
                        try {
                                UserGroupInformation ugi = UserGroupInformation.getLoginUser();
                                String username = ugi.getUserName();
                                System.out.println("ugi login username is:" + username);
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                        HBaseSample.deleteTable(tablename);
                        HBaseSample.creatTable(tablename, familys);
                        }
                        catch (Exception e) {
                        e.printStackTrace();
                }
        }
}
