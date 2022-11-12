data "digitalocean_project" "einstufungstest" {

  name = data.digitalocean_projects.projects_et.projects[0].name
}
data "digitalocean_projects" "projects_et" {
  filter {
    key    = "name"
    values = [var.project_name]
  }

}

data "digitalocean_ssh_keys" "keys_in_use" {

  filter {
    key    = "name"
    values = ["key-${var.project_name}"]
  }
}

data "http" "my_public_ip" {
  url = "https://ifconfig.co/json"
  request_headers = {
    Accept = "application/json"
  }
}