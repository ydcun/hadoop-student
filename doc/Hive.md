# Hive
    * 处理的数据存储在Hadoop中
    * 分析底层实现了MapReduce
    * 执行运行在yarn中
    
    是什么：
        * FaceBook开源的解决海量结构化日志数据统计
        * Hiver是基于Hadoop的数据仓库工具，将结构化数据文件映射成一张表
        
        * 建立在Hadoop之上使用HQL查询接口
        * 使用HDFS存储数据
        * 使用MapReduce计算。本质是将HQL转换成MapReducu程序
        * 灵活性扩展性比较好：支持UDF
        
        * 适合离线数据分析
    
## RDBMS
    表的概念：与列对应起来
        create table{
            
        }
        
## Hive 安装
    * 修改hive-env.sh配置文件(也可以在环境变量中配置)
        export HADOOP_HOME
        export HIVE_HOME
    
    * 初始化default数据库
        schematool -dbType derby -initSchema
        
        ps:默认在内存数据库中存储matastore
    * hive 进入控制台
    * show databases;
    * use default;
    * show tables;
    * set; 查看当前所有配置
    
    bin/hdfs dfs -mkdir -p /user/hive/warehouse
    bin/hdfs dfs -chmod u+x /user/hive/warehouse
    
## 简单实例
    * 创建表
        create table students(id string,name string) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t';
        delimited
        fields
    * student.txt
        10001   ydcun
        1002    joa
    * 导入数据
        load data local inpath '/opt/app/hive-2.3.2/student.txt' into table students;
        
    * 查看表信息
        desc formatted tablename
        desc tablename
        desc function extended functionname
        desc function functionname
        
## Metastore三种模式
    * 内嵌Derby方式
        schematool -dbType derby -initSchema
    * Local方式
        schematool -dbType mysql -initSchema
        <property>
                <name>hive.metastore.local</name>
                <value>local</value>
        </property>
        <property>
                <name>javax.jdo.option.ConnectionDriverName</name>
                <value>com.mysql.jdbc.Driver</value>
        </property>
        <property>
                <name>javax.jdo.option.ConnectionUserName</name>
                <value>root</value>
        </property>
        <property>
                <name>javax.jdo.option.ConnectionPassword</name>
                <value>123abc</value>
        </property>
        <property>
                <name>javax.jdo.option.ConnectionURL</name>
                        <value>jdbc:mysql://master:3306/matestore?createDatabaseIfNotExist=true</value>
        </property>
    * Remote方式
    
  ## Hive客户端打印当前数据和输出表头
        <property>
                <name>hive.cli.print.header</name>
                <value>true</value>
        </property> 
        <property>
                <name>hive.cli.print.current.db</name>
                <value>true</value>
        </property>
        
        启动hive客户端🈴️时候设置参数
            例如：hive --hiveconf hive.root.logger=INFO,console
    
  ## metastore
        默认的default不会在warehouse里创建文件夹，default中的表直接在warehouse中创建同名文件   
        
  ## Hive交互命令
         -d,--define <key=value>          Variable substitution to apply to Hive
                                          commands. e.g. -d A=B or --define A=B
            --database <databasename>     Specify the database to use
         -e <quoted-query-string>         SQL from command line
         -f <filename>                    SQL from files
         -H,--help                        Print help information
            --hiveconf <property=value>   Use value for given property
            --hivevar <key=value>         Variable substitution to apply to Hive
                                          commands. e.g. --hivevar A=B
         -i <filename>                    Initialization SQL file
         -S,--silent                      Silent mode in interactive shell
         -v,--verbose                     Verbose mode (echo executed SQL to the
                                          console)
                                          
         hive -e 'select * from db_hive.students;'   -e -f不能同时使用
         hive -f 'sqlscript.sql'  
         hive -i 'init.sql'     与用户自定义udf配合使用 -i可以与任意参数结合使用       
         
         
