#!/bin/bash
# File="hosts"
# Hosts=$(cat $File)
# read -sp 'Password: ' password
# for Host in $Hosts
# do	
# 	echo $USER@${Host}
# 	sshpass -p $password ssh-copy-id -i ~/.ssh/id_rsa.pub -o StrictHostKeyChecking=no $USER@${Host}
# done

read -p 'Traes Directory: ' directory

# Check if the target is not a directory
if [ ! -d "$directory" ]; then
    echo "Not a directory"
    exit 1
fi

for trace in "$directory"/*
do
    if [ -d "$trace" ]; then
        echo ${trace}
    fi
done