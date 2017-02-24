#!/bin/bash

if [ -z "$1" ]; then
    exit 0;
fi
 
# Constant
fileType_Properties=properties;

# decode user input
userInputDecoded=$(echo -n "$1" | base64 -d);

# Get all changes
IFS='|' read -r -a fileChanges <<<$userInputDecoded;


function do_properties_file_modify {
 # Seperate the pattern and replacing value
 IFS='=' read -r -a tmp <<<$1;
 # Removing special characters
 local pattern=$(echo "$(echo "${tmp[0]}" | sed -e 's/\//\\\//g')" | sed -e 's/\$/\\$/');
 local replace=$(echo "$(echo "${tmp[1]}" | sed -e 's/\//\\\//g')" | sed -e 's/\$/\\$/');
 sed -i "/$pattern/c$pattern=$replace" "$file.$fileType";
}

function do_xml_file_modify {

 # Separate the  pattern and replacing value
 IFS='*' read -r -a tmp_xml <<<$1;

 # Replace / and $
 local pattern=$(echo "$(echo "${tmp_xml[0]}" | sed -e 's/\//\\\//g')" | sed -e 's/\$/\\$/');
 local replace=$(echo "$(echo "${tmp_xml[1]}" | sed -e 's/\//\\\//g')" | sed -e 's/\$/\\$/');

 IFS='%' read -r -a tmp_pattern <<< $pattern;
 IFS='%' read -r -a tmp_replace <<< $replace;

 local startLine=$(sed -n -e "/${tmp_pattern[0]}/ =" "$file.$fileType");
 local endLine=$(($startLine+${#tmp_pattern[@]}i-1));

 local replace_index=0;
 for var in "${tmp_pattern[@]}"
  do
     local replace_word=${tmp_replace[$replace_index]};
     sed -i -e "$startLine,$endLine s/$var/$replace_word/" "$file.$fileType";
     replace_index=$((replace_index+1));
  done

}

function do_file_modify {
 # Read the argument in to an array
 IFS='~' read -r -a userInput <<<$1;

 local fileType=${userInput[0]};
 local file=${userInput[1]};

 # Remove file and file name
 # Param=val array
 unset userInput[0];
 userInput=( "${userInput[@]}" );
 unset userInput[0];
 userInput=( "${userInput[@]}" );


 for var in "${userInput[@]}"
   do
    if [ "${fileType}" = "$fileType_Properties" ]; then
	do_properties_file_modify $var;
     else
       do_xml_file_modify "$var";
   fi
   done
}

for var in "${fileChanges[@]}"
  do
    do_file_modify "$var";
  done


