package io.crm.core.builders;

/**
 * Created by xiongxl in 2022/3/12
 * 构建器公共接口
 * @param <T> 要构建的类型
 */
public interface Builder<T> {
    T build();
}
