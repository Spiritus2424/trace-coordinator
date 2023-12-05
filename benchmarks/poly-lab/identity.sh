#!/bin/bash
File="hosts"
Hosts=$(cat $File)
user=$1
read -sp 'Password: ' password
for Host in $Hosts
do	
	echo $user@${Host}
	sshpass -p $password ssh-copy-id -i ~/.ssh/id_rsa.pub -o StrictHostKeyChecking=no $user@${Host}
done
