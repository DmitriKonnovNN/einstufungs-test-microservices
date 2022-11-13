terraform {

  backend "s3" {
    bucket         = "remote-state-dmitrii-konnov-solutions-exam"
    key            = "et-mcr-svcs/terraform.tfstate"
    region         = "us-east-1"
    dynamodb_table = "terraform_state_locking"
    encrypt        = true
  }
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 3.27"
    }
  }
  required_version = ">= 0.13.1"
}


provider "aws" {
  profile = var.local_profile
  region  = var.aws_region
}


resource "aws_instance" "app_web" {
  count         = 2
  ami           = data.aws_ami.ubuntu_latest.id
  instance_type = var.environment_instance_settings[var.current_environment].instance_type
  //instance_type = terraform.workspace == "default" ? "t2.micro" : (terraform.workspace == "test" ? "t2.small" : (terraform.workspace == "dev" ? "t2.medium" : "t2.large"))
  monitoring             = var.environment_instance_settings[var.current_environment].monitoring
  subnet_id              = module.vpc.public_subnets[count.index]
  vpc_security_group_ids = [aws_security_group.sg-web.id]
  key_name               = aws_key_pair.ec2-key-pair.key_name
  user_data              = templatefile("../init-data-cloud.sh.tpl", { instance_name = var.app_tag_name })

  root_block_device {
    volume_size = var.environment_instance_settings[var.current_environment].ebs_volume_size
    volume_type = "gp2"
  }
  tags = {
    Name  = "ec2-${var.app_tag_name}"
    Owner = "ec2-${var.owner}"
    Env   = var.current_environment
  }

  lifecycle {
    create_before_destroy = true
  }
}

resource "aws_key_pair" "ec2-key-pair" {
  key_name   = var.app_tag_name
  public_key = tls_private_key.rsa-4096-ec2.public_key_openssh
}

resource "tls_private_key" "rsa-4096-ec2" {
  algorithm = "RSA"
  rsa_bits  = 4096
}

resource "local_file" "ec2-rsa-keys" {
  content         = tls_private_key.rsa-4096-ec2.private_key_pem
  file_permission = 400
  filename        = "${var.app_tag_name}.pem"
}
module "vpc" {
  source = "terraform-aws-modules/vpc/aws"
  name   = "exams-vpc"
  cidr   = var.cidr


  azs             = var.current_azs
  private_subnets = var.private_subnets
  public_subnets  = var.public_subnets
  putin_khuylo    = true

  enable_nat_gateway = false
  enable_vpn_gateway = true


  tags = {
    Terraform   = "true"
    Environment = var.current_environment
    Name        = "sg-${var.app_tag_name}"
    Owner       = var.owner
  }
}





resource "aws_security_group" "sg-web" {
  name        = "${var.current_environment}-${var.app_tag_name}"
  description = "sg-web_${var.app_tag_name} inbound traffic"
  //vpc_id      = aws_vpc.exam-micro-vpc.id
  vpc_id = module.vpc.vpc_id

  ingress {
    cidr_blocks      = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
    from_port        = var.app_main_port
    to_port          = var.app_main_port
    protocol         = "tcp"
    description      = "http"
  }
  ingress {
    cidr_blocks = ["${local.my_public_ip.ip}/32"]
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    description = "ssh"
  }

  ingress {
    cidr_blocks = ["${local.my_public_ip.ip}/32"]
    description = "eureka"
    from_port   = 8761
    to_port     = 8761
    protocol    = "tcp"
  }

  ingress {
    cidr_blocks = ["${local.my_public_ip.ip}/32"]
    description = "zipkin"
    from_port   = 9411
    to_port     = 9411
    protocol    = "tcp"
  }
  ingress {
    cidr_blocks      = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
    from_port        = 443
    to_port          = 443
    protocol         = "tcp"
    description      = "https"
  }
  egress {
    cidr_blocks      = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
    from_port        = 0
    protocol         = "-1"
    to_port          = 0
    description      = "allow all egress traffic"
  }
  tags = {
    Name  = "sg-${var.app_tag_name}"
    Owner = var.owner
    Env   = var.current_environment
  }
}


locals {
  my_public_ip = jsondecode(data.http.my_public_ip.response_body)
}