# defaul shell
SHELL = /bin/bash

# Rule "help"
.PHONY: help
.SILENT: help
help:
	echo "Use make [rule]"
	echo "Rules:"
	echo ""
	echo "build 		- build application and generate docker image"
	echo "run-db 		- run mysql database on docker"
	echo "run-app		- run application on docker"
	echo "stop-app	- stop application"
	echo "stop-db		- stop database"
	echo "rm-app		- stop and delete application"
	echo "rm-db		- stop and delete database"
	echo ""
	echo "k-setup		- init minikube machine"
	echo "k-deploy-db	- deploy mysql on cluster"
	echo "build-app	- build app"
	echo "k-build-image - build app and create docker image inside minikube"
	echo "k-deploy-app	- deploy app on cluster"
	echo ""
	echo "k-start		- start minikube machine"
	echo "k-all		- do all the above k- steps"
	echo "k-stop		- stop minikube machine"
	echo "k-delete	- stop and delete minikube machine"
	echo ""
	echo "check		- check tools versions"
	echo "help		- show this message"

build-app:
	mvn clean package;

run-db:
	docker run --name mysql56 -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_PASSWORD=root -e MYSQL_DATABASE=brewer -d mysql:5.6

run-app:
	docker run --name brewer-app -p 8080:8080 -e JAVA_OPTS=-Dspring.profiles.active=docker-desenv -e JDBC_URL=jdbc:mysql://mysql56:3306/brewer?useSSL=false -e JDBC_USER=root -e JDBC_PASS=root --link mysql56:mysql56 -d rfaguiar/brewer:latest

stop-app:
	docker stop brewer-app; \

stop-db:
	docker stop mysql56;

rm-app:	stop-app
	docker rm brewer-app; \

rm-db: stop-db
	docker rm mysql56

k-setup:
	minikube -p dev-to start --cpus 2 --memory=4098; \
	minikube -p dev-to addons enable ingress; \
	minikube -p dev-to addons enable metrics-server; \
	kubectl create namespace dev-to; \

k-deploy-db:
	kubectl apply -f kubernetes/mysql/;


k-build-image: build-app
	eval $$(minikube -p dev-to docker-env) && docker build --force-rm -t rfaguiar/brewer:latest .;

k-deploy-app:
	kubectl apply -f kubernetes/app/;

k-start:
	minikube -p dev-to start;

k-all: k-setup k-deploy-db k-build-image k-deploy-app

k-stop:
	minikube -p dev-to stop;

k-delete:
	minikube -p dev-to stop && minikube -p dev-to delete

check:
	echo "make version " && make --version && echo
	minikube version && echo
	echo "kubectl version" && kubectl version --short --client && echo
	echo "virtualbox version" && vboxmanage --version  && echo

