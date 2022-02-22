package com.zx.fss.config;

import com.zx.fss.component.JwtTokenEnhancer;
import com.zx.fss.config.customer.CustomerAuthenticationTokenGranter;
import com.zx.fss.service.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.cloud.bootstrap.encrypt.KeyProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 认证服务器配置
 * Created by macro on 2020/6/19.
 * 添加认证服务相关配置Oauth2ServerConfig，需要配置加载用户信息的服务UserServiceImpl及RSA的钥匙对KeyPair
 */
@AllArgsConstructor
@Configuration
@EnableAuthorizationServer
public class Oauth2ServerConfig extends AuthorizationServerConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    private final UserServiceImpl userDetailsService;

    private final AuthenticationManager authenticationManager;

    private DataSource dataSource;

    //自定义的JWT内容增强器
    private final JwtTokenEnhancer jwtTokenEnhancer;


    /**************** token的存储方法
     * https://blog.csdn.net/qq_41071876/article/details/122387919
     * 作用：TokenStore主要作用是token的增删改查
     * TokenStore的初始化：在发生在AuthorizationServerEndpointsConfigurer中
     * 默认的TokenStore：是InMemoryTokenStore
     */

    @Bean
    public TokenStore tokenStore() {
        //将令牌存储到内存
        return new InMemoryTokenStore();
    }

    private RedisConnectionFactory redisConnectionFactory;
    @Bean
    public TokenStore redisTokenStore(){
        //将令牌存储到Redis
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        return redisTokenStore;
    }

    @Bean
    public TokenStore jdbcTokenStore(){
        //将令牌存储到数据库
        return new JdbcTokenStore(dataSource);
    }

    @Bean
    public TokenStore jwtTokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        //使用JWT令牌
        return new JwtTokenStore(jwtAccessTokenConverter);
    }


    /******************************JWT相关****************************************/

    //读取密钥的配置
    @Bean("keyProp")
    public KeyProperties keyProperties(){
        //需要在配置文件配置属性值
        return new KeyProperties();
    }
    @Resource(name = "keyProp")
    private KeyProperties keyProperties;

    @Bean
    public KeyPair keyPair() {
        org.springframework.core.io.Resource location = keyProperties.getKeyStore().getLocation();
        String password = keyProperties.getKeyStore().getSecret();
        String alias = keyProperties.getKeyStore().getAlias();
        //获取秘钥对
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(location, password.toCharArray());
        return keyStoreKeyFactory.getKeyPair(alias, password.toCharArray());
    }

    /**
     * jwt访问token转换器,返回jwt令牌转换器（帮助我们生成jwt令牌的）
     * 在这里，我们可以把签名密钥传递进去给转换器对象
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setKeyPair(keyPair());
        return jwtAccessTokenConverter;
    }



    /**************************客户端配置
     *
     * 用来配置客户端详情服务（ClientDetailsService），
     * 客户端详情信息在这里进行初始化，你能够把客户端详情信息写死在这里或者是通过数据库来存储调取详情信息
     */
    /*
     *方式一：基于内存
     */
