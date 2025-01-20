#!/bin/bash

# 기존 jar 파일 백업
if [ -e /home/ubuntu/Our-Beloved-KAIST-0.0.1-SNAPSHOT.jar ]; then
    mv /home/ubuntu/Our-Beloved-KAIST-0.0.1-SNAPSHOT.jar /home/ubuntu/Our-Beloved-KAIST-0.0.1-SNAPSHOT.jar.backup
fi