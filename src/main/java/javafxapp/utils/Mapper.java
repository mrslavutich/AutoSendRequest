package javafxapp.utils;

import javafxapp.controller.SmevController;

import java.lang.reflect.*;
import java.util.*;

public class Mapper {

    public Object instanceElementForList(Field field, Object parent){
        Type type = field.getGenericType();
        if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            for (Type t : pt.getActualTypeArguments()) {
                try {
                    Class<?> innerClass = Class.forName(((Class) t).getName());
                    Constructor<?> ctor = innerClass.getDeclaredConstructor(parent.getClass());
                    return createEmptyInsctance(ctor.newInstance(parent));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }


    static final Integer FIRST_AFRS = 0;

    public Object createEmptyInsctance(Object object) {
        Class cl = object.getClass();
        for (Method currentMethod : cl.getMethods()) {
            if (currentMethod.getName().startsWith("set")) {
                String parametrMethod = currentMethod.getParameterTypes()[FIRST_AFRS].getName();
                if (parametrMethod.equals("java.lang.String")) {
                    try {
                        currentMethod.invoke(object, "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        continue;
                    }
                }
                if (parametrMethod.equals("java.util.List")) {
                    try {
                        currentMethod.invoke(object, new ArrayList<Object>());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        continue;
                    }
                }
                try {
                    Object inner = Class.forName(parametrMethod).newInstance();
                    createEmptyInsctance(inner);
                    currentMethod.invoke(object, inner);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    continue;
                }
            }
        }

        return object;
    }

    public Object fillSmevPojo(HashMap jsonValues, Object pojo) throws Exception {
        createEmptyInsctance(pojo);
        Field field = pojo.getClass().getField("SenderCode");
        field.set(pojo, SmevController.senderCodeFNS.getText());
        field = pojo.getClass().getField("SenderName");
        field.set(pojo, SmevController.senderNameFNS.getText());
        field = pojo.getClass().getField("RecipientCode");
        field.set(pojo, SmevController.recipientCodeFNS.getText());
        field = pojo.getClass().getField("RecipientName");
        field.set(pojo, SmevController.recipientNameFNS.getText());
        field = pojo.getClass().getField("OriginatorCode");
        field.set(pojo, SmevController.originatorCodeFNS.getText());
        field = pojo.getClass().getField("OriginatorName");
        field.set(pojo, SmevController.originatorNameFNS.getText());

        if (jsonValues == null) return pojo;
        Set<String> keys = jsonValues.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            try {

                fillValue(jsonValues.get(key), key, pojo);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        return pojo;
    }

    public Object fillValue(Object value, String key, Object pojo) throws IllegalAccessException, NoSuchFieldException {

        Field field;
        if ((value instanceof String || value instanceof Long) && checkClassContainsField(pojo, key)) {

                field = pojo.getClass().getField(key);
                field.set(pojo, value.toString());


        }

        if(value instanceof ArrayList && checkClassContainsField(pojo, key) ) {
            field = pojo.getClass().getField(key);
            ArrayList<Object> list = (ArrayList)field.get(pojo);
            List elementValue = (List) value;
            for(Object element : elementValue) {
                Object listElelment = instanceElementForList(field, pojo);
                fillValue(element, null, listElelment);
                list.add(listElelment);
            }
        }

        if(value instanceof HashMap) {
            Set<String> keys = ((HashMap)value).keySet();
            Iterator<String> iterator = keys.iterator();
            Object fillObject = pojo;
            if(key != null && checkClassContainsField(pojo,key)) {
                field = pojo.getClass().getField(key);
                fillObject = field.get(pojo);
            }
            while (iterator.hasNext()) {
                String keyValue = iterator.next();
                fillValue(((HashMap)value).get(keyValue), keyValue, fillObject);
            }
        }

        return pojo;
    }
    private  boolean checkClassContainsField(Object pojo, String key){
       Field[] filelds=pojo.getClass().getFields();
       boolean isFound=false;
        for(Field filed:filelds){
            if(filed.getName().equals(key))
            isFound=true;
        }
        return isFound;

    }

}
