resource "aws_ecr_repository" "ecr" {
  name = "j3-repository-${var.env}"
  force_delete = true
}