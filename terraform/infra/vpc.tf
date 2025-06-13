resource "aws_security_group" "alb_sg" {
  name        = "j3-alb-sg-${var.region}-${var.env}"
  description = "Allow HTTP traffic to ALB"
  vpc_id      = "vpc-9cf492f6"

  ingress {
    description = "Allow HTTP from anywhere"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "ecs_service_sg" {
  name        = "j3-ecs-service-sg-${var.region}-${var.env}"
  description = "Allow traffic from ALB to ECS tasks"
  vpc_id      = "vpc-9cf492f6"

  ingress {
    description = "Allow traffic from ALB"
    from_port   = 8077
    to_port     = 8080
    protocol    = "tcp"
    security_groups = [aws_security_group.alb_sg.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}
