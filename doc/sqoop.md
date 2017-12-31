# Sqoop笔记

# sqoop安装
    统一使用CHD-5.13.1版本
    配置文件：sqoop-env.sh
        
        #Set path to where bin/hadoop is available
        export HADOOP_COMMON_HOME=/opt/chd-5.13.1/hadoop-2.6.0-cdh5.13.1
        
        #Set path to where hadoop-*-core.jar is available
        export HADOOP_MAPRED_HOME=/opt/chd-5.13.1/hadoop-2.6.0-cdh5.13.1
        
        #set the path to where bin/hbase is available
        #export HBASE_HOME=
        
        #Set the path to where bin/hive is available
        export HIVE_HOME=/opt/chd-5.13.1/hive-1.1.0-cdh5.13.1
        
        #Set the path for where zookeper config dir is
        export ZOOCFGDIR=/opt/chd-5.13.1/zookeeper-3.4.5-cdh5.13.1/conf
        
        
# 基本应用
    读取mysql数据库列表：
        ./sqoop list-databases \
        --connect jdbc:mysql://master:3306 \
        --username root \
        --password 123abc
        
    导入mysql中一张表到hdfs：
        ./sqoop import \
        --connect jdbc:mysql://master:3306/mysql \
        --username root \
        --password 123abc \
        --table user
        
    指定导入的hdfs路径
        ./sqoop import \
        --connect jdbc:mysql://master:3306/mysql \
        --username root \
        --password 123abc \
        --table user \
        --target-dir /user/ydcun/mysql/ \
        --num-mappers 1
        
    导入parquet格式文件
        ./sqoop import \
        --connect jdbc:mysql://master:3306/mysql \
        --username root \
        --password 123abc \
        --table user \
        --target-dir /user/ydcun/sqoop/mysql_parquet/ \
        --num-mappers 1 \
        --as-parquetfile
        
    通过查询语句导入：
        ./sqoop import \
        --connect jdbc:mysql://master:3306/mysql \
        --username root \
        --password 123abc \
        --query 'select * from user where $CONDITIONS' \
        --target-dir /user/ydcun/sqoop/mysql_query_parquet/ \
        --num-mappers 1 \
        --as-parquetfile