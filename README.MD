# Repositorio para proyecto de reporteo

## Instrucciones :

### *Compilar*
> javac -classpath ".;C:\Users\server\Desktop\computo\eclipse-workspace\finalXML\src\finalXML\libs\*;C:\xampp\tomcat\lib\*"  *.java XmlFinal/*.java

### *Ejecutar Servidor*
> start java -cp ".;C:\Users\server\Desktop\computo\eclipse-workspace\finalXML\src\finalXML\libs\*;C:\xampp\tomcat\lib\*" XMLEstructServer -ORBInitialPort 3400 -ORBInitialHost localhost

### *Ejecutar cliente*
> start java -cp ".;C:\Users\server\Desktop\computo\eclipse-workspace\finalXML\src\finalXML\libs\*;C:\xampp\tomcat\lib\*" cliente -ORBInitialPort 3400 -ORBInitialHost localhost
