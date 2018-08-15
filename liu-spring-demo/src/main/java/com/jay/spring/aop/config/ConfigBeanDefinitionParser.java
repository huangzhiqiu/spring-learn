package com.jay.spring.aop.config;

import com.jay.spring.bean.BeanDefinition;
import com.jay.spring.bean.PropertyValue;
import com.jay.spring.bean.factory.config.RuntimeBeanReference;
import com.jay.spring.bean.factory.support.BeanDefinitionReaderUtils;
import com.jay.spring.bean.factory.support.BeanDefinitionRegistry;
import com.jay.spring.bean.factory.support.GenericBeanDefinition;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiang.wei on 2018/8/15
 *
 * @author xiang.wei
 */
public class ConfigBeanDefinitionParser {
    private static final String ASPECT = "aspect";
    private static final String EXPRESSION = "expression";
    private static final String ID = "id";

    private static final String POINTCUT = "pointcut";
    private static final String POINTCUT_REF = "pointcut-ref";
    private static final String REF = "ref";
    private static final String BEFORE = "before";
    private static final String AFTER = "after";
    private static final String AFTER_RETURNING_ELEMENT = "after-returning";
    private static final String AFTER_THROWING_ELEMENT = "after-throwing";
    private static final String AROUND = "around";
    private static final String ASPECT_NAME_PROPERTY = "aspectName";


    public void parse(Element ele, BeanDefinitionRegistry registry) {
        List<Element> childElts = ele.elements();
        for (Element childElt : childElts) {
            String localName = childElt.getName();
            if (ASPECT.equals(localName)) {
                parseAspect(childElt, registry);
            }
        }
    }

    private void parseAspect(Element aspectElement, BeanDefinitionRegistry registry) {
        //TODO  aspectId没用到
        String aspectId = aspectElement.attributeValue(ID);
        String aspectName = aspectElement.attributeValue(REF);

        List<BeanDefinition> beanDefinitions = new ArrayList<BeanDefinition>();
        List<RuntimeBeanReference> beanReferences = new ArrayList<RuntimeBeanReference>();

        List<Element> eleList = aspectElement.elements();
        boolean adviceFoundAlready = false;

        for (int i = 0; i < eleList.size(); i++) {
            Element ele = eleList.get(i);
            if (isAdviceNode(ele)) {

            }
        }
    }

    private boolean isAdviceNode(Element element) {
        String name = element.getName();
        return (BEFORE.equals(name) || AFTER.equals(name) || AFTER_RETURNING_ELEMENT.equals(name) ||
                AFTER_THROWING_ELEMENT.equals(name) || AROUND.equals(name));
    }

    private GenericBeanDefinition parseAdvice(String aspectName, int order, Element aspectElement, Element adviceElement, BeanDefinitionRegistry registry,
                                              List<BeanDefinition> beanDefinitions, List<RuntimeBeanReference> beanReferences) {
        GenericBeanDefinition methodDefinition = new GenericBeanDefinition(MethodLocatingFactory.class);
        methodDefinition.getPropertyValues().add(new PropertyValue("targetBeanName", aspectName));
        methodDefinition.getPropertyValues().add(new PropertyValue("methodName", adviceElement.attributeValue("method")));
        methodDefinition.setSynthetic(true);

        // create instance factory definition
        GenericBeanDefinition aspectFactoryDef = new GenericBeanDefinition(AspectInstanceFactory.class);
        aspectFactoryDef.getPropertyValues().add(new PropertyValue("aspectBeanName", aspectName));
        aspectFactoryDef.setSynthetic(true);
        // register the pointcut
        GenericBeanDefinition adviceDef = createAdviceDefinition(adviceElement, registry, aspectName, order, methodDefinition, aspectFactoryDef,
                beanDefinitions, beanReferences);
        adviceDef.setSynthetic(true);

        // register the final advisor
        BeanDefinitionReaderUtils.registerWithGeneratedName(adviceDef, registry);
        return adviceDef;
    }

    /**
     * Creates the RootBeanDefinition for a POJO advice bean. Also causes pointcut
     * parsing to occur so that the pointcut may be associate with the advice bean.
     * This same pointcut is also configured as the pointcut for the enclosing
     * Advisor definition using the supplied MutablePropertyValues.
     */
    private GenericBeanDefinition createAdviceDefinition(
            Element adviceElement, BeanDefinitionRegistry registry, String aspectName, int order,
            GenericBeanDefinition methodDef, GenericBeanDefinition aspectFactoryDef,
            List<BeanDefinition> beanDefinitions, List<RuntimeBeanReference> beanReferences) {
        // TODO: 2018/8/16
        return null;
    }
}
