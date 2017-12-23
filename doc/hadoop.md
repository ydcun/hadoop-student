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
## NameNode
    存储文件元数据
        文件名 路径  所属者  所属组  副本数   。。。。

## DateNode
    默认大小128M
    Block方式存储，存放在各个机器的本地磁盘
    本地磁盘路径配置：
        <property>
            <name>dfs.datanode.data.dir</name>
            <value>file://${hadoop.tmp.dir}/dfs/data</value>
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

## 时间同步ntp
    rpm -qa | grep ntp  
    vim /etc/ntp.conf
        修改1，打开这个注释，运行同步该时间的网段
            # Hosts on local network are less restricted.
            restrict 10.41.5.0 mask 255.255.255.0 nomodify notrap
        修改2，注释掉自动链接的时间服务器（自己可以改时间）
            # Use public servers from the pool.ntp.org project.
            # Please consider joining the pool (http://www.pool.ntp.org/join.html).
            #server 0.centos.pool.ntp.org iburst
            #server 1.centos.pool.ntp.org iburst
            #server 2.centos.pool.ntp.org iburst
            #server 3.centos.pool.ntp.org iburst
        修改3，
            # 外部时间服务器不可用时，以本地时间作为时间服务
            server  127.127.1.0     # local clock
            fudge   127.127.1.0 stratum 10
    vim /etc/sysconfig/ntpd
        # Drop root to id 'ntp:ntp' by default.
        SYNC_HWCLOCK=yes
        OPTIONS="-u ntp:ntp -p /var/run/ntpd.pid -g"
        
    ====================================================================
    客户端：10分钟自动同步
        crontab -e
        0-59/10 * * * * /usr/sbin/ntpdate master
        
        
        
## Haodoop HA 
    Hadoop 在2.0 以前容易出现单点故障（SPOS）
    提出：
        namenode active
        namenode standby
        
    * share edits文件
        JournalNode
    * Namenode 配置两个一个active 一个standby
    * client
        proxy 代理选择active状态的Namenode
    * fence
        确保同一时刻只有一个namenode是active对外提供服务
       
       
       
       
    * 手动故障转移
        hadoop haadmin -help    
            -transitionToActive nn1
            -transitionToStandby nn1
    * 自动故障转移利用Zookeeper
        * 启动后选举一个active
        * 监控故障 
            ZKFC：故障转移监控器
            
## HDFS Federation（连门）
    多个NameNode负责不同的业务
    NameNode        NameNode        NameNode
    log             machine         业务数据
    
    数据保存到所有的DataNode中（共用）
    
    
## 文件系统快照（HDFS Snapshots）

## HDFS集中式缓存（Centralized Cache Management）
    2.3.0版本后，DataNode的数据缓存到内存中
    
## 分布式拷贝（Distributed Copy）
    作用：多个集群数据间迁移
    
    //不同版本间拷贝
    hadoop distcp -i hftp://sourceFS:50070/src hdfs://destFS:8020/dest
    
## yarn HA
    
## ResourceManager restart
## NodedManager restart