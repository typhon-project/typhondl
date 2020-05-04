#!/bin/bash
echo "Create Typhon namespace"
kubectl create namespace typhon 
sleep 1
echo "Deploy databases and upload models to metadata database (polystore-mongo)"
kubectl apply -n typhon -f databases.yaml 
sleep 1
echo "Wait for databases to complete startup"
kubectl wait --for=condition=complete --timeout=300s -n typhon job.batch/insert-models 
echo "Deploy Polystore"
kubectl apply -n typhon -f polystore.yaml 

echo "Polystore installation completed."
echo "It may take a few minutes for all services to be up and running."
exit 1
