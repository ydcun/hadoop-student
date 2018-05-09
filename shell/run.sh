#!/bin/sh

dir=/opt/hadoop/data
echo "add jar to classpath"
export HADOOP_CLASSPATH=${dir}/jars/hadoop-student-1.0-SNAPSHOT.jar
export PATH=$PATH:/root/cdh/hadoop-2.6.0-cdh5.14.0//bin
echo "run hadoop task"
hadoop com.ydcun.hadoop.student.mapreduce.MyWordCountMapReduce  /user/ydcun/  /user/ydcun/output2/