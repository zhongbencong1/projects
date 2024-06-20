package com.faker.project.schedule;

import com.faker.project.Entity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * spel表达式简单用法
 */
@Slf4j
public class SpelExpSimple {
    /**
     * ExpressionParser 表达式解析器: 负责解析表达式字符串, 将字符串表达式解析为表达式对象
     * SpelExpressionParser：SpEL解析器。实例是可重用的，且是线程安全的（最常用）
     * 主要方法：parseExpression：将字符串表达式转化为表达式对象； parseExpression：将字符串表达式转化为表达式对象（根据表达式解析模板上下文）
     */

    /**
     * Expression 提供getValue方法用于获取表达式值，提供setValue方法用于设置对象值
     * SpelExpression：SpEL表达式（最常用） 主要方法：getValue setValue
     */

    /**
     * EvaluationContext 上下文，使用setRootObject方法来设置根对象，使用setVariable方法来注册自定义变量，使用registerFunction来注册自定义函数等
     * StandardEvaluationContext：标准上下文（最常用）
     */

    /**
     * 使用步骤：
     * 1. 创建解析器:创建解析器ExpressionParser(如:SpelExpressionParser)
     * 2. 解析表达式:使用ExpressionParser的parseExpression来解析表达式得到表达式对象Expression(如:SpelExpression)。
     * 3. 构造上下文:创建上下文EvaluationContext(如:StandardEvaluationContext), 设置需要的数据。
     * 4. 得到值:通过Expression的getValue方法根据上下文获得表达式值。
     */

    public static ExpressionParser parser = new SpelExpressionParser();

    public static void main(String[] args) throws NoSuchMethodException{
        // 1.支持基本表达式: 字面量表达式 \ 算数运算表达式 \ 关系运算表达式 \ 逻辑运算表达式 \ 字符串连接及截取表达式 \ 三目运算 \ Elivis表达式 \ 正则表达式
        log.info(String.valueOf(parseObj("true", Boolean.class))); // true
        log.info(String.valueOf(parseObj("1+1+2+1", Integer.class))); // 5
        log.info(String.valueOf(parseObj("3>2", Boolean.class))); // true
        log.info(String.valueOf(parseObj("2>1 and false", Boolean.class))); // false
        log.info(String.valueOf(parseObj("'hello java'[0]", String.class))); // h
        log.info(String.valueOf(parseObj("3>2 ? true : false", Boolean.class))); // true

        // 2.支持类相关表达式: 类类型 类实例 instanceof 获取对象属性 赋值
        // SpEL支持使用T(Type)来表示java.lang.Class实例
        Class aClass = parseObj("T(com.faker.project.schedule.JDKTimer)", Class.class);
        log.info(Arrays.toString(aClass.getMethods())); // JDKTimer的方法
        Date date = parseObj("new java.util.Date()", Date.class);
        log.info(DateFormat.getDateInstance().format(date.getTime())); // 当前时间
        log.info(String.valueOf(parseObj("'str' instanceof T(String)", Boolean.class))); // true
        // 获取对象属性
        Entity faker = new Entity("no.1", "faker", new Entity.DataInfo());
        EvaluationContext context = new StandardEvaluationContext(faker);
        log.info(parser.parseExpression("id").getValue(context, String.class)); // no.1
        // 赋值
        log.info(parser.parseExpression("id = 'no.2'").getValue(context, String.class)); // no.2
        log.info(faker.getId()); // no.2



    }

    // 计算exp, 返回要求的clazz
    public static <T> T parseObj(String exp, Class<T> clazz) {
        return parser.parseExpression(exp).getValue(clazz);
    }
}
