package com.zdkk.speed.context;

/**
 * 存储当前用户的id
 */
public class BaseContext {
    private static final ThreadLocal<Long> context = new ThreadLocal<>();
    /**
     * 设置当前用户的id
     * @param id
     */
    public static void setCurrentId(Long id) {
        context.set(id);
    }
    /**
     * 获取当前用户的id
     * @return
     */
    public static Long getCurrentId() {
        return context.get();
    }
    /**
     * 移除当前用户的id
     */
    public static void removeCurrentId() {
        context.remove();
    }
}
