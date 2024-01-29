#!/bin/bash

user=$1
read -sp 'Password: ' password

# Source directory
source_directory="/workspaces/trace-coordinator/traces/hpc-soma-1400000"

# Host file containing the list of servers (one per line)
host_file="/workspaces/trace-coordinator/benchmarks/hosts"

# Group to distribute folders to
target_group="workers"


# Function to distribute folders to servers
function distribute_folders() {
  local source_folder="$1"
  local target_server="$2"
  local target_folder="$3"

  # SSH command to create the directory on the target server
  sshpass -p $password ssh "$user@$target_server" "mkdir -p $target_folder"

  # Copy folders to the target server using scp
  sshpass -p $password scp -r "$source_folder" "$user@$target_server:$target_folder"

  if [ $? -eq 0 ]; then
    echo "Folders $source_folder successfully copied to $target_server"
  else
    echo "Error copying folders $source_folder to $target_server"
  fi
}

function benchmark() {
	local benchmark_name="$1"
  	local folders=("${!2}")
  	local workers=("${!3}")
	size=${#workers[@]}

	echo $benchmark_name
	echo "${#folders[@]} traces folder"
	echo "$size workers"
	# Distribute folders to servers
	for ((i=0; i<${#folders[@]}; i++))
	do
		distribute_folders "${folders[i]}" "${workers[i % $size]}" "/home/tmp/'$benchmark_name'"
	done
}


# Check if the host file exists
if [ ! -f "$host_file" ]; then
  echo "Host file '$host_file' not found."
  exit 1
fi

# Read server addresses from the Ansible host file
one_workers_one_trace=(l4714-02.info.polymtl.ca
		l4714-03.info.polymtl.ca
		l4714-04.info.polymtl.ca
		l4714-05.info.polymtl.ca 
		l4714-06.info.polymtl.ca
		l4714-07.info.polymtl.ca
		l4714-08.info.polymtl.ca
		l4714-13.info.polymtl.ca
		l4714-14.info.polymtl.ca
		l4714-16.info.polymtl.ca
		l4714-17.info.polymtl.ca
		l4714-18.info.polymtl.ca
		l4714-19.info.polymtl.ca
		l4714-20.info.polymtl.ca
		l4714-21.info.polymtl.ca
		l4714-22.info.polymtl.ca
		l4714-23.info.polymtl.ca
		l4714-25.info.polymtl.ca
		l4714-26.info.polymtl.ca
		l4714-27.info.polymtl.ca)


one_workers_two_traces=(l4714-02.info.polymtl.ca
		l4714-03.info.polymtl.ca
		l4714-04.info.polymtl.ca
		l4714-05.info.polymtl.ca 
		l4714-06.info.polymtl.ca
		l4714-07.info.polymtl.ca
		l4714-08.info.polymtl.ca
		l4714-13.info.polymtl.ca
		l4714-14.info.polymtl.ca
		l4714-16.info.polymtl.ca)

one_workers_four_traces=(l4714-02.info.polymtl.ca
		l4714-03.info.polymtl.ca
		l4714-04.info.polymtl.ca
		l4714-05.info.polymtl.ca 
		l4714-06.info.polymtl.ca)


one_workers_20_traces=(l4714-02.info.polymtl.ca)

# Source directory
source_directory="/workspaces/trace-coordinator/traces/hpc-soma-140000"

# List all folders in the source directory
folders=("$source_directory"/*)

# benchmark "benchmark-1" folders[@] one_workers_one_trace[@]
# benchmark "benchmark-2" folders[@] one_workers_two_traces[@]
# benchmark "benchmark-3" folders[@] one_workers_four_traces[@]
# benchmark "benchmark-4" folders[@] one_workers_20_traces[@]

# Source directory
source_directory="/workspaces/trace-coordinator/traces/hpc-soma-1400000"

# List all folders in the source directory
folders=("$source_directory"/*)

benchmark "benchmark-5" folders[@] one_workers_one_trace[@]
benchmark "benchmark-6" folders[@] one_workers_two_traces[@] 
benchmark "benchmark-7" folders[@] one_workers_four_traces[@] 
benchmark "benchmark-8" folders[@] one_workers_20_traces[@]
