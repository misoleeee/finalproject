# 택시 호출

# Table of contents

- [예제 - 택시호출](#---)
  - [서비스 시나리오](#서비스-시나리오)
  - [분석/설계](#분석설계)
    - [클라우드 아키텍처 구성, MSA 아키텍처 구성도](#클라우드아키텍처구성,MSA아키텍처구성도)
    - [도메인분석 - 이벤트스토밍](#도메인분석-이벤트스토밍)
  - [구현:](#구현-)
    - [분산트랜잭션 - Saga](#분산트랜잭션-Saga)
    - [단일 진입점 - Gateway](#단일진입점-Gateway)
    - [보상처리 - Compensation ](#보상처리-Compensation)
    - [분산 데이터 프로젝션 - CQRS ](#분산데이터프로젝션-CQRS)
  - [운영](#운영)
    - [CI/CD 설정](#cicd설정)
    - [컨테이너 자동확장 - HPA ](#컨테이너자동확장-HPA)
    - [컨테이너로부터 환경분리 - CofigMap](#컨테이너로부터환경분리-CofigMap)
    - [클라우드스토리지 활용 - PVC ](#클라우드스토리지활용-PVC)
    - [셀프 힐링/무정지배포 - Liveness/Rediness Probe](#셀프힐링/무정지배포-Liveness)
    - [서비스 메쉬 응용 - Mesh](#서비스메쉬응용-Mesh)
    - [통합 모니터링 - Loggregation/Monitoring](#통합모니터링-Loggregation/Monitoring)

# 서비스 시나리오

카카오T 커버하기 

기능적 요구사항
1. 고객이 택시를 호출한다.
2. 택시기사가 승인하면 사전결제가 이루어진다.
3. 사전결제가 완료되면 택시가 출발한다.
4. 택시가 도착하면 최종결제가 이루어진다.
5. 고객이 호출을 취소할 수 있다.
6. 고객이 예약 상태를 중간중간 조회한다.

비기능적 요구사항
1. 트랜잭션
    1. 운행 가능한 택시가 없을 경우, 거래가 성립되지 않아야 한다 Sync 호출 
1. 장애격리
    1. 택시관리 기능이 수행되지 않더라도 365일 24시간 호출 받을 수 있어야 한다 Async (event-driven), Eventual Consistency
1. 성능
    1. 고객이 자주 예약관리에서 확인할 수 있는 택시상태를 Mypage에서 확인할 수 있어야 한다  CQRS
       

# 분석/설계


## 클라우드 아키텍처 구성, MSA 아키텍처 구성도
![image](https://github.com/kimkyusook/finalproject-miso/assets/156638565/261aa2b5-5751-4491-b833-5cf9818a4cfe)



## Event Storming 결과
* MSAEz 로 모델링한 이벤트스토밍 결과:  (https://www.msaez.io/#/storming/finalproject)
![image](https://github.com/kimkyusook/finalproject-miso/assets/156638565/952d27f5-f667-4e43-a9d0-33f036652eec)

    - 고객이 택시서비스를 선택하여 호출한다 (ok)
    -  고객이 택시를 호출한다. (ok)
    -  택시기사가 승인하면 사전결제가 이루어진다. (ok)
    -  사전결제가 완료되면 택시가 출발한다. (ok)
    -  택시가 도착하면 최종결제가 이루어진다. (ok)
    -  고객이 호출을 취소할 수 있다. (ok)
    -  고객이 예약 상태를 중간중간 조회한다. (ok)


### 비기능 요구사항에 대한 검증

![image](https://github.com/kimkyusook/finalproject-miso/assets/156638565/54381734-7563-4f53-8c17-b0867e8cdcd5)



    - 마이크로 서비스를 넘나드는 시나리오에 대한 트랜잭션 처리
    - 호출이 승인되지 않은 예약은 절대 받지 않는다에 따라, ACID 트랜잭션 적용. 

# 구현:

분석/설계 단계에서 도출된 아키텍처에 따라, 각 BC별로 대변되는 마이크로 서비스들을 스프링부트로 구현하였다. 구현한 각 서비스를 로컬에서 실행하는 방법은 아래와 같다 (각자의 포트넘버는 8080 ~ 808n 이다)

```
cd call
mvn spring-boot:run

cd Payment
mvn spring-boot:run 

cd driver
mvn spring-boot:run  

cd Mypage
mvn spring-boot:run

cd gateway
mvn spring-boot:run
```

## [분산트랜잭션 - Saga, 단일 진입점 - Gateway]

```
//가용 택시 재고 생성
gitpod /workspace/finalproject-miso (main) $ http post localhost:8080/drivers driverQty=300 taxiType="normal"
HTTP/1.1 201 Created
Content-Type: application/json
Date: Thu, 22 Feb 2024 11:03:12 GMT
Location: http://localhost:8083/drivers/1
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers
transfer-encoding: chunked

{
    "_links": {
        "driver": {
            "href": "http://localhost:8083/drivers/1"
        },
        "self": {
            "href": "http://localhost:8083/drivers/1"
        }
    },
    "driverQty": 300,
    "status": null,
    "taxiType": "normal"
}

//택시호출
gitpod /workspace/finalproject-miso (main) $ http post localhost:8080/calls customerId=12345 taxiType=1 charge=30000
HTTP/1.1 201 Created
Content-Type: application/json
Date: Thu, 22 Feb 2024 11:18:33 GMT
Location: http://localhost:8082/calls/2
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers
transfer-encoding: chunked

{
    "_links": {
        "call": {
            "href": "http://localhost:8082/calls/2"
        },
        "self": {
            "href": "http://localhost:8082/calls/2"
        }
    },
    "callDt": null,
    "charge": 30000,
    "customerId": 12345,
    "status": null,
    "taxiType": "1"
}
```
```
// 토픽확인
[appuser@fcd4fde9e121 bin]$ ./kafka-console-consumer --bootstrap-server localhost:9092 --topic localmslee  --from-beginning


{"eventType":"TaxiCalled","timestamp":1708599834533,"id":1,"customerId":12345,"status":null,"callDt":null,"charge":30000,"taxiType":"1"}
{"eventType":"TaxiAccepted","timestamp":1708599834744,"id":1,"driverQty":299,"taxiType":"1","status":null}
{"eventType":"AdvancePayment","timestamp":1708599834759,"id":1,"customerId":null,"driverId":null,"callDt":null,"charge":30000,"paymentStatus":null}
{"eventType":"TaxiDepartured","timestamp":1708599834766,"id":1,"driverQty":null,"taxiType":null,"status":null}
{"eventType":"TaxiArrived","timestamp":1708599834846,"id":1}
{"eventType":"FinalPayment","timestamp":1708599834852,"id":1,"customerId":null,"driverId":null,"callDt":null,"charge":30000,"paymentStatus":null}
```

## [보상트랜젝션 확인]
가용택시 재고를 0으로 생성 후 테스트하였다.
```
gitpod /workspace/finalproject-miso (main) $ http post localhost:8080/calls customerId=12345 taxiType=4 charge=30000
HTTP/1.1 201 Created
Content-Type: application/json
Date: Thu, 22 Feb 2024 11:21:38 GMT
Location: http://localhost:8082/calls/4
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers
transfer-encoding: chunked

{
    "_links": {
        "call": {
            "href": "http://localhost:8082/calls/4"
        },
        "self": {
            "href": "http://localhost:8082/calls/4"
        }
    },
    "callDt": null,
    "charge": 30000,
    "customerId": 12345,
    "status": null,
    "taxiType": "4"
}
```

```
{"eventType":"TaxiCalled","timestamp":1708600898387,"id":4,"customerId":12345,"status":null,"callDt":null,"charge":30000,"taxiType":"4"}
{"eventType":"TaxiCanceled","timestamp":1708600898394,"id":4,"driverQty":0,"taxiType":"4","status":null}
{"eventType":"CallCanceled","timestamp":1708600898442,"id":4,"customerId":null,"status":null,"callDt":null,"charge":null,"taxiType":"4"}
```

## [CQRS - mypage]
```
gitpod /workspace/finalproject-miso (main) $ http :8086/myPages/4
HTTP/1.1 200 
Connection: keep-alive
Content-Type: application/hal+json
Date: Thu, 22 Feb 2024 11:26:02 GMT
Keep-Alive: timeout=60
Transfer-Encoding: chunked
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers

{
    "_links": {
        "myPage": {
            "href": "http://localhost:8086/myPages/4"
        },
        "self": {
            "href": "http://localhost:8086/myPages/4"
        }
    },
    "callDt": null,
    "charge": 30000,
    "customerId": 12345,
    "status": null
}
```

# 운영

## CI/CD 설정


aws CodeBuild를 활용한 CI/CD 처리, pipeline build script 는 buildspec.yml 에 포함되었다.
```
version: 0.2

env:
  variables:
    IMAGE_REPO_NAME: "user14-taxi"

phases:
  install:
    runtime-versions:
      java: corretto17
      docker: 20
  pre_build:
    commands:
      - cd gateway
      - echo Logging in to Amazon ECR...
      - echo $IMAGE_REPO_NAME
      - echo $AWS_ACCOUNT_ID
      - echo $AWS_DEFAULT_REGION
      - echo $CODEBUILD_RESOLVED_SOURCE_VERSION
      - echo start command
      - $(aws ecr get-login --no-include-email --region $AWS_DEFAULT_REGION)
  build:
    commands:
      - echo Build started on `date`
      - echo Building the Docker image...
      - mvn package -Dmaven.test.skip=true
      - docker build -t $AWS_ACCOUNT_ID.dkr.ecr.$AWS_DEFAULT_REGION.amazonaws.com/$IMAGE_REPO_NAME:$CODEBUILD_RESOLVED_SOURCE_VERSION  .
  post_build:
    commands:
      - echo Build completed on `date`
      - echo Pushing the Docker image...
      - docker push $AWS_ACCOUNT_ID.dkr.ecr.$AWS_DEFAULT_REGION.amazonaws.com/$IMAGE_REPO_NAME:$CODEBUILD_RESOLVED_SOURCE_VERSION

#cache:
#  paths:
#    - '/root/.m2/**/*'
```

- 프로젝트 빌드 결과
![image](https://github.com/kimkyusook/finalproject-miso/assets/156638565/44fd5fe4-cf0b-4d6d-8ad6-9e22954964b6)


- 프라이빗 ECR 결과
![image](https://github.com/kimkyusook/finalproject-miso/assets/156638565/10895ee6-9b6b-4526-876d-7f8a7c40425c)


- S3
![image](https://github.com/kimkyusook/finalproject-miso/assets/156638565/03a6709b-9567-47a2-a771-032c0144ff62)


## 컨테이너 자동확장 - HPA 

- Kubernetes 클러스터에 Metrics Server를 배포하여 리소스 모니터링을 활성화
```
kubectl apply -f https://github.com/kubernetes-sigs/metrics-server/releases/latest/download/components.yaml
kubectl get deployment metrics-server -n kube-system
```

- deployment.yaml 아래 추가
```
containers:
        - name: call
          image: misoleeeee/call:0221
          ports:
            - containerPort: 8080
          resources:
            requests:
              cpu: "200m"
```

- Kubernetes 클러스터에서 call 디플로이먼트를 자동으로 확장
```
//cpu-percent=50: CPU 사용률이 50%를 넘으면 자동으로 스케일링을 시작
//min=1: 최소 파드 수를 1로 설정합니다. 따라서 최소 1개의 파드가 항상 실행
//max=3: 최대 파드 수를 3으로 설정합니다. 따라서 파드 수는 최대 3개까지 확장
kubectl autoscale deployment call --cpu-percent=50 --min=1 --max=3
```

- 부하 테스트 Pod 설치 후 부하 발생
```
kubectl apply -f - <<EOF
apiVersion: v1
kind: Pod
metadata:
  name: siege
spec:
  containers:
  - name: siege
    image: apexacme/siege-nginx
EOF

$ kubectl exec -it siege -- /bin/bash
$ siege -c20 -t40S -v http://10.100.41.22:8080/calls

...

Lifting the server siege...
Transactions:                  20054 hits
Availability:                 100.00 %
Elapsed time:                  39.57 secs
Data transferred:               5.97 MB
Response time:                  0.03 secs
Transaction rate:             506.80 trans/sec
Throughput:                     0.15 MB/sec
Concurrency:                   16.55
Successful transactions:       20056
Failed transactions:               0
Longest transaction:            0.35
Shortest transaction:           0.00

```

- autoscale 결과
```
gitpod /workspace/finalproject-miso (main) $ kubectl get all
NAME                       READY   STATUS    RESTARTS   AGE
pod/call-59bb97785-2h7n4   1/1     Running   0          41s
pod/call-59bb97785-2zmxl   1/1     Running   0          6m8s
pod/call-59bb97785-5d8gn   1/1     Running   0          41s
pod/my-kafka-0             1/1     Running   0          31h
pod/my-kafka-client        1/1     Running   0          30h
pod/siege                  1/1     Running   0          6h3m

NAME                        TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)                      AGE
service/call                ClusterIP   10.100.41.22    <none>        8080/TCP                     4m10s
service/kubernetes          ClusterIP   10.100.0.1      <none>        443/TCP                      32h
service/my-kafka            ClusterIP   10.100.108.59   <none>        9092/TCP                     31h
service/my-kafka-headless   ClusterIP   None            <none>        9092/TCP,9094/TCP,9093/TCP   31h

NAME                   READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/call   3/3     3            3           6m8s

NAME                             DESIRED   CURRENT   READY   AGE
replicaset.apps/call-59bb97785   3         3         3       6m8s

NAME                        READY   AGE
statefulset.apps/my-kafka   1/1     31h

NAME                                       REFERENCE         TARGETS    MINPODS   MAXPODS   REPLICAS   AGE
horizontalpodautoscaler.autoscaling/call   Deployment/call   484%/50%   1         3         3          102s

```



## 컨테이너로부터 환경분리 - CofigMap

- 진행 전 application-resource.yaml 파일에 logging 이 추가된 docker image를 사용
- 데이터베이스 연결 정보와 로그 레벨을 ConfigMap에 저장하여 관리
```
$ kubectl apply -f - <<EOF
apiVersion: v1
kind: ConfigMap
metadata:
  name: config-dev
  namespace: default
data:
  ORDER_DB_URL: jdbc:mysql://mysql:3306/connectdb1?serverTimezone=Asia/Seoul&useSSL=false
  ORDER_DB_USER: myuser
  ORDER_DB_PASS: mypass
  ORDER_LOG_LEVEL: DEBUG
EOF
```

```
gitpod /workspace/finalproject-miso/call (main) $ kubectl get configmap
NAME               DATA   AGE
config-dev         4      16m
kube-root-ca.crt   1      5h37m
my-kafka-scripts   1      4h11m

gitpod /workspace/finalproject-miso (main) $ kubectl get configmap config-dev -o yaml
apiVersion: v1
data:
  ORDER_DB_PASS: mypass
  ORDER_DB_URL: jdbc:mysql://mysql:3306/connectdb1?serverTimezone=Asia/Seoul&useSSL=false
  ORDER_DB_USER: myuser
  ORDER_LOG_LEVEL: INFO
kind: ConfigMap
metadata:
  annotations:
    kubectl.kubernetes.io/last-applied-configuration: |
      {"apiVersion":"v1","data":{"ORDER_DB_PASS":"mypass","ORDER_DB_URL":"jdbc:mysql://mysql:3306/connectdb1?serverTimezone=Asia/Seoul\u0026useSSL=false","ORDER_DB_USER":"myuser","ORDER_LOG_LEVEL":"DEBUG"},"kind":"ConfigMap","metadata":{"annotations":{},"name":"config-dev","namespace":"default"}}
  creationTimestamp: "2024-02-21T10:54:11Z"
  name: config-dev
  namespace: default
  resourceVersion: "76504"
  uid: b1580f81-495d-4b16-8ca0-d6a9dc7ae0e5
```


- call 재배포 후 로그 레벨 INFO에서 DEBUG 변경 확인 >>> kubectl logs -l app=call
![스크린샷 2024-02-21 200535](https://github.com/kimkyusook/finalproject-miso/assets/156638565/4684f47f-6d27-4333-bb0f-2dfd3d2a5faa)



## 클라우드스토리지 활용 - PVC 

```
// Kubernetes 클러스터에 PersistentVolumeClaim 생성
$ kubectl apply -f - <<EOF
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: ebs-pvc
  labels:
    app: ebs-pvc
spec:
  accessModes:
  - ReadWriteOnce
  resources:
    requests:
      storage: 1Mi
  storageClassName: ebs-sc
EOF
// pvc 생성했지만 consumer 등록 전까지 Pending 유지

gitpod /workspace/finalproject-miso (main) $ kubectl get pvc
NAME              STATUS   VOLUME                                     CAPACITY   ACCESS MODES   STORAGECLASS   AGE
data-my-kafka-0   Bound    pvc-6b085bb2-96cd-4a60-8cb2-97f5f16cd773   8Gi        RWO            ebs-sc         32h
ebs-pvc           Bound    pvc-5b636e92-130e-418a-b4e0-89ff92279c8c   1Gi        RWO            ebs-sc         27h
gitpod /workspace/finalproject-miso (main) $ kubectl get pv
NAME                                       CAPACITY   ACCESS MODES   RECLAIM POLICY   STATUS   CLAIM                         STORAGECLASS   REASON   AGE
pvc-5b636e92-130e-418a-b4e0-89ff92279c8c   1Gi        RWO            Delete           Bound    default/ebs-pvc               ebs-sc                  26h
pvc-6b085bb2-96cd-4a60-8cb2-97f5f16cd773   8Gi        RWO            Delete           Bound    default/data-my-kafka-0       ebs-sc                  32h
pvc-8b0ad07f-2efa-4fb4-8b39-9a02fcbc0142   8Gi        RWO            Delete           Bound    taxi/data-my-kafka-0          ebs-sc                  7h17m
```

```
// taxi서비스를 배포할 pvcconfig.yaml 생성
apiVersion: "apps/v1"
kind: "Deployment"
metadata:
  name: taxi
  labels:
    app: "taxi"
spec:
  selector:
    matchLabels:
      app: "taxi"
  replicas: 1
  template:
    metadata:
      labels:
        app: "taxi"
    spec:
      containers:
      - name: "taxi"
        image: misoleeeee/call:0221
        ports:
          - containerPort: 8080
        volumeMounts: # 컨테이너에 마운트할 볼륨을 정의
          - mountPath: "/mnt/data" # 볼륨을 마운트할 경로를 지정
            name: volume
      volumes: # Pod에 사용될 볼륨을 정의
      - name: volume
        persistentVolumeClaim:
           claimName: ebs-pvc 
```

```
gitpod /workspace/finalproject-miso/call (main) $ kubectl delete -f pvcconfig.yaml 
deployment.apps "taxi" deleted
gitpod /workspace/finalproject-miso/call (main) $ kubectl apply -f pvcconfig.yaml 
deployment.apps/taxi created

gitpod /workspace/finalproject-miso/call (main) $ kubectl get all
NAME                          READY   STATUS              RESTARTS   AGE
pod/call-59bb97785-xshnc      1/1     Running             0          85m
pod/gateway-ffc9b88b7-w9vt9   1/1     Running             0          144m
pod/my-kafka-0                1/1     Running             0          5h50m
pod/my-kafka-client           1/1     Running             0          5h21m
pod/mypage-6f7d566b4-2gxkp    1/1     Running             0          141m
pod/payment-9d96b4759-2495r   1/1     Running             0          144m
pod/siege                     1/1     Running             0          4h43m
pod/taxi-59f7498895-8kn6g     1/1     Running             0          45s
```
- taxi Pod에 대해 쉘을 실행하고, /mnt/data 경로에 test_mslee.txt 테스트 파일 생성
```         
gitpod /workspace/finalproject-miso/call (main) $ kubectl exec -it pod/taxi-59f7498895-8kn6g -- /bin/sh
/ # cd /mnt/data
/mnt/data # ls
lost+found
/mnt/data #  touch test_mslee.txt
/mnt/data # ls
test_mslee.txt  lost+found
/mnt/data # exit
```
- taxi 서비스 중지 후 다시 배포하였을때 클라우드스토리지에 생성한 test_mslee.txt 테스트 파일이 조회되어야한다.
```
gitpod /workspace/finalproject-miso/call (main) $ kubectl delete -f pvc-config.yaml
deployment.apps "taxi" deleted
gitpod /workspace/finalproject-miso/call (main) $ kubectl apply -f pvc-config.yaml
deployment.apps/taxi created
gitpod /workspace/finalproject-miso/call (main) $ kubectl get all
NAME                          READY   STATUS    RESTARTS   AGE
pod/call-59bb97785-xshnc      1/1     Running   0          87m
pod/gateway-ffc9b88b7-w9vt9   1/1     Running   0          146m
pod/my-kafka-0                1/1     Running   0          5h52m
pod/my-kafka-client           1/1     Running   0          5h23m
pod/mypage-6f7d566b4-2gxkp    1/1     Running   0          143m
pod/payment-9d96b4759-2495r   1/1     Running   0          146m
pod/siege                     1/1     Running   0          4h44m
pod/taxi-59f7498895-fksvm     1/1     Running   0          6s
gitpod /workspace/finalproject-miso/call (main) $ kubectl exec -it pod/taxi-59f7498895-fksvm -- /bin/sh
/ # cd /mnt/data
/mnt/data # ls
lost+found      test_mslee.txt
/mnt/data # 
```


# 셀프 힐링/무정지배포 - Liveness/Rediness Probe 

- HttpGet type의 Probe Action이 설정된 call 서비스 배포
``` 
$ vi deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: call
  labels:
    app: call
spec:
  replicas: 1
  selector:
    matchLabels:
      app: call
  template:
    metadata:
      labels:
        app: call
    spec:
      containers:
        - name: call
          image: misoleeeee/call:02222
          imagePullPolicy: Always
          ports:
            - containerPort: 8080
          livenessProbe:
            httpGet:
              path: '/actuator/health'
              port: 8080
            initialDelaySeconds: 15
            timeoutSeconds: 2
            successThreshold: 1
            periodSeconds: 5
            failureThreshold: 3

gitpod /workspace/finalproject-miso (main) $ kubectl apply -f kubernetes/deployment.yaml
gitpod /workspace/finalproject-miso (main) $ kubectl get service
NAME                TYPE           CLUSTER-IP       EXTERNAL-IP                                                                  PORT(S)                      AGE
call                LoadBalancer   10.100.137.119   ac86fee8391e3449895f46a1ea6d8309-2079251611.eu-central-1.elb.amazonaws.com   8080:30753/TCP               62s
kubernetes          ClusterIP      10.100.0.1       <none>                                                                       443/TCP                      33h
my-kafka            ClusterIP      10.100.108.59    <none>                                                                       9092/TCP                     31h
my-kafka-headless   ClusterIP      None             <none>                                                                       9092/TCP,9094/TCP,9093/TCP   31h                                                                 9092/TCP,9094/TCP,9093/TCP   31h


gitpod /workspace/finalproject-miso (main) $ kubectl expose deploy call --type=LoadBalancer --port=8080
service/call exposed
gitpod /workspace/finalproject-miso (main) $ http ac86fee8391e3449895f46a1ea6d8309-2079251611.eu-central-1.elb.amazonaws.com:8080/actuator/health
HTTP/1.1 200 
Connection: keep-alive
Content-Type: application/vnd.spring-boot.actuator.v3+json
Date: Thu, 22 Feb 2024 14:33:00 GMT
Keep-Alive: timeout=60
Transfer-Encoding: chunked

{
    "groups": [
        "liveness",
        "readiness"
    ],
    "status": "UP"
}

```

- call CrashLoopBackOff에 따른 서버복구 이력
```
gitpod /workspace/finalproject-miso (main) $ kubectl describe pod/call-exec
Name:             call-exec
Namespace:        default
Priority:         0
Service Account:  default
Node:             ip-192-168-8-95.eu-central-1.compute.internal/192.168.8.95
Start Time:       Thu, 22 Feb 2024 05:31:34 +0000
Labels:           test=call
Annotations:      <none>
Status:           Running
IP:               192.168.5.199
IPs:
  IP:  192.168.5.199
Containers:
  call:
    Container ID:  containerd://46e31881b596cabdd2cd25093b34adb2bc7b23da3b19412f931a26d5b7d3ee5f
    Image:         misoleeeee/call:02223
    Image ID:      docker.io/misoleeeee/call@sha256:d5ce0908ac54de774f4e7e93a4ce9066992a0ad5e5c60e2c5e1047e1d1987735
    Port:          <none>
    Host Port:     <none>
    Args:
      /bin/sh
      -c
      touch /tmp/healthy; sleep 30; rm -rf /tmp/healthy; sleep 600
    State:          Running
      Started:      Thu, 22 Feb 2024 05:32:15 +0000
    Last State:     Terminated
      Reason:       Error
      Exit Code:    143
      Started:      Thu, 22 Feb 2024 05:31:55 +0000
      Finished:     Thu, 22 Feb 2024 05:32:14 +0000
    Ready:          True
    Restart Count:  2
    Liveness:       exec [cat /tmp/healthy] delay=5s timeout=1s period=5s #success=1 #failure=3
    Environment:    <none>
    Mounts:
      /var/run/secrets/kubernetes.io/serviceaccount from kube-api-access-ljhtl (ro)
Conditions:
  Type              Status
  Initialized       True 
  Ready             True 
  ContainersReady   True 
  PodScheduled      True 
Volumes:
  kube-api-access-ljhtl:
    Type:                    Projected (a volume that contains injected data from multiple sources)
    TokenExpirationSeconds:  3607
    ConfigMapName:           kube-root-ca.crt
    ConfigMapOptional:       <nil>
    DownwardAPI:             true
QoS Class:                   BestEffort
Node-Selectors:              <none>
Tolerations:                 node.kubernetes.io/not-ready:NoExecute op=Exists for 300s
      
Events:
  Type     Reason     Age               From               Message
  ----     ------     ----              ----               -------
  Normal   Scheduled  46s               default-scheduler  Successfully assigned default/call-exec to ip-192-168-8-95.eu-central-1.compute.internal
  Warning  Unhealthy  6s (x6 over 36s)  kubelet            Liveness probe failed: cat: can't open '/tmp/healthy': No such file or directory
  Normal   Killing    6s (x2 over 26s)  kubelet            Container call failed liveness probe, will be restarted
  Normal   Pulled     5s (x3 over 45s)  kubelet            Container image "misoleeeee/call:02223" already present on machine
  Normal   Created    5s (x3 over 45s)  kubelet            Created container call
  Normal   Started    5s (x3 over 45s)  kubelet            Started container call
```

# 서비스 메쉬 응용 - Istio

-  Istio 설치
```
export ISTIO_VERSION=1.18.1
curl -L https://istio.io/downloadIstio | ISTIO_VERSION=$ISTIO_VERSION TARGET_ARCH=x86_64 sh -
export PATH=$PWD/bin:$PATH // 경로세팅
istioctl install --set profile=demo --set hub=gcr.io/istio-release // demo를 기반으로 core모듈 설치
kubectl get ns // 네임스페이스 생성확인
kubectl get all -n istio-system // 객체생성확인
```
```
// Istio add-on Dashboard 설치
mv samples/addons/loki.yaml samples/addons/loki.yaml.old
curl -o samples/addons/loki.yaml https://raw.githubusercontent.com/msa-school/Lab-required-Materials/main/Ops/loki.yaml
kubectl apply -f samples/addons
kubectl get svc -n istio-system // istio-ingressgateway 서비스 생성 확인, (참고,ClusterIP 타입은 외부서 접속 불가)
```

- 브라우저로 접속
  > - adbdc0a86925a4bb3843faacbc562d5c-1685211282.eu-central-1.elb.amazonaws.com  // istio-ingressgateway
  > - af52f3fc2d95a4c06b0973caf9bd8042-2104780021.eu-central-1.elb.amazonaws.com:20001/kiali/  //Kiali
  > - http://a3592e7dcb70349b4b927a8a5d456989-2066282320.eu-central-1.elb.amazonaws.com
```
gitpod /workspace/finalproject-miso (main) $ kubectl get service -n istio-system
NAME                   TYPE           CLUSTER-IP       EXTERNAL-IP                                                                  PORT(S)                                                                      AGE
grafana                LoadBalancer   10.100.78.179    a363b70e747f6425a9ef880f6521fa3a-570572981.eu-central-1.elb.amazonaws.com    3000:31829/TCP                                                               3h53m
istio-egressgateway    ClusterIP      10.100.159.78    <none>                                                                       80/TCP,443/TCP                                                               3h54m
istio-ingressgateway   LoadBalancer   10.100.250.93    adbdc0a86925a4bb3843faacbc562d5c-1685211282.eu-central-1.elb.amazonaws.com   15021:30063/TCP,80:31307/TCP,443:30502/TCP,31400:31616/TCP,15443:32157/TCP   3h54m
istiod                 ClusterIP      10.100.97.60     <none>                                                                       15010/TCP,15012/TCP,443/TCP,15014/TCP                                        3h54m
jaeger-collector       ClusterIP      10.100.41.58     <none>                                                                       14268/TCP,14250/TCP,9411/TCP                                                 3h53m
kiali                  LoadBalancer   10.100.134.79    af52f3fc2d95a4c06b0973caf9bd8042-2104780021.eu-central-1.elb.amazonaws.com   20001:31943/TCP,9090:31793/TCP                                               3h53m
loki                   ClusterIP      10.100.50.198    <none>                                                                       3100/TCP,9095/TCP                                                            3h53m
loki-headless          ClusterIP      None             <none>                                                                       3100/TCP                                                                     3h53m
loki-memberlist        ClusterIP      None             <none>                                                                       7946/TCP                                                                     3h53m
prometheus             LoadBalancer   10.100.237.150   a828f32a683bc46fd8312932494ee82a-2100726676.eu-central-1.elb.amazonaws.com   9090:31770/TCP                                                               3h53m
tracing                ClusterIP      10.100.125.172   <none>                                                                       80/TCP,16685/TCP                                                             3h53m
zipkin                 ClusterIP      10.100.229.35    <none>                                                                       9411/TCP                    
```



- Istio의 기능을 사용하여 네트워크 관리 및 보안을 강화하기 위해 Kubernetes 클러스터에 배포된 애플리케이션에 Sidecar 프록시를 주입
- Sidecar 프록시는 애플리케이션과 통신하여 네트워크 정책, 트래픽 관리 및 보안 기능을 제공
```
//inject Sidecar on Istio environment : 아래 YAML을 deployment.yaml로 저장
apiVersion: apps/v1
kind: Deployment
metadata:
  name: hello-nginx
spec:
  replicas: 1
  selector:
    matchLabels:
      app: hello-nginx
  template:
    metadata:
      labels:
        app: hello-nginx
    spec:
      containers:
        - name: hello-nginx
          image: nginx:latest
          ports:
            - containerPort: 80
```
```
istioctl kube-inject -f deployment.yaml > output.yaml //저장후 실행할 커맨드
```
![image](https://github.com/kimkyusook/finalproject-miso/assets/156638565/552795bd-3d7a-49ed-b567-4d2c023e37a1)
![image](https://github.com/kimkyusook/finalproject-miso/assets/156638565/d86b0600-1962-4cbd-b1bb-a1c259cef84c)
![image](https://github.com/kimkyusook/finalproject-miso/assets/156638565/e0d6a65d-b69c-40e3-9fd4-cba09b478f79)



# 통합 모니터링 - Loggregation/Monitoring

- Prometheus/Grafana기반 K8s 통합 모니터링
```
// 통합로깅대상 서비스 설치
gitpod /workspace/finalproject-miso/istio-1.18.1 (main) $ kubectl create ns taxi
gitpod /workspace/finalproject-miso/istio-1.18.1 (main) $ kubectl label namespace taxi istio-injection=enabled
gitpod /workspace/finalproject-miso/istio-1.18.1 (main) $ kubectl apply -f ../call/kubernetes/deployment.yaml -n taxi
gitpod /workspace/finalproject-miso/istio-1.18.1 (main) $ kubectl expose deploy call --port=8080 -n taxi
# taxi에 kafka 설치
helm install my-kafka bitnami/kafka --version 23.0.5 --namespace taxi
# Client Pod deploy
kubectl apply -f https://raw.githubusercontent.com/acmexii/demo/master/edu/siege-pod.yaml -n taxi

// Prometheus UI 사용을 위해 Service Scope을 LoadBalancer Type으로 수정
kubectl patch service/prometheus -n istio-system -p '{"spec": {"type": "LoadBalancer"}}'
```
![스크린샷 2024-02-22 175401](https://github.com/kimkyusook/finalproject-miso/assets/156638565/99bb9cf2-1b87-42fa-b288-f3f4987b7b81)


- 계속하기 전 call 서비스 엔드 포인트를 조회한다
``` 
kubectl exec -it pod/siege -n taxi -- /bin/bash
gitpod /workspace/finalproject-miso/istio-1.18.1 (main) $ kubectl exec -it pod/siege -n taxi -- /bin/bash
root@siege:/# http GET http://call:8080
HTTP/1.1 200 OK
content-type: application/hal+json
date: Thu, 22 Feb 2024 14:47:24 GMT
server: envoy
transfer-encoding: chunked
vary: Origin,Access-Control-Request-Method,Access-Control-Request-Headers
x-envoy-upstream-service-time: 4

{
    "_links": {
        "calls": {
            "href": "http://call/calls{?page,size,sort}",
            "templated": true
        },
        "orderStatuses": {
            "href": "http://call/orderStatuses{?page,size,sort}",
            "templated": true
        },
        "profile": {
            "href": "http://call/profile"
        }
    }
}
```

- 부하테스트
```
siege -c30 -t40S -v http://call:8080
```

- Expression Browser에 아래 쿼리로 모니터링
```
rate(istio_requests_total{app="call",destination_service="call.taxi.svc.cluster.local",response_code="200"}[5m])
```
![image](https://github.com/kimkyusook/finalproject-miso/assets/156638565/17f3b261-25d3-45de-a00e-32c14b453d37)

![image](https://github.com/kimkyusook/finalproject-miso/assets/156638565/58f936b1-a41a-4873-a3d9-9e69a2c1f738)
![image](https://github.com/kimkyusook/finalproject-miso/assets/156638565/1b859142-19d9-4bca-9032-2fdb5dd30c6f)



- Grafana 서비스 Open
```
- kubectl patch service/grafana -n istio-system -p '{"spec": {"type": "LoadBalancer"}}'
- a363b70e747f6425a9ef880f6521fa3a-570572981.eu-central-1.elb.amazonaws.com/3000
```
![스크린샷 2024-02-22 175110](https://github.com/kimkyusook/finalproject-miso/assets/156638565/f2f4b767-c732-4fc4-9057-e6016cd00f91)
![스크린샷 2024-02-22 175204](https://github.com/kimkyusook/finalproject-miso/assets/156638565/da758ff9-dc26-40ad-9d7c-9f76217e9492)





- 부하테스트
```
siege -c30 -t40S -v http://call:8080
```
![스크린샷 2024-02-22 175709](https://github.com/kimkyusook/finalproject-miso/assets/156638565/7cae3a94-240c-4b6e-a31c-1e982183ed54)

![image](https://github.com/kimkyusook/finalproject-miso/assets/156638565/b8bd7fc8-54f2-41bf-9575-ea6d49e2b32d)
