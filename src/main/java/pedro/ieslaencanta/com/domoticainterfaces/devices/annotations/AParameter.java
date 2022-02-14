/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/AnnotationType.java to edit this template
 */
package pedro.ieslaencanta.com.domoticainterfaces.devices.annotations;

/**
 *
 * @author Pedro
 */
public @interface AParameter {
    public String name() default "";
    public enum ParameterType{
        INT,
        FLOAT,
        STRING,
        BOOLEAN,
        LONG,
        VOID
    }
    public ParameterType type();
}
