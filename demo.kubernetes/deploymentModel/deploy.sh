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
helm repo add bitnami https://charts.bitnami.com/bitnami
helm install vehicledatadb --set fullnameOverride=vehicledatadb --set mongodbRootPassword=km2ZKCTDnFkegtkf bitnami/mongodb-sharded -n typhon

helm repo add bitnami https://charts.bitnami.com/bitnami
helm install appdata --set fullnameOverride=appdata --set rootUser.password=P0Sx5M1p0yMPzDWW bitnami/mariadb-galera -n typhon


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
echo "Running Typhon Kafka K8s installation ..."
kubectl create namespace kafka $kubeconfig
sleep 1
kubectl apply -n kafka -f analyticsKubernetes/kafka/strimzi-0.17.0/install/cluster-operator/ $kubeconfig
sleep 1
kubectl apply -n typhon -f analyticsKubernetes/kafka/strimzi-0.17.0/install/cluster-operator/020-RoleBinding-strimzi-cluster-operator.yaml $kubeconfig
sleep 1
kubectl apply -n typhon -f analyticsKubernetes/kafka/strimzi-0.17.0/install/cluster-operator/032-RoleBinding-strimzi-cluster-operator-topic-operator-delegation.yaml $kubeconfig
sleep 1
kubectl apply -n typhon -f analyticsKubernetes/kafka/strimzi-0.17.0/install/cluster-operator/031-RoleBinding-strimzi-cluster-operator-entity-operator-delegation.yaml $kubeconfig
sleep 2
kubectl create -n typhon -f analyticsKubernetes/kafka/typhon-cluster.yaml $kubeconfig
echo "Waiting for Typhon Kafka K8s deployment to complete ..."
kubectl wait kafka/typhon-cluster --for=condition=Ready --timeout=300s -n typhon $kubeconfig
echo "Typhon Kafka K8s deployment completed."
echo "Typhon Kafka K8s installation completed."
echo ""
echo "Running Typhon Flink K8s installation ..."
sleep 2
kubectl -n typhon apply -f analyticsKubernetes/flink/flink-configuration-configmap.yaml $kubeconfig
sleep 1
kubectl -n typhon apply -f analyticsKubernetes/flink/jobmanager-service.yaml $kubeconfig
sleep 1
kubectl -n typhon apply -f analyticsKubernetes/flink/jobmanager-deployment.yaml $kubeconfig
sleep 1
kubectl -n typhon apply -f analyticsKubernetes/flink/taskmanager-deployment.yaml $kubeconfig
sleep 1
echo "Typhon Flink K8s installation completed."
echo "It may take a few minutes for all services to be up and running."	
echo "Polystore installation completed."
