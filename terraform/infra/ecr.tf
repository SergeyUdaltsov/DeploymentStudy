resource "aws_ecr_repository" "j3_ecr" {
  name = "j3-repository"
  force_delete = true
}