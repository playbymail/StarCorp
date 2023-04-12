#summary Building from source
#labels Phase-Implementation

= Requirements =

In addition to the latest source distribution package you will need:
  * Java 1.5 or higher
  * Ant 1.7 or higher
  * Launch4j
  * NSIS

= Instructions =

The project has an Ant build script which should be used.  The default target compiles all source files, prepares the necessary jar files and libraries in the "dist" subfolder and build windows exe / installers for the client application.

For full details please examine the build.xml file.
