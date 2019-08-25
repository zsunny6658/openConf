# conf
本地配置注解获取

### 说明

#### version 1.2.1
1.新增对json配置文件的支持

#### version 1.2
1. 新增动态配置支持，实时修改配置无需重启应用
2. 新增@Dynamic注解，用于指示动态配置项
3. 增加对动态灵敏度支持，动态配置生效时长可控

#### version 1.1
新增了自定义配置文件，使用注解ConfSource(""),指定配置文件路径，默认为classpath:configer.properties

#### version 1.0
本项目主要用于帮助开发者可以更快更方便更轻量地导入配置文件中的配置项，本项目基本没有外部依赖，主要基于jdk实现。目前为1.0版本，支持yaml、xml和properties等格式的配置文件；读取配置的方式主要为注解方式，之后会推出更多方式。欢迎针对本项目提出isuue

### 用法
本项目用法非常简单。
##### 1.导入本组件
###### 方法1
目前已经发布1.1版本，开发者可以在release中下载jar包，并加入本地仓库中引用。
下载jar包后，使用maven命令
```
mvn install:install-file -Dfile=<filePath> -DgroupId=<group id> -DartifactId=<artifact id> -Dversion=<version> -Dpackaging=<package type>
```
将依赖加入本地仓库中，在需要使用的项目中，添加相应的依赖即可。
###### 方法2
开发者也可以选择导入代码，手动打包。
maven中增加插件设置：
```
<plugin>
    <artifactId>maven-assembly-plugin</artifactId>
    <executions>
        <execution>
            <phase>package</phase>
            <goals>
                <goal>single</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <descriptorRefs>
            <descriptorRef>jar-with-dependencies</descriptorRef>
        </descriptorRefs>
    </configuration>
</plugin>
```
执行maven命令：
```
mvn assembly:assembly
```
后根据方法1的步骤将打包出来的jar包加入本地库
##### 2.在需要进行配置读取的变量上添加相应注解
###### 2.1 变量配置
目前主要有两种注解@ConfPath和@SystemConfPath用于对变量进行配置，需要修饰静态变量，其value设置为需要读取的配置项如
```java
@ConfPath("server.port)
public static String port;

@SystemConfPath("system.conf.active")
public static String activeFile;
```
通常将业务参数通过@ConfPath读取（ConfPath无法读取系统配置项），系统配置项通过@SystemConfPath读取。
当前版本系统配置项只有system.conf.active和system.conf.listener前者用于在application.***文件中指定active的配置文件，如application-prod.xml;后者用于添加监听器，监听器需要实现ConfListner接口及其相对获取配置的前置方法和后置方法。
###### 2.2 类配置
另外，还支持对类进行整体配置，使用@ConfClass注解，可用@ConfClassPrefix指定固定前缀；
类中所有静态变量按照变量名寻找配置项，可用@ConfClassIgnore过滤变量不进行配置；
使用@ConfClassDefault进行变量默认值配置，若不存在该配置项则使用默认值。
示例：
```java
@ConfClass
@ConfClassPrefix("test.")
public class ExampleClass {
    private static String a;
    private static String b = "2";
    
    @ConfClassIgnore
    private static String c;
    @ConfClassDefault("ddddd")
    private static String d;
    @ConfClassAlias("d")
    private static String e;

    public static void print(){
        System.out.println("class-a:" + a);
        System.out.println("class-b:" + b);
        System.out.println("class-c:" + c);
        System.out.println("class-d:" + d);
    }
}
```
##### 3.入口逻辑实现
在启动类main方法中，MainProcessor.process()方法来实现配置获取逻辑。

### 快速上手
项目中Sample.java和SampleClass.java文件中为用注解接收配置值的例子；Test为启动类，在其main方法中调用了MainProcessor.process()将配置文件中的配置加入注解所对应的类变量中。
##### 1.属性配置读取
属性配置主要有@ConfPath和@SystemConfPath，分别用于指示业务项配置和系统项配置。
```java
@ConfPath("server.port)
public static String port;
@SystemConfPath("system.conf.active")
public static String activeFile;
```
##### 2.属性类配置
属性类配置的类必须用@ConfClass进行修饰，@ConfClassPrefix可以用于指示配置路径的前缀；属性名称则是其配置项的名称。
@ConfClassIgnore用于指示某个属性不用于接收配置内容；@ConfClassDefault用来指示某个属性的默认配置值，如果配置文件中存在相应的配置值则会覆盖默认值；@ConfClassAlias用于指示类变量别名。
```java
@ConfClass
@ConfClassPrefix("test")
public class ExampleClass {
    private static String a;
    private static String b = "2";
    @ConfClassIgnore
    private static String c;
    @ConfClassDefault("ddddd")
    private static String d;
    @ConfClassAlias("d")
    private static String e;
}
```
##### 3.监听器
###### 3.1 配置设置监听器
SampleConfProcessListener为示例process监听器，可增加配置设置的前置与后置处理，其实现了ConfProcessListener接口并实现了doBefore和doAfter方法。
```java
public class SampleConfProcessListener implements ConfProcessListner{
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
```java
system.conf.listener: com.sunny.TestListener
```
###### 3.2 配置源加载监听器
SampleSourceListener为示例配置源加载监听器，可前置和后置处理加载配置源。
```java
public class SampleSourceListener implements SourceListener{
    @Override
    public void doBefore() {
        System.out.println("test source listener before");
    }

    @Override
    public void doAfter() {
        System.out.println("test source listener after");
    }
}
```
此处只需要实现SourceListener接口即可，无需在配置中设置任何配置。
另外，会存在一个默认的配置源读取监听器DefaultConfSourceListner，它主要用于前置读取用户自定义配置源。
##### 4.自定义配置源
项目中Example类上添加了注解@ConfSource,用于指定添加默认配置之外的配置源，注意自定义添加的配置总是优先于默认配置，其他方法参考上面介绍
```java
@ConfSource("classpath: configer.properties")
//@ConfSource()
public class Example {
	@ConfPath("other.file.configer")
	private static String other;
 	....

}
```
##### 5.动态配置
支持对静态变量和配置类使用动态配置模式，加上@Dynamic注解即可，如：
```java
@Dynamic
@ConfPath("dynamic.test")
private static String t;
```
和：
```java
@Dynamic
@ConfClass
@ConfClassPrefix("test.")
public class ExampleClass {...}
```
在配置文件中可配置动态灵敏度，即动态配置生效最长时间，如：
```
System:
  dynamic:
    interval: 5
    unit: s
```
其中，System.dynamic.interval用于指示时长，System.dynamic.unit用于指示单位，目前支持h、m和s，分别表示小时、分钟和秒，默认时长为10秒，默认单位为秒。

### 注意事项
1. 配置读取注解的变量必须为静态变量
2. 配置文件放入resource中
3. 注意避开系统配置项，否则通过ConfPath注解将无法正常获取
4. 开启动态配置后，会占用一定系统资源，包括内存和CPU，慎用

### 敬请期待
接下来将更丰富本项目的功能，下一步将加入网络配置接口，敬请期待。

### 联系交流
author：Sunny, junehappylove

mail: zsunny@yeah.net | wjw.happy.love@163.com

