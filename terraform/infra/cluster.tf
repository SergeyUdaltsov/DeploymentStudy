resource "aws_ecs_cluster" "this" {
  name = "j3-cluster"

  capacity_providers = ["FARGATE"]
}