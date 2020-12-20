package com.southwind.ioc.impl;

import com.southwind.ioc.ApplicationContext;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ClassPathXmlApplicationContext implements ApplicationContext {
    private Map<String, Object> iocMap = new HashMap<String, Object>();

    public ClassPathXmlApplicationContext(String path) {
        try{
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read("./src/main/resources/"+path);
            Element rootElement = document.getRootElement();
            Iterator elementIterator = rootElement.elementIterator();
            while (elementIterator.hasNext()){
                Element bean = (Element)elementIterator.next();
                String idStr = bean.attributeValue("id");
                String classStr = bean.attributeValue("class");
                //使用反射机制创建对象
                Class aClass = Class.forName(classStr);
                Constructor constructor = aClass.getConstructor();
                Object object = constructor.newInstance();
                Iterator propertyIterator = bean.elementIterator();
                while (propertyIterator.hasNext()){
                    Element property = (Element) propertyIterator.next();
                    String nameStr = property.attributeValue("name");
                    String valueStr = property.attributeValue("value");
                    //使用反射机制获取set方法
                    String methodStr = "set"+nameStr.substring(0,1).toUpperCase()+nameStr.substring(1);
                    Field field = aClass.getDeclaredField(nameStr);
                    Method setMethod = aClass.getDeclaredMethod(methodStr, field.getType());
                    Object value = null;
                    switch(field.getType().getName()){
                        case "int":
                            value = Integer.parseInt(valueStr);
                            break;
                        case "long":
                            value = Long.parseLong(valueStr);
                            break;
                        case "java.lang.String":
                            value = valueStr;
                    }
                    setMethod.invoke(object, value);
                }
                iocMap.put(idStr, object);
            }
        }catch(DocumentException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }catch(NoSuchMethodException e){
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }

    public Object getBean(String id) {
        return iocMap.get(id);
    }
}
