package io.crm.core.noop;

/**
 * Created by xiongxl in 2022/3/18
 * 无参类型，表示一个空参数
 * 不使用java.lang.Void的原因是其无实例
 * @see Void
 */
public final class NoopArg {
    @SuppressWarnings("InstantiationOfUtilityClass")
    private static final NoopArg NOOP = new NoopArg();

    private NoopArg() {
    }

    public static NoopArg get() {
        return NOOP;
    }
}
