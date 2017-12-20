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