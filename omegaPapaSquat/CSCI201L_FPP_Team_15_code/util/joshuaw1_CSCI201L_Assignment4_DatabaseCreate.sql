DROP DATABASE if exists omegaLift;
CREATE DATABASE omegaLift;
USE omegaLift;
CREATE TABLE Workouts (
	workoutID int primary key not null auto_increment,
    title varchar(200) not null,
    caption text not null,
    description text not null,
    posted datetime not null
);