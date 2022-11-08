resource "digitalocean_firewall" "et-fw" {
  name        = "et-fw"
  droplet_ids = [digitalocean_droplet.my_droplets["einstufungstest"].id]

  depends_on = [
    digitalocean_droplet.my_droplets
  ]

  dynamic "inbound_rule" {
    for_each = ["22","80","9411","8761"]
    content {
      protocol = "tcp"
      port_range = inbound_rule.value
      source_addresses = ["${local.my_public_ip.ip}/32"]
    }
  }

  dynamic "outbound_rule" {
    for_each = ["tcp","udp","icmp"]
    content {
      protocol = outbound_rule.value
      port_range            = "1-65535"
      destination_addresses = ["0.0.0.0/0", "::/0"]
    }
  }


}
locals {
  my_public_ip = jsondecode(data.http.my_public_ip.response_body)
}

