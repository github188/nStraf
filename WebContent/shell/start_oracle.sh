#!/bin/bash
  echo "start oracle....................."
  lsnrctl start
  sleep 6
  dbstart
  echo "success start oracle "