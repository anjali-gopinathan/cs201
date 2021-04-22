DROP DATABASE if exists salStocks;
CREATE DATABASE salStocks;
USE salStocks;
CREATE TABLE Stocks (
	stockID int primary key not null auto_increment,
    ticker varchar(200) not null,
    name text not null,
    exchangeCode text not null,
    description text not null,
    startDate text not null.
    stockBrokers int not null
);