/* resource "aws_subnet" "subnet1" {
  cidr_block              = "10.16.16.0/20"
  vpc_id                  = aws_vpc.exam-micro-vpc.id
  map_public_ip_on_launch = "true"
  availability_zone       = var.current_azs[0]
  tags = {
    Name  = "subnet1-${var.app_tag_name}"
    Owner = "subnet1-${var.owner}"
    Env   = var.current_environment
  }
}

resource "aws_vpc" "exam-micro-vpc" {
  cidr_block           = var.cidr
  enable_dns_hostnames = "true"
  tags = {
    Name  = "vpc-${var.app_tag_name}"
    Owner = "vpc-${var.owner}"
    Env   = var.current_environment
  }
}

resource "aws_internet_gateway" "gateway1" {
  vpc_id = aws_vpc.exam-micro-vpc.id
  tags = {
    Name  = "gw1-${var.app_tag_name}"
    Owner = "gw1-${var.owner}"
    Env   = var.current_environment
  }
}

resource "aws_route_table" "route_table1" {
  vpc_id = aws_vpc.exam-micro-vpc.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.gateway1.id
  }
  tags = {
    Name  = "rt1-${var.app_tag_name}"
    Owner = "rt1-${var.owner}"
    Env   = var.current_environment
  }
}

resource "aws_route_table_association" "route_subnet1" {
  subnet_id      = aws_subnet.subnet1.id
  route_table_id = aws_route_table.route_table1.id
} */