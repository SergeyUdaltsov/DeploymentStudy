resource "aws_ecr_repository" "ecr" {
  name = "j3-repository"
  force_delete = true
}