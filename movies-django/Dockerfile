FROM --platform=$BUILDPLATFORM python:3.10-bullseye AS backend-build
EXPOSE 8000
WORKDIR /backend
COPY requirements.txt /backend
RUN pip3 install -r requirements.txt --no-cache-dir
COPY . /backend

CMD python3 manage.py makemigrations api; python3 manage.py makemigrations; python3 manage.py migrate; python3 manage.py runserver 127.0.0.1:14432
