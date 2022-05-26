# SpringBoot-CRUD
@[TOC](目录)

记录创建普通maven项目-＞二次添加Spring boot框架-＞单表增删查改-＞shiro安全验证->管理ip  
项目地址： [github](https://github.com/LiangYang666/SpringBoot-CRUD)  
记录帖：[csdn](https://blog.csdn.net/qq_39165617/article/details/124960824?spm=1001.2014.3001.5502)
# 一、 普通maven项目创建
1. 选择File-New Project，不选择Maven Archetype
2. 选择Name
   Maven
   com.liang
   ![在这里插入图片描述](https://img-blog.csdnimg.cn/1e98159da38e483588d929f000ac55de.png)
3. 测试，新建类
   ```java
   package com.liang;
   public class MyTest {
       public static void main(String[] args) {
           System.out.println("hello world");
       }
   }
   ```

# 二、添加Spring boot框架
1. 添加父级工程
   ```xml
   <parent>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-parent</artifactId>
       <version>2.5.6</version>
   </parent>
   ## 在这个之上添加<groupId>com.liang</groupId>
   ```
2. 添加spring-boot-starter-web依赖

   ```xml
   <dependencies>
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-web</artifactId>
       </dependency>
   </dependencies>
   ```
3. 修改main函数
   ```java
   package com.liang;
   
   import org.springframework.boot.SpringApplication;
   import org.springframework.boot.autoconfigure.SpringBootApplication;
   
   @SpringBootApplication
   public class MyTest {
       public static void main(String[] args) {
           SpringApplication.run(MyTest.class, args);
       }
   }
   ```
4. 添加一个简单Controller
   ```java
   @RestController
   public class HelloWorldController {
       @RequestMapping("/")
       public String sayHello(){
           return "Hello World!";
       }
   }
   ```
5. 测试
   http://127.0.0.1:8080/
   ![在这里插入图片描述](https://img-blog.csdnimg.cn/b0f89b2dbb4b49b6a23226c9cda10a91.png)
# 三、配置
## 3.1 sqlite数据库
1. 检查： 一般而言，Linux都预装了sqlite3，可直接命令行sqlite3查看
   ![在这里插入图片描述](https://img-blog.csdnimg.cn/0a567f5c7cbc47348723c0892dfae142.png)
2. 创建数据库
   切换至想存储数据库文件的路径，输入指令 `sqlite3 testDB.db`即可在当前目录下创建一个testDB.db数据库文件，直接使用即可

   ```bash
   创建完成后进入执行
   sqlite> .table 显示表格
   sqlite> .databases 显示当前数据库 会显示存储位置
   ```
3. idea中使用sqlite数据库
   ![在这里插入图片描述](https://img-blog.csdnimg.cn/b85ac29b58104759867ae9a8eb6c4e95.png)
   选择好文件即可使用console来连接处理文件夹即可，下面的URL会自动填充
   ![在这里插入图片描述](https://img-blog.csdnimg.cn/2ae2212d30ad4726ac7e4e4da104e7ee.png)
4. 创建sql文件，连接处理db，配置使用的concle，未弹出配置则可以ctrl+enter来执行，会让你选择使用哪个console
   ![在这里插入图片描述](https://img-blog.csdnimg.cn/a35367b241044e9383489fd2a87d664d.png)

   ```sql
   drop table allowed_ip;
   create table allowed_ip(
       ip vchar(15) PRIMARY KEY, -- 允许的ip
       address vchar(20) ,      -- 允许的ip所属地址
       create_time date not null, -- 创建时间
       comment vchar(100) -- 备注
   );
   insert into allowed_ip values('192.168.0.1', null, '2022-5-1', '测试1');
   insert into allowed_ip values('192.168.0.2', null, '2022-5-2', '测试2');
   insert into allowed_ip values('192.168.0.3', null, '2022-5-3', '测试3');
   insert into allowed_ip values('192.168.0.4', null, '2022-5-4', '测试4');
   insert into allowed_ip values('192.168.0.5', null, '2022-5-5', '测试5');
   insert into allowed_ip values('192.168.0.6', null, '2022-5-6', '测试6');
   select * from allowed_ip;
   ```
## 3.2 项目配置测试
1. 添加依赖
   ```xml
           <dependency>
               <groupId>org.xerial</groupId>
               <artifactId>sqlite-jdbc</artifactId>
               <version>3.36.0.3</version>
           </dependency>
           <dependency>
               <groupId>com.baomidou</groupId>
               <artifactId>mybatis-plus-boot-starter</artifactId>
               <version>3.5.1</version>
           </dependency>
           <dependency>
               <groupId>org.projectlombok</groupId>
               <artifactId>lombok</artifactId>
           </dependency>
   
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-test</artifactId>
               <scope>test</scope>
           </dependency>
   ```
2. 设置application.yaml
   ```yaml
   spring:
     datasource:
       driver-class-name: org.sqlite.JDBC
       url: jdbc:sqlite::resource:static/testDB.db
       username:
       password:
   
   # mybatis-plus配置
   mybatis-plus:
     mapper-locations: classpath:mappering/*.xml
     #实体扫描，多个package用逗号或者分号分隔
     type-aliases-package: com.liang.web.entity
   ```
3. 实体类

   ```java
   @Data
   @ToString
   @TableName("allowed_ip")
   public class IpEntity {
       @TableId
       private String ip;
       private String address;
       private String createTime;
       private String comment;
   }
   ```
4. Dao

   ```java
   @Repository
   public interface IpDao extends BaseMapper<IpEntity> {
   
   }
   ```
5. 主启动类添加MapperScan
   ```java
   @SpringBootApplication
   @MapperScan("com.liang.web.dao")
   public class MyTest {
       public static void main(String[] args) {
           SpringApplication.run(MyTest.class, args);
       }
   }
   ```
6. 在test中同级添加Test测试类
   注意必须要同级，需要在com.liang下，不然Autowired将不成功
   ```java
   @SpringBootTest
   public class Test {
   
       @Autowired
       private IpDao ipDao;
   
       @org.junit.jupiter.api.Test
       public void test(){
           List<IpEntity> allowedIpEntities = ipDao.selectList(null);
           System.out.println(allowedIpEntities);
       }
   }
   ```
   ![在这里插入图片描述](https://img-blog.csdnimg.cn/fbcc74f35301468cbb26907d8eb89295.png)
7. 启动测试类即可运行得到结果
## 3.3 前端配置
文件目录整体图
![在这里插入图片描述](https://img-blog.csdnimg.cn/b192806eccb44828848072b57f25936a.png)

1. 添加bootstrap和jquery
   [bootstrapV3.4.1下载链接](https://v3.bootcss.com/getting-started/#download)，放置位置如下
   [jquery下载链接](https://fastly.jsdelivr.net/npm/jquery@1.12.4/dist/jquery.min.js)放到js内
   ![在这里插入图片描述](https://img-blog.csdnimg.cn/37f0cb386d744e3a93b77930ce3e107e.png)
2. 添加thymeleaf依赖
   ```xml
      <dependency>
          <groupId>org.thymeleaf</groupId>
          <artifactId>thymeleaf-spring5</artifactId>
      </dependency>
   ```
   完善application.yaml文件，添加thymeleaf配置
   ```yaml
   spring:
     datasource:
       driver-class-name: org.sqlite.JDBC
       url: jdbc:sqlite::resource:static/testDB.db
       username:
       password:
     # thymeleaf配置
     thymeleaf:
       cache: false
       mode: HTML5
       encoding: UTF-8
       suffix: .html
   
   # mybatis-plus配置
   mybatis-plus:
     mapper-locations: classpath:mappering/*.xml
     #实体扫描，多个package用逗号或者分号分隔
     type-aliases-package: com.liang.web.entity
   ```

3.  添加三个html文件至templates下

    ipList.html
    ```html
    <!DOCTYPE html>
    <html lang="en" xmlns:th="http://www.thymeleaf.org">
    <head>
        <meta charset="UTF-8">
        <title>允许IP列表</title>
        <link href="/css/bootstrap.css" rel="stylesheet">
    </head>
    
    <style>
        a{
            color:#fff;
        }
    </style>
    
    <body>
    <button class="btn btn-primary form-control" style="height:50px"><a th:href="@{'/insertPage'}">添加允许IP</a></button>
    <table class="table table-striped table-bordered table-hover text-center">
        <thead>
        <tr style="text-align:center">
            <th>IP白名单</th>
            <th>IP属地</th>
            <th>创建时间</th>
            <th>备注</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <tr th:each="ip_info:${ips}">
            <td th:text="${ip_info.ip}"></td>
            <td th:text="${ip_info.address}"></td>
            <td th:text="${ip_info.createTime}"></td>
            <td th:text="${ip_info.comment}"></td>
            <td>
                <a class="btn btn-primary" th:href="@{'/updatePage/'+${ip_info.ip}}">更改</a>
                <a class="btn btn-danger" th:href="@{'/delete/'+${ip_info.ip}}">删除</a>
            </td>
        </tr>
        </tbody>
    </table>
    </body>
    </html>
    ```

    insertPage.html
    ```html
    <!DOCTYPE html>
    <html lang="en" xmlns:th="http://www.thymeleaf.org">
    <head>
        <meta charset="UTF-8">
        <title>添加允许的IP</title>
        <link href="/css/bootstrap.css" rel="stylesheet">
    </head>
    
    <body>
    
    <div style="width:800px;height:100%;margin-left:270px;">
        <form action="/insert" method="post">
            ip：<input class="form-control" type="text" name="ip"><br>
            备注：<input class="form-control" type="text" name="comment"><br>
            <button class="btn btn-primary btn-lg btn-block">保存</button>
        </form>
    </div>
    
    </body>
    </html>
    ```
    updatePage.html
    ```html
    <!DOCTYPE html>
    <html lang="en" xmlns:th="http://www.thymeleaf.org">
    <head>
        <meta charset="UTF-8">
        <title>修改IP信息</title>
        <link href="/css/bootstrap.css" rel="stylesheet">
    </head>
    <body>
    
    <div style="width:800px;height:100%;margin-left:270px;">
        <form action="/update" method="post">
            IP：<input class="form-control"  type="text" th:value="${ip.ip}" name="ip" readonly="readonly"><br>
            IP属地：<input class="form-control" type="text" th:value="${ip.address}" name="address"><br>
            备 注：<input class="form-control" type="text" th:value="${ip.comment}" name="comment"><br>
            <button class="btn btn-primary btn-lg btn-block" type="submit">提交</button>
        </form>
    </div>
    </body>
    </html>
    
    ```
## 3.4 后台代码
1. IPController
   ```java
   @Controller
   public class IpController {
   
       @Autowired
       private IpService ipService;
   
       @GetMapping("/ipList")
       public String table(Model model) {
           List<IpEntity> ipEntities = ipService.getAllIp();
           model.addAttribute("ips", ipEntities);
           return "ipList";
       }
   
       @RequestMapping("/insertPage")
       public String toInsertPage(){
           return "insertPage";
       }
   
       @RequestMapping("/insert")
       public String save(IpEntity allowedIp){
           ipService.saveIp(allowedIp);
           System.out.println("新增了IP：" + allowedIp);
           return "redirect:/ipList";
       }
   
       @GetMapping("/delete/{ip}")
       public String delete(@PathVariable String ip){
           ipService.deleteByIp(ip);
           System.out.println("删除了IP：" + ip);
           return "redirect:/ipList";
       }
   
       @GetMapping("/updatePage/{ip}")
       public String updatePage(@PathVariable String ip, Model model){
           IpEntity allowedIp = ipService.getByIp(ip);
           model.addAttribute("ip", allowedIp);
           return "updatePage";
       }
   
       @PostMapping("/update")
       public String update(IpEntity allowedIp){
           ipService.updateIp(allowedIp);
           System.out.println("更新了IP：" + allowedIp);
           return "redirect:/ipList";
       }
   }
   ```
2. IpService
   ```java
   @Service
   public class IpService {
       @Autowired
       private IpDao ipDao;
   
       public List<IpEntity> getAllIp(){
           return ipDao.selectList(null);
       }
   
       public void saveIp(IpEntity ipEntity){
           String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
           ipEntity.setCreateTime(time);
           ipDao.insert(ipEntity);
       }
       public void deleteByIp(String ip){
           ipDao.deleteById(ip);
       }
       public IpEntity getByIp(String ip){
           return ipDao.selectById(ip);
       }
   
       public void updateIp(IpEntity allowedIp) {
           ipDao.updateById(allowedIp);
       }
   }
   
   ```
## 3.5 测试
列表
![在这里插入图片描述](https://img-blog.csdnimg.cn/82959a43c8214bb595c0cc91ab6850c8.png)
修改
![在这里插入图片描述](https://img-blog.csdnimg.cn/69e4254a9d454307a89534ad1b034463.png)
添加
![在这里插入图片描述](https://img-blog.csdnimg.cn/f795474afd194d0c8f023a8bc7b2ca29.png)
## 3.6 shiro登录

这一部分的登录只是管理员登录，用户和密码恒为admin+123456
1. 添加依赖
   ```xml
    <!--shiro 配置-->
    <dependency>
        <groupId>org.apache.shiro</groupId>
        <artifactId>shiro-spring-boot-web-starter</artifactId>
        <version>1.9.0</version>
    </dependency>
   ```
2. 前端
   ```html
   <!DOCTYPE html>
   <html lang="en">
   <head>
       <meta charset="UTF-8">
       <title>登录</title>
       <link rel="stylesheet" href="/css/bootstrap.css">
       <style>
           .loginForm {
               /*边框高度*/
               height: 350px;
               /*边框宽度*/
               width: 500px;
               /*边框颜色*/
               border: #4d4d4d solid 1px;
               /*边框圆角*/
               border-radius: 4px;
               /*阴影 水平方向，竖直方向，模糊距离*/
               box-shadow: 5px 5px 5px #4d4d4d;
               /*上边界距离*/
               margin-top: 300px;
               /*左边界距离：自动*/
               margin-left: auto;
               /*右边界距离：自动*/
               margin-right: auto;
               /*用户名、密码间距*/
               padding: 20px 40px;
           }
   
           /*将用户登录置于中间*/
           .loginForm h2 {
               text-align: center;
           }
   
           /*修改button属性*/
           .button {
               text-align: center;
               vertical-align: middle;
           }
       </style>
   </head>
   <body>
   <div class="loginForm">
       <h2>管理员登录</h2>
       <form id="form_login" name="loginForm" method="post" action="/doLogin" autocomplete="off">
           <div class="form-group">
               <label for="exampleInputEmail1">用户名</label>
               <input type="text" class="form-control" id="exampleInputEmail1" name="username"  value="admin" placeholder="请输入用户名">
           </div>
           <div class="form-group">
               <label for="exampleInputPassword1">密码</label>
               <input type="password" class="form-control" id="exampleInputPassword1" placeholder="请输入密码" name="password"  title="请输入密码" autocomplete="on">
           </div>
           <div class="button">
               <input type="submit" class="btn btn-primary" value="登 录"/>
           </div>
       </form>
   </div>
   
   </body>
   </html>
   ```
3. UserRealm
   ```java
   package com.liang.web.shiro;
   
   import org.apache.shiro.authc.AuthenticationException;
   import org.apache.shiro.authc.AuthenticationInfo;
   import org.apache.shiro.authc.AuthenticationToken;
   import org.apache.shiro.authc.SimpleAuthenticationInfo;
   import org.apache.shiro.authz.AuthorizationInfo;
   import org.apache.shiro.realm.AuthorizingRealm;
   import org.apache.shiro.subject.PrincipalCollection;
   
   public class UserRealm extends AuthorizingRealm {
       @Override
       protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
           return null;
       }
   
       @Override
       protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
           String username = (String) authenticationToken.getPrincipal();
           if (!username.equals("admin")) {
               throw new AuthenticationException("用户名不正确");
           }
           return new SimpleAuthenticationInfo(username, "123456", getName());
       }
   }
   
   ```
4. shiroConfig

   ```java
   package com.liang.web.shiro;
   
   import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
   import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
   import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
   import org.springframework.beans.factory.annotation.Qualifier;
   import org.springframework.context.annotation.Bean;
   import org.springframework.context.annotation.Configuration;
   
   import java.util.LinkedHashMap;
   import java.util.Map;
   
   @Configuration
   public class ShiroConfig {
       //  1.定义userRealm进springboot组件
       @Bean
       public UserRealm userRealm() {
           UserRealm userRealm = new UserRealm();
           userRealm.setCredentialsMatcher(new SimpleCredentialsMatcher());
           return userRealm;
       }
       //2. 获取安全管理器
       @Bean(name = "defaultWebSecurityManager")
       public DefaultWebSecurityManager defaultWebSecurityManager(
               @Qualifier("userRealm") UserRealm userRealm){
           DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
           securityManager.setRealm(userRealm);    //注入
           return securityManager;
       }
       // 3. 过滤器设置
       @Bean
       public ShiroFilterFactoryBean shiroFilterFactoryBean(
               @Qualifier("defaultWebSecurityManager") DefaultWebSecurityManager defaultWebSecurityManager){
           ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
           bean.setSecurityManager(defaultWebSecurityManager); // 设置安全管理器
           //传入未登录用户访问登陆用户的权限所跳转的页面
           bean.setLoginUrl("/login");
   
           //访问未授权网页所跳转的页面
           bean.setUnauthorizedUrl("/unauthorized");
           Map<String, String> map = new LinkedHashMap<>();
           //允许未认证访问  需要设置login为anon 否则登陆成功后无法成功跳转。
           map.put("/login", "anon");
           map.put("/doLogin", "anon");
           map.put("/css/*.css", "anon");
           map.put("/js/*.js", "anon");
   
           //需要认证才能访问
           map.put("/**", "authc");
           bean.setFilterChainDefinitionMap(map);
           return bean;
       }
   
   }
   
   ```

5. 登录Controller
   ```java
   package com.liang.web.controller;
   
   import org.apache.shiro.SecurityUtils;
   import org.apache.shiro.authc.UsernamePasswordToken;
   import org.apache.shiro.subject.Subject;
   import org.springframework.stereotype.Controller;
   import org.springframework.web.bind.annotation.GetMapping;
   import org.springframework.web.bind.annotation.PostMapping;
   import org.springframework.web.bind.annotation.RequestParam;
   @Controller
   public class LoginController {
       //如果需要使用shiro长期登陆，设置subject的rememberMe属性并且设置允许的范围为user。authc不允许被rememberMe用户访问。
       //这就是我们传入账号密码测试的地方
       @PostMapping(value = "/doLogin")
       public String doLogin(@RequestParam(value = "username") String username,
                           @RequestParam(value = "password") String password){
           Subject subject = SecurityUtils.getSubject();
           try {
               UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
               subject.login(usernamePasswordToken);
               System.out.println("登陆成功");
           }catch (Exception e){
               e.printStackTrace();
               System.out.println("登陆失败");
           }
           return "redirect:/index";
       }
   
       @GetMapping(value = "/login")
       public String login(){
           return "login";
       }
       @GetMapping("/logout")
       public String logout(){
           Subject subject = SecurityUtils.getSubject();
           subject.logout();
           return "redirect:/login";
       }
   }
   ```
6. 测试，这里注意可将ipController改为@GetMapping({"/ipList", "/", "/index"})
   ![在这里插入图片描述](https://img-blog.csdnimg.cn/24bf99a988ed4d8aad39ac9f5ee6266e.png)















