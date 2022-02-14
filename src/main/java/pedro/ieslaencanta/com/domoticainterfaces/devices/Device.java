package pedro.ieslaencanta.com.domoticainterfaces.devices;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import pedro.ieslaencanta.com.domoticainterfaces.devices.annotations.AMethod;
import pedro.ieslaencanta.com.domoticainterfaces.devices.annotations.AParameter;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Pedro
 */


public abstract class Device {

    protected String description;
    protected String name;
    private HashMap<String, Method> metodos;

    public Device(String description, String name) {
        this.description = description;
        this.name = name;
        this.init();
    }

    private void init() {
        this.metodos = new LinkedHashMap<String, Method>();
        Class<?> clase = (Class<?>) this.getClass();
        for (int i = 0; i < clase.getInterfaces().length; i++) {
            Class<?> t = clase.getInterfaces()[i];
            for (Method method : t.getDeclaredMethods()) {
                if (method.isAnnotationPresent(AMethod.class)) {
                    metodos.put(method.getName(), method);//method.getAnnotation(AMethod.class));
                }
            }
        }
        String[] t = (String[]) metodos.keySet().toArray(new String[0]);
        for (int i = 0; i < t.length; i++) {
            System.out.println(t[i]);
        }
    }

    public Method getMethod(String method_name) {
        Method metodo = null;
        metodo = this.metodos.get(method_name);
        return metodo;
    }

 

    public String[] getMethodAnnotation() {

        return (String[]) metodos.keySet().toArray(new String[0]);
    }

    public AMethod getAMethod(String actionname) {
        AMethod metodo = null;
        metodo = this.metodos.get(actionname).getAnnotation(AMethod.class);

        return metodo;
    }

   
}
