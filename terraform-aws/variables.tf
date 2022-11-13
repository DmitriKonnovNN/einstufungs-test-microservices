variable "owner" {
  type = string
}

variable "app_tag_name" {
  description = "Enter tag name for all resources to be built"
  type        = string
}
variable "aws_region" {
  type        = string
  description = "AWS region"

}
variable "local_profile" {
  description = "local aws cli profile"
  type        = string
}

variable "app_main_port" {
  description = "standard port web server listens to"
  type        = number
}

variable "current_azs" {
  description = "AZs to be used"
  type        = list(string)
}

variable "private_subnets" {
  type = list(string)
}

variable "public_subnets" {
  type = list(string)
}

variable "cidr" {
  type = string
}

variable "current_environment" {
  type        = string
  description = "Choose one of available environments: DEFAULT, TEST, QA, DEV, STAGE, PROD"
}

variable "environments" {
  type    = list(string)
  default = ["DEFAULT", "TEST", "QA", "DEV", "STAGE", "PROD"]
}


variable "environment_instance_type" {
  type = map(string)
  default = {
    "DEFAULT" = "t2.micro",
    "TEST"    = "t2.micro",
    "QA"      = "t2.small",
    "DEV"     = "t2.medium",
    "STAGE"   = "t2.large",
    "PROD"    = "t2.large"
  }
}

variable "environment_instance_settings" {
  type = map(object({
    instance_type   = string,
    monitoring      = bool,
    ebs_volume_size = number
  }))
  default = {
    "DEFAULT" = {
      instance_type   = "t2.micro",
      monitoring      = false,
      ebs_volume_size = 10
    },
    "TEST" = {
      instance_type   = "t2.micro",
      monitoring      = false,
      ebs_volume_size = 20
    },
    "QA" = {
      instance_type   = "t2.small",
      monitoring      = false,
      ebs_volume_size = 30
    },
    "DEV" = {
      instance_type   = "t2.medium",
      monitoring      = true,
      ebs_volume_size = 50
    },
    "STAGE" = {
      instance_type   = "t2.large",
      monitoring      = true,
      ebs_volume_size = 80
    },
    "PROD" = {
      instance_type   = "t2.large",
      monitoring      = true,
      ebs_volume_size = 80
    },
  }
}
variable "remote_backend_bucket_name" {
  type = string
}

variable "remote_backend_bucket_region" {
  type = string
}