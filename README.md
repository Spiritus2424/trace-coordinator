# Trace Compass Coordinator

## Table of Contents

- [Trace Compass Coordinator](#trace-compass-coordinator)
  - [Table of Contents](#table-of-contents)
  - [Authenticating to GitHub Packages](#authenticating-to-github-packages)
  - [Compiling manually](#compiling-manually)
  - [Running the trace coordinator](#running-the-trace-coordinator)
  - [Trace Coordinator Configuration File](#trace-coordinator-configuration-file)
  - [Trace Coordinator Properties](#trace-coordinator-properties)

## Authenticating to GitHub Packages

This project use a package from `Github Packages`. You have to get authenticate to the `Github Packages` before compiling the project. To authenticate you have to create a [personal access token][personal-access-token] with at least `read:packages` scope to install packages.

Then you will have to store your Github `Username` and `Token` into environment variables.

    export USERNAME=<YOUR_USERNAME>
    export GITHUB_TOKEN=<YOUR_GIT_TOKEN>

[personal-access-token]: https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token

## Compiling manually

Please ensure to have a [github personal access token](#authenticating-to-github-packages) before continuing.

The Gradle project build requires version 8.0.2 or later. It can be downloaded following the step [here][gradle-install].

To build the trace-coordinator manually using Gradle, simply run the following command from the git project top-level directory:

    gradle build

The default command will compile and run the unit tests. Running the tests can take some time, to skip them you can append `-x test` to the `gradle` command:

    gradle build -x test

The resulting executables will be in `app/build/libs/`.

[gradle-install]: https://gradle.org/install/

## Running the trace coordinator

To run the trace-coordinator using Gradle, simply run the following command from the git project top-level directory:

    gradle run

This server is an implementation of the [Trace Server Protocol][trace-server-protocol], whose API is documented using the OpenAPI REST specification.

[trace-server-protocol]: https://github.com/eclipse-cdt-cloud/trace-server-protocol

## Trace Coordinator Configuration File

The trace coordinator need to load a configuration file, at top-level directory, that configure the Tracecompass Server. The file is named `.trace-coordinator.yml`. Here's an example:

    trace-servers:
      - host: http://172.20.0.1 
        port: 8081  
        traces-path: [          
        "/apt",
        ]
      - host: http://172.20.0.1
        port: 8082
        traces-path: [
        "/pacman",
        ]

## Trace Coordinator Properties

The following properties are supported:

- `TRACE_COORDINATOR_FILE`:  Path to the `trace-coordinator.yml` file. By default, it will check if the file is at the root of the project.
