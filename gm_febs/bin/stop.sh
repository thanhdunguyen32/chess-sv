#!/bin/bash
kill -15 `ps -ef|grep "FEBS-Shiro-2.0.jar" | grep -v "grep"|awk '{print $2} '`
