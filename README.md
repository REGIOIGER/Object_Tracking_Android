Object_Tracking_Android
=======================

Using openCV and android for tracking objects (first step for make some robot).

Basicamente es el OpenCV Tutorial2 - Mixed Processing que acompaña la descarga de OpenCV y en lugar de usar el jni de FindFeatures lo remplace por uno que llame ObjectTrack que es en su mayoria codigo de Kyle Hounslow (2013) que usando treshold y algunos criterios puede seguir un objeto y marcarlo con una crossair y mostrar sus coordenadas en pantalla. Quiero usar este codigo en un telefono con android conectado a un microcontrolador para construir un robot que sea capaz de seguir objetos.

Basically this is the "OpenCV Tutorial2 - Mixed Processing" that accompanies the download of OpenCV and instead of using the jni FindFeatures code I has replaced it by a ObjectTrack code, I take the code from Hounslow Kyle (2013) project that using some criteria and treshold can track an object and mark a crossair on it. The purpose is use this code on a android phone connected to a microcontroller to build a cell robot that is able to track objects.


Kyle Hounslow object tracking tutorial
http://www.youtube.com/watch?v=bSeFrPrqZ2A


Important file to replace in the original tutorial:
Tutorial2Activity.java
Android.mk
jni_prog_00.cpp

Next steps-> add trackbars or buttons for treshold like in the Kyle Hounslow project.
