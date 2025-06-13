resource "aws_cloudwatch_log_group" "log_group" {
  name              = "j3-study-logs-${var.region}-${var.env}"
  retention_in_days = 5
}