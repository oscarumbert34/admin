#!/bin/bash
sudo chmod +x /home/ec2-user/server/admin-core/logs
sudo chmod +x /home/ec2-user/server/admin-core/logs/error.log
sudo chmod +x /home/ec2-user/server/admin-core/logs/debug.log

var="$(cat /home/ec2-user/server/admin-core/admin-service.pid)"
sudo kill $var
sudo rm -rf /home/ec2-user/server/admin-core/admin-service.pid