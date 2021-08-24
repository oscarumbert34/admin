#!/usr/bin/env bash
cd /home/ec2-user/server/admin-core

sudo rm -rf /home/ec2-user/server/admin-core/admin-service.pid

echo"eliminando arcchivo"

sudo java -jar -Dspring.profiles.active=prod -Dspring.datasource.url=jdbc:mysql://clickescuela.ccmmeszml0xl.us-e$
 admin-core-0.0.1-SNAPSHOT.jar > /dev/null 2> /dev/null < /dev/null & echo $! > admin-service.pid