#!/bin/sh
# Parametros
OWNER="wapl"
PROJECT="mutant"
VERSION="0.0.2-SNAPSHOT.jar"
TAGS="mutant-0.0.2-SNAPSHOT.jar"
BUCKET="mutantwapl"
APPJAR="mutant-0.0.2-SNAPSHOT.jar"
INSTANCES=1
# Crea el bucket
#gsutil mb gs://mutantwapl
#gsutil cp build/libs/* gs://mutantwapl/$APPJAR
PROJECTID=$(curl -s "http://metadata.google.internal/computeMetadata/v1/project/project-id" -H "Metadata-Flavor: Google")
gcloud compute instance-templates create mutant-0.0.2-SNAPSHOT.jar-template \
    --image-family debian-9 \
    --image-project debian-cloud \
    --machine-type g1-small \
    --scopes "userinfo-email,cloud-platform" \
    --metadata-from-file startup-script=mutant-0.0.2-SNAPSHOT.jar-startup$.sh \
    --metadata BUCKET=mutantwapl \
    --tags mutant-0.0.2-SNAPSHOT.jar
gcloud compute instance-groups managed create mutant-0.0.2-SNAPSHOT.jar-group \
    --base-instance-name mutant-0.0.2-SNAPSHOT.jar-group \
    --size 1 \
    --template mutant-0.0.2-SNAPSHOT.jar-template \
    --zone us-east1-b
gcloud compute firewall-rules create default-allow-http-8080 \
    --allow tcp:8080 \
    --source-ranges 0.0.0.0/0 \
    --target-tags mutant-0.0.2-SNAPSHOT.jar \
    --description "Allow port 8080 access to "mutant-0.0.2-SNAPSHOT.jar
gcloud compute instances list
gcloud compute http-health-checks create mutant-0.0.2-SNAPSHOT.jar-health-check \
    --request-path /health \
    --port 8080
gcloud compute instance-groups managed set-named-ports mutant-0.0.2-SNAPSHOT.jar-group \
    --named-ports http:8080 \
    --zone us-east1-b
gcloud compute backend-services create mutant-0.0.2-SNAPSHOT.jar-service \
    --http-health-checks mutant-0.0.2-SNAPSHOT.jar-health-check \
    --global
gcloud compute backend-services add-backend mutant-0.0.2-SNAPSHOT.jar-service \
    --instance-group mutant-0.0.2-SNAPSHOT.jar-group \
    --global \
    --instance-group-zone us-east1-b
gcloud compute url-maps create mutant-0.0.2-SNAPSHOT.jar-service-map \
    --default-service mutant-0.0.2-SNAPSHOT.jar-service
gcloud compute target-http-proxies create mutant-0.0.2-SNAPSHOT.jar-service-proxy \
    --url-map mutant-0.0.2-SNAPSHOT.jar-service-map
gcloud compute forwarding-rules create mutant-0.0.2-SNAPSHOT.jar-http-rule \
    --target-http-proxy mutant-0.0.2-SNAPSHOT.jar-service-proxy \
    --ports 80 \
    --global
gcloud compute backend-services get-health mutant-0.0.2-SNAPSHOT.jar-service \
    --global
gcloud compute forwarding-rules list --global