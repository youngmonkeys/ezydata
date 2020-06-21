CREATE TABLE IF NOT EXISTS `jpa_example_user` (
	`userId` INT AUTO_INCREMENT,
	`password` VARCHAR(45),
	`fullName` VARCHAR(45),
	`type` VARCHAR(45),
	`email` VARCHAR(45),
	PRIMARY KEY (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;