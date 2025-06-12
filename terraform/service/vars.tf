data "terraform_remote_state" "infra-data" {
  backend = "s3"
  config  = {
    bucket = "j3-terraform-state"
    key    = "j3-infra/terraform.tfstate"
    region = "eu-central-1"
  }
}

variable "image_tag" {
  type = string
  default = ""
  description = "Number of build"
}

variable "env" {
  type = string
  description = "Environment"
}