/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/AnnotationType.java to edit this template
 */
package pedro.ieslaencanta.com.domoticainterfaces.devices.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Pedro
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AMethod  {
   
   
    public FIELDS_ACCESS direction();
    public AParameter return_type() default @AParameter(name = "",type = AParameter.ParameterType.VOID);
    public AParameter[] parameters() default {};
    
}
