FROM node:22-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm install -g @ionic/cli
RUN npm install
COPY . ./
# RUN npm run build --prod
RUN ionic build --dev
### STAGE 2: Run ###

FROM nginx:alpine

COPY --from=build /app/nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/www/ /usr/share/nginx/html/

CMD [ "nginx" , "-g" , "daemon off;" ]