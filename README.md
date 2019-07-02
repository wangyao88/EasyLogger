**一 简介**
    easy-logger是一款简单高效的日志插件，支持按文件大小。按天分割日志文件，也可定时将日志落盘。核心采用两个队列来协同完成日志接收和落盘的工作。名为
master的队列用来接收日志，名为replica的队列用来落盘，落盘完成后，切换双方的身份（master变为replica，replica变为master），可承受每秒十万级别的日
志写入，并完成落盘。

**二 easy-logger系统配置说明**
String  masterQueue             主队列名字，默认为master
String  replicaQueue            备份队列名字，默认为replica
int     queueSizeThreshold      工作队列落盘阀值，默认为10000
int     flushInterval           定时器定时落盘日志的时间间隔，单位秒， 默认为10秒
double  fileMaxSize             日志文件拆分阀值 默认为100M
String  infoFilePath            日志级别为info的日志文件名称，默认为info
String  debugFilePath           日志级别为debug的日志文件名称，默认为debug
String  warnFilePath            日志级别为warn的日志文件名称，默认为warn
String  errorFilePath           日志级别为error的日志文件名称，默认为error
String  logFilePath             在不按日志级别拆分日志文件的情况下，日志文件的日志名，默认为log
String  logPreffix              日志文件路径的前缀，默认为./
String  logSuffix               日志文件的后缀，默认为.log
boolean spliteLevel             是否按日志级别拆分日志
boolean needWriteInfoToConsole  是否将info及级别的日志打印到控制台，默认为true
boolean needWriteDebugToConsole 是否将debug及级别的日志打印到控制台，默认为false
boolean needWriteWarnToConsole  是否将warn及级别的日志打印到控制台，默认为false
boolean needWriteErrorToConsole 是否将error及级别的日志打印到控制台，默认为true