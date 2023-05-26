import yaml
def read_ansible_hosts(file_path, section_name):
    hosts = []
    with open(file_path, 'r') as f:
        in_section = False
        for line in f:
            line = line.strip()
            if line.startswith('[') and line.endswith(']'):
                if in_section:
                    break  # Exit loop if we've already processed the desired section
                if line[1:-1] == section_name:
                    in_section = True
                else:
                    in_section = False
            elif in_section and line:
                hosts.append(line.split()[0])
    return hosts

def generate_trace_coordinator_config_file(workers):
	config = {}
	trace_servers = []
	for worker in workers:
			trace_servers.append({
					"host": worker,
					"port": 8080,
					"traces-path": []})
                        
	config["trace-servers"] = trace_servers
	return config



# Replace 'hosts_file_path' with the actual path to your Ansible "hosts" file
hosts_file_path = './benchamrks/hosts'
section_name = 'workers'
workers = read_ansible_hosts(hosts_file_path, section_name)

# Print the workers data
for worker in workers:
    print(worker)
    


config = generate_trace_coordinator_config_file(workers)


file_path = 'output.yml'

# Write the data to a YAML file
with open(file_path, 'w') as f:
    yaml.dump(config, f)

print(f"YAML file '{file_path}' generated successfully.")
