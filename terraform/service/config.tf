terraform {
  required_version = ">= 1.8.5"
  backend "s3" {
    bucket         = "j3-terraform-state"
    region         = "eu-central-1"
    encrypt        = true
  }
}

provider "aws" {
  region = var.region
}