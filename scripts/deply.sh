#!/bin/bash

REPOSITORY=/home/ubuntu/app
cd $REPOSITORY

echo "> 프로젝트 빌드 시작"
./gradlew build -x test

echo "> 홈 디렉토리로 이동"
cd /home/ubuntu

echo "> 현재 구동중인 애플리케이션 pid 확인"
CURRENT_PID=$(pgrep -f Our-Beloved-KAIST)

if [ -z "$CURRENT_PID" ]; then
    echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
    echo "> kill -15 $CURRENT_PID"
    kill -15 $CURRENT_PID
    sleep 5
fi

echo "> 새 애플리케이션 배포"

# build 폴더의 jar 파일을 홈 디렉토리로 복사
cp $REPOSITORY/build/libs/Our-Beloved-KAIST-0.0.1-SNAPSHOT.jar .

echo "> 새 애플리케이션 실행"
nohup java -jar Our-Beloved-KAIST-0.0.1-SNAPSHOT.jar > application.log 2>&1 &