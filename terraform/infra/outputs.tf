output "service_role" {
  value = aws_iam_role.service_role
}

output "task_role" {
  value = aws_iam_role.task_role
}

output "log_group" {
  value = aws_cloudwatch_log_group.log_group
}

output "cluster" {
  value = aws_ecs_cluster.j3-cluster
}