处理异常: 
从源头减少异常：
忽略不必要的异常：允许一定的容错、网络/rpc调用可以重试、对业务不产生影响
异常链进行定位：抛出异常时带上原来的异常信息：把cause传递下去
其他规范：不使用大段try、清理释放资源、尽早暴露异常、异常发生时尽量避免影响系统的状态
