@/* DialogData deutschtracker Starting point script */;

@/* Clear the screen */;
clear;

@/* Script is automated ( will take default answers ) */;
set ACCEPT_DEFAULTS true;

@/* Create a new project in the current directory */;
new-project --named deutschtracker --topLevelPackage de.dialogdata.aclabs;

@/* Turn our Java project into a Web project with JSF, CDI, EJB, and JPA  */;
scaffold setup --scaffoldType faces;

@/* Create persistence unit to connect to our MySQL datasource */;
persistence setup  --provider HIBERNATE --jndiDataSource java:jboss/datasources/dt-mysql --container JBOSS_AS7 --database MYSQL


@/* Create User entity */;
entity --named UserBE --package ~.entities;
field string --named firstName;
field string --named lastName;
field string --named userName;
field string --named password;
field temporal --type DATE --named lastLogin;

@/* Leave User Class */;
cd ..;

@/* Create an Enum */;
java new-enum-type --named Level --package ~.enums;
java new-enum-const A1;
java new-enum-const A2;
java new-enum-const B1;
java new-enum-const B2;
java new-enum-const C1;
java new-enum-const C2;

@/* Go home, you-re drunk! */;
cd ~~;

@/* Create Group entity */;
entity --named GroupBE --package ~.entities;
field string --named name;
field custom --named level --type de.dialogdata.aclabs.enums.Level.java;

@/* go back one step */;
cd ..;

@/* Select User again */;
cd UserBE.java;

@/* Add relationship */;
field manyToOne --named group --fieldType de.dialogdata.aclabs.entities.GroupBE.java --inverseFieldName users;


@/* Generate the UI for all of our Entities at once */;
scaffold from-entity ~.entities.* --scaffoldType faces --overwrite;
cd ~~;

@/* Build the project */;
build;

@/* These are not the droids you are looking for */;
clear;

set ACCEPT_DEFAULTS false;

@/* User input now required for JBOSS setup */;
echo "-------------------------------------------------";
echo "---------- User Input now required --------------";
echo "--- When promted for input -press Enter twice ---";
echo "--- For 3rd question type '/jboss' --------------";
echo "-------------------------------------------------";
as7 setup;

@/* Script is automated ( will take default answers ) */;
set ACCEPT_DEFAULTS true;

as7 start;
as7 deploy;

@/* Return to the project root directory and leave it in your hands */;
cd ~~;

clear;


echo "------------------------------------------------------------------------------";
echo "-------------------EPIC FORGE, SUCH MAGIC, MUCH WOW --------------------------";
echo "-- Check out your project at 'http://localhost:8080/deutschtracker' ----------";
echo "-- To see the code open a new terminal and run 'ee' command to open eclipse --";
echo "-- Now Click 'File' -> 'Import' -> 'Existing Maven Project' -> ---------------";
echo "-- -> Root Directory '/home/dialogdata/ACLabs/workspace/projects' ------------";
echo "------------------------------------------------------------------------------";
echo "-------------- You now have a starting point for your quest-------------------";
echo "---- Good luck in developing the rest! May the odds be ever in your favor ----";
echo "------------------------------------------------------------------------------";

