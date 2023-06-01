mkdir repos
cd repos

git clone https://oauth2:glpat-yhvmah644bzQWq7-jUM8@gitlab-stud.elka.pw.edu.pl/pis1/movieservice.git
git clone https://oauth2:glpat-yhvmah644bzQWq7-jUM8@gitlab-stud.elka.pw.edu.pl/pis1/eurekaserver.git
git clone https://oauth2:glpat-yhvmah644bzQWq7-jUM8@gitlab-stud.elka.pw.edu.pl/pis1/gateway-service.git
git clone https://oauth2:glpat-yhvmah644bzQWq7-jUM8@gitlab-stud.elka.pw.edu.pl/pis1/userauthservice.git
git clone https://oauth2:glpat-yhvmah644bzQWq7-jUM8@gitlab-stud.elka.pw.edu.pl/pis1/searchmicroservice.git
git clone https://oauth2:glpat-yhvmah644bzQWq7-jUM8@gitlab-stud.elka.pw.edu.pl/pis1/movies-django.git
git clone https://oauth2:glpat-yhvmah644bzQWq7-jUM8@gitlab-stud.elka.pw.edu.pl/pis1/frontend.git

echo "create table if not exists public.users
(
    id       serial
        primary key,
    name     varchar,
    email    varchar,
    password varchar,
    role     varchar
);

alter table public.users
    owner to postgres;

create database pis_projekt_movies
    with owner postgres;" > db.ddl;
    
echo "services:
  db:
    image: postgres
    restart: always
    environment:
      POSTGRES_PASSWORD: 'postgres'
      PGPASSWORD: 'postgres'
    ports:
      - '5432:5432'
    expose:
      - 5432
    healthcheck:
      test: pg_isready
      interval: 10s
      retries: 5
      start_period: 5s
      timeout: 10s
    volumes:
      - ./db.ddl:/docker-entrypoint-initdb.d/create_tables.sql
  frontend:
    build:
      context: frontend
      target: frontend-build
    ports:
      - '3000:3000'
    volumes:
      - ./frontend/src:/frontend/src:ro" > compose.yaml

cd eurekaserver
mvn package
java -jar ./target/eureka.server-0.0.1-SNAPSHOT.jar &
cd ..

sudo docker compose build
sudo docker compose up -d

cd movieservice
mvn package
mkdir -p ~/minio/data
sudo docker run  -d \
  -p 9000:9000 \
  -p 9001:9001 \
  --name minio2 \
  -v ~/minio/data:/data \
  -e "MINIO_ROOT_USER=TTeKtaePWGwGM1IB" \
  -e "MINIO_ROOT_PASSWORD=IGeMJlTC9tua7BYsa8OvWqbOEXkx7jmT" \
  quay.io/minio/minio server /data --console-address ":9001"
  
java -jar ./target/MovieService-0.0.1-SNAPSHOT.jar &

cd ../userauthservice
mvn package
java -jar ./target/UserAuthService-0.0.1-SNAPSHOT.jar &

docker run -d -p 9200:9200 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:7.17.3

cd ../gateway-service
mvn package
java -jar ./target/gateway-service-0.0.1-SNAPSHOT.jar &

cd ../movies-django
sudo apt install python3-pip
pip3 install -r requirements.txt

python3 manage.py makemigrations
python3 manage.py migrate 
python3 manage.py runserver 127.0.0.1:14432 &

cd ../searchmicroservice
mvn package
java -jar ./target/SearchMicroservice-0.0.1-SNAPSHOT.jar

