# 工程简介



# 延伸阅读

#参考
1.Spring Cloud OAuth2 token存数据库实现
    https://www.jianshu.com/p/4ce5577bab74 
    
2.Spring Security与OAuth2介绍
    https://www.jianshu.com/p/63115c71a590
    
3.Spring Security Oauth2 如何增加自定义授权模式
    https://blog.csdn.net/Little_fxc/article/details/92791883
    https://blog.csdn.net/weixin_42271016/article/details/104212326
    
4.spring security 自定义Provider 实现多种认证方式
    https://blog.csdn.net/yfx000/article/details/107529401
    
5.微服务之网关处理全局异常以及封装统一响应体
    https://blog.csdn.net/BobZhangfighting/article/details/111210298?utm_medium=distribute.pc_aggpage_search_result.none-task-blog-2~aggregatepage~first_rank_ecpm_v1~rank_v31_ecpm-1-111210298.pc_agg_new_rank&utm_term=%E7%BD%91%E5%85%B3%E7%BB%9F%E4%B8%80%E5%A4%84%E7%90%86404&spm=1000.2123.3001.4430
    
#问题解决
1.解决IDEA配置.gitignore不生效的问题
  当使用IDEA提交项目数据到git时, 即使配置了提交忽略文件.gitignore,也无法生效,提交文件中会出现杂乱配置文件,不知道你自己所更改了哪些项目位置,查看比较麻烦
  原因：.gitignore只能忽略未被track的文件，而git本地缓存。如果某些文件已经被纳入了版本管理中，则修改.gitignore是无效的。
  解决：解决方法就是先把本地缓存删除（改变成未track状态），然后再提交。使用以下几个命令即可快速解决
  注意:以下命令需要在你项目中右键点击 Git Bash Here进行命令窗口输入
  git rm -r --cached .
  git add .
  git commit -m 'update .gitignore'
2.idea下springboot打包成jar包和war包，并且可以在外部tomcat下运行访问到
    https://www.cnblogs.com/sxdcgaq8080/p/7727249.html
3.SpringBoot 用war包部署到tomcat下详细教程（解决缺少web.xml报错的问题）
    https://www.jianshu.com/p/0b745afbe0aa




















