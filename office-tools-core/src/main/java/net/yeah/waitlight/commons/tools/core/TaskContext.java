package net.yeah.waitlight.commons.tools.core;

public final class TaskContext {

    private static final ThreadLocal<TaskContextHolder> contextHolder = new ThreadLocal<>();

    public void clearContext() {
        contextHolder.remove();
    }

    public static TaskContextHolder getContext() {
        return contextHolder.get();
    }
}
