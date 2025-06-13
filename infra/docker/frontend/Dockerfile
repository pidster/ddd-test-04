# Build stage
FROM node:20-alpine as build

# Set working directory
WORKDIR /app

# Environment variables
ARG APP_NAME
ENV REACT_APP_API_BASE_URL=/api

# Copy package.json and install dependencies
COPY ${APP_NAME}/package.json ${APP_NAME}/package-lock.json ./
RUN npm ci

# Copy source code
COPY ${APP_NAME}/public ./public
COPY ${APP_NAME}/src ./src
COPY ${APP_NAME}/tsconfig.json ./

# Build the application
RUN npm run build

# Production stage
FROM nginx:alpine

# Copy built files from build stage
COPY --from=build /app/build /usr/share/nginx/html

# Copy nginx configuration
COPY nginx/default.conf /etc/nginx/conf.d/default.conf

# Expose port
EXPOSE 80

# Start nginx
CMD ["nginx", "-g", "daemon off;"]
