#!/bin/bash
java -Xmx512M -server -XX:+UseParallelGC -jar $PWD/login-server-1.1.jar &

#debug模式启动
#java -server -Xms1024M -Xmx2048M -Xmn768M -XX:PermSize=32m -XX:MaxPermSize=64m -Djava.rmi.server.hostname=192.168.75.8 -Dcom.sun.management.jmxremote.port=18999 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -XX:+UseConcMarkSweepGC -XX:+UseCMSCompactAtFullCollection  -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -XX:+CMSParallelRemarkEnabled -XX:SoftRefLRUPolicyMSPerMB=0 -XX:+CMSClassUnloadingEnabled -XX:+DisableExplicitGC -XX:SurvivorRatio=20 -jar $PWD/idle-login-1.0.2.jar &