FROM java:8-jre-alpine

COPY maven/app/* /app/
COPY configuration.yml /etc/init.d/fakesmtp/
COPY fakesmtp.sh /app/

EXPOSE 80 25 8081

WORKDIR /app

RUN chmod +x /app/fakesmtp.sh

ENTRYPOINT ["/app/fakesmtp.sh"]
CMD ["server"]