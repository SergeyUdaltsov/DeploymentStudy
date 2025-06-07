resource "aws_ecs_service" "service" {
  name          = "J3StudyEcsService"
  cluster       = "j3-study-cluster"
  desired_count = 1

  task_definition = aws_ecs_task_definition.task_definition.arn
  launch_type     = "FARGATE"

  network_configuration {
    security_groups  = ["sg-086cca7c"]
    subnets          = ["subnet-2fb03453", "subnet-29581143", "subnet-7047cc3c"]
    assign_public_ip = true
  }

  depends_on = [aws_ecs_task_definition.task_definition]
}

resource "aws_ecs_task_definition" "task_definition" {
  family                   = "j3-study"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = 1024
  memory                   = 2048
  execution_role_arn       = "arn:aws:iam::143936507261:role/j3-service-role"
  task_role_arn            = "arn:aws:iam::143936507261:role/j3-ecs-task-role"

  container_definitions = jsonencode([
    {
      name      = "j3-study-container"
      image     = "143936507261.dkr.ecr.eu-central-1.amazonaws.com/j3-study:${var.image_tag}"
      essential = true
      portMappings = [
        {
          containerPort = 8080
          hostPort      = 8080
          protocol      = "tcp"
        }
      ]
    }
  ])
}

resource "aws_cloudwatch_log_group" "log_group" {
  name              = "j3-study-logs"
  retention_in_days = 5
}
