#!/bin/bash
# ------------------------------------------------------------------------
#
# Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
#
#   WSO2 Inc. licenses this file to you under the Apache License,
#   Version 2.0 (the "License"); you may not use this file except
#   in compliance with the License.
#   You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#   Unless required by applicable law or agreed to in writing,
#   software distributed under the License is distributed on an
#   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
#   KIND, either express or implied.  See the License for the
#   specific language governing permissions and limitations
#   under the License.
#
# ------------------------------------------------------------------------



# wait until container runs
running_containers=$(docker ps | sed '1d' | awk '{print $NF}')
while [ -z "$running_containers" ]; do
echo "no running containers yet, sleeping for 1s"
sleep 1
running_containers=$(docker ps | sed '1d' | awk '{print $NF}')
done

echo "Runing Container : $running_containers"

echo "Starting test run"

echo "Test-1 performing"
# Security check 01 : 4.1  - Create a user for the container
user=$(docker ps --quiet -a | xargs docker inspect --format '{{.Config.User}}')
test1="pass"
if [ "$user" = "" ] || [ "$user" = " " ] || [ "$user" = "[]" ]; then
        test1="fail";
fi

echo "Test-2 performing"
# Security check 02 : 5.5 Do not mount sensitive host system directories on containers
volumes=$(docker ps --quiet -a | xargs docker inspect --format 'Volumes={{ .Mounts }}')
sensitive_dirs='/boot
/dev
/etc
/lib
/proc
/sys
/usr'


for v in $sensitive_dirs; do
test2="pass"
if [ $volumes = *"$v"* ]
then
        test2="fail";
        break;
fi
done


echo "Test-3 performing"
# Security check 03 : 5.6 Do not run ssh within containers
container_name=$(docker ps | sed '1d' |  awk '{print $NF}')
processes=$(docker exec "$container_name" ps -el 2>/dev/null | grep -c sshd | awk '{print $1}')
test3="pass"
if [ "$processes" -ge 1 ]; then
        test3="fail"
fi


echo "Tests completed, publishing results..."

# =========================
# publishing test results
resultsJson="{\"TEST01\":\"$test1\",\"TEST02\":\"$test2\",\"TEST03\":\"$test3\"}"
echo "Results json : $resultsJson"
loginEndPoint="site/blocks/user/login/ajax/login.jag"
echo "Login Endpoint $loginEndPoint"
adminEndPoint="site/blocks/admin/admin.jag"
echo "Admin Endpoint $adminEndPoint"
curl -c cookies -v -X POST -k $APPCLOUD_URL$loginEndPoint -d "action=login&userName=$ADMIN_USERNAME&password=$ADMIN_PASSWORD"

curl -b cookies  -v -X POST -k $APPCLOUD_URL$adminEndPoint -d "action=publishDockerSecurityTestResults&testResultsJson=$resultsJson"

echo "Done...!!!!"