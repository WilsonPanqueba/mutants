#!/bin/sh
# Parametros
OWNER=wapl
TAGS=mutant
VERSION=0.0.1-SNAPSHOT.jar
BUCKET=${TAGS}${OWNER}
APPJAR=${TAGS}-${VERSION}
INSTANCES=2

# Crea el bucket
gsutil mb gs://${BUCKET}
gsutil cp build/libs/* gs://${BUCKET}/${APPJAR}

gcloud compute instance-templates create ${TAGS}-template \
    --image-family debian-9 \
    --image-project debian-cloud \
    --machine-type g1-small \
    --scopes "userinfo-email,cloud-platform" \
    --metadata-from-file startup-script=${TAGS}-startup.sh \
    --metadata BUCKET=${BUCKET} \
    --tags ${TAGS}

gcloud compute instance-groups managed create ${TAGS}-group \
    --base-instance-name ${TAGS}-group \
    --size ${INSTANCES} \
    --template ${TAGS}-template \
    --zone us-east1-b

gcloud compute firewall-rules create default-allow-http-8080 \
    --allow tcp:8080 \
    --source-ranges 0.0.0.0/0 \
    --target-tags ${TAGS} \
    --description "Allow port 8080 access to "${TAGS}

gcloud compute instances list

gcloud compute http-health-checks create ${TAGS}-health-check \
    --request-path /health \
    --port 8080

gcloud compute instance-groups managed set-named-ports ${TAGS}-group \
    --named-ports http:8080 \
    --zone us-east1-b

gcloud compute backend-services create ${TAGS}-service \
    --http-health-checks ${TAGS}-health-check \
    --global

gcloud compute backend-services add-backend ${TAGS}-service \
    --instance-group ${TAGS}-group \
    --global \
    --instance-group-zone us-east1-b

gcloud compute url-maps create ${TAGS}-service-map \
    --default-service ${TAGS}-service

gcloud compute target-http-proxies create ${TAGS}-service-proxy \
    --url-map ${TAGS}-service-map

gcloud compute forwarding-rules create ${TAGS}-http-rule \
    --target-http-proxy ${TAGS}-service-proxy \
    --ports 80 \
    --global

gcloud compute backend-services get-health ${TAGS}-service \
    --global

gcloud compute forwarding-rules list --global

