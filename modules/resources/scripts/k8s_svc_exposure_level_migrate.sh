#!/bin/bash

#KUBERNETES_PATH="/home/appcloud/scripts/"   #162
KUBERNETES_PATH=""    #Production / Staging

exposure_level="public"     #public or private

kub_command="kubectl get namespaces"
getnamespaces_command=$KUBERNETES_PATH$kub_command

namespaces_output="$(eval $getnamespaces_command)"

row_number=0

while read -r line
do
        if [ "$row_number" -ne "0" ];then
                #echo "$line"
                tenant_name="$(cut -d ' ' -f1 <<< $line)"
                echo $tenant_name

               # set_exposure_level_command="kubectl label --overwrite svc --all exposure-level="$exposure_level" --namespace="$tenant_name
                set_exposure_level_command="kubectl label svc --all exposure-level="$exposure_level" --namespace="$tenant_name
                echo $set_exposure_level_command

                set_exposure_level_output="$(eval $KUBERNETES_PATH$set_exposure_level_command)"
                echo $set_exposure_level_output

        fi

        row_number=$((row_number+1))

done <<<"$namespaces_output"
