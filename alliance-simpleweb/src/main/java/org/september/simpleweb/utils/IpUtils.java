package org.september.simpleweb.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 
 * @author huangwq 下午11:45:46
 *
 */
public class IpUtils {

    private static String Ip_Unknown = "unknown";

    /**
     * @date 2017年6月22日 上午10:24:02
     * @param request
     * @return
     */
    public static String getIp(HttpServletRequest request) {
        if (null == request) {
            return null;
        }
        String ip = request.getHeader("x-forwarded-for");
        if (!validateIp(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
            if (!validateIp(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
                if (!validateIp(ip)) {
                    ip = request.getRemoteAddr();
                }
            }
        }
        return conver(ip);
    }

    private static boolean validateIp(String ip) {
        return (ip == null || ip.length() == 0 || Ip_Unknown.equalsIgnoreCase(ip)) ? false : true;
    }

    private static String conver(String ip) {
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            return "127.0.0.1";
        } else {
            return ip;
        }
    }

    /**
     * 获取连接WEB服务器的真实IP，前端有UTR的拦截请求
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || Ip_Unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        } else {
            String[] ipArray = ip.split("\\,");
            ip = ipArray[0];//  获取第一个IP
        }
        if (ip == null || ip.length() == 0 || Ip_Unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || Ip_Unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("clientip");
        }
        if (ip == null || ip.length() == 0 || Ip_Unknown.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return conver(ip);
    }

    /**
     * 获取当前机器的ip
     * @date 2017年6月22日 上午10:24:24
     * @return
     * @throws SocketException
     */
    public static InetAddress getIpAddress() throws SocketException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface current = interfaces.nextElement();
            if (!current.isUp() || current.isLoopback() || current.isVirtual()) {
                continue;
            }
            Enumeration<InetAddress> addresses = current.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();
                if (addr.isLoopbackAddress() || addr.isLinkLocalAddress()) {
                    continue;
                }
                return addr;
            }
        }

        throw new SocketException("Can't get our ip address, interfaces are: " + interfaces);
    }

    /**
     * 判断是否ip
     * @author yexinzhou
     * @date 2017年6月22日 上午10:24:47
     * @param addr
     * @return
     */
    public static boolean isIP(String addr) {
        if (addr.length() < 7 || addr.length() > 15 || "".equals(addr)) {
            return false;
        }
        /**
         * 判断IP格式和范围
         */
        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

        Pattern pat = Pattern.compile(rexp);

        Matcher mat = pat.matcher(addr);

        boolean ipAddress = mat.find();

        return ipAddress;
    }

    public static void main(String[] args) throws SocketException {
        System.out.println("192.168.1.1=" + IpUtils.isIP("192.168.1.1"));
        System.out.println("192.168.1.0=" + IpUtils.isIP("192.168.1.0"));
        System.out.println("127.0.0.1=" + IpUtils.isIP("127.0.0.1"));
        System.out.println("202.103.224.68=" + IpUtils.isIP("202.103.224.68"));
        System.out.println("255.255.255.255=" + IpUtils.isIP("255.255.255.255"));
        System.out.println("asdfas=" + IpUtils.isIP("243rwq"));
    }
}
