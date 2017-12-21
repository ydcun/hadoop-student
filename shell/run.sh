#!/bin/sh
echo "add jar to classpath"
export HADOOP_CLASSPATH=/opt/install/hadoop-2.7.5/mywork/jars/hadoop-student-1.0-SNAPSHOT.jar
export PATH=$PATH:/opt/install/hadoop-2.7.5/bin
echo "run hadoop task"
hadoop com.ydcun.hadoop.student.mapreduce.MyWordCountMapReduce  /user/ydcun/mapreduce/wordcount/input/  /user/ydcun/mapreduce/wordcount/output5/