output "droplet_pub_ip" {
  description = "droplet's public ip+"
  value       = { for k, md in digitalocean_droplet.my_droplets : k => md.ipv4_address }
}

output "droplet_id" {
  description = "droplet's id="
  value       = values(digitalocean_droplet.my_droplets)[*].id
}