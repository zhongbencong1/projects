package com.faker.project.schedule;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.faker.project.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * spel表达式使用, 使用流程
 * 1. 创建解析器:创建解析器ExpressionParser(如:SpelExpressionParser)
 * 2. 解析表达式:使用ExpressionParser的parseExpression来解析表达式得到表达式对象Expression(如:SpelExpression)。
 * 3. 构造上下文:创建上下文EvaluationContext(如:StandardEvaluationContext), 设置需要的数据。
 * 4. 得到值:通过Expression的getValue方法根据上下文获得表达式值。
 */
@Slf4j
@Data
@NoArgsConstructor
public class SpelExpComplex {
    // 表达式解析器: 负责解析表达式字符串, 将字符串表达式解析为表达式对象
    private ExpressionParser expressionParser;

    // 模板上下文
    private ParserContext parserContext;

    // 上下文: 使用setRootObject方法来设置根对象, 使用setVariable方法来注册自定义变量, 使用registerFunction来注册自定义函数等
    private EvaluationContext evaluationContext;

    // 表达式: 提供getValue方法用于获取表达式值, 提供setValue方法用于设置对象值
    private Expression expression ;

    // 解析: 将source中的值赋予template中的变量值, 转为clazz类型返回
    public <T> T parse(String template, Object source, Class<T> clazz) {
        Expression expression = expressionParser.parseExpression(template, parserContext);
        if (ObjectUtil.isNotEmpty(source)) {
            return expression.getValue(evaluationContext, source, clazz);
        } else {
            return expression.getValue(evaluationContext, clazz);
        }
    }

    public static SpelExpComplex initSpelParser() {
        ApplicationContext applicationContext = SpringUtil.getApplicationContext();
        SpelExpComplex spelParser = new SpelExpComplex();
        if (ObjectUtil.isNotEmpty(applicationContext)) {
            spelParser.setEvaluationContext(applicationContext.getBean(EvaluationContext.class));
            spelParser.setExpressionParser(applicationContext.getBean(ExpressionParser.class));
        } else {
            StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
            // 设置属性访问为public权限,
            evaluationContext.addPropertyAccessor(new JsonPropertyAccessor());
            spelParser.setExpressionParser(new SpelExpressionParser(new SpelParserConfiguration()));
            spelParser.setEvaluationContext(evaluationContext);
        }

        // ParserContext接口, 会解析 以'${'开头 以'}$'结尾的表达式
        spelParser.setParserContext(new ParserContext() {
            @Override
            public boolean isTemplate() {
                return true;
            }

            @Override
            public String getExpressionPrefix() {
                return "${";
            }

            @Override
            public String getExpressionSuffix() {
                return "}$";
            }
        });
        return spelParser;
    }

    /**
     * 用类的方法计算json中的属性: T() 代表类
     * ${T(类).方法名(需要的参数路径)}$; jsonString中含有路径的参数, Boolean.class 需要返回的结果
     */
    public static void main(String[] args) {
        Entity entity = new Entity("no.01", "faker", new Entity.DataInfo("1887856", "faker@aliyun.com"));
        String jsonString = JSON.toJSONString(entity);


        SpelExpComplex spelExpComplex = initSpelParser();
        Boolean parse = spelExpComplex.parse("${T(org.apache.commons.lang.StringUtils).isBlank(data.phone)}$", jsonString, Boolean.class);
        log.info("spelExpComplex parse result: {}", parse);
    }

}
