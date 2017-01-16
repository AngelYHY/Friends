package freestar.friends.util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2016/7/15 0015.
 */
public class NetUtil {
    public static void getDataByGet(final String urlStr, final Handler handler) {
        //创建子线程
        new Thread() {
            @Override
            public void run() {
                Log.d("FreeStar", "NetUtil→→→run:进入线程");
                super.run();
                HttpURLConnection connection = null;
                InputStream is = null;
                try {
                    URL url = new URL(urlStr);
                    //建立和服务端连接
                    try {
                        //注意HttpURLConnection  不是HttpsURLConnection
                        connection = (HttpURLConnection) url.openConnection();
                        //设置请求方式：get
                        connection.setRequestMethod("GET");
                        //设置连接超时时间
//                        connection.setConnectTimeout(10000);
                        Log.d("FreeStar", "NetUtil→→→run" + connection.getResponseCode());
                        //服务端返回成功：200
                        if (connection.getResponseCode() == 200) {
                            //获取服务端返回的数据
                            is = connection.getInputStream();
                            //is转换成字符串
                            String readStream = readStream(is);
                            //通过handler 发送消息：将数据传给主线程
                            //创建消息对象
                            Message message = Message.obtain();//从消息池中找到一个消息对象
                            //设置消息的标识
                            message.what = 1;
                            //设置消息携带的参数
                            message.obj = readStream;
                            //通过handler，发送消息
                            handler.sendMessage(message);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } finally {
                    connection.disconnect();
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    //把一个inputstream 转换成一个String
    public static String readStream(InputStream in) throws IOException {
        int len = -1;
        //定义一个内存输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while ((len = in.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
        }
        in.close();
        String content = new String(baos.toByteArray());
        return content;
    }

    //url,参数的map，handler
    public static void getDataByPost(final String url, final Map<String, Object> map, final Handler handler) {
        new Thread() {


            @Override
            public void run() {
                InputStream is = null;
                OutputStream os = null;
                HttpURLConnection connection = null;
                URL urls;
                super.run();
                Log.d("FreeStar", "NetUtil→→→run:进入run");
                try {
                    urls = new URL(url);
                    //post方式访问网络
                    try {
                        connection = (HttpURLConnection) urls.openConnection();
                        connection.setRequestMethod("POST");//设置请求方式：post
                        //取出map中数据，放在string: key1=value1&key2=value2&
                        Set<Map.Entry<String, Object>> set = map.entrySet();
                        StringBuilder builder = new StringBuilder();
                        for (Map.Entry<String, Object> entry : set) {
                            String key = entry.getKey();
                            Object value = entry.getValue();
                            builder.append(key + "=" + value + "&");
                        }
                        //转换成字符串
                        String s = builder.toString();
                        //结束位置（不包含）：去掉最后一个”&“
                        String substring = s.substring(0, s.length() - 1);
                        //设置post请求参数
                        os = connection.getOutputStream();
                        //参数格式： key1=value1&key2=value2
                        //转换成流
                        os.write(substring.getBytes());
                        Log.d("FreeStar", "NetUtil→→→run：10");
                        Log.d("FreeStar", "NetUtil→→→run" + connection.getResponseCode());
                        if (connection.getResponseCode() == 200) {
                            is = connection.getInputStream();
                            String s1 = readStream(is);
                            //创建消息
                            Message mgs = Message.obtain();
                            mgs.what = 1;
                            mgs.obj = s1;
                            //handler发送消息
                            Log.d("FreeStar", "NetUtil→→→run:发送消息");
                            handler.sendMessage(mgs);
                        } else {
                            Log.d("FreeStar", "NetUtil→→→run:失败了");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } finally {
                    connection.disconnect();
                    try {
                        is.close();
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }.start();
    }
}
