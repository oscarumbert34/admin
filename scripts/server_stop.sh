#!/bin/bash
var="$(cat /home/ec2-user/server/admin-core/admin-service.pid)"
sudo kill $var
sudo rm -rf /home/ec2-user/server/admin-core/admin-service.pid