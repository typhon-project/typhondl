#!/bin/bash
kubeconfig=""
echo "Create Typhon namespace"
kubectl create namespace typhon $kubeconfig
echo "Running Typhon Kafka K8s installation ..."
kubectl create namespace kafka $kubeconfig
sleep 1
kubectl apply -n kafka -f kafka/strimzi-0.17.0/install/cluster-operator/ $kubeconfig
sleep 1
kubectl apply -n typhon -f kafka/strimzi-0.17.0/install/cluster-operator/020-RoleBinding-strimzi-cluster-operator.yaml $kubeconfig
sleep 1
kubectl apply -n typhon -f kafka/strimzi-0.17.0/install/cluster-operator/032-RoleBinding-strimzi-cluster-operator-topic-operator-delegation.yaml $kubeconfig
sleep 1
kubectl apply -n typhon -f kafka/strimzi-0.17.0/install/cluster-operator/031-RoleBinding-strimzi-cluster-operator-entity-operator-delegation.yaml $kubeconfig
sleep 2
kubectl create -n typhon -f kafka/typhon-cluster.yml $kubeconfig
echo "Waiting for Typhon Kafka K8s deployment to complete ..."
kubectl wait kafka/typhon-cluster --for=condition=Ready --timeout=300s -n typhon $kubeconfig
echo "Typhon Kafka K8s deployment completed."
echo "Typhon Kafka K8s installation completed."
echo ""
echo "Running Typhon Flink K8s installation ..."
sleep 2
kubectl -n typhon apply -f flink/flink-configuration-configmap.yaml $kubeconfig
sleep 1
kubectl -n typhon apply -f flink/jobmanager-service.yaml $kubeconfig
sleep 1
kubectl -n typhon apply -f flink/jobmanager-deployment.yaml $kubeconfig
sleep 1
kubectl -n typhon apply -f link/taskmanager-deployment.yaml $kubeconfig
sleep 1
echo "Typhon Flink K8s installation completed."
echo "It may take a few minutes for all services to be up and running."
