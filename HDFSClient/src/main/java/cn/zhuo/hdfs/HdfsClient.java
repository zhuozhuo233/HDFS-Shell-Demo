package cn.zhuo.hdfs;

/*
客户端代码流程：
    1.获取一个客户端对象
    2.执行相关的操作
    3.关闭资源
    HDFS zookeeper
 */

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;


public class HdfsClient {

    private FileSystem fs;

    @Before
    public void init() throws URISyntaxException, IOException, InterruptedException {
        //连接集群nn地址
        URI uri = new URI("hdfs://hadoop102:8020");
        //创建一个配置文件
        Configuration configuration = new Configuration();

        configuration.set("dfs.replication","2");

        //用户
        String user = "zhuo";

        //1.获取到了客户端对象
        fs = FileSystem.get(uri, configuration,user);

    }

    @After
    public void close() throws IOException {
        //3.关闭资源
        fs.close();
    }



    @Test
    public void testmkdir() throws URISyntaxException, IOException {

        //2.创建一个文件夹
        fs.mkdirs(new Path("/xiyou/huaguoshan1"));
    }

    //上传
    @Test
    public void testPut() throws IOException {
        //参数解读：参数一：表示删除元数据； 参数二：表示是否允许覆盖； 参数三：原数据路径； 参数四：目的地路径

        fs.copyFromLocalFile(false,true,new Path("E:\\sunwukong.txt"),new Path("/xiyou/huaguoshan"));
    }

    @Test
    //下载
    public void testGet() throws IOException {
        //参数解读：参数一：源文件是否删除； 参数二：源文件路径HDFS； 参数三：目标地址路径win； 参数四：
        fs.copyToLocalFile(true, new Path("/xiyou/huaguoshan"), new Path("E:\\"),true);
    }


    @Test
    //删除
    public void testRm() throws IOException {
        //参数解读： 参数一：要删除的路径； 参数二：是否递归删除
        //删除文件
//        fs.delete(new Path("/xiyou/huaguoshan1"),true);

        //删除空目录
//        fs.delete(new Path("/xiyou"),false);

        //删除非空目录
        fs.delete(new Path("/jinguo"),true);
    }

    //文件得更名和移动
    @Test
    public void tetsmv() throws IOException {
        //参数解读： 参数一：原文件路径 参数二：目标文件路径
        //对文件名称的修改
//        fs.rename(new Path("/wcinput/word.txt"),new Path("/wcinput/zz.txt"));

        //文件的移动和更名
//        fs.rename(new Path("/wcinput/zz.txt"),new Path("/cls.txt"));

        //目录更名
        fs.rename(new Path("/wcinput"),new Path("/wcinput1"));
    }

    @Test
    public void fileDetail() throws IOException {
        //获取所有文件信息
        RemoteIterator<LocatedFileStatus> listFiles = fs.listFiles(new Path("/"), true);

        //遍历所有文件
        while (listFiles.hasNext()){
            LocatedFileStatus fileStatus = listFiles.next();

            System.out.println("============" + fileStatus.getPath() + "============");
            System.out.println(fileStatus.getPermission());
            System.out.println(fileStatus.getOwner());
            System.out.println(fileStatus.getGroup());
            System.out.println(fileStatus.getLen());
            System.out.println(fileStatus.getModificationTime());
            System.out.println(fileStatus.getReplication());
            System.out.println(fileStatus.getBlockSize());
            System.out.println(fileStatus.getPath().getName());

            //获取块信息
            BlockLocation[] blockLocations = fileStatus.getBlockLocations();

            System.out.println(Arrays.toString(blockLocations));

        }

    }

    //判断是文件夹还是文件
    @Test
    public void testFile() throws IOException {
        FileStatus[] listStatus = fs.listStatus(new Path("/"));

        for (FileStatus status: listStatus){

            if (status.isFile()){
                System.out.println("文件:" + status.getPath().getName());
            }else {
                System.out.println("目录:" + status.getPath().getName());
            }

        }
    }

}
