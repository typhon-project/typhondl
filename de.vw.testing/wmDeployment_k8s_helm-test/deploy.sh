#!/bin/bash
kubeconfig=""
echo "Create Typhon namespace"
kubectl create namespace typhon $kubeconfig
if [ -n "$kubeconfig" ]; then
    echo "Using Cluster configuration file ${kubeconfig}"
fi
sleep 1
echo "----------------------------------------------------------------------------"
echo "Create databases"
kubectl apply -n typhon -f databases.yaml $kubeconfig
sleep 1
echo "Apply Helm Charts"
helm repo add bitnami https://charts.bitnami.com/bitnami
helm install vehiclemetadatadb --set fullnameOverride=vehiclemetadatadb --set rootUser.password=password bitnami/mariadbgalera -n typhon
echo "----------------------------------------------------------------------------"
echo "Wait for the models to be inserted into the metadata database"
kubectl wait --for=condition=complete --timeout=300s -n typhon job.batch/insert-models $kubeconfig
kubectl logs job/insert-models
echo "----------------------------------------------------------------------------"
echo "Wait for all databases to be ready"
kubectl wait --for=condition=available --timeout=100s --all -n typhon deployments $kubeconfig
echo "----------------------------------------------------------------------------"
echo "Deploy Polystore"
kubectl apply -n typhon -f polystore.yaml $kubeconfig
echo "----------------------------------------------------------------------------"
echo "Wait for the API, UI and QL to be ready"
kubectl wait --for=condition=available --timeout=300s --all -n typhon deployments $kubeconfig
echo "----------------------------------------------------------------------------"
echo "Polystore installation completed."
exit 1
