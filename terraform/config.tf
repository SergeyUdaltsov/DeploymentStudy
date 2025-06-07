terraform {
  backend "s3" {
    bucket         = "j3-terraform-state"
    key            = "ecs-service/terraform.tfstate"
    region         = "eu-central-1"
    encrypt        = true
  }
}

provider "aws" {
  region = "eu-central-1"
}