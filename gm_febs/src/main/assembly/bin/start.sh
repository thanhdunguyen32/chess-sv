#!/bin/bash
JAVA_OPTS="-server -Xms1024m -Xmx2048m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=256m"

# Đường dẫn tuyệt đối đến thư mục gốc
BASE_DIR=$(cd "$(dirname "$0")"/.. || exit; pwd)

echo "Starting application..."
echo "BASE_DIR: $BASE_DIR"

# Chạy Spring Boot JAR trực tiếp
java $JAVA_OPTS -jar "$BASE_DIR/FEBS-Shiro-jht-2.0.jar" 