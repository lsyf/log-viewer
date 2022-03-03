#! /bin/bash

#======================================================================
# 项目重启shell脚本
# 先调用shutdown.sh停服
# 然后调用startup.sh启动服务
#
#======================================================================

# 项目名称
APPLICATION="@build.finalName@"


# 停服
echo stop ${APPLICATION} Application...
sh stop.sh

# 启动服务
echo start ${APPLICATION} Application...
sh start.sh