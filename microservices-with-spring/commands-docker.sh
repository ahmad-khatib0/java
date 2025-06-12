#!/bin/bash

# finding out how many available processors (that is, CPU cores) Java sees without applying any constraints
echo 'Runtime.getRuntime().availableProcessors()' | docker run --rm -i eclipse-temurin:17 jshell -q

# restrict the Docker container to only be allowed to use three CPU cores
echo 'Runtime.getRuntime().availableProcessors()' | docker run --rm -i --cpus=3 eclipse-temurin:17 jshell -q

# ask the JVM for the maximum size that it thinks it can allocate for the heap
docker run -it --rm eclipse-temurin:17 java -XX:+PrintFlagsFinal | grep "size_t MaxHeapSize"

# With no JVM memory constraints (that is, not using the JVM parameter -Xmx), Java will
# allocate one-quarter of the memory available to the container for its heap.
# So, we expect it to allocate up to 4 GB to its heap (from 16GB machine).

# If we constrain the Docker container to only use up to 1 GB of memory using the Docker
# option -m=1024M, we expect to see a lower max memory allocation.
docker run -it --rm -m=1024M eclipse-temurin:17 java -XX:+PrintFlagsFinal | grep "size_t MaxHeapSize"

# custom: allow the JVM to use 600 MB of the total 1 GB we have for its heap
docker run -it --rm -m=1024M eclipse-temurin:17 java -Xmx600m \
  -XX:+PrintFlagsFinal -version | grep "size_t MaxHeapSize"

# Let’s conclude with an “out of memory” test to ensure that this really works!
# First, try to allocate a byte array of 100 MB:
echo 'new byte[100_000_000]' | docker run -i --rm -m=1024M eclipse-temurin:17 jshell -q

# Let’s try to allocate a byte array that is larger than the max heap size, for example, 500 MB:
echo 'new byte[500_000_000]' | docker run -i --rm -m=1024M eclipse-temurin:17 jshell -q

# in chapter4:
./gradlew :microservices:product-service:build

# Since we only want to build product-service and the projects it depends on (the api and util projects),
# we don’t use the normal build command, which builds all the microservices. Instead, we use a variant
# that tells Gradle to only build the product-service project: :microservices:product-service:build.

# scale a container
docker-compose up -d --scale product=1
