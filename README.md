#DeviceHiveDevice

DeviceHiveDevice is a simple application that runs on your android device. From the Admin panel on your hive
you can send various commands to device. For now are implemented only commands directed mainly to device equipment.
In this case device eqipment are the various sensors in the android device. They report their measurments to the hive.
You can check device batery level, device screen size, device time on and device gps position. These four are 
executed on device itself.

##DeviceHiveDevice Screens

* Info - displays various information about device
* Equipment - displays registerd device sensors in the hive
* Commands - displays recieved commands
* Notification - manual notification sender
* Setting - you can edit some of the preferences for the device

##DeviceHiveDevice Initial Setup

When the application is started for the first time it may show dialog with "Device not registered". You must go to Settings and manualy enter the API Endpoint. Restart the app and everything will go smooth.


Original sample project and DeviceHive Framework can be found on: http://devicehive.com