//    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        clients.inMemory()
//                .withClient("client-app")
//                .secret(passwordEncoder.encode("123456"))
//                .scopes("all")
//                .authorizedGrantTypes("my_password", "refresh_token")
//                .accessTokenValiditySeconds(3600)
//                .refreshTokenValiditySeconds(86400);
//    }

    /*
     *方式二：基于数据库
     */
    @Bean
    public ClientDetailsService jdbcClientDetailsService() {
        return new JdbcClientDetailsService(dataSource);
    }
    //使用默认表oauth_client_details
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //clients.jdbc(this.dataSource).clients(this.jdbcClientDetailsService());
        clients.withClientDetails(jdbcClientDetailsService());
    }
    //不适用默认表，可以创建一个类（如ClientDetailsServiceImpl）继承 JdbcClientDetailsService ，模仿查询
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        //clientService 继承了 JdbcClientDetailsService
//        //不使用oauth2默认表可以在这里设置（我使用默认表）
//        clientService.setSelectClientDetailsSql(Oauth2Constant.SELECT_CLIENT_DETAIL_SQL);
//        clientService.setFindClientDetailsSql(Oauth2Constant.FIND_CLIENT_DETAIL_SQL);
//        clients.withClientDetails(clientService);
//    }


    /***************************授权服务器的安全配置****************/
    /**
     * 用来配置令牌端点(Token Endpoint)的安全约束
     * 认证服务器最终是以api接口的方式对外提供服务（校验合法性并生成令牌、校验令牌等）
     * 那么，以api接口方式对外的话，就涉及到接口的访问权限，我们需要在这里进行必要的配置
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//        // 开启端口/oauth/token_key的访问权限（允许）
//        security.tokenKeyAccess("permitAll()");
//        // 开启端口/oauth/check_token的访问权限（允许）
//        security.checkTokenAccess("isAuthenticated()");
        // 允许客户端表单认证,相当于打开endpoints 访问接口的开关，这样的话后期我们能够访问该接口
        security.allowFormAuthenticationForClients();

    }



    /*************************************授权服务器端点配置
     *
     * 用来配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token services)。
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        endpoints.tokenStore(jdbcTokenStore())//token存储方式默认InMemoryTokenStore,这里用数据库
//                .authenticationManager(authenticationManager)
//                //登陆时grant_type = password 的会进到这个业务
//                .userDetailsService(userDetailsService) //配置加载用户信息的服务,不指定会默认去找实现了UserDetailsService的类
//                //.accessTokenConverter(jwtAccessTokenConverter())//jwtAccessTokenConverter是一种token增强器，在下面已经添加
//                .tokenEnhancer(getTokenEnhancerChain())//设置token增强器，可以添加自己的增强器
//                .tokenGranter(getTokenGranter(endpoints));//设置授权验证模式，可以添加自己的授权模式

        endpoints.userDetailsService(userDetailsService) //配置加载用户信息的服务,不指定会默认去找实现了UserDetailsService的类
                .tokenGranter(getTokenGranter(endpoints))//设置授权验证模式，可以添加自己的授权模式
                .tokenServices(tokenServices());// token服务的一个描述，可以认为是token生成细节的描述，比如有效时间多少等

    }

    private DefaultTokenServices tokenServices(){
        // 使用默认实现
        DefaultTokenServices tokenServices = new DefaultTokenServices();

        // 是否支持刷新令牌
        tokenServices.setSupportRefreshToken(Boolean.TRUE);
        //是否重复使用刷新令牌（直到过期）
        tokenServices.setReuseRefreshToken(Boolean.TRUE);
        // 设置令牌有效时间（一般设置为2个小时），默认12小时
        tokenServices.setAccessTokenValiditySeconds(600); // access_token就是我们请求资源需要携带的令牌
        // 设置刷新令牌的有效时间，默认3天
        tokenServices.setRefreshTokenValiditySeconds(259200);

        //设置客户端,从客户端获取信息如token过期时间、和刷新时间，这个优先级高于上面的过期时间
        tokenServices.setClientDetailsService(jdbcClientDetailsService());

        //存储方式
        //tokenServices.setTokenStore(jdbcTokenStore());
        //tokenServices.setTokenStore(redisTokenStore());
        //tokenServices.setTokenStore(tokenStore());
        tokenServices.setTokenStore(jwtTokenStore(jwtAccessTokenConverter()));//每次登录，都是新的token，过期时间重置
        // token增强链
        tokenServices.setTokenEnhancer(getTokenEnhancerChain());
        //身份认证管理器
        tokenServices.setAuthenticationManager(authenticationManager);

        return tokenServices;


    }

    /**
     * 返回 循环其委托增强器的复合令牌增强器。
     * 可以按自己的需求增加，此处只增加了自定义token增强器 和 jwt访问token转换器（一种令牌增强器）
     */
    private TokenEnhancerChain getTokenEnhancerChain(){
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
//        List<TokenEnhancer> delegates = new ArrayList<>();
//        delegates.add(jwtTokenEnhancer);//自定义token增强器
//        delegates.add(accessTokenConverter());//在 JWT 编码的令牌值和 OAuth 身份验证信息（双向）之间转换的助手。授予令牌时也充当token增强器
//        enhancerChain.setTokenEnhancers(delegates); //配置JWT的内容增强器
        //上面注释合并写法
        enhancerChain.setTokenEnhancers(Arrays.asList(jwtTokenEnhancer,jwtAccessTokenConverter())); //配置JWT的内容增强器
        return enhancerChain;
    }


    /**
     * 重点
     * 先获取已经有的五种授权，然后添加我们自己的进去
     * @param endpoints AuthorizationServerEndpointsConfigurer
     * @return TokenGranter
     */
    private TokenGranter getTokenGranter(AuthorizationServerEndpointsConfigurer endpoints) {
        //List<TokenGranter> granters = new ArrayList<>(Collections.singletonList(endpoints.getTokenGranter()));
        List<TokenGranter> list = new ArrayList<>();
//        // 这里配置密码模式
//        if (authenticationManager != null) {
//            list.add(new ResourceOwnerPasswordTokenGranter(authenticationManager,
//                    endpoints.getTokenServices(),
//                    endpoints.getClientDetailsService(),
//                    endpoints.getOAuth2RequestFactory()));
//        }
        //刷新token模式、
        list.add(new RefreshTokenGranter
                (endpoints.getTokenServices(),
                        endpoints.getClientDetailsService(),
                        endpoints.getOAuth2RequestFactory()));
//        //授权码模式、
//        list.add(new AuthorizationCodeTokenGranter(
//                endpoints.getTokenServices(),
//                endpoints.getAuthorizationCodeServices(),
//                endpoints.getClientDetailsService(),
//                endpoints.getOAuth2RequestFactory()));
//        //、简化模式
//        list.add(new ImplicitTokenGranter(
//                endpoints.getTokenServices(),
//                endpoints.getClientDetailsService(),
//                endpoints.getOAuth2RequestFactory()));
//
//        //客户端模式
//        list.add(new ClientCredentialsTokenGranter(endpoints.getTokenServices(), endpoints.getClientDetailsService(),  endpoints.getOAuth2RequestFactory()));

        // 自定义密码验证模式
        list.add(new CustomerAuthenticationTokenGranter(
                authenticationManager,
                endpoints.getTokenServices(),
                endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory(),"my_password"));
        return new CompositeTokenGranter(list);
    }




}
