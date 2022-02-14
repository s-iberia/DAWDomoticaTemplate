
# Interfaces y anotaciones básica en Java

Práctica para crear y aplicar interfaces así como uso de anotaciones para añadir metainformación a las clases

## Enunciado


El ayuntamiento ha instalado diferentes dispositivos que desea  controlar de forma remota, por ejemplo:
    -Luces.
    -Luces con intensidad.
    -Termómetros.
    -Persianas.
    -...
Para ello se tiene una clase ABSTRACTA Device que contiene diferentes métodos para poder llamar usando reflexión a los métodos de las clases que la heredan.

También se tiene una serie de anotaciones para añadir metainformación a las clases y que permitan ofrecer información para poder "pintarse" en el mapa sin necesidad de interactuar.

Las anotaciones que se tienen son:

    -ADevice, a nivel de clase y en tiempo de ejecución, la información es el icono a mostrar.
    -AMethod, información sobre el método, que contiene la dirección (READ,WRITE), lista de parámetros (de tipo AParameter) y valor devuelto también de tipo AParameter.
    -AParameter, nombre del parámetro y tipo (INT,FLOAT,BOOLEAN,STRING, VOID)

Se han de desarrollar diferentes interfaces para otros tantos dispositivos, en concreto:

    -ILight: Para los dispositivos que implementen la funcionalidad de una Bombilla(encender, apagar, consultar estado)
    -IDimmer: Similar a la anterior, pero se puede "jugar" con la intensidad.
    -IThermometer: Representa un termómetro.
    -IBlind: Persiana, ha de poder subir todo, bajar todo, consultar el estado, subir algo y bajar algo.

A los métodos de las interfaces se han de añadir anotaciones que sirvan para poder interactuar de desde la parte gráfica, por ejemplo el método getState de la interfaz ILight:

```java
 @AMethod(direction = FIELDS_ACCESS.READ, return_type = @AParameter(name="LigthState",type = AParameter.ParameterType.BOOLEAN))
    public boolean getLightState();
```
En el que se indica que es un método de lectura, y devuelve un booleano llamado LigthState

Un dispositivo se crea heredando de Device e implementando las interfaces necesarias, por ejemplo para implementar una Bombilla:

```java
    @ADevice(icon = "light")
    public class Light extends  Device implements ILigth{....
```
Los dispositivos también tienen una anotación, que indica el nombre del icono a mostar (imágenes en carpeta resources).
Se han de crear los dispositivos:

    -Ligth
    -Dimmer
    -Thermometer
    -Blind
    -Alarm, sistema de alarma que implementa la interfaz ILight, la interfaz IThermometer y si es necesario alguna otra..., cuando el termometro alcance una temperatura (se ha de ponder de forma manual) se ha de encender la luz.

En la clase principal se han de crear los objetos de cada dispositivo indicando las coordenada en la que han de aparecer:

```java
private void initmap() {
        Device d;
        this.mapa = new MapView();
        this.mapa.setId("mapa");
        this.mapa.setCenter(new Coordinate(38.0883, -0.724255));
        this.devices = new HashMap<Marker, Device>();
        //luz
        d= new Light("Luces del polideportivo","polideportivo",false);
        //añade el dispositivo al mapa en las coordenadas indicadas
        this.mapa.addMarker(this.addDevice(d,new Coordinate(38.08617676979483, -0.7176315683413612)));
        // resto de dispositivos
       
```

Por último se desea crear un dispositivo motor, que ha de implementar los métodos Start,Stop, incRPM, decRPM, getState y getRPM definiendo una interfaz, además a de implementar
la interfaz Runnable y crear un hilo, mostrando por pantalla cada segundo en caso de estar iniciado, las RPM actuales.

![image](https://raw.githubusercontent.com/pass1enator/DAWDomoticaTemplate/master/captura.png)
