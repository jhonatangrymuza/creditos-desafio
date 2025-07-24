# Dockerfile para o frontend Angular
FROM node:18-alpine AS builder

# Diretório de trabalho
WORKDIR /app

# Copiar package.json e package-lock.json
COPY package*.json ./

# Instalar dependências com --force para resolver conflitos
RUN npm install --force

# Copiar código fonte
COPY . .

# Build da aplicação para produção
RUN npm run build

# Estágio final com Nginx
FROM nginx:alpine

# Copiar configuração customizada do Nginx
COPY devops/nginx.conf /etc/nginx/nginx.conf

# Copiar arquivos buildados
COPY --from=builder /app/dist/creditos-frontend /usr/share/nginx/html

# Expor porta 80
EXPOSE 80

# Nginx já tem seu próprio CMD
