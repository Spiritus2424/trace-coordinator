# Run SPEC benchmark on multiple computers

This manual is specific to L4712 PC of Polytechnique Montréal Computing department but can be reused to run benchmark on other systems.

## Requirements

- Ansible

## Getting started

### Install SPEC

mkdir /usagers3/pidena/tracing

touch /usagers3/pidena/tracing/hosts

mkdir lib

ln -s /usr/lib64/libnsl.so.3 /usagers3/pidena/lib/libnsl.so.1

export LD_LIBRARY_PATH=/usagers3/pidena/lib:$LD_LIBRARY_PATH

scp /home/pierre/Projets/POLYMTL/TRACE_COMPASS/BENCHMARK/hpc2021-1.1.tar.gz <pidena@l4712-01.info.polymtl.ca>:/home/tmp/

cd /home/tmp/

tar -xf hpc2021-1.1.tar.gz #Untar the archive

cd hpc2021-1.1.7/ #go in the SPEC folder

./install.sh # run the installer, should finish with "Installation successful.  Source the shrc or cshrc in /home/tmp/hpc2021-1.1.7 to set up your environment for the benchmark"

. ./shrc # source shrc to set up environment variables and paths for SPEC

cd config

cp Example_gnu.cfg 4712-gnu.cfg

chmod 770 4712-gnu.cfg

nano 4712-gnu.cfg

## edit these lines

MPIRUN_OPTS = --hostfile /home/tmp/tracing/hosts

submit = mpirun --hostfile /home/tmp/tracing/hosts -np 8 $command

Benchmarks selected: 505.lbm_t, 513.soma_t, 518.tealeaf_t, , 521.miniswp_t, , 532.sph_exa_t, 534.hpgmgfv_t, 535.weather_t #519.clvleaf_t don't work  528.pot3d_t

## Run compilation for each benchmark

runhpc --config=4712-gnu.cfg --action=build --tune=base -ranks 12 535.weather_t

cp -r benchspec/HPC ~/tracing/

################# "Run benchmark "

# on L4712-02 -> XX

sh start.sh exp_soma_14000

# on L4712-01

time MPICH_PORT_RANGE=5000:5050 mpirun --hostfile /usagers3/pidena/tracing/hosts -np 120 ./soma_base.gnu_mpi -r 42 -t 200 --npoly=140000 --gen-state-file

# on L4712-02 -> XX

sh stop.sh
