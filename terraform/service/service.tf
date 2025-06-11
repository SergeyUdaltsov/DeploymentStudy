resource "aws_ecs_service" "service" {
  name          = "J3StudyEcsService"
  cluster       = data.terraform_remote_state.infra-data.outputs.cluster.name
  desired_count = 1

  task_definition = aws_ecs_task_definition.task_definition.arn
  launch_type     = "FARGATE"

  network_configuration {
    security_groups  = ["sg-086cca7c"]
    subnets          = ["subnet-2fb03453", "subnet-29581143", "subnet-7047cc3c"]
    assign_public_ip = true
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
  execution_role_arn       = data.terraform_remote_state.infra-data.outputs.service_role.arn
  task_role_arn            = data.terraform_remote_state.infra-data.outputs.task_role.arn

  container_definitions = jsonencode([
    {
      name      = "j3-study-container"
      image     = "143936507261.dkr.ecr.eu-central-1.amazonaws.com/j3-repository:${var.image_tag}"
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
          "awslogs-group"         = data.terraform_remote_state.infra-data.outputs.log_group.name
          "awslogs-region"        = "eu-central-1"
          "awslogs-stream-prefix" = "j3-study"
        }
      }
    }
  ])
}


