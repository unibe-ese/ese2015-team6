# ese2015-team6

##Prototype: 
 https://www.fluidui.com/editor/live/preview/p_11b002272439085d2f39b9f67bd5014e.1444673932913

##Setting up in eclipse
- Import the project via >File >Import... >Maven >Existing Maven Projects
- After importing the project you may need to update the project with Alt+F5
- For setting up the runing configuration
  - Right click on the project
  - >Run as > Maven build...
  - Add the follow to the goals: "jetty:stop jetty:run"
- Apply it and you can run it. 
- Be sure you have installed XAMPP or WAMP or something similar with Apache, Mysql and PHP. 

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
