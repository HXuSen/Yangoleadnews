package com.yango.utils.thread;

import com.yango.model.user.pojos.ApUser;
import com.yango.model.wemedia.pojos.WmUser;

/**
 * ClassName: WmThreadLocalUtil
 * Package: com.yango.utils.thread
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/28-22:13
 */
public class AppThreadLocalUtil {

    private final static ThreadLocal<ApUser> WM_USER_THREAD_LOCAL = new ThreadLocal<>();

    public static void setUser(ApUser apUser){
        WM_USER_THREAD_LOCAL.set(apUser);
    }

    public static ApUser getUser(){
        return WM_USER_THREAD_LOCAL.get();
    }

    public static void clear(){
        WM_USER_THREAD_LOCAL.remove();
    }

}
