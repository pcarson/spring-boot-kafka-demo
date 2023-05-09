FROM grafana/grafana-oss:latest

# create a datasource and default dashboard
COPY provisioning/prometheus-datasource-config.yml /etc/grafana/provisioning/datasources/influx-datasource.yml
COPY provisioning/demo-dashboard-config.yml /etc/grafana/provisioning/dashboards/demo-dashboard.yml
COPY jvm-micrometer_rev9.json /etc/grafana/dashboards/jvm-micrometer_rev9-dashboard.json
COPY demo-dashboard.json /etc/grafana/dashboards/demo-dashboard.json

