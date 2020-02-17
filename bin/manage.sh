#!/usr/bin/env bash

echo CashVest Management Script

echo Select an Action
echo 1.  Build Application
echo 2.  Clean Application
echo 3.  Clean Database
echo 4.  Run Tests
echo 99. Quit


read -r selectedItem

if [ "$selectedItem" -eq 1 ]
  then cd .. && mvn clean && mvn package
elif [ "$selectedItem" -eq 2 ];
  then cd .. && mvn clean
elif [ "$selectedItem" -eq 3 ];
  then rm -rf ../target/h2db/db
elif [ "$selectedItem" -eq 4 ];
  then cd .. && mvn test
else
  echo Quitting . . .
fi



