#!/bin/bash

# 定义颜色
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
BLUE='\033[0;34m'
NC='\033[0m' # 无颜色

# 项目基础目录
BASE_DIR=$(cd "$(dirname "$0")/.." && pwd)
LIB_DIR="$BASE_DIR/lib"
CONFIG_DIR="$BASE_DIR/config"
LOG_DIR="$BASE_DIR/logs"
DEFAULT_CONFIG="$LIB_DIR/application.yml"   # 默认配置
CUSTOM_CONFIG="$CONFIG_DIR/application.yml" # 用户自定义配置
PID_FILE="$BASE_DIR/dbapi.pid"

# 检查并创建日志目录
if [ ! -d "$LOG_DIR" ]; then
    mkdir -p "$LOG_DIR"
    echo -e "${GREEN}Created log directory: $LOG_DIR${NC}"
fi

# 动态获取最新的 JAR 文件（匹配 dbapi-web 开头的文件）
get_latest_jar() {
    ls "$LIB_DIR"/dbapi-web-*.jar 2>/dev/null | sort -V | tail -n 1
}

JAR_PATH=$(get_latest_jar)

if [ -z "$JAR_PATH" ]; then
    echo -e "${RED}No JAR file found in $LIB_DIR. Please check your deployment.${NC}"
    exit 1
fi

echo -e "${BLUE}Using JAR file: $JAR_PATH${NC}"

# 确定使用的配置文件
FINAL_CONFIG="$DEFAULT_CONFIG,$CUSTOM_CONFIG"

if [ ! -f "$DEFAULT_CONFIG" ]; then
    echo -e "${RED}Default configuration not found: $DEFAULT_CONFIG${NC}"
    exit 1
fi

if [ -f "$CUSTOM_CONFIG" ]; then
    echo -e "${YELLOW}Using custom configuration: $CUSTOM_CONFIG${NC}"
else
    echo -e "${YELLOW}Custom configuration not found. Only using default configuration: $DEFAULT_CONFIG${NC}"
    FINAL_CONFIG="$DEFAULT_CONFIG"
fi

start() {
    if [ -f "$PID_FILE" ]; then
        if ps -p "$(cat "$PID_FILE")" > /dev/null 2>&1; then
            echo -e "${YELLOW}Service is already running. (PID: $(cat "$PID_FILE"))${NC}"
            exit 1
        else
            echo -e "${RED}PID file exists but service is not running. Cleaning up...${NC}"
            rm -f "$PID_FILE"
        fi
    fi

    echo -e "${BLUE}Starting service...${NC}"
    export LOG_HOME_PATH="$LOG_DIR"
    nohup java -jar "$JAR_PATH" --spring.config.location="$FINAL_CONFIG" > "$LOG_DIR/app.log" 2>&1 &
    echo $! > "$PID_FILE"
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}Service started successfully. (PID: $!)${NC}"
    else
        echo -e "${RED}Failed to start service.${NC}"
    fi
}

stop() {
    if [ -f "$PID_FILE" ]; then
        PID=$(cat "$PID_FILE")
        if ps -p $PID > /dev/null 2>&1; then
            echo -e "${BLUE}Stopping service with PID $PID...${NC}"
            kill $PID
            rm -f "$PID_FILE"
            echo -e "${GREEN}Service stopped successfully.${NC}"
        else
            echo -e "${RED}PID file found but service is not running. Removing stale PID file.${NC}"
            rm -f "$PID_FILE"
        fi
    fi

    # 通过进程名称查找并停止服务
    echo -e "${BLUE}Checking for additional running instances...${NC}"
    PIDS=$(ps -ef | grep "$JAR_PATH" | grep -v grep | awk '{print $2}')
    if [ -n "$PIDS" ]; then
        echo -e "${YELLOW}Stopping processes: $PIDS${NC}"
        kill $PIDS
        echo -e "${GREEN}All processes stopped.${NC}"
    else
        echo -e "${GREEN}No additional running instances found.${NC}"
    fi
}

status() {
    if [ -f "$PID_FILE" ]; then
        PID=$(cat "$PID_FILE")
        if ps -p $PID > /dev/null 2>&1; then
            echo -e "${GREEN}Service is running. (PID: $PID)${NC}"
        else
            echo -e "${RED}Service is not running, but PID file exists. (Stale PID file)${NC}"
        fi
    else
        echo -e "${RED}Service is not running.${NC}"
    fi

    # 检查通过进程名称的服务状态
    PIDS=$(ps -ef | grep "$JAR_PATH" | grep -v grep | awk '{print $2}')
    if [ -n "$PIDS" ]; then
        echo -e "${YELLOW}Additional running instances detected: $PIDS${NC}"
    fi
}

restart() {
    echo -e "${BLUE}Restarting service...${NC}"
    stop
    start
}

# 根据输入的参数进行判断
case "$1" in
    start)
        start
        ;;
    stop)
        stop
        ;;
    status)
        status
        ;;
    restart)
        restart
        ;;
    *)
        echo -e "${YELLOW}Usage: $0 {start|stop|status|restart}${NC}"
        exit 1
        ;;
esac
