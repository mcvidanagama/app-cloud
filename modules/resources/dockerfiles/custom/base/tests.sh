#!/bin/sh
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

# Security check 01 : 4.1  - Create a user for the container

user=$(docker ps --quiet | xargs docker inspect --format 'User={{.Config.User}}')
test1="true"
if [ "$user" = "User=" -o "$user" = "User=[]" -o "$user" = "User=<no value>" ]; then
        test1="false";
fi
echo "1-$test1" >> result.log

# Security check 02 : 5.5 Do not mount sensitive host system directories on containers

volumes=$(docker ps --quiet | xargs docker inspect --format 'Volumes={{ .Mounts }}')
sensitive_dirs='/boot
/dev
/etc
/lib
/proc
/sys
/usr'

for v in $sensitive_dirs; do

test2="true"
if [[ $volumes == *"$v"* ]]
then
        test2="false";
        break;
fi
done
echo "2-$test2" >> result.log

# Security check 03 : 5.6 Do not run ssh within containers

container_name=$(docker ps | sed '1d' |  awk '{print $NF}')
processes=$(docker exec "$container_name" ps -el 2>/dev/null | grep -c sshd | awk '{print $1}')
test3="true"
if [ "$processes" -ge 1 ]; then
        test3="false"
fi

echo "2-$test3" >> result.log

