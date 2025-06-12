resource "aws_dynamodb_table" "file_metadata" {
  name         = "j3_payloads_metadata-${var.env}"
  billing_mode = "PAY_PER_REQUEST"
  hash_key     = "id"

  attribute {
    name = "id"
    type = "S"
  }
}