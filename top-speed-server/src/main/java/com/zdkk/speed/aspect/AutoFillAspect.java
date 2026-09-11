package com.zdkk.speed.aspect;

import com.zdkk.speed.annotation.AutoFill;
import com.zdkk.speed.constant.AutoFillConstant;
import com.zdkk.speed.context.BaseContext;
import com.zdkk.speed.enumeration.OperationType;
import com.zdkk.speed.exception.AutoFillException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;

import static com.zdkk.speed.constant.MessageConstant.AUTO_FILL_FAILED;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    @Pointcut("execution(* com.zdkk.speed.service.AutoFillService.*(..)) && @annotation(com.zdkk.speed.annotation.AutoFill)")
    public void autoFillPointCut() {}

    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("AutoFillAspect autoFill {}", joinPoint.getTarget());

        // 获取AutoFill注解里的操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = autoFill.value();

        // 获取方法参数
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0 || args[0] == null) {
            log.warn("AutoFillAspect autoFill args is null");
            return;
        }

        Object object = args[0];

        LocalDateTime time = LocalDateTime.now();
        Long id = BaseContext.getCurrentId();

        if (operationType == OperationType.INSERT) {
            try {
                object.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class).invoke(object, time);
                object.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class).invoke(object, time);
                object.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class).invoke(object, id);
                object.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class).invoke(object, id);
            } catch (NoSuchMethodException e) {
            log.error("【自动填充】实体类 {} 缺少方法，请检查 AutoFillConstant 常量与实体方法是否匹配",
                    object.getClass().getName(), e);
                throw new AutoFillException(AUTO_FILL_FAILED);
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.error("【自动填充】调用 setter 失败，实体类：{}", object.getClass().getName(), e);
                throw new AutoFillException(AUTO_FILL_FAILED);
            }
        } else if (operationType == OperationType.UPDATE) {
            try {
                object.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class).invoke(object, time);
                object.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class).invoke(object, id);
            } catch (NoSuchMethodException e) {
                log.error("【自动填充】实体类 {} 缺少方法，请检查 AutoFillConstant 常量与实体方法是否匹配",
                        object.getClass().getName(), e);
                throw new AutoFillException(AUTO_FILL_FAILED);
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.error("【自动填充】调用 setter 失败，实体类：{}", object.getClass().getName(), e);
                throw new AutoFillException(AUTO_FILL_FAILED);
            }
        }
    }
}
