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
目前主要有两种注解@ConfPath和@SystemConfPath，需要修饰静态变量，其value设置为需要读取的配置项如
```
@ConfPath("server.port)
public static String port;

@SystemConfPath("system.conf.active")
public static String activeFile;
```
通常将业务参数通过@ConfPath读取（ConfPath无法读取系统配置项），系统配置项通过@SystemConfPath读取。
当前版本系统配置项只有system.conf.active和system.conf.listener前者用于在application.***文件中指定active的配置文件，如application-prod.xml;后者用于添加监听器，监听器需要实现ConfListner接口及其相对获取配置的前置方法和后置方法。
##### 3.入口逻辑实现
在启动类main方法中，MainProcessor.process()方法来实现配置获取逻辑。

### 快速上手
项目中Example.java和ExampleClass.java文件中为用注解接收配置值的例子；Test为启动类，在其main方法中调用了MainProcessor.process()将配置文件中的配置加入注解所对应的类变量中。
##### 1.属性配置读取
属性配置主要有@ConfPath和@SystemConfPath，分别用于指示业务项配置和系统项配置。
```
@ConfPath("server.port)
public static String port;
@SystemConfPath("system.conf.active")
public static String activeFile;
```
##### 2.属性类配置
属性类配置的类必须用@ConfClass进行修饰，@ConfClassPrefix可以用于指示配置路径的前缀；属性名称则是其配置项的名称。
@ConfClassIgnore用于指示某个属性不用于接收配置内容；@ConfClassDefault用来指示某个属性的默认配置值，如果配置文件中存在相应的配置值则会覆盖默认值。
```
@ConfClass
@ConfClassPrefix("test")
public class ExampleClass {
    private static String a;
    private static String b = "2";
    @ConfClassIgnore
    private static String c;
    @ConfClassDefault("ddddd")
    private static String d;
}
```
##### 3.监听器
TestListener为示例监听器，其实现了ConfListener接口并实现了doBefore和doAfter方法。
```
public class TestListener implements ConfListner{
    @Override
    public void doBefore() {
        System.out.println("before");
    }
    @Override
    public void doAfter() {
        System.out.println("after");
    }
}
```
另外需要在配置文件中指示类名称：
```
system.conf.listener: com.sunny.TestListener
```

### 注意事项
1. 配置读取注解的变量必须为静态变量
2. 配置文件放入resource中
3. 注意避开系统配置项，否则通过ConfPath注解将无法正常获取

### 敬请期待
接下来将更丰富本项目的功能，增加如类配置读取等功能，敬请期待。

### 联系交流
author：Sunny

mail: zsunny@yeah.net

