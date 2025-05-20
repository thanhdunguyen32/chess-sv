#!/bin/bash
kill -15 `ps -ef|grep "$PWD" | grep -v "grep"|awk '{print $2} '`