## Hive 命令
    create table is not exists tablename(id int comment '', name string)
    
    create table tablename2 as select * from tablename;
       
    create table tablename like tablename;
    
    alter table tablename rename to name_rename
    
    * 创建外部表，会将指定的文件夹内的文件都加载到外部表中
        create EXTERNAL table student_ext(
        id string,
        name string
        )
        ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t'
        LOCATION '/user/ydcun/student/'
        
    * 分区表，指定的目录，下面的子目录即为分区
        create EXTERNAL table student_part(
        id string,
        name string
        )
        PARTITIONED BY (month string)
        ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t'
        LOCATION '/user/ydcun/student_part/'
        
        load data local inpath '/opt/app/hive-2.3.2/student.txt' into table student_part partition(month='201712');
        
        select * from student_part where month='201712'; 
        
        ps:
            如果直接在dfs创建文件夹并创建以分区字段命名的子文件，因没有记录分区信息使得表中查不到数据
            方案1：执行修复：
                msck repair table tablename;
            方案2：
                alter table tablename partition(month='201711')
                
            查询分区数：
                show partitions tablename;
                
 ## 文件导出
    insert overwrite local directory '/user/ydcun/hive_exp_student'
    select * from students;
    insert overwrite local directory '/user/ydcun/hive_exp_student'
    ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' COLLECTION ITEMS TERMINATED BY '\n'
    select * from students_ext;
    
    hive -e 'select * from student;' > /opt/app/hive-2.3.2/exp.txt
    
    ps：
        local 倒到本地
        
 ## 数据处理流程
    * load          E
    * select python T
    * sub select    L        
    
## DML
    * 查看所有函数
        show functions;
    * 查看函数说明
        desc function max;
        
## exprot/import
    EXPORT TABLE student_ext TO '/user/ydcun/export/student_ext'; 
    IMPORT TABLE student_ext_imp FROM '/user/ydcun/export/student_ext'; 
    
## 排序
    order by 全局排序
    
    sort by  reduce内排序
        set mapreduce.job.reduces = 3;  //生成3个 reduce 输出
        select * from student_ext sort by id;    //会有三个reduce执行
        
    distribute by   类似map中的分区结合sort by进行使用
        select * from student_ext distribute by name sort by id;
        ps：distibute by 必须在sort by之前
        
    cluster by  sort by 与 distribute by 的组合，当这两个排序的字段相同的时候用clustger by       

## UDF（user definition function）用户自定义函数
    show functions;  查看自带函数
    desc extended function max;
    
    * UDF  一进一出
    * UDAF 多进一出  count
    * UDTF 一进多处  lateral 
    
    使用过程：
        * 编写函数
            <dependency>
              <groupId>org.apache.hive</groupId>
              <artifactId>hive-jdbc</artifactId>
              <version>${hiver.version}</version>
            </dependency>
            <dependency>
              <groupId>org.apache.hive</groupId>
              <artifactId>hive-exec</artifactId>
              <version>${hiver.version }</version>
            </dependency>
        * 打成jar包
        * hive中  add jar '/****/**.jar'
        * create temporary function my_lower as "com.ydcun.hadoop.student.hive.udf.LowarFunction"
        * show function 中就就可以看到 my_lower 这个函数
        
        方法2：
            CREATE FUNCTION myfunc AS 'myclass' USING JAR 'hdfs:///path/to/jar';
            
            
 ## hiverservice2
    bin/hiveserver2
    ps -ef | grep java
    * beeline 链接
        方法1
            bin/beeline
            !connect jdbc:hive2://master:10000 root root
            
                ps：User: root is not allowed to impersonate root这个错误解决方法
                    修改core-site.xml文件：
                        root用户可以代理其它任意用户，需要重启hadoop
                        <property>
                          <name>hadoop.proxyuser.root.hosts</name>
                          <value>*</value>
                         </property>
                         <property>
                          <name>hadoop.proxyuser.root.groups</name>
                          <value>*</value>
                        </property>
                        
        方法2 直接登录
            bin/beeline -u jdbc:hive2://master:10000/default
        
        退出：
            !quit
   
