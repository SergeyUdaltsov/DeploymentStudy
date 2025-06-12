resource "aws_lb" "j3_alb" {
  name               = "j3-alb-${var.env}"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.alb_sg.id]
  subnets            = ["subnet-2fb03453", "subnet-29581143", "subnet-7047cc3c"]
}

resource "aws_lb_target_group" "j3_tg" {
  name     = "j3-tg-${var.env}"
  port     = 8080
  protocol = "HTTP"
  vpc_id   = "vpc-9cf492f6"
  target_type = "ip"

  health_check {
    path                = "/health"
    protocol            = "HTTP"
    port                = "8080"
    matcher             = "200-399"
    interval            = 30
    timeout             = 5
    healthy_threshold   = 2
    unhealthy_threshold = 2
  }
}

resource "aws_lb_listener" "j3_listener" {
  load_balancer_arn = aws_lb.j3_alb.arn
  port              = 80
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.j3_tg.arn
  }
}
