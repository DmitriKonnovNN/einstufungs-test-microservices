terraform {

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 3.27"
    }
  }
  #   required_version = "~>0.12.0"
}


provider "aws" {
  profile = var.local_profile
  region  = var.aws_region
}

resource "aws_eip" "static_ip" {
  instance = aws_instance.app_web.id
}

resource "aws_instance" "app_web" {
  ami           = data.aws_ami.ubuntu_latest.id
  instance_type = terraform.workspace == "default" ? "t2.micro" : (terraform.workspace == "test" ? "t2.small" : (terraform.workspace == "dev" ? "t2.medium" : "t2.large"))

  subnet_id = aws_subnet.subnet1.id
  vpc_security_group_ids = [aws_security_group.sg-web.id]
  key_name               = aws_key_pair.ec2-key-pair.key_name
  user_data              = templatefile("../init-data-cloud.sh.tpl", { instance_name = "${var.app_tag_name}" })

  root_block_device {
    volume_size = terraform.workspace == "prod" ? 50 : 30
    volume_type = "gp2"
  }
  tags = {
    Name  = "ec2-${var.app_tag_name}"
    Owner = "ec2-${var.owner}"
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

resource "aws_subnet" "subnet1" {
  cidr_block              = "10.16.16.0/20"
  vpc_id                  = aws_vpc.exam-micro-vpc.id
  map_public_ip_on_launch = "true"
  availability_zone       = var.current_azs[0]
  tags = {
    Name  = "ec2-${var.app_tag_name}"
    Owner = "ec2-${var.owner}"
  }
}

#resource "aws_subnet" "subnet" {
#
#  for_each          = toset(var.current_azs)
#  vpc_id            = aws_vpc.exam-micro-vpc.id
#  availability_zone = each.value
#  map_public_ip_on_launch = "true"
#
#
#}

resource "aws_vpc" "exam-micro-vpc" {
  cidr_block           = var.cidr
  enable_dns_hostnames = "true"
  tags = {
    Name  = "ec2-${var.app_tag_name}"
    Owner = "ec2-${var.owner}"
  }
}

resource "aws_internet_gateway" "gateway1" {
  vpc_id = aws_vpc.exam-micro-vpc.id
  tags = {
    Name  = "ec2-${var.app_tag_name}"
    Owner = "ec2-${var.owner}"
  }
}

resource "aws_route_table" "route_table1" {
  vpc_id = aws_vpc.exam-micro-vpc.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.gateway1.id
  }
  tags = {
    Name  = "ec2-${var.app_tag_name}"
    Owner = "ec2-${var.owner}"
  }
}

resource "aws_route_table_association" "route_subnet1" {
  subnet_id = aws_subnet.subnet1.id
  route_table_id = aws_route_table.route_table1.id
  tags = {
    Name  = "ec2-${var.app_tag_name}"
    Owner = "ec2-${var.owner}"
  }
}

resource "aws_security_group" "sg-web" {
  name        = "${terraform.workspace}-${var.app_tag_name}"
  description = "sg-web_${var.app_tag_name} inbound traffic"
  vpc_id = aws_vpc.exam-micro-vpc.id

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

  }
}


locals {
  my_public_ip = jsondecode(data.http.my_public_ip.response_body)
}