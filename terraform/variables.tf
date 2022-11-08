variable "owner" {
  type = string
}
variable "region" {
  type = string
}

variable "token" {
  type = string
}

# variable "name" {
#   type = string
# }


variable "droplet_names" {
  type = list(string)
}
variable "droplet_size" {
  type = string
}

variable "image_type" {
  type = string
}

variable "project_name" {
  type = string

}