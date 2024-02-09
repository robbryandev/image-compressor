FROM node:16-alpine

WORKDIR /app

COPY . .

RUN npm ci
RUN npm i -g serve

RUN npm run build

ENV NODE_ENV production

EXPOSE 3000

CMD [ "npx", "serve", "build" ]