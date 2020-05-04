#!/bin/bash
kubeconfig="--kubeconfig=/home/flug/.kube/config"
echo "Create Typhon namespace"
if [ -n "$kubeconfig" ]; then
    echo "Using Cluster configuration file ${kubeconfig}"
fi
kubectl create namespace typhon $kubeconfig
sleep 1
echo "Deploy databases and upload models to metadata database (polystore-mongo)"
kubectl apply -n typhon -f databases.yaml $kubeconfig
sleep 1
echo "Wait for databases to complete startup"
kubectl wait --for=condition=complete --timeout=300s -n typhon job.batch/insert-models $kubeconfig
echo "Deploy Polystore"
kubectl apply -n typhon -f polystore.yaml $kubeconfig

echo "Polystore installation completed."
echo "It may take a few minutes for all services to be up and running."
exit 1
