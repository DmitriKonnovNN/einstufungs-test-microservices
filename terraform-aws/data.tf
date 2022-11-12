data "aws_ami" "ubuntu_latest" {
  most_recent = true
  owners      = ["099720109477"] // ubuntu canoical
  filter {
    name   = "name"
    values = ["ubuntu/images/hvm-ssd/ubuntu-focal-20.04-amd64-server-*"]
  }
  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }
  filter {
    name   = "root-device-type"
    values = ["ebs"]
  }
  filter {
    name   = "architecture"
    values = ["x86_64"]
  }
}

data "aws_availability_zones" "zones" {
}

data "aws_vpc" "aws_default_vpc" {
  default = true
}

data "aws_subnet_ids" "subnets" {
  vpc_id = data.aws_vpc.aws_default_vpc.id
}

data "http" "my_public_ip" {
  url = "https://ifconfig.co/json"
  request_headers = {
    Accept = "application/json"
  }
}

data "aws_caller_identity" "my_identity" {

}

data "aws_region" "current_region" {}

# https://stackoverflow.com/questions/63899082/terraform-list-of-ami-specific-to-ubuntu-20-08-lts-aws