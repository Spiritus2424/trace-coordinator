# Run SPEC benchmark on multiple computers <!-- omit from toc -->

## Table of Contents <!-- omit from toc -->

- [Requirements](#requirements)
- [Getting started](#getting-started)

## Requirements

- Ansible
- A copy of SPEChpc 2021 archive named `hpc2021-1.1.tar.gz`

## Getting started

Copy the archive from your computer to the remote target

```shell
scp $HOME/ws/hpc2021-1.1.tar.gz USER@DEST_HOST:/home/tmp/
```

> The following command are made inside the `$HOME/ws/trace-coordinator/benchmarks` folder on our computer.

Create the inventory file

```shell
mkdir poly-lab/inventory
touch poly-lab/inventory/hosts
```

This file defines the managed nodes you automate.

```INI
[all:vars]
ansible_connection=ssh
ansible_user=<user>
ansible_password=<password>

[master]
foo.example.com

[workers]
one.example.com
two.example.com
...
ten.example.com
...

```

Create the hosts file

```shell
touch poly-lab/hosts
```

This file defines the managed nodes that mpi will use.

```INI
foo.example.com
one.example.com
two.example.com
...
ten.example.com
...
```

Run Ansible to Setup Polytechnique Labs

```shell
ansible-playbook -i poly-lab/inventory/hosts poly-lab/playbook.yml
```

Start Lttng

```shell
ansible-playbook -i poly-lab/inventory/hosts lttng/playbook.yml
```

In a new terminal start the MPI program on the master node.

> The following command are made inside the `/home/tmp/hpc2021-1.1.7` folder on the remote master node.

```shell
time MPICH_PORT_RANGE=5000:5050 mpirun --hostfile $HOME/tracing/hosts -np 120 ./benchspec/HPC/513.soma_t/exe/soma_base.gnu_mpi -r 42 -t 200 --npoly=140000 --gen-state-file
```
