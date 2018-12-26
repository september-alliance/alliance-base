# alliance-base
Alliance base library (jar)

九月联盟项目的公共基础jar包，基于springboot搭建，目前的版本是springboot 2.1.0.release。

base项目目前包括以下几个部分

## alliace-core
   公共基础类，常量，帮助类，对第三方依赖最少的又常用，有用的类放在这里。
## alliace-smartdao
   基于mybatis的通用dao，目前的mybatis版本是3.4.6。smartdao旨在极度简化dao层，对使用着而言，dao被弱化成只有一个类--CommonDao,单表的增删改查不必再
   需要mapper.xml文件，不必再写sql语句。
   
### smartdao的特性
   1. 单表免sql
   2. 集成分页插件，sql语句无侵入
   3. 多数据源配置，编程式动态切换数据源
   4. 数据源读写分离，每个数据源都可以配置成read , wirte , read/write , 事务只走写库。
   5. 内置tomcat数据库连接池
   6. sql条件非空智能判断----mapper中不必再写mybatis的<if test>判断。
   7. 分表正在规划中

## alliance-simpleweb
   1. 集成了thymeleaf,springsecurity。针对springsecurity做了一些基础配置，拿来即用，避免一些不必要的坑。
   2. 统一定义了后端返回前端的数据结构ResponseVo
   3. 使用PublicMethod注解来标注方法是否需要登录验证
   4. 定义了统一的异常处理方式DefaultExceptionHandler
   
目前的功能还比较简单，将持续完善，欢迎关注。

## 

九月联盟不在于重新打造轮子，九月联盟旨在还原问题的本质，用最自然的方式解决最复杂的问题。

在这里你也许看不到华丽的技巧，在这里我们追求更正确的认知，在这里我们寻找最朴素的答案，并将这答案告知每一个奔波的码农。

你的生活不需要掌握无数的绝技，你需要的恰恰是不需要绝技的生活。
