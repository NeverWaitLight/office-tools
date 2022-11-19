package net.yeah.waitlight.commons.tools.core;

public final class TaskContext {

    private static final ThreadLocal<LanguageCountry> contextHolder = new ThreadLocal<>();

    public void clearContext() {
        contextHolder.remove();
    }

    public LanguageCountry getLanguageCountry() {
        return null;
    }

    public void setContext(LanguageCountry languageCountry) {
    }

}
