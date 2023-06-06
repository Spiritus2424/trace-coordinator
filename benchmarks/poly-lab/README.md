# Run SPEC benchmark on multiple computers <!-- omit from toc -->

## Table of Contents <!-- omit from toc -->

- [Requirements](#requirements)
- [Getting started](#getting-started)
  - [Install SPEC](#install-spec)
    - [Your computer](#your-computer)
    - [Remote computer](#remote-computer)
- [Edit these lines](#edit-these-lines)
- [Run compilation for each benchmark](#run-compilation-for-each-benchmark)
- [Authorized SSH Key](#authorized-ssh-key)
- [Run benchmark](#run-benchmark)

## Requirements

- Ansible
- Python 3 (Optional)

## Getting started

### Install SPEC

#### Your computer

> The following command are made inside the `$HOME/ws` folder on our computer.

1. scp `$HOME/ws/hpc2021-1.1.tar.gz` user@DEST_HOST:`/home/tmp/`

#### Remote computer

> The following command are made inside the `/home/tmp/` folder on the remote.

1. mkdir `$HOME/tracing`
2. touch `$HOME/tracing/hosts`
3. mkdir `/home/tmp/lib`
4. ln -s `/usr/lib64/libnsl.so.3` `/home/tmp/lib/libnsl.so.1`
5. export `LD_LIBRARY_PATH=/home/tmp/lib:$LD_LIBRARY_PATH`
6. cd `/home/tmp/`
7. tar -xf `hpc2021-1.1.tar.gz` #Untar the archive
8. cd `hpc2021-1.1.7/` #go in the SPEC folder
9. `./install.sh` # run the installer, should finish with "Installation successful.  Source the shrc or cshrc in /home/tmp/hpc2021-1.1.7 to set up your environment for the benchmark"
10. `source ./shrc` # source shrc to set up environment variables and paths for SPEC
11. cd `config`
12. cp `Example_gnu.cfg` `4712-gnu.cfg`
13. chmod 770 `4712-gnu.cfg`
14. nano `4712-gnu.cfg`

## Edit these lines

- `MPIRUN_OPTS` = --hostfile /home/tmp/tracing/hosts
- submit = mpirun --hostfile /home/tmp/tracing/hosts -np 8 $command

Benchmarks selected: 505.lbm_t, 513.soma_t, 518.tealeaf_t, , 521.miniswp_t, , 532.sph_exa_t, 534.hpgmgfv_t, 535.weather_t #519.clvleaf_t don't work  528.pot3d_t, 528.pot3d_t

> Benchmark 519.clvleaf_t don't work

## Run compilation for each benchmark

    runhpc --config=4712-gnu.cfg --action=build --tune=base -ranks 12 535.weather_t

    cp -r benchspec/HPC ~/tracing/

## Authorized SSH Key

## Run benchmark

- (Worker) sh start.sh exp_soma_14000
- (Master) time MPICH_PORT_RANGE=5000:5050 mpirun --hostfile /usagers3/pidena/tracing/hosts -np 120 ./soma_base.gnu_mpi -r 42 -t 200 --npoly=140000 --gen-state-file
- (Worker) sh stop.sh
