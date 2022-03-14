package com.zx.fss.config;

import com.zx.fss.constant.SpecialSymbolsUtil;
import com.zx.fss.properties.LogServiceProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * WebSocket获取实时日志并输出到Web页面
 */
@Slf4j
@Component
@ServerEndpoint(value = "/websocket/logging/{applicationName}/{logLevel}/{lineNumStart}", configurator = MyEndpointConfigure.class)
public class LoggingWSServer {

    @Autowired
    LogServiceProperties logServiceProperties;



    /**
     * 连接集合
     */
    private static Map<String, Session> sessionMap = new ConcurrentHashMap<String, Session>();
    private static Map<String, Integer> lengthMap = new ConcurrentHashMap<String, Integer>();
    String key = null;
    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session,
                       @PathParam("logLevel") String  logLevel,
                       @PathParam("applicationName") String  applicationName,
                       @PathParam("lineNumStart") Integer  lineNumStart) throws Exception {

        key = session.getId() + applicationName + logLevel;
        //添加到集合中
        sessionMap.put(key, session);

        //日志路径
        String filePath = logServiceProperties.getRootPath()
                + SpecialSymbolsUtil.separator
                + applicationName
                + SpecialSymbolsUtil.separator
                +"log_"+logLevel.toLowerCase()+".log";

        //获取一共多少行日志
        int lineCount = lineCount(session, filePath);
        if(lineCount == 0){
            return;
        }
        //从第几行开始
        if(lineNumStart==-1){
            //默认最后500行
            lineNumStart = lineCount - logServiceProperties.getDefaultLastLineSize() < 0 ? 1:lineCount - logServiceProperties.getDefaultLastLineSize();
        }
        if(lineNumStart>=lineCount){
            lineNumStart = lineCount;
        }
        lengthMap.put(key, lineNumStart);

        //获取日志信息
        new Thread(() -> {

            log.info("LoggingWebSocketServer 任务开始");
            boolean first = true;
            while (sessionMap.get(key) != null) {
                BufferedReader reader = null;
                //查到的最后一行
                int endSize = 0;
                try {
                    //日志文件路径，获取最新的
                    //String filePath = System.getProperty("user.home") + "/log/" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + "/"+applicationName+".log";


                    //字符流
                    reader = new BufferedReader(new FileReader(filePath));
                    Object[] lines = reader.lines().toArray();

                    //只取从上次之后产生的日志
//                    Object[] copyOfRange = Arrays.copyOfRange(lines, lengthMap.get(key), lines.length);
                    //查到的最后一行
                    endSize = lengthMap.get(key) + logServiceProperties.getMaxLogLineSize();
                    if(endSize >= lines.length){
                        endSize = lines.length;
                    }
                    Object[] copyOfRange = Arrays.copyOfRange(lines, lengthMap.get(key), endSize);

                    //对日志进行着色，更加美观  PS：注意，这里要根据日志生成规则来操作
                    for (int i = 0; i < copyOfRange.length; i++) {
                        String line = (String) copyOfRange[i];
                        //先转义
                        line = "【第"+(lengthMap.get(key)+i)+"行】" +
                                line.replaceAll("&", "&amp;")
                                .replaceAll("<", "&lt;")
                                .replaceAll(">", "&gt;")
                                .replaceAll("\"", "&quot;");

                        //处理等级
                        line = line.replace("DEBUG", "<span style='color: blue;'>DEBUG</span>");
                        line = line.replace("INFO", "<span style='color: green;'>INFO</span>");
                        line = line.replace("WARN", "<span style='color: orange;'>WARN</span>");
                        line = line.replace("ERROR", "<span style='color: red;'>ERROR</span>");
                        line = line.replace("Error", "<span style='color: red;'>ERROR</span>");

                        //处理类名
//                        String[] split = line.split("]");
//                        if (split.length >= 2) {
//                            String[] split1 = split[1].split("-");
//                            if (split1.length >= 2) {
//                                line = split[0] + "]" + "<span style='color: #298a8a;'>" + split1[0] + "</span>" + "-" + split1[1];
//                            }
//                        }
                        line = addColor(line);
                        copyOfRange[i] = line;

                    }

                    //存储最新一行开始
                    //lengthMap.put(key, lines.length);

                    //第一次如果太大，截取最新的200行就够了，避免传输的数据太大
//                    if(first && copyOfRange.length > 200){
//                        copyOfRange = Arrays.copyOfRange(copyOfRange, copyOfRange.length - 200, copyOfRange.length);
//                        first = false;
//                    }

                    String result = StringUtils.join(copyOfRange, "<br/>");

                    result = result + "#####" + (lines.length-1) +"#####"+"@@@@@"+(endSize - lengthMap.get(key))+"@@@@@";
                    //发送
                    send(session, result);

                    //休眠一秒
                    Thread.sleep(1000);
                } catch (Exception e) {
                    //捕获但不处理
                    e.printStackTrace();
                    //发送
                    send(session, e.getMessage());
                } finally {
                    try {
                        reader.close();
                    } catch (IOException ignored) {
                    }
                    try {
                        session.close();
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                }
            }
            log.info("LoggingWebSocketServer 任务结束");
        }).start();
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        //从集合中删除
        sessionMap.remove(key);
        lengthMap.remove(key);
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    /**
     * 服务器接收到客户端消息时调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session) {

    }

    /**
     * 封装一个send方法，发送消息到前端
     */
    private void send(Session session, String message) {
        try {
            if(session.isOpen())
                session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取一共多少行日志
     * @param session
     * @return
     * @throws FileNotFoundException
     */
    private int lineCount(Session session,String filePath) throws IOException {
        //字符流
        BufferedReader bufferedReader = null;
        Object[] arr = new Object[0];
        try {
            bufferedReader = new BufferedReader(new FileReader(filePath));
            arr = bufferedReader.lines().toArray();
            //发送
            send(session, "#####" + arr.length+"#####");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            send(session, "#####0#####"+"@@@@@0@@@@@");
            session.close();
        }

        return arr.length;
    }

    private String addColor(String line){
        String pattern = "(com.zx.fss.[\\S\\.]*)\\s+";
        pattern = "(([\\S\\.]*) - )|((com.zx.fss.[\\S\\.]*)\\s+)|((com.zx.fss.[\\S\\.]*)\\))";
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);

        // 现在创建 matcher 对象
        Matcher m = r.matcher(line);
        while (m.find()){
            String group = m.group();
            String color = "green";
            if(group.contains("com.zx.fss")){
                color = "red";
            }
            line = line.replace(group,"<span style='color:"+color+";'>" + group + "</span>");
        }

        return line;
    }






}
