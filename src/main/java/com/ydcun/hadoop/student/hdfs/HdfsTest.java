package com.ydcun.hadoop.student.hdfs;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class HdfsTest {
    /**
     * get hadoop fileSystem
     * @return
     * @throws IOException
     */
    public static FileSystem getHdfsFileSystem()throws IOException {
        Configuration configuration = new Configuration();
        FileSystem fs = FileSystem.get(configuration);
        //System.out.println(fs);
        return fs;
    }
    public static void readToSystemOut(String fileName) throws IOException {
        FileSystem fileSystem = getHdfsFileSystem();

        //String fileName = "/user/ydcun/mapreduce/wordcount/input/wc.input";
        Path path = new Path(fileName);
        FSDataInputStream inputStream = fileSystem.open(path);

        try{
            IOUtils.copyBytes(inputStream,System.out,1024,false);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            IOUtils.closeStream(inputStream);
        }

    }
    public static void main(String[] arge) throws IOException {
//        String fileName = "/user/ydcun/mapreduce/wordcount/input/wc.input";
//        readToSystemOut(fileName);


        String writeFileName = "/user/ydcun/mapreduce/wordcount/input/wc3.input";
        Path writePath = new Path(writeFileName);

        FileSystem fileSystem = getHdfsFileSystem();
        FSDataOutputStream outStream = fileSystem.create(writePath);

        FileInputStream fileInputStream = new FileInputStream(new File("/Users/ydcun-pro/myapp/hadoop-2.7.5/README.txt"));
        IOUtils.copyBytes(fileInputStream,outStream,4096,false);
        IOUtils.closeStream(outStream);
        IOUtils.closeStream(fileInputStream);
    }
}
