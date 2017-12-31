# Zookeeper笔记

## Zookeeeper安装

    * 解压压缩包
    * 配置conf/zoo.cfg
        # The number of milliseconds of each tick
        tickTime=2000
        # The number of ticks that the initial 
        # synchronization phase can take
        initLimit=10
        # The number of ticks that can pass between 
        # sending a request and getting an acknowledgement
        syncLimit=5
        # the directory where the snapshot is stored.
        dataDir=/opt/app/zookeeper-3.3.6/zkdata
        # the port at which the clients will connect
        clientPort=2181
        
        server.1=master:2888:3888
        server.2=slaves1:2888:3888
        server.3=slaves2:2888:3888
        
    * 在三个服务器的dataDir目录下创建myid文件保存文件编号
 
    ps：
        server.1=master:2888:3888
        2888是与leader通讯接口
        3888是选举leader端口
    
    启动：
        在各个节点分别执行：
        bin/zkServer.sh start
## 常用命令
    bin/zkCli.sh
        create /test "test-date"
        get /test
