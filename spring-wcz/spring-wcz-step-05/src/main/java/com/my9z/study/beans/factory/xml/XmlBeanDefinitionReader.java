package com.my9z.study.beans.factory.xml;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import com.my9z.study.beans.BeansException;
import com.my9z.study.beans.PropertyValue;
import com.my9z.study.beans.factory.config.BeanDefinition;
import com.my9z.study.beans.factory.config.BeanReference;
import com.my9z.study.beans.factory.support.AbstractBeanDefinitionReader;
import com.my9z.study.beans.factory.support.BeanDefinitionRegistry;
import com.my9z.study.core.io.Resource;
import com.my9z.study.core.io.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;

/**
 * @description: xml bean配置文件资源读取实现
 * @author: wczy9
 * @createTime: 2022-12-03  15:56
 */
public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        super(registry, resourceLoader);
    }

    @Override
    public void loadBeanDefinitions(Resource resource) throws BeansException {
        try (InputStream inputStream = resource.getInputStream()){
            doLoadBeanDefinitions(inputStream);
        } catch (ClassNotFoundException | IOException e) {
            throw new BeansException("IOException parsing XML document from " + resource, e);
        }
    }

    @Override
    public void loadBeanDefinitions(Resource... resources) throws BeansException {
        for (Resource resource : resources) {
            loadBeanDefinitions(resource);
        }
    }

    @Override
    public void loadBeanDefinitions(String location) throws BeansException {
        Resource resource = getResourceLoader().getResource(location);
        loadBeanDefinitions(resource);
    }

    protected void doLoadBeanDefinitions(InputStream inputStream) throws ClassNotFoundException {
        //读取xml文件
        Document document = XmlUtil.readXML(inputStream);
        Element root = document.getDocumentElement();
        NodeList childNodes = root.getChildNodes();
        //遍历节点读取bean
        for (int i = 0; i < childNodes.getLength(); i++) {
            //判断元素
            if (!(childNodes.item(i) instanceof Element)) continue;
            //判断bean标签
            if (!"bean".equals(childNodes.item(i).getNodeName())) continue;
            //1、读取bean对象
            Element bean = (Element) childNodes.item(i);
            //1.1、解析class对象路径
            String className = bean.getAttribute("class");
            //1.2、获取Class
            Class<?> clazz = Class.forName(className);
            //1.3、定义BeanDefinition
            BeanDefinition beanDefinition = new BeanDefinition(clazz);
            //2、读取bean的属性并填充
            for (int j = 0; j < bean.getChildNodes().getLength(); j++) {
                //判断元素
                if (!(bean.getChildNodes().item(j) instanceof Element)) continue;
                //判断property标签
                if (!"property".equals(bean.getChildNodes().item(j).getNodeName())) continue;
                //2.1、解析property标签
                Element property = (Element) bean.getChildNodes().item(j);
                //解析字段名
                String attrName = property.getAttribute("name");
                //解析字段值
                String attrValue = property.getAttribute("value");
                //解析字段引用
                String attrRef = property.getAttribute("ref");
                //2.2、确定字段值
                Object value = StrUtil.isNotEmpty(attrRef) ? new BeanReference(attrRef) : attrValue;
                //2.3、设置bean属性
                beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(attrName,value));
            }
            //3、注册BeanDefinition
            //解析bean对象注册id
            String id = bean.getAttribute("id");
            //解析bean对象注册名
            String name = bean.getAttribute("name");
            //3.1决定注册的beanName 优先级id>name
            String beanName = StrUtil.isNotEmpty(id) ? id : name;
            //如果配置文件中id和name属性都为空，则取name名
            if (StrUtil.isEmpty(beanName)) {
                beanName = StrUtil.lowerFirst(clazz.getSimpleName());
            }
            //3.2、判断当前bean是否以及注册
            if (getRegistry().containsBeanDefinition(beanName)){
                throw new BeansException("Duplicate beanName[" + beanName + "] is not allowed");
            }
            //3.3、注册BeanDefinition
            getRegistry().registerBeanDefinition(beanName,beanDefinition);
        }
    }
}