provider "aws" {
  region = "eu-central-1"
}

resource "aws_cognito_user_pool" "main" {
  name = "photo-game-users"

  admin_create_user_config {
    allow_admin_create_user_only = false
  }

  auto_verified_attributes = ["email"]

  schema {
    attribute_data_type      = "String"
    developer_only_attribute = false
    mutable                  = true
    name                     = "email"
    required                 = true

    string_attribute_constraints {
      min_length = 3
      max_length = 254
    }
  }

  password_policy {
    minimum_length    = 8
    require_lowercase = true
    require_numbers   = true
    require_symbols   = true
    require_uppercase = true
  }

  verification_message_template {
    default_email_option  = "CONFIRM_WITH_CODE"
    email_message         = "{####} is your verification code"
    email_subject         = "Verify your email for Photo Game app"
    // sms_message           = "{####} is your verification code"
  }
}