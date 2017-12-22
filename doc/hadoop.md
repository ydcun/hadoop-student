[TOC]

#hadoop学习笔记

## Hadoop安装
    用户自定义配置文件(etc/hadoop)：
    	core-site.xml
    	hdfs-site.xml
    	yarn-site.xml
    	mapred-site.xml
    默认配置文件(在各模块jar包里):
    	core-default.xml
    	hdfs-default.xml
    	yarn-default.xml
        mapred-default.xml

## Hadoop启动
    单独启动：
        HDFS
            sbin/hadoop-daemon.sh start|stop namenode|datanode|secodarynamenode
   
    	YARN
    		sbin/yarn-daemon.sh start|stop resourcemanager|nodemanager
    	mapreduce
    		sibin/mr-jobhistory-daemon.sh start|stop historyserver
    		
    模块启动：
        HDFS
            sbin/start-dfs.sh
            sbin/stop-dfs.sh
        YARN
            sbin/start-yarn.sh
            sbin/stop-yarn.sh
    		
    全部启动：
        sbin/start-all.sh
        sbin/stop-all.sh


## 各个组件地址配置
    HDSF
        NameNode(core-site.xml)
             <property>
                <name>fs.defaultFS</name>
                <value>hdfs://master:8020</value>
             </property>
                 
        DataNode(slaves)
            master
                
        secod(hdfs-site.xml)
            <property>
                <name>dfs.namenode.secondary.http-address</name>
                <value>master:50090</value>
             </property>
    			 
    Yarn
    	ResourceManager(yarn-site.xml)
            <property>
                <name>yarn.resourcemanager.hostname</name>
                <value>master</value>
             </property>
    			
    	NodeManager(slaves)
            master
    	
    	
    Mapreduce 
        HistoryServer(mapred-site.xml)
    		<property>
                <name>mapreduce.jobhistory.address</name>
                <value>master:10020</value>
    		</property>
            <property>
                <name>mapreduce.jobhistory.webapp.address</name>
                <value>master:19888</value>
            </property>

## SecodaryNameNode

## ResourceManager

## Application master

## Container

## 安全模式
    作用：
    	• 等待Datanode上传black  report
    	• 上报blackes/total blasces =99.9999%。30秒后Safemode is off。
        ps：可以在master:50090的Summary中看到描述和状态变化
    查看安全模式状态：
        hdfs dfsadmin -safemode get  	查看安全模式状态  
        Hdfs dfsadmin -safemode enter	进入安全模式
        Hdfs dfsadmin -safemode leave	离开安全模式
## Hadoop权限
    设置如下环境变量，可以使集群识别操作权限为root
        export HADOOP_USER_NAME=root
        
## 查看块报告
    hdfs dfsadmin -report
    与master:50090中显示的一样
    
## MapReduce过程
    * step 1:
        input
            InputFormat
                * 读取数据
                * 转换<key,value>
            FileInputFormat
                * TextInputFormat
                
    * step 2:
        map
            ModuleMapper
                map(KEYIN,VALUEIN,KEYOUT,VALUEOUT>
            默认情况：  
                KEYIN默认：偏移量
                VALUEIN默认：TEXT
    * step 3
        shuffle
            * process
                * map, output<key,value>
                    * 开始在memory内存里进行
                    * spill，内存不够了就溢写到磁盘中，可能会产生好多文件
                        * 分区partitioner。分配到不同的reduc中运行
                        * 排序
                    * 很多小文件，spill
                        * 合并，merge
                        * 排序
                        合并成一个大文件 -> Map Task运行在机器的本地磁盘。
                    * copy
                        Reduce Task, 会到Map Task运行的机器中拷贝要处理的数据
                    * 合并，merge，排序
                    * 分组 group
                        将相同的key的value放到一起
        PS:
            * 分区
                partitioner 
            * 排序
                sort
            * copy，用户无法干涉与修改
                拷贝               
            * 分组
                group
            * 压缩
                compress 可以自己设置修改节省空间
                在mapred-site.xml中设置  
                    mapreduce.map.output.compress  默认false
                也可以在Configuration中设置
                    conf.set("mapreduce.map.output.compress","true");
                    conf.set("mapreduce.map.output.compress.codec","org.apache.hadoop.io.compress.SnappyCodec");

            * combiner 组合
                Map Task端的Reduce
    * step 4
        reduce
            reduce（KEYIN,VALUEIN,KEYOUT,VALUEOUT>
            map输出的key，value数据类型与reduce输入的key，value一致
            
    * step 5
        output 
            OutputFormat
            
        FileOutputFormat   
             TextOutputFomart
              每个key，value对输出一行，key与value中间的分隔符是\t.默认调用key，value的toString方法
        并行的3个Map在执行
            Map-01
            
            Map-02
            
            Map-03
        两个Reduce处理
            Reduce-01
                a-zA-Z
            Reduce-02
                other  
