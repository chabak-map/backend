#!/bin/bash

REPOSITORY=/home/ubuntu/backend
cd $REPOSITORY

APP_NAME=chabak
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep '.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME


# =====================================
# 현재 구동 중인 application pid 확인
# =====================================
CURRENT_PID=$(pgrep -fl chabak | grep java | awk '{print $1}')

if [ -z "$CURRENT_PID" ]; then
    echo "NOT RUNNING"
else
    echo "> kill -9 $CURRENT_PID"
    kill -15 $CURRENT_PID
    sleep 5
fi

echo "> $JAR_PATH 배포"
#nohup java -jar $JAR_PATH > /dev/null 2> /dev/null < /dev/null &
#code deploy에 출력이 되기 때문에 nohup.out 파일을 사용해야 한다.
nohup java -jar -Dspring.profiles.active=prod $JAR_PATH > $REPOSITORY/nohup.out 2>&1 &
