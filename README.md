# log-viewer

这是一个用于 优化分布式调用链日志 展示的web服务。 

原理是：
- 存储日志使用elasticsearch
- 分布式跟踪中间件使用skywalking
- 对skywalking agent做二次开发，使微服务的日志中 **全局tid**  转变为  **全局tid|父线程tid|线程tid**。  参考：https://github.com/lsyf/skywalking/commit/249c587d1ede7f8ba5f1cf1dab64954ac3c48df0
- 而当前的web服务 用于梳理日志，使其分层展示。

效果如下

![image](https://user-images.githubusercontent.com/13670487/156492344-2d8273fb-91fe-4eff-ac64-1f5ee1131a91.png)

![image](https://user-images.githubusercontent.com/13670487/156493520-266ba84c-a8b7-4813-92ed-0e44c4a3c7b4.png)

![image](https://user-images.githubusercontent.com/13670487/156492466-71ff979c-0d51-404f-9a0f-40b23a6fc508.png)

![image](https://user-images.githubusercontent.com/13670487/156492629-3860f404-9f0d-4d05-aa0d-043695c45dc8.png)



