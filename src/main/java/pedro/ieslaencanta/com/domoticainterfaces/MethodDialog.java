/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pedro.ieslaencanta.com.domoticainterfaces;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import pedro.ieslaencanta.com.domoticainterfaces.devices.annotations.AMethod;
import pedro.ieslaencanta.com.domoticainterfaces.devices.annotations.AParameter;
import pedro.ieslaencanta.com.domoticainterfaces.devices.annotations.FIELDS_ACCESS;
import pedro.ieslaencanta.com.domoticainterfaces.devices.Device;


/**
 *
 * @author Pedro
 */
public class MethodDialog extends Alert {

    private final AMethod method;
    private final Device d;
    private final Method m;
    private Control[] n;

    public MethodDialog(Device d, String method_name, AMethod method) {//, AlertType tipe) {
        //si es de solo lectura o de escritura
        super(method.direction() == FIELDS_ACCESS.READ ? AlertType.INFORMATION : AlertType.CONFIRMATION);

        this.method = method;
        this.d = d;
        this.m = d.getMethod(method_name);
        this.initWrite();
        //si es de escritura se tiene que procesar
        if (method.direction() == FIELDS_ACCESS.WRITE) {
            this.setOnCloseRequest(eh -> {
                if (this.getResult() == ButtonType.OK) {
                    try {
                        Object[] o = new Object[this.n.length];
                        //se obtienen los parametros, QUIZAS PASAR EL PARAMETRO¿?
                        for (int i = 0; i < n.length; i++) {
                            try {
                                o[i] = getValue(n[i], method.parameters()[i]);
                            } catch (Exception ex) {
                                Logger.getLogger(MethodDialog.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        //se invoca al procedimiento
                        this.m.invoke(d, o);
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(MethodDialog.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalArgumentException ex) {
                        Logger.getLogger(MethodDialog.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InvocationTargetException ex) {
                        Logger.getLogger(MethodDialog.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        } else {
            if (method.direction() == FIELDS_ACCESS.READ) {
                this.initRead();
            }
        }
    }

    private void initWrite() {
        Label label;
        Node t;
        GridPane contenido = new GridPane();
        contenido.setMaxWidth(Double.MAX_VALUE);
        this.n = new Control[this.method.parameters().length];
        //alert.getDialogPane().setExpandableContent(expContent);
        for (int i = 0; this.method != null && i < this.method.parameters().length; i++) {
            label = new Label(this.method.parameters()[i].name());
            t = this.getControl(this.method.parameters()[i].type());
            contenido.add(label, 0, i);
            contenido.add(t, 1, i);
            this.n[i] = (Control) t;
            //   expContent.add(textArea, 0, 1);

        }
        this.getDialogPane().setContent(contenido);

    }

    private void initRead() {
        try {
            Label label, label2;
            Node t;
            GridPane contenido = new GridPane();
            //contenido.setPadding(new Insets(20,20,20,20));
            contenido.setMaxWidth(Double.MAX_VALUE);
            label = new Label(this.method.return_type().name()+"     ");
            label2 = new Label(this.m.invoke(this.d).toString());
            contenido.add(label, 0, 0);
            contenido.add(label2, 1, 0);

            this.getDialogPane().setContent(contenido);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MethodDialog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(MethodDialog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(MethodDialog.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private Node getControl(AParameter.ParameterType parameter) {
        Node tempo = null;
        AParameter.ParameterType tipo = parameter;
        switch (tipo) {
            case INT:
                Slider s = new Slider(0, 100, 0);
                s.setBlockIncrement(1);
                tempo = s;
                break;
            case BOOLEAN:
                tempo = new CheckBox();
                break;
            case LONG:
                break;
            case FLOAT:
                tempo = new Slider(0.0, 100.0, 0);
                break;
            case STRING:
                tempo = new TextField();
                break;
        }
        return tempo;
    }

    private <T> T getValue(Control control, AParameter ap) throws Exception {
        if (control instanceof Slider ){
            if(ap.type()==AParameter.ParameterType.INT) {
            return (T) ((Integer) (int) ((Slider) control).getValue());
            }else{
                 return (T) ((Double) (double) ((Slider) control).getValue());
            }
        } else {
            if(control instanceof TextField){
                return (T) ((String) ((TextField) control).getText());
            }
            else{
                if(control instanceof CheckBox){
                    return (T) ((Boolean) ((CheckBox) control).isSelected());
                }else{
                    throw  new Exception("No es posible obtener el valor para el campo");
                }
            }
            
        }
    }
}
