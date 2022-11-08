terraform {
  required_providers {
    digitalocean = {
      source  = "digitalocean/digitalocean"
      version = "~> 2.0"
    }
  }
}

provider "digitalocean" {
  token = var.token
}

resource "digitalocean_droplet" "my_droplets" {
  for_each  = toset(var.droplet_names)
  ssh_keys  = (length(data.digitalocean_ssh_keys.keys_in_use.ssh_keys) == 0 ? [digitalocean_ssh_key.ssh_key[0].fingerprint] : [data.digitalocean_ssh_keys.keys_in_use.ssh_keys[0].fingerprint])
  image     = var.image_type
  name      = each.value
  region    = var.region
  size      = var.droplet_size
  user_data = templatefile("init-data.sh.tpl", { droplet_name = "${each.value}" })
  tags      = ["dev"]
}
resource "digitalocean_project_resources" "einstufungstest" {
  for_each  = digitalocean_droplet.my_droplets
  project   = data.digitalocean_project.einstufungstest.id
  resources = [digitalocean_droplet.my_droplets[each.key].urn]
}

resource "digitalocean_ssh_key" "ssh_key" {
  count = length(data.digitalocean_ssh_keys.keys_in_use.ssh_keys) == 1 ? 0 : 1

  # lifecycle {
  #   prevent_destroy = true
  # }
  name       = "key-${var.project_name}"
  public_key = file("C:/Users/lildk/.ssh/id_rsa.pub")
}
