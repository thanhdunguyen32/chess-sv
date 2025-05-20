#!/bin/bash

# Clean temp files
rm -f /tmp/libnetty*

# Set system properties
export LD_LIBRARY_PATH=/usr/lib/x86_64-linux-gnu:$LD_LIBRARY_PATH

# Run application
java -server -XX:+UseG1GC -Xmx1024M \
-Dio.netty.tryReflectionSetAccessible=true \
-Dio.netty.noNative=true \
-Dio.netty.leakDetection.level=disabled \
-Dnetty.epoll.enabled=false \
-jar ../target/FEBS-Shiro-jht-2.0.jar --spring.profiles.active=prod --server.port=8001 &
