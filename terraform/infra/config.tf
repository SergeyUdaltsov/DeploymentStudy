terraform {
  required_version = ">= 1.8.5"
  backend "s3" {
    bucket         = "j3-terraform-state"
    key            = "j3-infra/terraform.tfstate"
    region         = "eu-central-1"
    encrypt        = true
  }
}

provider "aws" {
  region = "eu-central-1"
}