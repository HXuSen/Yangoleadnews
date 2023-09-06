package com.yango.utils.thread;

import com.yango.model.wemedia.pojos.WmUser;

/**
 * ClassName: WmThreadLocalUtil
 * Package: com.yango.utils.thread
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/28-22:13
 */
public class WmThreadLocalUtil {

    private final static ThreadLocal<WmUser> WM_USER_THREAD_LOCAL = new ThreadLocal<>();

    public static void setUser(WmUser wmUser){
        WM_USER_THREAD_LOCAL.set(wmUser);
    }

    public static WmUser getUser(){
        return WM_USER_THREAD_LOCAL.get();
    }

    public static void clear(){
        WM_USER_THREAD_LOCAL.remove();
    }

}
