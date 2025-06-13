resource "aws_sqs_queue" "sqs_queue" {
  name                        = "j3-queue-${var.region}-${var.env}"
  visibility_timeout_seconds  = 30
  message_retention_seconds   = 86400
}
