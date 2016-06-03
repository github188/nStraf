#!/bin/bash
echo "You should be tm to execute this function."
user=$(whoami)
user1="tomcat"
echo "The current user is $user"

if [ $user != $user1 ]
then
echo "You are not tomcat now, please switch to tm first!"
exit
fi

echo "Starting Tomcat programs......................"
#echo "You may be required to input tomcat's password:"
#su - tomcat
cd /
startup.sh
echo "Start Tomcat Success!"
