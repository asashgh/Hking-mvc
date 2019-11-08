package com.hgh.mvc.aop;

import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.PointcutPrimitive;
import org.aspectj.weaver.tools.ShadowMatch;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * 代理切点类
 */
public class ProxyPointCut {

    /**
     * 切点解析器
     */
    private PointcutParser pointcutParser;

    /**
     * (AspectJ)表达式
     */
    private String expression;

    /**
     * 表达式解析器
     */
    private PointcutExpression pointcutExpression;

    private static final Set<PointcutPrimitive> DEFAULT_SUPPORTED_PRIMITIVES = new HashSet<>();

    static {
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.ARGS);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.REFERENCE);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.THIS);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.TARGET);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.WITHIN);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ANNOTATION);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_WITHIN);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ARGS);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_TARGET);
    }

    public ProxyPointCut() {
        this(DEFAULT_SUPPORTED_PRIMITIVES);
    }

    public ProxyPointCut(Set<PointcutPrimitive> supportedPrimitives) {
        pointcutParser = PointcutParser
                .getPointcutParserSupportingSpecifiedPrimitivesAndUsingContextClassloaderForResolution(supportedPrimitives);

    }

    /**
     *  Class是否匹配切点表达式
     */
    public boolean matches(Class<?> clz){
        checkReadyToMatch();
        return pointcutExpression.couldMatchJoinPointsInType(clz);
    }

    /**
     * 判断Method是否匹配切点表达式
     * @param method
     * @return
     */
    public boolean matches(Method method){
        checkReadyToMatch();
        ShadowMatch shadowMatch = pointcutExpression.matchesMethodExecution(method);
        if(shadowMatch.alwaysMatches()){
            return true;
        }else if (shadowMatch.neverMatches()){
            return false;
        }
        return false;
    }

    /**
     * 初始化切点解析器
     */
    private void checkReadyToMatch(){
        if (null == pointcutExpression){
            pointcutExpression = pointcutParser.parsePointcutExpression(expression);
        }
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
