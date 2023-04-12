#summary Software Installation
#labels Phase-Deploy

= Client =

The [http://starcorp.googlecode.com/files/StarCorp%20Client-Installer%200.9.1.0.exe Windows installer] should be used and the instructions followed.

Other platforms are not currently supported for the client.  However, if there is demand for a particular platform support will be added (please leave a comment here).

= Server =

The server binary distribution requires some preparation before usage:

  * Download the [http://starcorp.googlecode.com/files/StarCorp-server-0.9.1.0.zip binary distribution].
  * Unzip to a convenient directory
  * Create a local mySQL database called starcorp.  _Currently the server assumes root / no password access to localhost running this database.  This is configured using the Hibernate configuration file.  Changing the database settings is beyond the scope of this document._
  * Create a copy of [http://starcorp.googlecode.com/svn/trunk/resources-server/sample.server.properties sample.server.properties] called server.properties in the root directory.  Fill in the appropriate property values for smtp host etc.
  * To launch the server shell use server.bat (for Windows) or java -cp -jar StarCorp-server-X.X.X.X.jar (where Xs are subtituted for the built version).