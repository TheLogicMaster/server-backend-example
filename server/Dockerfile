FROM python:3.8-slim

MAINTAINER TheLogicMaster

USER root

WORKDIR /app

ADD . /app

RUN pip install --trusted-host pypi.python.org -r requirements.txt

EXPOSE 6969

CMD ["python", "main.py"]
