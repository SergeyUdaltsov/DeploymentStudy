resource "aws_cloudwatch_log_group" "log_group" {
  name              = "j3-study-logs"
  retention_in_days = 5
}