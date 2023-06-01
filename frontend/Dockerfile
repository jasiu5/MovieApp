# FROM node:latest as build

# WORKDIR /app
# COPY package*.json /app/

# RUN npm install
# COPY ./ /app/
# RUN npm run build

# FROM nginx:latest
# COPY --from=build app/build/ /usr/share/nginx/html

FROM --platform=$BUILDPLATFORM node:lts AS frontend-build

WORKDIR /frontend
COPY package.json /frontend/package.json
COPY package-lock.json /frontend/package-lock.json

RUN npm config set legacy-peer-deps true &&\
    npm ci
COPY . /frontend

ENV CI=true
ENV PORT=3000

CMD [ "npm", "start" ]
