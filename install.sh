#!/bin/sh

mvn clean compile assembly:single

python3 -m spacy download en_core_web_sm --user