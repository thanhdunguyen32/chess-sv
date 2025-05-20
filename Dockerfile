FROM mysql:8.0

# Copy SQL files vào container
COPY ./sql/login /home/sql/login/
COPY ./sql/game /home/sql/game/ 