## 压缩
    * snappy 
        先安装snappy   
        编译hadoop源码，添加snappy可选项
            mvn package -Pdist,native,src -DskipTests -Dtar -Drequire.snappy
            
            编译的库在target/hadoop*/lib/native
        检查方法：
        $ hadoop checknative
        17/12/27 13:16:47 WARN bzip2.Bzip2Factory: Failed to load/initialize native-bzip2 library system-native, will use pure-Java version
        17/12/27 13:16:47 INFO zlib.ZlibFactory: Successfully loaded & initialized native-zlib library
        Native library checking:
        hadoop:  true /opt/app/hadoop-2.7.5/lib/native/libhadoop.so.1.0.0
        zlib:    true /lib64/libz.so.1
        snappy:  false
        lz4:     true revision:99
        bzip2:   false
        openssl: false Cannot load libcrypto.so (libcrypto.so: 无法打开共享对象文件: 没有那个文件或目录)!

        查看文件或目录大小：
            hdfs dfs -du -h /user/ydcun/hive
            
            
## hive优化
    * 不走mapreduce
        hive.fetch.task.comversion     minimal|mores   
        minimal：select * | 分区表 | limit
        mores ：minimal ，时间戳，虚拟列
        
    * 大表拆分
        create table   。。。。 as  select 。。。。
    
    * 外部表，分区表
        多级分区
    
    * 数据
        存储：textfile，orcfile，parquet
        压缩：snappy
        
    * SQL语句
        join
            * common| Shuffle| reduce join
                链接发生的阶段是在reduce阶段，大表对大表的时候比较适合
            * map join
                链接发生在map阶段，大表对小表的时候比较适合
                大表数据从稳重读取，
                小表数据从内存中加载
                DistributedCache进行缓存小表内容
                set hive.auto.convert.join=true;
            * SMB join  
                Sort-Merge-Bucket Join
                    join的表通过相同的字段进行sort后分区，放到不同的桶中。
                    针对相同的桶进行单独join即可，解决大表对大表的问题
                set hive.auto.convert.sortmerge.join=true;
                
                
    * hive 执行计划
    
        EXPLAIN [EXTENDED|DEPENDENCY|AUTHORIZATION] query
        
            explain select * from student_ext;
        
    * 并行执行
        hive.exec.parallel=ture;
        hive.exec.parallel.thread.number=8     //建议不要超过20
        job1 a join b   aa
        job2 c join d   cc
        job3 aa join cc
        job1与job2可以并行执行
        
    * JVM重用
        map task/reduce task都需要JVM环境，JVM重用节省JVM启用时间
        mapreduce.job.jvm.numtasks
     
    * Reduce数目
        默认是快的数量决定的。
        mapreduce.job.reduces=1
        
    * 推测执行
        hive.mapred.reduce.tasks.speculative.execution=true;
        mapreduce.map.speculative=true;
        mapreduce.reduce.speculative=true;
    * Map数目
        hive.merge.size.per.task=256000000  参考块的大小
        
    * 企业优化动态分区
        分区（mouth，day）
        ydcun_log_ip
            /mouth=2017-11/day=25
        加载数据：
            load data path '' into table ydcun_log_ip partition(month='2017-1',day='01') 
        hive.exec.dynamic.partition=true;
        hive.exec.dynamic.partition.mode=strict    至少有一个分区是静态的
        hive.exec.dynamic.partition.pernode=100     每个mapper或reducer创建的最大动态分区数
        hive.exec.dynamic.partitions=100            一个动态分区所创建的最大动态分区数
        hive.exec.max.created.files=10000           全局可创建的最大文件数
        
        set hive.mapred.mode=nonstrict|strict;   
        strict是严格模式：分区表where中不指定分区查询不到
                        对于order by必须使用limit
                        笛卡尔积不使用on而使用where的也将禁止
                        
                        
## 实战
    创建表：使用正则的方式
        CREATE TABLE serde_regex(
          host STRING,
          identity STRING,
          user STRING,
          time STRING,
          request STRING,
          status STRING,
          size STRING,
          referer STRING,
          agent STRING)
        ROW FORMAT SERDE 'org.apache.hadoop.hive.contrib.serde2.RegexSerDe'
        WITH SERDEPROPERTIES (
          "input.regex" = "([^ ]*) ([^ ]*) ([^ ]*) (-|\\[[^\\]]*\\]) ([^ \"]*|\"[^\"]*\") (-|[0-9]*) (-|[0-9]*)(?: ([^ \"]*|\"[^\"]*\") ([^ \"]*|\"[^\"]*\"))?",
          "output.format.string" = "%1$s %2$s %3$s %4$s %5$s %6$s %7$s %8$s %9$s"
        )
        STORED AS TEXTFILE;
        
        