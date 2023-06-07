#!/bin/bash
Hosts=$(hostname -s) #get short hostname
lttng -g gigl disable-event --session=${Hosts} --kernel --all-events
lttng -g gigl stop ${Hosts}
lttng -g gigl destroy ${Hosts}
