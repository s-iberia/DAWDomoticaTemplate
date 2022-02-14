/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pedro.ieslaencanta.com.domoticainterfaces;

import com.sothawo.mapjfx.Configuration;
import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.MapLabel;
import com.sothawo.mapjfx.MapType;
import com.sothawo.mapjfx.MapView;
import com.sothawo.mapjfx.Marker;
import com.sothawo.mapjfx.Projection;
import com.sothawo.mapjfx.event.MapViewEvent;
import com.sothawo.mapjfx.event.MarkerEvent;
import static com.sun.glass.ui.Cursor.setVisible;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import pedro.ieslaencanta.com.domoticainterfaces.devices.annotations.ADevice;
import pedro.ieslaencanta.com.domoticainterfaces.devices.annotations.AMethod;
import pedro.ieslaencanta.com.domoticainterfaces.devices.Device;


/**
 *
 * @author Pedro
 */
public class Principal extends Application {

    private int width = 500;
    private int height = 500;
    private com.sothawo.mapjfx.MapView mapa;
    ContextMenu contextMenu;
    private HashMap<Marker, Device> devices;

    Marker markerKaHarbour;
    Marker seleccionado;
    // private DeviceExample device;

    @Override
    public void start(Stage stage) throws Exception {
        StackPane panel = new StackPane();
        Scene scene = new Scene(panel, this.width, this.height);
        //inicializa el mapa
        this.initmap();
        // create a menu
        this.initContextMenu();
        // this.mapa.initialize();
        panel.getChildren().add(this.mapa);
        scene.setOnContextMenuRequested(eh -> {
            contextMenu.show(stage, eh.getScreenX(), eh.getScreenY());
        });

        stage.setTitle("DAW Interfaces");
        stage.setResizable(false);
        stage.setScene(scene);
        //para que cierre al pulsar el icono
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
        stage.show();
    }

    private Marker addDevice(Device d, Coordinate c) {
        Marker tempo;
        //se obtiene el icono de la anotación
        String imagen=d.getClass().getAnnotation(ADevice.class).icon();
        tempo = new Marker(getClass().getResource("/"+imagen+".png"), -20, -20).setPosition(c)
                .setVisible(true);
        this.devices.put(tempo, d);
        return tempo;
    }

    private void initmap() {
        Device d;
        this.mapa = new MapView();
        this.mapa.setId("mapa");
        this.mapa.setCenter(new Coordinate(38.0883, -0.724255));
        this.devices = new HashMap<Marker, Device>();
       /*
       //luz
        d= new Light("Luces del polideportivo","polideportivo",false);
        this.mapa.addMarker(this.addDevice(d,new Coordinate(38.08617676979483, -0.7176315683413612)));
        //dimmer
        d= new Dimmer("Luces del Monte Calvario","monte",0);
        this.mapa.addMarker(this.addDevice(d,new Coordinate(38.083568436932396, -0.7271113615589764)));
        
        this.mapa.setCustomMapviewCssURL(getClass().getResource("/custom_mapview.css"));*/
        //cierra el menú contextual si está abierto
        this.mapa.addEventHandler(MapViewEvent.MAP_CLICKED, e -> {
            if (contextMenu.isShowing()) {
                contextMenu.hide();
            }
        }
        );
        //al hacer click en una marca crea le menu del objeto con los métodos posibles.
        this.mapa.addEventHandler(MarkerEvent.MARKER_RIGHTCLICKED, event -> {
            MenuItem t;
            Device tempo;
            event.consume();
            seleccionado = event.getMarker();
            tempo = devices.get(seleccionado);
            if (tempo != null) {
                contextMenu.getItems().remove(0, contextMenu.getItems().size());
                String[] metodos = tempo.getMethodAnnotation();
                for (int i = 0; i < metodos.length; i++) {
                    t = new MenuItem(metodos[i]);
                    t.setOnAction(ev -> {
                        ev.consume();
                        AMethod am = tempo.getAMethod(((MenuItem) ev.getSource()).getText());
                        Alert alerta;
                        alerta = new MethodDialog(tempo, ((MenuItem) ev.getSource()).getText(), am);
                        alerta.showAndWait();

                    });
                    contextMenu.getItems().add(t);
                }
            }
        });
        this.mapa.initializedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                Marker[] m = (Marker[]) this.devices.keySet().toArray(new Marker[0]);
                for (int i = 0; i < m.length; i++) //this.mapa.addMarker(markerKaHarbour);
                {
                    this.mapa.addMarker(m[i]);
                }
            }
        });
        // this.mapa.setMapType(MapType.BINGMAPS_AERIAL);
        this.mapa.initialize(Configuration.builder()
                .showZoomControls(true).interactive(true)
                .build());

    }
    //añadir el menú contextual y el evento cuando se cierra
    private void initContextMenu() {
        contextMenu = new ContextMenu();
        contextMenu.setOnHidden(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                seleccionado = null;

            }
        });

    }
}
