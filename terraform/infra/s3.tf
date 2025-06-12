resource "aws_s3_bucket" "j3_bucket" {
  bucket = "j3-service-bucket-${var.env}"
  force_destroy = true
}