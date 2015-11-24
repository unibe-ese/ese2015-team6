# ese2015-team6
##Prototype: 
https://www.fluidui.com/editor/live/preview/p_11b002272439085d2f39b9f67bd5014e.1444673932913

## Setup guides:
### Prerequisites
- *[XAMPP][1]*, *[WAMP][2]* or sth similar that lets you run a *MySQL* server locally. (Apache and PHP support may be needed)

### Using Eclipse
1. Import the project via **File >Import... >Maven >Existing Maven Projects**
  - After importing the project you may need to update the project with Alt+F5
2. Set up the run configuration
  - Right click on the project
  - **Run as > Maven build...**
  - Add the following to the goals: `jetty:stop jetty:run`
3. Run the new configuration

### Running the servlet from console
1. Install [Apache Maven](https://maven.apache.org/) and configure it to be run in console (you can follow [this guide][3])
2. Using the command line, browse into the tutorfinder directory
3. Execute `mvn jetty:run`

[1]: https://www.apachefriends.org/
[2]: http://www.wampserver.com/en/
[3]: http://www.tutorialspoint.com/maven/maven_environment_setup.htm

## Sample Data:
Import the _tutorfinderdb_.sql file from the Sample Data folder into you database and you will 
have the follow profiles as demo.

* ##### Email / Password / Role
  * ese@example.ch / software / Tutor
  * student@example.ch / software / Student
  * tutor@example.ch / software / Tutor

* #####  Available Subjects:
  * ESE
  * Programming 1
  * Programming 2
  * Management
  * Lineare Algebra
