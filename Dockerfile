# Use Ubuntu 22.04 as base image
FROM ubuntu:22.04

# Install prerequisites
RUN apt-get update \
    && apt-get install -y wget gnupg2 \
    && rm -rf /var/lib/apt/lists/*

# Import MongoDB public GPG key
RUN wget -qO - https://www.mongodb.org/static/pgp/server-6.0.asc | apt-key add -

# Add MongoDB repository to sources list
RUN echo "deb [ arch=amd64,arm64 ] https://repo.mongodb.org/apt/ubuntu jammy/mongodb-org/6.0 multiverse" | tee /etc/apt/sources.list.d/mongodb-org-6.0.list

# Update package list and install MongoDB
RUN apt-get update \
    && apt-get install -y mongodb-org \
    && rm -rf /var/lib/apt/lists/*

# Create necessary directories
RUN mkdir -p /data/db /data/configdb

# Change ownership of directories
RUN chown -R mongodb:mongodb /data/db /data/configdb

# Add your stuff below:
RUN apt-get update \
    && apt-get install -y --no-install-recommends \
    vim \
    nodejs \
    npm \
    openjdk-17-jdk \
    maven \
    curl \
    git \
    && wget https://downloads.apache.org/tomcat/tomcat-9/v9.0.89/bin/apache-tomcat-9.0.89.tar.gz \
    && tar -xzf apache-tomcat-9.0.89.tar.gz -C /opt \
    && mv /opt/apache-tomcat-9.0.89 /opt/tomcat \
    && rm -rf /var/lib/apt/lists/* /apache-tomcat-9.0.89.tar.gz
WORKDIR /root/project
COPY milestone3 ./milestone3
COPY run.sh .
RUN chmod +x run.sh
EXPOSE 8080
CMD ["/bin/bash", "run.sh"]