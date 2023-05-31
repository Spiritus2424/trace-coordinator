#!/bin/bash
File="hosts"
Hosts=$(cat $File)
read -sp 'Password: ' password
for Host in $Hosts
do	
	echo $USER@${Host}
	sshpass -p $password ssh-copy-id -i ~/.ssh/id_rsa.pub $USER@${Host}
done
