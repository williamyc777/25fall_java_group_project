FROM node:18.17.1
LABEL authors="Shinomiya"

EXPOSE 8080

COPY ./ /usr/src/myapp/frontend
WORKDIR /usr/src/myapp/frontend

RUN chmod 777 node_modules/.bin/vue-cli-service && \
    rm Jenkinsfile && \
    rm Dockerfile

CMD ["npm", "run", "serve"]

