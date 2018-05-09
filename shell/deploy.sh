#!/bin/sh
dir=/opt/hadoop/data
echo "deploy jar"
scp ../target/hadoop-student-1.0-SNAPSHOT.jar root@master:/opt/install/hadoop-2.7.5/mywork/jars/
echo "deploy run.sh"
scp run.sh root@master:${dir}/jars/
echo "change authority"
ssh root@master "chmod 755 ${dir}/jars/run.sh"
echo "start run.sh"
ssh root@master "${dir}/jars/run.sh"