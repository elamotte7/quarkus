use `quarkus`;

create table CALCUL (
  answerId bigint(20) not null primary key,
  companyId bigint(20),
  groupId bigint(20),
  userId bigint(20),
  createDate datetime(6) null,
  modifiedDate datetime(6) null,
  createdBy bigint(20),
  modifiedBy bigint(20),
  ipsosCode VARCHAR(75) null,
  order_ INTEGER,
  global BOOLEAN,
  label VARCHAR(75) null,
  valid BOOLEAN
);

CREATE TABLE `INDICES`(
    id bigint(20) not null primary key,
    variation DOUBLE,
    type VARCHAR(255) null
);

CREATE TABLE `ESPERANCE`(
    id bigint(20) not null primary key,
    sexe VARCHAR(1),
    age DOUBLE,
    esperance_de_vie DOUBLE
);

CREATE TABLE `BAREME_FISCAL`(
    id bigint(20) not null primary key,
    age DOUBLE,
    taux DOUBLE
);
