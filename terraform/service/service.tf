resource "aws_ecs_service" "service" {
  name          = "J3StudyEcsService-${var.region}-${var.env}"
  cluster       = data.terraform_remote_state.infra-data.outputs.cluster_name
  desired_count = 1

  task_definition = aws_ecs_task_definition.task_definition.arn
  launch_type     = "FARGATE"

  network_configuration {
    security_groups  = [data.terraform_remote_state.infra-data.outputs.service_sg_id]
    subnets          = data.terraform_remote_state.infra-data.outputs.subnets
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = data.terraform_remote_state.infra-data.outputs.target_group_arn
    container_name   = local.container_name
    container_port   = 8080
  }

  wait_for_steady_state = true
  timeouts {
    create = "5m"
    update = "5m"
  }
}

resource "aws_ecs_task_definition" "task_definition" {
  family                   = "j3-study"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = 1024
  memory                   = 2048
  execution_role_arn       = data.terraform_remote_state.infra-data.outputs.service_role_arn
  task_role_arn            = data.terraform_remote_state.infra-data.outputs.task_role_arn

  container_definitions = jsonencode([
    {
      name      = local.container_name
      image     = "143936507261.dkr.ecr.${var.region}.amazonaws.com/j3-repository-${var.region}-${var.env}:${var.image_tag}"
      essential = true
      portMappings = [
        {
          containerPort = 8080
          hostPort      = 8080
          protocol      = "tcp"
        }
      ]
      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = data.terraform_remote_state.infra-data.outputs.log_group_name
          "awslogs-region"        = var.region
          "awslogs-stream-prefix" = "j3-study"
        }
      }
      environment = [
        {
          name  = "ENV"
          value = var.env
        },
        {
          name  = "REGION"
          value = var.region
        }
      ]
    }
  ])
}

resource "aws_appautoscaling_target" "ecs_service" {
  max_capacity       = 3
  min_capacity       = 1
  resource_id        = "service/${data.terraform_remote_state.infra-data.outputs.cluster_name}/${aws_ecs_service.service.name}"
  scalable_dimension = "ecs:service:DesiredCount"
  service_namespace  = "ecs"
}

resource "aws_appautoscaling_policy" "ecs_service_rps_policy" {
  name               = "J3StudyEcsService-RPS-Scaling-${var.region}-${var.env}"
  policy_type        = "TargetTrackingScaling"
  resource_id        = aws_appautoscaling_target.ecs_service.resource_id
  scalable_dimension = aws_appautoscaling_target.ecs_service.scalable_dimension
  service_namespace  = aws_appautoscaling_target.ecs_service.service_namespace

  target_tracking_scaling_policy_configuration {
    predefined_metric_specification {
      predefined_metric_type = "ALBRequestCountPerTarget"
      resource_label         = "${data.terraform_remote_state.infra-data.outputs.alb_arn_suffix}/${data.terraform_remote_state.infra-data.outputs.target_group_arn_suffix}"
    }

    target_value       = 50.0
    scale_in_cooldown  = 60
    scale_out_cooldown = 60
  }
}

locals {
  container_name = "j3-study-container-${var.region}-${var.env}"
}


