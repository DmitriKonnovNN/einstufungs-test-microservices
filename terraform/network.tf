resource "digitalocean_firewall" "et-fw" {
  name        = "et-fw"
  droplet_ids = [digitalocean_droplet.my_droplets["einstufungstest"].id]

  depends_on = [
    digitalocean_droplet.my_droplets
  ]

  inbound_rule {
    protocol         = "tcp"
    port_range       = "22"
    source_addresses = ["${local.my_public_ip.ip}/32"]
  }

  inbound_rule {
    protocol         = "tcp"
    port_range       = "80"
    source_addresses = ["0.0.0.0/0", "::/0"]
  }
  inbound_rule {
    protocol         = "tcp"
    port_range       = "9411"
    source_addresses = ["${local.my_public_ip.ip}/32"]
  }


  inbound_rule {
    protocol         = "tcp"
    port_range       = "8761"
    source_addresses = ["${local.my_public_ip.ip}/32"]
  }


  outbound_rule {
    protocol              = "tcp"
    port_range            = "1-65535"
    destination_addresses = ["0.0.0.0/0", "::/0"]
  }

  outbound_rule {
    protocol              = "udp"
    port_range            = "1-65535"
    destination_addresses = ["0.0.0.0/0", "::/0"]
  }

  outbound_rule {
    protocol              = "icmp"
    destination_addresses = ["0.0.0.0/0", "::/0"]
  }

}
locals {
  my_public_ip = jsondecode(data.http.my_public_ip.response_body)
}

