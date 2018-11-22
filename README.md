# conf
本地配置注解获取

### 说明
本项目主要用于帮助开发者可以更快更方便更轻量地导入配置文件中的配置项，本项目基本没有外部依赖，主要基于jdk实现。目前为1.0版本，支持yaml、xml和properties等格式的配置文件；读取配置的方式主要为注解方式，之后会推出更多方式。欢迎针对本项目提出isuue

### 用法
本项目用法非常简单。
##### 1.导入本组件
目前已经发布1.0版本，开发者可以在release中下载jar包，并加入本地仓库中引用。
下载jar包后，使用maven命令
```
mvn install:install-file -Dfile=<filePath> -DgroupId=<group id> -DartifactId=<artifact id> -Dversion=<version> -Dpackaging=<package type>
```
将依赖加入本地仓库中，在需要使用的项目中，添加相应的依赖即可。
##### 2.在需要进行配置读取的变量上添加相应注解
目前主要有两种注解@ConfPath和@SystemConfPath，其value设置为需要读取的配置项如
```
@ConfPath("server.port)
```
通常将业务参数通过@ConfPath读取（ConfPath无法读取系统配置项），系统配置项通过@SystemConfPath读取。
当前版本系统配置项只有system.conf.active和system.conf.listener前者用于在application.***文件中指定active的配置文件，如application-prod.xml;后者用于添加监听器，监听器需要实现ConfListner接口及其相对获取配置的前置方法和后置方法。
##### 3.入口逻辑实现
在启动类main方法中，调用ConfStarter.start()方法来实现配置获取逻辑。
### 注意事项
1. 配置读取注解的变量必须为静态变量
2. 配置文件放入resource中
3. 注意避开系统配置项，否则通过ConfPath注解将无法正常获取
### 联系交流
author：Sunny

mail: zsunny@yeah.net

