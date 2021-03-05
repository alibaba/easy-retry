package com.alibaba.easyretry.extension.mybatis.common.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

public class HostUtils {

    private static final String IP;

    static {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            //do noting
        }
        if (Objects.isNull(address)) {
            IP = "UNKNOW-IP";
        } else {
            IP = address.getHostAddress();
        }
    }

    public static String getHostIP() {
        return IP;
    }
}
