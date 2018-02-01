#!/usr/bin/env bash

openssl aes-256-cbc -K $encrypted_6f81f2bbc018_key -iv $encrypted_6f81f2bbc018_iv -in cd/codesigning.gpg.enc -out cd/codesigning.gpg -d

