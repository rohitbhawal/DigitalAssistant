# DigitalAssistant
Android application which allows users to trigger task based on the GPS coordinate set for the task and the proximity


Wifi or bluetooth sensor based event triggering system is common but it is not useful all the time especially when same wifi network name is used around entire campus or area. 
In order to trigger events based on specific location GPS coordinates is the best way to achieve more accurate triggering system.

It is a prototype application which allows user to add a task and assign a date or day of week on which you want to trigger the task and also assign GPS location and proximity to it.
Developed android mobile application for creating task acting as frontend, developed web service which is hosted in AWS EC2 and used AWS RDS for DB like storing user information and task information.
Proximity distance is calculated between user current location and task's assigned location so that the triggering mechanism works.

What's next for Digital Assistant
Can be used to trigger any actions like opening application, sending messages, reminder, changing mobile settings (volume), etc

Built With
java
python
flask
google-maps
amazon-web-services
ec2
amazon-rds-relational-database-service
eclipse
android-studio
