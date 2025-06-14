resource "aws_lb" "j3_alb" {
  name               = "j3-alb-${var.region}-${var.env}"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.alb_sg.id]
  subnets            = var.subnets
}

resource "aws_lb_target_group" "j3_tg" {
  name     = "j3-tg-${var.region}-${var.env}"
  port     = 8080
  protocol = "HTTP"
  vpc_id   = var.vpcId
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
