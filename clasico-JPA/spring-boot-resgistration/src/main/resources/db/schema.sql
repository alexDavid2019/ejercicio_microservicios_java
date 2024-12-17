
DROP TABLE IF EXISTS `roles`
DROP TABLE IF EXISTS `phones`
DROP TABLE IF EXISTS `users`
DROP TABLE IF EXISTS `user_roles`

CREATE TABLE `phones` (
	`id` BINARY(16) NOT NULL,
	`user_id` BINARY(16) NOT NULL,
	`number` VARCHAR(50) NOT NULL,
	`cityCode` VARCHAR(10) NOT NULL,
	`countryCode` VARCHAR(10) NOT NULL,
	PRIMARY KEY ( id )
);

CREATE TABLE `roles` (
	`id` BINARY(16) NOT NULL,
	`role_name` VARCHAR(255) NOT NULL,
	PRIMARY KEY ( id )
);

CREATE TABLE `users` (
	`id` BINARY(16) NOT NULL,
	`name` VARCHAR(255) NOT NULL,
	`email` VARCHAR(255) NOT NULL,
	`password` VARCHAR(255) NOT NULL,
	`token` VARCHAR(600),
	`created` timestamp,
	`modified` timestamp,
	`last_login` timestamp,
	`is_active` boolean,
	PRIMARY KEY ( id )
);


CREATE TABLE `user_roles` (
	`user_id` BINARY(16) NOT NULL,
	`role_id` BINARY(16) NOT NULL,
	PRIMARY KEY ( user_id, role_id )
);


alter table `users` 
        add constraint UK_user_email unique (email)

alter table  `phones` 
        add constraint FK_phones_userId 
        foreign key (user_id) 
        references users

alter table `user_roles` 
        add constraint FK_user_roles_roleId 
        foreign key (role_id) 
        references roles

alter table `user_roles` 
        add constraint FK_user_roles_userId 
        foreign key (user_id) 
        references users



