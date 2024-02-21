package demosecurity.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class AopExpressions {

    @Pointcut("execution(* demosecurity.controller.handleSseWithEmitter.*(..))")
    public void forGameController() {}

}
