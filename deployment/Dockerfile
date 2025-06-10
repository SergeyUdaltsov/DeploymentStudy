FROM jenkins/jenkins:lts

USER root

# Install dependencies
RUN apt-get update && apt-get install -y unzip curl docker.io

# Install AWS CLI
RUN curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip" && \
    unzip awscliv2.zip && ./aws/install && rm -rf awscliv2.zip aws

# Install Terraform
ARG TERRAFORM_VERSION=1.9.2
RUN curl -O https://releases.hashicorp.com/terraform/${TERRAFORM_VERSION}/terraform_${TERRAFORM_VERSION}_linux_amd64.zip && \
    unzip terraform_${TERRAFORM_VERSION}_linux_amd64.zip && \
    mv terraform /usr/local/bin/ && \
    rm terraform_${TERRAFORM_VERSION}_linux_amd64.zip

# Fix docker group GID
ARG DOCKER_GID=113

# Delete existing docker group if it exists, then recreate with correct GID
RUN if getent group docker; then groupdel docker; fi && groupadd -g ${DOCKER_GID} docker && usermod -aG docker jenkins

USER jenkins
