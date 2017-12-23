# Hadoop学习项目

## Hadoop集群环境

       4G                     4G                   4G
       1cpu                   1cpu                 1cpu
       master                 sleaves1             sleaves2
       10.41.5.10             10.41.5.11           10.41.5.12     
    hdfs
       namenode                    
       datanode               datanode             datanode
                                                   secondarynamenode
    yarn                                              
                              resourcemanager
       nodemanmager           nodemanger           nodemanager
    mapreduce
       Jobhistoryserver      
       
    HA
       NameNode               NameNode
       JournalNode            JournalNode          JournalNode
       DateNode               DateNode             DataNode
       
       