#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 -U "$POSTGRES_USER" <<-EOSQL
CREATE DATABASE IF NOT EXISTS kenac_auth_db;
CREATE DATABASE IF NOT EXISTS kenac_banking_db;
CREATE DATABASE IF NOT EXISTS kenac_customer_db;
EOSQL
