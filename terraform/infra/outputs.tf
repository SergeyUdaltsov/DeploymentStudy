output "service_role_arn" {
  value = aws_iam_role.service_role.arn
}

output "task_role_arn" {
  value = aws_iam_role.task_role.arn
}

output "log_group_name" {
  value = aws_cloudwatch_log_group.log_group.name
}

output "cluster_name" {
  value = aws_ecs_cluster.j3-cluster.name
}

output "target_group_arn" {
  value = aws_lb_target_group.j3_tg.arn
}

output "target_group_arn_suffix" {
  value = aws_lb_target_group.j3_tg.arn_suffix
}

output "alb_arn_suffix" {
  value = aws_lb.j3_alb.arn_suffix
}

output "service_sg_id" {
  value = aws_security_group.ecs_service_sg.id
}