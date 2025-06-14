resource "aws_iam_role" "service_role" {
  name               = "j3-service-role-${var.region}-${var.env}"
  assume_role_policy = jsonencode({
    Version   = "2012-10-17"
    Statement = [
      {
        Action    = "sts:AssumeRole"
        Effect    = "Allow"
        Sid       = ""
        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        }
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "managed_attachment_service" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEC2ContainerRegistryPowerUser"
  role       = aws_iam_role.service_role.name
}

resource "aws_iam_role_policy" "inline_service_policy" {
  name = "j3-specific-policy-${var.region}-${var.env}"
  role = aws_iam_role.service_role.name

  policy = jsonencode({
    Version   = "2012-10-17"
    Statement = [
      {
        Action = [
          "ecr:GetRegistryPolicy",
          "ecr:DescribeImageScanFindings",
          "ecr:GetLifecyclePolicyPreview",
          "ecr:GetDownloadUrlForLayer",
          "ecr:DescribeRegistry",
          "ecr:DescribeImageReplicationStatus",
          "ecr:DescribeRepositoryCreationTemplates",
          "ecr:GetAuthorizationToken",
          "ecr:ListTagsForResource",
          "ecr:BatchGetRepositoryScanningConfiguration",
          "ecr:GetRegistryScanningConfiguration",
          "ecr:ValidatePullThroughCacheRule",
          "ecr:GetAccountSetting",
          "ecr:BatchGetImage",
          "ecr:DescribeRepositories",
          "ecr:GetImageCopyStatus",
          "ecr:BatchCheckLayerAvailability",
          "ecr:GetRepositoryPolicy",
          "ecr:GetLifecyclePolicy"
        ]
        Effect   = "Allow"
        Resource = [aws_ecr_repository.ecr.arn]
      },
      {
        Action = [
          "logs:CreateLogStream",
          "logs:PutLogEvents"
        ]
        Effect   = "Allow"
        Resource = ["*"]
      }
    ]
  })
}

resource "aws_iam_role" "task_role" {
  name               = "j3-task-role-${var.region}-${var.env}"
  assume_role_policy = jsonencode({
    Version   = "2012-10-17"
    Statement = [
      {
        Action    = "sts:AssumeRole"
        Effect    = "Allow"
        Sid       = ""
        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        }
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "managed_attachment_task" {
  policy_arn = "arn:aws:iam::aws:policy/CloudWatchLogsFullAccess"
  role       = aws_iam_role.task_role.name
}

resource "aws_iam_role_policy" "s3_policy" {
  name = "j3-s3-access-policy-${var.region}-${var.env}"
  role = aws_iam_role.task_role.name

  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect = "Allow",
        Action = [
          "s3:GetObject",
          "s3:PutObject",
          "s3:DeleteObject",
          "s3:ListBucket"
        ],
        Resource = [
          aws_s3_bucket.j3_bucket.arn,
          "${aws_s3_bucket.j3_bucket.arn}/*"
        ]
      }
    ]
  })
}

resource "aws_iam_role_policy" "dynamoDb_policy" {
  name = "j3-dynamoDb-access-policy-${var.region}-${var.env}"
  role = aws_iam_role.task_role.name

  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect = "Allow",
        Action = [
          "dynamodb:PutItem",
          "dynamodb:UpdateItem",
          "dynamodb:GetItem"
        ],
        Resource = [
          aws_dynamodb_table.file_metadata.arn
        ]
      }
    ]
  })
}

resource "aws_iam_role_policy" "sqs_policy" {
  name = "j3-sqs-access-policy-${var.region}-${var.env}"
  role = aws_iam_role.task_role.name

  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect = "Allow",
        Action = [
          "sqs:SendMessage",
          "sqs:ReceiveMessage",
          "sqs:DeleteMessage"
        ],
        Resource = [
          aws_sqs_queue.sqs_queue.arn
        ]
      }
    ]
  })
}